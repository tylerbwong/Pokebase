package com.example.tylerbwong.pokebase.backend;

import java.util.List;

/**
 * Created by brittanyberlanga on 6/10/16.
 */
public class TeamNameResult extends QueryResult {
    private List<Integer> mTeamIds;
    private List<String> mTeamNames;

    public TeamNameResult(List<Integer> teamIds, List<String> teamNames) {
        this.mTeamIds = teamIds;
        this.mTeamNames = teamNames;
    }
    @Override
    public List<String> getStringInfo() {
        return mTeamNames;
    }

    @Override
    public List<Integer> getIntInfo() {
        return mTeamIds;
    }

    @Override
    public String getType() {
        return QueryResult.TEAM_NAMES;
    }
}
