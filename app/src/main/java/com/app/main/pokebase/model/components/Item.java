package com.app.main.pokebase.model.components;

/**
 * @author Tyler Wong
 */
public class Item {
   private int mId;
   private String mIdentifier;
   private String mName;
   private int mCost;
   private String mDescription;

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

   public String getDescription() {
      return mDescription;
   }

   public void setDescription(String description) {
      this.mDescription = description;
   }
}
