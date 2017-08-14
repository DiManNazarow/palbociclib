package ru.mbg.palbociclib.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.mbg.palbociclib.R;


public class PagePickerView extends LinearLayout{

    @BindView(R.id.first_pick_button)
    protected TextView mFirstPickView;
    @BindView(R.id.second_pick_button)
    protected TextView mSecondPickView;

    private View mRootView;

    private boolean isFirstPick = true;

    private boolean isSecondPick = false;

    public interface OnPickerClickListener {
        void onFirstPickerClick();
        void onSecondPickerClick();
    }

    private OnPickerClickListener mOnPickerClickListener;

    public PagePickerView(Context context) {
        super(context);
        init();
    }

    public PagePickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PagePickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PagePickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.page_picker_view, this, true);
        ButterKnife.bind(this, mRootView);
    }

    private void pickFirst(){
        isFirstPick = true;
        isSecondPick = false;
        if (mOnPickerClickListener != null){
            mOnPickerClickListener.onFirstPickerClick();
        }
        mFirstPickView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        mSecondPickView.setTextColor(ContextCompat.getColor(getContext(), R.color.picker_color));
        mFirstPickView.setBackgroundResource(R.drawable.left_picker_button_fill);
        mSecondPickView.setBackgroundResource(R.drawable.right_pick_button);
    }

    private void pickSecond(){
        isFirstPick = false;
        isSecondPick = true;
        if (mOnPickerClickListener != null){
            mOnPickerClickListener.onSecondPickerClick();
        }
        mFirstPickView.setTextColor(ContextCompat.getColor(getContext(), R.color.picker_color));
        mSecondPickView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        mFirstPickView.setBackgroundResource(R.drawable.left_pick_button);
        mSecondPickView.setBackgroundResource(R.drawable.right_picker_button_fill);
    }

    @OnClick(R.id.first_pick_button)
    protected void onFirstPickerClick(){
        pickFirst();
    }

    @OnClick(R.id.second_pick_button)
    protected void onSecondPickerClick(){
        pickSecond();
    }

    public void setOnPickerClickListener(OnPickerClickListener onPickerClickListener) {
        mOnPickerClickListener = onPickerClickListener;
    }

    public void setFirstPickerText(String text){
        mFirstPickView.setText(text);
    }

    public void setFirstPickerText(@StringRes int stringId){
        mFirstPickView.setText(stringId);
    }

    public void setSecondPickerText(String text){
        mSecondPickView.setText(text);
    }

    public void setSecondPickerText(@StringRes int stringId){
        mSecondPickView.setText(stringId);
    }

    public boolean isFirstPick() {
        return isFirstPick;
    }

    public boolean isSecondPick() {
        return isSecondPick;
    }
}
