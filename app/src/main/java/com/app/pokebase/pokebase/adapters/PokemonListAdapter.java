package com.app.pokebase.pokebase.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.pokebase.pokebase.R;
import com.app.pokebase.pokebase.activities.PokemonProfileActivity;
import com.app.pokebase.pokebase.components.PokemonListItem;
import com.app.pokebase.pokebase.holders.PokemonListItemViewHolder;

/**
 * @author Brittany Berlanga
 */
public class PokemonListAdapter extends RecyclerView.Adapter implements View.OnClickListener {
   private Context mContext;
   private PokemonListItem[] mItems;

   public PokemonListAdapter(Context context, PokemonListItem[] items) {
      this.mContext = context;
      this.mItems = items;
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
      holder.mIdView.setText(String.valueOf(item.mId));
      holder.mNameView.setText(item.mName);
      int imageResourceId = mContext.getResources().getIdentifier("icon_" + item.mId,
            "drawable", mContext.getPackageName());
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
   }
}
