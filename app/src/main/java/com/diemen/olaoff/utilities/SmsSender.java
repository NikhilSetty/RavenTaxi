package com.diemen.olaoff.utilities;

import android.location.Location;
import android.telephony.SmsManager;

/**
 * Created by vikoo on 27/09/15.
 */
public class SmsSender {

    private static final String SEPARATOR = ",";

    public static void sendSms(String phoneNum, String msg, Location myLocation,/* Location destination,*/ String cabType) {

        StringBuilder builder = new StringBuilder();
        builder.append("OLA" + SEPARATOR);
        builder.append(myLocation.getLatitude() + SEPARATOR);
        builder.append(myLocation.getLongitude() + SEPARATOR);
        // auth code
        builder.append("auth" + SEPARATOR);
        // cab type
        builder.append(cabType + SEPARATOR);
        // destination
        builder.append("Kundallahalli" + SEPARATOR);
        // timer
        builder.append("10");

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNum, null, msg, null, null);
    }
}
