package com.app.main.pokebase.gui.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.app.main.pokebase.R;
import com.app.main.pokebase.gui.adapters.MoveListAdapter;
import com.app.main.pokebase.gui.adapters.TextViewSpinnerAdapter;
import com.app.main.pokebase.gui.views.AnimatedRecyclerView;
import com.app.main.pokebase.model.database.DatabaseOpenHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Tyler Wong
 */
public class MovesFragment extends Fragment implements AdapterView.OnItemSelectedListener {
   @BindView(R.id.type_spinner) Spinner mTypeSpinner;
   @BindView(R.id.class_spinner) Spinner mClassSpinner;
   @BindView(R.id.moves_list) AnimatedRecyclerView mMovesList;

   private DatabaseOpenHelper mDatabaseHelper;
   private Unbinder mUnbinder;
   private String[] mMoves;

   private final static String TYPES = "Types";
   private final static String CLASSES = "Classes";

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      getActivity().setTheme(R.style.PokemonEditorTheme);
      mDatabaseHelper = DatabaseOpenHelper.getInstance(getContext());
      setHasOptionsMenu(true);
   }

   @Override
   public void onResume() {
      super.onResume();
      getActivity().setTheme(R.style.PokemonEditorTheme);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.moves_fragment, container, false);
      mUnbinder = ButterKnife.bind(this, view);

      ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
      if (actionBar != null) {
         actionBar.setTitle(R.string.moves);
      }

      return view;
   }

   @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
      super.onCreateOptionsMenu(menu, inflater);
      menu.findItem(R.id.clear_all_teams_action).setVisible(false);
      menu.findItem(R.id.number_action).setVisible(false);
      menu.findItem(R.id.name_action).setVisible(false);
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         default:
            break;
      }
      return super.onOptionsItemSelected(item);
   }

   @Override
   public void onViewCreated(View view, Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

      mTypeSpinner.setOnItemSelectedListener(this);
      mClassSpinner.setOnItemSelectedListener(this);

      mMovesList.setLayoutManager(new LinearLayoutManager(getContext()));
      mMovesList.setHasFixedSize(true);

      new LoadMoveList().execute();
   }

   @Override
   public void onNothingSelected(AdapterView<?> parent) {

   }

   @Override
   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      refreshData();
   }

   private void refreshData() {
      String type = (String) mTypeSpinner.getSelectedItem();
      String className = (String) mClassSpinner.getSelectedItem();

      new RefreshData().execute(type, className);
   }

   @Override
   public void onDestroyView() {
      super.onDestroyView();
      mUnbinder.unbind();
   }

   private class RefreshData extends AsyncTask<String, Void, String[]> {
      @Override
      protected String[] doInBackground(String... params) {
         String type = params[0];
         String className = params[1];

         if (type.equals(TYPES) && !className.equals(CLASSES)) {
            mMoves = mDatabaseHelper.queryMovesByClass(className);
         }
         else if (!type.equals(TYPES) && className.equals(CLASSES)) {
            mMoves = mDatabaseHelper.queryMovesByType(type);
         }
         else if (!type.equals(TYPES) && !className.equals(CLASSES)) {
            mMoves = mDatabaseHelper.queryMovesByTypeAndClass(type, className);
         }
         else {
            mMoves = mDatabaseHelper.queryAllMoves();
         }

         return mMoves;
      }

      @Override
      protected void onPostExecute(String[] result) {
         super.onPostExecute(result);
         mMovesList.setAdapter(new MoveListAdapter(getContext(), mMoves));
      }
   }

   private class LoadMoveList extends AsyncTask<Void, Void, Pair<Pair<String[], String[]>, String[]>> {
      @Override
      protected Pair<Pair<String[], String[]>, String[]> doInBackground(Void... params) {
         String[] types = mDatabaseHelper.queryAllTypes();
         String[] classes = mDatabaseHelper.queryAllClasses();
         String[] moves = mDatabaseHelper.queryAllMoves();

         return new Pair<> (new Pair<> (types, classes), moves);
      }

      @Override
      protected void onPostExecute(Pair<Pair<String[], String[]>, String[]> result) {
         super.onPostExecute(result);
         mTypeSpinner.setAdapter(new TextViewSpinnerAdapter(getContext(), result.first.first));
         mClassSpinner.setAdapter(new TextViewSpinnerAdapter(getContext(), result.first.second));
         mMovesList.setAdapter(new MoveListAdapter(getContext(), result.second));
      }
   }
}
