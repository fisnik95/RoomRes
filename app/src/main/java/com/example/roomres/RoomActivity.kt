package com.example.roomres

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast

import com.example.roomres.MODELS.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.lits.*

import java.io.IOException

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class RoomActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)


        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        getDataUsingOkHttpEnqueue()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.login_item -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.add_item -> {
                val intentAdd = Intent(this, AddReservationActivity::class.java)
                startActivity(intentAdd)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun getDataUsingOkHttpEnqueue() {
        val client = OkHttpClient()
        val requestBuilder = Request.Builder()
        requestBuilder.url(URI)
        val request = requestBuilder.build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonString = response.body!!.string()
                    runOnUiThread { populateList(jsonString) }
                } else {
                    runOnUiThread {
                        val messageView = findViewById<TextView>(R.id.main_message_textview)
                        messageView.text = URI + "\n" + response.code + " " + response.message
                    }
                }
            }

            override fun onFailure(call: Call, ex: IOException) {
                runOnUiThread {
                    val messageView = findViewById<TextView>(R.id.main_message_textview)
                    messageView.text = ex.message
                }
            }
        })
    }

    private fun populateList(jsonString: String) {
        val gson = GsonBuilder().create()
        Log.d("ROA", jsonString)
        val rooms = gson.fromJson(jsonString, Array<Room>::class.java)
        val listView = findViewById<ListView>(R.id.rooms_Listview)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, rooms)
        listView.adapter = adapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            Toast.makeText(this@RoomActivity, "Room clicked: " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT)
                    .show()
            val intent = Intent(baseContext, ReservationRoomActivity::class.java)
            val room = parent.getItemAtPosition(position) as Room
            intent.putExtra(ReservationRoomActivity.ROOM, room)
            startActivity(intent)
        }
    }

    companion object {
        val URI = "http://anbo-roomreservationv3.azurewebsites.net/api/rooms"
    }


}

