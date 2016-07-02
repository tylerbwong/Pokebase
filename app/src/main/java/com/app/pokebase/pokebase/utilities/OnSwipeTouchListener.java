package com.app.pokebase.pokebase.utilities;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * @author Tyler Wong
 */
public class OnSwipeTouchListener implements OnTouchListener {

   private final GestureDetector gestureDetector;

   public OnSwipeTouchListener (Context context){
      gestureDetector = new GestureDetector(context, new GestureListener());
   }

   @Override
   public boolean onTouch(View view, MotionEvent event) {
      return gestureDetector.onTouchEvent(event);
   }

   private final class GestureListener extends SimpleOnGestureListener {

      private static final int SWIPE_THRESHOLD = 100;
      private static final int SWIPE_VELOCITY_THRESHOLD = 100;

      @Override
      public boolean onDown(MotionEvent event) {
         return true;
      }

      @Override
      public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
         boolean result = false;
         try {
            float diffY = event2.getY() - event1.getY();
            float diffX = event2.getX() - event1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
               if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                  if (diffX > 0) {
                     onSwipeRight();
                  } else {
                     onSwipeLeft();
                  }
               }
               result = true;
            }
            else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
               if (diffY > 0) {
                  onSwipeBottom();
               } else {
                  onSwipeTop();
               }
            }
            result = true;

         } catch (Exception exception) {
            exception.printStackTrace();
         }
         return result;
      }
   }

   public void onSwipeRight() {
   }

   public void onSwipeLeft() {
   }

   public void onSwipeTop() {
   }

   public void onSwipeBottom() {
   }
}