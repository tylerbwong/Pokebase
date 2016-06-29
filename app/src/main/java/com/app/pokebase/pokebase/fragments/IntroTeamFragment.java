package com.app.pokebase.pokebase.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.pokebase.pokebase.R;
import com.app.pokebase.pokebase.utilities.Typefaces;

/**
 * @author Tyler Wong
 */
public class IntroTeamFragment extends Fragment {
   private TextView mDescription;

   Typeface robotoLight;

   final static String ROBOTO_PATH = "fonts/roboto-light.ttf";

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.intro_team_fragment, container, false);
      robotoLight = Typefaces.get(getContext(), ROBOTO_PATH);

      mDescription = (TextView) view.findViewById(R.id.description);

      if (robotoLight != null) {
         mDescription.setTypeface(robotoLight);
      }
      return view;
   }
}
