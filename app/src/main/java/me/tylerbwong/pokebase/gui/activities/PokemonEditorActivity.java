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

package me.tylerbwong.pokebase.gui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tylerbwong.pokebase.R;
import me.tylerbwong.pokebase.gui.adapters.TextViewSpinnerAdapter;
import me.tylerbwong.pokebase.gui.views.PokemonInfoView;
import me.tylerbwong.pokebase.model.database.DatabaseOpenHelper;
import me.tylerbwong.pokebase.model.utilities.ArrayUtils;

/**
 * @author Tyler Wong
 */
public class PokemonEditorActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.profile_image)
    ImageView profileImg;
    @BindView(R.id.nickname_input)
    TextInputEditText nicknameInput;
    @BindView(R.id.level_spinner)
    Spinner levelSpinner;
    @BindViews({R.id.move_one_spinner, R.id.move_two_spinner, R.id.move_three_spinner,
            R.id.move_four_spinner})
    Spinner[] moveSpinners;

    private int teamId;
    private int memberId;
    private int pokemonId;
    private int level;
    private String name;
    private String nickname;
    private String moveOne;
    private String moveTwo;
    private String moveThree;
    private String moveFour;
    private String title;
    private String description;
    private DatabaseOpenHelper databaseHelper;
    private PokemonInfoView infoView;
    private LovelyCustomDialog profileDialog;

    private static final int PROFILE_IMG_ELEVATION = 40;
    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 100;
    public static final String TEAM_ID = "teamId";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String MEMBER_ID = "memberId";
    public static final String POKEMON_ID = "pokemonId";
    public static final String LEVEL = "level";
    public static final String NAME = "name";
    public static final String NICKNAME = "nickname";
    public static final String MOVE_ONE = "moveOne";
    public static final String MOVE_TWO = "moveTwo";
    public static final String MOVE_THREE = "moveThree";
    public static final String MOVE_FOUR = "moveFour";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_editor);
        ButterKnife.bind(this);

        databaseHelper = DatabaseOpenHelper.getInstance(this);
        infoView = new PokemonInfoView(this);

        Intent intent = getIntent();
        teamId = intent.getIntExtra(TEAM_ID, 0);
        title = intent.getStringExtra(TITLE);
        description = intent.getStringExtra(DESCRIPTION);
        memberId = intent.getIntExtra(MEMBER_ID, 0);
        pokemonId = intent.getIntExtra(POKEMON_ID, 0);
        level = intent.getIntExtra(LEVEL, 0);
        name = intent.getStringExtra(NAME);
        nickname = intent.getStringExtra(NICKNAME);
        moveOne = intent.getStringExtra(MOVE_ONE);
        moveTwo = intent.getStringExtra(MOVE_TWO);
        moveThree = intent.getStringExtra(MOVE_THREE);
        moveFour = intent.getStringExtra(MOVE_FOUR);

        String[] movesResult = databaseHelper.queryPokemonMoves(pokemonId);

        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        profileImg.setClipToOutline(true);
        profileImg.setElevation(PROFILE_IMG_ELEVATION);

        if (actionBar != null) {
            actionBar.setTitle(nickname);
        }

        infoView.loadPokemonInfo(pokemonId);
        infoView.setButtonsVisible(false);

        Glide.with(this)
                .load(String.format(getString(R.string.sprite_url), name.toLowerCase()))
                .into(profileImg);

        nicknameInput.setText(nickname);

        nicknameInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence sequence, int start, int before, int count) {
                if (actionBar != null) {
                    actionBar.setTitle(sequence.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence sequence, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        String[] levels = new String[MAX_LEVEL];
        for (int lvl = MIN_LEVEL; lvl <= levels.length; lvl++) {
            levels[lvl - MIN_LEVEL] = String.valueOf(lvl);
        }

        levelSpinner.setAdapter(new TextViewSpinnerAdapter(this, levels, 18));
        levelSpinner.setSelection(level - 1);

        moveSpinners[0].setAdapter(new TextViewSpinnerAdapter(this, movesResult));
        moveSpinners[0].setSelection(ArrayUtils.indexOf(movesResult, moveOne));
        moveSpinners[1].setAdapter(new TextViewSpinnerAdapter(this, movesResult));
        moveSpinners[1].setSelection(ArrayUtils.indexOf(movesResult, moveTwo));
        moveSpinners[2].setAdapter(new TextViewSpinnerAdapter(this, movesResult));
        moveSpinners[2].setSelection(ArrayUtils.indexOf(movesResult, moveThree));
        moveSpinners[3].setAdapter(new TextViewSpinnerAdapter(this, movesResult));
        moveSpinners[3].setSelection(ArrayUtils.indexOf(movesResult, moveFour));

        profileDialog = new LovelyCustomDialog(this)
                .setView(infoView)
                .setIcon(R.drawable.ic_dns_white_24dp)
                .setTitle(nicknameInput.getText().toString())
                .setCancelable(true)
                .setTopColor(ContextCompat.getColor(this, R.color.colorPrimary));

        if (savedInstanceState != null) {
            moveSpinners[0].setSelection(savedInstanceState.getInt(MOVE_ONE, 0));
            moveSpinners[1].setSelection(savedInstanceState.getInt(MOVE_TWO, 0));
            moveSpinners[2].setSelection(savedInstanceState.getInt(MOVE_THREE, 0));
            moveSpinners[3].setSelection(savedInstanceState.getInt(MOVE_FOUR, 0));
            levelSpinner.setSelection(savedInstanceState.getInt(LEVEL, 0));
            nicknameInput.setText(savedInstanceState.getString(NICKNAME, ""));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        inflater.inflate(R.menu.menu_trash, menu);
        inflater.inflate(R.menu.menu_submit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.profile_action:
                showProfile();
                break;
            case R.id.delete_action:
                showDeleteDialog();
                break;
            case R.id.submit_action:
                updatePokemon();
                break;
            default:
                break;
        }
        return true;
    }

    private void showDeleteDialog() {
        new LovelyStandardDialog(this)
                .setIcon(R.drawable.ic_info_white_24dp)
                .setTitle(R.string.delete_pokemon)
                .setMessage(String.format(getString(R.string.delete_team_prompt),
                        nicknameInput.getText().toString()))
                .setCancelable(true)
                .setPositiveButton(R.string.yes, v -> deletePokemon())
                .setNegativeButton(R.string.no, null)
                .setTopColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .show();
    }

    private void deletePokemon() {
        databaseHelper.deleteTeamPokemonSingle(memberId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Toast.makeText(this, String.format(getString(R.string.team_deleted),
                            nicknameInput.getText().toString()), Toast.LENGTH_SHORT).show();
                    backToTeamView();
                });
    }

    private void updatePokemon() {
        databaseHelper.updateTeamPokemon(memberId, teamId, nicknameInput.getText().toString(),
                Integer.parseInt(String.valueOf(levelSpinner.getSelectedItem())),
                String.valueOf(moveSpinners[0].getSelectedItem()),
                String.valueOf(moveSpinners[1].getSelectedItem()),
                String.valueOf(moveSpinners[2].getSelectedItem()),
                String.valueOf(moveSpinners[3].getSelectedItem()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Toast.makeText(this, String.format(getString(R.string.team_update),
                            nicknameInput.getText().toString()), Toast.LENGTH_SHORT).show();
                    backToTeamView();
                });
    }

    private void backToTeamView() {
        Intent teamIntent = new Intent(this, TeamViewActivity.class);
        Bundle extras = new Bundle();
        extras.putInt(TeamViewActivity.TEAM_ID_KEY, teamId);
        extras.putBoolean(TeamViewActivity.UPDATE_KEY, true);
        extras.putString(TeamViewActivity.TEAM_NAME, title);
        extras.putString(TeamViewActivity.DESCRIPTION, description);
        teamIntent.putExtras(extras);
        startActivity(teamIntent);
    }

    private void showBackDialog() {
        new LovelyStandardDialog(this)
                .setIcon(R.drawable.ic_info_white_24dp)
                .setTitle(R.string.go_back)
                .setMessage(R.string.back_prompt)
                .setCancelable(true)
                .setPositiveButton(R.string.yes, v -> backToTeamView())
                .setNegativeButton(R.string.no, null)
                .setTopColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .show();
    }

    private void showProfile() {
        profileDialog.show();
    }

    @Override
    public void onBackPressed() {
        showBackDialog();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MOVE_ONE, moveSpinners[0].getSelectedItemPosition());
        outState.putInt(MOVE_TWO, moveSpinners[1].getSelectedItemPosition());
        outState.putInt(MOVE_THREE, moveSpinners[2].getSelectedItemPosition());
        outState.putInt(MOVE_FOUR, moveSpinners[3].getSelectedItemPosition());
        outState.putInt(LEVEL, levelSpinner.getSelectedItemPosition());
        outState.putString(NICKNAME, nicknameInput.getText().toString());
    }
}
