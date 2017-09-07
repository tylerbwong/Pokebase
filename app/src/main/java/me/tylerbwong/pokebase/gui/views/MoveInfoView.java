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
import android.graphics.drawable.PaintDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.tylerbwong.pokebase.R;

/**
 * @author Tyler Wong
 */
public class MoveInfoView extends RelativeLayout {
    @BindView(R.id.type_label)
    TextView typeLabel;
    @BindView(R.id.power_label)
    TextView powerLabel;
    @BindView(R.id.pp_label)
    TextView ppLabel;
    @BindView(R.id.accuracy_label)
    TextView accuracyLabel;
    @BindView(R.id.class_label)
    TextView classLabel;
    @BindView(R.id.description)
    TextView description;

    private Context context;

    private static final String CLASS = "class";
    private static final String TYPE = "type";
    private static final String COLOR = "color";

    public MoveInfoView(Context context) {
        super(context, null);
        this.context = context;
        init();
    }

    public MoveInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public MoveInfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {
        View view = inflate(context, R.layout.move_info, this);
        ButterKnife.bind(this, view);
    }

    public void setFields(String type, String power, String pp, String accuracy, String className, String description) {
        PaintDrawable backgroundColor;
        float dimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
                getResources().getDisplayMetrics());

        this.typeLabel.setText(type);
        this.powerLabel.setText(power);
        this.ppLabel.setText(pp);
        this.accuracyLabel.setText(accuracy);
        classLabel.setText(className);
        this.description.setText(description);

        String classColor = CLASS + className;
        int colorResId = getResources().getIdentifier(classColor, COLOR, context.getPackageName());
        backgroundColor = new PaintDrawable(ContextCompat.getColor(context, colorResId));
        backgroundColor.setCornerRadius(dimension);
        classLabel.setBackground(backgroundColor);

        String typeColor = TYPE + type;
        colorResId = getResources().getIdentifier(typeColor, COLOR, context.getPackageName());
        backgroundColor = new PaintDrawable(ContextCompat.getColor(context, colorResId));
        backgroundColor.setCornerRadius(dimension);
        this.typeLabel.setBackground(backgroundColor);
    }
}
