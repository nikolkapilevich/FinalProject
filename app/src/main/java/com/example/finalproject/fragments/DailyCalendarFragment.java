package com.example.finalproject.fragments;

import static com.example.finalproject.classes.CalendarUtils.selectedDate;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.finalproject.classes.CalendarUtils;
import com.example.finalproject.classes.Event;
import com.example.finalproject.adapters.HourAdapter;
import com.example.finalproject.classes.HourEvent;
import com.example.finalproject.R;

import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class DailyCalendarFragment extends Fragment {
    public View view;
    private TextView monthDayText , tvDayOfWeek;
    private ListView hourListView;
    private Button backDay,forwardDay, btnEvent;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_daily_calendar, container, false);
        initWidgets();

        backDay = view.findViewById(R.id.backDay);
        forwardDay = view.findViewById(R.id.forwardDay);
        backDay.setOnClickListener(this::previousDay);
        forwardDay.setOnClickListener(this::nextDay);
        btnEvent = view.findViewById(R.id.btnEvent);
        btnEvent.setOnClickListener(this::newEventAction);

        return view;
    }

    private void initWidgets()
    {
        monthDayText = view.findViewById(R.id.monthDayText);
        tvDayOfWeek = view.findViewById(R.id.tvDayOfWeek);
        hourListView = view.findViewById(R.id.hourListView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        setDayView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setDayView()
    {
        monthDayText.setText(CalendarUtils.monthDayFromDate(selectedDate));
        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        tvDayOfWeek.setText(dayOfWeek);
        setHourAdapter();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setHourAdapter()
    {
        HourAdapter hourAdapter = new HourAdapter(getActivity().getApplicationContext(), hourEventList());
        hourListView.setAdapter(hourAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<HourEvent> hourEventList()
    {
        ArrayList<HourEvent> list = new ArrayList();
        for (int hour=0; hour<24; hour++)
        {
            LocalTime time = LocalTime.of(hour,0);
            ArrayList<Event> events = Event.eventsForDateAndTime(selectedDate,time);
            HourEvent hourEvent = new HourEvent(time,events);
            list.add(hourEvent);

        }
        return list;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousDay (View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusDays(1);
        setDayView();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextDay (View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusDays(1);
        setDayView();

    }

    public void newEventAction (View view)
    {
        Fragment secondFrag = new EventEditFragment();
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.frameLayout,secondFrag).commit();
    }
}