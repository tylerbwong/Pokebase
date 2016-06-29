package com.app.pokebase.pokebase.components;

import java.util.List;

/**
 * @author Tyler Wong
 */
public class PokemonProfile {
   private int mId;
   private String mName;
   private int mHeight;
   private int mWeight;
   private int mBaseExp;
   private String mRegion;
   private List<String> mTypes;
   private List<String> mMoves;

   public PokemonProfile(int id, String name, int height, int weight, int baseExp, String region,
                         List<String> types, List<String> moves) {
      this.mId = id;
      this.mName = name;
      this.mHeight = height;
      this.mWeight = weight;
      this.mBaseExp = baseExp;
      this.mRegion = region;
      this.mTypes = types;
      this.mMoves = moves;
   }

   public int getWeight() {
      return mWeight;
   }

   public int getId() {
      return mId;
   }

   public String getName() {
      return mName;
   }

   public int getHeight() {
      return mHeight;
   }

   public int getBaseExp() {
      return mBaseExp;
   }

   public String getRegion() {
      return mRegion;
   }

   public List<String> getTypes() {
      return mTypes;
   }

   public List<String> getMoves() {
      return mMoves;
   }
}
