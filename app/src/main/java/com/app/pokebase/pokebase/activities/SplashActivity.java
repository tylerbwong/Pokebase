package com.app.pokebase.pokebase.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.pokebase.pokebase.R;
import com.app.pokebase.pokebase.utilities.Typefaces;

/**
 * @author Brittany Berlanga
 */
public class SplashActivity extends AppCompatActivity {
   private TextView mTitleLabel;
   private Typeface mRobotoLight;

   final static int SPLASH_DISPLAY_LENGTH = 1000;
   final static String ROBOTO_PATH = "fonts/roboto-light.ttf";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      mRobotoLight = Typefaces.get(this, ROBOTO_PATH);

      setContentView(R.layout.activity_splash);

      mTitleLabel = (TextView) findViewById(R.id.title_label);

      if (mRobotoLight != null) {
         mTitleLabel.setTypeface(mRobotoLight);
      }

      SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
      boolean loggedIn = pref.getBoolean("loggedIn", false);
      boolean appIntroFinished = pref.getBoolean("appIntroFinished", false);
      if (loggedIn) {
         Intent mainIntent = new Intent(this, MainActivity.class);
         startActivity(mainIntent);
         finish();
      }
      else if (appIntroFinished && !loggedIn) {
         Intent loginIntent = new Intent(this, LoginActivity.class);
         startActivity(loginIntent);
         finish();
      }
      else {
         Editor ed = pref.edit();
         ed.putBoolean("appIntroFinished", true);
         ed.apply();
      }
   }

   @Override
   protected void onResume() {
      super.onResume();

      new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {
            Intent startIntent = new Intent(SplashActivity.this, StartActivity.class);
            startIntent.putExtra("logo_transition", true);
            LinearLayout logo = (LinearLayout) findViewById(R.id.logo);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                  makeSceneTransitionAnimation(SplashActivity.this, logo, getString(R.string.logo_transition));
            SplashActivity.this.startActivity(startIntent, options.toBundle());
         }
      }, SPLASH_DISPLAY_LENGTH);
   }
}
