package com.app.pokebase.pokebase.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.app.pokebase.pokebase.components.PokemonListItem;
import com.app.pokebase.pokebase.components.PokemonProfile;
import com.app.pokebase.pokebase.components.PokemonTeamItem;
import com.app.pokebase.pokebase.components.PokemonTeamMember;
import com.app.pokebase.pokebase.components.Team;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

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
   private final static String NAME_COL = "name";
   private final static String HEIGHT_COL = "height";
   private final static String WEIGHT_COL = "weight";
   private final static String BASE_EXP_COL = "baseExp";
   private final static String REGION_COL = "region";
   private final static String TYPES = "Types";
   private final static String REGIONS = "Regions";

   private final static String ALL =
         "SELECT P.id, P.name " +
               "FROM Pokemon AS P";
   private final static String ALL_TYPES =
         "SELECT T.name FROM Types AS T";
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
   private final static String SELECTED_INFO_QUERY =
         "SELECT P.id, P.name, P.height, P.weight, P.baseExp, R.name AS region " +
               "FROM Pokemon AS P " +
               "JOIN Regions AS R ON P.region = R.id " +
               "WHERE P.id = ?";
   private final static String SELECTED_TYPES_QUERY =
         "SELECT T.name " +
               "FROM Pokemon AS P " +
               "JOIN PokemonTypes AS Y ON Y.pokemonId = P.id " +
               "JOIN Types AS T ON Y.typeId = T.id " +
               "WHERE P.id = ?";
   private final static String SELECTED_MOVES_QUERY =
         "SELECT DISTINCT M.name " +
               "FROM Pokemon AS P " +
               "JOIN PokemonMoves AS O ON P.id = O.pokemonId " +
               "JOIN Moves AS M ON O.moveId = M.id " +
               "WHERE P.id = ?";
   private final static String SELECTED_EVOLUTIONS_QUERY =
         "SELECT K.id, K.name " +
               "FROM Pokemon AS P " +
               "JOIN PokemonEvolutions AS E ON E.evolvesFrom = P.id " +
               "JOIN Pokemon AS K ON E.id = K.id " +
               "WHERE P.id = ?";
   private final static String ALL_TEAM_INFO =
         "SELECT T._id, T.name, T.description " +
               "FROM Teams AS T " +
               "ORDER BY T._id";
   private final static String ALL_TEAM_POKEMON =
         "SELECT T.pokemonId " +
               "FROM TeamPokemon AS T " +
               "WHERE T.teamId = ?";
   private final static String POKEMON_BY_TEAM =
         "SELECT P._id, P.pokemonId, P.nickname, P.level, P.moveOne, P.moveTwo, P.moveThree, P.moveFour " +
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

   private DatabaseOpenHelper(Context context) {
      super(context, DB_NAME, null, DB_VERSION);
      mDatabase = getWritableDatabase();
   }

   public static DatabaseOpenHelper getInstance(Context context) {
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

   public PokemonListItem[] queryByType(String type) {
      Cursor cursor = mDatabase.rawQuery(TYPE_QUERY, new String[]{type});
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

   public PokemonListItem[] queryByRegion(String region) {
      Cursor cursor = mDatabase.rawQuery(REGION_QUERY, new String[]{region});
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

   public PokemonListItem[] queryByTypeAndRegion(String type, String region) {
      Cursor cursor = mDatabase.rawQuery(TYPE_REGION_QUERY, new String[]{type, region});
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

   public PokemonListItem[] queryAll() {
      Cursor cursor = mDatabase.rawQuery(ALL, null);
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

   public String[] querySelectedPokemonTypes(int id) {
      Cursor cursor = mDatabase.rawQuery(SELECTED_TYPES_QUERY, new String[]{String.valueOf(id)});
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

   public String[] querySelectedPokemonMoves(int id) {
      Cursor cursor = mDatabase.rawQuery(SELECTED_MOVES_QUERY, new String[]{String.valueOf(id)});
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

   public PokemonProfile querySelectedPokemonProfile(int id) {
      PokemonProfile pokemon;
      Cursor cursor = mDatabase.rawQuery(SELECTED_INFO_QUERY, new String[]{String.valueOf(id)});
      cursor.moveToFirst();

      pokemon = new PokemonProfile(cursor.getInt(cursor.getColumnIndex(ID_COL)),
            cursor.getString(cursor.getColumnIndex(NAME_COL)),
            cursor.getInt(cursor.getColumnIndex(HEIGHT_COL)),
            cursor.getInt(cursor.getColumnIndex(WEIGHT_COL)),
            cursor.getInt(cursor.getColumnIndex(BASE_EXP_COL)),
            cursor.getString(cursor.getColumnIndex(REGION_COL)),
            querySelectedPokemonTypes(id), querySelectedPokemonMoves(id));

      cursor.close();
      return pokemon;
   }

   public PokemonListItem[] queryPokemonEvolutions(int id) {
      Cursor cursor = mDatabase.rawQuery(SELECTED_EVOLUTIONS_QUERY, new String[]{String.valueOf(id)});
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
      mDatabase.insert(TEAM_POKEMON_TABLE, null, contentValues);
      return true;
   }

   public boolean updateTeam(int teamId, String name, String description) {
      ContentValues contentValues = new ContentValues();
      String idFilter = ROW_ID_COL + "=" + teamId;
      contentValues.put(NAME_COL, name);
      contentValues.put(DESCRIPTION_COL, description);
      mDatabase.update(TEAMS_TABLE, contentValues, idFilter, null);
      return true;
   }

   public int queryTeamCount(int teamId) {
      int result = 0;
      Cursor cursor = mDatabase.rawQuery(TEAM_SIZE, new String[]{String.valueOf(teamId)});
      cursor.moveToFirst();
      result = cursor.getInt(0);

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

   public PokemonTeamItem[] queryTeamPokemonIds(int teamId) {
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
               queryTeamPokemonIds(teamId));
         index++;
         cursor.moveToNext();
      }
      cursor.close();
      return teams;
   }

   public String queryPokemonMove(int moveId) {
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

   public String[] queryPokemonMoves(int moveOne, int moveTwo, int moveThree, int moveFour) {
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
                     cursor.getInt(cursor.getColumnIndex(MOVE_FOUR_COL))));
         index++;
         cursor.moveToNext();
      }
      cursor.close();
      return pokemon;
   }

   public int queryMoveIdByName(String moveName) {
      int moveId;
      Cursor cursor = mDatabase.rawQuery(QUERY_MOVE_ID, new String[]{moveName});
      cursor.moveToFirst();
      moveId = cursor.getInt(0);
      return moveId;
   }

   public boolean updateTeamPokemon(int memberId, String nickname, int level, String moveOne,
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
      mDatabase.update(TEAM_POKEMON_TABLE, contentValues, idFilter, null);
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
}
