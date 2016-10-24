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

package com.app.main.pokebase.model.components;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Tyler Wong
 */
public class ItemTest {
   private Item mTestItemOne;
   private Item mTestItemTwo;
   private Item mTestItemThree;

   @Before
   public void setUp() throws Exception {
      mTestItemOne = new Item(0, "", "", 0);
      mTestItemTwo = new Item(1, "elixir", "Elixir", 300);
      mTestItemThree = new Item(2, "pokeball", "Pokeball", 200);
   }

   @Test
   public void itemCreate() {
      assertNotNull(mTestItemOne);
      assertNotNull(mTestItemTwo);
      assertNotNull(mTestItemThree);
   }

   @Test
   public void checkId() {
      assertEquals(0, mTestItemOne.getId());
      assertEquals(1, mTestItemTwo.getId());
      assertEquals(2, mTestItemThree.getId());
   }

   @Test
   public void checkIdentifier() {
      assertEquals("", mTestItemOne.getIdentifier());
      assertEquals("elixir", mTestItemTwo.getIdentifier());
      assertEquals("pokeball", mTestItemThree.getIdentifier());
   }

   @Test
   public void checkName() {
      assertEquals("", mTestItemOne.getName());
      assertEquals("Elixir", mTestItemTwo.getName());
      assertEquals("Pokeball", mTestItemThree.getName());
   }

   @Test
   public void checkCost() {
      assertEquals(0, mTestItemOne.getCost());
      assertEquals(300, mTestItemTwo.getCost());
      assertEquals(200, mTestItemThree.getCost());
   }

   @Test
   public void checkSetAndGetDescription() {
      mTestItemOne.setDescription("");
      assertEquals("", mTestItemOne.getDescription());
      mTestItemTwo.setDescription("Restores some PP.");
      assertEquals("Restores some PP.", mTestItemTwo.getDescription());
      mTestItemThree.setDescription("Catches a wild Pokemon.");
      assertEquals("Catches a wild Pokemon.", mTestItemThree.getDescription());
   }
}
