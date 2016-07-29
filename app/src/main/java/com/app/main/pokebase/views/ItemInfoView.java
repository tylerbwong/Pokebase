package com.app.main.pokebase.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.main.pokebase.R;

/**
 * @author Tyler Wong
 */
public class ItemInfoView extends RelativeLayout {

   private Context mContext;

   private TextView mCost;
   private ImageView mItem;

   public ItemInfoView(Context context) {
      super(context, null);
      mContext = context;
      init();
   }

   public ItemInfoView(Context context, AttributeSet attrs) {
      super(context, attrs);
      mContext = context;
      init();
   }

   public ItemInfoView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      mContext = context;
      init();
   }

   private void init() {
      View view = inflate(mContext, R.layout.item_info, this);

      mCost = (TextView) view.findViewById(R.id.cost);
      mItem = (ImageView) view.findViewById(R.id.item);
   }

   public void setFields(String cost, int identifier) {
      mCost.setText(cost);
      mItem.setImageResource(identifier);

      if (mItem.getDrawable() == null) {
         mItem.setImageResource(R.drawable.tm_normal);
      }
   }
}
