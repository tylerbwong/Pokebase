package com.app.main.pokebase.model.utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;

import java.util.Hashtable;

/**
 * @author Tyler Wong
 */
public class Typefaces {
   private static final Hashtable<String, Typeface> cache = new Hashtable<>();

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
               return null;
            }
         }
         return cache.get(assetPath);
      }
   }
}
