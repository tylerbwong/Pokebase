package com.example.tylerbwong.pokebase.backend;

import java.util.List;

/**
 * Created by brittanyberlanga on 6/10/16.
 */
public class TypeRegionResult extends QueryResult {
    private List<String> mTypes;
    private List<String> mRegions;

    public TypeRegionResult(List<String> types, List<String> regions) {
        this.mTypes = types;
        this.mRegions = regions;
    }
    @Override
    public List<String> getStringInfo() {
        return mTypes;
    }

    @Override
    public List<String> getMoreStringInfo() {
        return mRegions;
    }

    @Override
    public String getType() {
        return QueryResult.ALL_TYPES_REGIONS;
    }
}
