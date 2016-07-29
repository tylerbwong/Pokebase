package com.app.main.pokebase.model.components;

/**
 * @author Tyler Wong
 */
public class PokemonTeamMember {
   public final int mMemberId;
   public final int mPokemonId;
   public final int mLevel;
   public final String mNickname;
   public String[] mMoves;
   public String mLastUpdated;

   public PokemonTeamMember(int memberId, int id, String nickname, int level, String[] moves,
                            String lastUpdated) {
      this.mMemberId = memberId;
      this.mPokemonId = id;
      this.mLevel = level;
      this.mNickname = nickname;
      this.mMoves = moves;
      this.mLastUpdated = lastUpdated;
   }

   public int getId() {
      return mPokemonId;
   }
}
