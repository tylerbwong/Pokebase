package com.app.main.pokebase.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.app.main.pokebase.R;
import com.app.main.pokebase.database.DatabaseOpenHelper;
import com.app.main.pokebase.utilities.Typefaces;

/**
 * @author Brittany Berlanga
 */
public class SplashActivity extends AppCompatActivity {

   private TextView mTitleLabel;

   private Typeface mRobotoLight;

   final static String ROBOTO_PATH = "fonts/roboto-light.ttf";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      mRobotoLight = Typefaces.get(this, ROBOTO_PATH);
      setContentView(R.layout.activity_splash);

      mTitleLabel = (TextView) findViewById(R.id.title_label);
      mTitleLabel.setTypeface(mRobotoLight);

      new DatabaseTask().execute();
   }

   private class DatabaseTask extends AsyncTask<Void, Void, Void> {
      @Override
      protected Void doInBackground(Void... voids) {
         DatabaseOpenHelper.getInstance(SplashActivity.this);
         return null;
      }

      @Override
      protected void onPostExecute(Void aVoid) {
         super.onPostExecute(aVoid);

         SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
         boolean appIntroFinished = pref.getBoolean("appIntroFinished", false);
         boolean signUpComplete = pref.getBoolean("signUpComplete", false);

         Intent resultIntent;

         if (appIntroFinished && signUpComplete) {
            resultIntent = new Intent(SplashActivity.this, MainActivity.class);
         }
         else if (appIntroFinished && !signUpComplete) {
            resultIntent = new Intent(SplashActivity.this, SignUpActivity.class);
         }
         else {
            resultIntent = new Intent(SplashActivity.this, IntroActivity.class);
         }

         startActivity(resultIntent);
         finish();
      }
   }
}
