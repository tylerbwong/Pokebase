package com.app.pokebase.pokebase.components;

/**
 * @author Tyler Wong
 */
public class Team {
   public final int mId;
   public final String mName;
   public final String mDescription;
   public final PokemonTeamItem[] mTeam;

   public Team(int id, String name, String description, PokemonTeamItem[] team) {
      this.mId = id;
      this.mName = name;
      this.mDescription = description;
      this.mTeam = team;
   }
}
