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

package me.tylerbwong.pokebase.gui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import me.tylerbwong.pokebase.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Tyler Wong
 */
public class ItemInfoView extends RelativeLayout {
    @BindView(R.id.cost)
    TextView cost;
    @BindView(R.id.item)
    ImageView item;
    @BindView(R.id.description)
    TextView description;

    private Context context;

    public ItemInfoView(Context context) {
        super(context, null);
        this.context = context;
        init();
    }

    public ItemInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ItemInfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {
        View view = inflate(context, R.layout.item_info, this);
        ButterKnife.bind(this, view);
    }

    public void setFields(String cost, int identifier, String description) {
        this.cost.setText(cost);
        item.setImageResource(identifier);
        this.description.setText(description);

        if (item.getDrawable() == null) {
            Glide.with(context)
                    .load(R.drawable.tm_normal)
                    .into(item);
        }
    }
}
