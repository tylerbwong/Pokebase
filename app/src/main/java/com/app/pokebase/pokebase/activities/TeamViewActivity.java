package com.app.pokebase.pokebase.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.pokebase.pokebase.R;
import com.app.pokebase.pokebase.adapters.PokemonTeamMemberAdapter;
import com.app.pokebase.pokebase.components.PokemonTeamMember;
import com.app.pokebase.pokebase.database.DatabaseOpenHelper;
import com.app.pokebase.pokebase.utilities.AnimatedRecyclerView;
import com.github.fabtransitionactivity.SheetLayout;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

/**
 * @author Tyler Wong
 */
public class TeamViewActivity extends AppCompatActivity implements SheetLayout.OnFabAnimationEndListener {
   public static final String TEAM_ID_KEY = "team_id_key";
   public static final String UPDATE_KEY = "update_key";
   private Toolbar mToolbar;
   private FloatingActionButton mFab;
   private SheetLayout mSheetLayout;
   private AnimatedRecyclerView mPokemonList;
   private LinearLayout mEmptyView;
   private TextInputEditText mNameInput;
   private TextInputEditText mDescriptionInput;

   private PokemonTeamMemberAdapter mPokemonAdapter;
   private PokemonTeamMember[] mPokemon;
   private DatabaseOpenHelper mDatabaseHelper;

   private boolean mUpdateKey;
   private int mTeamId;

   private final static String DEFAULT_NAME = "Team";
   private final static String DEFAULT_DESCRIPTION = "None";
   private final static int REQUEST_CODE = 1;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_team);

      mToolbar = (Toolbar) findViewById(R.id.toolbar);
      mFab = (FloatingActionButton) findViewById(R.id.fab);
      mSheetLayout = (SheetLayout) findViewById(R.id.bottom_sheet);
      mPokemonList = (AnimatedRecyclerView) findViewById(R.id.team_list);
      mEmptyView = (LinearLayout) findViewById(R.id.empty_layout);
      mNameInput = (TextInputEditText) findViewById(R.id.name_input);
      mDescriptionInput = (TextInputEditText) findViewById(R.id.description_input);

      mDatabaseHelper = DatabaseOpenHelper.getInstance(this);
      mPokemonList.setHasFixedSize(true);

      mFab.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            onFabClick();
         }
      });

      mSheetLayout.setFab(mFab);
      mSheetLayout.setFabAnimationEndListener(this);

      Intent intent = getIntent();
      Bundle extras = intent.getExtras();
      mTeamId = extras.getInt(TEAM_ID_KEY);
      mUpdateKey = extras.getBoolean(UPDATE_KEY, false);
      String teamTitle = extras.getString("teamName", DEFAULT_NAME + " "
            + (mDatabaseHelper.queryLastTeamAddedId() + 1));
      String description = extras.getString("description", DEFAULT_DESCRIPTION);

      setSupportActionBar(mToolbar);
      final ActionBar actionBar = getSupportActionBar();

      if (actionBar != null) {
         actionBar.setDisplayHomeAsUpEnabled(true);
         actionBar.setTitle(teamTitle);
      }

      mNameInput.addTextChangedListener(new TextWatcher() {

         @Override
         public void onTextChanged(CharSequence sequence, int start, int before, int count) {
            if (actionBar != null) {
               actionBar.setTitle(sequence.toString());

               if (!mUpdateKey && sequence.toString().trim().length() == 0) {
                  actionBar.setTitle(R.string.new_team);
               }
            }
         }

         @Override
         public void beforeTextChanged(CharSequence sequence, int start, int count, int after) {

         }

         @Override
         public void afterTextChanged(Editable editable) {
         }
      });

      mNameInput.setText(teamTitle);
      mDescriptionInput.setText(description);

      if (mUpdateKey) {
         mPokemon = mDatabaseHelper.queryPokemonTeamMembers(mTeamId);
         mPokemonAdapter = new PokemonTeamMemberAdapter(this, mPokemon, mTeamId,
               mNameInput.getText().toString().trim(),
               mDescriptionInput.getText().toString().trim());
         mPokemonList.setAdapter(mPokemonAdapter);

         if (mPokemon.length == 6) {
            ViewGroup viewGroup = (ViewGroup) mFab.getParent();
            viewGroup.removeView(mFab);
         }
      }
      else {
         if (actionBar != null) {
            actionBar.setTitle(R.string.new_team);
         }
      }

      LinearLayoutManager llm = new LinearLayoutManager(this);
      llm.setOrientation(LinearLayoutManager.VERTICAL);
      mPokemonList.setLayoutManager(llm);

      if (mPokemon == null || mPokemon.length == 0) {
         mPokemonList.setVisibility(View.GONE);
         mEmptyView.setVisibility(View.VISIBLE);
      }
      else {
         mPokemonList.setVisibility(View.VISIBLE);
         mEmptyView.setVisibility(View.GONE);
      }
   }

   public void onFabClick() {
      mSheetLayout.expandFab();
   }

   @Override
   public void onFabAnimationEnd() {
      if (addTeam()) {
         Intent intent = new Intent(this, MainActivity.class);
         Bundle extras = new Bundle();
         if (mTeamId == 0) {
            mTeamId = mDatabaseHelper.queryLastTeamAddedId();
         }
         extras.putInt(TEAM_ID_KEY, mTeamId);
         extras.putBoolean(UPDATE_KEY, true);
         extras.putString("teamName", mNameInput.getText().toString().trim());
         extras.putString("description", mDescriptionInput.getText().toString().trim());
         extras.putBoolean("pokemonAdd", true);
         intent.putExtras(extras);
         startActivityForResult(intent, REQUEST_CODE);
      }
      else {
         mSheetLayout.contractFab();
      }
   }

   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if (requestCode == REQUEST_CODE) {
         mSheetLayout.contractFab();
      }
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.menu_trash, menu);
      inflater.inflate(R.menu.menu_submit, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case android.R.id.home:
            onBackPressed();
            break;
         case R.id.delete_action:
            showDeleteDialog();
            break;
         case R.id.submit_action:
            if (addTeam()) {
               backToMain();
            }
            break;
         default:
            break;
      }
      return true;
   }

   private boolean addTeam() {
      String name = mNameInput.getText().toString().trim();
      String description = mDescriptionInput.getText().toString().trim();
      boolean doesTeamNameExist = mDatabaseHelper.doesTeamNameExist(name, mTeamId, mUpdateKey);
      boolean result = false;

      if (!mUpdateKey && !doesTeamNameExist) {
         if (name.length() == 0) {
            name = DEFAULT_NAME + " " + (mDatabaseHelper.queryLastTeamAddedId() + 1);
         }
         if (description.length() == 0) {
            description = DEFAULT_DESCRIPTION;
         }
         mDatabaseHelper.insertTeam(name, description);
         Toast.makeText(this, "Added new team " + name + "!", Toast.LENGTH_LONG).show();
         result = true;
      }
      else if (mUpdateKey && !doesTeamNameExist){
         mDatabaseHelper.updateTeam(mTeamId, name, description);
         Toast.makeText(this, "Updated " + name + "!", Toast.LENGTH_LONG).show();
         result = true;
      }
      else {
         showUsedNameDialog();
      }

      return result;
   }

   private void showUsedNameDialog() {
      new LovelyStandardDialog(this)
            .setIcon(R.drawable.ic_info_white_24dp)
            .setTitle(R.string.used_name)
            .setMessage(mNameInput.getText().toString().trim() + " " + getString(R.string.used_name_info))
            .setCancelable(true)
            .setNeutralButton(getString(R.string.ok), null).setTopColor(
            ContextCompat.getColor(this, R.color.colorPrimary)).show();
   }

   private void showDeleteDialog() {
      new LovelyStandardDialog(this)
            .setIcon(R.drawable.ic_info_white_24dp)
            .setTitle(R.string.delete_team)
            .setMessage(getResources().getString(R.string.delete_team_prompt)
                  + " " + mNameInput.getText().toString().trim() + "?").setCancelable(true)
            .setPositiveButton(R.string.yes, new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                  deleteTeam();
               }
            }).setNegativeButton(R.string.no, null).setTopColor(
            ContextCompat.getColor(this, R.color.colorPrimary)).show();
   }

   private void deleteTeam() {
      mDatabaseHelper.deleteTeamPokemonAll(mTeamId);
      mDatabaseHelper.deleteTeam(mTeamId);

      Toast.makeText(this, "Deleted " + mNameInput.getText().toString().trim(),
            Toast.LENGTH_LONG).show();
      backToMain();
   }

   private void showBackDialog() {
      new LovelyStandardDialog(this)
            .setIcon(R.drawable.ic_info_white_24dp)
            .setTitle(R.string.go_back)
            .setMessage(R.string.back_prompt).setCancelable(true)
            .setPositiveButton(R.string.yes, new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                  backToMain();
               }
            }).setNegativeButton(R.string.no, null).setTopColor(
            ContextCompat.getColor(this, R.color.colorPrimary)).show();
   }

   private void backToMain() {
      Intent mainIntent = new Intent(this, MainActivity.class);
      startActivity(mainIntent);
   }

   @Override
   public void onBackPressed() {
      showBackDialog();
   }
}
