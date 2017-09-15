package ru.mbg.palbociclib.new_version.calendar.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ru.mbg.palbociclib.R;

public class ColumnNameViewHolder extends RecyclerView.ViewHolder {

    public ColumnNameViewHolder(Context context, ViewGroup viewGroup) {
        super(LayoutInflater.from(context).inflate(R.layout.layout_column_name_view_holder, viewGroup, false));
    }

}
