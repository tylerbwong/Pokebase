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
