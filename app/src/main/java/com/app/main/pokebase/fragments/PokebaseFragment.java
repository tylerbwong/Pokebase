package com.app.main.pokebase.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.app.main.pokebase.R;
import com.app.main.pokebase.adapters.PokemonListAdapter;
import com.app.main.pokebase.adapters.TextViewSpinnerAdapter;
import com.app.main.pokebase.components.PokemonListItem;
import com.app.main.pokebase.database.DatabaseOpenHelper;
import com.app.main.pokebase.views.AnimatedRecyclerView;

/**
 * @author Tyler Wong
 */
public class PokebaseFragment extends Fragment implements AdapterView.OnItemSelectedListener {
   private Spinner mTypeSpinner;
   private Spinner mRegionSpinner;
   private AnimatedRecyclerView mPokemonList;
   private DatabaseOpenHelper mDatabaseHelper;
   private PokemonListItem[] mPokemon;
   private boolean mIsAlphabetical = false;

   private final static String TYPES = "Types";
   private final static String REGIONS = "Regions";

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      getActivity().setTheme(R.style.PokemonEditorTheme);
      mDatabaseHelper = DatabaseOpenHelper.getInstance(getContext());
      setHasOptionsMenu(true);
   }

   @Override
   public void onResume() {
      super.onResume();
      getActivity().setTheme(R.style.PokemonEditorTheme);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.pokebase_fragment, container, false);

      ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
      if (actionBar != null) {
         actionBar.setTitle(R.string.pokebase);
      }

      return view;
   }

   @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
      super.onCreateOptionsMenu(menu, inflater);
      menu.findItem(R.id.clear_all_teams_action).setVisible(false);
      menu.findItem(R.id.number_action).setVisible(true);
      menu.findItem(R.id.name_action).setVisible(true);
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch(item.getItemId()) {
         case R.id.number_action:
            mIsAlphabetical = false;
            refreshData();
            break;
         case R.id.name_action:
            mIsAlphabetical = true;
            refreshData();
            break;
         default:
            break;
      }
      return super.onOptionsItemSelected(item);
   }

   @Override
   public void onViewCreated(View view, Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

      mTypeSpinner = (Spinner) view.findViewById(R.id.type_spinner);
      mTypeSpinner.setOnItemSelectedListener(this);
      mRegionSpinner = (Spinner) view.findViewById(R.id.region_spinner);
      mRegionSpinner.setOnItemSelectedListener(this);

      mPokemonList = (AnimatedRecyclerView) view.findViewById(R.id.pokemon_list);
      mPokemonList.setLayoutManager(new LinearLayoutManager(getContext()));
      mPokemonList.setHasFixedSize(true);

      mTypeSpinner.setAdapter(new TextViewSpinnerAdapter(getContext(),
            mDatabaseHelper.queryAllTypes()));
      mRegionSpinner.setAdapter(new TextViewSpinnerAdapter(getContext(),
            mDatabaseHelper.queryAllRegions()));
      mPokemonList.setAdapter(new PokemonListAdapter(getContext(),
            mDatabaseHelper.queryAll(mIsAlphabetical), false));
   }

   @Override
   public void onNothingSelected(AdapterView<?> parent) {

   }

   @Override
   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      refreshData();
   }

   private void refreshData() {
      String type = (String) mTypeSpinner.getSelectedItem();
      String region = (String) mRegionSpinner.getSelectedItem();

      if (type.equals(TYPES) && !region.equals(REGIONS)) {
         mPokemon = mDatabaseHelper.queryByRegion(region, mIsAlphabetical);
      }
      else if (!type.equals(TYPES) && region.equals(REGIONS)) {
         mPokemon = mDatabaseHelper.queryByType(type, mIsAlphabetical);
      }
      else if (!type.equals(TYPES) && !region.equals(REGIONS)) {
         mPokemon = mDatabaseHelper.queryByTypeAndRegion(type, region, mIsAlphabetical);
      }
      else {
         mPokemon = mDatabaseHelper.queryAll(mIsAlphabetical);
      }

      mPokemonList.setAdapter(new PokemonListAdapter(getContext(), mPokemon, false));
   }
}
