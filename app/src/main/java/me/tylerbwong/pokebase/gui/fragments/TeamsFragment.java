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

package me.tylerbwong.pokebase.gui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.github.fabtransitionactivity.SheetLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tylerbwong.pokebase.R;
import me.tylerbwong.pokebase.gui.activities.TeamViewActivity;
import me.tylerbwong.pokebase.gui.adapters.TeamAdapter;
import me.tylerbwong.pokebase.gui.views.AnimatedRecyclerView;
import me.tylerbwong.pokebase.model.components.Team;
import me.tylerbwong.pokebase.model.database.DatabaseOpenHelper;

/**
 * @author Tyler Wong
 */
public class TeamsFragment extends Fragment implements SheetLayout.OnFabAnimationEndListener {
    @BindView(R.id.bottom_sheet)
    SheetLayout sheetLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.team_list)
    AnimatedRecyclerView teamList;
    @BindView(R.id.no_team)
    ImageView noTeam;
    @BindView(R.id.empty_layout)
    LinearLayout emptyView;

    private DatabaseOpenHelper databaseHelper;
    private Unbinder unbinder;
    private TeamAdapter adapter;

    private static final int REQUEST_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teams_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        databaseHelper = DatabaseOpenHelper.getInstance(getContext());

        teamList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        teamList.setLayoutManager(layoutManager);
        adapter = new TeamAdapter(getContext(), null);
        teamList.setAdapter(adapter);

        sheetLayout.setFab(fab);
        sheetLayout.setFabAnimationEndListener(this);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.teams);
        }

        Glide.with(this)
                .load(R.drawable.no_teams)
                .into(noTeam);

        loadTeams();

        return view;
    }

    public void loadTeams() {
        databaseHelper.queryAllTeams()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(teams -> {
                    adapter.setTeams(teams);
                    checkEmpty(teams);
                });
    }

    public void refreshAdapter() {
        loadTeams();
    }

    private void checkEmpty(Team[] teams) {
        if (teams.length == 0) {
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            emptyView.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        sheetLayout.expandFab();
    }

    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(getContext(), TeamViewActivity.class);
        Bundle extras = new Bundle();
        extras.putInt(TeamViewActivity.TEAM_ID_KEY, 0);
        extras.putBoolean(TeamViewActivity.UPDATE_KEY, false);
        intent.putExtras(extras);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            sheetLayout.contractFab();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
