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

package me.tylerbwong.pokebase.gui.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mancj.materialsearchbar.MaterialSearchBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.Unbinder;
import me.tylerbwong.pokebase.R;
import me.tylerbwong.pokebase.gui.adapters.PokemonListAdapter;
import me.tylerbwong.pokebase.gui.adapters.TextViewSpinnerAdapter;
import me.tylerbwong.pokebase.model.components.PokemonListItem;
import me.tylerbwong.pokebase.model.database.DatabaseOpenHelper;

/**
 * @author Tyler Wong
 */
public class PokebaseFragment extends Fragment implements AdapterView.OnItemSelectedListener,
      MaterialSearchBar.OnSearchActionListener {
   @BindView(R.id.search_bar)
   MaterialSearchBar mSearchBar;
   @BindView(R.id.mt_editText)
   EditText mSearchInput;
   @BindView(R.id.mt_clear)
   ImageView mClear;
   @BindView(R.id.type_spinner)
   Spinner mTypeSpinner;
   @BindView(R.id.region_spinner)
   Spinner mRegionSpinner;
   @BindView(R.id.pokemon_list)
   RecyclerView mPokemonList;
   @BindView(R.id.no_results)
   ImageView mNoResults;
   @BindView(R.id.empty_layout)
   LinearLayout mEmptyView;

   private DatabaseOpenHelper mDatabaseHelper;
   private PokemonListItem[] mPokemon;
   private Unbinder mUnbinder;
   private boolean mIsAlphabetical = false;

   private static final String TYPES = "Types";
   private static final String REGIONS = "Regions";
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
      mUnbinder = ButterKnife.bind(this, view);

      ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
      if (actionBar != null) {
         actionBar.setTitle(R.string.pokebase);
      }

      mSearchBar.setOnSearchActionListener(this);
      mSearchBar.setTextColor(android.R.color.black);
      mSearchBar.setTextHintColor(android.R.color.secondary_text_dark);

      mPokemonList.setLayoutManager(new LinearLayoutManager(getContext()));
      mPokemonList.setHasFixedSize(true);

      mSearchInput.addTextChangedListener(new TextWatcher() {

         @Override
         public void onTextChanged(CharSequence sequence, int start, int before, int count) {
            refreshData();
         }

         @Override
         public void beforeTextChanged(CharSequence sequence, int start, int count, int after) {

         }

         @Override
         public void afterTextChanged(Editable editable) {
         }
      });

      String[] types = mDatabaseHelper.queryAllTypes();
      String[] regions = mDatabaseHelper.queryAllRegions();
      PokemonListItem[] pokemon = mDatabaseHelper.queryAll("", mIsAlphabetical);

      mTypeSpinner.setAdapter(new TextViewSpinnerAdapter(getContext(), types));
      mRegionSpinner.setAdapter(new TextViewSpinnerAdapter(getContext(), regions));
      mPokemonList.setAdapter(new PokemonListAdapter(getContext(), pokemon, false));
      checkEmpty(pokemon);

      Glide.with(this)
            .load(R.drawable.no_teams)
            .into(mNoResults);

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
   public void onNothingSelected(AdapterView<?> parent) {

   }

   @OnClick(R.id.mt_clear)
   public void onClear() {
      mSearchInput.setText("");
      refreshData();
   }

   private void checkEmpty(PokemonListItem[] pokemon) {
      if (pokemon.length == 0) {
         mEmptyView.setVisibility(View.VISIBLE);
      }
      else {
         mEmptyView.setVisibility(View.INVISIBLE);
      }
   }

   @OnItemSelected({R.id.type_spinner, R.id.region_spinner})
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

   @Override
   public void onDestroyView() {
      super.onDestroyView();
      mUnbinder.unbind();
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
         checkEmpty(mPokemon);
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
