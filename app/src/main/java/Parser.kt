import android.util.Xml
import okio.IOException
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.InputStream


private val ns: String? = null

class Parser {


    @Throws(XmlPullParserException::class, IOException::class)
    private fun readFeed(parser: XmlPullParser) : List<Entry> {
        val entries = MutableListOf<Entry>()

        parser.require(XmlPullParser.START_TAG, ns, "Feed")
        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.eventType != XmlPullParser.START_TAG){
                continue
            }
            if(parser.name == "SegmentTemplate") {
                entries.add(readSegmentTemplate(parser))

            } else {
                skip(parser)
            }
        }
        return entries
    }

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

}

data class SegmentTemplate(val mediaImage: String? )

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readSegmentTemplate(parser: XmlPullParser) : Entry {
        parser.require(XmlPullParser.START_TAG, ns, "entry")
        val mediaImage : String? = null
        while (parser.next() != XmlPullParser.END_TAG) {
            if(parser.eventType != XmlPullParser.START_TAG){
                continue

            }
            when(parser.name){
                "mediaImage" -> mediaImage = readMediaImage(parser)
                else -> skip(parser)
            }
        }
        return Entry(mediaImage)



        @Throws(XmlPullParserException::class, IOException::class)
        fun readMediaImage(parser: XmlPullParser){}


    }


