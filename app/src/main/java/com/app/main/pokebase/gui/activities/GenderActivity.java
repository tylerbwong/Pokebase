package com.app.main.pokebase.gui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.main.pokebase.R;
import com.app.main.pokebase.model.utilities.Typefaces;

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

   public final static String GENDER = "gender";
   public final static String MALE = "M";
   public final static String FEMALE = "F";

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
