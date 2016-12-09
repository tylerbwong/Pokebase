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
public class Move {
   private int moveId;
   private String name;
   private int typeId;
   private int power;
   private int pp;
   private int accuracy;
   private String className;
   private String description;
   private String typeName;

   public Move(int moveId, String name, int typeId, int power, int pp, int accuracy, String className) {
      this.moveId = moveId;
      this.name = name;
      this.typeId = typeId;
      this.power = power;
      this.pp = pp;
      this.accuracy = accuracy;
      this.className = className;
   }

   public int getMoveId() {
      return moveId;
   }

   public String getName() {
      return name;
   }

   public int getTypeId() {
      return typeId;
   }

   public int getPower() {
      return power;
   }

   public int getPp() {
      return pp;
   }

   public int getAccuracy() {
      return accuracy;
   }

   public String getClassName() {
      return className;
   }

   public String getDescription() {
      return description;
   }

   public String getTypeName() {
      return typeName;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public void setTypeName(String typeName) {
      this.typeName = typeName;
   }
}
