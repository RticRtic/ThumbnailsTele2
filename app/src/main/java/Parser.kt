import android.app.Activity
import android.os.AsyncTask
import android.preference.PreferenceManager
import android.util.Log
import android.util.Xml
import android.widget.ImageView
import com.example.thumbnailtele2.R
import okio.IOException
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


private val ns: String? = null

class Parser {

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream): List <*>{
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readFeed(parser)
        }



    }
    @Throws(XmlPullParserException::class, IOException::class)
    private fun readFeed(parser: XmlPullParser) : List<SegmentTemplate> {
        val templates = mutableListOf<SegmentTemplate>()

        parser.require(XmlPullParser.START_TAG, ns, "Feed")
        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.eventType != XmlPullParser.START_TAG){
                continue
            }
            if(parser.name == "SegmentTemplate") {
                templates.add(readSegmentTemplate(parser))

            } else {
                skipTag(parser)
            }
        }
        return templates
    }

}


data class SegmentTemplate(val mediaImage: String? )

@Throws(XmlPullParserException::class, IOException::class)
 fun readSegmentTemplate(parser: XmlPullParser) : SegmentTemplate {
    parser.require(XmlPullParser.START_TAG, ns, "SegmentTemplate")
    var mediaImage : String? = null
    while (parser.next() != XmlPullParser.END_TAG) {
        if(parser.eventType != XmlPullParser.START_TAG){
            continue

        }
        when(parser.name){
            "SegmentTemplate" -> mediaImage = readMediaImage(parser)
            else -> skipTag(parser)
        }
    }
    return SegmentTemplate(mediaImage)


}
@Throws (XmlPullParserException::class, IOException::class)
fun skipTag(parser: XmlPullParser){
    if(parser.eventType != XmlPullParser.START_TAG){
        throw IllegalStateException()
    }
    var depth = 1
    while (depth != 0) {
        when(parser.next()) {
            XmlPullParser.END_TAG -> depth--
            XmlPullParser.START_TAG -> depth ++
        }
    }
}

@Throws(XmlPullParserException::class, IOException::class)
fun readMediaImage(parser: XmlPullParser) : String {
    parser.require(XmlPullParser.START_TAG, ns, "mediaImage")
    val image = readImage(parser)
    parser.require(XmlPullParser.END_TAG, ns, "mediaImage")
    return image
}
@Throws(IOException::class, XmlPullParserException::class)
fun readImage(parser: XmlPullParser) : String {
    var result = ""
    if(parser.next() == XmlPullParser.TEXT){
        result = parser.text
        parser.nextTag()
    }
    return result
}


//    class NetworkActivity : Activity() {
//        val TAG = "!!!"
//
//        companion object {
//            const val WIFI = "Wi-Fi"
//            const val ANY = "Any"
//            const val SO_URL = "https://vcdn.tv.comhem.se/vod/dash/cenc/HD/25fps/high/2ch/2nd/a48ea5c9-0d09-419d-9bbf-4636ca4968da/manifest?chSessionId=5d8b6648-97c1-44c3-b182-5ea4d025f9d7"
//
//            // Whether there is a Wi-Fi connection.
//            private var wifiConnected = false
//            // Whether there is a mobile connection.
//            private var mobileConnected = false
//
//            var refreshDisplay = true
//            // The user's current network preference setting.
//            var sPref: String? = null
//        }
//
//        fun loadPage(){
//            if(sPref.equals(ANY) && (wifiConnected || mobileConnected)){
//                DownloadXmlTask().execute(SO_URL)
//            } else if(sPref.equals(WIFI) && wifiConnected){
//                DownloadXmlTask().execute(SO_URL)
//            } else {
//                Log.d(TAG, "loadPage: Error connecting to internet")
//            }
//        }
//
//        private inner class DownloadXmlTask : AsyncTask<String, Void, String>(){
//            override fun doInBackground(vararg urls: String): String {
//                return try {
//                    loadXmlFromNetwork(urls[0])
//                } catch (e: XmlPullParserException) {
//                    resources.getString(R.string.xml)
//
//                }
//            }
//
//            override fun onPostExecute(result: String?) {
//                setContentView(R.layout.activity_main)
//                findViewById<ImageView>(R.id.ivThumbnail)?.apply {
//
//                }
//            }
//
//        }
//        @Throws(XmlPullParserException::class, IOException::class)
//        private fun loadXmlFromNetwork(urlString: String): String {
//            // Checks whether the user set the preference to include summary text
//            val pref: Boolean = PreferenceManager.getDefaultSharedPreferences(this)?.run {
//                getBoolean("summaryPref", false)
//            } ?: false
//
////            val templates: List<SegmentTemplate> = downloadUrl(urlString)?.use { stream ->
////                Parser().parse(stream)
////            } ?: emptyList()
//
//            return StringBuilder().apply {
////                append("<h3>${resources.getString(R.string.page_title)}</h3>")
////                append("<em>${resources.getString(R.string.updated)} ")
////                append("${formatter.format(rightNow.time)}</em>")
//                // StackOverflowXmlParser returns a List (called "entries") of Entry objects.
//                // Each Entry object represents a single post in the XML feed.
//                // This section processes the entries list to combine each entry with HTML markup.
//                // Each entry is displayed in the UI as a link that optionally includes
//                // a text summary.
////                templates.forEach { template ->
////                    append("<p><a href='")
////                    append(template.mediaImage)
////                    append("'>" + entry.title + "</a></p>")
////                    if (pref) {
////                        append(entry.summary)
////                    }
////                }
//            }.toString()
//
//        }
//        @Throws(IOException::class)
//        private fun downloadUrl(urlString: String): InputStream? {
//            val url = URL(urlString)
//            return (url.openConnection() as? HttpURLConnection)?.run {
//                readTimeout = 10000
//                connectTimeout = 15000
//                requestMethod = "GET"
//                doInput = true
//                // Starts the query
//                connect()
//                inputStream
//            }
//        }










