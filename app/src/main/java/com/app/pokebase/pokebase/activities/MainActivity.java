package com.app.pokebase.pokebase.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pokebase.pokebase.R;
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

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
      final View headerView = mNavigationView.getHeaderView(0);
      mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

      mProfilePicture = (ImageView) headerView.findViewById(R.id.profile_image);
      mUsernameView = (TextView) headerView.findViewById(R.id.username);

      SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
      mUsernameView.setText(pref.getString("username", ""));

      if (pref.getString("gender", "M").equals("M")) {
         mProfilePicture.setImageDrawable(getDrawable(R.drawable.boy));
      }
      else {
         mProfilePicture.setImageDrawable(getDrawable(R.drawable.girl));
      }

      mToolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(mToolbar);

      mNavigationView.getMenu().getItem(0).setChecked(true);
      TeamsFragment eventsFragment = new TeamsFragment();
      fragmentTransaction = getSupportFragmentManager().beginTransaction();
      fragmentTransaction.replace(R.id.frame, eventsFragment);
      fragmentTransaction.commit();

      mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

         @Override
         public boolean onNavigationItemSelected(MenuItem menuItem) {
            mDrawerLayout.closeDrawers();

            switch (menuItem.getItemId()) {
               case R.id.teams:
                  TeamsFragment eventsFragment = new TeamsFragment();
                  fragmentTransaction = getSupportFragmentManager().beginTransaction();
                  fragmentTransaction.replace(R.id.frame, eventsFragment);
                  fragmentTransaction.commit();
                  return true;

               case R.id.pokebase:
                  PokebaseFragment pokeFragment = new PokebaseFragment();
                  fragmentTransaction = getSupportFragmentManager().beginTransaction();
                  fragmentTransaction.replace(R.id.frame, pokeFragment);
                  fragmentTransaction.commit();
                  return true;

               case R.id.logout:
                  showLogoutDialog();
                  return true;

               default:
                  return false;
            }
         }
      });

      ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
            this, mDrawerLayout, mToolbar, R.string.openDrawer, R.string.closeDrawer) {

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

   private void showLogoutDialog() {
      new LovelyStandardDialog(this)
            .setIcon(R.drawable.ic_info_white_48dp)
            .setTitle(R.string.logout_question).setCancelable(true).setPositiveButton(R.string.yes, new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            logout();
         }
      }).setNegativeButton(R.string.no, null)
            .setTopColor(ContextCompat.getColor(this, R.color.colorPrimary)).show();
   }

   private void logout() {
      Intent loginIntent = new Intent(this, LoginActivity.class);
      startActivity(loginIntent);
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

   }
}
