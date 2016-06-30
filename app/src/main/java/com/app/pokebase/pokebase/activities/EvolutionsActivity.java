package com.app.pokebase.pokebase.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.app.pokebase.pokebase.R;
import com.app.pokebase.pokebase.adapters.PokemonRecyclerViewAdapter;
import com.app.pokebase.pokebase.database.DatabaseOpenHelper;
import com.app.pokebase.pokebase.utilities.AnimatedRecyclerView;

/**
 * @author Brittany Berlanga
 */
public class EvolutionsActivity extends AppCompatActivity {
   private Toolbar mToolbar;
   private AnimatedRecyclerView mEvolutionList;
   private ActionBar mActionBar;
   private DatabaseOpenHelper mDatabaseHelper;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_evolutions);
      mDatabaseHelper = DatabaseOpenHelper.getInstance(this);

      Intent intent = getIntent();
      Bundle extras = intent.getExtras();
      int pokemonId = extras.getInt(PokemonProfileActivity.POKEMON_ID_KEY);
      String pokemonName = extras.getString(PokemonProfileActivity.POKEMON_NAME_KEY);

      mToolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(mToolbar);
      mActionBar = getSupportActionBar();

      if (mActionBar != null) {
         mActionBar.setDisplayHomeAsUpEnabled(true);
         mActionBar.setTitle("Evolutions of " + pokemonName);
      }

      mEvolutionList = (AnimatedRecyclerView) findViewById(R.id.evolutions_list);
      mEvolutionList.setLayoutManager(new LinearLayoutManager(this));
      mEvolutionList.setAdapter(new PokemonRecyclerViewAdapter(
            this, mDatabaseHelper.queryPokemonEvolutions(pokemonId)));
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case android.R.id.home:
            onBackPressed();
            break;
         default:
            break;
      }
      return true;
   }
}
