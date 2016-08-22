package com.app.main.pokebase.gui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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

import com.app.main.pokebase.R;
import com.app.main.pokebase.gui.fragments.ItemsFragment;
import com.app.main.pokebase.gui.fragments.MovesFragment;
import com.app.main.pokebase.gui.fragments.PokebaseFragment;
import com.app.main.pokebase.gui.fragments.TeamsFragment;
import com.app.main.pokebase.model.database.DatabaseOpenHelper;
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
   private FragmentTransaction mFragmentTransaction;
   private Fragment mCurrentFragment;
   private DatabaseOpenHelper mDatabaseHelper;

   private boolean mPokemonAdd;

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

      SharedPreferences pref = getSharedPreferences(SplashActivity.ACTIVITY_PREF, Context.MODE_PRIVATE);
      mUsernameView.setText(String.format(getString(R.string.trainer),
            pref.getString(SignUpActivity.USERNAME, "")));

      new LoadGenderImage().execute((pref.getString(GenderActivity.GENDER, GenderActivity.MALE)));

      mToolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(mToolbar);

      new SetupTask().execute();

      mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

         @Override
         public boolean onNavigationItemSelected(MenuItem menuItem) {
            mDrawerLayout.closeDrawers();
            mFragmentTransaction = getSupportFragmentManager().beginTransaction();

            switch (menuItem.getItemId()) {
               case R.id.teams:
                  mCurrentFragment = new TeamsFragment();
                  break;

               case R.id.pokebase:
                  mCurrentFragment = new PokebaseFragment();
                  break;

               case R.id.moves:
                  mCurrentFragment = new MovesFragment();
                  break;

               case R.id.items:
                  mCurrentFragment = new ItemsFragment();
                  break;

               case R.id.settings:
                  switchToSettings();
                  break;
            }

            mFragmentTransaction.replace(R.id.frame, mCurrentFragment);
            mFragmentTransaction.commit();
            return true;
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

   private void showExitDialog() {
      new LovelyStandardDialog(this)
            .setIcon(R.drawable.ic_info_white_24dp)
            .setTitle(R.string.close_title)
            .setMessage(R.string.close_prompt)
            .setCancelable(true)
            .setPositiveButton(R.string.yes, new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                  Intent intent = new Intent(Intent.ACTION_MAIN);
                  intent.addCategory(Intent.CATEGORY_HOME);
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  startActivity(intent);
               }
            }).setNegativeButton(R.string.no, null)
            .setTopColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .show();
   }

   @Override
   public void onBackPressed() {
      if (mPokemonAdd) {
         Intent toIntent = new Intent(this, TeamViewActivity.class);
         toIntent.putExtras(getIntent().getExtras());
         startActivity(toIntent);
      }
      else {
         showExitDialog();
      }
   }

   private class LoadGenderImage extends AsyncTask<String, Void, Drawable> {
      @Override
      protected Drawable doInBackground(String... params) {
         Drawable image;
         if (params[0].equals(GenderActivity.MALE)) {
            image = getDrawable(R.drawable.boy);
         }
         else {
            image = getDrawable(R.drawable.girl);
         }
         return image;
      }

      @Override
      protected void onPostExecute(Drawable loaded) {
         super.onPostExecute(loaded);
         mProfilePicture.setImageDrawable(loaded);
      }
   }

   private class SetupTask extends AsyncTask<Void, Void, Void> {
      @Override
      protected Void doInBackground(Void... params) {
         return null;
      }

      @Override
      protected void onPostExecute(Void result) {
         super.onPostExecute(result);

         Intent intent = getIntent();
         mPokemonAdd = intent.getBooleanExtra(TeamViewActivity.POKEMON_ADD, false);

         if (mPokemonAdd) {
            mNavigationView.getMenu().getItem(1).setChecked(true);
            mCurrentFragment = new PokebaseFragment();
         }
         else {
            mNavigationView.getMenu().getItem(0).setChecked(true);
            mCurrentFragment = new TeamsFragment();
         }

         mFragmentTransaction = getSupportFragmentManager().beginTransaction();
         mFragmentTransaction.replace(R.id.frame, mCurrentFragment);
         mFragmentTransaction.commit();
      }
   }
}
