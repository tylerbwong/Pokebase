package com.app.pokebase.pokebase.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * @author Brittany Berlanga
 */
public final class TextViewAdapter implements ListAdapter {
   private static int TEXT_SIZE = 16;
   private Context mContext;
   private String[] mItems;

   public TextViewAdapter(Context context, String[] items) {
      super();
      this.mContext = context;
      this.mItems = items;
   }

   @Override
   public boolean isEnabled(int position) {
      return false;
   }

   @Override
   public boolean areAllItemsEnabled() {
      return false;
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
      textView.setTextSize(TEXT_SIZE);
      textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
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
