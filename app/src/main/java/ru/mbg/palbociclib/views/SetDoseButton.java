package ru.mbg.palbociclib.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.Button;

import ru.mbg.palbociclib.R;

public class SetDoseButton extends android.support.v7.widget.AppCompatButton {

    private int mColorRes;

    public SetDoseButton(Context context) {
        super(context);
    }

    public SetDoseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SetDoseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setEnabled(boolean enable){
        super.setEnabled(enable);
        if (enable){
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else {
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.disable_color));
        }
    }
}
