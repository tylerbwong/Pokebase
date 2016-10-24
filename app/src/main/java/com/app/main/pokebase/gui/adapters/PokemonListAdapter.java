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

package com.app.main.pokebase.gui.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.main.pokebase.R;
import com.app.main.pokebase.gui.activities.PokemonProfileActivity;
import com.app.main.pokebase.model.components.PokemonListItem;
import com.app.main.pokebase.gui.holders.PokemonListItemViewHolder;

/**
 * @author Tyler Wong
 */
public class PokemonListAdapter extends RecyclerView.Adapter implements View.OnClickListener {
   private Context mContext;
   private PokemonListItem[] mItems;
   private boolean mIsEvolutions = false;

   private final static String DRAWABLE = "drawable";
   private final static String ICON = "icon_";

   public PokemonListAdapter(Context context, PokemonListItem[] items, boolean isEvolutions) {
      this.mContext = context;
      this.mItems = items;
      mIsEvolutions = isEvolutions;
   }

   @Override
   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_pokemon, parent, false);
      return new PokemonListItemViewHolder(view);
   }

   @Override
   public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
      PokemonListItemViewHolder holder = (PokemonListItemViewHolder) viewHolder;
      PokemonListItem item = mItems[position];
      holder.mIdView.setText(PokemonProfileActivity.formatId(item.mId));
      holder.mNameView.setText(item.mName);
      int imageResourceId = mContext.getResources().getIdentifier(ICON + item.mId,
            DRAWABLE, mContext.getPackageName());
      holder.mIconView.setImageResource(imageResourceId);
      holder.mView.setOnClickListener(this);
   }

   @Override
   public int getItemCount() {
      return mItems.length;
   }

   @Override
   public void onClick(View v) {
      int pokemonId = Integer.valueOf((((TextView) v.findViewById(R.id.id)).getText()).toString());
      Intent profileIntent = new Intent(mContext, PokemonProfileActivity.class);
      Bundle extras = new Bundle();
      extras.putInt(PokemonProfileActivity.POKEMON_ID_KEY, pokemonId);
      profileIntent.putExtras(extras);
      mContext.startActivity(profileIntent);

      if (mIsEvolutions) {
         ((PokemonProfileActivity) mContext).finish();
         ((PokemonProfileActivity) mContext).closeEvolutionsDialog();
      }
   }
}
