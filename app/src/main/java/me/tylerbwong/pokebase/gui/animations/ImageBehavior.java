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
    private Context context;

    private float customFinalYPosition;
    private float customStartXPosition;
    private float customStartToolbarPosition;
    private float customStartHeight;
    private float customFinalHeight;

    private float avatarMaxSize;
    private float finalLeftAvatarPadding;
    private float startPosition;
    private int startXPosition;
    private float startToolbarPosition;
    private int startYPosition;
    private int finalYPosition;
    private int startHeight;
    private int finalXPosition;
    private float changeBehaviorPoint;

    public ImageBehavior(Context context, AttributeSet attrs) {
        this.context = context;

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageBehavior);
            customFinalYPosition = array.getDimension(R.styleable.AvatarImageBehavior_finalYPosition, 0);
            customStartXPosition = array.getDimension(R.styleable.AvatarImageBehavior_startXPosition, 0);
            customStartToolbarPosition = array.getDimension(R.styleable.AvatarImageBehavior_startToolbarPosition, 0);
            customStartHeight = array.getDimension(R.styleable.AvatarImageBehavior_startHeight, 0);
            customFinalHeight = array.getDimension(R.styleable.AvatarImageBehavior_finalHeight, 0);

            array.recycle();
        }

        init();

        finalLeftAvatarPadding = context.getResources().getDimension(R.dimen.spacing_normal);
    }

    private void init() {
        bindDimensions();
    }

    private void bindDimensions() {
        avatarMaxSize = context.getResources().getDimension(R.dimen.image_width);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
        return dependency instanceof Toolbar;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {
        maybeInitProperties(child, dependency);

        final int maxScrollDistance = (int) (startToolbarPosition);
        float expandedPercentageFactor = dependency.getY() / maxScrollDistance;

        if (expandedPercentageFactor < changeBehaviorPoint) {
            float heightFactor = (changeBehaviorPoint - expandedPercentageFactor) / changeBehaviorPoint;

            float distanceXToSubtract = ((startXPosition - finalXPosition)
                    * heightFactor) + (child.getHeight() / 2);
            float distanceYToSubtract = ((startYPosition - finalYPosition)
                    * (1f - expandedPercentageFactor)) + (child.getHeight() / 2);

            child.setX(startXPosition - distanceXToSubtract);
            child.setY(startYPosition - distanceYToSubtract);

            float heightToSubtract = ((startHeight - customFinalHeight) * heightFactor);

            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            lp.width = (int) (startHeight - heightToSubtract);
            lp.height = (int) (startHeight - heightToSubtract);
            child.setLayoutParams(lp);
        }
        else {
            float distanceYToSubtract = ((startYPosition - finalYPosition)
                    * (1f - expandedPercentageFactor)) + (startHeight / 2);

            child.setX(startXPosition - child.getWidth() / 2);
            child.setY(startYPosition - distanceYToSubtract);

            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            lp.width = startHeight;
            lp.height = startHeight;
            child.setLayoutParams(lp);
        }
        return true;
    }

    private void maybeInitProperties(ImageView child, View dependency) {
        if (startYPosition == 0)
            startYPosition = (int) (dependency.getY());

        if (finalYPosition == 0)
            finalYPosition = (dependency.getHeight() / 2);

        if (startHeight == 0)
            startHeight = child.getHeight();

        if (startXPosition == 0)
            startXPosition = (int) (child.getX() + (child.getWidth() / 2));

        if (finalXPosition == 0)
            finalXPosition = context.getResources().getDimensionPixelOffset(
                    R.dimen.abc_action_bar_content_inset_material) + ((int) customFinalHeight / 2) +
                    Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40,
                            dependency.getContext().getResources().getDisplayMetrics()));

        if (startToolbarPosition == 0)
            startToolbarPosition = dependency.getY();

        if (changeBehaviorPoint == 0) {
            changeBehaviorPoint = (child.getHeight() - customFinalHeight) / (2f * (startYPosition - finalYPosition));
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier(STATUS_BAR_HEIGHT, DIMEN, ANDROID);

        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
