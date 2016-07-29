package com.app.main.pokebase.gui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.main.pokebase.R;
import com.app.main.pokebase.model.utilities.Typefaces;

/**
 * @author Tyler Wong
 */
public class GenderActivity extends AppCompatActivity {
   private TextView mTitleLabel;
   private ImageButton mBoyButton;
   private ImageButton mGirlButton;
   private Button mGoButton;

   private boolean mIsBoy = true;
   private Typeface mRobotoLight;

   public final static String GENDER = "gender";
   public final static String MALE = "M";
   public final static String FEMALE = "F";

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_gender);

      mRobotoLight = Typefaces.get(this, Typefaces.ROBOTO_PATH);

      mTitleLabel = (TextView) findViewById(R.id.title_label);
      mBoyButton = (ImageButton) findViewById(R.id.boy_button);
      mGirlButton = (ImageButton) findViewById(R.id.girl_button);
      mGoButton = (Button) findViewById(R.id.gender_select);

      mGoButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            submitGender();
         }
      });

      mBoyButton.setOnTouchListener(new View.OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
            mIsBoy = true;
            mBoyButton.setPressed(true);
            mGirlButton.setPressed(false);
            return true;
         }
      });

      mGirlButton.setOnTouchListener(new View.OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
            mIsBoy = false;
            mGirlButton.setPressed(true);
            mBoyButton.setPressed(false);
            return true;
         }
      });

      if (mRobotoLight != null) {
         mTitleLabel.setTypeface(mRobotoLight);
      }
   }

   @Override
   public void onBackPressed() {

   }

   private void submitGender() {
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
      Intent mainIntent = new Intent(this, MainActivity.class);
      startActivity(mainIntent);
   }
}
