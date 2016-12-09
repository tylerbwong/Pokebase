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

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.tylerbwong.pokebase.R;
import me.tylerbwong.pokebase.model.utilities.Typefaces;

/**
 * @author Tyler Wong
 */
public class IntroPokebaseFragment extends Fragment {
   @BindView(R.id.description)
   TextView mDescription;
   @BindView(R.id.small_img)
   ImageView mSmallImage;
   @BindView(R.id.med_img)
   ImageView mMedImage;
   @BindView(R.id.big_img)
   ImageView mBigImage;

   private Unbinder mUnbinder;

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.intro_pokebase_fragment, container, false);
      mUnbinder = ButterKnife.bind(this, view);

      Glide.with(this)
            .load(R.drawable.pokedex)
            .into(mSmallImage);
      Glide.with(this)
            .load(R.drawable.charizard)
            .into(mMedImage);
      Glide.with(this)
            .load(R.drawable.starters)
            .into(mBigImage);

      Typeface robotoLight = Typefaces.get(getContext(), Typefaces.ROBOTO_PATH);

      if (robotoLight != null) {
         mDescription.setTypeface(robotoLight);
      }
      return view;
   }

   @Override
   public void onDestroyView() {
      super.onDestroyView();
      mUnbinder.unbind();
   }
}
