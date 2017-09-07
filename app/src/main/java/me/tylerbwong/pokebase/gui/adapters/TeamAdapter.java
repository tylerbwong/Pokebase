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
import me.tylerbwong.pokebase.gui.holders.TeamCardViewHolder;
import me.tylerbwong.pokebase.model.components.Team;

/**
 * @author Tyler Wong
 */
public class TeamAdapter extends RecyclerView.Adapter {
    private Team[] teams;
    private Context context;

    public TeamAdapter(Context context, Team[] teams) {
        this.context = context;
        this.teams = teams;
    }

    @Override
    public TeamCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.team_card, parent, false);
        return new TeamCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        TeamCardViewHolder holder = (TeamCardViewHolder) viewHolder;
        Team curTeam = teams[position];
        holder.titleLabel.setText(curTeam.name);
        holder.description.setText(curTeam.description);
        holder.lastUpdated.setText(curTeam.lastUpdated);
        int teamSize = curTeam.team.length;

        if (teamSize > 0) {
            for (int index = 0; index < teamSize; index++) {
                Glide.with(context)
                        .load(String.format(context.getString(R.string.sprite_url),
                                curTeam.team[index].name.toLowerCase()))
                        .into(holder.pokemonList[index]);
            }
        }
        else {
            for (int index = 0; index < teamSize; index++) {
                Glide.with(context)
                        .clear(holder.pokemonList[index]);
            }
        }

        holder.setTeamId(curTeam.id);
    }

    public void setTeams(Team[] teams) {
        this.teams = teams;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (teams != null) {
            return teams.length;
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
