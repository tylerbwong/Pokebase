package com.app.main.pokebase.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.app.main.pokebase.R;

/**
 * @author Tyler Wong
 */
public class MoveListItemViewHolder extends RecyclerView.ViewHolder {
   public final View mView;
   public final TextView mNameView;

   public MoveListItemViewHolder(View itemView) {
      super(itemView);
      mView = itemView;
      mNameView = (TextView) itemView.findViewById(R.id.name);
   }
}
