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

package com.app.main.pokebase.gui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.main.pokebase.R;
import com.app.main.pokebase.gui.adapters.TextViewSpinnerAdapter;
import com.app.main.pokebase.gui.views.PokemonInfoView;
import com.app.main.pokebase.model.database.DatabaseOpenHelper;
import com.app.main.pokebase.model.utilities.ArrayUtils;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * @author Tyler Wong
 */
public class PokemonEditorActivity extends AppCompatActivity {
   @BindView(R.id.toolbar)
   Toolbar mToolbar;
   @BindView(R.id.profile_image)
   ImageView mProfileImg;
   @BindView(R.id.nickname_input)
   TextInputEditText mNicknameInput;
   @BindView(R.id.level_spinner)
   Spinner mLevelSpinner;
   @BindViews({R.id.move_one_spinner, R.id.move_two_spinner, R.id.move_three_spinner,
         R.id.move_four_spinner})
   Spinner[] mMoveSpinners;

   private int mTeamId;
   private int mMemberId;
   private int mPokemonId;
   private int mLevel;
   private String mNickname;
   private String mMoveOne;
   private String mMoveTwo;
   private String mMoveThree;
   private String mMoveFour;
   private String mTitle;
   private String mDescription;
   private DatabaseOpenHelper mDatabaseHelper;
   private PokemonInfoView mInfoView;
   private LovelyCustomDialog mProfileDialog;

   private final static int PROFILE_IMG_ELEVATION = 40;
   private final static int MIN_LEVEL = 1;
   private final static int MAX_LEVEL = 100;
   public final static String TEAM_ID = "teamId";
   public final static String TITLE = "title";
   public final static String DESCRIPTION = "description";
   public final static String MEMBER_ID = "memberId";
   public final static String POKEMON_ID = "pokemonId";
   public final static String LEVEL = "level";
   public final static String NICKNAME = "nickname";
   public final static String MOVE_ONE = "moveOne";
   public final static String MOVE_TWO = "moveTwo";
   public final static String MOVE_THREE = "moveThree";
   public final static String MOVE_FOUR = "moveFour";
   private final static String SPRITE = "sprites_";
   private final static String DRAWABLE = "drawable";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_pokemon_editor);
      ButterKnife.bind(this);

      mDatabaseHelper = DatabaseOpenHelper.getInstance(this);
      mInfoView = new PokemonInfoView(this);

      new LoadPokemonTeamMember(this, savedInstanceState).execute();
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.menu_profile, menu);
      inflater.inflate(R.menu.menu_trash, menu);
      inflater.inflate(R.menu.menu_submit, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case android.R.id.home:
            onBackPressed();
            break;
         case R.id.profile_action:
            showProfile();
            break;
         case R.id.delete_action:
            showDeleteDialog();
            break;
         case R.id.submit_action:
            updatePokemon();
            break;
         default:
            break;
      }
      return true;
   }

   private void showDeleteDialog() {
      new LovelyStandardDialog(this)
            .setIcon(R.drawable.ic_info_white_24dp)
            .setTitle(R.string.delete_pokemon)
            .setMessage(String.format(getString(R.string.delete_team_prompt),
                  mNicknameInput.getText().toString()))
            .setCancelable(true)
            .setPositiveButton(R.string.yes, v -> deletePokemon())
            .setNegativeButton(R.string.no, null)
            .setTopColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .show();
   }

   private void deletePokemon() {
      mDatabaseHelper.deleteTeamPokemonSingle(mMemberId);

      Toast.makeText(this, String.format(getString(R.string.team_deleted),
            mNicknameInput.getText().toString()), Toast.LENGTH_SHORT).show();
      backToTeamView();
   }

   private void updatePokemon() {
      mDatabaseHelper.updateTeamPokemon(mMemberId, mTeamId, mNicknameInput.getText().toString(),
            Integer.parseInt(String.valueOf(mLevelSpinner.getSelectedItem())),
            String.valueOf(mMoveSpinners[0].getSelectedItem()),
            String.valueOf(mMoveSpinners[1].getSelectedItem()),
            String.valueOf(mMoveSpinners[2].getSelectedItem()),
            String.valueOf(mMoveSpinners[3].getSelectedItem()));

      Toast.makeText(this, String.format(getString(R.string.team_update),
            mNicknameInput.getText().toString()), Toast.LENGTH_SHORT).show();
      backToTeamView();
   }

   private void backToTeamView() {
      Intent teamIntent = new Intent(this, TeamViewActivity.class);
      Bundle extras = new Bundle();
      extras.putInt(TeamViewActivity.TEAM_ID_KEY, mTeamId);
      extras.putBoolean(TeamViewActivity.UPDATE_KEY, true);
      extras.putString(TeamViewActivity.TEAM_NAME, mTitle);
      extras.putString(TeamViewActivity.DESCRIPTION, mDescription);
      teamIntent.putExtras(extras);
      startActivity(teamIntent);
   }

   private void showBackDialog() {
      new LovelyStandardDialog(this)
            .setIcon(R.drawable.ic_info_white_24dp)
            .setTitle(R.string.go_back)
            .setMessage(R.string.back_prompt)
            .setCancelable(true)
            .setPositiveButton(R.string.yes, v -> backToTeamView())
            .setNegativeButton(R.string.no, null)
            .setTopColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .show();
   }

   private void showProfile() {
      mProfileDialog.show();
   }

   @Override
   public void onBackPressed() {
      showBackDialog();
   }

   @Override
   protected void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
      outState.putInt(MOVE_ONE, mMoveSpinners[0].getSelectedItemPosition());
      outState.putInt(MOVE_TWO, mMoveSpinners[1].getSelectedItemPosition());
      outState.putInt(MOVE_THREE, mMoveSpinners[2].getSelectedItemPosition());
      outState.putInt(MOVE_FOUR, mMoveSpinners[3].getSelectedItemPosition());
      outState.putInt(LEVEL, mLevelSpinner.getSelectedItemPosition());
      outState.putString(NICKNAME, mNicknameInput.getText().toString());
   }

   private class LoadPokemonTeamMember extends AsyncTask<Void, Void, String[]> {
      private Context mContext;
      private Bundle mSavedInstanceState;

      public LoadPokemonTeamMember(Context context, Bundle savedInstanceState) {
         this.mContext = context;
         this.mSavedInstanceState = savedInstanceState;
      }

      @Override
      protected String[] doInBackground(Void... params) {
         Intent intent = getIntent();
         mTeamId = intent.getIntExtra(TEAM_ID, 0);
         mTitle = intent.getStringExtra(TITLE);
         mDescription = intent.getStringExtra(DESCRIPTION);
         mMemberId = intent.getIntExtra(MEMBER_ID, 0);
         mPokemonId = intent.getIntExtra(POKEMON_ID, 0);
         mLevel = intent.getIntExtra(LEVEL, 0);
         mNickname = intent.getStringExtra(NICKNAME);
         mMoveOne = intent.getStringExtra(MOVE_ONE);
         mMoveTwo = intent.getStringExtra(MOVE_TWO);
         mMoveThree = intent.getStringExtra(MOVE_THREE);
         mMoveFour = intent.getStringExtra(MOVE_FOUR);

         return mDatabaseHelper.queryPokemonMoves(mPokemonId);
      }

      @Override
      protected void onPostExecute(String[] result) {
         super.onPostExecute(result);

         setSupportActionBar(mToolbar);
         final ActionBar actionBar = getSupportActionBar();
         if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
         }

         mProfileImg.setClipToOutline(true);
         mProfileImg.setElevation(PROFILE_IMG_ELEVATION);

         if (actionBar != null) {
            actionBar.setTitle(mNickname);
         }

         mInfoView.loadPokemonInfo(mPokemonId);
         mInfoView.setButtonsVisible(false);

         int imageResourceId = getResources().getIdentifier(SPRITE + mPokemonId, DRAWABLE, getPackageName());
         mProfileImg.setImageResource(imageResourceId);

         mNicknameInput.setText(mNickname);

         mNicknameInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence sequence, int start, int before, int count) {
               if (actionBar != null) {
                  actionBar.setTitle(sequence.toString());
               }
            }

            @Override
            public void beforeTextChanged(CharSequence sequence, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
         });

         String[] levels = new String[MAX_LEVEL];
         for (int lvl = MIN_LEVEL; lvl <= levels.length; lvl++) {
            levels[lvl - MIN_LEVEL] = String.valueOf(lvl);
         }

         mLevelSpinner.setAdapter(new TextViewSpinnerAdapter(mContext, levels, 18));
         mLevelSpinner.setSelection(mLevel - 1);

         mMoveSpinners[0].setAdapter(new TextViewSpinnerAdapter(mContext, result));
         mMoveSpinners[0].setSelection(ArrayUtils.indexOf(result, mMoveOne));
         mMoveSpinners[1].setAdapter(new TextViewSpinnerAdapter(mContext, result));
         mMoveSpinners[1].setSelection(ArrayUtils.indexOf(result, mMoveTwo));
         mMoveSpinners[2].setAdapter(new TextViewSpinnerAdapter(mContext, result));
         mMoveSpinners[2].setSelection(ArrayUtils.indexOf(result, mMoveThree));
         mMoveSpinners[3].setAdapter(new TextViewSpinnerAdapter(mContext, result));
         mMoveSpinners[3].setSelection(ArrayUtils.indexOf(result, mMoveFour));

         mProfileDialog = new LovelyCustomDialog(mContext)
               .setView(mInfoView)
               .setIcon(mProfileImg.getDrawable())
               .setTitle(mNicknameInput.getText().toString())
               .setCancelable(true)
               .setTopColor(ContextCompat.getColor(mContext, R.color.colorPrimary));

         if (mSavedInstanceState != null) {
            mMoveSpinners[0].setSelection(mSavedInstanceState.getInt(MOVE_ONE, 0));
            mMoveSpinners[1].setSelection(mSavedInstanceState.getInt(MOVE_TWO, 0));
            mMoveSpinners[2].setSelection(mSavedInstanceState.getInt(MOVE_THREE, 0));
            mMoveSpinners[3].setSelection(mSavedInstanceState.getInt(MOVE_FOUR, 0));
            mLevelSpinner.setSelection(mSavedInstanceState.getInt(LEVEL, 0));
            mNicknameInput.setText(mSavedInstanceState.getString(NICKNAME, ""));
         }
      }
   }
}
