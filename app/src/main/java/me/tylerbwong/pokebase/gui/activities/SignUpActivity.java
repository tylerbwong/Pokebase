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
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import me.tylerbwong.pokebase.R;
import me.tylerbwong.pokebase.model.utilities.Typefaces;

/**
 * @author Tyler Wong
 */
@SuppressWarnings("unused")
public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.title_label)
    TextView titleLabel;
    @BindView(R.id.name_input)
    TextInputEditText nameInput;
    @BindView(R.id.name_count)
    TextView nameCount;
    @BindView(R.id.create_user)
    Button createButton;

    private boolean hasText = false;

    public static final String USERNAME = "username";
    private static final String MAX_LENGTH = "/15";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        createButton.setEnabled(false);

        Typeface robotoLight = Typefaces.get(this, Typefaces.ROBOTO_PATH);

        if (robotoLight != null) {
            titleLabel.setTypeface(robotoLight);
            nameInput.setTypeface(robotoLight);
            nameCount.setTypeface(robotoLight);
        }
    }

    private void checkFields() {
        if (hasText) {
            createButton.setEnabled(true);
        }
        else {
            createButton.setEnabled(false);
        }
    }

    @OnTextChanged(R.id.name_input)
    public void textChanged(CharSequence sequence) {
        String charLeft = sequence.length() + MAX_LENGTH;

        if (sequence.toString().trim().length() == 0) {
            hasText = false;
        }
        else {
            hasText = true;
        }
        checkFields();
        nameCount.setText(charLeft);
    }

    @OnClick(R.id.create_user)
    public void createUser() {
        SharedPreferences pref = getSharedPreferences(SplashActivity.ACTIVITY_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        ed.putString(USERNAME, nameInput.getText().toString());
        ed.apply();
        startActivity(new Intent(this, GenderActivity.class));
    }

    @OnClick(R.id.exit_button)
    public void close() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}
