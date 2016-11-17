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

package com.app.main.pokebase.gui.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.main.pokebase.R;
import com.app.main.pokebase.gui.views.ItemInfoView;
import com.app.main.pokebase.model.components.Item;
import com.app.main.pokebase.model.database.DatabaseOpenHelper;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Tyler Wong
 */
public class ItemListItemViewHolder extends RecyclerView.ViewHolder {
   @BindView(R.id.name)
   public TextView mNameView;
   @BindView(R.id.small_icon)
   public ImageView mItemView;

   public final View mView;

   public Item mItem;

   private final static String DRAWABLE = "drawable";
   private final static String NONE = "0";
   private final static String NA = "N/A";

   public ItemListItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      this.mView = itemView;

      mView.setOnClickListener(view -> showItemDialog());
   }

   private void showItemDialog() {
      Context context = mView.getContext();
      DatabaseOpenHelper databaseHelper = DatabaseOpenHelper.getInstance(context);

      int imageResourceId = context.getResources().getIdentifier(mItem.getIdentifier(),
            DRAWABLE, context.getPackageName());

      String cost = String.valueOf(mItem.getCost());
      String description;

      if (mItem.getDescription() == null) {
         description = databaseHelper.queryItemDescription(mItem.getId());
         mItem.setDescription(description);
      }
      else {
         description = mItem.getDescription();
      }

      if (cost.equals(NONE)) {
         cost = NA;
      }

      ItemInfoView infoView = new ItemInfoView(context);
      infoView.setFields(cost, imageResourceId, description);

      new LovelyCustomDialog(mView.getContext())
            .setCancelable(true)
            .setTopColorRes(R.color.colorPrimary)
            .setIcon(R.drawable.ic_business_center_white_24dp)
            .setTitle(mItem.getName())
            .setView(infoView)
            .show();
   }
}
