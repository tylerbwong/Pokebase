/*
 * Copyright 2016 Tyler Wong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.tylerbwong.pokebase.gui.views;

import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.db.chart.animation.Animation;
import com.db.chart.model.BarSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.view.BarChartView;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import java.util.Locale;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tylerbwong.pokebase.R;
import me.tylerbwong.pokebase.gui.adapters.MoveListAdapter;
import me.tylerbwong.pokebase.gui.adapters.PokemonListAdapter;
import me.tylerbwong.pokebase.model.components.PokemonListItem;
import me.tylerbwong.pokebase.model.components.PokemonProfile;
import me.tylerbwong.pokebase.model.database.DatabaseOpenHelper;
import me.tylerbwong.pokebase.model.utilities.PokebaseCache;

/**
 * @author Tyler Wong
 */
@SuppressWarnings("unused")
public class PokemonInfoView extends NestedScrollView {
    @BindView(R.id.type_one)
    TextView typeOneView;
    @BindView(R.id.type_two)
    TextView typeTwoView;
    @BindView(R.id.region)
    TextView regionView;
    @BindView(R.id.height)
    TextView heightView;
    @BindView(R.id.weight)
    TextView weightView;
    @BindView(R.id.exp)
    TextView expView;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.chart)
    BarChartView barChart;
    @BindView(R.id.buttons)
    CardView buttons;
    @BindViews({R.id.hp, R.id.attack, R.id.defense, R.id.special_attack, R.id.special_defense, R.id.speed})
    TextView[] stats;

    private Context context;
    private PokemonProfile profile;
    private LovelyCustomDialog movesDialog;
    private LovelyCustomDialog evolutionsDialog;

    private static final double FT_PER_DM = 0.32808399;
    private static final double LB_PER_HG = 0.22046226218;
    private static final int KG_PER_HG = 10;
    private static final int IN_PER_FT = 12;
    private static final int DM_PER_M = 10;
    private static final String TYPE = "type";
    private static final String COLOR = "color";
    private static final String[] STATS =
            {"HP", "Attack", "Defense", "Sp. Atk", "Sp. Def", "Speed"};

    public PokemonInfoView(Context context) {
        super(context, null);
        this.context = context;
        init();
    }

    public PokemonInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public PokemonInfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {
        View view = inflate(context, R.layout.pokemon_info, this);
        ButterKnife.bind(this, view);
    }

    public void setButtonsVisible(boolean isVisible) {
        if (isVisible) {
            buttons.setVisibility(VISIBLE);
        }
        else {
            buttons.setVisibility(GONE);
        }
    }

    private void setupDialogs() {
        AnimatedRecyclerView movesList = new AnimatedRecyclerView(context);
        movesList.setLayoutManager(new LinearLayoutManager(context));
        movesList.setHasFixedSize(true);
        movesList.setAdapter(new MoveListAdapter(context, profile.getMoves()));

        movesDialog = new LovelyCustomDialog(context)
                .setTopColorRes(R.color.colorPrimary)
                .setTitle(R.string.moveset)
                .setIcon(R.drawable.ic_book_white_24dp)
                .setView(movesList)
                .setCancelable(true);

        PokemonListItem[] evolutions = profile.getEvolutions();

        AnimatedRecyclerView evolutionsList = new AnimatedRecyclerView(context);
        evolutionsList.setLayoutManager(new LinearLayoutManager(context));
        evolutionsList.setHasFixedSize(true);
        evolutionsList.setAdapter(new PokemonListAdapter(context, evolutions, true));

        evolutionsDialog = new LovelyCustomDialog(context)
                .setTopColorRes(R.color.colorPrimary)
                .setView(evolutionsList)
                .setIcon(R.drawable.ic_group_work_white_24dp)
                .setCancelable(true);

        if (evolutions.length == 0) {
            evolutionsDialog.setTitle(context.getString(R.string.no_evolutions));
            evolutionsDialog.setMessage(String.format(context.getString(R.string.no_evolutions_message),
                    profile.getName()));
        }
        else {
            evolutionsDialog.setTitle(context.getString(R.string.evolutions));
        }
    }

    @OnClick(R.id.moves)
    public void onMoves() {
        if (movesDialog != null) {
            movesDialog.show();
        }
    }

    @OnClick(R.id.evolutions)
    public void onEvolutions() {
        if (evolutionsDialog != null) {
            evolutionsDialog.show();
        }
    }

    public void closeEvolutionsDialog() {
        if (evolutionsDialog != null) {
            evolutionsDialog.dismiss();
        }
    }

    public void loadPokemonInfo(int pokemonId) {
        PokebaseCache.getPokemonProfile(DatabaseOpenHelper.getInstance(context), pokemonId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pokemonProfile -> {
                    profile = pokemonProfile;
                    setupDialogs();
                    loadTypes(profile.getTypes());
                    loadChart(profile.getBaseStats());
                    setHeightViewText(profile.getHeight());
                    setWeightViewText(profile.getWeight());
                    description.setText(profile.getDescription());
                    regionView.setText(profile.getRegion());
                    expView.setText(String.valueOf(profile.getBaseExp()));
                });
    }

    private void loadTypes(String[] types) {
        PaintDrawable backgroundColor;
        float dimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
                getResources().getDisplayMetrics());

        if (types.length == 1) {
            String type = types[0];
            String colorName = TYPE + type;
            int colorResId = getResources().getIdentifier(colorName, COLOR, context.getPackageName());

            typeTwoView.setVisibility(View.GONE);
            typeOneView.setText(type);
            backgroundColor = new PaintDrawable(ContextCompat.getColor(context, colorResId));
            backgroundColor.setCornerRadius(dimension);
            typeOneView.setBackground(backgroundColor);
        }
        else {
            String typeOne = types[0];
            String typeTwo = types[1];
            String colorName = TYPE + typeOne;
            int colorResId = getResources().getIdentifier(colorName, COLOR, context.getPackageName());

            typeOneView.setText(typeOne);
            backgroundColor = new PaintDrawable(ContextCompat.getColor(context, colorResId));
            backgroundColor.setCornerRadius(dimension);
            typeOneView.setBackground(backgroundColor);
            typeTwoView.setVisibility(View.VISIBLE);
            typeTwoView.setText(typeTwo);
            colorName = TYPE + typeTwo;
            colorResId = getResources().getIdentifier(colorName, COLOR, context.getPackageName());
            backgroundColor = new PaintDrawable(ContextCompat.getColor(context, colorResId));
            backgroundColor.setCornerRadius(dimension);
            typeTwoView.setBackground(backgroundColor);
        }
    }

    private void loadChart(float[] data) {
        BarSet dataSet = new BarSet();
        float tempVal;
        for (int index = 0; index < data.length; index++) {
            tempVal = data[index];
            dataSet.addBar(STATS[index], tempVal);
            stats[index].setText(String.valueOf(Math.round(tempVal)));
        }

        dataSet.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        barChart.addData(dataSet);
        barChart.setXAxis(false);
        barChart.setYAxis(false);
        barChart.setYLabels(AxisRenderer.LabelPosition.NONE);

        Animation animation = new Animation(1000);
        animation.setInterpolator(new BounceInterpolator());
        barChart.show(animation);
    }

    private void setHeightViewText(int decimeters) {
        int feet = (int) Math.floor(decimeters * FT_PER_DM);
        int inches = (int) Math.round((decimeters * FT_PER_DM - feet) * IN_PER_FT);
        if (inches == IN_PER_FT) {
            feet++;
            inches = 0;
        }
        double millimeters = (double) decimeters / DM_PER_M;
        String heightText = feet + "' " + inches + "'' ("
                + String.format(Locale.US, "%.2f", millimeters) + " m)";
        heightView.setText(heightText);
    }

    private void setWeightViewText(int hectograms) {
        double pounds = hectograms * LB_PER_HG;
        double kilograms = (double) hectograms / KG_PER_HG;
        String weightText = String.format(Locale.US, "%.1f", pounds) + " lbs (" +
                String.format(Locale.US, "%.1f", kilograms) + " kg)";
        weightView.setText(weightText);
    }
}
