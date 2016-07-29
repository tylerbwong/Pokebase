package com.app.main.pokebase.fragments;

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
import android.widget.LinearLayout;

import com.app.main.pokebase.R;
import com.app.main.pokebase.activities.TeamViewActivity;
import com.app.main.pokebase.adapters.TeamAdapter;
import com.app.main.pokebase.components.Team;
import com.app.main.pokebase.database.DatabaseOpenHelper;
import com.app.main.pokebase.views.AnimatedRecyclerView;
import com.github.fabtransitionactivity.SheetLayout;

/**
 * @author Tyler Wong
 */
public class TeamsFragment extends Fragment implements SheetLayout.OnFabAnimationEndListener {

   private SheetLayout mSheetLayout;
   private FloatingActionButton mFab;
   private AnimatedRecyclerView mTeamList;
   private LinearLayout mEmptyView;

   private TeamAdapter mTeamAdapter;
   private Team[] mTeams;
   private DatabaseOpenHelper mDatabaseHelper;

   private final static int REQUEST_CODE = 1;

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.teams_fragment, container, false);

      mSheetLayout = (SheetLayout) view.findViewById(R.id.bottom_sheet);
      mTeamList = (AnimatedRecyclerView) view.findViewById(R.id.team_list);
      mFab = (FloatingActionButton) view.findViewById(R.id.fab);
      mEmptyView = (LinearLayout) view.findViewById(R.id.empty_layout);

      mTeamList.setHasFixedSize(true);

      mFab.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            onFabClick();
         }
      });

      mSheetLayout.setFab(mFab);
      mSheetLayout.setFabAnimationEndListener(this);

      final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
      if (actionBar != null) {
         actionBar.setTitle(R.string.teams);
      }

      mDatabaseHelper = DatabaseOpenHelper.getInstance(getContext());

      mTeams = mDatabaseHelper.queryAllTeams();

      LinearLayoutManager llm = new LinearLayoutManager(getContext());
      llm.setOrientation(LinearLayoutManager.VERTICAL);
      mTeamList.setLayoutManager(llm);
      mTeamAdapter = new TeamAdapter(getContext(), mTeams);
      mTeamList.setAdapter(mTeamAdapter);

      checkEmpty();

      return view;
   }

   public void refreshAdapter() {
      mTeams = mDatabaseHelper.queryAllTeams();
      mTeamAdapter = new TeamAdapter(getContext(), mTeams);
      mTeamList.setAdapter(mTeamAdapter);

      checkEmpty();
   }

   private void checkEmpty() {
      if (mTeams.length == 0) {
         mTeamList.setVisibility(View.GONE);
         mEmptyView.setVisibility(View.VISIBLE);
      }
      else {
         mTeamList.setVisibility(View.VISIBLE);
         mEmptyView.setVisibility(View.GONE);
      }
   }

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
}
