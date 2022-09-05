package com.example.thumbnailtele2

import RequestInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL


class MainActivity : AppCompatActivity() {

    val TAG = "!!!"
    var client : OkHttpClient = OkHttpClient()

    val url = "https://vcdn.tv.comhem.se/vod/dash/cenc/HD/25fps/high/2ch/2nd/a48ea5c9-0d09-419d-9bbf-4636ca4968da/manifest?chSessionId=5d8b6648-97c1-44c3-b182-5ea4d025f9d7"

    private fun getRequest(sUrl: String) : String? {
        var result: String? = null

        try {
            val url = URL(sUrl)
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            result = response.body?.string()
            Log.d(TAG, "getRequest: fetch: " + result.toString())


        } catch (err: Error) {
            Log.d(TAG, "getRequest: Error to get request : " + err.localizedMessage)
        }

        return result
    }

    private fun fetchRequest(sUrl: String) : RequestInfo? {
        var requestInfo: RequestInfo? = null
        lifecycleScope.launch(Dispatchers.IO) {
            val result = getRequest(sUrl)
        }
        return requestInfo
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val etRequestText = findViewById<EditText>(R.id.etEnterRequest)
        val btnSendRequestButton = findViewById<Button>(R.id.btnSendRequest)
        val ivShowThumbNail = findViewById<ImageView>(R.id.ivThumbnail)


        btnSendRequestButton.setOnClickListener {
//            val text = etRequestText.text
            fetchRequest(url)


//            Log.d("!!!", text.toString())
//            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
//            ivShowThumbNail.setImageResource(R.drawable.ireland)



        }



    }
}