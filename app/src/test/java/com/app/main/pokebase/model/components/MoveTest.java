package com.app.main.pokebase.model.components;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Tyler Wong
 */
public class MoveTest {
   private Move mTestMoveOne;
   private Move mTestMoveTwo;
   private Move mTestMoveThree;

   @Before
   public void setUp() throws Exception {
      mTestMoveOne = new Move(0, "", 0, 0, 0, 0, "");
      mTestMoveTwo = new Move(1, "Tackle", 1, 20, 10, 100, "Physical");
      mTestMoveThree = new Move(2, "Quick Attack", 1, 10, 15, 100, "Physical");
   }

   @Test
   public void moveCreate() {
      assertNotNull(mTestMoveOne);
      assertNotNull(mTestMoveTwo);
      assertNotNull(mTestMoveThree);
   }

   @Test
   public void checkId() {
      assertEquals(0, mTestMoveOne.getMoveId());
      assertEquals(1, mTestMoveTwo.getMoveId());
      assertEquals(2, mTestMoveThree.getMoveId());
   }

   @Test
   public void checkName() {
      assertEquals("", mTestMoveOne.getName());
      assertEquals("Tackle", mTestMoveTwo.getName());
      assertEquals("Quick Attack", mTestMoveThree.getName());
   }

   @Test
   public void checkTypeId() {
      assertEquals(0, mTestMoveOne.getTypeId());
      assertEquals(1, mTestMoveTwo.getTypeId());
      assertEquals(1, mTestMoveThree.getTypeId());
   }

   @Test
   public void checkPower() {
      assertEquals(0, mTestMoveOne.getPower());
      assertEquals(20, mTestMoveTwo.getPower());
      assertEquals(10, mTestMoveThree.getPower());
   }

   @Test
   public void checkPp() {
      assertEquals(0, mTestMoveOne.getPp());
      assertEquals(10, mTestMoveTwo.getPp());
      assertEquals(15, mTestMoveThree.getPp());
   }

   @Test
   public void checkAccuracy() {
      assertEquals(0, mTestMoveOne.getAccuracy());
      assertEquals(100, mTestMoveTwo.getAccuracy());
      assertEquals(100, mTestMoveThree.getAccuracy());
   }

   @Test
   public void checkClassName() {
      assertEquals("", mTestMoveOne.getClassName());
      assertEquals("Physical", mTestMoveTwo.getClassName());
      assertEquals("Physical", mTestMoveThree.getClassName());
   }

   @Test
   public void checkSetAndGetTypeName() {
      mTestMoveOne.setTypeName("");
      assertEquals("", mTestMoveOne.getTypeName());
      mTestMoveTwo.setTypeName("Normal");
      assertEquals("Normal", mTestMoveTwo.getTypeName());
      mTestMoveThree.setTypeName("Normal");
      assertEquals("Normal", mTestMoveThree.getTypeName());
   }

   @Test
   public void checkSetAndGetDescription() {
      mTestMoveOne.setDescription("");
      assertEquals("", mTestMoveOne.getDescription());
      mTestMoveTwo.setDescription("Attacks the opponent.");
      assertEquals("Attacks the opponent.", mTestMoveTwo.getDescription());
      mTestMoveThree.setDescription("Quickly attacks the opponent.");
      assertEquals("Quickly attacks the opponent.", mTestMoveThree.getDescription());
   }
}
