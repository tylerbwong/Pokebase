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
import android.widget.Button;
import android.widget.TextView;

import com.db.chart.model.BarSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.BarChartView;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.BounceEase;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import java.util.Locale;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.tylerbwong.pokebase.R;
import me.tylerbwong.pokebase.gui.adapters.MoveListAdapter;
import me.tylerbwong.pokebase.gui.adapters.PokemonListAdapter;
import me.tylerbwong.pokebase.model.components.PokemonListItem;
import me.tylerbwong.pokebase.model.components.PokemonProfile;
import me.tylerbwong.pokebase.model.database.DatabaseOpenHelper;
import me.tylerbwong.pokebase.model.utilities.PokebaseCache;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Tyler Wong
 */
public class PokemonInfoView extends NestedScrollView {
   @BindView(R.id.type_one)
   TextView mTypeOneView;
   @BindView(R.id.type_two)
   TextView mTypeTwoView;
   @BindView(R.id.region)
   TextView mRegionView;
   @BindView(R.id.height)
   TextView mHeightView;
   @BindView(R.id.weight)
   TextView mWeightView;
   @BindView(R.id.exp)
   TextView mExpView;
   @BindView(R.id.description)
   TextView mDescription;
   @BindView(R.id.moves)
   Button mMovesButton;
   @BindView(R.id.evolutions)
   Button mEvolutionsButton;
   @BindView(R.id.chart)
   BarChartView mBarChart;
   @BindView(R.id.buttons)
   CardView mButtons;
   @BindViews({R.id.hp, R.id.attack, R.id.defense, R.id.special_attack, R.id.special_defense, R.id.speed})
   TextView[] mStats;

   private Context mContext;
   private PokemonProfile mProfile;
   private LovelyCustomDialog mMovesDialog;
   private LovelyCustomDialog mEvolutionsDialog;

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
      mContext = context;
      init();
   }

   public PokemonInfoView(Context context, AttributeSet attrs) {
      super(context, attrs);
      mContext = context;
      init();
   }

   public PokemonInfoView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      mContext = context;
      init();
   }

   private void init() {
      View view = inflate(mContext, R.layout.pokemon_info, this);
      ButterKnife.bind(this, view);
   }

   public void setButtonsVisible(boolean isVisible) {
      if (isVisible) {
         mButtons.setVisibility(VISIBLE);
      }
      else {
         mButtons.setVisibility(GONE);
      }
   }

   private void setupDialogs() {
      AnimatedRecyclerView movesList = new AnimatedRecyclerView(mContext);
      movesList.setLayoutManager(new LinearLayoutManager(mContext));
      movesList.setHasFixedSize(true);
      movesList.setAdapter(new MoveListAdapter(mContext, mProfile.getMoves()));

      mMovesDialog = new LovelyCustomDialog(mContext)
            .setTopColorRes(R.color.colorPrimary)
            .setTitle(R.string.moveset)
            .setIcon(R.drawable.ic_book_white_24dp)
            .setView(movesList)
            .setCancelable(true);

      PokemonListItem[] evolutions = mProfile.getEvolutions();

      AnimatedRecyclerView evolutionsList = new AnimatedRecyclerView(mContext);
      evolutionsList.setLayoutManager(new LinearLayoutManager(mContext));
      evolutionsList.setHasFixedSize(true);
      evolutionsList.setAdapter(new PokemonListAdapter(mContext, evolutions, true));

      mEvolutionsDialog = new LovelyCustomDialog(mContext)
            .setTopColorRes(R.color.colorPrimary)
            .setView(evolutionsList)
            .setIcon(R.drawable.ic_group_work_white_24dp)
            .setCancelable(true);

      if (evolutions.length == 0) {
         mEvolutionsDialog.setTitle(mContext.getString(R.string.no_evolutions));
         mEvolutionsDialog.setMessage(String.format(mContext.getString(R.string.no_evolutions_message),
               mProfile.getName()));
      }
      else {
         mEvolutionsDialog.setTitle(mContext.getString(R.string.evolutions));
      }
   }

   @OnClick(R.id.moves)
   public void onMoves() {
      if (mMovesDialog != null) {
         mMovesDialog.show();
      }
   }

   @OnClick(R.id.evolutions)
   public void onEvolutions() {
      if (mEvolutionsDialog != null) {
         mEvolutionsDialog.show();
      }
   }

   public void closeEvolutionsDialog() {
      if (mEvolutionsDialog != null) {
         mEvolutionsDialog.dismiss();
      }
   }

   public void loadPokemonInfo(int pokemonId) {
      PokebaseCache.getPokemonProfile(DatabaseOpenHelper.getInstance(mContext), pokemonId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<PokemonProfile>() {
               @Override
               public void onCompleted() {

               }

               @Override
               public void onError(Throwable e) {

               }

               @Override
               public void onNext(PokemonProfile pokemonProfile) {
                  mProfile = pokemonProfile;
                  setupDialogs();
                  loadTypes(mProfile.getTypes());
                  loadChart(mProfile.getBaseStats());
                  setHeightViewText(mProfile.getHeight());
                  setWeightViewText(mProfile.getWeight());
                  mDescription.setText(mProfile.getDescription());
                  mRegionView.setText(mProfile.getRegion());
                  mExpView.setText(String.valueOf(mProfile.getBaseExp()));
               }
            });
   }

   private void loadTypes(String[] types) {
      PaintDrawable backgroundColor;
      float dimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
            getResources().getDisplayMetrics());

      if (types.length == 1) {
         String type = types[0];
         String colorName = TYPE + type;
         int colorResId = getResources().getIdentifier(colorName, COLOR, mContext.getPackageName());

         mTypeTwoView.setVisibility(View.GONE);
         mTypeOneView.setText(type);
         backgroundColor = new PaintDrawable(ContextCompat.getColor(mContext, colorResId));
         backgroundColor.setCornerRadius(dimension);
         mTypeOneView.setBackground(backgroundColor);
      }
      else {
         String typeOne = types[0];
         String typeTwo = types[1];
         String colorName = TYPE + typeOne;
         int colorResId = getResources().getIdentifier(colorName, COLOR, mContext.getPackageName());

         mTypeOneView.setText(typeOne);
         backgroundColor = new PaintDrawable(ContextCompat.getColor(mContext, colorResId));
         backgroundColor.setCornerRadius(dimension);
         mTypeOneView.setBackground(backgroundColor);
         mTypeTwoView.setVisibility(View.VISIBLE);
         mTypeTwoView.setText(typeTwo);
         colorName = TYPE + typeTwo;
         colorResId = getResources().getIdentifier(colorName, COLOR, mContext.getPackageName());
         backgroundColor = new PaintDrawable(ContextCompat.getColor(mContext, colorResId));
         backgroundColor.setCornerRadius(dimension);
         mTypeTwoView.setBackground(backgroundColor);
      }
   }

   private void loadChart(float[] data) {
      BarSet dataSet = new BarSet();
      float tempVal;
      for (int index = 0; index < data.length; index++) {
         tempVal = data[index];
         dataSet.addBar(STATS[index], tempVal);
         mStats[index].setText(String.valueOf(Math.round(tempVal)));
      }

      dataSet.setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
      mBarChart.addData(dataSet);
      mBarChart.setXAxis(false);
      mBarChart.setYAxis(false);
      mBarChart.setYLabels(AxisController.LabelPosition.NONE);

      Animation animation = new Animation(1000);
      animation.setEasing(new BounceEase());
      mBarChart.show(animation);
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
      mHeightView.setText(heightText);
   }

   private void setWeightViewText(int hectograms) {
      double pounds = hectograms * LB_PER_HG;
      double kilograms = (double) hectograms / KG_PER_HG;
      String weightText = String.format(Locale.US, "%.1f", pounds) + " lbs (" +
            String.format(Locale.US, "%.1f", kilograms) + " kg)";
      mWeightView.setText(weightText);
   }
}
