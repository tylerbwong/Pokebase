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

package com.app.main.pokebase.gui.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.main.pokebase.R;
import com.app.main.pokebase.gui.activities.TeamViewActivity;
import com.app.main.pokebase.gui.adapters.TeamAdapter;
import com.app.main.pokebase.gui.views.AnimatedRecyclerView;
import com.app.main.pokebase.model.components.Team;
import com.app.main.pokebase.model.database.DatabaseOpenHelper;
import com.github.fabtransitionactivity.SheetLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author Tyler Wong
 */
public class TeamsFragment extends Fragment implements SheetLayout.OnFabAnimationEndListener {
   @BindView(R.id.bottom_sheet)
   SheetLayout mSheetLayout;
   @BindView(R.id.fab)
   FloatingActionButton mFab;
   @BindView(R.id.team_list)
   AnimatedRecyclerView mTeamList;
   @BindView(R.id.empty_layout)
   LinearLayout mEmptyView;

   private DatabaseOpenHelper mDatabaseHelper;
   private Unbinder mUnbinder;

   private final static int REQUEST_CODE = 1;

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.teams_fragment, container, false);
      mUnbinder = ButterKnife.bind(this, view);

      mDatabaseHelper = DatabaseOpenHelper.getInstance(getContext());

      mTeamList.setHasFixedSize(true);
      LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
      layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
      mTeamList.setLayoutManager(layoutManager);

      mSheetLayout.setFab(mFab);
      mSheetLayout.setFabAnimationEndListener(this);

      ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
      if (actionBar != null) {
         actionBar.setTitle(R.string.teams);
      }

      new LoadEmptyView().execute();
      new LoadTeams().execute();

      return view;
   }

   public void refreshAdapter() {
      new LoadTeams().execute();
   }

   private void checkEmpty(Team[] teams) {
      if (teams.length == 0) {
         mEmptyView.setVisibility(View.VISIBLE);
      }
      else {
         mEmptyView.setVisibility(View.GONE);
      }
   }

   @OnClick(R.id.fab)
   public void onFabClick() {
      mSheetLayout.expandFab();
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
         mSheetLayout.contractFab();
      }
   }

   @Override
   public void onDestroyView() {
      super.onDestroyView();
      mUnbinder.unbind();
   }

   private class LoadEmptyView extends AsyncTask<Void, Void, Drawable> {
      @Override
      protected Drawable doInBackground(Void... params) {
         return ContextCompat.getDrawable(getContext(), R.drawable.no_teams);
      }

      @Override
      protected void onPostExecute(Drawable loaded) {
         super.onPostExecute(loaded);
         ImageView emptyImage = (ImageView) getActivity().findViewById(R.id.no_team);
         emptyImage.setImageDrawable(loaded);
      }
   }

   private class LoadTeams extends AsyncTask<Void, Void, Team[]> {
      @Override
      protected Team[] doInBackground(Void... params) {
         return mDatabaseHelper.queryAllTeams();
      }

      @Override
      protected void onPostExecute(Team[] loaded) {
         super.onPostExecute(loaded);
         mTeamList.setAdapter(new TeamAdapter(getContext(), loaded));
         checkEmpty(loaded);
      }
   }
}
