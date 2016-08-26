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
