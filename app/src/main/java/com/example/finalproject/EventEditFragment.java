package com.example.finalproject;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.finalproject.CalendarUtils;
import com.example.finalproject.Event;
import com.example.finalproject.R;

import java.time.LocalTime;


public class EventEditFragment extends Fragment {

    private View view;
    private EditText etEventName;
    private TextView tvEventDate , tvEventTime;
    private LocalTime time;
    private Button btnSave;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_edit, container, false);
        initWidgets();
        time = LocalTime.now();
        tvEventDate.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        tvEventTime.setText("Time: " + CalendarUtils.formattedTime(time));
        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this::saveEvent);

        return view;
    }

    private void initWidgets()
    {
        etEventName = view.findViewById(R.id.etEventName);
        tvEventDate = view.findViewById(R.id.tvEventDate);
        tvEventTime = view.findViewById(R.id.tvEventTime);
    }

    public void saveEvent (View view)
    {
        String eventName = etEventName.getText().toString();
        Event newEvent = new Event(eventName,CalendarUtils.selectedDate,time);
        Event.eventsList.add(newEvent);
        // finish();

    }
}