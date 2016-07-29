package com.app.main.pokebase.gui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.main.pokebase.R;
import com.app.main.pokebase.model.components.Team;
import com.app.main.pokebase.gui.holders.TeamCardViewHolder;

/**
 * @author Tyler Wong
 */
public class TeamAdapter extends RecyclerView.Adapter {
   private Team[] mTeams;
   private Context mContext;

   private final static String DRAWABLE = "drawable";
   private final static String ICON = "icon_";

   public TeamAdapter(Context context, Team[] teams) {
      this.mContext = context;
      this.mTeams = teams;
   }

   @Override
   public TeamCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(mContext)
            .inflate(R.layout.team_card, parent, false);
      return new TeamCardViewHolder(view);
   }

   @Override
   public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
      TeamCardViewHolder holder = (TeamCardViewHolder) viewHolder;
      Team curTeam = mTeams[position];
      holder.mTitleLabel.setText(curTeam.mName);
      holder.mDescription.setText(curTeam.mDescription);
      holder.mLastUpdated.setText(curTeam.mLastUpdated);
      int teamSize = curTeam.mTeam.length;

      if (teamSize > 0) {
         for (int index = 0; index < teamSize; index++) {
            holder.pokemonList[index].setImageResource(mContext.getResources()
                  .getIdentifier(ICON + curTeam.mTeam[index].mPokemonId,
                        DRAWABLE, mContext.getPackageName()));
         }
      }
      else {
         holder.mPokemonOne.setImageDrawable(null);
         holder.mPokemonTwo.setImageDrawable(null);
         holder.mPokemonThree.setImageDrawable(null);
         holder.mPokemonFour.setImageDrawable(null);
         holder.mPokemonFive.setImageDrawable(null);
         holder.mPokemonSix.setImageDrawable(null);
      }

      holder.setTeamId(curTeam.mId);
   }

   @Override
   public int getItemCount() {
      return mTeams.length;
   }

   @Override
   public void onAttachedToRecyclerView(RecyclerView recyclerView) {
      super.onAttachedToRecyclerView(recyclerView);
   }


}
