package com.trongphuc.emoappchat;

/**
 * Created by Phuc on 7/18/15.
 */

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class PagerFragment extends Fragment implements PagerItemFragment.OnChildInteractionListener {

    private OnClickStickerListener mListener;
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;

    private Animation mAnimationOut;
    private Animation mAnimationIn;
    private View horizontalBar;
    private boolean isAnimating = false;

    private Timer timer;
    protected int increate = 0;
    private static final long TIMER_PERIOD = 500;
    private static final long TIMER_DELAY = 0;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PagerFragment newInstance() {
        PagerFragment fragment = new PagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sticker_pager, container, false);
    }


    // BEGIN_INCLUDE (fragment_onviewcreated)

    /**
     * This is called after the {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has finished.
     * Here we can pick out the {@link View}s we need to configure from the content view.
     * <p/>
     * We set the {@link ViewPager}'s adapter to be an instance of {@link ChatStixAdapter}. The
     * {@link SlidingTabLayout} is then given the {@link ViewPager} so that it can populate itself.
     *
     * @param view View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.vpPager);
        ChatStixAdapter pageAdapter = new ChatStixAdapter(getChildFragmentManager());
        mViewPager.setAdapter(pageAdapter);
        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.horizontal_item, R.id.content);
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onScrollDown(false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // END_INCLUDE (setup_slidingtablayout)

        setupHorizontalBarAnimation(view);


        ImageView btnShop = (ImageView) view.findViewById(R.id.btnRight);
        btnShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Will Open StickerShop", Toast.LENGTH_LONG).show();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
    // END_INCLUDE (fragment_onviewcreated)

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onClickSticker(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnClickStickerListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
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
    public interface OnClickStickerListener {
        // TODO: Update argument type and name
        public void onClickSticker(Uri uri);
    }

    private void setupHorizontalBarAnimation(View v) {
        horizontalBar = v.findViewById(R.id.scroll);
        mAnimationIn = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_in_bottom_up);
        mAnimationOut = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_out_top_down);
        mAnimationIn.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                horizontalBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimating = false;
                deleteTimer();
            }
        });

        mAnimationOut.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                horizontalBar.setVisibility(View.INVISIBLE);
                isAnimating = false;
                // Start timer
                setupTimer();
            }
        });

    }

    private void deleteTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    /**
     * Setup timer for bottom bar animation
     */
    private void setupTimer() {
        if (horizontalBar.getVisibility() != View.VISIBLE) {
            deleteTimer();
            timer = new Timer();
            // Create timer task for bottom bar
            TimerTask myTimerTask = new TimerTask() {

                @Override
                public void run() {
                    if (increate >= 3) {
                        onScrollDown(false);
                        increate = 0;
                    } else {
                        increate++;
                    }
                }
            };
            timer.schedule(myTimerTask, TIMER_DELAY, TIMER_PERIOD);
        }
    }

    @Override
    public void onScrollDown(boolean isScrollDown) {
        if (isAnimating)
            return;
        if (isScrollDown) {
            if (horizontalBar.getVisibility() == View.VISIBLE) {
                isAnimating = true;
                Handler refresh = new Handler(Looper.getMainLooper());
                refresh.postDelayed(new Runnable() {
                    public void run() {
                        horizontalBar.startAnimation(mAnimationOut);
                    }
                }, 75);
            }
        } else {
            if (horizontalBar.getVisibility() != View.VISIBLE) {
                isAnimating = true;
                Handler refresh = new Handler(Looper.getMainLooper());
                refresh.postDelayed(new Runnable() {
                    public void run() {
                        horizontalBar.startAnimation(mAnimationIn);
                    }
                }, 75);
            }
        }
    }

    @Override
    public void resetCountUpAnimation() {
        increate = 0;
    }

    @Override
    public void clickOnItem(int i) {
        if (mListener != null) {
            mListener.onClickSticker(null);
        }
    }

}
