package com.app.main.pokebase.gui.fragments;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.ListView;

import com.app.main.pokebase.R;

/**
 * @author Tyler Wong
 */
public class PreferencesFragment extends PreferenceFragment implements
      Preference.OnPreferenceClickListener {
   private Preference mUpdatePreference;
   private Preference mVersionPreference;

   private boolean mListStyled = false;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      View rootView = getView();
      ListView list = null;
      if (rootView != null) {
         list = (ListView) rootView.findViewById(android.R.id.list);
      }
      if (list != null) {
         list.setDivider(null);
      }

      PackageInfo packageInfo = null;

      try {
         packageInfo = getActivity().getPackageManager().getPackageInfo(
               getActivity().getPackageName(), 0);
      }
      catch (PackageManager.NameNotFoundException e) {

      }

      addPreferencesFromResource(R.xml.preferences);
      mUpdatePreference = getPreferenceScreen().findPreference(getString(R.string.update_key));
      mVersionPreference = getPreferenceScreen().findPreference(getString(R.string.version_key));

      mUpdatePreference.setOnPreferenceClickListener(this);

      if (packageInfo != null) {
         mVersionPreference.setSummary(packageInfo.versionName);
      }
   }

   @Override
   public void onResume() {
      super.onResume();
      mUpdatePreference.setSummary(R.string.check_updates);

      if (!mListStyled) {
         View rootView = getView();
         if (rootView != null) {
            ListView list = (ListView) rootView.findViewById(android.R.id.list);
            list.setPadding(0, 0, 0, 0);
            list.setDivider(null);
            //any other styling call
            mListStyled = true;
         }
      }
   }

   @Override
   public boolean onPreferenceClick(Preference preference) {
      if(preference.getKey().equals(getString(R.string.update_key))){
         preference.setSummary(getString(R.string.checking));

         Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.update_url)));
         startActivity(browserIntent);
      }
      return false;
   }
}
