package com.app.main.pokebase.gui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.main.pokebase.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Tyler Wong
 */
public class ItemInfoView extends RelativeLayout {
   @BindView(R.id.cost)
   TextView mCost;
   @BindView(R.id.item)
   ImageView mItem;
   @BindView(R.id.description)
   TextView mDescription;

   private Context mContext;

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
      ButterKnife.bind(this, view);
   }

   public void setFields(String cost, int identifier, String description) {
      mCost.setText(cost);
      mItem.setImageResource(identifier);
      mDescription.setText(description);

      if (mItem.getDrawable() == null) {
         mItem.setImageResource(R.drawable.tm_normal);
      }
   }
}
