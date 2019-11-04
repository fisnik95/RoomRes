package com.example.roomres;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ReservationListItemAdapter extends ArrayAdapter<Reservation> {
    private final int resource;

    public ReservationListItemAdapter(Context context, int resource, Reservation[] objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    public ReservationListItemAdapter(Context context, int resource, List<Reservation> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    /**
     fromTime  int
     toTime  int
     userId  string
     purpose  string
     roomId  int
     */

    @NonNull
    @Override
    public View getView(int position,View convertView, @NonNull ViewGroup parent) {
        Reservation reservation = getItem(position);
        int fromTime = reservation.getFromTime();
        int toTime = reservation.getToTime();

        String userId = reservation.getUserId();
        String purpose = reservation.getPurpose();

        int roomId = reservation.getRoomId();

        LinearLayout reservationView;
        if (convertView == null){
            reservationView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(inflater);
            li.inflate(resource, reservationView, true);
        } else {
            reservationView = (LinearLayout) convertView;
        }

        TextView fromTimeView = reservationView.findViewById(R.id.reservationlist_item_fromTime);
        TextView toTimeView = reservationView.findViewById(R.id.reservationlist_item_toTime);
//        TextView userIdView = reservationView.findViewById(R.id.reservationlist_item_userId);
//        TextView purposeView = reservationView.findViewById(R.id.reservationlist_item_purpose);
        TextView roomIdView = reservationView.findViewById(R.id.reservationlist_item_roomId);

        fromTimeView.setText(fromTime);
        toTimeView.setText(toTime);
//        userIdView.setText(userId);
//        purposeView.setText(purpose);
        roomIdView.setText(roomId);

        return reservationView;
    }
}
