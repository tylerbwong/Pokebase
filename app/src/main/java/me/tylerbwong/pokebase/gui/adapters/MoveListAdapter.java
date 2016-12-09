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

import me.tylerbwong.pokebase.R;
import me.tylerbwong.pokebase.gui.holders.MoveListItemViewHolder;

/**
 * @author Tyler Wong
 */
public class MoveListAdapter extends RecyclerView.Adapter {
   private Context mContext;
   private String[] mMoves;

   public MoveListAdapter(Context context, String[] items) {
      this.mContext = context;
      this.mMoves = items;
   }

   @Override
   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_move, parent, false);
      return new MoveListItemViewHolder(view);
   }

   @Override
   public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
      MoveListItemViewHolder holder = (MoveListItemViewHolder) viewHolder;
      holder.mNameView.setText(mMoves[position]);
   }

   @Override
   public int getItemCount() {
      if (mMoves != null) {
         return mMoves.length;
      }
      return 0;
   }
}
