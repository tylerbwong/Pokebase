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

package me.tylerbwong.pokebase.gui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tylerbwong.pokebase.R;
import me.tylerbwong.pokebase.gui.views.PokemonInfoView;
import me.tylerbwong.pokebase.model.database.DatabaseOpenHelper;

/**
 * @author Tyler Wong
 */
public class PokemonProfileActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
   @BindView(R.id.toolbar)
   Toolbar mToolbar;
   @BindView(R.id.previous)
   LinearLayout mPrevious;
   @BindView(R.id.next)
   LinearLayout mNext;
   @BindView(R.id.profile_image)
   ImageView mProfileImg;
   @BindView(R.id.pokemon_name)
   TextView mTitle;
   @BindView(R.id.main_title)
   TextView mMainTitle;
   @BindView(R.id.previous_label)
   TextView mPreviousLabel;
   @BindView(R.id.next_label)
   TextView mNextLabel;
   @BindView(R.id.previous_image)
   ImageView mPreviousImage;
   @BindView(R.id.next_image)
   ImageView mNextImage;
   @BindView(R.id.layout)
   CoordinatorLayout mLayout;
   @BindView(R.id.title_layout)
   LinearLayout mTitleContainer;
   @BindView(R.id.app_bar)
   AppBarLayout mAppBar;
   @BindView(R.id.info_view)
   PokemonInfoView mInfoView;

   private ActionBar mActionBar;

   private DatabaseOpenHelper mDatabaseHelper;
   private FirebaseAnalytics mAnalytics;
   private int mPokemonId;
   private String mPokemonName;
   private List<Pair<Integer, String>> mPokemonTeams;
   private boolean mIsTheTitleVisible = false;
   private boolean mIsTheTitleContainerVisible = true;

   private static final String ICON = "icon_";
   private static final String SPRITE = "sprites_";
   private static final String AUDIO = "audio_";
   private static final String DRAWABLE = "drawable";
   private static final String RAW = "raw";
   private static final String UNDO = "UNDO";
   private static final String PROFILE = "profile";
   private static final int PROFILE_IMG_ELEVATION = 40;
   private static final int DEFAULT_LEVEL = 1;
   private static final int DEFAULT_MOVE = 0;
   private static final int FIRST_POKEMON = 1;
   private static final int LAST_POKEMON = 721;
   private static final int TEN = 10;
   private static final int HUNDRED = 100;
   private static final String DOUBLE_ZERO = "00";
   private static final String ZERO = "0";

   private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
   private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
   private static final int ALPHA_ANIMATIONS_DURATION = 200;

   public static final String POKEMON_ID_KEY = "pokemon_id";
   public static final String POKEMON_NAME_KEY = "pokemon_name";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_profile);
      ButterKnife.bind(this);

      mDatabaseHelper = DatabaseOpenHelper.getInstance(this);
      mAnalytics = FirebaseAnalytics.getInstance(this);

      mProfileImg.setClipToOutline(true);
      mProfileImg.setElevation(PROFILE_IMG_ELEVATION);

      setSupportActionBar(mToolbar);
      mActionBar = getSupportActionBar();
      if (mActionBar != null) {
         mActionBar.setDisplayHomeAsUpEnabled(true);
         mActionBar.setDisplayShowTitleEnabled(false);
      }
      mAppBar.addOnOffsetChangedListener(this);
      startAlphaAnimation(mTitle, 0, View.INVISIBLE);

      SlidrConfig config = new SlidrConfig.Builder()
            .sensitivity(1f)
            .scrimColor(Color.BLACK)
            .scrimStartAlpha(0.8f)
            .scrimEndAlpha(0f)
            .velocityThreshold(2400)
            .distanceThreshold(0.25f)
            .edge(true)
            .edgeSize(0.18f)
            .build();

      Slidr.attach(this, config);

      Bundle extras = getIntent().getExtras();
      mPokemonId = extras.getInt(POKEMON_ID_KEY);
      mPokemonName = extras.getString(POKEMON_NAME_KEY);

      mInfoView.setButtonsVisible(true);
      mInfoView.loadPokemonInfo(mPokemonId);

      loadNextPrevious();

      Glide.with(PokemonProfileActivity.this)
            .load(String.format(getString(R.string.sprite_url),
                  mDatabaseHelper.queryPokemonNameById(mPokemonId).toLowerCase()))
            .fitCenter()
            .into(mProfileImg);

      String formattedName = String.format(getString(R.string.pokemon_name),
            formatId(mPokemonId), mPokemonName);
      mTitle.setText(formattedName);
      mMainTitle.setText(formattedName);

      Bundle bundle = new Bundle();
      bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, mPokemonId);
      bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, mPokemonName);
      bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, PROFILE);
      mAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
   }

   @OnClick(R.id.previous)
   public void onPrevious() {
      if (mPokemonId == FIRST_POKEMON) {
         switchPokemon(LAST_POKEMON);
      }
      else {
         switchPokemon(mPokemonId - 1);
      }
   }

   @OnClick(R.id.next)
   public void onNext() {
      if (mPokemonId == LAST_POKEMON) {
         switchPokemon(FIRST_POKEMON);
      }
      else {
         switchPokemon(mPokemonId + 1);
      }
   }

   public void closeEvolutionsDialog() {
      mInfoView.closeEvolutionsDialog();
   }

   public static String formatId(int id) {
      String result;

      if (id < TEN) {
         result = DOUBLE_ZERO + String.valueOf(id);
      }
      else if (id >= TEN && id < HUNDRED) {
         result = ZERO + String.valueOf(id);
      }
      else {
         result = String.valueOf(id);
      }

      return result;
   }

   private void switchPokemon(int pokemonId) {
      Intent intent = getIntent();
      Bundle extras = new Bundle();
      extras.putInt(POKEMON_ID_KEY, pokemonId);
      extras.putString(POKEMON_NAME_KEY, mDatabaseHelper.queryPokemonNameById(pokemonId));
      intent.putExtras(extras);
      startActivity(intent);
      finish();
   }

   private void loadNextPrevious() {
      int nextPokemonId, previousPokemonId;

      if (mPokemonId == FIRST_POKEMON) {
         previousPokemonId = LAST_POKEMON;
         nextPokemonId = mPokemonId + 1;
      }
      else if (mPokemonId == LAST_POKEMON) {
         previousPokemonId = mPokemonId - 1;
         nextPokemonId = FIRST_POKEMON;
      }
      else {
         previousPokemonId = mPokemonId - 1;
         nextPokemonId = mPokemonId + 1;
      }

      mPreviousLabel.setText(String.format(getString(R.string.hashtag_format), formatId(previousPokemonId)));
      Glide.with(this)
            .load(String.format(getString(R.string.icon_url),
                  mDatabaseHelper.queryPokemonNameById(previousPokemonId).toLowerCase()))
            .fitCenter()
            .into(mPreviousImage);
      mNextLabel.setText(String.format(getString(R.string.hashtag_format), formatId(nextPokemonId)));
      Glide.with(this)
            .load(String.format(getString(R.string.icon_url),
                  mDatabaseHelper.queryPokemonNameById(nextPokemonId).toLowerCase()))
            .fitCenter()
            .centerCrop()
            .into(mNextImage);
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case android.R.id.home:
            onBackPressed();
            break;
         case R.id.audio_action:
            playAudio();
            break;
         case R.id.add_action:
            showAddToTeamDialog();
            break;
         default:
            break;
      }
      return true;
   }

   private void playAudio() {
      final MediaPlayer player = MediaPlayer.create(this, getResources().getIdentifier(
            AUDIO + mPokemonId, RAW, getPackageName()));
      player.setOnCompletionListener(mediaPlayer -> player.release());
      player.start();
      Toast.makeText(this, getString(R.string.sound_played), Toast.LENGTH_SHORT).show();
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.menu_audio, menu);
      inflater.inflate(R.menu.menu_add, menu);
      return true;
   }

   private void showAddToTeamDialog() {
      mDatabaseHelper.queryTeamIdsAndNames()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(teams -> {
                  String[] teamNames = new String[teams.size()];
                  for (int index = 0; index < teams.size(); index++) {
                     teamNames[index] = teams.get(index).second;
                  }

                  if (!teams.isEmpty()) {
                     new LovelyChoiceDialog(PokemonProfileActivity.this)
                           .setTopColorRes(R.color.colorPrimary)
                           .setTitle(String.format(getString(R.string.add_team_title), mPokemonName))
                           .setIcon(R.drawable.ic_add_circle_outline_white_24dp)
                           .setItems(teamNames, (position, item) ->
                              mDatabaseHelper.insertTeamPokemon(teams.get(position).first, mPokemonId,
                                    mPokemonName, DEFAULT_LEVEL, DEFAULT_MOVE, DEFAULT_MOVE,
                                    DEFAULT_MOVE, DEFAULT_MOVE)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(() -> {
                                       Snackbar snackbar = Snackbar.make(mLayout, String.format(
                                               getString(R.string.add_success), mPokemonName, item), Snackbar.LENGTH_LONG)
                                               .setAction(UNDO, view -> mDatabaseHelper.deleteLastAddedPokemon());

                                       snackbar.setActionTextColor(ContextCompat.getColor(
                                               getApplicationContext(), R.color.colorPrimary));
                                       snackbar.show();
                                    })
                           )
                           .show();
                  }
                  else {
                     new LovelyChoiceDialog(PokemonProfileActivity.this)
                           .setTopColorRes(R.color.colorPrimary)
                           .setTitle(getString(R.string.no_teams_title))
                           .setMessage(String.format(getString(R.string.no_teams), mPokemonName))
                           .setIcon(R.drawable.ic_add_circle_outline_white_24dp)
                           .show();
                  }
            });
   }

   @Override
   public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
      int maxScroll = appBarLayout.getTotalScrollRange();
      float percentage = (float) Math.abs(offset) / (float) maxScroll;

      handleAlphaOnTitle(percentage);
      handleToolbarTitleVisibility(percentage);

      if (Math.abs(offset) >= mAppBar.getHeight() - TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 55, getResources().getDisplayMetrics())) {
         mActionBar.setElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
               getResources().getDisplayMetrics()));
         mActionBar.setBackgroundDrawable(new ColorDrawable(
               ContextCompat.getColor(this, R.color.colorPrimary)));
      }
      else {
         mActionBar.setElevation(0);
         mActionBar.setBackgroundDrawable(null);
      }
   }

   private void handleToolbarTitleVisibility(float percentage) {
      if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
         if (!mIsTheTitleVisible) {
            startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
            mIsTheTitleVisible = true;
         }

      }
      else {
         if (mIsTheTitleVisible) {
            startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
            mIsTheTitleVisible = false;
         }
      }
   }

   private void handleAlphaOnTitle(float percentage) {
      if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
         if (mIsTheTitleContainerVisible) {
            startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
            mIsTheTitleContainerVisible = false;
         }

      }
      else {
         if (!mIsTheTitleContainerVisible) {
            startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
            mIsTheTitleContainerVisible = true;
         }
      }
   }

   public static void startAlphaAnimation(View view, long duration, int visibility) {
      AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
            ? new AlphaAnimation(0f, 1f)
            : new AlphaAnimation(1f, 0f);

      alphaAnimation.setDuration(duration);
      alphaAnimation.setFillAfter(true);
      view.startAnimation(alphaAnimation);
   }
}
