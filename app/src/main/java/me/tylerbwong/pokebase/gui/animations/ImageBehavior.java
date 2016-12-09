/*
 * Copyright 2016 Tyler Wong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.tylerbwong.pokebase.gui.animations;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import me.tylerbwong.pokebase.R;

@SuppressWarnings("unused")
public class ImageBehavior extends CoordinatorLayout.Behavior<ImageView> {
   private static final String TAG = "behavior";
   private static final String STATUS_BAR_HEIGHT = "status_bar_height";
   private static final String DIMEN = "dimen";
   private static final String ANDROID = "android";
   private Context mContext;

   private float mCustomFinalYPosition;
   private float mCustomStartXPosition;
   private float mCustomStartToolbarPosition;
   private float mCustomStartHeight;
   private float mCustomFinalHeight;

   private float mAvatarMaxSize;
   private float mFinalLeftAvatarPadding;
   private float mStartPosition;
   private int mStartXPosition;
   private float mStartToolbarPosition;
   private int mStartYPosition;
   private int mFinalYPosition;
   private int mStartHeight;
   private int mFinalXPosition;
   private float mChangeBehaviorPoint;

   public ImageBehavior(Context context, AttributeSet attrs) {
      mContext = context;

      if (attrs != null) {
         TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageBehavior);
         mCustomFinalYPosition = array.getDimension(R.styleable.AvatarImageBehavior_finalYPosition, 0);
         mCustomStartXPosition = array.getDimension(R.styleable.AvatarImageBehavior_startXPosition, 0);
         mCustomStartToolbarPosition = array.getDimension(R.styleable.AvatarImageBehavior_startToolbarPosition, 0);
         mCustomStartHeight = array.getDimension(R.styleable.AvatarImageBehavior_startHeight, 0);
         mCustomFinalHeight = array.getDimension(R.styleable.AvatarImageBehavior_finalHeight, 0);

         array.recycle();
      }

      init();

      mFinalLeftAvatarPadding = context.getResources().getDimension(R.dimen.spacing_normal);
   }

   private void init() {
      bindDimensions();
   }

   private void bindDimensions() {
      mAvatarMaxSize = mContext.getResources().getDimension(R.dimen.image_width);
   }

   @Override
   public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
      return dependency instanceof Toolbar;
   }

   @Override
   public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {
      maybeInitProperties(child, dependency);

      final int maxScrollDistance = (int) (mStartToolbarPosition);
      float expandedPercentageFactor = dependency.getY() / maxScrollDistance;

      if (expandedPercentageFactor < mChangeBehaviorPoint) {
         float heightFactor = (mChangeBehaviorPoint - expandedPercentageFactor) / mChangeBehaviorPoint;

         float distanceXToSubtract = ((mStartXPosition - mFinalXPosition)
               * heightFactor) + (child.getHeight() / 2);
         float distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
               * (1f - expandedPercentageFactor)) + (child.getHeight() / 2);

         child.setX(mStartXPosition - distanceXToSubtract);
         child.setY(mStartYPosition - distanceYToSubtract);

         float heightToSubtract = ((mStartHeight - mCustomFinalHeight) * heightFactor);

         CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
         lp.width = (int) (mStartHeight - heightToSubtract);
         lp.height = (int) (mStartHeight - heightToSubtract);
         child.setLayoutParams(lp);
      }
      else {
         float distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
               * (1f - expandedPercentageFactor)) + (mStartHeight / 2);

         child.setX(mStartXPosition - child.getWidth() / 2);
         child.setY(mStartYPosition - distanceYToSubtract);

         CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
         lp.width = mStartHeight;
         lp.height = mStartHeight;
         child.setLayoutParams(lp);
      }
      return true;
   }

   private void maybeInitProperties(ImageView child, View dependency) {
      if (mStartYPosition == 0)
         mStartYPosition = (int) (dependency.getY());

      if (mFinalYPosition == 0)
         mFinalYPosition = (dependency.getHeight() / 2);

      if (mStartHeight == 0)
         mStartHeight = child.getHeight();

      if (mStartXPosition == 0)
         mStartXPosition = (int) (child.getX() + (child.getWidth() / 2));

      if (mFinalXPosition == 0)
         mFinalXPosition = mContext.getResources().getDimensionPixelOffset(
               R.dimen.abc_action_bar_content_inset_material) + ((int) mCustomFinalHeight / 2) +
               Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40,
                     dependency.getContext().getResources().getDisplayMetrics()));

      if (mStartToolbarPosition == 0)
         mStartToolbarPosition = dependency.getY();

      if (mChangeBehaviorPoint == 0) {
         mChangeBehaviorPoint = (child.getHeight() - mCustomFinalHeight) / (2f * (mStartYPosition - mFinalYPosition));
      }
   }

   public int getStatusBarHeight() {
      int result = 0;
      int resourceId = mContext.getResources().getIdentifier(STATUS_BAR_HEIGHT, DIMEN, ANDROID);

      if (resourceId > 0) {
         result = mContext.getResources().getDimensionPixelSize(resourceId);
      }
      return result;
   }
}
