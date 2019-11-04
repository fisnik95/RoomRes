package com.example.roomres;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReservationActivity extends AppCompatActivity {
    private static final String BASE_URI = "http://anbo-roomreservationv3.azurewebsites.net/api/Reservations/";
    public static final String RESERVATION = "RESERVATION";
    private Reservation reservation;

    /**
     fromTime  int
     toTime  int
     userId  string
     purpose  string
     roomId  int
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        Intent intent = getIntent();
        reservation = (Reservation) intent.getSerializableExtra(RESERVATION);

        TextView headingView = findViewById(R.id.reservation_heading_textview);
        headingView.setText("Reservation Id" + reservation.getId());

        EditText fromTimeView = findViewById(R.id.reservation_fromTime_edittext);
        fromTimeView.setText(reservation.getFromTime());

        EditText toTimeView = findViewById(R.id.reservation_toTime_edittext);
        toTimeView.setText(reservation.getToTime());

        EditText userId = findViewById(R.id.reservation_userId_edittext);
        userId.setText(reservation.getUserId());

        EditText purpose = findViewById(R.id.reservation_purpose_edittext);
        purpose.setText(reservation.getPurpose());

        EditText roomId = findViewById(R.id.reservation_roomId_edittext);
        roomId.setText(reservation.getRoomId());
    }

    public void deleteReservation(View view) {
        final String url = "http://anbo-roomreservationv3.azurewebsites.net/api/Reservations/" + reservation.getId();
        //DeleteTask task = new DeleteTask();
        //task.execute(url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).delete().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException ex) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView messageView = findViewById(R.id.add_reservation_message_textview);
                        messageView.setText(ex.getMessage());

                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView messageView = findViewById(R.id.add_reservation_message_textview);
                        if (response.isSuccessful()) {
                            messageView.setText("Book deleted");
                            finish();
                        } else {
                            messageView.setText(url + "\n" + response.code() + " " + response.message());
                        }
                    }
                });
            }
        });
    }

    public void updateReservation(View view) {
        // code missing: Left as an exercise
        Toast.makeText(this, "Update: Code missing. Left as an exercise", Toast.LENGTH_LONG).show();
    }


    public void back(View view) {
        finish();
    }

    private class DeleteTask extends AsyncTask<String, Void, CharSequence> {
        @Override
        protected CharSequence doInBackground(String... urls) {
            String urlString = urls[0];
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("DELETE");
                int responseCode = connection.getResponseCode();
                if (responseCode % 100 != 2) {
                    throw new IOException("Response code: " + responseCode);
                }
                return "Nothing";
            } catch (MalformedURLException e) {
                return e.getMessage() + " " + urlString;
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onCancelled(CharSequence charSequence) {
            super.onCancelled(charSequence);
            TextView messageView = findViewById(R.id.add_reservation_message_textview);
            messageView.setText(charSequence);
        }
    }

}
