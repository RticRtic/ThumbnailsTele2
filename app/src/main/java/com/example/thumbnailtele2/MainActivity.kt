package com.example.thumbnailtele2


import RequestInfo
import android.os.Bundle
import android.os.Message
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
import java.util.logging.XMLFormatter


class MainActivity : AppCompatActivity() {

    val TAG = "!!!"
    var client: OkHttpClient = OkHttpClient()


    //    var factory = XmlPullParserFactory.newInstance()
    //    var parser = factory.newPullParser()
    //    var event = parser.eventType

    val url =
        "https://vcdn.tv.comhem.se/vod/dash/cenc/HD/25fps/high/2ch/2nd/a48ea5c9-0d09-419d-9bbf-4636ca4968da/manifest?chSessionId=5d8b6648-97c1-44c3-b182-5ea4d025f9d7"


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





    private fun fetchRequest(sUrl: String): RequestInfo? {
        var requestInfo: RequestInfo? = null
        lifecycleScope.launch(Dispatchers.IO) {
            var result = getRequest(sUrl)
            if (result != null) {


                try {
                    parseXmlUsePullParser(result)
                    Log.d(TAG, "fetchRequest: $result")


                } catch (err: Error) {
                    Log.d(TAG, "fetchRequest: Error when parsin JSON: " + err.localizedMessage)
                }

            } else {
                Log.d(TAG, "fetchRequest: Error: get request returned no respone")
            }
        }
        return requestInfo


    }




    private fun parseXmlUsePullParser(xmlString: String): String? {
        val retBuf = StringBuffer()
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
                Log.d("PULLPARSER", "XMLPULLPARSER NAME : ${xmlPullParser.name} ")
                if (!TextUtils.isEmpty(nodeName)) {
                    if (eventType == XmlPullParser.START_TAG) {
                        Log.d("PULLPARSER", "ELEMENT NODENAME $nodeName")
                        if ("SegmentTemplate".equals(nodeName, ignoreCase = true)

                        ) {
                            retBuf.append(nodeName)
                            Log.d("PULLPARSER", "RETBUFF : $retBuf")


                            // Get xml element text value.
                            val value = xmlPullParser.nextText()
                            retBuf.append(" = ")
                            retBuf.append("<h3>${value.length}")
                            retBuf.append("\r\n\r\n")
//                            Log.d("PULLPARSER", "ELEMENT VALUE : $value")
                            Log.d("RETBUFFXML", "parseXmlUsePullParser: RETBUFF XML")
                        }
                    }
//                    else if (eventType == XmlPullParser.END_TAG) {
//                        Log.d(TAG, "parseXmlUsePullParser: $nodeName")
//                        if ("SegmentTemplate".equals(nodeName, ignoreCase = true)) {
//                            retBuf.append("************************\r\n\r\n")
//                        }
//                    }
                }
                eventType = xmlPullParser.next()
            }
        } catch (ex: XmlPullParserException) {
            retBuf.append(ex.message)
        } finally {
            return retBuf.toString()

        }
    }


}








