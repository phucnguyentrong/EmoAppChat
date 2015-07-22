package com.trongphuc.emoappchat;

/**
 * Created by Phuc on 7/18/15.
 */

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

public class ScrollingHandler extends LinearLayoutManager {
    private final int duration;

    public ScrollingHandler(Context context, int orientation, boolean reverseLayout, int duration) {
        super(context, orientation, reverseLayout);
        this.duration = duration;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
                                       int position) {
        int maxHeight = 0;
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            int h = recyclerView.getChildAt(i).getHeight();
            if (maxHeight < h) {
                maxHeight = h;
            }
        }
//        View firstVisibleChild = recyclerView.getChildAt(0);
//        int itemHeight = firstVisibleChild.getHeight();
//        int currentPosition = recyclerView.getChildPosition(firstVisibleChild);
        int currentPosition = findFirstVisibleItemPosition();
        int distanceInPixels = Math.abs((currentPosition - position) * maxHeight);
        if (distanceInPixels == 0) {
            distanceInPixels = (int) Math.abs(recyclerView.getChildAt(0).getY());
        }
        SmoothScroller smoothScroller = new SmoothScroller(recyclerView.getContext(), distanceInPixels, duration);
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    private class SmoothScroller extends LinearSmoothScroller {
        private static final int TARGET_SEEK_SCROLL_DISTANCE_PX = 2000;
        private static final float MILLISECONDS_PER_INCH = 400f;
        private final float distanceInPixels;
        private final float duration;

        public SmoothScroller(Context context, int distanceInPixels, int duration) {
            super(context);
            this.distanceInPixels = distanceInPixels;
            float millisPerPx = calculateSpeedPerPixel(context.getResources().getDisplayMetrics());
            this.duration = distanceInPixels < TARGET_SEEK_SCROLL_DISTANCE_PX ? (int) (Math.abs(distanceInPixels) * millisPerPx) : duration;
        }

        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            return ScrollingHandler.this.computeScrollVectorForPosition(targetPosition);
        }

        @Override
        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
        }

        @Override
        protected int calculateTimeForScrolling(int dx) {
            float proportion = (float) dx / distanceInPixels;
            return (int) (duration * proportion);
        }
    }
}
