package ru.mbg.palbociclib.new_version.calendar.viewholders;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mbg.palbociclib.R;


public class MonthNameViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.month_text_view)
    protected TextView mMonthNameTextView;

    public MonthNameViewHolder(Context context, ViewGroup viewGroup) {
        super(LayoutInflater.from(context).inflate(R.layout.layout_month_name_view_holder, viewGroup, false));
        ButterKnife.bind(this, itemView);
    }

    public void setMonthName(String name){
        mMonthNameTextView.setText(name);
    }

    public void setMonthName(@StringRes int stringRes){
        mMonthNameTextView.setText(stringRes);
    }
}
