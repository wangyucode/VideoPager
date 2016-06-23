package com.ourslook.vpdemo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.util.Util;
import com.ourslook.videopager.VideoPager;
import com.ourslook.videopager.player.DemoPlayer;
import com.ourslook.videopager.player.ExtractorRendererBuilder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PagerActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    public static final String TAG = "PagerActivity";
    public static final String URLS_EXTRA = "URLS_EXTRA";


    VideoPager videoPager;
    List<DemoPlayer> players = new ArrayList<>();
    List<String> urls;

    int currentItemPosition;

    private long playerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        videoPager = (VideoPager) findViewById(R.id.video_pager);
        videoPager.addOnPageChangeListener(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        releasePlayer();
        playerPosition = 0;
        setIntent(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            onShown();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23) {
            onShown();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
//        audioCapabilitiesReceiver.unregister();
        releasePlayer();
    }

    private void onShown() {
        Intent intent = getIntent();
        urls = intent.getStringArrayListExtra(URLS_EXTRA);
//        configureSubtitleView();
        videoPager.setVideos(urls);
        if (players.size() == 0) {
            initPlayer();
        } else {
            DemoPlayer player = players.get(currentItemPosition);
            player.setBackgrounded(false);
        }
    }

    private void initPlayer() {
        if (players.size() == 0)
            for (int i = 0; i < urls.size(); i++) {
                DemoPlayer player = preparePlayer(i, urls.get(i));
                players.add(player);
            }
    }

    private DemoPlayer preparePlayer(int position, String url) {
        String userAgent = Util.getUserAgent(this, "VPPlayerDemo");
        DemoPlayer.RendererBuilder render = new ExtractorRendererBuilder(this, userAgent, Uri.parse(url));
        DemoPlayer player = new DemoPlayer(render);

        PlayerListenerWithPosition listener = new PlayerListenerWithPosition(position);
        player.addListener(listener);
//            player.setCaptionListener(this);
//            player.setMetadataListener(this);
        player.seekTo(playerPosition);
//            playerNeedsPrepare = true;
//            mediaController.setMediaPlayer(player.getPlayerControl());
//            mediaController.setEnabled(true);
////            eventLogger = new EventLogger();
////            eventLogger.startSession();
////            player.addListener(eventLogger);
////            player.setInfoListener(eventLogger);
////            player.setInternalErrorListener(eventLogger);
////            debugViewHelper = new DebugTextViewHelper(player, debugTextView);
////            debugViewHelper.start();
//        }
//        if (playerNeedsPrepare) {
        player.prepare();
//            playerNeedsPrepare = false;
////            updateButtonVisibilities();
//        }
        SurfaceView surfaceView = (SurfaceView) videoPager.views.get(position).findViewById(R.id.surface_view);
        surfaceView.getHolder().addCallback(new SurfaceCallbackWithPosition(position));
        player.setSurface(surfaceView.getHolder().getSurface());
        player.setPlayWhenReady(position == currentItemPosition);
        return player;
    }

    private void releasePlayer() {
        if (players != null && players.size() == urls.size()) {

            playerPosition = players.get(currentItemPosition).getCurrentPosition();

            for (DemoPlayer player : players) {
                if (player != null) {

                    player.release();
                    player = null;
                }
            }
            players.clear();
        }
    }

    //pagerChangeListener
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        currentItemPosition = position;
        stopOtherPlay();
        startCurrentPlay();
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void stopOtherPlay() {
        for (int i = 0; i < players.size(); i++) {
            if (currentItemPosition != i) {
                if (players.get(i).getPlayerControl().isPlaying()) {
                    playerPosition = players.get(i).getCurrentPosition();
                    players.get(i).getPlayerControl().pause();
                }
            }
        }
    }

    private void startCurrentPlay() {
        if (players.size() > 0) {
            players.get(currentItemPosition).seekTo(playerPosition);
            players.get(currentItemPosition).getPlayerControl().start();
        }
    }


    class PlayerListenerWithPosition implements DemoPlayer.Listener {
        int position;

        public PlayerListenerWithPosition(int position) {
            this.position = position;
        }

        @Override
        public void onStateChanged(boolean playWhenReady, int playbackState) {
            TextView loadingText = (TextView) videoPager.views.get(position).findViewById(R.id.tv_loading);
            String text = "position=" + position + ", playWhenReady=" + playWhenReady + ", playbackState=";
            switch (playbackState) {
                case ExoPlayer.STATE_BUFFERING:
                    text += "buffering";
                    loadingText.setVisibility(View.VISIBLE);
                    break;
                case ExoPlayer.STATE_ENDED:
                    text += "ended";
                    break;
                case ExoPlayer.STATE_IDLE:
                    text += "idle";
                    break;
                case ExoPlayer.STATE_PREPARING:
                    text += "preparing";
                    loadingText.setVisibility(View.VISIBLE);
                    break;
                case ExoPlayer.STATE_READY:
                    text += "ready";
                    loadingText.setVisibility(View.GONE);
                    break;
                default:
                    text += "unknown";
                    loadingText.setVisibility(View.GONE);
                    break;
            }
            Log.d(TAG, "onStateChanged: " + text);
        }

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            AspectRatioFrameLayout videoFrame = (AspectRatioFrameLayout) videoPager.views.get(position).findViewById(R.id.video_frame);
            videoFrame.setAspectRatio(
                    height == 0 ? 1 : (width * pixelWidthHeightRatio) / height);
        }
    }

    class SurfaceCallbackWithPosition implements SurfaceHolder.Callback {
        int position;

        public SurfaceCallbackWithPosition(int position) {
            this.position = position;
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (players.get(position) != null) {
                players.get(position).setSurface(holder.getSurface());
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.d(TAG, "surfaceDestroyed: ");
            if (players.size() > 0 && players.get(position) != null) {
                players.get(position).blockingClearSurface();
            }
        }
    }
}
