package com.example.roomres;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URI = "http://anbo-roomreservationv3.azurewebsites.net/api/Reservations";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView listHeader = new TextView(this);
        listHeader.setText("Reservation");
        TextViewCompat.setTextAppearance(listHeader, android.R.style.TextAppearance_Large);

        ListView listView = findViewById(R.id.main_reservation_listview);
        listView.addHeaderView(listHeader);
    }

    public void addReservation(View veiw){
        Intent intent = new Intent(this, AddReservationActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ReadTask task = new ReadTask();
        task.execute(BASE_URI);

        //getDataUsingOkHttpEnqueue();
    }

    private void getDataUsingOkHttpEnqueue() {
        OkHttpClient client = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(BASE_URI);
        Request request = requestBuilder.build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() { // Alternative to AsyncTask
            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String jsonString = response.body().string();
                    runOnUiThread(new Runnable() {
                        // https://stackoverflow.com/questions/33418232/okhttp-update-ui-from-enqueue-callback
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
                            messageView.setText(BASE_URI + "\n" + response.code() + " " + response.message());
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


    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String uri = strings[0];
            OkHttpClient client = new OkHttpClient();
            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(uri);
            Request request = requestBuilder.build();
            Call call = client.newCall(request);
            try {
                Response response = call.execute();
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    String jsonString = responseBody.string();
                    return jsonString;
                } else {
                    cancel(true);
                    return uri + "\n" + response.code() + " " + response.message();
                }
            } catch (IOException ex) {
                cancel(true);
                return ex.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String jsonString) {
/*            final List<Book> books = new ArrayList<>();
            try {
                JSONArray array = new JSONArray(jsonString.toString());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String author = obj.getString("Author");
                    double price = obj.getDouble("Price");
                    String title = obj.getString("Title");
                    String publisher = obj.getString("Publisher");
                    int id = obj.getInt("Id");
                    Book book = new Book(id, author, title, publisher, price);
                    books.add(book);
                }
*/
            ProgressBar progressBar = findViewById(R.id.mainProgressbar);
            progressBar.setVisibility(View.GONE);
            populateList(jsonString);
           /* } catch (JSONException ex) {
                messageTextView.setText(ex.getMessage());
                Log.e("BOOKS", ex.getMessage());
            }*/
        }

        @Override
        protected void onCancelled(String message) {
            ProgressBar progressBar = findViewById(R.id.mainProgressbar);
            progressBar.setVisibility(View.GONE);
            TextView messageTextView = findViewById(R.id.main_message_textview);
            messageTextView.setText(message);
            Log.e("RESERVATION", message);
        }

    }

        private void populateList(String jsonString) {
            Gson gson = new GsonBuilder().create();
            final Reservation[] reservations = gson.fromJson(jsonString, Reservation[].class);
            ListView listView = findViewById(R.id.main_reservation_listview);
            //ArrayAdapter<Book> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, books);
            ReservationListItemAdapter adapter = new ReservationListItemAdapter(getBaseContext(), R.layout.reservationlist_item, reservations);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getBaseContext(), ReservationActivity.class);
                    // Book book = books.get((int) id);
                    // Book book = books[(int) id];
                    Reservation reservation = (Reservation) parent.getItemAtPosition(position);
                    intent.putExtra(ReservationActivity.RESERVATION, reservation);
                    startActivity(intent);
                }
            });
        }
    }

