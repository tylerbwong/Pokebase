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
   private Runnable mRunnable;

   private final static int TRANSLATION_Y = 100;
   private final static int DELAY = 100;
   private final static int DURATION = 200;

   public AnimatedRecyclerView(Context context) {
      this(context, null);
      init();
   }

   public AnimatedRecyclerView(Context context, AttributeSet attrs) {
      this(context, attrs, 0);
      init();
   }

   public AnimatedRecyclerView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      mScrollable = false;
      init();
   }

   private void init() {
      mRunnable = new Runnable() {
         @Override
         public void run() {
            mScrollable = true;
         }
      };
   }

   @Override
   public boolean dispatchTouchEvent(MotionEvent event) {
      return !mScrollable || super.dispatchTouchEvent(event);
   }

   @Override
   protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
      super.onLayout(changed, left, top, right, bottom);
      for (int index = 0; index < getChildCount(); index++) {
         animate(getChildAt(index), index);

         if (index == getChildCount() - 1) {
            getHandler().postDelayed(mRunnable, index * DELAY);
         }
      }
   }

   private void animate(View view, int position) {
      view.animate().cancel();
      view.setTranslationY(TRANSLATION_Y);
      view.setAlpha(0);
      view.animate().alpha(1.0f).translationY(0).setDuration(DURATION).setStartDelay(position * DELAY);
   }
}
