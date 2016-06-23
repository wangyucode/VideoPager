package com.ourslook.vpdemo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.exoplayer.util.Util;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button button1,button2,button3,button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);


        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent mpdIntent = new Intent(this, PlayActivity.class);
        Intent pagerIntent = new Intent(this,PagerActivity.class);
        Samples.Sample sample = null;
        switch (v.getId()){
            case R.id.button1:
                /*************数据*************/
                sample = new Samples.Sample("Dizzy", "http://wycode.cn/upload/movie/no9.mp4", Util.TYPE_OTHER);
                mpdIntent.setData(Uri.parse(sample.uri))
                        .putExtra(PlayActivity.CONTENT_ID_EXTRA, sample.contentId)
                        .putExtra(PlayActivity.CONTENT_TYPE_EXTRA, sample.type)
                        .putExtra(PlayActivity.PROVIDER_EXTRA, sample.provider);
                startActivity(mpdIntent);
                break;

            case R.id.button2:
                /*************数据*************/
                sample = new Samples.Sample("Day By Day", "asset:///feel.mp4", Util.TYPE_OTHER);
                mpdIntent.setData(Uri.parse(sample.uri))
                        .putExtra(PlayActivity.CONTENT_ID_EXTRA, sample.contentId)
                        .putExtra(PlayActivity.CONTENT_TYPE_EXTRA, sample.type)
                        .putExtra(PlayActivity.PROVIDER_EXTRA, sample.provider);
                startActivity(mpdIntent);
                break;

            case R.id.button3:
                ArrayList<String> urls = new ArrayList<>();
                urls.add("asset:///feel.mp4");
                urls.add("asset:///love_the_way_you_lie.mp4");
                urls.add("asset:///day_by_day.mp4");
                urls.add("asset:///no9.mp4");
                pagerIntent.putStringArrayListExtra(PagerActivity.URLS_EXTRA,urls);
                startActivity(pagerIntent);
                break;

            case R.id.button4:

                ArrayList<String> urls1 = new ArrayList<>();
                urls1.add("http://114.215.84.189:9000/video/love.mp4");
                urls1.add("http://114.215.84.189:9000/video/tell.mp4");
                urls1.add("http://114.215.84.189:9000/video/away.mp4");
                urls1.add("http://114.215.84.189:9000/video/love.mp4");
                pagerIntent.putStringArrayListExtra(PagerActivity.URLS_EXTRA,urls1);
                startActivity(pagerIntent);
                break;
        }
    }
}
