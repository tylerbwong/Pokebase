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
public class Team {
   public final int mId;
   public final String mName;
   public final String mDescription;
   public final String mLastUpdated;
   public final PokemonTeamItem[] mTeam;

   public Team(int id, String name, String description, String lastUpdated, PokemonTeamItem[] team) {
      this.mId = id;
      this.mName = name;
      this.mDescription = description;
      this.mLastUpdated = lastUpdated;
      this.mTeam = team;
   }
}
