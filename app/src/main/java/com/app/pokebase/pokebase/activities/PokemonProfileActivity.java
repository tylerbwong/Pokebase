package com.app.pokebase.pokebase.activities;

import android.content.Intent;
import android.media.MediaPlayer;
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

import com.app.pokebase.pokebase.R;
import com.app.pokebase.pokebase.adapters.PokemonListAdapter;
import com.app.pokebase.pokebase.components.PokemonListItem;
import com.app.pokebase.pokebase.components.PokemonProfile;
import com.app.pokebase.pokebase.database.DatabaseOpenHelper;
import com.app.pokebase.pokebase.utilities.AnimatedRecyclerView;
import com.app.pokebase.pokebase.utilities.OnSwipeTouchListener;
import com.db.chart.model.BarSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.BarChartView;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.BounceEase;
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
   private ImageView mProfileImg;
   private TextView mTypeOneView;
   private TextView mTypeTwoView;
   private TextView mRegionView;
   private TextView mHeightView;
   private TextView mWeightView;
   private TextView mExpView;
   private TextView mTitle;
   private TextView mMainTitle;
   private TextView[] mStats;
   private CoordinatorLayout mLayout;
   private LovelyChoiceDialog mMovesDialog;
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

   private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
   private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
   private static final int ALPHA_ANIMATIONS_DURATION              = 200;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      mDatabaseHelper = DatabaseOpenHelper.getInstance(this);

      setContentView(R.layout.activity_profile);
      mToolbar = (Toolbar) findViewById(R.id.toolbar);
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
      mTitleContainer = (LinearLayout) findViewById(R.id.title_layout);
      mAppBar = (AppBarLayout) findViewById(R.id.app_bar);

      mStats = new TextView[STATS.length];
      mStats[0] = (TextView) findViewById(R.id.hp);
      mStats[1] = (TextView) findViewById(R.id.attack);
      mStats[2] = (TextView) findViewById(R.id.defense);
      mStats[3] = (TextView) findViewById(R.id.special_attack);
      mStats[4] = (TextView) findViewById(R.id.special_defense);
      mStats[5] = (TextView) findViewById(R.id.speed);

      OnSwipeTouchListener swipeTouchListener = new OnSwipeTouchListener(PokemonProfileActivity.this) {
         @Override
         public void onSwipeLeft() {
            if (mPokemonId != LAST_POKEMON) {
               switchPokemon(mPokemonId + 1);
            }
         }

         @Override
         public void onSwipeRight() {
            if (mPokemonId != FIRST_POKEMON) {
               switchPokemon(mPokemonId - 1);
            }
         }
      };

      mLayout.setOnTouchListener(swipeTouchListener);

      setSupportActionBar(mToolbar);
      mActionBar = getSupportActionBar();
      if (mActionBar != null) {
         mActionBar.setDisplayHomeAsUpEnabled(true);
         mActionBar.setDisplayShowTitleEnabled(false);
      }
      mAppBar.addOnOffsetChangedListener(this);
      startAlphaAnimation(mTitle, 0, View.INVISIBLE);

      loadPokemonProfile();
   }

   private void switchPokemon(int pokemonId) {
      Intent intent = getIntent();
      Bundle extras = new Bundle();
      extras.putInt(PokemonProfileActivity.POKEMON_ID_KEY, pokemonId);
      intent.putExtras(extras);
      finish();
      startActivity(getIntent());
   }

   private void loadPokemonProfile() {
      Intent intent = getIntent();
      Bundle extras = intent.getExtras();
      int pokemonId = extras.getInt(POKEMON_ID_KEY);
      PokemonProfile pokemon = mDatabaseHelper.querySelectedPokemonProfile(pokemonId);

      mMovesDialog = new LovelyChoiceDialog(this)
            .setTopColorRes(R.color.colorPrimary)
            .setTitle(R.string.moveset)
            .setIcon(R.drawable.ic_book_white_24dp)
            .setItems(pokemon.getMoves(), null)
            .setCancelable(true);

      mEvolutions = mDatabaseHelper.queryPokemonEvolutions(pokemonId);
      mPokemonId = pokemon.getId();

      float[] data = mDatabaseHelper.querySelectedPokemonStats(pokemonId);
      BarSet dataset = new BarSet();
      float tempVal;
      for (int index = 0; index < data.length; index++) {
         tempVal = data[index];
         dataset.addBar(STATS[index], tempVal);
         mStats[index].setText(String.valueOf(Math.round(tempVal)));
      }
      dataset.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
      mBarChart.addData(dataset);
      mBarChart.setYLabels(AxisController.LabelPosition.NONE);
      Animation animation = new Animation(1000);
      animation.setEasing(new BounceEase());
      mBarChart.show(animation);

      int imageResourceId = this.getResources().getIdentifier("sprites_" + mPokemonId,
            "drawable", this.getPackageName());
      mProfileImg.setImageResource(imageResourceId);

      setHeightViewText(pokemon.getHeight());
      setWeightViewText(pokemon.getWeight());
      mExpView.setText(String.valueOf(pokemon.getBaseExp()));

      mPokemonName = pokemon.getName();
      String formattedName = String.format(getString(R.string.pokemon_name), mPokemonId, mPokemonName);
      mTitle.setText(formattedName);
      mMainTitle.setText(formattedName);
      mRegionView.setText(pokemon.getRegion());

      String[] types = pokemon.getTypes();

      if (types.length == 1) {
         String type = types[0];
         mTypeTwoView.setVisibility(View.GONE);
         mTypeOneView.setText(type);
         String colorName = "type" + type;
         int colorResId = getResources().getIdentifier(colorName, "color", getPackageName());
         mTypeOneView.setBackgroundColor(ContextCompat.getColor(this, colorResId));
      }
      else {
         String typeOne = types[0];
         String typeTwo = types[1];
         mTypeOneView.setText(typeOne);
         String colorName = "type" + typeOne;
         int colorResId = getResources().getIdentifier(colorName, "color", getPackageName());
         mTypeOneView.setBackgroundColor(ContextCompat.getColor(this, colorResId));
         mTypeTwoView.setVisibility(View.VISIBLE);
         mTypeTwoView.setText(typeTwo);
         colorName = "type" + typeTwo;
         colorResId = getResources().getIdentifier(colorName, "color", getPackageName());
         mTypeTwoView.setBackgroundColor(ContextCompat.getColor(this, colorResId));
      }
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
            "audio_" + mPokemonId, "raw", getPackageName()));
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
      mEvolutionsList = new AnimatedRecyclerView(this);
      mEvolutionsList.setLayoutManager(new LinearLayoutManager(this));
      mEvolutionsList.setHasFixedSize(true);
      mEvolutionsList.setAdapter(new PokemonListAdapter(this, mEvolutions));

      new LovelyCustomDialog(this)
            .setTopColorRes(R.color.colorPrimary)
            .setView(mEvolutionsList)
            .setIcon(R.drawable.ic_group_work_white_24dp)
            .setTitle(R.string.evolutions)
            .setCancelable(true)
            .show();
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
               .setTitle("Choose a team to add " + mPokemonName + " to!")
               .setIcon(R.drawable.ic_add_circle_outline_white_24dp)
               .setItems(teamNames, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                  @Override
                  public void onItemSelected(int position, String item) {
                     mDatabaseHelper.insertTeamPokemon(teams.get(position).first, mPokemonId,
                           mPokemonName, DEFAULT_LEVEL, DEFAULT_MOVE, DEFAULT_MOVE,
                           DEFAULT_MOVE, DEFAULT_MOVE);
                     Snackbar snackbar = Snackbar.make(mLayout, "Added " + mPokemonName + " to " +
                           item, Snackbar.LENGTH_LONG);
                     snackbar.show();
                  }
               })
               .show();
      }
      else {
         new LovelyChoiceDialog(this)
               .setTopColorRes(R.color.colorPrimary)
               .setTitle(getResources().getString(R.string.no_teams))
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

         if(!mIsTheTitleVisible) {
            startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
            mIsTheTitleVisible = true;
         }

      } else {

         if (mIsTheTitleVisible) {
            startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
            mIsTheTitleVisible = false;
         }
      }
   }

   private void handleAlphaOnTitle(float percentage) {
      if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
         if(mIsTheTitleContainerVisible) {
            startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
            mIsTheTitleContainerVisible = false;
         }

      } else {

         if (!mIsTheTitleContainerVisible) {
            startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
            mIsTheTitleContainerVisible = true;
         }
      }
   }

   public static void startAlphaAnimation (View view, long duration, int visibility) {
      AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
            ? new AlphaAnimation(0f, 1f)
            : new AlphaAnimation(1f, 0f);

      alphaAnimation.setDuration(duration);
      alphaAnimation.setFillAfter(true);
      view.startAnimation(alphaAnimation);
   }
}
