package com.ourslook.videopager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.ourslook.vpvideoview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyu on 16/6/13.
 */
public class VideoPager extends ViewPager {

    public List<View> views;

    public VideoPager(Context context) {
        super(context);
        initView();
    }

    public VideoPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        views = new ArrayList<>();
    }

    public void setVideos(List<String> urls){
        for(int i = 0;i<urls.size();i++){
            View v = LayoutInflater.from(getContext()).inflate(R.layout.view_pager,null);
            v.setTag(i);
            views.add(v);
        }
        setOffscreenPageLimit(urls.size());
        VideoPagerAdapter adapter = new VideoPagerAdapter(urls,getContext(),views);
        this.setAdapter(adapter);
    }
}
