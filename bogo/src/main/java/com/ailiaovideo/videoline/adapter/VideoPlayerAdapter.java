package com.ailiaovideo.videoline.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ailiaovideo.videoline.fragment.CuckooVideoPlayerFragment;
import com.ailiaovideo.videoline.json.jsonmodle.VideoModel;

import java.util.List;

public class VideoPlayerAdapter extends FragmentPagerAdapter {

    private List<VideoModel> list;
    private List<CuckooVideoPlayerFragment> mViewSparseArray;

    public VideoPlayerAdapter(FragmentManager fm, List<VideoModel> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        Bundle bundle = new Bundle();
        bundle.putParcelable(CuckooVideoPlayerFragment.VIDEO_DATA,list.get(position));
        CuckooVideoPlayerFragment shortVideoPlayerFragment = new CuckooVideoPlayerFragment();
        shortVideoPlayerFragment.setArguments(bundle);
        return shortVideoPlayerFragment;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return list.size();
    }

}