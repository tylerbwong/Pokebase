package com.app.main.pokebase.gui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.app.main.pokebase.gui.fragments.IntroMoveFragment;
import com.app.main.pokebase.gui.fragments.IntroPokebaseFragment;
import com.app.main.pokebase.gui.fragments.IntroTeamFragment;
import com.github.paolorotolo.appintro.AppIntro2;

/**
 * @author Tyler Wong
 */
public class IntroActivity extends AppIntro2 {
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      addSlide(new IntroPokebaseFragment());
      addSlide(new IntroMoveFragment());
      addSlide(new IntroTeamFragment());
   }

   @Override
   public void onSkipPressed(Fragment currentFragment) {
      super.onSkipPressed(currentFragment);
      done();
   }

   @Override
   public void onDonePressed(Fragment currentFragment) {
      super.onDonePressed(currentFragment);
      done();
   }

   @Override
   public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
      super.onSlideChanged(oldFragment, newFragment);
   }

   @Override
   public void onBackPressed() {

   }

   private void done() {
      SharedPreferences pref = getSharedPreferences(SplashActivity.ACTIVITY_PREF, Context.MODE_PRIVATE);
      SharedPreferences.Editor ed = pref.edit();
      ed.putBoolean(SplashActivity.INTRO_FINISHED, true);
      ed.apply();
      startActivity(new Intent(this, SignUpActivity.class));
      finish();
   }
}
