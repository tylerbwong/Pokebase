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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.google.firebase.analytics.FirebaseAnalytics;

import me.tylerbwong.pokebase.gui.fragments.IntroMoveFragment;
import me.tylerbwong.pokebase.gui.fragments.IntroPokebaseFragment;
import me.tylerbwong.pokebase.gui.fragments.IntroTeamFragment;

/**
 * @author Tyler Wong
 */
public class IntroActivity extends AppIntro2 {
    private FirebaseAnalytics analytics;

    private static final String SKIP_PRESSED = "skip_pressed";
    private static final String INTRO_SKIPPED = "intro_skipped";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = FirebaseAnalytics.getInstance(this);

        addSlide(new IntroPokebaseFragment());
        addSlide(new IntroMoveFragment());
        addSlide(new IntroTeamFragment());
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Bundle bundle = new Bundle();
        bundle.putBoolean(SKIP_PRESSED, true);
        analytics.logEvent(INTRO_SKIPPED, bundle);
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
