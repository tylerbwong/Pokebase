package com.example.tylerbwong.pokebase.backend;

import java.util.List;

/**
 * Created by brittanyberlanga on 6/9/16.
 */
public class PokemonListResult extends QueryResult {
    public final List<Integer> mIds;
    public final List<String> mNames;
    private String mType;
    public PokemonListResult(String type, List<Integer> ids, List<String> names) {
        this.mIds = ids;
        this.mNames = names;
        this.mType = type;
    }

    @Override
    public List<String> getStringInfo() {
        return mNames;
    }

    @Override
    public List<Integer> getIntInfo() {
        return mIds;
    }

    @Override
    public String getType() {
        return mType;
    }
}
