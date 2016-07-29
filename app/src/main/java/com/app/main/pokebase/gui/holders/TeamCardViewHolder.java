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

/**
 * @author Tyler Wong
 */
public class TeamCardViewHolder extends RecyclerView.ViewHolder {
   public View view;
   public TextView mTitleLabel;
   public TextView mDescription;
   public TextView mLastUpdated;
   public ImageView mPokemonOne;
   public ImageView mPokemonTwo;
   public ImageView mPokemonThree;
   public ImageView mPokemonFour;
   public ImageView mPokemonFive;
   public ImageView mPokemonSix;
   public int mTeamId;

   public ImageView[] pokemonList;

   private final static int NUM_POKEMON = 6;

   public TeamCardViewHolder(View itemView) {
      super(itemView);
      this.view = itemView;
      this.mTeamId = 0;

      this.view.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
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
      });

      mTitleLabel = (TextView) itemView.findViewById(R.id.title_label);
      mDescription = (TextView) itemView.findViewById(R.id.description);
      mLastUpdated = (TextView) itemView.findViewById(R.id.last_updated);
      mPokemonOne = (ImageView) itemView.findViewById(R.id.pokemon_1);
      mPokemonTwo = (ImageView) itemView.findViewById(R.id.pokemon_2);
      mPokemonThree = (ImageView) itemView.findViewById(R.id.pokemon_3);
      mPokemonFour = (ImageView) itemView.findViewById(R.id.pokemon_4);
      mPokemonFive = (ImageView) itemView.findViewById(R.id.pokemon_5);
      mPokemonSix = (ImageView) itemView.findViewById(R.id.pokemon_6);

      pokemonList = new ImageView[NUM_POKEMON];

      pokemonList[0] = mPokemonOne;
      pokemonList[1] = mPokemonTwo;
      pokemonList[2] = mPokemonThree;
      pokemonList[3] = mPokemonFour;
      pokemonList[4] = mPokemonFive;
      pokemonList[5] = mPokemonSix;
   }

   public void setTeamId(int teamId) {
      this.mTeamId = teamId;
   }
}
