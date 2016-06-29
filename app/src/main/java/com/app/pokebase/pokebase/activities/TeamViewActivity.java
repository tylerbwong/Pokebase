package com.app.pokebase.pokebase.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.pokebase.pokebase.R;
import com.app.pokebase.pokebase.adapters.PokemonTeamMemberAdapter;
import com.app.pokebase.pokebase.components.PokemonTeamMember;
import com.app.pokebase.pokebase.database.DatabaseOpenHelper;
import com.app.pokebase.pokebase.utilities.AnimatedRecyclerView;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.List;

/**
 * @author Tyler Wong
 */
public class TeamViewActivity extends AppCompatActivity {
   public static final String TEAM_ID_KEY = "team_id_key";
   public static final String UPDATE_KEY = "update_key";
   private Toolbar mToolbar;
   private AnimatedRecyclerView mPokemonList;
   private LinearLayout mEmptyView;
   private TextInputEditText mNameInput;
   private TextInputEditText mDescriptionInput;

   private PokemonTeamMemberAdapter mPokemonAdapter;
   private List<PokemonTeamMember> mPokemon;
   private DatabaseOpenHelper mDatabaseHelper;

   private boolean mUpdateKey;
   private int mTeamId;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_team);

      mToolbar = (Toolbar) findViewById(R.id.toolbar);
      mPokemonList = (AnimatedRecyclerView) findViewById(R.id.team_list);
      mEmptyView = (LinearLayout) findViewById(R.id.empty_layout);
      mNameInput = (TextInputEditText) findViewById(R.id.name_input);
      mDescriptionInput = (TextInputEditText) findViewById(R.id.description_input);

      setSupportActionBar(mToolbar);
      ActionBar actionBar = getSupportActionBar();

      if (actionBar != null) {
         actionBar.setDisplayHomeAsUpEnabled(true);
      }

      getSupportActionBar().setTitle(R.string.new_team);

      mDatabaseHelper = new DatabaseOpenHelper(this);

      Intent intent = getIntent();
      Bundle extras = intent.getExtras();
      mTeamId = extras.getInt(TEAM_ID_KEY);
      mUpdateKey = extras.getBoolean(UPDATE_KEY, false);
      String teamTitle = extras.getString("teamName");
      String description = extras.getString("description");

      mNameInput.setText(teamTitle);
      mDescriptionInput.setText(description);

      if (mUpdateKey) {
         mPokemon = mDatabaseHelper.queryPokemonTeamMembers(mTeamId);
         mPokemonAdapter = new PokemonTeamMemberAdapter(this, mPokemon, mTeamId,
               mNameInput.getText().toString(), mDescriptionInput.getText().toString());
         mPokemonList.setAdapter(mPokemonAdapter);
      }

      LinearLayoutManager llm = new LinearLayoutManager(this);
      llm.setOrientation(LinearLayoutManager.VERTICAL);
      mPokemonList.setLayoutManager(llm);

      if (mPokemon == null || mPokemon.isEmpty()) {
         mPokemonList.setVisibility(View.GONE);
         mEmptyView.setVisibility(View.VISIBLE);
      }
      else {
         mPokemonList.setVisibility(View.VISIBLE);
         mEmptyView.setVisibility(View.GONE);
      }
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.menu_submit, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case android.R.id.home:
            onBackPressed();
            break;
         case R.id.submit_action:
            addTeam();
            break;
         default:
            break;
      }
      return true;
   }

   private void addTeam() {
      if (!mUpdateKey) {
         mDatabaseHelper.insertTeam(mNameInput.getText().toString(),
               mDescriptionInput.getText().toString());
         Toast.makeText(this, "Added new team " + mNameInput.getText().toString() + "!",
               Toast.LENGTH_LONG).show();
      }
      else {
         mDatabaseHelper.updateTeam(mTeamId, mNameInput.getText().toString(),
               mDescriptionInput.getText().toString());
         Toast.makeText(this, "Updated team!", Toast.LENGTH_LONG).show();
      }
      backToMain();
   }

   private void showBackDialog() {
      new LovelyStandardDialog(this)
            .setIcon(R.drawable.ic_info_white_48dp)
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
