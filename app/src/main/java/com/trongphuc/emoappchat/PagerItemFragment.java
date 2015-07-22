package com.trongphuc.emoappchat;

/**
 * Created by Phuc on 7/18/15.
 */
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnStickerPagerFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PagerItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PagerItemFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private GridView gridView;
    private EmoAdapter sitckerGridAdapter;
    private int myLastVisiblePos;
    private boolean isGridViewScrolling;

    private OnStickerPagerFragmentInteractionListener mListener;
    private OnChildInteractionListener mChildListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PagerItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PagerItemFragment newInstance() {
        PagerItemFragment fragment = new PagerItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PagerItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sticker_pager_item, container,
                false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        gridView = (GridView) view.findViewById(R.id.gridView1);
        sitckerGridAdapter = new EmoAdapter(getActivity(),
                R.layout.chat_screen_sticker_set_grid_item);
        gridView.setAdapter(sitckerGridAdapter);

        myLastVisiblePos = gridView.getFirstVisiblePosition();
        isGridViewScrolling = false;
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                isGridViewScrolling = true;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int currentFirstVisPos = view.getFirstVisiblePosition();
                if (!isGridViewScrolling) {
                    myLastVisiblePos = currentFirstVisPos;
                    return;
                }
                if (mListener != null) {
                    mChildListener.resetCountUpAnimation();
                }
                if (firstVisibleItem == 0) {
                    if (currentFirstVisPos > myLastVisiblePos) {
                        if (mListener != null) {
                            mChildListener.onScrollDown(true);
                        }
                    }
                    myLastVisiblePos = currentFirstVisPos;
                    return;
                }

                if (currentFirstVisPos > myLastVisiblePos) {
                    if (mListener != null) {
                        mChildListener.onScrollDown(true);
                    }
                }
                if (currentFirstVisPos < myLastVisiblePos) {
                    if (mListener != null) {
                        mChildListener.onScrollDown(false);
                    }
                }
                myLastVisiblePos = currentFirstVisPos;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mChildListener.clickOnItem(i);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnStickerPagerFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        try {
            mChildListener = (OnChildInteractionListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnChildInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStickerPagerFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public interface OnChildInteractionListener {
        public void onScrollDown(boolean isScrollDown);
        public void resetCountUpAnimation();
        public void clickOnItem(int i);
    }
}

