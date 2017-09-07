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

package me.tylerbwong.pokebase.gui.holders;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.tylerbwong.pokebase.R;
import me.tylerbwong.pokebase.gui.activities.PokemonEditorActivity;
import me.tylerbwong.pokebase.gui.activities.TeamViewActivity;
import me.tylerbwong.pokebase.model.components.PokemonTeamMember;

/**
 * @author Tyler Wong
 */
public class PokemonTeamMemberViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.pokemon)
    public ImageView pokemon;
    @BindView(R.id.name)
    public TextView name;
    @BindView(R.id.level)
    public TextView level;
    @BindView(R.id.last_updated)
    public TextView lastUpdated;
    @BindView(R.id.moveset)
    public TextView moveset;

    public final View view;

    private PokemonTeamMember pokemonData;
    private String title;
    private String description;
    private int teamId;

    public PokemonTeamMemberViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.view = itemView;

        view.setOnClickListener(view -> {
                    Context cardContext = view.getContext();
                    Intent editorIntent = new Intent(cardContext, PokemonEditorActivity.class);
                    String transitionName = cardContext.getString(R.string.shared_transition);
                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                            (TeamViewActivity) cardContext, pokemon, transitionName);
                    editorIntent.putExtra(PokemonEditorActivity.POKEMON_ID, pokemonData.pokemonId);
                    editorIntent.putExtra(PokemonEditorActivity.TEAM_ID, teamId);
                    editorIntent.putExtra(PokemonEditorActivity.TITLE, title);
                    editorIntent.putExtra(PokemonEditorActivity.DESCRIPTION, description);
                    editorIntent.putExtra(PokemonEditorActivity.MEMBER_ID, pokemonData.memberId);
                    editorIntent.putExtra(PokemonEditorActivity.LEVEL, pokemonData.level);
                    editorIntent.putExtra(PokemonEditorActivity.NAME, pokemonData.name);
                    editorIntent.putExtra(PokemonEditorActivity.NICKNAME, pokemonData.nickname);
                    editorIntent.putExtra(PokemonEditorActivity.MOVE_ONE, pokemonData.moves[0]);
                    editorIntent.putExtra(PokemonEditorActivity.MOVE_TWO, pokemonData.moves[1]);
                    editorIntent.putExtra(PokemonEditorActivity.MOVE_THREE, pokemonData.moves[2]);
                    editorIntent.putExtra(PokemonEditorActivity.MOVE_FOUR, pokemonData.moves[3]);

                    view.getContext().startActivity(editorIntent, transitionActivityOptions.toBundle());
                }
        );
    }

    public void setPokemon(PokemonTeamMember member) {
        pokemonData = member;
    }

    public void setTitle(String name) {
        title = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
}