package com.ourslook.videopager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ourslook.vpvideoview.R;

import java.util.List;

/**
 * Created by wangyu on 16/6/13.
 */
public class VideoPagerAdapter extends PagerAdapter {

    List<String> videoUrls;
    Context context;
    List<View> views;

    public VideoPagerAdapter(List<String> videoUrls, Context context, List<View> views) {
        this.videoUrls = videoUrls;
        this.context = context;
        this.views = views;
    }

    @Override
    public int getCount() {
        return videoUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v= views.get(position);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
