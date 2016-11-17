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

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.main.pokebase.R;
import com.app.main.pokebase.gui.activities.PokemonEditorActivity;
import com.app.main.pokebase.gui.activities.TeamViewActivity;
import com.app.main.pokebase.model.components.PokemonTeamMember;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Tyler Wong
 */
public class PokemonTeamMemberViewHolder extends RecyclerView.ViewHolder {
   @BindView(R.id.pokemon)
   public ImageView mPokemon;
   @BindView(R.id.name)
   public TextView mName;
   @BindView(R.id.level)
   public TextView mLevel;
   @BindView(R.id.last_updated)
   public TextView mLastUpdated;
   @BindView(R.id.moveset)
   public TextView mMoveset;

   public final View mView;

   private PokemonTeamMember mPokemonData;
   private String mTitle;
   private String mDescription;
   private int mTeamId;

   public PokemonTeamMemberViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      this.mView = itemView;

      mView.setOnClickListener(view -> {
            Context cardContext = view.getContext();
            Intent editorIntent = new Intent(cardContext, PokemonEditorActivity.class);
            String transitionName = cardContext.getString(R.string.shared_transition);
            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                  (TeamViewActivity) cardContext, mPokemon, transitionName);
            editorIntent.putExtra("pokemonId", mPokemonData.mPokemonId);
            editorIntent.putExtra("teamId", mTeamId);
            editorIntent.putExtra("title", mTitle);
            editorIntent.putExtra("description", mDescription);
            editorIntent.putExtra("memberId", mPokemonData.mMemberId);
            editorIntent.putExtra("level", mPokemonData.mLevel);
            editorIntent.putExtra("nickname", mPokemonData.mNickname);
            editorIntent.putExtra("moveOne", mPokemonData.mMoves[0]);
            editorIntent.putExtra("moveTwo", mPokemonData.mMoves[1]);
            editorIntent.putExtra("moveThree", mPokemonData.mMoves[2]);
            editorIntent.putExtra("moveFour", mPokemonData.mMoves[3]);

            view.getContext().startActivity(editorIntent, transitionActivityOptions.toBundle());
         }
      );
   }

   public void setPokemon(PokemonTeamMember member) {
      mPokemonData = member;
   }

   public void setTitle(String name) {
      mTitle = name;
   }

   public void setDescription(String description) {
      mDescription = description;
   }

   public void setTeamId(int teamId) {
      mTeamId = teamId;
   }
}