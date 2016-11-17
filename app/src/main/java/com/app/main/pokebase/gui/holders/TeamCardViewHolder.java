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
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.main.pokebase.R;
import com.app.main.pokebase.gui.activities.TeamViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Tyler Wong
 */
public class TeamCardViewHolder extends RecyclerView.ViewHolder {
   @BindView(R.id.title_label)
   public TextView mTitleLabel;
   @BindView(R.id.description)
   public TextView mDescription;
   @BindView(R.id.last_updated)
   public TextView mLastUpdated;
   @BindView(R.id.pokemon_1)
   public ImageView mPokemonOne;
   @BindView(R.id.pokemon_2)
   public ImageView mPokemonTwo;
   @BindView(R.id.pokemon_3)
   public ImageView mPokemonThree;
   @BindView(R.id.pokemon_4)
   public ImageView mPokemonFour;
   @BindView(R.id.pokemon_5)
   public ImageView mPokemonFive;
   @BindView(R.id.pokemon_6)
   public ImageView mPokemonSix;

   public final View mView;
   public ImageView[] mPokemonList;

   public int mTeamId;

   private final static int NUM_POKEMON = 6;

   public TeamCardViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      this.mView = itemView;
      mTeamId = 0;

      this.mView.setOnClickListener(view -> {
            Context cardContext = view.getContext();
            Intent editorIntent = new Intent(cardContext, TeamViewActivity.class);
            Bundle extras = new Bundle();
            extras.putInt(TeamViewActivity.TEAM_ID_KEY, mTeamId);
            extras.putBoolean(TeamViewActivity.UPDATE_KEY, true);
            extras.putString(TeamViewActivity.TEAM_NAME, mTitleLabel.getText().toString());
            extras.putString(TeamViewActivity.DESCRIPTION, mDescription.getText().toString());
            editorIntent.putExtras(extras);
            view.getContext().startActivity(editorIntent);
         }
      );

      mPokemonList = new ImageView[NUM_POKEMON];

      mPokemonList[0] = mPokemonOne;
      mPokemonList[1] = mPokemonTwo;
      mPokemonList[2] = mPokemonThree;
      mPokemonList[3] = mPokemonFour;
      mPokemonList[4] = mPokemonFive;
      mPokemonList[5] = mPokemonSix;
   }

   public void setTeamId(int teamId) {
      this.mTeamId = teamId;
   }
}
