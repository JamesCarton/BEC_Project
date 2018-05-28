package com.rainbowloveapp.app.utils;

import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 *  on 7/6/17.
 */



public class MyDragListener implements View.OnDragListener {
    //Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
    //Drawable normalShape = getResources().getDrawable(R.drawable.shape);


    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                v.setVisibility(View.INVISIBLE);
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                //view.setBackgroundDrawable(enterShape);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                //view.setBackgroundDrawable(normalShape);
                break;
            case DragEvent.ACTION_DROP:
                // Dropped, reassign View to ViewGroup
                View view = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) view.getParent();
                owner.removeView(view);
                FrameLayout container = (FrameLayout) v;
                container.addView(view);
                view.setVisibility(View.VISIBLE);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                //v.setBackgroundDrawable(normalShape);
            default:
                break;
        }
        return true;
    }
}

