package com.example.finalproject;

import static com.example.finalproject.CalendarUtils.daysInMonthArray;
import static com.example.finalproject.CalendarUtils.daysInWeekArray;
import static com.example.finalproject.CalendarUtils.monthYearFromDate;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekViewFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private View view;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private Button backWeek , forwardWeek;


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

        return view;
    }
    private void initWidgets()
    {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
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
    public void onItemClick(int position, String dayText)
    {
            String message = "Selected Date" + dayText + " " + monthYearFromDate(CalendarUtils.selectedDate);


    }

    public void newEvent (View view)
    {

    }


}