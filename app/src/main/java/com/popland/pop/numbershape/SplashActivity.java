package com.popland.pop.numbershape;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
TextView TVnumber;
ImageView IVshape;
RelativeLayout RLlink;
    SQLite sqlite;
    public static ArrayList<byte[]> ImageArray;
MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        RLlink = (RelativeLayout)findViewById(R.id.RLlink);
        TVnumber = (TextView)findViewById(R.id.TVnumber);
        IVshape = (ImageView)findViewById(R.id.IVshape);

        Line line = new Line(this);
        RLlink.addView(line);

        sqlite = new SQLite(this);
        sqlite.check();
        ImageArray = sqlite.DBtoArrayList();

        mp = MediaPlayer.create(this,R.raw.nhacnen);
        mp.start();
        mp.setLooping(true);
    }

    float startX, startY, lastX, lastY;
    boolean drawingLock1 = false;
    class Line extends View {
        Paint paint = new Paint();

        public Line(Context context) {
            super(context);
            paint.setStrokeWidth(20);
            paint.setColor(Color.parseColor("#ff00dd"));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawLine(startX, startY, lastX, lastY, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch(event.getAction()){
                case MotionEvent.ACTION_MOVE:
                    if(event.getX()>=TVnumber.getLeft() && event.getX()<=TVnumber.getRight()
                            && event.getY()>=TVnumber.getTop() && event.getY()<=TVnumber.getBottom()) {
                        startX = (TVnumber.getLeft() + TVnumber.getRight()) / 2;
                        startY = (TVnumber.getTop() + TVnumber.getBottom()) / 2;
                        lastX = startX;
                        lastY = startY;
                        drawingLock1 = true;
                    }
                    if(drawingLock1) {
                        lastX = event.getX();
                        lastY = event.getY();
                        if(lastX>=IVshape.getLeft() && lastX<=IVshape.getRight()
                                && lastY>=IVshape.getTop() && lastY<=IVshape.getBottom()){
                            MediaPlayer media = MediaPlayer.create(SplashActivity.this,R.raw.right);
                            media.start();
                                Intent i = new Intent(SplashActivity.this,PlayActivity.class);
                                startActivity(i);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    drawingLock1 = false;
                    startX = startY = lastX = lastY = 0;
                    break;
            }
            invalidate();
            return true;
        }
    }
}
