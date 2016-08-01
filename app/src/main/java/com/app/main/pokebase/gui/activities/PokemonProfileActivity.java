package com.app.main.pokebase.gui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
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
import com.app.main.pokebase.gui.adapters.MoveListAdapter;
import com.app.main.pokebase.gui.adapters.PokemonListAdapter;
import com.app.main.pokebase.gui.views.AnimatedRecyclerView;
import com.app.main.pokebase.model.components.PokemonListItem;
import com.app.main.pokebase.model.components.PokemonProfile;
import com.app.main.pokebase.model.database.DatabaseOpenHelper;
import com.app.main.pokebase.model.utilities.PokebaseCache;
import com.db.chart.model.BarSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.BarChartView;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.BounceEase;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import java.util.List;
import java.util.Locale;

/**
 * @author Brittany Berlanga
 */
public class PokemonProfileActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
   private int mPokemonId;
   private String mPokemonName;
   private Toolbar mToolbar;
   private LinearLayout mPrevious;
   private LinearLayout mNext;
   private ImageView mProfileImg;
   private TextView mTypeOneView;
   private TextView mTypeTwoView;
   private TextView mRegionView;
   private TextView mHeightView;
   private TextView mWeightView;
   private TextView mExpView;
   private TextView mTitle;
   private TextView mMainTitle;
   private TextView mPreviousLabel;
   private TextView mNextLabel;
   private ImageView mPreviousImage;
   private ImageView mNextImage;
   private TextView[] mStats;
   private TextView mDescription;
   private CoordinatorLayout mLayout;
   private LovelyCustomDialog mMovesDialog;
   private LovelyCustomDialog mEvolutionsDialog;
   private LinearLayout mTitleContainer;
   private BarChartView mBarChart;
   private AppBarLayout mAppBar;
   private ActionBar mActionBar;
   private DatabaseOpenHelper mDatabaseHelper;
   private AnimatedRecyclerView mEvolutionsList;

   private PokemonListItem[] mEvolutions;
   private boolean mIsTheTitleVisible = false;
   private boolean mIsTheTitleContainerVisible = true;

   public final static String POKEMON_ID_KEY = "pokemon_id";
   private final static String ICON = "icon_";
   private final static String SPRITE = "sprites_";
   private final static String AUDIO = "audio_";
   private final static String TYPE = "type";
   private final static String COLOR = "color";
   private final static String DRAWABLE = "drawable";
   private final static String RAW = "raw";
   private final static double FT_PER_DM = 0.32808399;
   private final static double LB_PER_HG = 0.22046226218;
   private final static int KG_PER_HG = 10;
   private final static int IN_PER_FT = 12;
   private final static int DM_PER_M = 10;
   private final static int PROFILE_IMG_ELEVATION = 40;
   private final static int DEFAULT_LEVEL = 1;
   private final static int DEFAULT_MOVE = 0;
   private final static int FIRST_POKEMON = 1;
   private final static int LAST_POKEMON = 721;
   private final static String[] STATS =
         {"HP", "Attack", "Defense", "Sp. Attack", "Sp. Defense", "Speed"};

   private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
   private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
   private static final int ALPHA_ANIMATIONS_DURATION = 200;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      mDatabaseHelper = DatabaseOpenHelper.getInstance(this);

      setContentView(R.layout.activity_profile);
      mToolbar = (Toolbar) findViewById(R.id.toolbar);
      mPrevious = (LinearLayout) findViewById(R.id.previous);
      mNext = (LinearLayout) findViewById(R.id.next);
      mProfileImg = (ImageView) findViewById(R.id.profile_image);
      mProfileImg.setClipToOutline(true);
      mProfileImg.setElevation(PROFILE_IMG_ELEVATION);
      mTypeOneView = (TextView) findViewById(R.id.type_one);
      mTypeTwoView = (TextView) findViewById(R.id.type_two);
      mRegionView = (TextView) findViewById(R.id.region);
      mHeightView = (TextView) findViewById(R.id.height);
      mWeightView = (TextView) findViewById(R.id.weight);
      mExpView = (TextView) findViewById(R.id.exp);
      mBarChart = (BarChartView) findViewById(R.id.chart);
      mLayout = (CoordinatorLayout) findViewById(R.id.layout);
      mTitle = (TextView) findViewById(R.id.pokemon_name);
      mMainTitle = (TextView) findViewById(R.id.main_title);
      mDescription = (TextView) findViewById(R.id.description);
      mPreviousLabel = (TextView) findViewById(R.id.previous_label);
      mPreviousImage = (ImageView) findViewById(R.id.previous_image);
      mNextLabel = (TextView) findViewById(R.id.next_label);
      mNextImage = (ImageView) findViewById(R.id.next_image);
      mTitleContainer = (LinearLayout) findViewById(R.id.title_layout);
      mAppBar = (AppBarLayout) findViewById(R.id.app_bar);

      mStats = new TextView[STATS.length];
      mStats[0] = (TextView) findViewById(R.id.hp);
      mStats[1] = (TextView) findViewById(R.id.attack);
      mStats[2] = (TextView) findViewById(R.id.defense);
      mStats[3] = (TextView) findViewById(R.id.special_attack);
      mStats[4] = (TextView) findViewById(R.id.special_defense);
      mStats[5] = (TextView) findViewById(R.id.speed);

      mPrevious.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            if (mPokemonId == FIRST_POKEMON) {
               switchPokemon(LAST_POKEMON);
            }
            else {
               switchPokemon(mPokemonId - 1);
            }
         }
      });

      mNext.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            if (mPokemonId == LAST_POKEMON) {
               switchPokemon(FIRST_POKEMON);
            }
            else {
               switchPokemon(mPokemonId + 1);
            }
         }
      });

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

      new LoadPokemonProfile(this).execute();
   }

   private class LoadPokemonProfile extends AsyncTask<Void, Void, PokemonProfile> {
      private Context mContext;

      public LoadPokemonProfile(Context context) {
         this.mContext = context;
      }

      @Override
      protected PokemonProfile doInBackground(Void... params) {
         Bundle extras = getIntent().getExtras();
         return PokebaseCache.getPokemonProfile(mDatabaseHelper, extras.getInt(POKEMON_ID_KEY));
      }

      @Override
      protected void onPostExecute(PokemonProfile result) {
         super.onPostExecute(result);

         mPokemonId = result.getId();

         AnimatedRecyclerView movesList = new AnimatedRecyclerView(mContext);
         movesList.setLayoutManager(new LinearLayoutManager(mContext));
         movesList.setHasFixedSize(true);
         movesList.setAdapter(new MoveListAdapter(mContext, result.getMoves()));

         mMovesDialog = new LovelyCustomDialog(mContext)
               .setTopColorRes(R.color.colorPrimary)
               .setTitle(R.string.moveset)
               .setIcon(R.drawable.ic_book_white_24dp)
               .setView(movesList)
               .setCancelable(true);

         String evolutionsTitle;
         PokemonListItem[] evolutions = result.getEvolutions();

         if (evolutions.length == 0) {
            evolutionsTitle = getString(R.string.no_evolutions);
         }
         else {
            evolutionsTitle = getString(R.string.evolutions);
         }

         mEvolutionsList = new AnimatedRecyclerView(mContext);
         mEvolutionsList.setLayoutManager(new LinearLayoutManager(mContext));
         mEvolutionsList.setHasFixedSize(true);
         mEvolutionsList.setAdapter(new PokemonListAdapter(mContext, evolutions, true));

         mEvolutionsDialog = new LovelyCustomDialog(mContext)
               .setTopColorRes(R.color.colorPrimary)
               .setView(mEvolutionsList)
               .setIcon(R.drawable.ic_group_work_white_24dp)
               .setTitle(evolutionsTitle)
               .setCancelable(true);

         loadNextPrevious();

         mDescription.setText(result.getDescription());

         loadChart(result.getBaseStats());

         int imageResourceId = getResources().getIdentifier(SPRITE + mPokemonId,
               DRAWABLE, getPackageName());
         mProfileImg.setImageResource(imageResourceId);

         setHeightViewText(result.getHeight());
         setWeightViewText(result.getWeight());

         mExpView.setText(String.valueOf(result.getBaseExp()));

         mPokemonName = result.getName();
         String formattedName = String.format(getString(R.string.pokemon_name), mPokemonId, mPokemonName);
         mTitle.setText(formattedName);
         mMainTitle.setText(formattedName);
         mRegionView.setText(result.getRegion());

         loadTypes(result.getTypes());
      }
   }

   private void switchPokemon(int pokemonId) {
      Intent intent = getIntent();
      Bundle extras = new Bundle();
      extras.putInt(PokemonProfileActivity.POKEMON_ID_KEY, pokemonId);
      intent.putExtras(extras);
      startActivity(getIntent());
      finish();
   }

   private void loadTypes(String[] types) {
      if (types.length == 1) {
         String type = types[0];
         String colorName = TYPE + type;
         int colorResId = getResources().getIdentifier(colorName, COLOR, getPackageName());

         mTypeTwoView.setVisibility(View.GONE);
         mTypeOneView.setText(type);
         mTypeOneView.setBackgroundColor(ContextCompat.getColor(this, colorResId));
      }
      else {
         String typeOne = types[0];
         String typeTwo = types[1];
         String colorName = TYPE + typeOne;
         int colorResId = getResources().getIdentifier(colorName, COLOR, getPackageName());

         mTypeOneView.setText(typeOne);
         mTypeOneView.setBackgroundColor(ContextCompat.getColor(this, colorResId));
         mTypeTwoView.setVisibility(View.VISIBLE);
         mTypeTwoView.setText(typeTwo);
         colorName = TYPE + typeTwo;
         colorResId = getResources().getIdentifier(colorName, COLOR, getPackageName());
         mTypeTwoView.setBackgroundColor(ContextCompat.getColor(this, colorResId));
      }
   }

   private void loadChart(float[] data) {
      BarSet dataSet = new BarSet();
      float tempVal;
      for (int index = 0; index < data.length; index++) {
         tempVal = data[index];
         dataSet.addBar(STATS[index], tempVal);
         mStats[index].setText(String.valueOf(Math.round(tempVal)));
      }

      dataSet.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
      mBarChart.addData(dataSet);
      mBarChart.setXAxis(false);
      mBarChart.setYAxis(false);
      mBarChart.setYLabels(AxisController.LabelPosition.NONE);

      Animation animation = new Animation(1000);
      animation.setEasing(new BounceEase());
      mBarChart.show(animation);
   }

   private void loadNextPrevious() {
      String nextPokemonId, previousPokemonId;
      int nextImageId, previousImageId;

      if (mPokemonId == FIRST_POKEMON) {
         previousPokemonId = String.valueOf(LAST_POKEMON);
         nextPokemonId = String.valueOf(mPokemonId + 1);
      }
      else if (mPokemonId == LAST_POKEMON) {
         previousPokemonId = String.valueOf(mPokemonId - 1);
         nextPokemonId = String.valueOf(FIRST_POKEMON);
      }
      else {
         previousPokemonId = String.valueOf(mPokemonId - 1);
         nextPokemonId = String.valueOf(mPokemonId + 1);
      }
      previousImageId = getResources().getIdentifier(ICON + previousPokemonId,
            DRAWABLE, getPackageName());
      nextImageId = getResources().getIdentifier(ICON + nextPokemonId,
            DRAWABLE, getPackageName());

      mPreviousLabel.setText(String.format(getString(R.string.hashtag_format), previousPokemonId));
      mPreviousImage.setImageResource(previousImageId);
      mNextLabel.setText(String.format(getString(R.string.hashtag_format), nextPokemonId));
      mNextImage.setImageResource(nextImageId);
   }

   private void setHeightViewText(int decimeters) {
      int feet = (int) Math.floor(decimeters * FT_PER_DM);
      int inches = (int) Math.round((decimeters * FT_PER_DM - feet) * IN_PER_FT);
      if (inches == IN_PER_FT) {
         feet++;
         inches = 0;
      }
      double millimeters = (double) decimeters / DM_PER_M;
      String heightText = feet + "'" + inches + "'' ("
            + String.format(Locale.US, "%.2f", millimeters) + " m)";
      mHeightView.setText(heightText);
   }

   private void setWeightViewText(int hectograms) {
      double pounds = hectograms * LB_PER_HG;
      double kilograms = (double) hectograms / KG_PER_HG;
      String weightText = String.format(Locale.US, "%.1f", pounds) + " lbs (" +
            String.format(Locale.US, "%.1f", kilograms) + " kg)";
      mWeightView.setText(weightText);
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

   public void showMoves(View view) {
      mMovesDialog.show();
   }

   public void showEvolutions(View view) {
      mEvolutionsDialog.show();
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
                           getString(R.string.add_success), mPokemonName, item), Snackbar.LENGTH_LONG);
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
