package com.example.tylerbwong.pokebase.backend;

import java.util.List;

/**
 * Created by brittanyberlanga on 6/9/16.
 */
public class PokemonInfoResult extends QueryResult {
    private List<String> mMoves;
    private List<Integer> mValues; //id, height, weight, baseExp
    private List<String> mOtherValues; //name, region, type1, (type2)
    public PokemonInfoResult(List<Integer> values, List<String> otherValues, List<String> moves) {
        this.mMoves = moves;
        this.mValues = values;
        this.mOtherValues = otherValues;
    }
    @Override
    public List<String> getStringInfo() {
        return mOtherValues;
    }

    @Override
    public List<String> getMoreStringInfo() {
        return mMoves;
    }

    @Override
    public List<Integer> getIntInfo() {
        return mValues;
    }

    @Override
    public String getType() {
        return QueryResult.SELECTED_POKEMON;
    }
}
