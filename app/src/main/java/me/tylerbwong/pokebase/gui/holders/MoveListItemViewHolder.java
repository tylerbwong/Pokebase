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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tylerbwong.pokebase.R;
import me.tylerbwong.pokebase.gui.views.MoveInfoView;
import me.tylerbwong.pokebase.model.components.Move;
import me.tylerbwong.pokebase.model.database.DatabaseOpenHelper;
import me.tylerbwong.pokebase.model.utilities.PokebaseCache;

/**
 * @author Tyler Wong
 */
public class MoveListItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.name)
    public TextView nameView;

    public final View view;

    private Move move;
    private String type;
    private DatabaseOpenHelper databaseHelper;

    private static final String NA = "N/A";
    private static final String NONE = "0";
    private static final String TYPE = "type";
    private static final String COLOR = "color";

    public MoveListItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.view = itemView;
        databaseHelper = DatabaseOpenHelper.getInstance(view.getContext());

        view.setOnClickListener(view ->
                PokebaseCache.getMove(databaseHelper, nameView.getText().toString())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(move -> {
                            this.move = move;

                            if (this.move.getTypeName() == null) {
                                type = databaseHelper.queryTypeById(this.move.getTypeId());
                                this.move.setTypeName(type);
                            }
                            else {
                                type = this.move.getTypeName();
                            }

                            showMoveInfoDialog();
                        })
        );
    }

    private void showMoveInfoDialog() {
        String colorName = TYPE + type;
        Context context = view.getContext();
        int colorResId = context.getResources().getIdentifier(colorName, COLOR, context.getPackageName());
        String type = this.type;
        String power = String.valueOf(move.getPower());
        String pp = String.valueOf(move.getPp());
        String accuracy = String.valueOf(move.getAccuracy());
        String className = move.getClassName();
        String description;

        if (move.getDescription() == null) {
            description = databaseHelper.queryMoveDescriptionById(move.getMoveId());
            move.setDescription(description);
        }
        else {
            description = move.getDescription();
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

        new LovelyCustomDialog(view.getContext())
                .setTopColorRes(colorResId)
                .setIcon(R.drawable.ic_book_white_24dp)
                .setView(infoView)
                .setTitle(move.getName())
                .setCancelable(true)
                .show();
    }
}
