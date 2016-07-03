package com.app.pokebase.pokebase.activities;

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

import com.app.pokebase.pokebase.R;
import com.app.pokebase.pokebase.utilities.Typefaces;

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

   final static String ROBOTO_PATH = "fonts/roboto-light.ttf";

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_gender);

      mRobotoLight = Typefaces.get(this, ROBOTO_PATH);

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
      SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
      SharedPreferences.Editor ed = pref.edit();
      ed.putBoolean("signUpComplete", true);
      if (mIsBoy) {
         ed.putString("gender", "M");
      }
      else {
         ed.putString("gender", "F");
      }
      ed.apply();
      Intent mainIntent = new Intent(this, MainActivity.class);
      startActivity(mainIntent);
   }
}
