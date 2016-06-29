package com.app.pokebase.pokebase.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.app.pokebase.pokebase.R;
import com.app.pokebase.pokebase.adapters.PokemonRecyclerViewAdapter;
import com.app.pokebase.pokebase.components.PokemonListItem;
import com.app.pokebase.pokebase.database.DatabaseOpenHelper;

import java.util.List;

/**
 * @author Brittany Berlanga
 */
public class EvolutionsActivity extends AppCompatActivity {
   private Toolbar mToolbar;
   private RecyclerView mEvolutionList;
   private ActionBar mActionBar;
   private DatabaseOpenHelper mDatabaseHelper;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_evolutions);
      mDatabaseHelper = new DatabaseOpenHelper(this);

      Intent intent = getIntent();
      Bundle extras = intent.getExtras();
      int pokemonId = extras.getInt(ProfileActivity.POKEMON_ID_KEY);
      String pokemonName = extras.getString(ProfileActivity.POKEMON_NAME_KEY);

      mToolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(mToolbar);
      mActionBar = getSupportActionBar();

      if (mActionBar != null) {
         mActionBar.setDisplayHomeAsUpEnabled(true);
         mActionBar.setTitle("Evolutions of " + pokemonName);
      }

      List<PokemonListItem> pokemonEvolutions = mDatabaseHelper.queryPokemonEvolutions(pokemonId);
      mEvolutionList = (RecyclerView) findViewById(R.id.evolutions_list);
      mEvolutionList.setLayoutManager(new LinearLayoutManager(this));
      mEvolutionList.setAdapter(new PokemonRecyclerViewAdapter(
            this, pokemonEvolutions.toArray(new PokemonListItem[pokemonEvolutions.size()])));
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
