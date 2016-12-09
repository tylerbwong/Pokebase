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
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import me.tylerbwong.pokebase.R;
import me.tylerbwong.pokebase.gui.activities.PokemonProfileActivity;
import me.tylerbwong.pokebase.gui.holders.PokemonListItemViewHolder;
import me.tylerbwong.pokebase.model.components.PokemonListItem;

/**
 * @author Tyler Wong
 */
public class PokemonListAdapter extends RecyclerView.Adapter implements View.OnClickListener {
   private Context mContext;
   private PokemonListItem[] mItems;
   private boolean mIsEvolutions = false;

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
      holder.mIdView.setText(PokemonProfileActivity.formatId(item.id));
      holder.mNameView.setText(item.name);
      Glide.with(mContext)
            .load(String.format(mContext.getString(R.string.icon_url), item.originalName.toLowerCase()))
            .fitCenter()
            .into(holder.mIconView);
      holder.mView.setOnClickListener(this);
   }

   @Override
   public int getItemCount() {
      if (mItems != null) {
         return mItems.length;
      }
      return 0;
   }

   @Override
   public void onClick(View v) {
      int pokemonId = Integer.valueOf((((TextView) v.findViewById(R.id.id)).getText()).toString());
      Intent profileIntent = new Intent(mContext, PokemonProfileActivity.class);
      Bundle extras = new Bundle();
      extras.putInt(PokemonProfileActivity.POKEMON_ID_KEY, pokemonId);
      extras.putString(PokemonProfileActivity.POKEMON_NAME_KEY, (((TextView) v.findViewById(R.id.name)).getText()).toString());
      profileIntent.putExtras(extras);
      mContext.startActivity(profileIntent);

      if (mIsEvolutions) {
         ((PokemonProfileActivity) mContext).finish();
         ((PokemonProfileActivity) mContext).closeEvolutionsDialog();
      }
   }
}
