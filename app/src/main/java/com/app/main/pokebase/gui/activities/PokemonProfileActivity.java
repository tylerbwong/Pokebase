package com.app.main.pokebase.gui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
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

import com.app.main.pokebase.R;
import com.app.main.pokebase.gui.views.PokemonInfoView;
import com.app.main.pokebase.model.components.PokemonProfile;
import com.app.main.pokebase.model.database.DatabaseOpenHelper;
import com.app.main.pokebase.model.utilities.PokebaseCache;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Brittany Berlanga
 */
public class PokemonProfileActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
   @BindView(R.id.toolbar) Toolbar mToolbar;
   @BindView(R.id.previous) LinearLayout mPrevious;
   @BindView(R.id.next) LinearLayout mNext;
   @BindView(R.id.profile_image) ImageView mProfileImg;
   @BindView(R.id.pokemon_name) TextView mTitle;
   @BindView(R.id.main_title) TextView mMainTitle;
   @BindView(R.id.previous_label) TextView mPreviousLabel;
   @BindView(R.id.next_label) TextView mNextLabel;
   @BindView(R.id.previous_image) ImageView mPreviousImage;
   @BindView(R.id.next_image) ImageView mNextImage;
   @BindView(R.id.layout) CoordinatorLayout mLayout;
   @BindView(R.id.title_layout) LinearLayout mTitleContainer;
   @BindView(R.id.app_bar) AppBarLayout mAppBar;
   @BindView(R.id.info_view) PokemonInfoView mInfoView;

   private ActionBar mActionBar;

   private DatabaseOpenHelper mDatabaseHelper;
   private int mPokemonId;
   private String mPokemonName;
   private boolean mIsTheTitleVisible = false;
   private boolean mIsTheTitleContainerVisible = true;

   public final static String POKEMON_ID_KEY = "pokemon_id";
   private final static String ICON = "icon_";
   private final static String SPRITE = "sprites_";
   private final static String AUDIO = "audio_";
   private final static String DRAWABLE = "drawable";
   private final static String RAW = "raw";
   private final static String UNDO = "UNDO";
   private final static int PROFILE_IMG_ELEVATION = 40;
   private final static int DEFAULT_LEVEL = 1;
   private final static int DEFAULT_MOVE = 0;
   private final static int FIRST_POKEMON = 1;
   private final static int LAST_POKEMON = 721;
   private final static int TEN = 10;
   private final static int HUNDRED = 100;
   private final static String DOUBLE_ZERO = "00";
   private final static String ZERO = "0";

   private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
   private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
   private static final int ALPHA_ANIMATIONS_DURATION = 200;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_profile);
      ButterKnife.bind(this);

      mDatabaseHelper = DatabaseOpenHelper.getInstance(this);

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

      new LoadPokemonProfile().execute();
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

   private class LoadPokemonProfile extends AsyncTask<Void, Void, PokemonProfile> {
      @Override
      protected PokemonProfile doInBackground(Void... params) {
         Bundle extras = getIntent().getExtras();
         return PokebaseCache.getPokemonProfile(mDatabaseHelper, extras.getInt(POKEMON_ID_KEY));
      }

      @Override
      protected void onPostExecute(PokemonProfile result) {
         super.onPostExecute(result);

         mPokemonId = result.getId();
         mPokemonName = result.getName();

         mInfoView.loadPokemonInfo(mPokemonId);
         mInfoView.setButtonsVisible(true);

         loadNextPrevious();

         int imageResourceId = getResources().getIdentifier(SPRITE + mPokemonId,
               DRAWABLE, getPackageName());
         mProfileImg.setImageResource(imageResourceId);

         String formattedName = String.format(getString(R.string.pokemon_name),
               formatId(mPokemonId), mPokemonName);
         mTitle.setText(formattedName);
         mMainTitle.setText(formattedName);
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
      extras.putInt(PokemonProfileActivity.POKEMON_ID_KEY, pokemonId);
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
      mPreviousImage.setImageResource(getResources().getIdentifier(ICON + previousPokemonId,
            DRAWABLE, getPackageName()));
      mNextLabel.setText(String.format(getString(R.string.hashtag_format), formatId(nextPokemonId)));
      mNextImage.setImageResource(getResources().getIdentifier(ICON + nextPokemonId,
            DRAWABLE, getPackageName()));
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
            showAddToTeamDialog(mDatabaseHelper.queryTeamIdsAndNames());
            break;
         default:
            break;
      }
      return true;
   }

   private void playAudio() {
      final MediaPlayer player = MediaPlayer.create(this, getResources().getIdentifier(
            AUDIO + mPokemonId, RAW, getPackageName()));
      player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
         @Override
         public void onCompletion(MediaPlayer mediaPlayer) {
            player.release();
         }
      });
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

   private void showAddToTeamDialog(final List<Pair<Integer, String>> teams) {
      String[] teamNames = new String[teams.size()];
      for (int index = 0; index < teams.size(); index++) {
         teamNames[index] = teams.get(index).second;
      }

      if (!teams.isEmpty()) {
         new LovelyChoiceDialog(this)
               .setTopColorRes(R.color.colorPrimary)
               .setTitle(String.format(getString(R.string.add_team_title), mPokemonName))
               .setIcon(R.drawable.ic_add_circle_outline_white_24dp)
               .setItems(teamNames, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                  @Override
                  public void onItemSelected(int position, String item) {
                     mDatabaseHelper.insertTeamPokemon(teams.get(position).first, mPokemonId,
                           mPokemonName, DEFAULT_LEVEL, DEFAULT_MOVE, DEFAULT_MOVE,
                           DEFAULT_MOVE, DEFAULT_MOVE);
                     Snackbar snackbar = Snackbar.make(mLayout, String.format(
                           getString(R.string.add_success), mPokemonName, item), Snackbar.LENGTH_LONG)
                           .setAction(UNDO, new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                 mDatabaseHelper.deleteLastAddedPokemon();
                              }
                           });
                     snackbar.setActionTextColor(ContextCompat.getColor(
                           getApplicationContext(), R.color.colorPrimary));
                     snackbar.show();
                  }
               })
               .show();
      }
      else {
         new LovelyChoiceDialog(this)
               .setTopColorRes(R.color.colorPrimary)
               .setTitle(getString(R.string.no_teams_title))
               .setMessage(String.format(getString(R.string.no_teams), mPokemonName))
               .setIcon(R.drawable.ic_add_circle_outline_white_24dp)
               .show();
      }
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
