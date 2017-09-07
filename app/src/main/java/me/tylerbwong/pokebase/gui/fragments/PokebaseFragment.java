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
@SuppressWarnings("unused")
public class PokebaseFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        MaterialSearchBar.OnSearchActionListener {
    @BindView(R.id.search_bar)
    MaterialSearchBar searchBar;
    @BindView(R.id.mt_editText)
    EditText searchInput;
    @BindView(R.id.type_spinner)
    Spinner typeSpinner;
    @BindView(R.id.region_spinner)
    Spinner regionSpinner;
    @BindView(R.id.pokemon_list)
    RecyclerView pokemonList;
    @BindView(R.id.no_results)
    ImageView noResults;
    @BindView(R.id.empty_layout)
    LinearLayout emptyView;

    private DatabaseOpenHelper databaseHelper;
    private PokemonListItem[] pokemon;
    private Unbinder unbinder;
    private boolean isAlphabetical = false;

    private static final String TYPES = "Types";
    private static final String REGIONS = "Regions";
    private static final int RECOGNIZER_REQ_CODE = 1234;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTheme(R.style.PokebaseFragmentTheme);
        databaseHelper = DatabaseOpenHelper.getInstance(getContext());
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
        unbinder = ButterKnife.bind(this, view);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.pokebase);
        }

        searchBar.setOnSearchActionListener(this);
        searchBar.setTextColor(android.R.color.black);
        searchBar.setTextHintColor(android.R.color.secondary_text_dark);

        pokemonList.setLayoutManager(new LinearLayoutManager(getContext()));
        pokemonList.setHasFixedSize(true);

        searchInput.addTextChangedListener(new TextWatcher() {

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

        String[] types = databaseHelper.queryAllTypes();
        String[] regions = databaseHelper.queryAllRegions();
        PokemonListItem[] pokemon = databaseHelper.queryAll("", isAlphabetical);

        typeSpinner.setAdapter(new TextViewSpinnerAdapter(getContext(), types));
        regionSpinner.setAdapter(new TextViewSpinnerAdapter(getContext(), regions));
        pokemonList.setAdapter(new PokemonListAdapter(getContext(), pokemon, false));
        checkEmpty(pokemon);

        Glide.with(this)
                .load(R.drawable.no_teams)
                .into(noResults);

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
                isAlphabetical = false;
                refreshData();
                break;
            case R.id.name_action:
                isAlphabetical = true;
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
        searchInput.setText("");
        refreshData();
    }

    private void checkEmpty(PokemonListItem[] pokemon) {
        if (pokemon.length == 0) {
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            emptyView.setVisibility(View.INVISIBLE);
        }
    }

    @OnItemSelected({R.id.type_spinner, R.id.region_spinner})
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) view).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        refreshData();
    }

    private void refreshData() {
        new RefreshPokemon().execute(searchInput.getText().toString(), (String) typeSpinner.getSelectedItem(),
                (String) regionSpinner.getSelectedItem());
    }

    @Override
    public void onSearchConfirmed(CharSequence charSequence) {
        new RefreshPokemon().execute(charSequence.toString(), (String) typeSpinner.getSelectedItem(),
                (String) regionSpinner.getSelectedItem());
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
        unbinder.unbind();
    }

    private class RefreshPokemon extends AsyncTask<String, Void, PokemonListItem[]> {
        @Override
        protected PokemonListItem[] doInBackground(String... params) {
            String search = params[0];
            String type = params[1];
            String region = params[2];

            if (type.equals(TYPES) && !region.equals(REGIONS)) {
                pokemon = databaseHelper.queryByRegion(search, region, isAlphabetical);
            }
            else if (!type.equals(TYPES) && region.equals(REGIONS)) {
                pokemon = databaseHelper.queryByType(search, type, isAlphabetical);
            }
            else if (!type.equals(TYPES) && !region.equals(REGIONS)) {
                pokemon = databaseHelper.queryByTypeAndRegion(search, type, region, isAlphabetical);
            }
            else {
                pokemon = databaseHelper.queryAll(search, isAlphabetical);
            }

            return pokemon;
        }

        @Override
        protected void onPostExecute(PokemonListItem[] result) {
            super.onPostExecute(result);
            pokemonList.setAdapter(new PokemonListAdapter(getContext(), result, false));
            checkEmpty(pokemon);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RECOGNIZER_REQ_CODE && data != null) {
            searchInput.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
            refreshData();
        }
    }
}
