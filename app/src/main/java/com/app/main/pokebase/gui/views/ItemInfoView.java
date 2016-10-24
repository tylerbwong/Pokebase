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

package com.app.main.pokebase.gui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.main.pokebase.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Tyler Wong
 */
public class ItemInfoView extends RelativeLayout {
   @BindView(R.id.cost)
   TextView mCost;
   @BindView(R.id.item)
   ImageView mItem;
   @BindView(R.id.description)
   TextView mDescription;

   private Context mContext;

   public ItemInfoView(Context context) {
      super(context, null);
      mContext = context;
      init();
   }

   public ItemInfoView(Context context, AttributeSet attrs) {
      super(context, attrs);
      mContext = context;
      init();
   }

   public ItemInfoView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      mContext = context;
      init();
   }

   private void init() {
      View view = inflate(mContext, R.layout.item_info, this);
      ButterKnife.bind(this, view);
   }

   public void setFields(String cost, int identifier, String description) {
      mCost.setText(cost);
      mItem.setImageResource(identifier);
      mDescription.setText(description);

      if (mItem.getDrawable() == null) {
         mItem.setImageResource(R.drawable.tm_normal);
      }
   }
}
