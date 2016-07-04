package com.app.pokebase.pokebase.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pokebase.pokebase.R;
import com.app.pokebase.pokebase.holders.MoveListItemViewHolder;

/**
 * @author Tyler Wong
 */
public class MoveListAdapter extends RecyclerView.Adapter implements View.OnClickListener {
   private Context mContext;
   private String[] mItems;

   public MoveListAdapter(Context context, String[] items) {
      this.mContext = context;
      this.mItems = items;
   }

   @Override
   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_move, parent, false);
      return new MoveListItemViewHolder(view);
   }

   @Override
   public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
      MoveListItemViewHolder holder = (MoveListItemViewHolder) viewHolder;
      holder.mNameView.setText(mItems[position]);
   }

   @Override
   public int getItemCount() {
      return mItems.length;
   }

   @Override
   public void onClick(View v) {

   }
}
