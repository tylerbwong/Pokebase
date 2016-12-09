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

package me.tylerbwong.pokebase.model.components;

/**
 * @author Tyler Wong
 */
public class PokemonTeamMember {
   public final int memberId;
   public final int pokemonId;
   public final int level;
   public final String name;
   public final String nickname;
   public String[] moves;
   public String lastUpdated;

   public PokemonTeamMember(int memberId, int id, String name, String nickname, int level, String[] moves,
                            String lastUpdated) {
      this.memberId = memberId;
      this.pokemonId = id;
      this.level = level;
      this.name = name;
      this.nickname = nickname;
      this.moves = moves;
      this.lastUpdated = lastUpdated;
   }

   public int getId() {
      return pokemonId;
   }
}
