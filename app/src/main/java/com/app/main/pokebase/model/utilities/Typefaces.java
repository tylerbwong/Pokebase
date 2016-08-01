package com.app.main.pokebase.model.utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Hashtable;

/**
 * @author Tyler Wong
 */
public class Typefaces {
   private static final Hashtable<String, Typeface> cache = new Hashtable<>();

   private static final String TAG = "Typefaces";
   private final static String ERROR = "Could not getPokemonProfile typeface.";
   public final static String ROBOTO_PATH = "fonts/roboto-light.ttf";

   @Nullable
   public static Typeface get(Context context, String assetPath) {
      synchronized (cache) {
         if (!cache.containsKey(assetPath)) {
            try {
               Typeface typeface = Typeface.createFromAsset(context.getAssets(), assetPath);
               cache.put(assetPath, typeface);
            }
            catch (Exception e) {
               Log.e(TAG, ERROR);
               return null;
            }
         }
         return cache.get(assetPath);
      }
   }
}
