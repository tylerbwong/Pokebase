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
   @BindView(R.id.id)
   public TextView mIdView;
   @BindView(R.id.name)
   public TextView mNameView;
   @BindView(R.id.small_icon)
   public ImageView mIconView;

   public final View mView;

   public PokemonListItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      this.mView = itemView;
   }
}
