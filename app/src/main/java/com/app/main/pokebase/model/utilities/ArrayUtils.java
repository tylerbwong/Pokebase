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
