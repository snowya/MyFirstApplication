package com.jnu.student.myfirstapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder holder;
    DrawThread drawThread;

    ArrayList<Sprite> sprites = new ArrayList<Sprite>();
    int myScore;
    float touchX=-1,touchY=-1;
    Bitmap ghost=BitmapFactory.decodeResource(getResources(),R.drawable.ghost);

    public GameView(Context context) {
        super(context);
        holder = this.getHolder();
        holder.addCallback(this);
        setBackgroundResource(R.drawable.bg);
        setZOrderOnTop(true); // 使surfaceview放到最顶层
        getHolder().setFormat(PixelFormat.TRANSLUCENT);// 使窗口支持透明度

        sprites.add(new Sprite());
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchX = event.getX();
                touchY = event.getY();
                return true;
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread();
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(null!=drawThread) {
            drawThread.stopThread();
        }
    }

    public Bitmap changeBitmapSize (Bitmap bitmap, float x, float y) {
        int src_w = bitmap.getWidth();
        int src_h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scale_w = x / src_w;
        float scale_h = x / src_h;
        matrix.postScale(scale_w, scale_h);
        return Bitmap.createBitmap(bitmap,0,0,src_w,src_h,matrix,true);
    }

    private class DrawThread extends Thread {
        private Boolean alive=true;
        @Override
        public void run() {
            while(alive) {
                try {
                    Canvas canvas = holder.lockCanvas();
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); // 绘制透明色
                    Paint p = new Paint();
                    p.setTextSize(100);
                    p.setColor(Color.WHITE);

                    for (Sprite sprite: sprites) {
                        if(!sprite.getBeat() && touchX>=sprite.getX() && touchX<=sprite.getX()+sprite.getMouse().getWidth() && touchY>=sprite.getY() && touchY<=sprite.getY()+sprite.getMouse().getHeight()) {
                            sprite.setBeat(true);
                            myScore++;
                            touchX=-1;
                            touchY=-1;
                            ghost=changeBitmapSize(ghost,(float) 0.1*GameView.this.getWidth(),(float) 0.2*GameView.this.getHeight());
                        }
                        if(sprite.getBeat()) {
                            canvas.drawBitmap(ghost,sprite.getX(),sprite.getY()-25,p);
                        }
                        sprite.move();
                    }

                    for (Sprite sprite: sprites) {

                        canvas.drawBitmap(sprite.getMouse(),sprite.getX(),sprite.getY(),p);
                    }

                    canvas.drawText(myScore+"", getWidth()/23*11, 120, p);
                    holder.unlockCanvasAndPost(canvas);
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stopThread() {
            alive=false;
        }
    }

    private class Sprite {
        private ArrayList<Bitmap> mouses = new ArrayList<Bitmap>();
        private double[] Xs = {0.025,0.27,0.515,0.763};
        private double[] Ys = {0.1,0.32,0.526,0.736};
        private int distance=200,change=20;
        private float X,Y;
        private Bitmap mouse;
        private Boolean top=true;

        public Boolean getBeat() {
            return beat;
        }

        public void setBeat(Boolean beat) {
            this.beat = beat;
        }

        private Boolean beat=false;

        Sprite () {
            mouses.add(BitmapFactory.decodeResource(getResources(), R.drawable.mouse1));
            mouses.add(BitmapFactory.decodeResource(getResources(), R.drawable.mouse2));
            mouses.add(BitmapFactory.decodeResource(getResources(), R.drawable.mouse3));
            mouses.add(BitmapFactory.decodeResource(getResources(), R.drawable.mouse4));
            mouse=mouses.get((int)(Math.random()*mouses.size()));
        }

        public Bitmap getMouse () {
            return getMouseBitmap(mouse);
        }

        public float getX () {
            return X;
        }

        public float getY () {
            return Y+distance;
        }

        public Bitmap getMouseBitmap(Bitmap bitmap) {
            int src_w = bitmap.getWidth();
            int src_h = bitmap.getHeight();
            float x = GameView.this.getWidth();
            float scale_w = ((float) 0.21*GameView.this.getWidth()) / src_w;
            float scale_h = ((float) 0.2*GameView.this.getWidth()) / src_h;
            src_h-=distance/scale_h;
            Matrix matrix = new Matrix();
            matrix.postScale(scale_w, scale_h);
            return Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix,true);
        }

        public void move () {
            if(distance-change<=Math.random()*20) top=true;
            if(!top)
                distance-=change;
            else
                distance+=change;
            if(top && distance>=200) init();
        }

        public  void init() {
            distance=(int)((Math.random()+1)*100);
            change = (int)((Math.random()+1)*30);
            mouse=mouses.get((int)(Math.random()*mouses.size()));
            X=(float) Xs[(int)(Math.random()*Xs.length)]*GameView.this.getWidth();
            Y=(float) Ys[(int)(Math.random()*Xs.length)]*GameView.this.getHeight();
            top=false;
            beat=false;
        }
    }
}
