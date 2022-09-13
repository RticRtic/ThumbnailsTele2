package com.example.thumbnailtele2

import android.util.Log
import androidx.lifecycle.ViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import org.xmlpull.v1.XmlPullParser
import java.net.URL

class MainActivityViewModel : ViewModel() {
    val TAG = "!!!"

    var finalUrl = ""
    var addMutableValueToUrl = 0
    var finalMovieDuration = 0

    //Xml
    var client: OkHttpClient = OkHttpClient()



    fun thumbnailUrlFromMediaTemplate(parser: XmlPullParser, template: String) {
        val mediaValue = parser.getAttributeValue(null, "media")
        val changedURl = template.substring(0, template.indexOf("manifest?chSessionId=5d8b6648-97c1-44c3-b182-5ea4d025f9d7"))
        val newUrl = changedURl + mediaValue
        val changedUrlWithRemovedChar = newUrl.substring(0, newUrl.indexOf("$"))
        val addToEndUrl = ".jpg?scale=160x90&d=6000"
        finalUrl = changedUrlWithRemovedChar + addMutableValueToUrl + addToEndUrl

    }
    fun movieDuration(parser: XmlPullParser) {
        val movieDuration = parser.getAttributeValue(null, "mediaPresentationDuration")
        val changedMovieDurationValueWithFirstLetters = movieDuration.drop(2)
        val changedMovieDurationValueWithLastLetter = changedMovieDurationValueWithFirstLetters.dropLast(1)
        finalMovieDuration = changedMovieDurationValueWithLastLetter.toInt()
        Log.d("movieDuration", "movieDuration: $finalMovieDuration")
    }


    fun getRequest(sUrl: String): String? {
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



}