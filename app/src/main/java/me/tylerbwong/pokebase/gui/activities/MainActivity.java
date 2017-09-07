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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tylerbwong.pokebase.R;
import me.tylerbwong.pokebase.gui.fragments.ItemsFragment;
import me.tylerbwong.pokebase.gui.fragments.MovesFragment;
import me.tylerbwong.pokebase.gui.fragments.PokebaseFragment;
import me.tylerbwong.pokebase.gui.fragments.TeamsFragment;
import me.tylerbwong.pokebase.model.database.DatabaseOpenHelper;

/**
 * @author Tyler Wong
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ImageView profilePicture;
    private TextView usernameView;

    private Fragment currentFragment;
    private DatabaseOpenHelper databaseHelper;

    private int lastClicked;
    private int menuId;
    private boolean pokemonAdd;

    private static final int DELAY = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        databaseHelper = DatabaseOpenHelper.getInstance(this);

        View headerView = navigationView.getHeaderView(0);
        profilePicture = headerView.findViewById(R.id.profile_image);
        usernameView = headerView.findViewById(R.id.username);

        SharedPreferences pref = getSharedPreferences(SplashActivity.ACTIVITY_PREF, Context.MODE_PRIVATE);
        usernameView.setText(String.format(getString(R.string.trainer),
                pref.getString(SignUpActivity.USERNAME, "")));

        int image;

        if (pref.getString(GenderActivity.GENDER, GenderActivity.MALE).equals(GenderActivity.MALE)) {
            image = R.drawable.boy;
        }
        else {
            image = R.drawable.girl;
        }

        Glide.with(this)
                .load(image)
                .into(profilePicture);

        String fragTag;

        pokemonAdd = getIntent().getBooleanExtra(TeamViewActivity.POKEMON_ADD, false);

        if (pokemonAdd) {
            fragTag = PokebaseFragment.class.getSimpleName();
            navigationView.getMenu().getItem(1).setChecked(true);
            currentFragment = getSupportFragmentManager().findFragmentByTag(fragTag);
            if (currentFragment == null) {
                currentFragment = new PokebaseFragment();
            }
            lastClicked = R.id.pokebase;
        }
        else {
            fragTag = TeamsFragment.class.getSimpleName();
            navigationView.getMenu().getItem(0).setChecked(true);
            currentFragment = getSupportFragmentManager().findFragmentByTag(fragTag);
            if (currentFragment == null) {
                currentFragment = new TeamsFragment();
            }
            lastClicked = R.id.teams;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, currentFragment, fragTag).commit();

        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                hideKeyboard();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideKeyboard();
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences(SplashActivity.ACTIVITY_PREF, Context.MODE_PRIVATE);
        usernameView.setText(String.format(getString(R.string.trainer),
                pref.getString(SignUpActivity.USERNAME, "Error")));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.clear_all_teams_action).setVisible(true);
        menu.findItem(R.id.number_action).setVisible(false);
        menu.findItem(R.id.name_action).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_all_teams_action:
                showClearAllTeamsDialog();
                break;
            case R.id.settings:
                switchToSettings();
                break;
            case R.id.exit_action:
                showExitDialog();
                break;
            default:
                return false;
        }
        return true;
    }

    private void switchToSettings() {
        startActivity(new Intent(MainActivity.this, PreferencesActivity.class));
    }

    private void showClearAllTeamsDialog() {
        new LovelyStandardDialog(this)
                .setIcon(R.drawable.ic_info_white_24dp)
                .setTitle(R.string.clear_all_teams)
                .setMessage(R.string.clear_all_teams_prompt)
                .setCancelable(true)
                .setPositiveButton(R.string.yes, v -> clearAllTeams())
                .setNegativeButton(R.string.no, null)
                .setTopColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .show();
    }

    private void clearAllTeams() {
        databaseHelper.deleteAllTeams()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    if (currentFragment instanceof TeamsFragment) {
                        ((TeamsFragment) currentFragment).refreshAdapter();
                    }
                    Toast.makeText(this, getResources().getString(R.string.cleared_teams),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showExitDialog() {
        new LovelyStandardDialog(this)
                .setIcon(R.drawable.ic_info_white_24dp)
                .setTitle(R.string.close_title)
                .setMessage(R.string.close_prompt)
                .setCancelable(true)
                .setPositiveButton(R.string.yes, v -> {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }).setNegativeButton(R.string.no, null)
                .setTopColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .show();
    }

    @Override
    public void onBackPressed() {
        if (pokemonAdd) {
            Intent toIntent = new Intent(this, TeamViewActivity.class);
            toIntent.putExtras(getIntent().getExtras());
            startActivity(toIntent);
        }
        else {
            showExitDialog();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers();
        new Handler().postDelayed(() -> {
            String fragTag = "";
            menuId = item.getItemId();

            switch (menuId) {
                case R.id.teams:
                    fragTag = TeamsFragment.class.getSimpleName();
                    currentFragment = getSupportFragmentManager().findFragmentByTag(fragTag);
                    if (currentFragment == null) {
                        currentFragment = new TeamsFragment();
                    }
                    break;

                case R.id.pokebase:
                    fragTag = PokebaseFragment.class.getSimpleName();
                    currentFragment = getSupportFragmentManager().findFragmentByTag(fragTag);
                    if (currentFragment == null) {
                        currentFragment = new PokebaseFragment();
                    }
                    break;

                case R.id.moves:
                    fragTag = MovesFragment.class.getSimpleName();
                    currentFragment = getSupportFragmentManager().findFragmentByTag(fragTag);
                    if (currentFragment == null) {
                        currentFragment = new MovesFragment();
                    }
                    break;

                case R.id.items:
                    fragTag = ItemsFragment.class.getSimpleName();
                    currentFragment = getSupportFragmentManager().findFragmentByTag(fragTag);
                    if (currentFragment == null) {
                        currentFragment = new ItemsFragment();
                    }
                    break;

                case R.id.settings:
                    switchToSettings();
                    break;
            }

            if (menuId != R.id.settings && menuId != lastClicked) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, currentFragment, fragTag).commit();
                lastClicked = menuId;
            }
        }, DELAY);
        return true;
    }
}
