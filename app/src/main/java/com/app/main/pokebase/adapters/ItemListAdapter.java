package com.app.main.pokebase.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.main.pokebase.R;
import com.app.main.pokebase.holders.ItemListItemViewHolder;
import com.app.main.pokebase.components.Item;

/**
 * @author Tyler Wong
 */
public class ItemListAdapter extends RecyclerView.Adapter {
   private Context mContext;
   private Item[] mItems;

   public ItemListAdapter(Context context, Item[] items) {
      this.mContext = context;
      this.mItems = items;
   }

   @Override
   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_item, parent, false);
      return new ItemListItemViewHolder(view);
   }

   @Override
   public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
      ItemListItemViewHolder holder = (ItemListItemViewHolder) viewHolder;
      Item curItem = mItems[position];
      holder.mNameView.setText(curItem.getName());
      int imageResourceId = mContext.getResources().getIdentifier(curItem.getIdentifier(),
            "drawable", mContext.getPackageName());
      holder.mItemView.setImageResource(imageResourceId);

      if (holder.mItemView.getDrawable() == null) {
         holder.mItemView.setImageResource(R.drawable.tm_normal);
      }
      holder.mItem = curItem;
   }

   @Override
   public int getItemCount() {
      return mItems.length;
   }
}
