package com.app.main.pokebase.model.components;

/**
 * @author Tyler Wong
 */
public class Item {
   private int mId;
   private String mIdentifier;
   private String mName;
   private int mCost;

   public Item(int id, String identifier, String name, int cost) {
      this.mId = id;
      this.mIdentifier = identifier;
      this.mName = name;
      this.mCost = cost;
   }

   public int getId() {
      return mId;
   }

   public String getIdentifier() {
      return mIdentifier;
   }

   public String getName() {
      return mName;
   }

   public int getCost() {
      return mCost;
   }
}
