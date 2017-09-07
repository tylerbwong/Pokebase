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
@SuppressWarnings("unused")
public class GenderActivity extends AppCompatActivity {
    @BindView(R.id.title_label)
    TextView titleLabel;
    @BindView(R.id.boy_button)
    ImageButton boyButton;
    @BindView(R.id.girl_button)
    ImageButton girlButton;

    private boolean isBoy = true;

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
            titleLabel.setTypeface(robotoLight);
        }
    }

    @Override
    public void onBackPressed() {

    }

    @OnTouch(R.id.boy_button)
    public boolean boySelect() {
        isBoy = true;
        boyButton.setPressed(true);
        girlButton.setPressed(false);
        return true;
    }

    @SuppressWarnings("unused")
    @OnTouch(R.id.girl_button)
    public boolean girlSelect() {
        isBoy = false;
        girlButton.setPressed(true);
        boyButton.setPressed(false);
        return true;
    }

    @OnClick(R.id.gender_select)
    public void submitGender() {
        SharedPreferences pref = getSharedPreferences(SplashActivity.ACTIVITY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        ed.putBoolean(SplashActivity.SIGN_UP_COMPLETE, true);
        if (isBoy) {
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
