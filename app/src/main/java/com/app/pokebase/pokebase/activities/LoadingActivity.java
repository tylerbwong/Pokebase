package com.app.pokebase.pokebase.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.app.pokebase.pokebase.R;
import com.app.pokebase.pokebase.utilities.Typefaces;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Tyler Wong
 */
public class LoadingActivity extends AppCompatActivity {
   private TextView mTitlelabel;

   Typeface robotoLight;

   final static String ROBOTO_PATH = "fonts/roboto-light.ttf";
   final static int LOADING_TIME = 3000;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_loading);
      robotoLight = Typefaces.get(this, ROBOTO_PATH);

      mTitlelabel = (TextView) findViewById(R.id.title_label);

      if (robotoLight != null) {
         mTitlelabel.setTypeface(robotoLight);
      }

      TimerTask timerTask = new TimerTask() {
         @Override
         public void run() {
            switchToMain();
         }
      };

      Timer timer = new Timer();
      timer.schedule(timerTask, LOADING_TIME);
   }

   @Override
   public void onBackPressed() {

   }

   private void switchToMain() {
      Intent mainIntent = new Intent(this, MainActivity.class);
      startActivity(mainIntent);
   }
}
