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
public class PokemonListItem {
    public final int id;
    public final String originalName;
    public final String name;

    public PokemonListItem(int id, String name) {
        this.id = id;
        this.name = name;
        switch (this.name) {
            case "Nidoran♂":
                this.originalName = "Nidoran-M";
                break;
            case "Nidoran♀":
                this.originalName = "Nidoran-F";
                break;
            case "Mr. Mime":
                this.originalName = "Mr-Mime";
                break;
            case "Mime Jr.":
                this.originalName = "Mime-Jr";
                break;
            default:
                this.originalName = name;
                break;
        }
    }
}
