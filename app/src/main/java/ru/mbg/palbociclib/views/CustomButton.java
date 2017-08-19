package ru.mbg.palbociclib.views;

import android.content.Context;
import android.util.AttributeSet;

import ru.mbg.palbociclib.R;


public class CustomButton extends android.support.v7.widget.AppCompatButton {

    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setEnabled(boolean state){
        super.setEnabled(state);
        if (state){
            setBackgroundResource(R.drawable.round_button);
        } else {
            setBackgroundResource(R.drawable.round_button_disable);
        }
    }
}
