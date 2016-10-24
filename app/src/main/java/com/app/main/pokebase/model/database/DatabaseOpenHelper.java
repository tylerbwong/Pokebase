/*
 * Copyright 2016 Tyler Wong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.app.main.pokebase.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.app.main.pokebase.model.components.Move;
import com.app.main.pokebase.model.components.PokemonListItem;
import com.app.main.pokebase.model.components.PokemonProfile;
import com.app.main.pokebase.model.components.PokemonTeamItem;
import com.app.main.pokebase.model.components.PokemonTeamMember;
import com.app.main.pokebase.model.components.Team;
import com.app.main.pokebase.model.components.Item;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Tyler Wong
 */
public final class DatabaseOpenHelper extends SQLiteAssetHelper {
   private static DatabaseOpenHelper mDatabaseOpenHelper;
   private SQLiteDatabase mDatabase;

   private final static String DB_NAME = "Pokebase.db";
   private final static int DB_VERSION = 1;
   private final static int MAX_NUM_MOVES = 4;
   private final static int MAX_TEAM_SIZE = 6;
   private final static String NONE = "None";
   private final static String TEAM_POKEMON_TABLE = "TeamPokemon";
   private final static String TEAMS_TABLE = "Teams";
   private final static String DESCRIPTION_COL = "description";
   private final static String TEAM_ID_COL = "teamId";
   private final static String POKEMON_ID_COL = "pokemonId";
   private final static String NICKNAME_COL = "nickname";
   private final static String LEVEL_COL = "level";
   private final static String MOVE_ONE_COL = "moveOne";
   private final static String MOVE_TWO_COL = "moveTwo";
   private final static String MOVE_THREE_COL = "moveThree";
   private final static String MOVE_FOUR_COL = "moveFour";
   private final static String ROW_ID_COL = "_id";
   private final static String ID_COL = "id";
   private final static String IDENT_COL = "identifier";
   private final static String COST_COL = "cost";
   private final static String NAME_COL = "name";
   private final static String HEIGHT_COL = "height";
   private final static String WEIGHT_COL = "weight";
   private final static String BASE_EXP_COL = "baseExp";
   private final static String LAST_UPDATED_COL = "lastUpdated";
   private final static String REGION_COL = "region";
   private final static String BASE_STAT_COL = "baseStat";
   private final static String MOVE_ID_COL = "moveId";
   private final static String TYPE_ID_COL = "typeId";
   private final static String POWER_COL = "power";
   private final static String PP_COL = "pp";
   private final static String ACCURACY_COL = "accuracy";
   private final static String TYPES = "Types";
   private final static String REGIONS = "Regions";
   private final static String CLASSES = "Classes";
   private final static String DATE_FORMAT = "M/d/yyyy h:mm a";
   private final static String AND = "AND";
   private final static String WHERE = " WHERE";
   private final static String PERCENT_START = "\"%";
   private final static String PERCENT_END = "%\"";

   private final static String ALPHABETIZE =
         " ORDER BY P.name";
   private final static String NAME_SEARCH =
         " P.name LIKE ";
   private final static String ALL =
         "SELECT P.id, P.name FROM Pokemon AS P";
   private final static String ALL_TYPES =
         "SELECT T.name FROM Types AS T ORDER BY T.name";
   private final static String ALL_REGIONS =
         "SELECT R.name FROM Regions AS R";
   private final static String TYPE_QUERY =
         "SELECT P.id, P.name " +
               "FROM Pokemon AS P " +
               "JOIN PokemonTypes AS T ON P.id = T.pokemonId " +
               "JOIN Types AS Y ON Y.id = T.typeId " +
               "WHERE Y.name = ?";
   private final static String REGION_QUERY =
         "SELECT P.id, P.name " +
               "FROM Pokemon AS P " +
               "JOIN Regions AS R ON R.id = P.region " +
               "WHERE R.name = ?";
   private final static String TYPE_REGION_QUERY =
         "SELECT P.id, P.name " +
               "FROM Pokemon AS P " +
               "JOIN PokemonTypes AS T ON P.id = T.pokemonId " +
               "JOIN Types AS Y ON Y.id = T.typeId " +
               "JOIN Regions AS R ON R.id = P.region " +
               "WHERE Y.name = ? " +
               "AND R.name = ?";
   private final static String POKEMON_PROFILE_QUERY =
         "SELECT P.id, P.name, P.height, P.weight, P.baseExp, R.name AS region " +
               "FROM Pokemon AS P " +
               "JOIN Regions AS R ON P.region = R.id " +
               "WHERE P.id = ?";
   private final static String POKEMON_TYPES_QUERY =
         "SELECT T.name " +
               "FROM Pokemon AS P " +
               "JOIN PokemonTypes AS Y ON Y.pokemonId = P.id " +
               "JOIN Types AS T ON Y.typeId = T.id " +
               "WHERE P.id = ?";
   private final static String POKEMON_MOVES_QUERY =
         "SELECT DISTINCT M.name " +
               "FROM Pokemon AS P " +
               "JOIN PokemonMoves AS O ON P.id = O.pokemonId " +
               "JOIN Moves AS M ON O.moveId = M.id " +
               "WHERE P.id = ? " +
               "ORDER BY M.name";
   private final static String POKEMON_EVOLUTIONS_QUERY =
         "SELECT K.id, K.name " +
               "FROM Pokemon AS P " +
               "JOIN PokemonEvolutions AS E ON E.evolvesFrom = P.id " +
               "JOIN Pokemon AS K ON E.id = K.id " +
               "WHERE P.id = ?";
   private final static String ALL_TEAM_INFO =
         "SELECT T._id, T.name, T.description, T.lastUpdated " +
               "FROM Teams AS T " +
               "ORDER BY T._id";
   private final static String ALL_TEAM_POKEMON =
         "SELECT T.pokemonId " +
               "FROM TeamPokemon AS T " +
               "WHERE T.teamId = ?";
   private final static String POKEMON_BY_TEAM =
         "SELECT P._id, P.pokemonId, P.nickname, P.level, P.moveOne, P.moveTwo, P.moveThree, " +
               "P.moveFour, P.lastUpdated " +
               "FROM TeamPokemon AS P " +
               "WHERE P.teamId = ?";
   private final static String TEAM_NAMES =
         "SELECT T._id, T.name FROM Teams AS T";
   private final static String TEAM_SIZE =
         "SELECT COUNT(*) " +
               "FROM Teams AS T JOIN TeamPokemon AS P ON T._id = P.teamId " +
               "WHERE T._id = ?";
   private final static String QUERY_MOVE_ID =
         "SELECT M.id FROM Moves AS M WHERE M.name = ?";
   private final static String SINGLE_MOVE =
         "SELECT M.name FROM Moves AS M WHERE M.id = ?";
   private final static String LAST_TEAM_ADDED =
         "SELECT MAX(T._id) FROM Teams AS T";
   private final static String DOES_TEAM_EXIST =
         "SELECT COUNT(*) FROM Teams AS T WHERE T.name = ?";
   private final static String TEAM_NAME_BY_ID =
         "SELECT T.name FROM Teams AS T WHERE T._id = ?";
   private final static String SELECTED_POKEMON_STATS =
         "SELECT * " +
               "FROM PokemonStats AS P JOIN Stats AS S ON P.statId = S.id " +
               "WHERE P.pokemonId = ?";
   private final static String ALL_MOVES =
         "SELECT * FROM Moves AS M ORDER BY M.name";
   private final static String POKEMON_DESCRIPTION =
         "SELECT P.description FROM PokemonDescriptions AS P WHERE P.pokemonId = ?";
   private final static String MOVE_INFO_BY_NAME =
         "SELECT * FROM MovesInfo AS I JOIN Moves M ON I.moveId = M.id JOIN DamageClasses " +
               "C ON I.classId = C.id WHERE M.name = ?";
   private final static String TYPE_BY_ID =
         "SELECT T.name FROM Types AS T WHERE T.id = ?";
   private final static String MOVE_DESCRIPTION_BY_ID =
         "SELECT M.description FROM MoveDescriptions AS M WHERE M.moveId = ?";
   private final static String ALL_CLASSES =
         "SELECT C.name FROM DamageClasses AS C ORDER BY C.name";
   private final static String MOVES_BY_TYPE =
         "SELECT M.name FROM Moves AS M JOIN MovesInfo I ON M.id = I.moveId JOIN Types AS T ON " +
               "T.id = I.typeId WHERE T.name = ? ORDER BY M.name";
   private final static String MOVES_BY_CLASS =
         "SELECT M.name FROM Moves AS M JOIN MovesInfo I ON M.id = I.moveId JOIN DamageClasses C ON " +
               "C.id = I.classId WHERE C.name = ? ORDER BY M.name";
   private final static String MOVES_BY_TYPE_AND_CLASS =
         "SELECT M.name FROM Moves AS M JOIN MovesInfo I ON M.id = I.moveId JOIN DamageClasses C ON " +
               "C.id = I.classId JOIN Types T ON T.id = I.typeId WHERE T.name = ? AND C.name = ? " +
               "ORDER BY M.name";
   private final static String ALL_ITEMS =
         "SELECT * FROM Items AS I ORDER BY I.name";
   private final static String ITEM_DESCRIPTION_BY_ID =
         "SELECT I.description FROM ItemDescriptions I WHERE I.itemId = ?";
   private final static String DELETE_LAST_POKEMON =
         "DELETE FROM TeamPokemon WHERE _id = (SELECT MAX(_id) FROM TeamPokemon)";

   private DatabaseOpenHelper(Context context) {
      super(context, DB_NAME, null, DB_VERSION);
      mDatabase = getWritableDatabase();
   }

   public synchronized static DatabaseOpenHelper getInstance(Context context) {
      if (mDatabaseOpenHelper == null) {
         mDatabaseOpenHelper = new DatabaseOpenHelper(context);
      }
      return mDatabaseOpenHelper;
   }

   @Override
   public synchronized void close() {
      super.close();
   }

   @Override
   public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

   }

   public String[] queryAllTypes() {
      Cursor cursor = mDatabase.rawQuery(ALL_TYPES, null);
      String[] types = new String[cursor.getCount() + 1];
      types[0] = TYPES;
      int index = 1;
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         types[index] = cursor.getString(cursor.getColumnIndex(NAME_COL));
         index++;
         cursor.moveToNext();
      }
      cursor.close();
      return types;
   }

   public String[] queryAllRegions() {
      Cursor cursor = mDatabase.rawQuery(ALL_REGIONS, null);
      String[] regions = new String[cursor.getCount() + 1];
      regions[0] = REGIONS;
      int index = 1;
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         regions[index] = cursor.getString(cursor.getColumnIndex(NAME_COL));
         index++;
         cursor.moveToNext();
      }
      cursor.close();
      return regions;
   }

   public String[] queryAllClasses() {
      Cursor cursor = mDatabase.rawQuery(ALL_CLASSES, null);
      String[] classes = new String[cursor.getCount() + 1];
      classes[0] = CLASSES;
      int index = 1;
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         classes[index] = cursor.getString(cursor.getColumnIndex(NAME_COL));
         index++;
         cursor.moveToNext();
      }
      cursor.close();
      return classes;
   }

   public PokemonListItem[] queryByType(String search, String type, boolean alphabetical) {
      String query = TYPE_QUERY;

      if (search.equals("") && alphabetical) {
         query += ALPHABETIZE;
      }
      else if (!search.equals("") && alphabetical) {
         query += ((AND + NAME_SEARCH + PERCENT_START + search + PERCENT_END) + ALPHABETIZE);
      }
      else if (!search.equals("") && !alphabetical) {
         query += (AND + NAME_SEARCH + PERCENT_START + search + PERCENT_END);
      }

      Cursor cursor = mDatabase.rawQuery(query, new String[]{type});
      PokemonListItem[] typePokemon = new PokemonListItem[cursor.getCount()];
      int index = 0;
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         typePokemon[index] = new PokemonListItem(cursor.getInt(cursor.getColumnIndex(ID_COL)),
               cursor.getString(cursor.getColumnIndex(NAME_COL)));
         index++;
         cursor.moveToNext();
      }
      cursor.close();
      return typePokemon;
   }

   public PokemonListItem[] queryByRegion(String search, String region, boolean alphabetical) {
      String query = REGION_QUERY;

      if (search.equals("") && alphabetical) {
         query += ALPHABETIZE;
      }
      else if (!search.equals("") && alphabetical) {
         query += ((AND + NAME_SEARCH + PERCENT_START + search + PERCENT_END) + ALPHABETIZE);
      }
      else if (!search.equals("") && !alphabetical) {
         query += (AND + NAME_SEARCH + PERCENT_START + search + PERCENT_END);
      }

      Cursor cursor = mDatabase.rawQuery(query, new String[]{region});
      PokemonListItem[] regionPokemon = new PokemonListItem[cursor.getCount()];
      int index = 0;
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         regionPokemon[index] = new PokemonListItem(cursor.getInt(cursor.getColumnIndex(ID_COL)),
               cursor.getString(cursor.getColumnIndex(NAME_COL)));
         ;
         index++;
         cursor.moveToNext();
      }
      cursor.close();
      return regionPokemon;
   }

   public PokemonListItem[] queryByTypeAndRegion(String search, String type, String region,
                                                 boolean alphabetical) {
      String query = TYPE_REGION_QUERY;

      if (search.equals("") && alphabetical) {
         query += ALPHABETIZE;
      }
      else if (!search.equals("") && alphabetical) {
         query += ((AND + NAME_SEARCH + PERCENT_START + search + PERCENT_END) + ALPHABETIZE);
      }
      else if (!search.equals("") && !alphabetical) {
         query += (AND + NAME_SEARCH + PERCENT_START + search + PERCENT_END);
      }

      Cursor cursor = mDatabase.rawQuery(query, new String[]{type, region});
      PokemonListItem[] typeRegionPokemon = new PokemonListItem[cursor.getCount()];
      int index = 0;
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         typeRegionPokemon[index] = new PokemonListItem(cursor.getInt(cursor.getColumnIndex(ID_COL)),
               cursor.getString(cursor.getColumnIndex(NAME_COL)));
         index++;
         cursor.moveToNext();
      }
      cursor.close();
      return typeRegionPokemon;
   }

   public PokemonListItem[] queryAll(String search, boolean alphabetical) {
      String query = ALL;

      if (search.equals("") && alphabetical) {
         query += ALPHABETIZE;
      }
      else if (!search.equals("") && alphabetical) {
         query += ((WHERE + NAME_SEARCH + PERCENT_START + search + PERCENT_END) + ALPHABETIZE);
      }
      else if (!search.equals("") && !alphabetical) {
         query += (WHERE + NAME_SEARCH + PERCENT_START + search + PERCENT_END);
      }

      Cursor cursor = mDatabase.rawQuery(query, null);
      PokemonListItem[] pokemon = new PokemonListItem[cursor.getCount()];
      int index = 0;
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         pokemon[index] = new PokemonListItem(cursor.getInt(cursor.getColumnIndex(ID_COL)),
               cursor.getString(cursor.getColumnIndex(NAME_COL)));
         index++;
         cursor.moveToNext();
      }
      cursor.close();
      return pokemon;
   }

   private String[] queryPokemonTypes(int id) {
      Cursor cursor = mDatabase.rawQuery(POKEMON_TYPES_QUERY, new String[]{String.valueOf(id)});
      String[] pokemonTypes = new String[cursor.getCount()];
      int index = 0;
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         pokemonTypes[index] = cursor.getString(cursor.getColumnIndex(NAME_COL));
         index++;
         cursor.moveToNext();
      }
      cursor.close();
      return pokemonTypes;
   }

   public String[] queryPokemonMoves(int id) {
      Cursor cursor = mDatabase.rawQuery(POKEMON_MOVES_QUERY, new String[]{String.valueOf(id)});
      String[] pokemonMoves = new String[cursor.getCount()];
      int index = 0;
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         pokemonMoves[index] = cursor.getString(cursor.getColumnIndex(NAME_COL));
         index++;
         cursor.moveToNext();
      }
      cursor.close();
      return pokemonMoves;
   }

   public PokemonProfile queryPokemonProfile(int id) {
      PokemonProfile pokemon;
      Cursor cursor = mDatabase.rawQuery(POKEMON_PROFILE_QUERY, new String[]{String.valueOf(id)});
      cursor.moveToFirst();

      pokemon = new PokemonProfile(cursor.getInt(cursor.getColumnIndex(ID_COL)),
            cursor.getString(cursor.getColumnIndex(NAME_COL)),
            cursor.getInt(cursor.getColumnIndex(HEIGHT_COL)),
            cursor.getInt(cursor.getColumnIndex(WEIGHT_COL)),
            cursor.getInt(cursor.getColumnIndex(BASE_EXP_COL)),
            cursor.getString(cursor.getColumnIndex(REGION_COL)),
            queryPokemonTypes(id), queryPokemonMoves(id), queryPokemonEvolutions(id),
            queryPokemonDescription(id), queryPokemonStats(id));

      cursor.close();
      return pokemon;
   }

   private PokemonListItem[] queryPokemonEvolutions(int id) {
      Cursor cursor = mDatabase.rawQuery(POKEMON_EVOLUTIONS_QUERY, new String[]{String.valueOf(id)});
      PokemonListItem[] pokemonEvolutions = new PokemonListItem[cursor.getCount()];
      int index = 0;
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         pokemonEvolutions[index] = new PokemonListItem(cursor.getInt(cursor.getColumnIndex(ID_COL)),
               cursor.getString(cursor.getColumnIndex(NAME_COL)));
         index++;
         cursor.moveToNext();
      }
      cursor.close();
      return pokemonEvolutions;
   }

   public boolean insertTeam(String name, String description) {
      ContentValues contentValues = new ContentValues();
      contentValues.put(NAME_COL, name);
      contentValues.put(DESCRIPTION_COL, description);
      contentValues.put(LAST_UPDATED_COL, getDate());
      mDatabase.insert(TEAMS_TABLE, null, contentValues);
      return true;
   }

   public boolean insertTeamPokemon(int teamId, int pokemonId, String nickname, int level,
                                    int moveOne, int moveTwo, int moveThree, int moveFour) {
      ContentValues contentValues = new ContentValues();
      contentValues.put(TEAM_ID_COL, teamId);
      contentValues.put(POKEMON_ID_COL, pokemonId);
      contentValues.put(NICKNAME_COL, nickname);
      contentValues.put(LEVEL_COL, level);
      contentValues.put(MOVE_ONE_COL, moveOne);
      contentValues.put(MOVE_TWO_COL, moveTwo);
      contentValues.put(MOVE_THREE_COL, moveThree);
      contentValues.put(MOVE_FOUR_COL, moveFour);
      contentValues.put(LAST_UPDATED_COL, getDate());
      mDatabase.insert(TEAM_POKEMON_TABLE, null, contentValues);
      updateTeamLastUpdated(teamId);
      return true;
   }

   public boolean updateTeam(int teamId, String name, String description) {
      ContentValues contentValues = new ContentValues();
      String idFilter = ROW_ID_COL + "=" + teamId;
      contentValues.put(NAME_COL, name);
      contentValues.put(DESCRIPTION_COL, description);
      contentValues.put(LAST_UPDATED_COL, getDate());
      mDatabase.update(TEAMS_TABLE, contentValues, idFilter, null);
      return true;
   }

   private int queryTeamCount(int teamId) {
      int result;
      Cursor cursor = mDatabase.rawQuery(TEAM_SIZE, new String[]{String.valueOf(teamId)});
      cursor.moveToFirst();
      result = cursor.getInt(0);

      cursor.close();
      return result;
   }

   public int queryLastTeamAddedId() {
      int result;
      Cursor cursor = mDatabase.rawQuery(LAST_TEAM_ADDED, null);
      cursor.moveToFirst();
      result = cursor.getInt(0);

      cursor.close();
      return result;
   }

   private boolean updateTeamLastUpdated(int teamId) {
      String teamFilter = ROW_ID_COL + "=" + teamId;
      ContentValues contentValues = new ContentValues();
      contentValues.put(LAST_UPDATED_COL, getDate());
      mDatabase.update(TEAMS_TABLE, contentValues, teamFilter, null);
      return true;
   }

   public boolean doesTeamNameExist(String name, int teamId, boolean updateKey) {
      boolean result = true;
      int count;
      Cursor cursor = mDatabase.rawQuery(DOES_TEAM_EXIST, new String[]{name});
      cursor.moveToFirst();
      count = cursor.getInt(0);

      if (updateKey && count == 1) {
         String previousName = queryTeamName(teamId);

         if (name.equals(previousName)) {
            result = false;
         }
      }

      if (count == 0) {
         result = false;
      }

      cursor.close();
      return result;
   }

   private String queryTeamName(int teamId) {
      String result;
      Cursor cursor = mDatabase.rawQuery(TEAM_NAME_BY_ID, new String[]{String.valueOf(teamId)});
      cursor.moveToFirst();
      result = cursor.getString(0);

      cursor.close();
      return result;
   }

   public List<Pair<Integer, String>> queryTeamIdsAndNames() {
      List<Pair<Integer, String>> teamNames = new ArrayList<>();
      Cursor cursor = mDatabase.rawQuery(TEAM_NAMES, null);
      int teamId;
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         teamId = cursor.getInt(cursor.getColumnIndex(ROW_ID_COL));

         if (queryTeamCount(teamId) < MAX_TEAM_SIZE) {
            teamNames.add(new Pair<>(teamId, cursor.getString(cursor.getColumnIndex(NAME_COL))));
         }
         cursor.moveToNext();
      }
      cursor.close();
      return teamNames;
   }

   private PokemonTeamItem[] queryTeamPokemonIds(int teamId) {
      Cursor cursor = mDatabase.rawQuery(ALL_TEAM_POKEMON, new String[]{String.valueOf(teamId)});
      PokemonTeamItem[] teamPokemon = new PokemonTeamItem[cursor.getCount()];
      int index = 0;
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         teamPokemon[index] = new PokemonTeamItem(cursor.getInt(cursor.getColumnIndex(POKEMON_ID_COL)));
         index++;
         cursor.moveToNext();
      }
      cursor.close();
      return teamPokemon;
   }

   public Team[] queryAllTeams() {
      Cursor cursor = mDatabase.rawQuery(ALL_TEAM_INFO, null);
      Team[] teams = new Team[cursor.getCount()];
      int teamId, index = 0;
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         teamId = cursor.getInt(cursor.getColumnIndex(ROW_ID_COL));
         teams[index] = new Team(teamId, cursor.getString(cursor.getColumnIndex(NAME_COL)),
               cursor.getString(cursor.getColumnIndex(DESCRIPTION_COL)),
               cursor.getString(cursor.getColumnIndex(LAST_UPDATED_COL)),
               queryTeamPokemonIds(teamId));
         index++;
         cursor.moveToNext();
      }
      cursor.close();
      return teams;
   }

   private String queryPokemonMove(int moveId) {
      String result = "";
      Cursor cursor = mDatabase.rawQuery(SINGLE_MOVE, new String[]{String.valueOf(moveId)});
      cursor.moveToFirst();
      while (!cursor.isAfterLast()) {
         result = cursor.getString(cursor.getColumnIndex(NAME_COL));
         cursor.moveToNext();
      }

      if (result.equals("")) {
         result = NONE;
      }
      cursor.close();
      return result;
   }

   private String[] queryPokemonMoves(int moveOne, int moveTwo, int moveThree, int moveFour) {
      String[] moves = new String[MAX_NUM_MOVES];
      String moveOneName = queryPokemonMove(moveOne);
      String moveTwoName = queryPokemonMove(moveTwo);
      String moveThreeName = queryPokemonMove(moveThree);
      String moveFourName = queryPokemonMove(moveFour);

      moves[0] = moveOneName;
      moves[1] = moveTwoName;
      moves[2] = moveThreeName;
      moves[3] = moveFourName;

      return moves;
   }

   public PokemonTeamMember[] queryPokemonTeamMembers(int teamId) {
      Cursor cursor = mDatabase.rawQuery(POKEMON_BY_TEAM, new String[]{String.valueOf(teamId)});
      PokemonTeamMember[] pokemon = new PokemonTeamMember[cursor.getCount()];
      int index = 0;
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         pokemon[index] = new PokemonTeamMember(cursor.getInt(cursor.getColumnIndex(ROW_ID_COL)),
               cursor.getInt(cursor.getColumnIndex(POKEMON_ID_COL)),
               cursor.getString(cursor.getColumnIndex(NICKNAME_COL)),
               cursor.getInt(cursor.getColumnIndex(LEVEL_COL)),
               queryPokemonMoves(cursor.getInt(cursor.getColumnIndex(MOVE_ONE_COL)),
                     cursor.getInt(cursor.getColumnIndex(MOVE_TWO_COL)),
                     cursor.getInt(cursor.getColumnIndex(MOVE_THREE_COL)),
                     cursor.getInt(cursor.getColumnIndex(MOVE_FOUR_COL))),
               cursor.getString(cursor.getColumnIndex(LAST_UPDATED_COL)));
         index++;
         cursor.moveToNext();
      }
      cursor.close();
      return pokemon;
   }

   public String[] queryMovesByType(String type) {
      Cursor cursor = mDatabase.rawQuery(MOVES_BY_TYPE, new String[]{type});
      String[] moves = new String[cursor.getCount()];
      int index = 0;
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         moves[index] = cursor.getString(cursor.getColumnIndex(NAME_COL));
         index++;
         cursor.moveToNext();
      }
      cursor.close();
      return moves;
   }

   public String[] queryMovesByClass(String className) {
      Cursor cursor = mDatabase.rawQuery(MOVES_BY_CLASS, new String[]{className});
      String[] moves = new String[cursor.getCount()];
      int index = 0;
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         moves[index] = cursor.getString(cursor.getColumnIndex(NAME_COL));
         index++;
         cursor.moveToNext();
      }
      cursor.close();
      return moves;
   }

   public String[] queryMovesByTypeAndClass(String type, String className) {
      Cursor cursor = mDatabase.rawQuery(MOVES_BY_TYPE_AND_CLASS, new String[]{type, className});
      String[] moves = new String[cursor.getCount()];
      int index = 0;
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         moves[index] = cursor.getString(cursor.getColumnIndex(NAME_COL));
         index++;
         cursor.moveToNext();
      }
      cursor.close();
      return moves;
   }

   private int queryMoveIdByName(String moveName) {
      int moveId;
      Cursor cursor = mDatabase.rawQuery(QUERY_MOVE_ID, new String[]{moveName});
      cursor.moveToFirst();
      moveId = cursor.getInt(0);
      cursor.close();
      return moveId;
   }

   public Move queryMoveInfoByName(String name) {
      Move move;
      Cursor cursor = mDatabase.rawQuery(MOVE_INFO_BY_NAME, new String[]{name});
      cursor.moveToFirst();

      move = new Move(cursor.getInt(cursor.getColumnIndex(MOVE_ID_COL)), name,
            cursor.getInt(cursor.getColumnIndex(TYPE_ID_COL)),
            cursor.getInt(cursor.getColumnIndex(POWER_COL)),
            cursor.getInt(cursor.getColumnIndex(PP_COL)),
            cursor.getInt(cursor.getColumnIndex(ACCURACY_COL)),
            cursor.getString(cursor.getColumnIndex(NAME_COL)));

      cursor.close();
      return move;
   }

   public String queryMoveDescriptionById(int moveId) {
      String result;
      Cursor cursor = mDatabase.rawQuery(MOVE_DESCRIPTION_BY_ID, new String[]{String.valueOf(moveId)});
      cursor.moveToFirst();
      result = cursor.getString(0);
      cursor.close();
      return result;
   }

   public boolean updateTeamPokemon(int memberId, int teamId, String nickname, int level, String moveOne,
                                    String moveTwo, String moveThree, String moveFour) {
      int moveOneId = queryMoveIdByName(moveOne);
      int moveTwoId = queryMoveIdByName(moveTwo);
      int moveThreeId = queryMoveIdByName(moveThree);
      int moveFourId = queryMoveIdByName(moveFour);
      String idFilter = ROW_ID_COL + "=" + memberId;
      ContentValues contentValues = new ContentValues();

      contentValues.put(NICKNAME_COL, nickname);
      contentValues.put(LEVEL_COL, level);
      contentValues.put(MOVE_ONE_COL, moveOneId);
      contentValues.put(MOVE_TWO_COL, moveTwoId);
      contentValues.put(MOVE_THREE_COL, moveThreeId);
      contentValues.put(MOVE_FOUR_COL, moveFourId);
      contentValues.put(LAST_UPDATED_COL, getDate());
      mDatabase.update(TEAM_POKEMON_TABLE, contentValues, idFilter, null);
      updateTeamLastUpdated(teamId);
      return true;
   }

   public boolean deleteTeam(int teamId) {
      String teamFilter = ROW_ID_COL + "=" + teamId;
      mDatabase.delete(TEAMS_TABLE, teamFilter, null);
      return true;
   }

   public boolean deleteTeamPokemonAll(int teamId) {
      String teamFilter = TEAM_ID_COL + "=" + teamId;
      mDatabase.delete(TEAM_POKEMON_TABLE, teamFilter, null);
      return true;
   }

   public boolean deleteTeamPokemonSingle(int memberId) {
      String memberFilter = ROW_ID_COL + "=" + memberId;
      mDatabase.delete(TEAM_POKEMON_TABLE, memberFilter, null);
      return true;
   }

   public boolean deleteAllTeams() {
      mDatabase.delete(TEAM_POKEMON_TABLE, null, null);
      mDatabase.delete(TEAMS_TABLE, null, null);
      return true;
   }

   public boolean deleteLastAddedPokemon() {
      mDatabase.execSQL(DELETE_LAST_POKEMON);
      return true;
   }

   private float[] queryPokemonStats(int pokemonId) {
      Cursor cursor = mDatabase.rawQuery(SELECTED_POKEMON_STATS,
            new String[]{String.valueOf(pokemonId)});
      int index = 0;
      float[] stats = new float[cursor.getCount()];
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         stats[index] = (float) cursor.getInt(cursor.getColumnIndex(BASE_STAT_COL));
         index++;
         cursor.moveToNext();
      }
      cursor.close();
      return stats;
   }

   public String[] queryAllMoves() {
      Cursor cursor = mDatabase.rawQuery(ALL_MOVES, null);
      String[] moves = new String[cursor.getCount()];
      int index = 0;
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         moves[index] = cursor.getString(cursor.getColumnIndex(NAME_COL));
         index++;
         cursor.moveToNext();
      }
      cursor.close();
      return moves;
   }

   private String queryPokemonDescription(int pokemonId) {
      String result;
      Cursor cursor = mDatabase.rawQuery(POKEMON_DESCRIPTION, new String[]{String.valueOf(pokemonId)});
      cursor.moveToFirst();
      result = cursor.getString(0);
      cursor.close();
      return result;
   }

   public String queryTypeById(int typeId) {
      String result;
      Cursor cursor = mDatabase.rawQuery(TYPE_BY_ID, new String[]{String.valueOf(typeId)});
      cursor.moveToFirst();
      result = cursor.getString(0);
      cursor.close();
      return result;
   }

   public Item[] queryAllItems() {
      Cursor cursor = mDatabase.rawQuery(ALL_ITEMS, null);
      Item[] items = new Item[cursor.getCount()];
      int index = 0;
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         items[index] = new Item(cursor.getInt(cursor.getColumnIndex(ID_COL)),
               cursor.getString(cursor.getColumnIndex(IDENT_COL)),
               cursor.getString(cursor.getColumnIndex(NAME_COL)),
               cursor.getInt(cursor.getColumnIndex(COST_COL)));
         index++;
         cursor.moveToNext();
      }
      cursor.close();
      return items;
   }

   public String queryItemDescription(int itemId) {
      String result;
      Cursor cursor = mDatabase.rawQuery(ITEM_DESCRIPTION_BY_ID, new String[]{String.valueOf(itemId)});
      cursor.moveToFirst();
      result = cursor.getString(0);
      cursor.close();
      return result;
   }

   private String getDate() {
      DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
      Date date = new Date();
      return dateFormat.format(date);
   }
}
