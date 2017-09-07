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

package me.tylerbwong.pokebase.gui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import me.tylerbwong.pokebase.R;
import me.tylerbwong.pokebase.gui.holders.PokemonTeamMemberViewHolder;
import me.tylerbwong.pokebase.model.components.PokemonTeamMember;

/**
 * @author Tyler Wong
 */
public class PokemonTeamMemberAdapter extends RecyclerView.Adapter<PokemonTeamMemberViewHolder> {
    private PokemonTeamMember[] pokemon;
    private Context context;
    private String name;
    private String description;
    private int teamId;

    public PokemonTeamMemberAdapter(Context context, PokemonTeamMember[] pokemon, int teamId,
                                    String name, String description) {
        this.context = context;
        this.pokemon = pokemon;
        this.name = name;
        this.description = description;
        this.teamId = teamId;
    }

    @Override
    public PokemonTeamMemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pokemon_team_member_card, parent, false);

        return new PokemonTeamMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PokemonTeamMemberViewHolder holder, int position) {
        PokemonTeamMember curPokemon = pokemon[position];
        holder.name.setText(curPokemon.nickname);
        holder.level.setText(String.valueOf(curPokemon.level));
        holder.lastUpdated.setText(curPokemon.lastUpdated);

        Glide.with(context)
                .load(String.format(context.getString(R.string.sprite_url), curPokemon.name.toLowerCase()))
                .into(holder.pokemon);

        String[] moves = curPokemon.moves;
        String moveList = "";
        for (String move : moves) {
            moveList += move + "\n";
        }

        holder.moveset.setText(moveList);
        holder.setPokemon(curPokemon);
        holder.setTeamId(teamId);
        holder.setTitle(name);
        holder.setDescription(description);
    }

    public void setTeam(PokemonTeamMember[] team) {
        this.pokemon = team;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (pokemon != null) {
            return pokemon.length;
        }
        return 0;
    }
}
