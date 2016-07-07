package com.app.pokebase.pokebase.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import com.app.pokebase.pokebase.R;
import com.app.pokebase.pokebase.database.DatabaseOpenHelper;
import com.app.pokebase.pokebase.fragments.MovesFragment;
import com.app.pokebase.pokebase.fragments.PokebaseFragment;
import com.app.pokebase.pokebase.fragments.TeamsFragment;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

/**
 * @author Tyler Wong
 */
public class MainActivity extends AppCompatActivity {
   private NavigationView mNavigationView;
   private DrawerLayout mDrawerLayout;
   private Toolbar mToolbar;
   private ImageView mProfilePicture;
   private TextView mUsernameView;
   private FragmentTransaction fragmentTransaction;
   private Fragment mCurrentFragment;
   private DatabaseOpenHelper mDatabaseHelper;

   private boolean mPokemonAdd;

   private final static String TRAINER = "Trainer ";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
      final View headerView = mNavigationView.getHeaderView(0);
      mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

      mProfilePicture = (ImageView) headerView.findViewById(R.id.profile_image);
      mUsernameView = (TextView) headerView.findViewById(R.id.username);

      mDatabaseHelper = DatabaseOpenHelper.getInstance(this);

      SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
      mUsernameView.setText(String.format(getString(R.string.trainer),
            pref.getString("username", "")));

      if (pref.getString("gender", "M").equals("M")) {
         mProfilePicture.setImageDrawable(getDrawable(R.drawable.boy));
      }
      else {
         mProfilePicture.setImageDrawable(getDrawable(R.drawable.girl));
      }

      mToolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(mToolbar);

      Intent intent = getIntent();
      mPokemonAdd = intent.getBooleanExtra("pokemonAdd", false);

      if (mPokemonAdd) {
         mNavigationView.getMenu().getItem(1).setChecked(true);
         PokebaseFragment pokebaseFragment = new PokebaseFragment();
         mCurrentFragment = pokebaseFragment;
         fragmentTransaction = getSupportFragmentManager().beginTransaction();
         fragmentTransaction.replace(R.id.frame, pokebaseFragment);
         fragmentTransaction.commit();
      }
      else {
         mNavigationView.getMenu().getItem(0).setChecked(true);
         TeamsFragment teamsFragment = new TeamsFragment();
         mCurrentFragment = teamsFragment;
         fragmentTransaction = getSupportFragmentManager().beginTransaction();
         fragmentTransaction.replace(R.id.frame, teamsFragment);
         fragmentTransaction.commit();
      }

      mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

         @Override
         public boolean onNavigationItemSelected(MenuItem menuItem) {
            mDrawerLayout.closeDrawers();

            switch (menuItem.getItemId()) {
               case R.id.teams:
                  TeamsFragment teamsFragment = new TeamsFragment();
                  mCurrentFragment = teamsFragment;
                  fragmentTransaction = getSupportFragmentManager().beginTransaction();
                  fragmentTransaction.replace(R.id.frame, teamsFragment);
                  fragmentTransaction.commit();
                  return true;

               case R.id.pokebase:
                  PokebaseFragment pokeFragment = new PokebaseFragment();
                  mCurrentFragment = pokeFragment;
                  fragmentTransaction = getSupportFragmentManager().beginTransaction();
                  fragmentTransaction.replace(R.id.frame, pokeFragment);
                  fragmentTransaction.commit();
                  return true;

               case R.id.moves:
                  MovesFragment movesFragment = new MovesFragment();
                  mCurrentFragment = movesFragment;
                  fragmentTransaction = getSupportFragmentManager().beginTransaction();
                  fragmentTransaction.replace(R.id.frame, movesFragment);
                  fragmentTransaction.commit();
                  return true;

               default:
                  return false;
            }
         }
      });

      ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
            this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer) {

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

      mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
      actionBarDrawerToggle.syncState();
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
         default:
            return false;
      }
      return true;
   }

   private void showClearAllTeamsDialog() {
      new LovelyStandardDialog(this)
            .setIcon(R.drawable.ic_info_white_24dp)
            .setTitle(R.string.clear_all_teams)
            .setMessage(R.string.clear_all_teams_prompt)
            .setCancelable(true)
            .setPositiveButton(R.string.yes, new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                  clearAllTeams();
               }
            })
            .setNegativeButton(R.string.no, null)
            .setTopColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .show();
   }

   private void clearAllTeams() {
      mDatabaseHelper.deleteAllTeams();
      if (mCurrentFragment instanceof TeamsFragment) {
         ((TeamsFragment) mCurrentFragment).refreshAdapter();
      }
      Toast.makeText(this, getResources().getString(R.string.cleared_teams),
            Toast.LENGTH_SHORT).show();
   }

   private void hideKeyboard() {
      View view = getCurrentFocus();

      if (view != null) {
         InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
         imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
      }
   }

   @Override
   public void onBackPressed() {
      if (mPokemonAdd) {
         Intent toIntent = new Intent(this, TeamViewActivity.class);
         toIntent.putExtras(getIntent().getExtras());
         startActivity(toIntent);
      }
   }
}
