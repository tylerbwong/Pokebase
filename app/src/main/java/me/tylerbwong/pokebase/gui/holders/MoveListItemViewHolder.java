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

package me.tylerbwong.pokebase.gui.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.tylerbwong.pokebase.R;
import me.tylerbwong.pokebase.gui.views.MoveInfoView;
import me.tylerbwong.pokebase.model.components.Move;
import me.tylerbwong.pokebase.model.database.DatabaseOpenHelper;
import me.tylerbwong.pokebase.model.utilities.PokebaseCache;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

   private static final String NA = "N/A";
   private static final String NONE = "0";
   private static final String TYPE = "type";
   private static final String COLOR = "color";

   public MoveListItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      this.mView = itemView;
      mDatabaseHelper = DatabaseOpenHelper.getInstance(mView.getContext());

      mView.setOnClickListener(view ->
            PokebaseCache.getMove(mDatabaseHelper, mNameView.getText().toString())
                  .subscribeOn(Schedulers.newThread())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Subscriber<Move>() {
                     @Override
                     public void onCompleted() {

                     }

                     @Override
                     public void onError(Throwable e) {

                     }

                     @Override
                     public void onNext(Move move) {
                        mMove = move;

                        if (mMove.getTypeName() == null) {
                           mType = mDatabaseHelper.queryTypeById(mMove.getTypeId());
                           mMove.setTypeName(mType);
                        }
                        else {
                           mType = mMove.getTypeName();
                        }

                        showMoveInfoDialog();
                     }
                  })
      );
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
