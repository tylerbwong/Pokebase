package com.app.main.pokebase.gui.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.main.pokebase.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Brittany Berlanga
 */
public class PokemonListItemViewHolder extends RecyclerView.ViewHolder {
   @BindView(R.id.id) public TextView mIdView;
   @BindView(R.id.name) public TextView mNameView;
   @BindView(R.id.small_icon) public ImageView mIconView;

   public final View mView;

   public PokemonListItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      this.mView = itemView;
   }
}
