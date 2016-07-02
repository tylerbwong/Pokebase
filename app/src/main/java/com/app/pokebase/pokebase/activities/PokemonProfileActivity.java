package com.app.pokebase.pokebase.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pokebase.pokebase.R;
import com.app.pokebase.pokebase.adapters.TextViewAdapter;
import com.app.pokebase.pokebase.components.PokemonProfile;
import com.app.pokebase.pokebase.database.DatabaseOpenHelper;
import com.app.pokebase.pokebase.utilities.OnSwipeTouchListener;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.util.List;
import java.util.Locale;

/**
 * @author Brittany Berlanga
 */
public class PokemonProfileActivity extends AppCompatActivity {
   public static final String POKEMON_ID_KEY = "pokemon_id";
   public static final String POKEMON_NAME_KEY = "pokemon_name";
   private static final double FT_PER_DM = 0.32808399;
   private static final double LB_PER_HG = 0.22046226218;
   private static final int KG_PER_HG = 10;
   private static final int IN_PER_FT = 12;
   private static final int DM_PER_M = 10;
   private static final int PROFILE_IMG_ELEVATION = 40;
   private static final int DEFAULT_LEVEL = 1;
   private static final int DEFAULT_MOVE = 0;
   private static final int FIRST_POKEMON = 1;
   private static final int LAST_POKEMON = 721;
   private int mPokemonId;
   private String mPokemonName;
   private Toolbar mToolbar;
   private ImageView mProfileImg;
   private TextView mIdView;
   private TextView mNameView;
   private TextView mTypeOneView;
   private TextView mTypeTwoView;
   private TextView mRegionView;
   private TextView mHeightView;
   private TextView mWeightView;
   private TextView mExpView;
   private ListView mMovesList;
   private RelativeLayout mLayout;
   private ActionBar mActionBar;
   private DatabaseOpenHelper mDatabaseHelper;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      mDatabaseHelper = DatabaseOpenHelper.getInstance(this);

      setContentView(R.layout.activity_profile);
      mToolbar = (Toolbar) findViewById(R.id.toolbar);
      mProfileImg = (ImageView) findViewById(R.id.profile_image);
      mProfileImg.setClipToOutline(true);
      mProfileImg.setElevation(PROFILE_IMG_ELEVATION);
      mIdView = (TextView) findViewById(R.id.id);
      mNameView = (TextView) findViewById(R.id.name);
      mTypeOneView = (TextView) findViewById(R.id.type_one);
      mTypeTwoView = (TextView) findViewById(R.id.type_two);
      mRegionView = (TextView) findViewById(R.id.region);
      mHeightView = (TextView) findViewById(R.id.height);
      mWeightView = (TextView) findViewById(R.id.weight);
      mExpView = (TextView) findViewById(R.id.exp);
      mMovesList = (ListView) findViewById(R.id.moves_list);
      mLayout = (RelativeLayout) findViewById(R.id.layout);

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
      }

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

      mMovesList.setAdapter(new TextViewAdapter(this, pokemon.getMoves()));

      mPokemonId = pokemon.getId();
      mIdView.setText(String.valueOf(mPokemonId));

      int imageResourceId = this.getResources().getIdentifier("sprites_" + mPokemonId,
            "drawable", this.getPackageName());
      mProfileImg.setImageResource(imageResourceId);

      setHeightViewText(pokemon.getHeight());
      setWeightViewText(pokemon.getWeight());
      mExpView.setText(String.valueOf(pokemon.getBaseExp()));

      mPokemonName = pokemon.getName();
      mActionBar.setTitle(mPokemonName);
      mNameView.setText(mPokemonName);
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

   public void showEvolutions(View view) {
      Intent evolutionsIntent = new Intent(this, EvolutionsActivity.class);
      Bundle extras = new Bundle();
      extras.putInt(POKEMON_ID_KEY, mPokemonId);
      extras.putString(POKEMON_NAME_KEY, mPokemonName);
      evolutionsIntent.putExtras(extras);
      startActivity(evolutionsIntent);
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
}
