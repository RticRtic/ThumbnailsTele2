//import android.util.Xml
//import okio.IOException
//import org.xmlpull.v1.XmlPullParser
//import org.xmlpull.v1.XmlPullParserException
//import java.io.InputStream
//
//
//private val ns: String? = null
//
//class Parser {
//
//    @Throws(XmlPullParserException::class, IOException::class)
//    fun parse(inputStream: InputStream): List <*>{
//        inputStream.use { inputStream ->
//            val parser: XmlPullParser = Xml.newPullParser()
//            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
//            parser.setInput(inputStream, null)
//            parser.nextTag()
//            return readFeed(parser)
//        }
//
//    }
//
//}
//
//@Throws(XmlPullParserException::class, IOException::class)
//private fun readFeed(parser: XmlPullParser) : List<SegmentTemplate> {
//    val templates = mutableListOf<SegmentTemplate>()
//
//    parser.require(XmlPullParser.START_TAG, ns, "Feed")
//    while(parser.next() != XmlPullParser.END_TAG) {
//        if(parser.eventType != XmlPullParser.START_TAG){
//            continue
//        }
//        if(parser.name == "SegmentTemplate") {
//            templates.add(readSegmentTemplate(parser))
//
//        } else {
//            skip(parser)
//        }
//    }
//    return templates
//}
//
//
//data class SegmentTemplate(val mediaImage: String? )
//
//@Throws(XmlPullParserException::class, IOException::class)
//private fun readSegmentTemplate(parser: XmlPullParser) : SegmentTemplate {
//    parser.require(XmlPullParser.START_TAG, ns, "entry")
//    var mediaImage : String? = null
//    while (parser.next() != XmlPullParser.END_TAG) {
//        if(parser.eventType != XmlPullParser.START_TAG){
//            continue
//
//        }
//        when(parser.name){
//            "mediaImage" -> mediaImage = readMediaImage(parser)
//            else -> skip(parser)
//        }
//    }
//    return SegmentTemplate(mediaImage)
//
//
//}
//
//@Throws (XmlPullParserException::class, IOException::class)
//fun skip(parser: XmlPullParser){
//    if(parser.eventType != XmlPullParser.START_TAG){
//        throw IllegalStateException()
//    }
//}
//
//@Throws(XmlPullParserException::class, IOException::class)
//fun readMediaImage(parser: XmlPullParser) : String {
//    parser.require(XmlPullParser.START_TAG, ns, "mediaImage")
//    var image = readImage(parser)
//    parser.require(XmlPullParser.END_TAG, ns, "mediaImage")
//    return image
//}
//
//
//@Throws(IOException::class, XmlPullParserException::class)
//fun readImage(parser: XmlPullParser) : String {
//    var result = ""
//    if(parser.next() == XmlPullParser.TEXT){
//        result = parser.text
//        parser.nextTag()
//    }
//    return result
//}
//
//
