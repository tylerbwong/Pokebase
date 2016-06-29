package com.app.pokebase.pokebase.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.pokebase.pokebase.R;
import com.app.pokebase.pokebase.utilities.Typefaces;

/**
 * @author Tyler Wong
 */
public class StartActivity extends AppCompatActivity {
   private TextView mTitleLabel;
   private Button mEnterButton;

   Typeface robotoLight;

   final static String ROBOTO_PATH = "fonts/roboto-light.ttf";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      robotoLight = Typefaces.get(this, ROBOTO_PATH);

      setContentView(R.layout.activity_start);

      mTitleLabel = (TextView) findViewById(R.id.title_label);
      mEnterButton = (Button) findViewById(R.id.enter_button);

      if (robotoLight != null) {
         mTitleLabel.setTypeface(robotoLight);
         mEnterButton.setTypeface(robotoLight);
      }

      boolean logoTransition = getIntent().getBooleanExtra("logo_transition", false);
      if (logoTransition) {
         overridePendingTransition(R.anim.slow_transition, R.anim.slow_transition);
      }
   }

   public void nameEntry(View v) {
      Intent introIntent = new Intent(this, IntroActivity.class);
      startActivity(introIntent);
   }

   @Override
   public void onBackPressed() {

   }
}
