package com.mixiaoxiao.fastscroll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.mixiaoxiao.fastscroll.FastScrollDelegate.FastScrollable;

/**
 * https://github.com/Mixiaoxiao/FastScroll-Everywhere FastScrollRecyclerView
 *
 * @author Mixiaoxiao 2016-08-31
 */
public class FastScrollRecyclerView extends RecyclerView implements FastScrollable {

    private FastScrollDelegate mFastScrollDelegate;
    private OnKeyEventListener mListener;

    // ===========================================================
    // Constructors
    // ===========================================================

    public FastScrollRecyclerView(Context context) {
        super(context);
        createFastScrollDelegate(context);
    }

    public FastScrollRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createFastScrollDelegate(context);
    }

    public FastScrollRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        createFastScrollDelegate(context);
    }

    // ===========================================================
    // createFastScrollDelegate
    // ===========================================================

    private void createFastScrollDelegate(Context context) {
        mFastScrollDelegate = new FastScrollDelegate.Builder(this).build();
    }

    // ===========================================================
    // Delegate
    // ===========================================================

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mFastScrollDelegate.onInterceptTouchEvent(ev)) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mFastScrollDelegate.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mFastScrollDelegate.onAttachedToWindow();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (mFastScrollDelegate != null) {
            mFastScrollDelegate.onVisibilityChanged(changedView, visibility);
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mFastScrollDelegate.onWindowVisibilityChanged(visibility);
    }

    @Override
    protected boolean awakenScrollBars() {
        return mFastScrollDelegate.awakenScrollBars();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        mFastScrollDelegate.dispatchDrawOver(canvas);
    }

    // ===========================================================
    // FastScrollable IMPL, ViewInternalSuperMethods
    // ===========================================================

    @Override
    public void superOnTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
    }

    @Override
    public int superComputeVerticalScrollExtent() {
        return super.computeVerticalScrollExtent();
    }

    @Override
    public int superComputeVerticalScrollOffset() {
        return super.computeVerticalScrollOffset();
    }

    @Override
    public int superComputeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    @Override
    public View getFastScrollableView() {
        return this;
    }

    /**
     * @deprecated use {@link #getFastScrollDelegate()} instead
     */
    public FastScrollDelegate getDelegate() {
        return getFastScrollDelegate();
    }

    @Override
    public FastScrollDelegate getFastScrollDelegate() {
        return mFastScrollDelegate;
    }

    @Override
    public void setNewFastScrollDelegate(FastScrollDelegate newDelegate) {
        if (newDelegate == null) {
            throw new IllegalArgumentException("setNewFastScrollDelegate must NOT be NULL.");
        }
        mFastScrollDelegate.onDetachedFromWindow();
        mFastScrollDelegate = newDelegate;
        newDelegate.onAttachedToWindow();
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {


        if (this.mListener == null || !this.mListener.onKeyDown(i, keyEvent)) {
            return super.onKeyDown(i, keyEvent);
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int i, KeyEvent keyEvent) {

        if (this.mListener == null || !this.mListener.onKeyUp(i, keyEvent)) {
            return super.onKeyUp(i, keyEvent);
        }
        return true;
    }

    @Override
    public boolean requestFocus(int i, Rect rect) {
        if (getFocusedChild() != null) {
            return false;
        }
        return super.requestFocus(i, rect);
    }

    public void setOnKeyEventListener(OnKeyEventListener onKeyEventListener) {
        this.mListener = onKeyEventListener;
    }


    public interface OnKeyEventListener {
        boolean onKeyDown(int i, KeyEvent keyEvent);

        boolean onKeyUp(int i, KeyEvent keyEvent);
    }


}
