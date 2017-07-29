package ru.mbg.palbociclib.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.mbg.palbociclib.R;

/**
 * Created by Дмитрий on 29.07.2017.
 */

public class ItemView extends LinearLayout {

    @BindView(R.id.item_name_text_view)
    protected TextView mTitleTextView;
    @BindView(R.id.item_next_button)
    protected ImageButton mNextButton;

    private View mRootView;

    private interface OnNextButtonClickListener {
        void onNextButtonClick();
    }

    private OnNextButtonClickListener mNextButtonClickListener;

    public ItemView(Context context) {
        super(context);
        init();
    }

    public ItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.layout_item_view, this, true);
        ButterKnife.bind(this, mRootView);
    }

    public void setTitleTextViewText(String text){
        mTitleTextView.setText(text);
    }

    public void setTitleTextViewText(@StringRes int stringRes){
        mTitleTextView.setText(stringRes);
    }

    public void setNextButtonClickListener(OnNextButtonClickListener nextButtonClickListener) {
        mNextButtonClickListener = nextButtonClickListener;
    }

    @OnClick(R.id.item_next_button)
    public void onNextButtonClick(){
        if (mNextButtonClickListener != null){
            mNextButtonClickListener.onNextButtonClick();
        }
    }
}
