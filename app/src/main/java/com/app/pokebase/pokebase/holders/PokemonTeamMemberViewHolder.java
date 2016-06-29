package com.app.pokebase.pokebase.holders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pokebase.pokebase.R;
import com.app.pokebase.pokebase.activities.PokemonEditorActivity;
import com.app.pokebase.pokebase.components.PokemonTeamMember;

/**
 * @author Tyler Wong
 */
public class PokemonTeamMemberViewHolder extends RecyclerView.ViewHolder {
   public View view;
   public ImageView mPokemon;
   public TextView mName;
   public TextView mLevel;
   public TextView mMoveset;
   private PokemonTeamMember mPokemonData;
   private String mTitle;
   private String mDescription;
   private int mTeamId;

   public PokemonTeamMemberViewHolder(View itemView) {
      super(itemView);
      this.view = itemView;

      this.view.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Context cardContext = v.getContext();
            Intent editorIntent = new Intent(cardContext, PokemonEditorActivity.class);
            editorIntent.putExtra("pokemonId", mPokemonData.mPokemonId);
            editorIntent.putExtra("teamId", mTeamId);
            editorIntent.putExtra("title", mTitle);
            editorIntent.putExtra("description", mDescription);
            editorIntent.putExtra("memberId", mPokemonData.mMemberId);
            editorIntent.putExtra("level", mPokemonData.mLevel);
            editorIntent.putExtra("nickname", mPokemonData.mNickname);
            editorIntent.putExtra("moveOne", mPokemonData.mMoves.get(0));
            editorIntent.putExtra("moveTwo", mPokemonData.mMoves.get(1));
            editorIntent.putExtra("moveThree", mPokemonData.mMoves.get(2));
            editorIntent.putExtra("moveFour", mPokemonData.mMoves.get(3));

            v.getContext().startActivity(editorIntent);
         }
      });

      mPokemon = (ImageView) itemView.findViewById(R.id.pokemon);
      mName = (TextView) itemView.findViewById(R.id.name);
      mLevel = (TextView) itemView.findViewById(R.id.level);
      mMoveset = (TextView) itemView.findViewById(R.id.moveset);
   }

   public void setPokemon(PokemonTeamMember member) {
      this.mPokemonData = member;
   }

   public void setTitle(String name) {
      this.mTitle = name;
   }

   public void setDescription(String description) {
      this.mDescription = description;
   }

   public void setTeamId(int teamId) {
      this.mTeamId = teamId;
   }
}