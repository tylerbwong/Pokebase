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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Tyler Wong
 */
public class ArrayUtilsTest {
   private String[] mTestStringArray;

   @Before
   public void setUp() throws Exception {
      mTestStringArray = new String[] {"Aerial Ace", "Quick Attack", "Thundershock"};
   }

   @Test
   public void checkIndexOf() {
      assertEquals(1, ArrayUtils.indexOf(mTestStringArray, "Quick Attack"));
      assertEquals(0, ArrayUtils.indexOf(mTestStringArray, "Aerial Ace"));
      assertEquals(2, ArrayUtils.indexOf(mTestStringArray, "Thundershock"));
   }
}
