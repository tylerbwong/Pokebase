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
import me.tylerbwong.pokebase.gui.holders.PokemonTeamMemberViewHolder;
import me.tylerbwong.pokebase.model.components.PokemonTeamMember;

/**
 * @author Tyler Wong
 */
public class PokemonTeamMemberAdapter extends RecyclerView.Adapter<PokemonTeamMemberViewHolder> {
   private PokemonTeamMember[] mPokemon;
   private Context mContext;
   private String mName;
   private String mDescription;
   private int mTeamId;

   private static final String SPRITE = "sprites_";
   private static final String DRAWABLE = "drawable";

   public PokemonTeamMemberAdapter(Context context, PokemonTeamMember[] pokemon, int teamId,
                                   String name, String description) {
      this.mContext = context;
      this.mPokemon = pokemon;
      this.mName = name;
      this.mDescription = description;
      this.mTeamId = teamId;
   }

   @Override
   public PokemonTeamMemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.pokemon_team_member_card, parent, false);

      return new PokemonTeamMemberViewHolder(view);
   }

   @Override
   public void onBindViewHolder(final PokemonTeamMemberViewHolder holder, int position) {
      PokemonTeamMember curPokemon = mPokemon[position];
      holder.mName.setText(curPokemon.nickname);
      holder.mLevel.setText(String.valueOf(curPokemon.level));
      holder.mLastUpdated.setText(curPokemon.lastUpdated);

      Glide.with(mContext)
            .load(String.format(mContext.getString(R.string.sprite_url), curPokemon.name.toLowerCase()))
            .fitCenter()
            .into(holder.mPokemon);

      String[] moves = curPokemon.moves;
      String moveList = "";
      for (String move : moves) {
         moveList += move + "\n";
      }

      holder.mMoveset.setText(moveList);
      holder.setPokemon(curPokemon);
      holder.setTeamId(mTeamId);
      holder.setTitle(mName);
      holder.setDescription(mDescription);
   }

   public void setTeam(PokemonTeamMember[] team) {
      this.mPokemon = team;
      notifyDataSetChanged();
   }

   @Override
   public int getItemCount() {
      if (mPokemon != null) {
         return mPokemon.length;
      }
      return 0;
   }
}
