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
import android.widget.ImageView;
import android.widget.TextView;

import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.tylerbwong.pokebase.R;
import me.tylerbwong.pokebase.gui.views.ItemInfoView;
import me.tylerbwong.pokebase.model.components.Item;
import me.tylerbwong.pokebase.model.database.DatabaseOpenHelper;

/**
 * @author Tyler Wong
 */
public class ItemListItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.name)
    public TextView nameView;
    @BindView(R.id.small_icon)
    public ImageView pokemonItemView;

    public final View view;

    public Item item;

    private static final String DRAWABLE = "drawable";
    private static final String NONE = "0";
    private static final String NA = "N/A";

    public ItemListItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.view = itemView;

        view.setOnClickListener(view -> showItemDialog());
    }

    private void showItemDialog() {
        Context context = view.getContext();
        DatabaseOpenHelper databaseHelper = DatabaseOpenHelper.getInstance(context);

        int imageResourceId = context.getResources().getIdentifier(item.getIdentifier(),
                DRAWABLE, context.getPackageName());

        String cost = String.valueOf(item.getCost());
        String description;

        if (item.getDescription() == null) {
            description = databaseHelper.queryItemDescription(item.getId());
            item.setDescription(description);
        }
        else {
            description = item.getDescription();
        }

        if (cost.equals(NONE)) {
            cost = NA;
        }

        ItemInfoView infoView = new ItemInfoView(context);
        infoView.setFields(cost, imageResourceId, description);

        new LovelyCustomDialog(view.getContext())
                .setCancelable(true)
                .setTopColorRes(R.color.colorPrimary)
                .setIcon(R.drawable.ic_business_center_white_24dp)
                .setTitle(item.getName())
                .setView(infoView)
                .show();
    }
}
