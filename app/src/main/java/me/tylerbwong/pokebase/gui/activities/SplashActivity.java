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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.tylerbwong.pokebase.R;
import me.tylerbwong.pokebase.model.utilities.Typefaces;

/**
 * @author Tyler Wong
 */
public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.title_label)
    TextView titleLabel;
    @BindView(R.id.logo_image)
    ImageView logoImage;

    public static final String ACTIVITY_PREF = "ActivityPREF";
    public static final String INTRO_FINISHED = "appIntroFinished";
    public static final String SIGN_UP_COMPLETE = "signUpComplete";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        Typeface robotoLight = Typefaces.get(this, Typefaces.ROBOTO_PATH);

        if (robotoLight != null) {
            titleLabel.setTypeface(robotoLight);
        }

        Glide.with(this)
                .load(R.drawable.ic_material_pokeball)
                .into(logoImage);

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
