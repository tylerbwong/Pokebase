package com.app.main.pokebase.model.components;

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
   private String[] mTypes;
   private String[] mMoves;
   private PokemonListItem[] mEvolutions;
   private String mDescription;
   private float[] mBaseStats;

   public PokemonProfile(int id, String name, int height, int weight, int baseExp, String region,
                         String[] types, String[] moves, PokemonListItem[] evolutions,
                         String description, float[] baseStats) {
      this.mId = id;
      this.mName = name;
      this.mHeight = height;
      this.mWeight = weight;
      this.mBaseExp = baseExp;
      this.mRegion = region;
      this.mTypes = types;
      this.mMoves = moves;
      this.mEvolutions = evolutions;
      this.mDescription = description;
      this.mBaseStats = baseStats;
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

   public String[] getTypes() {
      return mTypes;
   }

   public String[] getMoves() {
      return mMoves;
   }

   public PokemonListItem[] getEvolutions() {
      return mEvolutions;
   }

   public String getDescription() {
      return mDescription;
   }

   public float[] getBaseStats() {
      return mBaseStats;
   }
}
