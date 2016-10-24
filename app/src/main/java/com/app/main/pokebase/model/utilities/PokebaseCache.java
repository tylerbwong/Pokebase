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

package com.app.main.pokebase.model.utilities;

import com.app.main.pokebase.model.components.Move;
import com.app.main.pokebase.model.components.PokemonProfile;
import com.app.main.pokebase.model.database.DatabaseOpenHelper;

import java.util.Hashtable;

/**
 * @author Tyler Wong
 */
public class PokebaseCache {
   private final static Hashtable<Integer, PokemonProfile> profileCache = new Hashtable<>();
   private final static Hashtable<String, Move> moveCache = new Hashtable<>();

   public static PokemonProfile getPokemonProfile(DatabaseOpenHelper databaseHelper, int pokemonId) {
      synchronized (profileCache) {
         if (!profileCache.containsKey(pokemonId)) {
            profileCache.put(pokemonId, databaseHelper.queryPokemonProfile(pokemonId));
         }
         return profileCache.get(pokemonId);
      }
   }

   public static Move getMove(DatabaseOpenHelper databaseHelper, String moveName) {
      synchronized (moveCache) {
         if (!moveCache.containsKey(moveName)) {
            moveCache.put(moveName, databaseHelper.queryMoveInfoByName(moveName));
         }
         return moveCache.get(moveName);
      }
   }
}
