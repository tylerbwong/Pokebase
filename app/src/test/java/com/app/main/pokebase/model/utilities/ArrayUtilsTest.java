package com.app.main.pokebase.model.utilities;

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
