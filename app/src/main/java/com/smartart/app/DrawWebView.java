package com.smartart.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class DrawWebView extends WebView {
    private GestureDetector mDetector;
    private float mInitialScale;
    private int mX;
    private int mY;
    Context mainContext;

    public DrawWebView(Context context) {
        super(context);
        mainContext = context;
        init();
    }

    public DrawWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        //loadUrl("file://" + Environment.getExternalStorageDirectory() + "/Pictures/boxes.jpg");
        setWebViewClient(new WebViewClient());
        getSettings().setBuiltInZoomControls(true);
        getSettings().setDisplayZoomControls(false);
        getSettings().setSupportZoom(true);
        getSettings().setUseWideViewPort(true);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        mDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {


            @Override
            public boolean onDown(MotionEvent e) {
                Toast.makeText(mainContext, "onDown", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.d("myLog", "x: "+e1.getX());
                Log.d("myLog", "y: "+e1.getY());
                Log.d("myLog", "distanceX: "+distanceX);
                Log.d("myLog", "distanceY: "+distanceY);
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                mX = (int) e.getX() + getScrollX();
                mY = (int) e.getY() + getScrollY();
                mInitialScale = getScale();
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(mainContext, "onTouch", Toast.LENGTH_SHORT).show();
                return mDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw (canvas);

        float scale = getScale() / mInitialScale;

        Paint p = new Paint();

        p.setColor(Color.RED);
        canvas.drawCircle(mX * scale, mY * scale, 10, p);

        p.setColor(Color.GREEN);
        canvas.drawCircle(mX, mY, 5, p);
        Toast.makeText(mainContext, "onDraw", Toast.LENGTH_SHORT).show();
    }
}