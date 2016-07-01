package com.app.pokebase.pokebase.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pokebase.pokebase.R;
import com.app.pokebase.pokebase.components.Team;
import com.app.pokebase.pokebase.holders.TeamCardViewHolder;

/**
 * @author Tyler Wong
 */
public class TeamAdapter extends RecyclerView.Adapter<TeamCardViewHolder> {

   private Team[] mTeams;
   private Context mContext;

   private final static String LAST_UPDATED = "Last Updated: ";

   public TeamAdapter(Context context, Team[] teams) {
      this.mContext = context;
      this.mTeams = teams;
   }

   @Override
   public TeamCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.team_card, parent, false);
      return new TeamCardViewHolder(view);
   }

   @Override
   public void onBindViewHolder(final TeamCardViewHolder holder, int position) {
      Team curTeam = mTeams[position];
      holder.mTitleLabel.setText(curTeam.mName);
      holder.mDescription.setText(curTeam.mDescription);
      holder.mLastUpdated.setText(LAST_UPDATED + curTeam.mLastUpdated);
      int teamSize = curTeam.mTeam.length;

      for (int index = 0; index < teamSize; index++) {
         holder.pokemonList[index].setImageResource(mContext.getResources()
               .getIdentifier("icon_" + curTeam.mTeam[index].mPokemonId,
                     "drawable", mContext.getPackageName()));
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
