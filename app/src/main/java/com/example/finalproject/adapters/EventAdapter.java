package com.example.finalproject.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.finalproject.classes.CalendarUtils;
import com.example.finalproject.classes.Event;
import com.example.finalproject.R;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event>
{

    public EventAdapter(@NonNull Context context, List<Event> events) {
        super(context, 0,events);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Event event = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell,parent,false);
        }

        TextView tvEventCell = convertView.findViewById(R.id.tvEventCell);

        String eventTitle = event.getName() + " " + CalendarUtils.formattedTime(event.getTime());
        tvEventCell.setText(eventTitle);
        return convertView;
    }
}
