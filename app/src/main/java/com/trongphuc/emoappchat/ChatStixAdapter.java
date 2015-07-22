package com.trongphuc.emoappchat;

/**
 * Created by Phuc on 7/18/15.
 */

import android.support.v4.app.FragmentManager;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ChatStixAdapter extends FragmentStatePagerAdapter {

//    private StickerClickListener mListener;

    public ChatStixAdapter(FragmentManager fm) {
        super(fm);
//        mListener = stickerClickListener;
    }

    @Override
    public Fragment getItem(int position) {
        return PagerItemFragment.newInstance();

    }

    // @Override
    // public void destroyItem(ViewGroup container, int position, Object object)
    // {
    // if (position >= getCount()) {
    // FragmentManager manager = ((Fragment) object).getFragmentManager();
    // FragmentTransaction trans = manager.beginTransaction();
    // trans.remove((Fragment) object);
    // trans.commit();
    // }
    // }

    @Override
    public int getCount() {
        return 20;
    }

    // @Override
    // public int getItemPosition(Object object) {
    // return POSITION_NONE;
    // }

    // http://stackoverflow.com/questions/10849552/update-viewpager-dynamically/17855730#17855730

    /**
     * Update/change data when notify data set change
     */

    public String getPageTitle(int i) {
        return "item " + (i + 1);
    }

    public interface StickerClickListener {

        public void onStickerClick(int page, int position);

    }
}
