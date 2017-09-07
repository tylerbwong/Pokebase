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

package me.tylerbwong.pokebase.model.utilities;

import java.util.Hashtable;

import io.reactivex.Observable;
import me.tylerbwong.pokebase.model.components.Move;
import me.tylerbwong.pokebase.model.components.PokemonProfile;
import me.tylerbwong.pokebase.model.database.DatabaseOpenHelper;

/**
 * @author Tyler Wong
 */
public class PokebaseCache {
    private static final Hashtable<Integer, Observable<PokemonProfile>> profileCache = new Hashtable<>();
    private static final Hashtable<String, Observable<Move>> moveCache = new Hashtable<>();

    public static Observable<PokemonProfile> getPokemonProfile(DatabaseOpenHelper databaseHelper, int pokemonId) {
        synchronized (profileCache) {
            if (!profileCache.containsKey(pokemonId)) {
                profileCache.put(pokemonId, databaseHelper.queryPokemonProfile(pokemonId));
            }
            return profileCache.get(pokemonId);
        }
    }

    public static Observable<Move> getMove(DatabaseOpenHelper databaseHelper, String moveName) {
        synchronized (moveCache) {
            if (!moveCache.containsKey(moveName)) {
                moveCache.put(moveName, databaseHelper.queryMoveInfoByName(moveName));
            }
            return moveCache.get(moveName);
        }
    }
}
