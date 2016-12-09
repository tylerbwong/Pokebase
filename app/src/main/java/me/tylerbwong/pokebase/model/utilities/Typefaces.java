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

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;

import java.util.Hashtable;

/**
 * @author Tyler Wong
 */
public class Typefaces {
   private static final Hashtable<String, Typeface> cache = new Hashtable<>();

   public static final String ROBOTO_PATH = "fonts/roboto-light.ttf";

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
