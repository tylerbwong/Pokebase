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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.tylerbwong.pokebase.R;
import me.tylerbwong.pokebase.gui.activities.TeamViewActivity;

/**
 * @author Tyler Wong
 */
public class TeamCardViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.title_label)
    public TextView titleLabel;
    @BindView(R.id.description)
    public TextView description;
    @BindView(R.id.last_updated)
    public TextView lastUpdated;
    @BindView(R.id.pokemon_1)
    public ImageView pokemonOne;
    @BindView(R.id.pokemon_2)
    public ImageView pokemonTwo;
    @BindView(R.id.pokemon_3)
    public ImageView pokemonThree;
    @BindView(R.id.pokemon_4)
    public ImageView pokemonFour;
    @BindView(R.id.pokemon_5)
    public ImageView pokemonFive;
    @BindView(R.id.pokemon_6)
    public ImageView pokemonSix;

    public final View view;
    public ImageView[] pokemonList;

    public int teamId;

    private static final int NUM_POKEMON = 6;

    public TeamCardViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.view = itemView;
        teamId = 0;

        this.view.setOnClickListener(view -> {
                    Context cardContext = view.getContext();
                    Intent editorIntent = new Intent(cardContext, TeamViewActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt(TeamViewActivity.TEAM_ID_KEY, teamId);
                    extras.putBoolean(TeamViewActivity.UPDATE_KEY, true);
                    extras.putString(TeamViewActivity.TEAM_NAME, titleLabel.getText().toString());
                    extras.putString(TeamViewActivity.DESCRIPTION, description.getText().toString());
                    editorIntent.putExtras(extras);
                    view.getContext().startActivity(editorIntent);
                }
        );

        pokemonList = new ImageView[NUM_POKEMON];

        pokemonList[0] = pokemonOne;
        pokemonList[1] = pokemonTwo;
        pokemonList[2] = pokemonThree;
        pokemonList[3] = pokemonFour;
        pokemonList[4] = pokemonFive;
        pokemonList[5] = pokemonSix;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
}
