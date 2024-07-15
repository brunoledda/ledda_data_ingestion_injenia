import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//Se un campo non dev'essere trasformato
row8.column_name = row7.column_name

String pdate = row7.publisheddate;
String udate = row7.updateddate;

SimpleDateFormat[] possibleDateTimeFormats = {
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"),
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"),
        new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'")
    };
for (SimpleDateFormat format : possibleDateTimeFormats) {
    format.setTimeZone(TimeZone.getTimeZone("UTC"));
}
for (SimpleDateFormat format : possibleDateTimeFormats) {
   try {
   	if(pdate!=null){
         row8.publisheddate= format.parse(pdate);
        }
    } catch (ParseException e) {
       	System.out.println(e.getMessage());
}
   try {
   	if(udate!=null){
            row8.updateddate = format.parse(udate);
        }
   } catch (ParseException e) {
       	System.out.println(e.getMessage());
   }
}
