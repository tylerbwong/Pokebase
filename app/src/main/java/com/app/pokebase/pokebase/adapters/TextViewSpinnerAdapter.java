package com.app.pokebase.pokebase.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

/**
 * @author Brittany Berlanga
 */
public class TextViewSpinnerAdapter implements SpinnerAdapter {
   private static final int TEXT_SIZE = 16;
   private static final int PADDING = 20;
   private static final int PADDING_LEFT = 30;
   private Context mContext;
   private String[] mItems;
   private int mTextSize;

   public TextViewSpinnerAdapter(Context context, String[] items) {
      super();
      mContext = context;
      mItems = items;
      mTextSize = TEXT_SIZE;
   }

   public TextViewSpinnerAdapter(Context context, String[] items, int textSize) {
      super();
      mContext = context;
      mItems = items;
      mTextSize = textSize;
   }

   @Override
   public View getDropDownView(int position, View convertView, ViewGroup parent) {
      TextView textView = new TextView(mContext);
      textView.setTextSize(mTextSize);
      textView.setPadding(PADDING_LEFT, PADDING, PADDING, PADDING);
      ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      textView.setLayoutParams(lp);
      textView.setText(mItems[position]);
      return textView;
   }

   @Override
   public boolean isEmpty() {
      return mItems.length == 0;
   }

   @Override
   public void registerDataSetObserver(DataSetObserver observer) {
   }

   @Override
   public void unregisterDataSetObserver(DataSetObserver observer) {
   }

   @Override
   public int getCount() {
      return mItems.length;
   }

   @Override
   public Object getItem(int position) {
      return mItems[position];
   }

   @Override
   public long getItemId(int position) {
      return position;
   }

   @Override
   public boolean hasStableIds() {
      return false;
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
      TextView textView = new TextView(mContext);
      textView.setTextSize(mTextSize);
      textView.setPadding(PADDING_LEFT, PADDING, PADDING, PADDING);
      ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      textView.setLayoutParams(lp);
      textView.setText(mItems[position]);
      return textView;
   }

   @Override
   public int getItemViewType(int position) {
      return 1;
   }

   @Override
   public int getViewTypeCount() {
      return 1;
   }
}
