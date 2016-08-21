package com.app.main.pokebase.gui.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.main.pokebase.R;
import com.app.main.pokebase.gui.adapters.PokemonListAdapter;
import com.app.main.pokebase.gui.adapters.TextViewSpinnerAdapter;
import com.app.main.pokebase.gui.views.AnimatedRecyclerView;
import com.app.main.pokebase.model.components.PokemonListItem;
import com.app.main.pokebase.model.database.DatabaseOpenHelper;
import com.mancj.materialsearchbar.MaterialSearchBar;

/**
 * @author Tyler Wong
 */
public class PokebaseFragment extends Fragment implements AdapterView.OnItemSelectedListener,
      MaterialSearchBar.OnSearchActionListener {
   private MaterialSearchBar mSearchBar;
   private EditText mSearchInput;
   private ImageView mClear;
   private Spinner mTypeSpinner;
   private Spinner mRegionSpinner;
   private AnimatedRecyclerView mPokemonList;
   private DatabaseOpenHelper mDatabaseHelper;
   private PokemonListItem[] mPokemon;
   private boolean mIsAlphabetical = false;

   private final static String TYPES = "Types";
   private final static String REGIONS = "Regions";
   private static final int RECOGNIZER_REQ_CODE = 1234;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      getActivity().setTheme(R.style.PokebaseFragmentTheme);
      mDatabaseHelper = DatabaseOpenHelper.getInstance(getContext());
      setHasOptionsMenu(true);
   }

   @Override
   public void onResume() {
      super.onResume();
      getActivity().setTheme(R.style.PokebaseFragmentTheme);
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
      switch (item.getItemId()) {
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

      mSearchBar = (MaterialSearchBar) view.findViewById(R.id.search_bar);
      mSearchBar.setOnSearchActionListener(this);
      mSearchBar.setTextColor(android.R.color.black);
      mSearchBar.setTextHintColor(android.R.color.secondary_text_dark);
      mSearchInput = (EditText) view.findViewById(R.id.mt_editText);
      mClear = (ImageView) view.findViewById(R.id.mt_clear);
      mClear.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            mSearchInput.setText("");
            refreshData();
         }
      });
      mTypeSpinner = (Spinner) view.findViewById(R.id.type_spinner);
      mTypeSpinner.setOnItemSelectedListener(this);
      mRegionSpinner = (Spinner) view.findViewById(R.id.region_spinner);
      mRegionSpinner.setOnItemSelectedListener(this);

      mPokemonList = (AnimatedRecyclerView) view.findViewById(R.id.pokemon_list);
      mPokemonList.setLayoutManager(new LinearLayoutManager(getContext()));
      mPokemonList.setHasFixedSize(true);

      new LoadPokemonList().execute();
   }

   @Override
   public void onNothingSelected(AdapterView<?> parent) {

   }

   @Override
   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      ((TextView) view).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
      refreshData();
   }

   private void refreshData() {
      new RefreshPokemon().execute(mSearchInput.getText().toString(), (String) mTypeSpinner.getSelectedItem(),
            (String) mRegionSpinner.getSelectedItem());
   }

   @Override
   public void onSearchConfirmed(CharSequence charSequence) {
      new RefreshPokemon().execute(charSequence.toString(), (String) mTypeSpinner.getSelectedItem(),
            (String) mRegionSpinner.getSelectedItem());
   }

   @Override
   public void onButtonClicked(int buttonCode) {
      if (buttonCode == MaterialSearchBar.BUTTON_SPEECH) {
         Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
         voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
               RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
         voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
         startActivityForResult(voiceIntent, RECOGNIZER_REQ_CODE);
      }
   }

   @Override
   public void onSearchStateChanged(boolean b) {

   }

   private class RefreshPokemon extends AsyncTask<String, Void, PokemonListItem[]> {
      @Override
      protected PokemonListItem[] doInBackground(String... params) {
         String search = params[0];
         String type = params[1];
         String region = params[2];

         if (type.equals(TYPES) && !region.equals(REGIONS)) {
            mPokemon = mDatabaseHelper.queryByRegion(search, region, mIsAlphabetical);
         }
         else if (!type.equals(TYPES) && region.equals(REGIONS)) {
            mPokemon = mDatabaseHelper.queryByType(search, type, mIsAlphabetical);
         }
         else if (!type.equals(TYPES) && !region.equals(REGIONS)) {
            mPokemon = mDatabaseHelper.queryByTypeAndRegion(search, type, region, mIsAlphabetical);
         }
         else {
            mPokemon = mDatabaseHelper.queryAll(search, mIsAlphabetical);
         }

         return mPokemon;
      }

      @Override
      protected void onPostExecute(PokemonListItem[] result) {
         super.onPostExecute(result);
         mPokemonList.setAdapter(new PokemonListAdapter(getContext(), result, false));
      }
   }

   private class LoadPokemonList extends AsyncTask<Void, Void, Pair<Pair<String[], String[]>, PokemonListItem[]>> {
      @Override
      protected Pair<Pair<String[], String[]>, PokemonListItem[]> doInBackground(Void... params) {
         String[] types = mDatabaseHelper.queryAllTypes();
         String[] regions = mDatabaseHelper.queryAllRegions();
         PokemonListItem[] pokemon = mDatabaseHelper.queryAll("", mIsAlphabetical);

         return new Pair<> (new Pair<> (types, regions), pokemon);
      }

      @Override
      protected void onPostExecute(Pair<Pair<String[], String[]>, PokemonListItem[]> result) {
         super.onPostExecute(result);
         mTypeSpinner.setAdapter(new TextViewSpinnerAdapter(getContext(), result.first.first));
         mRegionSpinner.setAdapter(new TextViewSpinnerAdapter(getContext(), result.first.second));
         mPokemonList.setAdapter(new PokemonListAdapter(getContext(), result.second, false));
      }
   }

   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      if (requestCode == RECOGNIZER_REQ_CODE && data != null) {
         mSearchInput.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
         refreshData();
      }
   }
}
