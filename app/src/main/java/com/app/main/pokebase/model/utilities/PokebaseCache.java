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
