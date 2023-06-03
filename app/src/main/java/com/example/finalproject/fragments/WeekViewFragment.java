package com.example.finalproject.fragments;

import static com.example.finalproject.classes.CalendarUtils.daysInWeekArray;
import static com.example.finalproject.classes.CalendarUtils.monthYearFromDate;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.finalproject.adapters.CalendarAdapter;
import com.example.finalproject.classes.CalendarUtils;
import com.example.finalproject.classes.Event;
import com.example.finalproject.adapters.EventAdapter;
import com.example.finalproject.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekViewFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private View view;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private Button backWeek , forwardWeek,btnEvent , btnDaily;
    private ListView eventListView;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_week_view, container, false);
        backWeek = view.findViewById(R.id.backWeek);
        forwardWeek = view.findViewById(R.id.forwardWeek);
        backWeek.setOnClickListener(this::previousWeek);
        forwardWeek.setOnClickListener(this::nextWeek);
        initWidgets();
        setWeekView();
        btnEvent = view.findViewById(R.id.btnEvent);
        btnEvent.setOnClickListener(this::newEvent);
        btnDaily = view.findViewById(R.id.btnDaily);
        btnDaily.setOnClickListener(this::Daily);

        return view;
    }
    private void initWidgets()
    {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
        eventListView = view.findViewById(R.id.eventListView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setWeekView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days,this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousWeek (View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextWeek (View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, LocalDate date)
    {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setEventAdapter();
    }

    private void setEventAdapter()
    {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getActivity().getApplicationContext(),dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }

    public void newEvent (View view)
    {
        Fragment secondFrag = new EventEditFragment();
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.frameLayout,secondFrag).commit();

    }

    public void Daily (View view)
    {
        Fragment secondFrag = new DailyCalendarFragment();
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.frameLayout,secondFrag).commit();

    }


}