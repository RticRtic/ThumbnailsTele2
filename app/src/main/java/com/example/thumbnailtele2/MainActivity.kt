package com.example.thumbnailtele2


import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    //Xml
    var client: OkHttpClient = OkHttpClient()
    var url =
        "https://vcdn.tv.comhem.se/vod/dash/cenc/HD/25fps/high/2ch/2nd/a48ea5c9-0d09-419d-9bbf-4636ca4968da/manifest?chSessionId=5d8b6648-97c1-44c3-b182-5ea4d025f9d7"
    var addMutableValueToUrl = 0
    var finalUrl = ""


    //Seekbar
    val seekBarMinValue = 0
    var finalMovieDuration = 0
//    var seekBarMaxValue = 0
    val seekBarStep = 6



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sbSeekBar = findViewById<SeekBar>(R.id.sbSeekBar)
        val etEnterUrl = findViewById<EditText>(R.id.etUrlRequest)
        val btnGetMovie = findViewById<Button>(R.id.btnGetRequest)
        val relativeLayout = findViewById<RelativeLayout>(R.id.relativeLayout)


        sbSeekBar.progress = seekBarMinValue
        sbSeekBar.max = finalMovieDuration


        btnGetMovie?.setOnClickListener {
            fetchRequest(url)


            Log.d("BUTTON", "BUTTON: ${finalMovieDuration} ")
        }


        sbSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekbar: SeekBar, progress: Int, fromUser: Boolean) {
                fetchRequest(url)
                val moveThumbNail = resources.displayMetrics.density
                val progressPadding = addMutableValueToUrl
                val paddingLeft = progressPadding * moveThumbNail.toInt()
                Log.d("paddingLeft", "paddingLeft: $paddingLeft ")

                if(progress in seekBarMinValue..finalMovieDuration){
                    addMutableValueToUrl = progress / seekBarStep
//                   relativeLayout.setPadding(paddingLeft,0,0,0)
                }

                Log.d("paddingLeft", "addMutableValueToUrl: $addMutableValueToUrl ")
                Log.d("paddingLeft", "moveThumbNail: $moveThumbNail ")



            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Log.d("SEEKBAR", "onStopTrackingTouch: ${sbSeekBar.progress}")


            }


        })

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
            val result = getRequest(sUrl)
            if (result != null) {

                try {

                    withContext(Dispatchers.Main) {
                        parseXmlUsePullParser(result)


                    }

                } catch (err: Error) {
                    Log.d("TRACER", "fetchRequest: Error when parsin JSON: " + err.localizedMessage)
                }

            } else {
                Log.d("TRACER", "fetchRequest: Error: get request returned no respone")
            }
        }



    }


    private fun parseXmlUsePullParser(xmlString: String) {

        val ivShowThumbNail = findViewById<ImageView>(R.id.ivThumbnail)


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
                        if ("SegmentTemplate".equals(nodeName, ignoreCase = true)) {

                            // Get xml SegmentTemplate element text value. And modify it
                            val mediaValue = xmlPullParser.getAttributeValue(null, "media")
                            val changedURl = url.substring(0, url.indexOf("manifest?chSessionId=5d8b6648-97c1-44c3-b182-5ea4d025f9d7"))
                            val newUrl = changedURl + mediaValue
                            val changedUrlWithRemovedChar = newUrl.substring(0, newUrl.indexOf("$"))
                            val addToEndUrl = ".jpg?scale=160x90&d=6000"
                            finalUrl = changedUrlWithRemovedChar + addMutableValueToUrl + addToEndUrl
//                            Log.d("SUBSTRING", "parseXmlUsePullParser: $finalUrl ")


                            //show image
                            Picasso.with(this)
                                .load(finalUrl)
                                .into(ivShowThumbNail)



                        // Get xml mediaPresentationDuration element text value. And modify it
                        } else if ("MPD".equals(nodeName, ignoreCase = true)) {
                            val movieDuration =
                                xmlPullParser.getAttributeValue(null, "mediaPresentationDuration")
                            val changedMovieDurationValueWithFirstLetters = movieDuration.drop(2)
                            val changedMovieDurationValueWithLastLetter = changedMovieDurationValueWithFirstLetters.dropLast(1)
                            finalMovieDuration = changedMovieDurationValueWithLastLetter.toInt()



                        }

                    }
                }
                eventType = xmlPullParser.next()

            }
        } catch (ex: XmlPullParserException) {
            Log.d("TRACER", "parseXmlUsePullParser: ERROR")

        }

    }



}
















