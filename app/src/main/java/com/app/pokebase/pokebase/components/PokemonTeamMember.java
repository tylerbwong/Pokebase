package com.app.pokebase.pokebase.components;

/**
 * @author Tyler Wong
 */
public class PokemonTeamMember {
   public final int mMemberId;
   public final int mPokemonId;
   public final int mLevel;
   public final String mNickname;
   public String[] mMoves;

   public PokemonTeamMember(int memberId, int id, String nickname, int level, String[] moves) {
      this.mMemberId = memberId;
      this.mPokemonId = id;
      this.mLevel = level;
      this.mNickname = nickname;
      this.mMoves = moves;
   }

   public int getId() {
      return mPokemonId;
   }
}
