package com.example.tylerbwong.pokebase.backend;

import java.util.List;

/**
 * Created by brittanyberlanga on 6/9/16.
 */
public abstract class QueryResult {
    public static final String ALL_POKEMON = "all";
    public static final String POKEMON_BY_TYPE = "by_type";
    public static final String POKEMON_BY_REGION = "by_region";
    public static final String POKEMON_BY_TYPE_AND_REGION = "by_type_and_region";
    public static final String SELECTED_POKEMON = "selected";
    public static final String SELECTED_POKEMON_EVOLUTIONS = "selected_evolutions";
    public static final String POKEMON_BY_NAME = "by_name";
    public static final String POKEMON_MOVES = "pokemon_moves";
    public static final String CHECK_USERNAME = "check_username";
    public static final String NEW_USER = "new_user";
    public static final String LOGIN = "login";
    public static final String NEW_TEAM = "new_team";
    public static final String NEW_POKEMON_ON_TEAM = "new_team_pokemon";
    public static final String UPDATE_TEAM = "update_team";
    public static final String UPDATE_POKEMON = "update_pokemon";
    public static final String DELETE_POKEMON = "delete_pokemon";
    public static final String DELETE_TEAM = "delete_team";
    public static final String ALL_TYPES_REGIONS = "all_types_and_regions";
    public static final String ALL_TEAMS = "all_teams";
    public static final String TEAM_BY_ID = "team_by_id";
    public static final String TEAM_NAMES = "team_names";
    public List<String> getStringInfo() {
        return null;
    }
    public List<String> getMoreStringInfo() {
        return null;
    }
    public List<Integer> getIntInfo() {
        return null;
    }
    public boolean getBoolean() {
        return false;
    }
    public String getType() {
        return "";
    }
    public int getCount() {
        return 0;
    }
    public List<List<Integer>> getListOfLists() {
        return null;
    }
    public List<List<String>> getListOfStringLists() {
        return null;
    }
    public List<Integer> getMoreIntInfo() {
        return null;
    }
    public List<Integer> getMoreMoreIntInfo() {
        return null;
    }
}
