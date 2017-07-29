package ru.mbg.palbociclib.activities;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarCellView;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DayViewAdapter;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderAdapter;
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration;
import ru.mbg.palbociclib.Constants;
import ru.mbg.palbociclib.EventsDictionary;
import ru.mbg.palbociclib.PatientProvider;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.Utils;
import ru.mbg.palbociclib.helpers.DateHelper;


public class CalendarFragment extends Fragment implements CalendarCellDecorator, DayViewAdapter, CalendarPickerView.DateSelectableFilter, CalendarPickerView.OnDateSelectedListener {
    private PatientListAdapter adapter;
    private StickyHeaderDecoration header;
    private MenuItem listMenuItem;
    private MenuItem calendarMenuItem;
    private boolean isDisplayAsList;
    private Date selectedDate;
    private Date currentMonth;

    private PatientProvider provider;
    private EventsDictionary events;

    public CalendarFragment() {
        isDisplayAsList = true;
        provider = new PatientProvider();
    }

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        adapter = new PatientListAdapter(Collections.<PatientProvider.EventRow>emptyList(), getActivity());
        header = new StickyHeaderDecoration(adapter, true);

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(header);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Обновить события
        if (isDisplayAsList) {
            events = provider.getEventsFor(null);
        } else {
            events = provider.getEventsFor(DateHelper.instance.currentDate());
        }
        adapter.setData(events.getAllEvents());
        getActivity().setTitle(R.string.title_calendar);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        listMenuItem = menu.add(0, 0, 0, "Список").setIcon(R.drawable.cal2list);
        listMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        calendarMenuItem = menu.add(0, 0, 0, "Календарь").setIcon(R.drawable.list2cal);
        calendarMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (isDisplayAsList) {
            listMenuItem.setVisible(false);
            calendarMenuItem.setVisible(true);
        } else {
            listMenuItem.setVisible(true);
            calendarMenuItem.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        isDisplayAsList = !isDisplayAsList;
        if (isDisplayAsList) {
            events = provider.getEventsFor(null);
            adapter.setData(events.getAllEvents());
        } else {
            currentMonth = DateHelper.instance.currentDate();
            events = provider.getEventsFor(currentMonth);
            selectedDate = events.getFirstDate();
            adapter.setData(events.getEventsFor(selectedDate));
            adapter.notifyDataSetChanged();
        }
        header.clearHeaderCache();
        getActivity().invalidateOptionsMenu();
        return true;
    }

    class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.ItemViewHolder> implements
            StickyHeaderAdapter<PatientListAdapter.HeaderHolder> {

        private final LayoutInflater inflater;

        class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView statusTextView;
            ImageView statusImageView;
            View statusView;
            CalendarPickerView calendarView;

            ItemViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
                statusTextView = (TextView) itemView.findViewById(R.id.status);
                statusImageView = (ImageView) itemView.findViewById(R.id.status_image);
                statusView = itemView.findViewById(R.id.status_color);
                calendarView = (CalendarPickerView) itemView.findViewById(R.id.calendar);
            }
        }

        class HeaderHolder extends RecyclerView.ViewHolder {
            TextView date;
            TextView day;

            HeaderHolder(View itemView) {
                super(itemView);
                date = (TextView) itemView.findViewById(R.id.date);
                day = (TextView) itemView.findViewById(R.id.day);
            }
        }

        private List<PatientProvider.EventRow> data;

        PatientListAdapter(List<PatientProvider.EventRow> items, Context context) {
            super();
            data = items;
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<PatientProvider.EventRow> items) {
            if (items == null) {
                data = Collections.emptyList();
            } else {
                data = items;
            }
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            if (!isDisplayAsList && position == 0) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            if (viewType == 0) {
                v = inflater.inflate(R.layout.calendar_list_calendar, parent, false);
            } else {
                v = inflater.inflate(R.layout.calendar_list_item, parent, false);
            }
            return new ItemViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            if (!isDisplayAsList) {
                if (position == 0) {
                    // У нас календарь
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(currentMonth);
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    Date startDate = cal.getTime();
                    cal.add(Calendar.MONTH, 1);
                    Date endDate = cal.getTime();

                    holder.calendarView.setDateSelectableFilter(CalendarFragment.this);
                    holder.calendarView.setOnInvalidDateSelectedListener(null);
                    holder.calendarView.setOnDateSelectedListener(CalendarFragment.this);
                    holder.calendarView.init(startDate, endDate);
                    if (selectedDate != null) {
                        holder.calendarView.selectDate(selectedDate);
                    }
                    holder.calendarView.setCustomDayView(CalendarFragment.this);
                    holder.calendarView.setDecorators(Collections.<CalendarCellDecorator>singletonList(CalendarFragment.this));
                    return;
                }
                position -= 1;
            }

            PatientProvider.EventRow item = data.get(position);
            holder.title.setText(item.name);
            holder.statusTextView.setText(item.description());
            holder.statusImageView.setImageResource(item.state.getImageResource());
            if (item.state == PatientProvider.State.nextTreatment) {
                holder.statusImageView.setColorFilter(ContextCompat.getColor(getContext(), item.getImageColor()), PorterDuff.Mode.SRC_ATOP);
            } else {
                holder.statusImageView.clearColorFilter();
            }
            holder.statusView.setBackgroundColor(ContextCompat.getColor(getContext(), item.state.getCardColor()));
        }

        @Override
        public int getItemCount() {
            if (!isDisplayAsList) {
                return data.size() + 1;
            } else {
                return data.size();
            }
        }

        @Override
        public long getHeaderId(int position) {
            if (!isDisplayAsList) {
                if (position == 0) {
                    return 0;
                }
                position -= 1;
            }

            return data.get(position).date.getTime();
        }

        @Override
        public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
            final View view = inflater.inflate(R.layout.calendar_list_header, parent, false);
            return new HeaderHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(HeaderHolder holder, int position) {
            if (!isDisplayAsList) {
                if (position == 0) {
                    // Перед нами календарь
                    return;
                }
                position -= 1;
            }

            Calendar cal = Calendar.getInstance();
            final Date date = data.get(position).date;
            cal.setTime(date);
            holder.date.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
            SimpleDateFormat sdf = new SimpleDateFormat("EEEEE");
            String weekDay = sdf.format(date);
            weekDay = weekDay.substring(0, 1).toUpperCase() + weekDay.substring(1);
            holder.day.setText(weekDay);
        }

    }

    @Override
    public void decorate(CalendarCellView cellView, Date date) {
        if (DateHelper.instance.isToday(date)) {
            cellView.getDayOfMonthTextView().setTypeface(null, Typeface.BOLD);
        } else {
            cellView.getDayOfMonthTextView().setTypeface(null, Typeface.NORMAL);
        }

        ImageView dot1 = (ImageView) cellView.findViewById(R.id.dot1);
        ImageView dot2 = (ImageView) cellView.findViewById(R.id.dot2);
        ImageView dot3 = (ImageView) cellView.findViewById(R.id.dot3);

        if (!cellView.isCurrentMonth()) {
            dot1.setVisibility(View.GONE);
            dot2.setVisibility(View.GONE);
            dot3.setVisibility(View.GONE);
            return;
        }

        List<PatientProvider.EventRow> currentEvents = events.getEventsFor(date);
        if (currentEvents != null) {
            int size = currentEvents.size();
            if (size > 0) {
                dot1.setVisibility(View.VISIBLE);
                dot1.setColorFilter(ContextCompat.getColor(getContext(), currentEvents.get(0).state.getCardColor()), PorterDuff.Mode.SRC_ATOP);
            } else {
                dot1.setVisibility(View.GONE);
            }
            if (size > 1) {
                dot2.setVisibility(View.VISIBLE);
                dot2.setColorFilter(ContextCompat.getColor(getContext(), currentEvents.get(1).state.getCardColor()), PorterDuff.Mode.SRC_ATOP);
            } else {
                dot2.setVisibility(View.GONE);
            }
            if (size > 2) {
                dot3.setVisibility(View.VISIBLE);
                dot3.setColorFilter(ContextCompat.getColor(getContext(), currentEvents.get(2).state.getCardColor()), PorterDuff.Mode.SRC_ATOP);
            } else {
                dot3.setVisibility(View.GONE);
            }
        } else {
            dot1.setVisibility(View.GONE);
            dot2.setVisibility(View.GONE);
            dot3.setVisibility(View.GONE);
        }
    }

    @Override
    public void makeCellView(CalendarCellView parent) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_view_events, null);
        parent.addView(layout);
        parent.setDayOfMonthTextView((TextView) layout.findViewById(R.id.day_view));
    }

    @Override
    public boolean isDateSelectable(Date date) {
        List<PatientProvider.EventRow> currentEvents = events.getEventsFor(date);
        return currentEvents != null && currentEvents.size() > 0;
    }

    @Override
    public void onDateSelected(Date date) {
        selectedDate = date;
        adapter.setData(events.getEventsFor(date));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDateUnselected(Date date) {}

    public void selectPrevMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentMonth);
        cal.add(Calendar.MONTH, -1);
        currentMonth = cal.getTime();
        events = provider.getEventsFor(currentMonth);
        selectedDate = events.getFirstDate();
        adapter.setData(events.getEventsFor(selectedDate));
        adapter.notifyDataSetChanged();
    }

    public void selectNextMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentMonth);
        cal.add(Calendar.MONTH, 1);
        currentMonth = cal.getTime();
        events = provider.getEventsFor(currentMonth);
        selectedDate = events.getFirstDate();
        adapter.setData(events.getEventsFor(selectedDate));
        adapter.notifyDataSetChanged();
    }
}
