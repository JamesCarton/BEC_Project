package com.rainbowloveapp.app.utils;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 *  on 8/6/17.
 */

public class MyTouchListener implements View.OnTouchListener {

    private int _xDelta;
    private int _yDelta;

    private float initialTouchX;
    private float initialTouchY;

    private final float SCROLL_THRESHOLD = 5;

    boolean isMoved;

    float dX, dY;

    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                /*FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;*/

                initialTouchX = event.getX();
                initialTouchY = event.getY();

                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();

                isMoved = false;
                break;
            case MotionEvent.ACTION_UP:
                if(!isMoved){
                    view.performClick();
                    return false;// to handle Click
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                /*if((Math.abs(initialTouchX - event.getX()) > SCROLL_THRESHOLD || Math.abs(initialTouchY - event.getY()) > SCROLL_THRESHOLD)) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                    layoutParams.leftMargin = X - _xDelta;
                    layoutParams.topMargin = Y - _yDelta;
                    layoutParams.rightMargin = -800;
                    layoutParams.bottomMargin = -100;
                    view.setLayoutParams(layoutParams);
                    isMoved = true;
                    Log.v("child...", "..." + view.getId());
                }*/
                if((Math.abs(initialTouchX - event.getX()) > SCROLL_THRESHOLD || Math.abs(initialTouchY - event.getY()) > SCROLL_THRESHOLD)){
                    view.animate()
                            .x(event.getRawX() + dX)
                            .y(event.getRawY() + dY)
                            .setDuration(0)
                            .start();
                    isMoved = true;
                }

                break;
        }
        view.invalidate();
        return true;

    }

}
