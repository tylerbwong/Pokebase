package com.app.main.pokebase.gui.fragments;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.main.pokebase.R;
import com.app.main.pokebase.model.utilities.Typefaces;

/**
 * @author Tyler Wong
 */
public class IntroTeamFragment extends Fragment {
   private TextView mDescription;
   private Typeface mRobotoLight;

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.intro_team_fragment, container, false);
      new LoadImage().execute();

      mRobotoLight = Typefaces.get(getContext(), Typefaces.ROBOTO_PATH);

      mDescription = (TextView) view.findViewById(R.id.description);

      if (mRobotoLight != null) {
         mDescription.setTypeface(mRobotoLight);
      }
      return view;
   }

   private class LoadImage extends AsyncTask<Void, Void, Drawable> {
      @Override
      protected Drawable doInBackground(Void... params) {
         return ContextCompat.getDrawable(getContext(), R.drawable.trainer);
      }

      @Override
      protected void onPostExecute(Drawable loaded) {
         ImageView trainerImage = (ImageView) getActivity().findViewById(R.id.trainer);
         trainerImage.setImageDrawable(loaded);
      }

      @Override
      protected void onPreExecute() {

      }

      @Override
      protected void onProgressUpdate(Void... values) {

      }
   }
}
