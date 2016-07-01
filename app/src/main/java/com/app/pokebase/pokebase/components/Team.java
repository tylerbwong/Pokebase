package com.app.pokebase.pokebase.components;

/**
 * @author Tyler Wong
 */
public class Team {
   public final int mId;
   public final String mName;
   public final String mDescription;
   public final String mLastUpdated;
   public final PokemonTeamItem[] mTeam;

   public Team(int id, String name, String description, String lastUpdated, PokemonTeamItem[] team) {
      this.mId = id;
      this.mName = name;
      this.mDescription = description;
      this.mLastUpdated = lastUpdated;
      this.mTeam = team;
   }
}
