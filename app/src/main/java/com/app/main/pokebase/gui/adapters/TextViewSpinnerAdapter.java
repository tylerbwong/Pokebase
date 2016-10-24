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

package com.app.main.pokebase.gui.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

/**
 * @author Tyler Wong
 */
public class TextViewSpinnerAdapter implements SpinnerAdapter {
   private Context mContext;
   private String[] mItems;
   private int mTextSize;

   private static final int TEXT_SIZE = 16;
   private static final int PADDING = 20;
   private static final int PADDING_LEFT = 30;

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
