package com.app.pokebase.pokebase.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.app.pokebase.pokebase.R;
import com.app.pokebase.pokebase.adapters.PokemonRecyclerViewAdapter;
import com.app.pokebase.pokebase.adapters.TextViewSpinnerAdapter;
import com.app.pokebase.pokebase.components.PokemonListItem;
import com.app.pokebase.pokebase.database.DatabaseOpenHelper;
import com.app.pokebase.pokebase.utilities.AnimatedRecyclerView;

/**
 * @author Tyler Wong
 */
public class PokebaseFragment extends Fragment implements AdapterView.OnItemSelectedListener {
   private Spinner mTypeSpinner;
   private Spinner mRegionSpinner;
   private AnimatedRecyclerView mPokemonList;
   private DatabaseOpenHelper mDatabaseHelper;
   private PokemonListItem[] mPokemon;

   private final static String TYPES = "Types";
   private final static String REGIONS = "Regions";

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      getActivity().setTheme(R.style.PokemonEditorTheme);
      mDatabaseHelper = DatabaseOpenHelper.getInstance(getContext());
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
   public void onViewCreated(View view, Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      mTypeSpinner = (Spinner) view.findViewById(R.id.type_spinner);
      mTypeSpinner.setOnItemSelectedListener(this);
      mRegionSpinner = (Spinner) view.findViewById(R.id.region_spinner);
      mRegionSpinner.setOnItemSelectedListener(this);
      mPokemonList = (AnimatedRecyclerView) view.findViewById(R.id.pokemon_list);
      mPokemonList.setLayoutManager(new LinearLayoutManager(getContext()));

      mTypeSpinner.setAdapter(new TextViewSpinnerAdapter(getContext(),
            mDatabaseHelper.queryAllTypes()));
      mRegionSpinner.setAdapter(new TextViewSpinnerAdapter(getContext(),
            mDatabaseHelper.queryAllRegions()));
      mPokemonList.setAdapter(new PokemonRecyclerViewAdapter(getContext(),
            mDatabaseHelper.queryAll()));
   }

   @Override
   public void onNothingSelected(AdapterView<?> parent) {

   }

   @Override
   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      String type = (String) mTypeSpinner.getSelectedItem();
      String region = (String) mRegionSpinner.getSelectedItem();

      if (type.equals(TYPES) && !region.equals(REGIONS)) {
         mPokemon = mDatabaseHelper.queryByRegion(region);
      }
      else if (!type.equals(TYPES) && region.equals(REGIONS)) {
         mPokemon = mDatabaseHelper.queryByType(type);
      }
      else if (!type.equals(TYPES) && !region.equals(REGIONS)) {
         mPokemon = mDatabaseHelper.queryByTypeAndRegion(type, region);
      }
      else {
         mPokemon = mDatabaseHelper.queryAll();
      }

      mPokemonList.setAdapter(new PokemonRecyclerViewAdapter(getContext(), mPokemon));
   }
}
