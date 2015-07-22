package com.trongphuc.emoappchat;

/**
 * Created by Phuc on 7/18/15.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<MessageItem> mDataset;
    public boolean isDeleteMsg = false;
    public boolean isSelectAll = false;
    public int mFirstVisiblePosition;
    private static OnClickChatScreenListener mListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private final int mViewType;
        private final ImageView mCheck;
        private ImageView mImgWarning;

        public TextView mTextView;
        public ImageView mAvatar;
        public ImageView mImageView;
        public ImageView mImageViewStatus;
        public RelativeLayout mChatStatusView;
        public Context mContext;

        public ViewHolder(ViewGroup v, int viewType) {
            super(v);
            mContext = v.getContext();
            this.mTextView = (TextView) v.findViewById(R.id.content);
            this.mAvatar = (ImageView) v.findViewById(R.id.avatar);
            this.mImageView = (ImageView) v.findViewById(R.id.img_content);
            this.mImgWarning = (ImageView) v.findViewById(R.id.warning);
            this.mChatStatusView = (RelativeLayout) v.findViewById(R.id.item_chat_status_root);
            this.mImageViewStatus = (ImageView) v.findViewById(R.id.item_chat_status_img);

            if (this.mImgWarning != null) {
                this.mImgWarning.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null)
                            mListener.onErrorMessageClick(v);
                    }
                });
            }
            this.mViewType = viewType;
            this.mCheck = (ImageView) v.findViewById(R.id.check);
            if (mCheck != null) {
                mCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCheck.setSelected(!mCheck.isSelected());
                    }
                });
            }

            if (this.mTextView != null) {
                if (mListener != null) {
                    this.mTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.onMessageClick(v, getAdapterPosition() - 1);
                        }
                    });
                }
                this.mTextView.setOnLongClickListener(this);

                if (MessageItem.isMyseft(this.mViewType)) {

                } else {

                }

            }

            if (this.mImageView != null) {
                if (MessageItem.isMyseft(this.mViewType)) {

                } else {

                }
                this.mImageView.setOnLongClickListener(this);
                if (mListener != null) {
                    this.mImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.onStickerMessageClick(v, getAdapterPosition() - 1);
                        }
                    });
                }
            }
        }

        @Override
        public boolean onLongClick(View view) {
            return true;
        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatAdapter(List<MessageItem> myDataset) {
        mDataset = myDataset;
    }

    public void setOnChatScreenClickListener(OnClickChatScreenListener listener) {
        mListener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v = null;
        switch (viewType) {
            case MessageItem.TYPE_MYSELF:
                // create a new view
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_chat_me, parent, false);
                break;
            case MessageItem.TYPE_FRIEND:
                // create a new view
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_chat_friend, parent, false);
                break;
            case MessageItem.TYPE_HEADER:
                // create a new view
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_chat_header, parent, false);
                break;
            case MessageItem.TYPE_FOOTER:
                // create a new view
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_chat_footer, parent, false);
                break;
            case MessageItem.TYPE_MYSELF_STICKER:
                // create a new view
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_chat_me_sticker, parent, false);
                break;
            case MessageItem.TYPE_FRIEND_STICKER:
                // create a new view
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_chat_friend_sticker, parent, false);
                break;
            case MessageItem.TYPE_DATE:
                // create a new view
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_chat_time, parent, false);
                break;
            default:
                break;
        }

        // set the view's size, margins, paddings and layout parameters
//        ...
        ViewHolder vh = new ViewHolder((ViewGroup) v, viewType);
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return MessageItem.TYPE_HEADER;
        } else if (position == mDataset.size() + 1) {
            return MessageItem.TYPE_FOOTER;
        } else {
            return mDataset.get(position - 1).getMessageType();
        }
    }

    public int getCurrentPosition() {
        return mFirstVisiblePosition;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Log.v("test", "pos: " + (position - 1));
        if (position == 0) {

        } else if (position == mDataset.size() + 1) {
            // Footer
        } else if (mDataset.get(position - 1).getMessageType() == MessageItem.TYPE_DATE) {

        } else {
            if (mDataset.get(position - 1).isMyseft()) {
                View bg = null;
                if (position > 15) {
                } else {
                }
                boolean isSticker = false;
                switch (mDataset.get(position - 1).getMessageType()) {
                    case MessageItem.TYPE_MYSELF_STICKER:
                        bg = holder.mImageView;
                        isSticker = true;
                        break;
                    case MessageItem.TYPE_MYSELF:
                        bg = holder.mTextView;
                        holder.mTextView.setText(mDataset.get(position - 1).getMessageText());

                        if (mDataset.get(position - 1).isWarning())
                            holder.mTextView.setTextColor(holder.mContext.getResources().getColor(R.color.stixchat_text_26_opa));
                        else
                            holder.mTextView.setTextColor(holder.mContext.getResources().getColor(R.color.black));
                        break;
                    default:
                        bg = holder.mTextView;
                        holder.mTextView.setText(mDataset.get(position - 1).getMessageText());
                        break;
                }


                if (!isSticker) {
                    if (position - 1 > 0) {
                        if (mDataset.get(position - 2).isMyseft()) {
                            if (position < mDataset.size()) {
                                if (mDataset.get(position).isMyseft()) {
                                    bg.setBackgroundResource(R.drawable.bubble_white);
                                } else {
                                    bg.setBackgroundResource(R.drawable.bubble_right_white);
                                }
                            } else {
                                bg.setBackgroundResource(R.drawable.bubble_right_white);
                            }
                        } else {
                            bg.setBackgroundResource(R.drawable.bubble_left_white);
                        }
                    } else {
                        bg.setBackgroundResource(R.drawable.bubble_left_white);
                    }

                    int padding = holder.mContext.getResources().getDimensionPixelOffset(R.dimen.stixchat_margin_12);
                    bg.setPadding(padding, padding, padding, padding);
                }

                if (mDataset.get(position - 1).isWarning())
                    holder.mImgWarning.setVisibility(View.VISIBLE);
                else
                    holder.mImgWarning.setVisibility(View.INVISIBLE);
                boolean isVisibleDateHistory = mDataset.get(position - 1).getVisibilityDate();
                boolean isVisibleStatus = mDataset.get(position - 1).getVisibilityStatus();
                if (isVisibleStatus) {
                    holder.mChatStatusView.setVisibility(View.VISIBLE);
                    holder.mImageViewStatus.setVisibility(View.VISIBLE);
                    holder.mImageViewStatus.setVisibility(View.GONE);
                } else if (isVisibleDateHistory) {
                    holder.mChatStatusView.setVisibility(View.VISIBLE);
                    holder.mImageViewStatus.setVisibility(View.GONE);
                } else {
                    holder.mChatStatusView.setVisibility(View.GONE);
                }
            } else {
                View bg = null;
                boolean isSticker = false;
                switch (mDataset.get(position - 1).getMessageType()) {
                    case MessageItem.TYPE_FRIEND_STICKER:
                        bg = holder.mImageView;
                        isSticker = true;
                        break;
                    case MessageItem.TYPE_FRIEND:
                        bg = holder.mTextView;
                        holder.mTextView.setText(mDataset.get(position - 1).getMessageText());
                        break;
                    default:
                        bg = holder.mTextView;
                        holder.mTextView.setText(mDataset.get(position - 1).getMessageText());
                        break;
                }

                if (position - 1 > 0) {
                    if (!mDataset.get(position - 2).isMyseft()) {
                        if (position < mDataset.size()) {
                            if (!mDataset.get(position).isMyseft()) {
                                setBackgroundResource(bg, isSticker, R.drawable.bubble_brown);
                                holder.mAvatar.setVisibility(View.INVISIBLE);
                            } else {
                                setBackgroundResource(bg, isSticker, R.drawable.bubble_right_brown);
                                holder.mAvatar.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            setBackgroundResource(bg, isSticker, R.drawable.bubble_right_brown);
                            holder.mAvatar.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        setBackgroundResource(bg, isSticker, R.drawable.bubble_left_brown);
                        holder.mAvatar.setVisibility(View.VISIBLE);
                    }
                } else {
                    setBackgroundResource(bg, isSticker, R.drawable.bubble_left_brown);
                    holder.mAvatar.setVisibility(View.INVISIBLE);
                }
                boolean isVisibleDateHistory = mDataset.get(position - 1).getVisibilityDate();
                if (isVisibleDateHistory) {
                    holder.mChatStatusView.setVisibility(View.VISIBLE);
                } else {
                    holder.mChatStatusView.setVisibility(View.GONE);
                }
            }

            holder.mCheck.setSelected(isSelectAll);
            if (isDeleteMsg) {
                holder.mCheck.setVisibility(View.VISIBLE);
            } else {
                holder.mCheck.setVisibility(View.GONE);
            }


        }
    }

    private void setBackgroundResource(View v, boolean isSticker, int idResource) {
        if (!isSticker)
            v.setBackgroundResource(idResource);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size() + 2;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    public interface OnClickChatScreenListener {
        void onErrorMessageClick(View view);

        void onMessageClick(View view, int position);

        void onStickerMessageClick(View view, int position);
    }
}