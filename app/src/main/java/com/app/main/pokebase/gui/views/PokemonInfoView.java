package com.app.main.pokebase.gui.views;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.app.main.pokebase.R;
import com.app.main.pokebase.model.components.PokemonProfile;
import com.app.main.pokebase.model.database.DatabaseOpenHelper;
import com.app.main.pokebase.model.utilities.PokebaseCache;
import com.db.chart.model.BarSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.BarChartView;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.BounceEase;

import java.util.Locale;

/**
 * @author Tyler Wong
 */
public class PokemonInfoView extends NestedScrollView {
   private Context mContext;
   private TextView mTypeOneView;
   private TextView mTypeTwoView;
   private TextView mRegionView;
   private TextView mHeightView;
   private TextView mWeightView;
   private TextView mExpView;
   private TextView[] mStats;
   private TextView mDescription;
   private BarChartView mBarChart;

   private final static double FT_PER_DM = 0.32808399;
   private final static double LB_PER_HG = 0.22046226218;
   private final static int KG_PER_HG = 10;
   private final static int IN_PER_FT = 12;
   private final static int DM_PER_M = 10;
   private final static String TYPE = "type";
   private final static String COLOR = "color";
   private final static String[] STATS =
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

      mTypeOneView = (TextView) view.findViewById(R.id.type_one);
      mTypeTwoView = (TextView) view.findViewById(R.id.type_two);
      mRegionView = (TextView) view.findViewById(R.id.region);
      mHeightView = (TextView) view.findViewById(R.id.height);
      mWeightView = (TextView) view.findViewById(R.id.weight);
      mExpView = (TextView) view.findViewById(R.id.exp);
      mBarChart = (BarChartView) view.findViewById(R.id.chart);
      mDescription = (TextView) view.findViewById(R.id.description);

      mStats = new TextView[STATS.length];
      mStats[0] = (TextView) view.findViewById(R.id.hp);
      mStats[1] = (TextView) view.findViewById(R.id.attack);
      mStats[2] = (TextView) view.findViewById(R.id.defense);
      mStats[3] = (TextView) view.findViewById(R.id.special_attack);
      mStats[4] = (TextView) view.findViewById(R.id.special_defense);
      mStats[5] = (TextView) view.findViewById(R.id.speed);

      view.findViewById(R.id.buttons).setVisibility(GONE);
   }

   public void loadPokemonInfo(int pokemonId) {
      new LoadPokemonInfo(mContext).execute(pokemonId);
   }

   private void loadTypes(String[] types) {
      if (types.length == 1) {
         String type = types[0];
         String colorName = TYPE + type;
         int colorResId = getResources().getIdentifier(colorName, COLOR, mContext.getPackageName());

         mTypeTwoView.setVisibility(View.GONE);
         mTypeOneView.setText(type);
         mTypeOneView.setBackgroundColor(ContextCompat.getColor(mContext, colorResId));
      }
      else {
         String typeOne = types[0];
         String typeTwo = types[1];
         String colorName = TYPE + typeOne;
         int colorResId = getResources().getIdentifier(colorName, COLOR, mContext.getPackageName());

         mTypeOneView.setText(typeOne);
         mTypeOneView.setBackgroundColor(ContextCompat.getColor(mContext, colorResId));
         mTypeTwoView.setVisibility(View.VISIBLE);
         mTypeTwoView.setText(typeTwo);
         colorName = TYPE + typeTwo;
         colorResId = getResources().getIdentifier(colorName, COLOR, mContext.getPackageName());
         mTypeTwoView.setBackgroundColor(ContextCompat.getColor(mContext, colorResId));
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

   private class LoadPokemonInfo extends AsyncTask<Integer, Void, PokemonProfile> {
      private Context mContext;

      public LoadPokemonInfo(Context context) {
         this.mContext = context;
      }

      @Override
      protected PokemonProfile doInBackground(Integer... params) {
         return PokebaseCache.getPokemonProfile(DatabaseOpenHelper.getInstance(mContext), params[0]);
      }

      @Override
      protected void onPostExecute(PokemonProfile loaded) {
         super.onPostExecute(loaded);

         loadTypes(loaded.getTypes());
         loadChart(loaded.getBaseStats());
         setHeightViewText(loaded.getHeight());
         setWeightViewText(loaded.getWeight());
         mDescription.setText(loaded.getDescription());
         mRegionView.setText(loaded.getRegion());
         mExpView.setText(String.valueOf(loaded.getBaseExp()));
      }
   }
}
