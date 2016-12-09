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
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import me.tylerbwong.pokebase.R;
import me.tylerbwong.pokebase.model.utilities.Typefaces;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * @author Tyler Wong
 */
public class GenderActivity extends AppCompatActivity {
   @BindView(R.id.title_label)
   TextView mTitleLabel;
   @BindView(R.id.boy_button)
   ImageButton mBoyButton;
   @BindView(R.id.girl_button)
   ImageButton mGirlButton;
   @BindView(R.id.gender_select)
   Button mGoButton;

   private boolean mIsBoy = true;

   public static final String GENDER = "gender";
   public static final String MALE = "M";
   public static final String FEMALE = "F";

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_gender);
      ButterKnife.bind(this);

      Typeface robotoLight = Typefaces.get(this, Typefaces.ROBOTO_PATH);

      if (robotoLight != null) {
         mTitleLabel.setTypeface(robotoLight);
      }
   }

   @Override
   public void onBackPressed() {

   }

   @OnTouch(R.id.boy_button)
   public boolean boySelect() {
      mIsBoy = true;
      mBoyButton.setPressed(true);
      mGirlButton.setPressed(false);
      return true;
   }

   @OnTouch(R.id.girl_button)
   public boolean girlSelect() {
      mIsBoy = false;
      mGirlButton.setPressed(true);
      mBoyButton.setPressed(false);
      return true;
   }

   @OnClick(R.id.gender_select)
   public void submitGender() {
      SharedPreferences pref = getSharedPreferences(SplashActivity.ACTIVITY_PREF, Context.MODE_PRIVATE);
      SharedPreferences.Editor ed = pref.edit();
      ed.putBoolean(SplashActivity.SIGN_UP_COMPLETE, true);
      if (mIsBoy) {
         ed.putString(GENDER, MALE);
      }
      else {
         ed.putString(GENDER, FEMALE);
      }
      ed.apply();
      startActivity(new Intent(this, MainActivity.class));
      finish();
   }
}
