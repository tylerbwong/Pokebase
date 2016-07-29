package com.app.main.pokebase.gui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.app.main.pokebase.gui.fragments.IntroTeamFragment;
import com.app.main.pokebase.gui.fragments.IntroPokebaseFragment;
import com.github.paolorotolo.appintro.AppIntro;

/**
 * @author Tyler Wong
 */
public class IntroActivity extends AppIntro {
   private IntroPokebaseFragment mIntroPokebaseFragment;
   private IntroTeamFragment mIntroTeamFragment;

   @Override
   public void init(Bundle savedInstanceState) {
      mIntroPokebaseFragment = new IntroPokebaseFragment();
      mIntroTeamFragment = new IntroTeamFragment();

      addSlide(mIntroPokebaseFragment);
      addSlide(mIntroTeamFragment);

      showSkipButton(false);
   }

   @Override
   public void onSkipPressed() {

   }

   @Override
   public void onDonePressed() {
      SharedPreferences pref = getSharedPreferences(SplashActivity.ACTIVITY_PREF, Context.MODE_PRIVATE);
      SharedPreferences.Editor ed = pref.edit();
      ed.putBoolean(SplashActivity.INTRO_FINISHED, true);
      ed.apply();
      Intent signUpIntent = new Intent(this, SignUpActivity.class);
      startActivity(signUpIntent);
   }

   @Override
   public void onSlideChanged() {

   }

   @Override
   public void onNextPressed() {

   }

   @Override
   public void onBackPressed() {

   }
}
