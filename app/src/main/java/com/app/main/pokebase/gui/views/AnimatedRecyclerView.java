package com.app.main.pokebase.gui.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Tyler Wong
 */
public class AnimatedRecyclerView extends RecyclerView {
   private boolean mScrollable;

   public AnimatedRecyclerView(Context context) {
      this(context, null);
   }

   public AnimatedRecyclerView(Context context, AttributeSet attrs) {
      this(context, attrs, 0);
   }

   public AnimatedRecyclerView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      mScrollable = false;
   }

   @Override
   public boolean dispatchTouchEvent(MotionEvent ev) {
      return !mScrollable || super.dispatchTouchEvent(ev);
   }

   @Override
   protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
      super.onLayout(changed, left, top, right, bottom);
      for (int index = 0; index < getChildCount(); index++) {
         animate(getChildAt(index), index);

         if (index == getChildCount() - 1) {
            getHandler().postDelayed(new Runnable() {
               @Override
               public void run() {
                  mScrollable = true;
               }
            }, index * 100);
         }
      }
   }

   private void animate(View view, final int pos) {
      view.animate().cancel();
      view.setTranslationY(100);
      view.setAlpha(0);
      view.animate().alpha(1.0f).translationY(0).setDuration(300).setStartDelay(pos * 100);
   }
}
