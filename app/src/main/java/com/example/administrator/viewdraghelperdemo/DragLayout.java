package com.example.administrator.viewdraghelperdemo;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/3/25.
 */

public class DragLayout extends FrameLayout {

    private static final String TAG = "DragLayout";
    private ViewDragHelper mViewDragHelper;
    private View mCover;
    private View mBar;
    private ImageView mImageCover;
    private int mCoverImageInitX;
    private int mCoverImageInitY;

    public DragLayout(Context context) {
       this(context,null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }


    private void init() {

        mViewDragHelper = ViewDragHelper.create(this,mCallback);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mCoverImageInitX =  mImageCover.getLeft();
                mCoverImageInitY =  mImageCover.getTop();
                Log.d(TAG, "onGlobalLayout: "+mCoverImageInitX+":::"+mCoverImageInitY);
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

    }

    @Override
    protected void onFinishInflate() {
        mCover = findViewById(R.id.cover);
        mBar =  findViewById(R.id.bottom_bar);
        mImageCover = (ImageView) findViewById(R.id.image_cover);

    }

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {

      /*  是否拖拽孩子*/
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mCover;
        }

        /**
         * 竖直方向拖动孩子时孩子的顶边位置
         * @param child
         * @param top
         * @param dy
         * @return
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {

            int max = getHeight()-mBar.getHeight();
           /* if(top<=0){
                return 0;
            }else if(top>=max) {
                return max;
            }

            return top;*/

            return Math.min(Math.max(0, top), max);
        }

        /**
         *  当位置发生变化的回调
         * @param changedView
         * @param left
         * @param top
         * @param dx
         * @param dy
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            //底部bar动画的设置
            int max = getHeight()-mBar.getHeight();
            float rate = top*1.0f/max;
            mBar.setTranslationY(-top);//白色孩子走多远，bar走多远
            mBar.setScaleX(1-rate*2/5);
            mBar.setScaleY(1-rate*2/5);
            mBar.setPivotX(mBar.getWidth());
            //


        }

        /**
         * 松开位置时候的回调
         * @param releasedChild
         * @param xvel
         * @param yvel
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {

        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }
}
