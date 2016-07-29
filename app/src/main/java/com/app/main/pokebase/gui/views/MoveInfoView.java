package com.app.main.pokebase.gui.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.main.pokebase.R;

/**
 * @author Tyler Wong
 */
public class MoveInfoView extends RelativeLayout {
   private Context mContext;

   private TextView mType;
   private TextView mPower;
   private TextView mPp;
   private TextView mAccuracy;
   private TextView mClass;
   private TextView mDescription;

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

      mType = (TextView) view.findViewById(R.id.type_label);
      mPower = (TextView) view.findViewById(R.id.power_label);
      mPp = (TextView) view.findViewById(R.id.pp_label);
      mAccuracy = (TextView) view.findViewById(R.id.accuracy_label);
      mClass = (TextView) view.findViewById(R.id.class_label);
      mDescription = (TextView) view.findViewById(R.id.description);
   }

   public void setFields(String type, String power, String pp, String accuracy, String className, String description) {
      mType.setText(type);
      mPower.setText(power);
      mPp.setText(pp);
      mAccuracy.setText(accuracy);
      mClass.setText(className);
      mDescription.setText(description);

      String classColor = CLASS + className;
      int colorResId = getResources().getIdentifier(classColor, COLOR, mContext.getPackageName());
      mClass.setBackgroundColor(ContextCompat.getColor(mContext, colorResId));

      String typeColor = TYPE + type;
      colorResId = getResources().getIdentifier(typeColor, COLOR, mContext.getPackageName());
      mType.setBackgroundColor(ContextCompat.getColor(mContext, colorResId));
   }
}
