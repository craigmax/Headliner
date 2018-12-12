package com.example.max.headliner

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_main.layoutManager  = LinearLayoutManager(this)

        //Do only if Network is connected
        if (isNetworkConnected()) {

            //Get info from web requests
            fetchJSON()

        } else {

            //Alert on no network connection
            AlertDialog.Builder(this).setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again")
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .setIcon(android.R.drawable.ic_dialog_alert).show()
        }
    }

    fun fetchJSON() {

        //Setup API Http Request
        val url = "https://api.nytimes.com/svc/movies/v2/reviews/search.json"
        val request =  Request.Builder()
            .url(url)
            .addHeader("api-key",InsertApiKeyHere)
            .build()
        val client = OkHttpClient()

        //Make API request and store response in body container

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(response: Response?) {
                val body = response?.body()?.string()
                val gson = GsonBuilder().create()
                val newsFeed = gson.fromJson(body, NewsFeed::class.java)

                //Execute on main UI thread
                runOnUiThread {
                    rv_main.adapter = MainAdapter(newsFeed)
                }
            }

            override fun onFailure(request: Request?, e: IOException?) {
                println("Failed API Request!")
            }
        })
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager //1
        val networkInfo = connectivityManager.activeNetworkInfo //2
        return networkInfo != null && networkInfo.isConnected //3
    }

}

// API Response Class definitions
class NewsFeed(val results: List<Movies>)
class Movies(val display_title: String, val mpaa_rating: String, val summary_short: String,
             val opening_date: String, val multimedia: Multimedia )
class Multimedia (val src: String)


