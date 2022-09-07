package com.example.thumbnailtele2


import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.net.URL


class MainActivity : AppCompatActivity() {

    val TAG = "!!!"
    val PULLPARSER = "PULLPARSER"
    var client: OkHttpClient = OkHttpClient()


    val url =
        "https://vcdn.tv.comhem.se/vod/dash/cenc/HD/25fps/high/2ch/2nd/a48ea5c9-0d09-419d-9bbf-4636ca4968da/manifest?chSessionId=5d8b6648-97c1-44c3-b182-5ea4d025f9d7"
    var segmentTemplateValue = ""
    var newUrlContainsImageTemplate = ""
    var imageTemplateNumber = "150"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etRequestText = findViewById<EditText>(R.id.etEnterRequest)
        val btnSendRequestButton = findViewById<Button>(R.id.btnSendRequest)
        val ivShowThumbNail = findViewById<ImageView>(R.id.ivThumbnail)



        btnSendRequestButton.setOnClickListener {
            fetchRequest(url)


        }

    }


    private fun getRequest(sUrl: String): String? {
        var result: String? = null


        try {
            val url = URL(sUrl)
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            result = response.body?.string()


        } catch (err: Error) {
            Log.d(TAG, "getRequest: Error to get request : " + err.localizedMessage)
        }

        return result
    }





    private fun fetchRequest(sUrl: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            var result = getRequest(sUrl)
            if (result != null) {

                try {
                    parseXmlUsePullParser(result)

                    Log.d("IMAGEURL", "IMAGEURL: $newUrlContainsImageTemplate")

                } catch (err: Error) {
                    Log.d(TAG, "fetchRequest: Error when parsin JSON: " + err.localizedMessage)
                }

            } else {
                Log.d(TAG, "fetchRequest: Error: get request returned no respone")
            }
        }



    }




    private fun parseXmlUsePullParser(xmlString: String) {

        try {
            // Create xml pull parser factory.
            val parserFactory = XmlPullParserFactory.newInstance()

            // Create XmlPullParser.
            val xmlPullParser = parserFactory.newPullParser()

            // Create a new StringReader.
            val xmlStringReader = StringReader(xmlString)

            // Set the string reader as XmlPullParser input.
            xmlPullParser.setInput(xmlStringReader)

            // Get event type during xml parse.
            var eventType = xmlPullParser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // Get xml element node name.
                val nodeName = xmlPullParser.name
                if (!TextUtils.isEmpty(nodeName)) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if ("SegmentTemplate".equals(nodeName, ignoreCase = true)

                        ) {
                            // Get xml element text value.
                            var segmentTemplateMediaValue = xmlPullParser.getAttributeValue(null, "media")

                            segmentTemplateValue = segmentTemplateMediaValue

                            // modify the valuedata with imageTemplateNumber for an imageframe
                            segmentTemplateMediaValue = "sc-gaFEOw/V0_F$imageTemplateNumber\$.jpg?scale=160x90&d=6000"

                            newUrlContainsImageTemplate = "https://vcdn.tv.comhem.se/vod/dash/cenc/HD/25fps/high/2ch/2nd/a48ea5c9-0d09-419d-9bbf-4636ca4968da/$segmentTemplateMediaValue"

                            Log.d("NEWURL", "ATTRIBUTE VALUE : $newUrlContainsImageTemplate")
                        }
                    }
                }
                eventType = xmlPullParser.next()
            }
        } catch (ex: XmlPullParserException) {
            Log.d(PULLPARSER, "parseXmlUsePullParser: ERROR")
        }
    }


}












