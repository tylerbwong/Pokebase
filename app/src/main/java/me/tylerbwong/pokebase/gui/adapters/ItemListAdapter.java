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

package me.tylerbwong.pokebase.gui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import me.tylerbwong.pokebase.R;
import me.tylerbwong.pokebase.gui.holders.ItemListItemViewHolder;
import me.tylerbwong.pokebase.model.components.Item;

/**
 * @author Tyler Wong
 */
public class ItemListAdapter extends RecyclerView.Adapter {
   private Context mContext;
   private Item[] mItems;

   private static final String DRAWABLE = "drawable";

   public ItemListAdapter(Context context, Item[] items) {
      this.mContext = context;
      this.mItems = items;
   }

   @Override
   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_item, parent, false);
      return new ItemListItemViewHolder(view);
   }

   @Override
   public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
      ItemListItemViewHolder holder = (ItemListItemViewHolder) viewHolder;
      Item curItem = mItems[position];
      holder.mNameView.setText(curItem.getName());

      int imageResourceId = mContext.getResources().getIdentifier(curItem.getIdentifier(),
            DRAWABLE, mContext.getPackageName());
      Glide.with(mContext)
            .load(imageResourceId)
            .placeholder(R.drawable.tm_normal)
            .into(holder.mItemView);

      holder.mItem = curItem;
   }

   public void setItems(Item[] items) {
      this.mItems = items;
      notifyDataSetChanged();
   }

   @Override
   public int getItemCount() {
      if (mItems != null) {
         return mItems.length;
      }
      return 0;
   }
}
