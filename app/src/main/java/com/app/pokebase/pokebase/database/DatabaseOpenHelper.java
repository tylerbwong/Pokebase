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
public class DatabaseOpenHelper extends SQLiteAssetHelper {
   private final static String DB_NAME = "Pokebase.db";
   private static final int DB_VERSION = 1;
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
   private final static String UPDATE_TEAM =
         "UPDATE Teams SET name = ?, description = ? WHERE _id = ?";
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

   public DatabaseOpenHelper(Context context) {
      super(context, DB_NAME, null, DB_VERSION);
   }

   @Override
   public synchronized void close() {
      super.close();
   }

   @Override
   public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

   }

   public List<String> queryAllTypes() {
      SQLiteDatabase database = getReadableDatabase();
      List<String> typeList = new ArrayList<>();
      Cursor cursor = database.rawQuery(ALL_TYPES, null);
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         typeList.add(cursor.getString(cursor.getColumnIndex(NAME_COL)));
         cursor.moveToNext();
      }
      cursor.close();
      database.close();
      return typeList;
   }

   public List<String> queryAllRegions() {
      SQLiteDatabase database = getReadableDatabase();
      List<String> regionList = new ArrayList<>();
      Cursor cursor = database.rawQuery(ALL_REGIONS, null);
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         regionList.add(cursor.getString(cursor.getColumnIndex(NAME_COL)));
         cursor.moveToNext();
      }
      cursor.close();
      database.close();
      return regionList;
   }

   public List<PokemonListItem> queryByType(String type) {
      SQLiteDatabase database = getReadableDatabase();
      List<PokemonListItem> typePokemon = new ArrayList<>();
      Cursor cursor = database.rawQuery(TYPE_QUERY, new String[]{type});
      cursor.moveToFirst();

      PokemonListItem tempPokemon;

      while (!cursor.isAfterLast()) {
         tempPokemon = new PokemonListItem(cursor.getInt(cursor.getColumnIndex(ID_COL)),
               cursor.getString(cursor.getColumnIndex(NAME_COL)));
         typePokemon.add(tempPokemon);
         cursor.moveToNext();
      }
      cursor.close();
      database.close();
      return typePokemon;
   }

   public List<PokemonListItem> queryByRegion(String region) {
      SQLiteDatabase database = getReadableDatabase();
      List<PokemonListItem> regionPokemon = new ArrayList<>();
      Cursor cursor = database.rawQuery(REGION_QUERY, new String[]{region});
      cursor.moveToFirst();

      PokemonListItem tempPokemon;

      while (!cursor.isAfterLast()) {
         tempPokemon = new PokemonListItem(cursor.getInt(cursor.getColumnIndex(ID_COL)),
               cursor.getString(cursor.getColumnIndex(NAME_COL)));
         regionPokemon.add(tempPokemon);
         cursor.moveToNext();
      }
      cursor.close();
      database.close();
      return regionPokemon;
   }

   public List<PokemonListItem> queryByTypeAndRegion(String type, String region) {
      SQLiteDatabase database = getReadableDatabase();
      List<PokemonListItem> typeRegionPokemon = new ArrayList<>();
      Cursor cursor = database.rawQuery(TYPE_REGION_QUERY, new String[]{type, region});
      cursor.moveToFirst();

      PokemonListItem tempPokemon;

      while (!cursor.isAfterLast()) {
         tempPokemon = new PokemonListItem(cursor.getInt(cursor.getColumnIndex(ID_COL)),
               cursor.getString(cursor.getColumnIndex(NAME_COL)));
         typeRegionPokemon.add(tempPokemon);
         cursor.moveToNext();
      }
      cursor.close();
      database.close();
      return typeRegionPokemon;
   }

   public List<PokemonListItem> queryAll() {
      SQLiteDatabase database = getReadableDatabase();
      List<PokemonListItem> pokemon = new ArrayList<>();
      Cursor cursor = database.rawQuery(ALL, null);
      cursor.moveToFirst();

      PokemonListItem tempPokemon;

      while (!cursor.isAfterLast()) {
         tempPokemon = new PokemonListItem(cursor.getInt(cursor.getColumnIndex(ID_COL)),
               cursor.getString(cursor.getColumnIndex(NAME_COL)));
         pokemon.add(tempPokemon);
         cursor.moveToNext();
      }
      cursor.close();
      database.close();
      return pokemon;
   }

   public List<String> querySelectedPokemonTypes(int id) {
      SQLiteDatabase database = getReadableDatabase();
      List<String> pokemonTypes = new ArrayList<>();
      Cursor cursor = database.rawQuery(SELECTED_TYPES_QUERY, new String[]{String.valueOf(id)});
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         pokemonTypes.add(cursor.getString(cursor.getColumnIndex(NAME_COL)));
         cursor.moveToNext();
      }
      cursor.close();
      database.close();
      return pokemonTypes;
   }

   public List<String> querySelectedPokemonMoves(int id) {
      SQLiteDatabase database = getReadableDatabase();
      List<String> pokemonMoves = new ArrayList<>();
      Cursor cursor = database.rawQuery(SELECTED_MOVES_QUERY, new String[]{String.valueOf(id)});
      cursor.moveToFirst();

      while (!cursor.isAfterLast()) {
         pokemonMoves.add(cursor.getString(cursor.getColumnIndex(NAME_COL)));
         cursor.moveToNext();
      }
      cursor.close();
      database.close();
      return pokemonMoves;
   }

   public PokemonProfile querySelectedPokemonProfile(int id) {
      SQLiteDatabase database = getReadableDatabase();
      PokemonProfile pokemon;
      Cursor cursor = database.rawQuery(SELECTED_INFO_QUERY, new String[]{String.valueOf(id)});
      cursor.moveToFirst();

      pokemon = new PokemonProfile(cursor.getInt(cursor.getColumnIndex(ID_COL)),
            cursor.getString(cursor.getColumnIndex(NAME_COL)),
            cursor.getInt(cursor.getColumnIndex(HEIGHT_COL)),
            cursor.getInt(cursor.getColumnIndex(WEIGHT_COL)),
            cursor.getInt(cursor.getColumnIndex(BASE_EXP_COL)),
            cursor.getString(cursor.getColumnIndex(REGION_COL)),
            querySelectedPokemonTypes(id), querySelectedPokemonMoves(id));

      cursor.close();
      database.close();
      return pokemon;
   }

   public List<PokemonListItem> queryPokemonEvolutions(int id) {
      SQLiteDatabase database = getReadableDatabase();
      List<PokemonListItem> pokemonEvolutions = new ArrayList<>();
      Cursor cursor = database.rawQuery(SELECTED_EVOLUTIONS_QUERY, new String[]{String.valueOf(id)});
      cursor.moveToFirst();

      PokemonListItem tempPokemon;

      while (!cursor.isAfterLast()) {
         tempPokemon = new PokemonListItem(cursor.getInt(cursor.getColumnIndex(ID_COL)),
               cursor.getString(cursor.getColumnIndex(NAME_COL)));
         pokemonEvolutions.add(tempPokemon);
         cursor.moveToNext();
      }
      cursor.close();
      database.close();
      return pokemonEvolutions;
   }

   public boolean insertTeam(String name, String description) {
      SQLiteDatabase database = getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put(NAME_COL, name);
      contentValues.put(DESCRIPTION_COL, description);
      database.insert(TEAMS_TABLE, null, contentValues);
      database.close();
      return true;
   }

   public boolean insertTeamPokemon(int teamId, int pokemonId, String nickname, int level,
                                    int moveOne, int moveTwo, int moveThree, int moveFour) {
      SQLiteDatabase database = getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put(TEAM_ID_COL, teamId);
      contentValues.put(POKEMON_ID_COL, pokemonId);
      contentValues.put(NICKNAME_COL, nickname);
      contentValues.put(LEVEL_COL, level);
      contentValues.put(MOVE_ONE_COL, moveOne);
      contentValues.put(MOVE_TWO_COL, moveTwo);
      contentValues.put(MOVE_THREE_COL, moveThree);
      contentValues.put(MOVE_FOUR_COL, moveFour);
      database.insert(TEAM_POKEMON_TABLE, null, contentValues);
      database.close();
      return true;
   }

   public boolean updateTeam(int teamId, String name, String description) {
      SQLiteDatabase database = getWritableDatabase();
      database.rawQuery(UPDATE_TEAM, new String[]{name, description, String.valueOf(teamId)});
      database.close();
      return true;
   }

   public int queryTeamCount(int teamId) {
      SQLiteDatabase database = getReadableDatabase();
      int result = 0;
      Cursor cursor = database.rawQuery(TEAM_SIZE, new String[]{String.valueOf(teamId)});
      cursor.moveToFirst();
      result = cursor.getInt(0);

      cursor.close();
      database.close();
      return result;
   }

   public List<Pair<Integer, String>> queryTeamIdsAndNames() {
      SQLiteDatabase database = getReadableDatabase();
      List<Pair<Integer, String>> teamNames = new ArrayList<>();
      Cursor cursor = database.rawQuery(TEAM_NAMES, null);
      cursor.moveToFirst();

      Pair<Integer, String> tempPair;
      int teamId;

      while (!cursor.isAfterLast()) {
         teamId = cursor.getInt(cursor.getColumnIndex(ROW_ID_COL));

         if (queryTeamCount(teamId) < MAX_TEAM_SIZE) {
            tempPair = new Pair<>(teamId, cursor.getString(cursor.getColumnIndex(NAME_COL)));
            teamNames.add(tempPair);
         }
         cursor.moveToNext();
      }
      cursor.close();
      database.close();
      return teamNames;
   }

   public List<PokemonTeamItem> queryTeamPokemonIds(int teamId) {
      SQLiteDatabase database = getReadableDatabase();
      List<PokemonTeamItem> teamPokemon = new ArrayList<>();
      Cursor cursor = database.rawQuery(ALL_TEAM_POKEMON, new String[]{String.valueOf(teamId)});
      cursor.moveToFirst();

      PokemonTeamItem tempPokemon;

      while (!cursor.isAfterLast()) {
         tempPokemon = new PokemonTeamItem(cursor.getInt(cursor.getColumnIndex(POKEMON_ID_COL)));
         teamPokemon.add(tempPokemon);
         cursor.moveToNext();
      }
      cursor.close();
      database.close();
      return teamPokemon;
   }

   public List<Team> queryAllTeams() {
      SQLiteDatabase database = getReadableDatabase();
      List<Team> teams = new ArrayList<>();
      Cursor cursor = database.rawQuery(ALL_TEAM_INFO, null);
      cursor.moveToFirst();

      Team tempTeam;
      int teamId;

      while (!cursor.isAfterLast()) {
         teamId = cursor.getInt(cursor.getColumnIndex(ROW_ID_COL));
         tempTeam = new Team(teamId, cursor.getString(cursor.getColumnIndex(NAME_COL)),
               cursor.getString(cursor.getColumnIndex(DESCRIPTION_COL)),
               queryTeamPokemonIds(teamId));
         teams.add(tempTeam);
         cursor.moveToNext();
      }
      cursor.close();
      database.close();
      return teams;
   }

   public String queryPokemonMove(int moveId) {
      SQLiteDatabase database = getReadableDatabase();
      String result = "";
      Cursor cursor = database.rawQuery(SINGLE_MOVE, new String[]{String.valueOf(moveId)});
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

   public List<String> queryPokemonMoves(int moveOne, int moveTwo, int moveThree, int moveFour) {
      List<String> moves = new ArrayList<>();
      String moveOneName = queryPokemonMove(moveOne);
      String moveTwoName = queryPokemonMove(moveTwo);
      String moveThreeName = queryPokemonMove(moveThree);
      String moveFourName = queryPokemonMove(moveFour);

      moves.add(moveOneName);
      moves.add(moveTwoName);
      moves.add(moveThreeName);
      moves.add(moveFourName);

      return moves;
   }

   public List<PokemonTeamMember> queryPokemonTeamMembers(int teamId) {
      SQLiteDatabase database = getReadableDatabase();
      List<PokemonTeamMember> pokemon = new ArrayList<>();
      Cursor cursor = database.rawQuery(POKEMON_BY_TEAM, new String[]{String.valueOf(teamId)});
      cursor.moveToFirst();

      PokemonTeamMember tempPokemon;

      while (!cursor.isAfterLast()) {
         tempPokemon = new PokemonTeamMember(cursor.getInt(cursor.getColumnIndex(ROW_ID_COL)),
               cursor.getInt(cursor.getColumnIndex(POKEMON_ID_COL)),
               cursor.getString(cursor.getColumnIndex(NICKNAME_COL)),
               cursor.getInt(cursor.getColumnIndex(LEVEL_COL)),
               queryPokemonMoves(cursor.getInt(cursor.getColumnIndex(MOVE_ONE_COL)),
                     cursor.getInt(cursor.getColumnIndex(MOVE_TWO_COL)),
                     cursor.getInt(cursor.getColumnIndex(MOVE_THREE_COL)),
                     cursor.getInt(cursor.getColumnIndex(MOVE_FOUR_COL))));
         pokemon.add(tempPokemon);
         cursor.moveToNext();
      }
      cursor.close();
      database.close();
      return pokemon;
   }

   public int queryMoveIdByName(String moveName) {
      SQLiteDatabase database = getReadableDatabase();
      int moveId = 0;
      Cursor cursor = database.rawQuery(QUERY_MOVE_ID, new String[]{moveName});
      cursor.moveToFirst();
      moveId = cursor.getInt(0);
      return moveId;
   }

   public boolean updateTeamPokemon(int memberId, String nickname, int level, String moveOne,
                                    String moveTwo, String moveThree, String moveFour) {
      SQLiteDatabase database = getWritableDatabase();
      int moveOneId = queryMoveIdByName(moveOne);
      int moveTwoId = queryMoveIdByName(moveTwo);
      int moveThreeId = queryMoveIdByName(moveThree);
      int moveFourId = queryMoveIdByName(moveFour);
      String idFilter = ROW_ID_COL + "=" + "'" + memberId + "'";
      ContentValues contentValues = new ContentValues();

      contentValues.put(NICKNAME_COL, nickname);
      contentValues.put(LEVEL_COL, level);
      contentValues.put(MOVE_ONE_COL, moveOneId);
      contentValues.put(MOVE_TWO_COL, moveTwoId);
      contentValues.put(MOVE_THREE_COL, moveThreeId);
      contentValues.put(MOVE_FOUR_COL, moveFourId);
      database.update(TEAM_POKEMON_TABLE, contentValues, idFilter, null);
      database.close();
      return true;
   }

   // TODO deleteTeam
   public boolean deleteTeam(int teamId) {
      return false;
   }

   // TODO deleteTeamPokemon
   public boolean deleteTeamPokemon(int memberId) {
      return false;
   }
}
