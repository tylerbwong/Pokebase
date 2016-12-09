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

package me.tylerbwong.pokebase.model.components;

/**
 * @author Tyler Wong
 */
public class Item {
   private int id;
   private String identifier;
   private String name;
   private int cost;
   private String description;

   public Item(int id, String identifier, String name, int cost) {
      this.id = id;
      this.identifier = identifier;
      this.name = name;
      this.cost = cost;
   }

   public int getId() {
      return id;
   }

   public String getIdentifier() {
      return identifier;
   }

   public String getName() {
      return name;
   }

   public int getCost() {
      return cost;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }
}
