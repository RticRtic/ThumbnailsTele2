//import android.app.Activity
//import android.os.AsyncTask
//import android.preference.PreferenceManager
//import android.util.Log
//import android.webkit.WebView
//import android.widget.ImageView
//import com.example.thumbnailtele2.R
//import okio.IOException
//import org.xmlpull.v1.XmlPullParserException
//import java.util.prefs.Preferences
//import java.util.prefs.PreferencesFactory
//
//
//
//class NetworkActivity : Activity() {
//    val TAG = "!!!"
//
//    companion object {
//        const val WIFI = "Wi-Fi"
//        const val ANY = "Any"
//        const val SO_URL = "https://vcdn.tv.comhem.se/vod/dash/cenc/HD/25fps/high/2ch/2nd/a48ea5c9-0d09-419d-9bbf-4636ca4968da/manifest?chSessionId=5d8b6648-97c1-44c3-b182-5ea4d025f9d7"
//
//        // Whether there is a Wi-Fi connection.
//        private var wifiConnected = false
//        // Whether there is a mobile connection.
//        private var mobileConnected = false
//
//        var refreshDisplay = true
//        // The user's current network preference setting.
//        var sPref: String? = null
//    }
//
//    fun loadPage(){
//        if(sPref.equals(ANY) && (wifiConnected || mobileConnected)){
//            DownloadXmlTask().execute(SO_URL)
//        } else if(sPref.equals(WIFI) && wifiConnected){
//            DownloadXmlTask().execute(SO_URL)
//        } else {
//            Log.d(TAG, "loadPage: Error connecting to internet")
//        }
//    }
//
//    private inner class DownloadXmlTask : AsyncTask<String, Void, String>(){
//        override fun doInBackground(vararg urls: String?): String {
//            return try {
//                loadFromNetWork(urls[0])
//            } catch (e: XmlPullParserException) {
//                resources.getString(R.string.xml)
//
//            }
//        }
//
//        override fun onPostExecute(result: String?) {
//            setContentView(R.layout.activity_main)
//            findViewById<ImageView>(R.id.ivThumbnail)?.apply {
//
//            }
//        }
//
//    }
//}
//
//@Throws(XmlPullParserException::class, IOException::class)
//private fun loadXmlFromNetwork(urlString: String): String {
//    // Checks whether the user set the preference to include summary text
//    val pref: Boolean = PreferenceManager.getDefaultSharedPreferences(this)?.run {
//        getBoolean("summaryPref", false)
//    } ?: false
//
//    val entries: List<Entry> = downloadUrl(urlString)?.use { stream ->
//        // Instantiate the parser
//        StackOverflowXmlParser().parse(stream)
//    } ?: emptyList()
//
//    return StringBuilder().apply {
//        append("<h3>${resources.getString(R.string.page_title)}</h3>")
//        append("<em>${resources.getString(R.string.updated)} ")
//        append("${formatter.format(rightNow.time)}</em>")
//        // StackOverflowXmlParser returns a List (called "entries") of Entry objects.
//        // Each Entry object represents a single post in the XML feed.
//        // This section processes the entries list to combine each entry with HTML markup.
//        // Each entry is displayed in the UI as a link that optionally includes
//        // a text summary.
//        entries.forEach { entry ->
//            append("<p><a href='")
//            append(entry.link)
//            append("'>" + entry.title + "</a></p>")
//            // If the user set the preference to include summary text,
//            // adds it to the display.
//            if (pref) {
//                append(entry.summary)
//            }
//        }
//    }.toString()
//}