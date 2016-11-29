package com.example.jakubkalinowski.contractfoxandroid.helper_classes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.jakubkalinowski.contractfoxandroid.Navigation_Fragments.ContractorScheduleFragment;

/**
 * Created by MD on 11/13/2016.
 */

public class RecyclerViewItemTouchHelper implements RecyclerView.OnItemTouchListener {

    public interface OnItemClickListener{
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    private GestureDetector mGestureDetector;
    private OnItemClickListener mOnItemClickListener;


    public RecyclerViewItemTouchHelper(Context context,
                                       OnItemClickListener listener, final RecyclerView rv){
        mOnItemClickListener = listener;
        mGestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        View child = rv.findChildViewUnder(e.getX(), e.getY());
                        if (child != null && mOnItemClickListener != null) {
                            mOnItemClickListener.onLongClick(child, rv.getChildPosition(child));
                        }
                    }
                });
    }


    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mOnItemClickListener != null && mGestureDetector.onTouchEvent(e)) {
            mOnItemClickListener.onClick(childView, rv.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
