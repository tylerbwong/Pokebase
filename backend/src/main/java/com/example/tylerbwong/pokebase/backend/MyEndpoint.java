/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.tylerbwong.pokebase.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.pokebase.tylerbwong.example.com",
                ownerName = "backend.pokebase.tylerbwong.example.com",
                packagePath = ""
        )
)
public class MyEndpoint {
    private String url;
    private final static String ALL =
            "SELECT P.id, P.name " +
                    "FROM Pokemon P";
    private final static String ALL_TYPES =
            "SELECT T.name FROM Types T";
    private final static String ALL_REGIONS =
            "SELECT R.name FROM Regions R";
    private final static String TYPE_QUERY =
            "SELECT P.id, P.name " +
                    "FROM Pokemon P " +
                    "JOIN PokemonTypes T ON P.id = T.pokemonId " +
                    "JOIN Types Y ON Y.id = T.typeId " +
                    "WHERE Y.name = ?";
    private final static String REGION_QUERY =
            "SELECT P.id, P.name " +
                    "FROM Pokemon P " +
                    "JOIN Regions R ON R.id = P.region " +
                    "WHERE R.name = ?";
    private final static String TYPE_REGION_QUERY =
            "SELECT P.id, P.name " +
                    "FROM Pokemon P " +
                    "JOIN PokemonTypes T ON P.id = T.pokemonId " +
                    "JOIN Types Y ON Y.id = T.typeId " +
                    "JOIN Regions R ON R.id = P.region " +
                    "WHERE Y.name = ? " +
                    "AND R.name = ?";
    private final static String ADD_NEW_USER =
            "INSERT INTO Users VALUES (0, ?, ?)";

    private final static String CHECK_USER =
            "SELECT COUNT(*) AS count FROM Users U WHERE U.name = ?";

    private final static String SELECTED_INFO_QUERY =
            "SELECT P.id, P.name, P.height, P.weight, P.baseExp, R.name AS region " +
                    "FROM Pokemon P " +
                    "JOIN Regions R ON P.region = R.id " +
                    "WHERE P.id = ?";
    private final static String SELECTED_TYPES_QUERY =
            "SELECT T.name " +
                    "FROM Pokemon P " +
                    "JOIN PokemonTypes Y ON Y.pokemonId = P.id " +
                    "JOIN Types T ON Y.typeId = T.id " +
                    "WHERE P.id = ?";
    private final static String SELECTED_MOVES_QUERY =
            "SELECT DISTINCT M.name " +
                    "FROM Pokemon P " +
                    "JOIN PokemonMoves O ON P.id = O.pokemonId " +
                    "JOIN Moves M ON O.moveId = M.id " +
                    "WHERE P.id = ?";
    private final static String SELECTED_EVOLUTIONS_QUERY =
            "SELECT K.id, K.name " +
                    "FROM Pokemon P " +
                    "JOIN PokemonEvolutions E ON E.evolvesFrom = P.id " +
                    "JOIN Pokemon K ON E.id = K.id " +
                    "WHERE P.id = ?";
    private final static String ALL_TEAM_INFO =
            "SELECT T.id, T.name, T.description " +
                    "FROM Teams T " +
                    "JOIN UserTeams M ON T.id = M.teamId " +
                    "JOIN Users U ON U.id = M.userId " +
                    "WHERE U.name = ? " +
                    "ORDER BY T.id";
    private final static String ALL_TEAM_POKEMON =
            "SELECT T.id, K.pokemonId " +
                    "FROM Teams T " +
                    "JOIN UserTeams M ON T.id = M.teamId " +
                    "JOIN Users U ON U.id = M.userId " +
                    "JOIN TeamPokemon K ON K.teamId = T.id " +
                    "WHERE U.name = ? " +
                    "ORDER BY T.id";
    private final static String POKEMON_BY_TEAM =
            "SELECT P.id, P.pokemonId, P.nickname, P.level, P.moveOne, P.moveTwo, P.moveThree, P.moveFour " +
                    "FROM TeamPokemon P " +
                    "WHERE P.teamId = ?";

    private final static String USER_ID =
            "SELECT U.id FROM Users U WHERE U.name = ?";

    private final static String NEW_TEAM =
            "INSERT INTO Teams Values (0, ?, ?)";

    private final static String UPDATE_TEAM =
            "UPDATE Teams SET name = ?, description = ? WHERE id = ?";

    private static final String TEAM_NAMES_BY_USER = "SELECT T.id, T.name FROM Teams T " +
            "JOIN UserTeams E ON E.teamId = T.id " +
            "JOIN Users U ON U.id = E.userId " +
            "WHERE U.name = ? " +
            "AND T.id != ALL " +
            "(SELECT T.id " +
            "FROM Teams T " +
            "JOIN UserTeams E ON E.teamId = T.id " +
            "JOIN Users U ON U.id = E.userId " +
            "JOIN TeamPokemon X ON X.teamId = T.id " +
            "WHERE U.name = ? " +
            "GROUP BY T.id " +
            "HAVING COUNT(*) = 6)";

    private static final String MAX_TEAM_ID =
            "SELECT MAX(T.id) AS maxId FROM Teams T";

    private static final String NEW_USER_TEAM =
            "INSERT INTO UserTeams VALUES (?, ?)";

    private static final String NEW_POKEMON_TEAM =
            "INSERT INTO TeamPokemon Values (0, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final static String POKEMON_MOVES =
            "SELECT M.name FROM Moves M WHERE M.id = ?";

    private final static String UPDATE_POKEMON =
            "UPDATE TeamPokemon SET nickname = ?, level = ?, moveOne = ?, moveTwo = ?, " +
                  "moveThree = ?, moveFour = ? WHERE id = ?";

    private final static String QUERY_MOVE_ID =
            "SELECT M.id FROM Moves M WHERE M.name = ?";

    @ApiMethod(name = "queryAllTypesRegions")
    public QueryResult queryAllTypesRegions() {
        instantiateDriver();

        List<String> types = new ArrayList<>();
        List<String> regions = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(ALL_TYPES);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                types.add(resultSet.getString("name"));
            }
            preparedStatement = connection.prepareStatement(ALL_REGIONS);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                regions.add(resultSet.getString("name"));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            types.add(e.getMessage());
            regions.add(e.getMessage());
        }

        return new TypeRegionResult(types, regions);
    }

    @ApiMethod(name = "queryByType")
    public QueryResult queryByType(@Named("type") String type) {
        instantiateDriver();

        List<Integer> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(TYPE_QUERY);
            preparedStatement.setString(1, type);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ids.add(resultSet.getInt("id"));
                names.add(resultSet.getString("name"));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            ids.add(0);
            names.add(e.getMessage());
        }

        return new PokemonListResult(QueryResult.POKEMON_BY_TYPE, ids, names);
    }

    @ApiMethod(name = "queryByRegion")
    public QueryResult queryByRegion(@Named("region") String region) {
        instantiateDriver();

        List<Integer> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(REGION_QUERY);
            preparedStatement.setString(1, region);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ids.add(resultSet.getInt("id"));
                names.add(resultSet.getString("name"));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            ids.add(0);
            names.add(e.getMessage());
        }

        return new PokemonListResult(QueryResult.POKEMON_BY_REGION, ids, names);
    }

    @ApiMethod(name = "queryByTypeAndRegion")
    public QueryResult queryByTypeAndRegion(@Named("type") String type, @Named("region") String region) {
        instantiateDriver();

        List<Integer> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(TYPE_REGION_QUERY);
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, region);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ids.add(resultSet.getInt("id"));
                names.add(resultSet.getString("name"));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            ids.add(0);
            names.add(e.getMessage());
        }
        return new PokemonListResult(QueryResult.POKEMON_BY_TYPE_AND_REGION, ids, names);
    }

    @ApiMethod(name = "queryAll")
    public QueryResult queryAll() {
        instantiateDriver();

        List<Integer> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(ALL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ids.add(resultSet.getInt("id"));
                names.add(resultSet.getString("name"));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            ids.add(0);
            names.add(e.getMessage());
        }
        return new PokemonListResult(QueryResult.ALL_POKEMON, ids, names);
    }

    @ApiMethod(name = "querySelected")
    public QueryResult querySelected(@Named("id") int id) {
        instantiateDriver();

        List<Integer> intInfo = new ArrayList<>();
        List<String> stringInfo = new ArrayList<>();
        List<String> movesInfo = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(SELECTED_INFO_QUERY);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                intInfo.add(resultSet.getInt("id"));
                stringInfo.add(resultSet.getString("name"));
                intInfo.add(resultSet.getInt("height"));
                intInfo.add(resultSet.getInt("weight"));
                intInfo.add(resultSet.getInt("baseExp"));
                stringInfo.add(resultSet.getString("region"));
            }
            preparedStatement = connection.prepareStatement(SELECTED_TYPES_QUERY);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                stringInfo.add(resultSet.getString("name"));
            }
            preparedStatement = connection.prepareStatement(SELECTED_MOVES_QUERY);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                movesInfo.add(resultSet.getString("name"));
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            stringInfo.add(e.getMessage());
        }

        return new PokemonInfoResult(intInfo, stringInfo, movesInfo);
    }

    @ApiMethod(name = "queryEvolutions")
    public QueryResult queryEvolutions(@Named("id") int id) {
        instantiateDriver();

        List<Integer> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(SELECTED_EVOLUTIONS_QUERY);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ids.add(resultSet.getInt("id"));
                names.add(resultSet.getString("name"));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            ids.add(0);
            names.add(e.getMessage());
        }

        return new PokemonListResult(QueryResult.SELECTED_POKEMON_EVOLUTIONS, ids, names);
    }

    @ApiMethod(name = "queryCheckUser")
    public QueryResult queryCheckUser(@Named("name") String name) {
        boolean success = false;
        int count = 0;
        ResultSet resultSet;
        instantiateDriver();

        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(CHECK_USER);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                count = resultSet.getInt("count");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new CheckResult(QueryResult.CHECK_USERNAME, success, count);
    }

    @ApiMethod(name = "newUser")
    public QueryResult newUser(@Named("name") String name, @Named("gender") String gender) {
        instantiateDriver();

        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_NEW_USER);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, gender);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new CheckResult(QueryResult.NEW_USER, true);
    }

    @ApiMethod(name = "newTeam")
    public QueryResult newTeam(@Named("username") String username,
                               @Named("name") String name, @Named("description") String description) {
        ResultSet resultSet;
        int userId = 0;
        int teamId = 0;
        instantiateDriver();

        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(USER_ID);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                userId = resultSet.getInt("id");
            }

            preparedStatement = connection.prepareStatement(NEW_TEAM);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(MAX_TEAM_ID);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                teamId = resultSet.getInt("maxId");
            }

            preparedStatement = connection.prepareStatement(NEW_USER_TEAM);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, teamId);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new CheckResult(QueryResult.NEW_TEAM, true);
    }

    @ApiMethod(name = "newPokemonTeam")
    public QueryResult newPokemonTeam(@Named("teamId") int teamId, @Named("pokemonId") int pokemonId,
                                      @Named("nickname") String nickname, @Named("level") int level,
                                      @Named("moveOne") int moveOne, @Named("moveTwo") int moveTwo,
                                      @Named("moveThree") int moveThree, @Named("moveFour") int moveFour) {

        instantiateDriver();
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(NEW_POKEMON_TEAM);
            preparedStatement.setInt(1, teamId);
            preparedStatement.setInt(2, pokemonId);
            preparedStatement.setString(3, nickname);
            preparedStatement.setInt(4, level);
            preparedStatement.setInt(5, moveOne);
            preparedStatement.setInt(6, moveTwo);
            preparedStatement.setInt(7, moveThree);
            preparedStatement.setInt(8, moveFour);
            preparedStatement.executeUpdate();
            connection.close();
        }
        catch (Exception e) {

        }

        return new CheckResult(QueryResult.NEW_POKEMON_ON_TEAM, true);
    }

    @ApiMethod(name = "updateTeam")
    public QueryResult updateTeam(@Named("teamId") int teamId, @Named("name") String name,
                                  @Named("description") String description) {
        instantiateDriver();

        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TEAM);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, teamId);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new CheckResult(QueryResult.UPDATE_TEAM, true);
    }

    @ApiMethod(name = "queryTeamNames")
    public QueryResult queryTeamNames(@Named("name") String name) {
        instantiateDriver();

        List<Integer> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(TEAM_NAMES_BY_USER);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ids.add(resultSet.getInt("id"));
                names.add(resultSet.getString("name"));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            ids.add(0);
            names.add(e.getMessage());
        }

        return new TeamNameResult(ids, names);
    }

    @ApiMethod(name = "queryAllTeams")
    public QueryResult queryAllTeams(@Named("userName") String userName) {
        instantiateDriver();

        List<Integer> teamIds = new ArrayList<>();
        List<String> teamNames = new ArrayList<>();
        List<String> teamDescriptions = new ArrayList<>();
        List<List<Integer>> pokemon = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(ALL_TEAM_INFO);
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                teamIds.add(resultSet.getInt("id"));
                teamNames.add(resultSet.getString("name"));
                teamDescriptions.add(resultSet.getString("description"));
            }
            preparedStatement = connection.prepareStatement(ALL_TEAM_POKEMON);
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();

            for (int i = 0; i < teamIds.size(); i++) {
                pokemon.add(new ArrayList<Integer>());
            }

            int teamI = 0;
            while (resultSet.next()) {
                int teamId = resultSet.getInt("id");
                int pokemonId = resultSet.getInt("pokemonId");
                boolean foundTeam = false;

                while (!foundTeam) {
                    if (teamIds.get(teamI) == teamId) {
                        foundTeam = true;
                        pokemon.get(teamI).add(pokemonId);
                    }
                    else {
                        teamI++;
                    }
                }
            }

            for (int i = 0; i < teamIds.size(); i++) {
                if (pokemon.get(i).isEmpty()) {
                    pokemon.get(i).add(-1);
                }
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            teamNames.add(e.getMessage());
        }
        return new PokemonTeamResult(teamIds, teamNames, teamDescriptions, pokemon);
    }

    @ApiMethod(name = "queryTeamId")
    public QueryResult queryTeamId(@Named("teamId") int teamId) {
        instantiateDriver();

        List<Integer> memberIds = new ArrayList<>();
        List<Integer> pokemonIds = new ArrayList<>();
        List<String> nicknames = new ArrayList<>();
        List<Integer> levels = new ArrayList<>();
        List<List<String>> moves = new ArrayList<>();

        ResultSet moveResultSet;
        String newMove = "";
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(POKEMON_BY_TEAM);
            preparedStatement.setInt(1, teamId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                memberIds.add(resultSet.getInt("id"));
                pokemonIds.add(resultSet.getInt("pokemonId"));
                nicknames.add(resultSet.getString("nickname"));
                levels.add(resultSet.getInt("level"));

                List<String> moveSet = new ArrayList<>();
                String move = resultSet.getString("moveOne");
                if (move.equals("0")) {
                    moveSet.add("None");
                }
                else {
                    preparedStatement = connection.prepareStatement(POKEMON_MOVES);
                    preparedStatement.setInt(1, Integer.valueOf(move));
                    moveResultSet = preparedStatement.executeQuery();
                    while (moveResultSet.next()) {
                        newMove = moveResultSet.getString("name");
                    }
                    moveSet.add(newMove);
                }
                move = resultSet.getString("moveTwo");
                if (move.equals("0")) {
                    moveSet.add("None");
                }
                else {
                    preparedStatement = connection.prepareStatement(POKEMON_MOVES);
                    preparedStatement.setInt(1, Integer.valueOf(move));
                    moveResultSet = preparedStatement.executeQuery();
                    while (moveResultSet.next()) {
                        newMove = moveResultSet.getString("name");
                    }
                    moveSet.add(newMove);
                }
                move = resultSet.getString("moveThree");
                if (move.equals("0")) {
                    moveSet.add("None");
                }
                else {
                    preparedStatement = connection.prepareStatement(POKEMON_MOVES);
                    preparedStatement.setInt(1, Integer.valueOf(move));
                    moveResultSet = preparedStatement.executeQuery();
                    while (moveResultSet.next()) {
                        newMove = moveResultSet.getString("name");
                    }
                    moveSet.add(newMove);
                }
                move = resultSet.getString("moveFour");
                if (move.equals("0")) {
                    moveSet.add("None");
                }
                else {
                    preparedStatement = connection.prepareStatement(POKEMON_MOVES);
                    preparedStatement.setInt(1, Integer.valueOf(move));
                    moveResultSet = preparedStatement.executeQuery();
                    while (moveResultSet.next()) {
                        newMove = moveResultSet.getString("name");
                    }
                    moveSet.add(newMove);
                }
                moves.add(moveSet);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            nicknames.add(e.getMessage());
        }

        return new TeamPokemonResult(memberIds, pokemonIds, nicknames, levels, moves);
    }

    @ApiMethod(name = "deleteTeam")
    public QueryResult deleteTeam(@Named("userId") int userId, @Named("teamId") int teamId) {
        //DELETE SPECIFIC TEAM

        //DELETE FROM TeamPokemon TP
        //JOIN UserTeams UT ON UT.teamId = UT.teamId
        //JOIN Teams T ON T.id = UT.teamId
        //JOIN Users U ON U.id = UT.userId
        //WHERE U.id = (userId ?)
        //AND T.id = (teamId ?)

        return new CheckResult(QueryResult.DELETE_TEAM, true);
    }

    @ApiMethod(name = "deleteTeamPokemon")
    public QueryResult deleteTeamPokemon(@Named("userId") int userId, @Named("teamId") int teamId,
                                         @Named("pokemonId") int pokemonId) {
        // DELETE POKEMON FROM TEAM

        //DELETE FROM TeamPokemon TP
        //JOIN UserTeams UT ON UT.teamId = UT.teamId
        //JOIN Teams T ON T.id = UT.teamId
        //JOIN Users U ON U.id = UT.userId
        //WHERE U.id = (userId ?)
        //AND T.id = (teamId ?)
        //AND TP.pokemonId = (pokemonId ?)

        return new CheckResult(QueryResult.DELETE_POKEMON, true);
    }

    @ApiMethod(name = "updateTeamPokemon")
    public QueryResult updateTeamPokemon(@Named("memberId") int memberId, @Named("nickname") String nickname,
                                         @Named("level") int level, @Named("moveOne") String moveOne,
                                         @Named("moveTwo") String moveTwo, @Named("moveThree") String moveThree,
                                         @Named("moveFour") String moveFour) {

        int moveOneId = 0, moveTwoId = 0, moveThreeId = 0, moveFourId = 0;
        ResultSet resultSet;
        instantiateDriver();

        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_MOVE_ID);
            preparedStatement.setString(1, moveOne);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                moveOneId = resultSet.getInt("id");
            }
            connection.close();
        }
        catch (Exception e) {

        }

        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_MOVE_ID);
            preparedStatement.setString(1, moveTwo);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                moveTwoId = resultSet.getInt("id");
            }
            connection.close();
        }
        catch (Exception e) {

        }

        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_MOVE_ID);
            preparedStatement.setString(1, moveThree);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                moveThreeId = resultSet.getInt("id");
            }
            connection.close();
        }
        catch (Exception e) {

        }

        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_MOVE_ID);
            preparedStatement.setString(1, moveFour);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                moveFourId = resultSet.getInt("id");
            }
            connection.close();
        }
        catch (Exception e) {

        }

        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_POKEMON);
            preparedStatement.setString(1, nickname);
            preparedStatement.setInt(2, level);
            preparedStatement.setInt(3, moveOneId);
            preparedStatement.setInt(4, moveTwoId);
            preparedStatement.setInt(5, moveThreeId);
            preparedStatement.setInt(6, moveFourId);
            preparedStatement.setInt(7, memberId);
            preparedStatement.executeUpdate();
            connection.close();
        }
        catch (Exception e) {

        }

        return new CheckResult(QueryResult.UPDATE_POKEMON, true);
    }

    private void instantiateDriver() {
        if (System
                .getProperty("com.google.appengine.runtime.version").startsWith("Google App Engine/")) {
            // Check the System properties to determine if we are running on appengine or not
            // Google App Engine sets a few system properties that will reliably be present on a remote
            // instance.
            url = System.getProperty("ae-cloudsql.cloudsql-database-url");
            try {
                // Load the class that provides the new "jdbc:google:mysql://" prefix.
                Class.forName("com.mysql.jdbc.GoogleDriver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            // Set the url with the local MySQL database connection url when running locally
            url = System.getProperty("ae-cloudsql.local-database-url");
        }
    }
}
