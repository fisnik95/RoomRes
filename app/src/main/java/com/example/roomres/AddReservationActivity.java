package com.example.roomres;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddReservationActivity extends AppCompatActivity {
    private static final String BASE_URI = "http://anbo-roomreservationv3.azurewebsites.net/api/Reservations";


    /**
     fromTime  long
     toTime  long
     userId  string
     purpose  string
     roomId  int
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);
    }

    public void addReservation2(View view){
        String fromTimeString = ((EditText) findViewById(R.id.add_reservation_fromTime)).getText().toString();
        int fromTime = Integer.parseInt(fromTimeString);

        String toTimeString = ((EditText) findViewById(R.id.add_reservation_toTime)).getText().toString();
        int toTime = Integer.parseInt(toTimeString);

        String userId = ((EditText) findViewById(R.id.add_reservation_userId)).getText().toString();

        String purpose = ((EditText) findViewById(R.id.add_reservation_purpose)).getText().toString();

        String roomIdString = ((EditText) findViewById(R.id.add_reservation_roomId)).getText().toString();
        int roomId = Integer.parseInt(roomIdString);


        TextView messageView = findViewById(R.id.add_reservation_message_textview);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fromTime", fromTime);
            jsonObject.put("toTime", toTime);
            jsonObject.put("userId", userId);
            jsonObject.put("purpose", purpose);
            jsonObject.put("roomId", roomId);

            String jsonDocument =  jsonObject.toString();
            messageView.setText(jsonDocument);

            PostBookOkHttpTask task = new PostBookOkHttpTask();
            task.execute(BASE_URI, jsonDocument);
        } catch (JSONException ex) {
            messageView.setText(ex.getMessage());
        }
    }

    private class PostBookOkHttpTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            // https://trinitytuts.com/get-and-post-request-using-okhttp-in-android-application/
            String url = strings[0];
            String postdata = strings[1];
            MediaType MEDIA_TYPE = MediaType.parse("application/json");
            // https://stackoverflow.com/questions/57100451/okhttp3-requestbody-createcontenttype-content-deprecated
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
            TextView messageView = findViewById(R.id.add_reservation_message_textview);
            messageView.setText(jsonString);
            Log.d("MINE", jsonString);
            //  finish();
        }

        @Override
        protected void onCancelled(String message) {
            super.onCancelled(message);
            TextView messageView = findViewById(R.id.add_reservation_message_textview);
            messageView.setText(message);
            Log.d("MINE", message);
            //finish();
        }

    }
}
