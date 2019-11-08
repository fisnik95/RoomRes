package com.example.roomres;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddReservationActivity extends AppCompatActivity {
    private Calendar meetingStart = Calendar.getInstance();
    private Button dateButton;
    private Button timeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);

        dateButton = findViewById(R.id.)
    }

    public void addReservation(View view) {
       timeButton =  findViewById(R.id.add_reservation_fromtime);
        timeButton =  findViewById(R.id.add_reservation_totime);
        Log.d("APP", toTimeButton);
        Log.d("APP", fromTimeButton);
       String purpose = ((EditText) findViewById(R.id.add_reservation_purpose)).getText().toString();
        TextView messageView = findViewById(R.id.add_reservation_message);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fromTime", fromTimeButton);
            jsonObject.put("toTime", toTimeButton);
            jsonObject.put("purpose", purpose);
            String jsonDocument = jsonObject.toString();
            messageView.setText(jsonDocument);
            PostBookOkHttpTask task = new PostBookOkHttpTask();
            task.execute("http://anbo-roomreservationv3.azurewebsites.net/api/Reservations", jsonDocument);
       } catch (JSONException ex) {
           messageView.setText(ex.getMessage());
        }

    }

    private class PostBookOkHttpTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String postdata = strings[1];
            MediaType MEDIA_TYPE = MediaType.parse("application/json");
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(postdata, MEDIA_TYPE);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    String message = url + "\n" + response.code() + " " + response.message();
                    return message;
                }
            } catch (IOException ex) {
                Log.e("BOOKS", ex.getMessage());
                return ex.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);
            TextView messageView = findViewById(R.id.add_reservation_message);
            messageView.setText(jsonString);
            Log.d("MINE", jsonString);
            //  finish();
        }

        @Override
        protected void onCancelled(String message) {
            super.onCancelled(message);
            TextView messageView = findViewById(R.id.add_reservation_message);
            messageView.setText(message);
            Log.d("MINE", message);
            //finish();
        }
    }

    public void timePickButtonClicked(View view) {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                meetingStart.set(Calendar.HOUR_OF_DAY, hourOfDay);
                meetingStart.set(Calendar.MINUTE, minute);
                DateFormat df = DateFormat.getTimeInstance();
                String timeString = df.format(meetingStart.getTimeInMillis());
                timeButton.setText(timeString);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int currentHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(this, timeSetListener, currentHourOfDay,
                currentMinute, true);
        dialog.show();
    }
}
