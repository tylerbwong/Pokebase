package com.example.tylerbwong.pokebase.backend;

import java.util.List;

/**
 * Created by brittanyberlanga on 6/10/16.
 */
public class PokemonTeamResult extends QueryResult{
    private List<Integer> mTeamIds;
    private List<String> mTeamNames;
    private List<String> mTeamDescriptions;
    private List<List<Integer>> mPokemon;

    public PokemonTeamResult(List<Integer> teamIds, List<String> teamNames, List<String> teamDescriptions,
                             List<List<Integer>> pokemon) {
        this.mTeamIds = teamIds;
        this.mTeamNames = teamNames;
        this.mTeamDescriptions = teamDescriptions;
        this.mPokemon = pokemon;
    }
    @Override
    public List<String> getStringInfo() {
        return mTeamNames;
    }

    @Override
    public List<String> getMoreStringInfo() {
        return mTeamDescriptions;
    }

    @Override
    public String getType() {
        return QueryResult.ALL_TEAMS;
    }

    @Override
    public List<List<Integer>> getListOfLists() {
        return mPokemon;
    }

    @Override
    public List<Integer> getIntInfo() {
        return mTeamIds;
    }
}
