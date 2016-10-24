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
