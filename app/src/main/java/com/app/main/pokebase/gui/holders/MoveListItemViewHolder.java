package com.app.main.pokebase.gui.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.app.main.pokebase.R;
import com.app.main.pokebase.gui.views.MoveInfoView;
import com.app.main.pokebase.model.components.Move;
import com.app.main.pokebase.model.database.DatabaseOpenHelper;
import com.app.main.pokebase.model.utilities.PokebaseCache;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Tyler Wong
 */
public class MoveListItemViewHolder extends RecyclerView.ViewHolder {
   @BindView(R.id.name)
   public TextView mNameView;

   public final View mView;

   private Move mMove;
   private String mType;
   private DatabaseOpenHelper mDatabaseHelper;

   private final static String NA = "N/A";
   private final static String NONE = "0";
   private final static String TYPE = "type";
   private final static String COLOR = "color";

   public MoveListItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      this.mView = itemView;
      mDatabaseHelper = DatabaseOpenHelper.getInstance(mView.getContext());

      mView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            mMove = PokebaseCache.getMove(mDatabaseHelper, mNameView.getText().toString());

            if (mMove.getTypeName() == null) {
               mType = mDatabaseHelper.queryTypeById(mMove.getTypeId());
               mMove.setTypeName(mType);
            }
            else {
               mType = mMove.getTypeName();
            }

            showMoveInfoDialog();
         }
      });
   }

   private void showMoveInfoDialog() {
      String colorName = TYPE + mType;
      Context context = mView.getContext();
      int colorResId = context.getResources().getIdentifier(colorName, COLOR, context.getPackageName());
      String type = mType;
      String power = String.valueOf(mMove.getPower());
      String pp = String.valueOf(mMove.getPp());
      String accuracy = String.valueOf(mMove.getAccuracy());
      String className = mMove.getClassName();
      String description;

      if (mMove.getDescription() == null) {
         description = mDatabaseHelper.queryMoveDescriptionById(mMove.getMoveId());
         mMove.setDescription(description);
      }
      else {
         description = mMove.getDescription();
      }

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
