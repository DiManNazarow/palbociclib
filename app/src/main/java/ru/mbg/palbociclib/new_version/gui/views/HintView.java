package ru.mbg.palbociclib.new_version.gui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.utils.GuiUtils;

/**
 * Created by Дмитрий on 25.12.2017.
 */

public class HintView extends RelativeLayout {

    private final int DIVIDER_MIN_OPEN_HEIGHT = 40;

    @BindView(R.id.hint_text_view)
    protected TextView mHintTextView;
    @BindView(R.id.open_button)
    protected ImageButton mOpenButton;
    @BindView(R.id.content_view)
    protected LinearLayout mContentView;
    @BindView(R.id.divider)
    protected View mDivider;

    public HintView(Context context) {
        super(context);
        init();
    }

    public HintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.layout_hint_view, this, true);
        ButterKnife.bind(this, this);
    }

    @OnClick(R.id.open_button)
    public void onOpenButtonClick(){
        if (mContentView.getVisibility() == GONE){
            mContentView.setVisibility(VISIBLE);
            mHintTextView.setVisibility(GONE);
            mOpenButton.setImageResource(R.drawable.ic_chevron_up_grey600_24dp);
            mContentView.post(new Runnable() {
                @Override
                public void run() {
                    int height = HintView.this.getHeight();
                    int margin = (int)GuiUtils.convertDpToPixel(8, getContext()) * 2;
                    ViewGroup.LayoutParams params = (LayoutParams) mDivider.getLayoutParams();
                    params.height = height - margin;
                    mDivider.setLayoutParams(params);
                }
            });
        } else if (mContentView.getVisibility() == VISIBLE){
            mContentView.setVisibility(GONE);
            ViewGroup.LayoutParams params = (LayoutParams) mDivider.getLayoutParams();
            params.height = (int)GuiUtils.convertDpToPixel(DIVIDER_MIN_OPEN_HEIGHT, getContext());
            mDivider.setLayoutParams(params);
            mHintTextView.setVisibility(VISIBLE);
            mOpenButton.setImageResource(R.drawable.ic_chevron_down_grey600_24dp);
        }
    }

}
