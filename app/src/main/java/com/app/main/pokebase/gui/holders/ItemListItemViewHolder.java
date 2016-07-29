package com.app.main.pokebase.gui.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.main.pokebase.R;
import com.app.main.pokebase.model.components.Item;
import com.app.main.pokebase.gui.views.ItemInfoView;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;

/**
 * @author Tyler Wong
 */
public class ItemListItemViewHolder extends RecyclerView.ViewHolder {
   public final View mView;
   public final TextView mNameView;
   public final ImageView mItemView;

   public Item mItem;

   private final static String DRAWABLE = "drawable";
   private final static String NONE = "0";
   private final static String NA = "N/A";

   public ItemListItemViewHolder(View itemView) {
      super(itemView);
      mView = itemView;
      mNameView = (TextView) itemView.findViewById(R.id.name);
      mItemView = (ImageView) itemView.findViewById(R.id.small_icon);

      mView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            showItemDialog();
         }
      });
   }

   private void showItemDialog() {
      Context context = mView.getContext();
      int imageResourceId = context.getResources().getIdentifier(mItem.getIdentifier(),
            DRAWABLE, context.getPackageName());

      String cost = String.valueOf(mItem.getCost());

      if (cost.equals(NONE)) {
         cost = NA;
      }

      ItemInfoView infoView = new ItemInfoView(context);
      infoView.setFields(cost, imageResourceId);

      new LovelyCustomDialog(mView.getContext())
            .setCancelable(true)
            .setTopColorRes(R.color.colorPrimary)
            .setIcon(R.drawable.ic_business_center_white_24dp)
            .setTitle(mItem.getName())
            .setView(infoView)
            .show();
   }
}
