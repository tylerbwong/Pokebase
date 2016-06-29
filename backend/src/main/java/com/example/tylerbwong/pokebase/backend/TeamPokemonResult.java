package com.example.tylerbwong.pokebase.backend;

import java.util.List;

/**
 * Created by brittanyberlanga on 6/10/16.
 */
public class TeamPokemonResult extends QueryResult {
    private List<Integer> mMemberId;
    private List<Integer> mPokemonId;
    private List<String> mNicknames;
    private List<Integer> mLevels;
    private List<List<String>> mMoves;
    public TeamPokemonResult(List<Integer> memberId, List<Integer> pokemonId, List<String> nicknames, List<Integer> levels,
                             List<List<String>> moves) {
        this.mPokemonId = pokemonId;
        this.mNicknames = nicknames;
        this.mLevels = levels;
        this.mMoves = moves;
        this.mMemberId = memberId;

    }
    @Override
    public List<List<String>> getListOfStringLists() {
        return mMoves;
    }

    @Override
    public String getType() {
        return QueryResult.TEAM_BY_ID;
    }

    @Override
    public List<Integer> getIntInfo() {
        return mPokemonId;
    }

    @Override
    public List<Integer> getMoreIntInfo() {
        return mLevels;
    }

    @Override
    public List<Integer> getMoreMoreIntInfo() {
        return mMemberId;
    }

    @Override
    public List<String> getStringInfo() {
        return mNicknames;
    }
}
