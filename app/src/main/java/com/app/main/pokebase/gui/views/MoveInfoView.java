package com.app.main.pokebase.gui.views;

import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.main.pokebase.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Tyler Wong
 */
public class MoveInfoView extends RelativeLayout {
   @BindView(R.id.type_label)
   TextView mType;
   @BindView(R.id.power_label)
   TextView mPower;
   @BindView(R.id.pp_label)
   TextView mPp;
   @BindView(R.id.accuracy_label)
   TextView mAccuracy;
   @BindView(R.id.class_label)
   TextView mClass;
   @BindView(R.id.description)
   TextView mDescription;

   private Context mContext;

   private final static String CLASS = "class";
   private final static String TYPE = "type";
   private final static String COLOR = "color";

   public MoveInfoView(Context context) {
      super(context, null);
      mContext = context;
      init();
   }

   public MoveInfoView(Context context, AttributeSet attrs) {
      super(context, attrs);
      mContext = context;
      init();
   }

   public MoveInfoView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      mContext = context;
      init();
   }

   private void init() {
      View view = inflate(mContext, R.layout.move_info, this);
      ButterKnife.bind(this, view);
   }

   public void setFields(String type, String power, String pp, String accuracy, String className, String description) {
      PaintDrawable backgroundColor;
      float dimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
            getResources().getDisplayMetrics());

      mType.setText(type);
      mPower.setText(power);
      mPp.setText(pp);
      mAccuracy.setText(accuracy);
      mClass.setText(className);
      mDescription.setText(description);

      String classColor = CLASS + className;
      int colorResId = getResources().getIdentifier(classColor, COLOR, mContext.getPackageName());
      backgroundColor = new PaintDrawable(ContextCompat.getColor(mContext, colorResId));
      backgroundColor.setCornerRadius(dimension);
      mClass.setBackground(backgroundColor);

      String typeColor = TYPE + type;
      colorResId = getResources().getIdentifier(typeColor, COLOR, mContext.getPackageName());
      backgroundColor = new PaintDrawable(ContextCompat.getColor(mContext, colorResId));
      backgroundColor.setCornerRadius(dimension);
      mType.setBackground(backgroundColor);
   }
}
