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

package me.tylerbwong.pokebase.gui.views;

import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.tylerbwong.pokebase.R;

/**
 * @author Tyler Wong
 */
public class MoveInfoView extends RelativeLayout {
   @BindView(R.id.type_label)
   TextView mType;
   @BindView(R.id.power_label)
   TextView mPower;
   @BindView(R.id.pp_label)
   TextView mPp;
   @BindView(R.id.accuracy_label)
   TextView mAccuracy;
   @BindView(R.id.class_label)
   TextView mClass;
   @BindView(R.id.description)
   TextView mDescription;

   private Context mContext;

   private static final String CLASS = "class";
   private static final String TYPE = "type";
   private static final String COLOR = "color";

   public MoveInfoView(Context context) {
      super(context, null);
      mContext = context;
      init();
   }

   public MoveInfoView(Context context, AttributeSet attrs) {
      super(context, attrs);
      mContext = context;
      init();
   }

   public MoveInfoView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      mContext = context;
      init();
   }

   private void init() {
      View view = inflate(mContext, R.layout.move_info, this);
      ButterKnife.bind(this, view);
   }

   public void setFields(String type, String power, String pp, String accuracy, String className, String description) {
      PaintDrawable backgroundColor;
      float dimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
            getResources().getDisplayMetrics());

      mType.setText(type);
      mPower.setText(power);
      mPp.setText(pp);
      mAccuracy.setText(accuracy);
      mClass.setText(className);
      mDescription.setText(description);

      String classColor = CLASS + className;
      int colorResId = getResources().getIdentifier(classColor, COLOR, mContext.getPackageName());
      backgroundColor = new PaintDrawable(ContextCompat.getColor(mContext, colorResId));
      backgroundColor.setCornerRadius(dimension);
      mClass.setBackground(backgroundColor);

      String typeColor = TYPE + type;
      colorResId = getResources().getIdentifier(typeColor, COLOR, mContext.getPackageName());
      backgroundColor = new PaintDrawable(ContextCompat.getColor(mContext, colorResId));
      backgroundColor.setCornerRadius(dimension);
      mType.setBackground(backgroundColor);
   }
}
