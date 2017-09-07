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
public class PokemonProfile {
    private int id;
    private String name;
    private int height;
    private int weight;
    private int baseExp;
    private String region;
    private String[] types;
    private String[] moves;
    private PokemonListItem[] evolutions;
    private String description;
    private float[] baseStats;

    public PokemonProfile(int id, String name, int height, int weight, int baseExp, String region,
                          String[] types, String[] moves, PokemonListItem[] evolutions,
                          String description, float[] baseStats) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.baseExp = baseExp;
        this.region = region;
        this.types = types;
        this.moves = moves;
        this.evolutions = evolutions;
        this.description = description;
        this.baseStats = baseStats;
    }

    public int getWeight() {
        return weight;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public int getBaseExp() {
        return baseExp;
    }

    public String getRegion() {
        return region;
    }

    public String[] getTypes() {
        return types;
    }

    public String[] getMoves() {
        return moves;
    }

    public PokemonListItem[] getEvolutions() {
        return evolutions;
    }

    public String getDescription() {
        return description;
    }

    public float[] getBaseStats() {
        return baseStats;
    }
}
