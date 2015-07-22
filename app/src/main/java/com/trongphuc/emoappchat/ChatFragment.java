package com.trongphuc.emoappchat;

/**
 * Created by Phuc on 7/18/15.
 */

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnItemTouchListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the
 * interface.
 */
public class ChatFragment extends Fragment implements AbsListView.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private ChatAdapter mChatAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<MessageItem> arrayList = new ArrayList<>();
    private boolean mNoConversation;
    private boolean mIsClearNoConversation;
    private int mPreviousPositionItemClick = -1;

    // TODO: Rename and change types of parameters
    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ChatFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        int duration = 1000;
        ScrollingHandler mLayoutManager = new ScrollingHandler(getActivity(), LinearLayoutManager.VERTICAL, false, duration);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)

        mNoConversation = false;
        for (int i = 0; i < 20; i++) {
            MessageItem item = new MessageItem();
            if (i == 10 || i == 17) {
                item.setMessageType(MessageItem.TYPE_DATE);
            } else if (i % 3 == 0) {
                item.setMessageType(MessageItem.TYPE_MYSELF);
                item.setMessageText("String abc  abc long long long long  abc long long long long  abc long long long long  abc long long long long " + i);
            } else {
                item.setMessageType(MessageItem.TYPE_FRIEND);
                item.setMessageText("UUU String abc  abc long long long long  abc long long long long  abc long long long long  abc long long long long  abc long long long long  abc long long long long " + i);
            }
            arrayList.add(item);
        }

        mChatAdapter = new ChatAdapter(arrayList);

        mChatAdapter.setOnChatScreenClickListener(new ChatAdapter.OnClickChatScreenListener() {
            @Override
            public void onErrorMessageClick(View view) {
            }

            @Override
            public void onMessageClick(View view, int position) {
                invisiblePreviousMessageStatus(position);
                MessageItem item = arrayList.get(position);
                if (item != null) {
                    boolean isSet = item.getVisibilityDate();
                    item.setVisibilityDate(!isSet);
                    mChatAdapter.notifyDataSetChanged();
                }
                mPreviousPositionItemClick = position;
            }

            @Override
            public void onStickerMessageClick(View view, int position) {
                invisiblePreviousMessageStatus(position);
                MessageItem item = arrayList.get(position);
                if (item != null) {
                    boolean isSet = item.getVisibilityDate();
                    item.setVisibilityDate(!isSet);
                    mChatAdapter.notifyDataSetChanged();
                }
                mPreviousPositionItemClick = position;
            }
        });


        mRecyclerView.setAdapter(mChatAdapter);
        mRecyclerView.scrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
        mRecyclerView.addOnItemTouchListener(new OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange1, R.color.green1, R.color.blue1);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setupAdapter();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void invisiblePreviousMessageStatus(int currentPos) {
        if (currentPos != mPreviousPositionItemClick &&
                mPreviousPositionItemClick != -1 && arrayList.size() > mPreviousPositionItemClick) {
            MessageItem previousItem = arrayList.get(mPreviousPositionItemClick);
            if (previousItem != null)
                previousItem.setVisibilityDate(false);
        }
    }

    public void setupAdapter() {
        if (!mNoConversation) {
            for (int i = 0; i < 2; i++) {
                MessageItem item = new MessageItem();
                if (i % 2 == 0) {
                    item.setMessageType(MessageItem.TYPE_MYSELF);
                    item.setMessageText("String abc long long long long  abc long long long long  abc long long long long  abc long long long long " + i);
                } else {
                    item.setMessageType(MessageItem.TYPE_FRIEND);
                    item.setMessageText("UUU String abc  abc long long long long  abc long long long long  abc long long long long  abc long long long long  abc long long long long  abc long long long long " + i);
                }
                arrayList.add(item);
            }

            mChatAdapter.notifyDataSetChanged();
        }
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount());
            }
        }, 100);


        //Sticky Header
        // final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
        //mRecyclerView.addItemDecoration(headersDecor);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void sendMessage(String message) {
        if (mNoConversation && !mIsClearNoConversation) {
            mIsClearNoConversation = true;
            arrayList.clear();
        }

        if (arrayList.size() > 0) {
            arrayList.get(arrayList.size() - 1).setVisibilityStatus(false);
        }

        MessageItem item = new MessageItem();
        item.setMessageType(MessageItem.TYPE_MYSELF);
        item.setMessageText(message);
        item.setIsWarning(true);
        item.setVisibilityStatus(true);
        arrayList.add(item);

        mChatAdapter.notifyDataSetChanged();
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount());
            }
        }, 100);
    }

    public void sendMessage(boolean isSticker, boolean isMe) {
        if (mNoConversation && !mIsClearNoConversation) {
            mIsClearNoConversation = true;
            arrayList.clear();
        }

        if (arrayList.size() > 0) {
            arrayList.get(arrayList.size() - 1).setVisibilityStatus(false);
        }

        MessageItem item = new MessageItem();
        if (isSticker && isMe) {
            item.setIsWarning(true);
            item.setMessageType(MessageItem.TYPE_MYSELF_STICKER);
        } else {
            if (isSticker) {
                item.setMessageType(MessageItem.TYPE_FRIEND_STICKER);
            }
        }
        item.setVisibilityStatus(true);
        arrayList.add(item);

        mChatAdapter.notifyDataSetChanged();
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount());
            }
        }, 100);
    }

    public void scrollToLast() {
        mRecyclerView.scrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
    }
}

