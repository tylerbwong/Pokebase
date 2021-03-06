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

package me.tylerbwong.pokebase.gui.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.tylerbwong.pokebase.R;
import me.tylerbwong.pokebase.model.utilities.Typefaces;

/**
 * @author Tyler Wong
 */
public class IntroTeamFragment extends Fragment {
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.trainer)
    ImageView trainerImage;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intro_team_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        trainerImage.setImageResource(R.drawable.trainer);

        Typeface robotoLight = Typefaces.get(getContext(), Typefaces.ROBOTO_PATH);

        if (robotoLight != null) {
            description.setTypeface(robotoLight);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
