package com.app.main.pokebase.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.app.main.pokebase.R;
import com.app.main.pokebase.components.Move;
import com.app.main.pokebase.database.DatabaseOpenHelper;
import com.app.main.pokebase.views.MoveInfoView;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;

/**
 * @author Tyler Wong
 */
public class MoveListItemViewHolder extends RecyclerView.ViewHolder {
   public final View mView;
   public final TextView mNameView;

   private Move mMove;
   private String mType;
   private DatabaseOpenHelper mDatabaseHelper;

   private final static String NA = "N/A";
   private final static String NONE = "0";

   public MoveListItemViewHolder(View itemView) {
      super(itemView);
      mView = itemView;
      mNameView = (TextView) itemView.findViewById(R.id.name);
      mDatabaseHelper = DatabaseOpenHelper.getInstance(mView.getContext());

      mView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            mMove = mDatabaseHelper.queryMoveInfoByName(mNameView.getText().toString());
            mType = mDatabaseHelper.queryTypeById(mMove.getTypeId());
            showMoveInfoDialog();
         }
      });
   }

   private void showMoveInfoDialog() {
      String colorName = "type" + mType;
      Context context = mView.getContext();
      int colorResId = context.getResources().getIdentifier(colorName, "color", context.getPackageName());
      String type = mType;
      String power = String.valueOf(mMove.getPower());
      String pp = String.valueOf(mMove.getPp());
      String accuracy = String.valueOf(mMove.getAccuracy());
      String className = mMove.getClassName();
      String description = mDatabaseHelper.queryMoveDescriptionById(mMove.getMoveId());

      if (power.equals(NONE)) {
         power = NA;
      }
      if (pp.equals(NONE)) {
         pp = NA;
      }
      if (accuracy.equals(NONE)) {
         accuracy = NA;
      }

      MoveInfoView infoView = new MoveInfoView(context);
      infoView.setFields(type, power, pp, accuracy, className, description);

      new LovelyCustomDialog(mView.getContext())
            .setTopColorRes(colorResId)
            .setIcon(R.drawable.ic_book_white_24dp)
            .setView(infoView)
            .setTitle(mMove.getName())
            .setCancelable(true)
            .show();
   }
}
