package com.app.main.pokebase.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.app.main.pokebase.adapters.MoveListAdapter;
import com.app.main.pokebase.adapters.TextViewSpinnerAdapter;
import com.app.main.pokebase.database.DatabaseOpenHelper;
import com.app.main.pokebase.views.AnimatedRecyclerView;

/**
 * @author Tyler Wong
 */
public class MovesFragment extends Fragment implements AdapterView.OnItemSelectedListener {
   private Spinner mTypeSpinner;
   private Spinner mClassSpinner;
   private AnimatedRecyclerView mMovesList;

   private DatabaseOpenHelper mDatabaseHelper;
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
      switch(item.getItemId()) {
         default:
            break;
      }
      return super.onOptionsItemSelected(item);
   }

   @Override
   public void onViewCreated(View view, Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

      mTypeSpinner = (Spinner) view.findViewById(R.id.type_spinner);
      mTypeSpinner.setOnItemSelectedListener(this);
      mClassSpinner = (Spinner) view.findViewById(R.id.class_spinner);
      mClassSpinner.setOnItemSelectedListener(this);

      mMovesList = (AnimatedRecyclerView) view.findViewById(R.id.moves_list);
      mMovesList.setLayoutManager(new LinearLayoutManager(getContext()));
      mMovesList.setHasFixedSize(true);

      mTypeSpinner.setAdapter(new TextViewSpinnerAdapter(getContext(),
            mDatabaseHelper.queryAllTypes()));
      mClassSpinner.setAdapter(new TextViewSpinnerAdapter(getContext(),
            mDatabaseHelper.queryAllClasses()));
      mMovesList.setAdapter(new MoveListAdapter(getContext(),
            mDatabaseHelper.queryAllMoves()));
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

      mMovesList.setAdapter(new MoveListAdapter(getContext(), mMoves));
   }
}
