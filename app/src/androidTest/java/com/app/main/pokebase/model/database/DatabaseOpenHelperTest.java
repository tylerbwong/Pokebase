package com.app.main.pokebase.model.database;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * @author Tyler Wong
 */
public class DatabaseOpenHelperTest {
   private DatabaseOpenHelper mDatabaseHelper;

   @Before
   public void setUp() throws Exception {
      mDatabaseHelper = null;
   }

   @Test
   public void checkGetInstance() {
      assertNull(mDatabaseHelper);
   }
}
