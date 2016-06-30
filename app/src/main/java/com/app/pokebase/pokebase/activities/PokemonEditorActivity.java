package com.app.pokebase.pokebase.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.pokebase.pokebase.R;
import com.app.pokebase.pokebase.adapters.TextViewSpinnerAdapter;
import com.app.pokebase.pokebase.database.DatabaseOpenHelper;
import com.app.pokebase.pokebase.utilities.ArrayUtils;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

/**
 * @author Brittany Berlanga
 */
public class PokemonEditorActivity extends AppCompatActivity {
   private static final int PROFILE_IMG_ELEVATION = 40;
   private static final int NUM_SPINNERS = 4;
   private static final int MIN_LEVEL = 1;
   private static final int MAX_LEVEL = 100;
   private Toolbar mToolbar;
   private ImageView mProfileImg;
   private TextInputEditText mNickNameView;
   private Spinner mLevelSpinner;
   private Spinner[] mMoveSpinners;
   private int mTeamId;
   private int mMemberId;
   private int mPokemonId;
   private int mLevel;
   private String mNickname;
   private String mMoveOne;
   private String mMoveTwo;
   private String mMoveThree;
   private String mMoveFour;
   private String mTitle;
   private String mDescription;
   private DatabaseOpenHelper mDatabaseHelper;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_pokemon_editor);

      mDatabaseHelper = DatabaseOpenHelper.getInstance(this);

      mToolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(mToolbar);
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null) {
         actionBar.setDisplayHomeAsUpEnabled(true);
      }

      mProfileImg = (ImageView) findViewById(R.id.profile_image);
      mProfileImg.setClipToOutline(true);
      mProfileImg.setElevation(PROFILE_IMG_ELEVATION);

      Intent intent = getIntent();
      mTeamId = intent.getIntExtra("teamId", 0);
      mTitle = intent.getStringExtra("title");
      mDescription = intent.getStringExtra("description");
      mMemberId = intent.getIntExtra("memberId", 0);
      mPokemonId = intent.getIntExtra("pokemonId", 0);
      mLevel = intent.getIntExtra("level", 0);
      mNickname = intent.getStringExtra("nickname");
      mMoveOne = intent.getStringExtra("moveOne");
      mMoveTwo = intent.getStringExtra("moveTwo");
      mMoveThree = intent.getStringExtra("moveThree");
      mMoveFour = intent.getStringExtra("moveFour");
      if (actionBar != null) {
         actionBar.setTitle(mNickname);
      }

      int imageResourceId = getResources().getIdentifier("sprites_" + mPokemonId, "drawable", getPackageName());
      mProfileImg.setImageResource(imageResourceId);
      mNickNameView = (TextInputEditText) findViewById(R.id.nickname_input);
      mNickNameView.setText(mNickname);

      mLevelSpinner = (Spinner) findViewById(R.id.level_spinner);
      mMoveSpinners = new Spinner[NUM_SPINNERS];
      mMoveSpinners[0] = (Spinner) findViewById(R.id.move_one_spinner);
      mMoveSpinners[1] = (Spinner) findViewById(R.id.move_two_spinner);
      mMoveSpinners[2] = (Spinner) findViewById(R.id.move_three_spinner);
      mMoveSpinners[3] = (Spinner) findViewById(R.id.move_four_spinner);

      String[] moves = mDatabaseHelper.querySelectedPokemonMoves(mPokemonId);

      String[] levels = new String[MAX_LEVEL];
      for (int lvl = MIN_LEVEL; lvl <= levels.length; lvl++) {
         levels[lvl - MIN_LEVEL] = String.valueOf(lvl);
      }

      mLevelSpinner.setAdapter(new TextViewSpinnerAdapter(this, levels, 18));
      mLevelSpinner.setSelection(mLevel - 1);

      mMoveSpinners[0].setAdapter(new TextViewSpinnerAdapter(this, moves));
      mMoveSpinners[0].setSelection(ArrayUtils.indexOf(moves, mMoveOne));
      mMoveSpinners[1].setAdapter(new TextViewSpinnerAdapter(this, moves));
      mMoveSpinners[1].setSelection(ArrayUtils.indexOf(moves, mMoveTwo));
      mMoveSpinners[2].setAdapter(new TextViewSpinnerAdapter(this, moves));
      mMoveSpinners[2].setSelection(ArrayUtils.indexOf(moves, mMoveThree));
      mMoveSpinners[3].setAdapter(new TextViewSpinnerAdapter(this, moves));
      mMoveSpinners[3].setSelection(ArrayUtils.indexOf(moves, mMoveFour));
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
            updatePokemon();
            break;
         default:
            break;
      }
      return true;
   }

   private void showDeleteDialog() {
      new LovelyStandardDialog(this)
            .setIcon(R.drawable.ic_info_white_48dp)
            .setTitle(R.string.delete_pokemon)
            .setMessage(R.string.delete_pokemon_prompt).setCancelable(true)
            .setPositiveButton(R.string.yes, new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                  deletePokemon();
               }
            }).setNegativeButton(R.string.no, null).setTopColor(
            ContextCompat.getColor(this, R.color.colorPrimary)).show();
   }

   private void deletePokemon() {
      mDatabaseHelper.deleteTeamPokemonSingle(mMemberId);

      Toast.makeText(this, "Deleted pokemon " + mNickNameView.getText().toString() + ".",
            Toast.LENGTH_LONG).show();
      backToTeamView();
   }

   private void updatePokemon() {
      mDatabaseHelper.updateTeamPokemon(mMemberId, mNickNameView.getText().toString(),
            Integer.parseInt(String.valueOf(mLevelSpinner.getSelectedItem())),
            String.valueOf(mMoveSpinners[0].getSelectedItem()),
            String.valueOf(mMoveSpinners[1].getSelectedItem()),
            String.valueOf(mMoveSpinners[2].getSelectedItem()),
            String.valueOf(mMoveSpinners[3].getSelectedItem()));

      Toast.makeText(this, "Updated " + mNickNameView.getText().toString() + "!",
            Toast.LENGTH_LONG).show();
      backToTeamView();
   }

   private void backToTeamView() {
      Intent teamIntent = new Intent(this, TeamViewActivity.class);
      Bundle extras = new Bundle();
      extras.putInt(TeamViewActivity.TEAM_ID_KEY, mTeamId);
      extras.putBoolean(TeamViewActivity.UPDATE_KEY, true);
      extras.putString("teamName", mTitle);
      extras.putString("description", mDescription);
      teamIntent.putExtras(extras);
      startActivity(teamIntent);
   }

   private void showBackDialog() {
      new LovelyStandardDialog(this)
            .setIcon(R.drawable.ic_info_white_48dp)
            .setTitle(R.string.go_back)
            .setMessage(R.string.back_prompt).setCancelable(true)
            .setPositiveButton(R.string.yes, new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                  backToTeamView();
               }
            }).setNegativeButton(R.string.no, null).setTopColor(
            ContextCompat.getColor(this, R.color.colorPrimary)).show();
   }

   @Override
   public void onBackPressed() {
      showBackDialog();
   }
}
