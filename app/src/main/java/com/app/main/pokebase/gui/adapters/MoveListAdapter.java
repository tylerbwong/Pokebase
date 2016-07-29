package com.app.main.pokebase.gui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.main.pokebase.R;
import com.app.main.pokebase.gui.holders.MoveListItemViewHolder;

/**
 * @author Tyler Wong
 */
public class MoveListAdapter extends RecyclerView.Adapter {
   private Context mContext;
   private String[] mMoves;

   public MoveListAdapter(Context context, String[] items) {
      this.mContext = context;
      this.mMoves = items;
   }

   @Override
   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_move, parent, false);
      return new MoveListItemViewHolder(view);
   }

   @Override
   public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
      MoveListItemViewHolder holder = (MoveListItemViewHolder) viewHolder;
      holder.mNameView.setText(mMoves[position]);
   }

   @Override
   public int getItemCount() {
      return mMoves.length;
   }
}
