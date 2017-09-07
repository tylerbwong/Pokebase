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

package me.tylerbwong.pokebase.gui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import me.tylerbwong.pokebase.R;
import me.tylerbwong.pokebase.gui.holders.ItemListItemViewHolder;
import me.tylerbwong.pokebase.model.components.Item;

/**
 * @author Tyler Wong
 */
public class ItemListAdapter extends RecyclerView.Adapter {
    private Context context;
    private Item[] items;

    private static final String DRAWABLE = "drawable";

    public ItemListAdapter(Context context, Item[] items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_item, parent, false);
        return new ItemListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ItemListItemViewHolder holder = (ItemListItemViewHolder) viewHolder;
        Item curItem = items[position];
        holder.nameView.setText(curItem.getName());

        int imageResourceId = context.getResources().getIdentifier(curItem.getIdentifier(),
                DRAWABLE, context.getPackageName());

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.tm_normal);

        Glide.with(context)
                .load(imageResourceId)
                .apply(options)
                .into(holder.pokemonItemView);

        holder.item = curItem;
    }

    public void setItems(Item[] items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.length;
        }
        return 0;
    }
}
