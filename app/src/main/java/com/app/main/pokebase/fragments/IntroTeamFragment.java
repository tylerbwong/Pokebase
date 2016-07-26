package com.app.main.pokebase.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.main.pokebase.R;
import com.app.main.pokebase.utilities.Typefaces;

/**
 * @author Tyler Wong
 */
public class IntroTeamFragment extends Fragment {
   private TextView mDescription;
   private Typeface mRobotoLight;

   final static String ROBOTO_PATH = "fonts/roboto-light.ttf";

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.intro_team_fragment, container, false);
      mRobotoLight = Typefaces.get(getContext(), ROBOTO_PATH);

      mDescription = (TextView) view.findViewById(R.id.description);

      if (mRobotoLight != null) {
         mDescription.setTypeface(mRobotoLight);
      }
      return view;
   }
}
