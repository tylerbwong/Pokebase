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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Tyler Wong
 */
public class IntroMoveFragment extends Fragment {
   @BindView(R.id.description) TextView mDescription;

   private Unbinder mUnbinder;

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.intro_move_fragment, container, false);
      mUnbinder = ButterKnife.bind(this, view);

      new LoadImage().execute();

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

   private class LoadImage extends AsyncTask<Void, Void, Drawable> {
      @Override
      protected Drawable doInBackground(Void... params) {
         return ContextCompat.getDrawable(getContext(), R.drawable.move_dialog);
      }

      @Override
      protected void onPostExecute(Drawable loaded) {
         super.onPostExecute(loaded);
         ImageView trainerImage = (ImageView) getActivity().findViewById(R.id.move_dialog);
         trainerImage.setImageDrawable(loaded);
      }
   }
}
