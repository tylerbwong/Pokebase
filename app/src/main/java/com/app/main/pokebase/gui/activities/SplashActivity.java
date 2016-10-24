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

package com.app.main.pokebase.gui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.main.pokebase.R;
import com.app.main.pokebase.model.database.DatabaseOpenHelper;
import com.app.main.pokebase.model.utilities.Typefaces;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Tyler Wong
 */
public class SplashActivity extends AppCompatActivity {
   @BindView(R.id.title_label)
   TextView mTitleLabel;

   public final static String ACTIVITY_PREF = "ActivityPREF";
   public final static String INTRO_FINISHED = "appIntroFinished";
   public final static String SIGN_UP_COMPLETE = "signUpComplete";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_splash);
      ButterKnife.bind(this);

      Typeface robotoLight = Typefaces.get(this, Typefaces.ROBOTO_PATH);

      if (robotoLight != null) {
         mTitleLabel.setTypeface(robotoLight);
      }

      new BackgroundTask(this).execute();
   }

   private class BackgroundTask extends AsyncTask<Void, Void, Drawable> {
      private Context mContext;

      public BackgroundTask(Context context) {
         this.mContext = context;
      }

      @Override
      protected Drawable doInBackground(Void... voids) {
         DatabaseOpenHelper.getInstance(SplashActivity.this);
         return ContextCompat.getDrawable(mContext, R.drawable.ic_material_pokeball);
      }

      @Override
      protected void onPostExecute(Drawable loaded) {
         super.onPostExecute(loaded);
         ImageView logoImage = (ImageView) findViewById(R.id.logo_image);
         logoImage.setImageDrawable(loaded);

         SharedPreferences pref = getSharedPreferences(ACTIVITY_PREF, Context.MODE_PRIVATE);
         boolean appIntroFinished = pref.getBoolean(INTRO_FINISHED, false);
         boolean signUpComplete = pref.getBoolean(SIGN_UP_COMPLETE, false);

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
