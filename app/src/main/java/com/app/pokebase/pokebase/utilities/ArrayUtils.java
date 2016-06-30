package com.app.pokebase.pokebase.utilities;

/**
 * @author Tyler Wong
 */
public final class ArrayUtils {
   private final static int NO_RESULT = -1;

   public static int indexOf(String[] array, String key) {
      for (int index = 0; index < array.length; index++) {
         if (array[index].equals(key)) {
            return index;
         }
      }
      return NO_RESULT;
   }
}
