package com.example.roomres;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roomres.MODELS.Reservation;
import com.example.roomres.MODELS.Room;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ReservationRoomActivity extends AppCompatActivity {
    public static final String URI = "http://anbo-roomreservationv3.azurewebsites.net/api/reservations/room/";
    public static final String ROOM = "ROOM";
    private Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationroom);

        Intent intent = getIntent();
        room = (Room) intent.getSerializableExtra(ROOM);

        TextView roomLabelTxtView = findViewById(R.id.roomName);
        roomLabelTxtView.setText(room.toString());
        getDataUsingOkHttpEnqueue();
    }

    private void getDataUsingOkHttpEnqueue() {
        OkHttpClient client = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(URI + room.getId());
        Request request = requestBuilder.build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String jsonString = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            populateList(jsonString);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView messageView = findViewById(R.id.main_message_textview);
                            messageView.setText(URI + "\n" + response.code() + " " + response.message());
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException ex) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView messageView = findViewById(R.id.main_message_textview);
                        messageView.setText(ex.getMessage());
                    }
                });
            }
        });
    }

    private void populateList(String jsonString) {
        Gson gson = new GsonBuilder().create();
        Log.d("ROA", jsonString);
        final Reservation[] reservations = gson.fromJson(jsonString, Reservation[].class);
        ListView listView = findViewById(R.id.listViewRoomRes);
        ArrayAdapter<Reservation> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reservations);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ReservationRoomActivity.this, "Reservation clicked: " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}






























}
