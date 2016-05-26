package net.raysforge.commons;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * SimpleDateFormat is slow and not thread-safe
 * @author rhulha
 *
 */
public class DateUtils {

	// utc sql format can be read like this too:
	// ava.sql.Timestamp.valueOf

	public static Calendar getZeroTimeCalendar() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(0);
		return c;
	}

	//    private static SimpleDateFormat localSqlDateFormat = null;

	public static SimpleDateFormat getLocalSqlDateFormat() {
		SimpleDateFormat localSqlDateFormat = null;
		//        if( localSqlDateFormat == null)
		//      {
		localSqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//    }
		return localSqlDateFormat;
	}

	//    private static SimpleDateFormat utcSqlDateFormat = null;

	public static SimpleDateFormat getUtcSqlDateFormat() {
		SimpleDateFormat utcSqlDateFormat = null;
		//        if( utcSqlDateFormat == null)
		//      {
		utcSqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		utcSqlDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		//    }
		return utcSqlDateFormat;
	}

	//    private static SimpleDateFormat soapDateFormat = null;

	public static SimpleDateFormat getUtcSoapDateFormat() {
		SimpleDateFormat soapDateFormat = null;
		//        if( soapDateFormat == null)
		//      {
		soapDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		soapDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		//    }
		return soapDateFormat;
	}

}
