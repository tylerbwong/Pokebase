package com.app.pokebase.pokebase.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pokebase.pokebase.R;
import com.app.pokebase.pokebase.components.PokemonTeamMember;
import com.app.pokebase.pokebase.holders.PokemonTeamMemberViewHolder;

import java.util.List;

/**
 * @author Tyler Wong
 */
public class PokemonTeamMemberAdapter extends RecyclerView.Adapter<PokemonTeamMemberViewHolder> {

   private List<PokemonTeamMember> mPokemon;
   private Context mContext;
   private String mName;
   private String mDescription;
   private int mTeamId;

   public PokemonTeamMemberAdapter(Context context, List<PokemonTeamMember> pokemon, int teamId,
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
      PokemonTeamMember curPokemon = mPokemon.get(position);
      holder.mName.setText(curPokemon.mNickname);
      holder.mLevel.setText(String.valueOf(curPokemon.mLevel));
      int imageResourceId = mContext.getResources().getIdentifier("sprites_" +
            curPokemon.mPokemonId, "drawable", mContext.getPackageName());
      holder.mPokemon.setImageResource(imageResourceId);
      List<String> moves = curPokemon.mMoves;
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

   @Override
   public int getItemCount() {
      return mPokemon.size();
   }
}
