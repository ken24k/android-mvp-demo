package com.ken24k.android.mvpdemo.common.permission;

import android.Manifest;

public class PermissionConstants {

    public class Calendar {
        public static final String READ_CALENDAR = Manifest.permission.READ_CALENDAR;
        public static final String WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR;
    }

    public class Camera {
        public static final String CAMERA = Manifest.permission.CAMERA;
    }

    public class Contacts {
        public static final String READ_CONTACTS = Manifest.permission.READ_CONTACTS;
        public static final String WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS;
        public static final String GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    }

    public class Location {
        public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
        public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    }

    public class Microphone {
        public static final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    }

    public class Phone {
        public static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
        public static final String CALL_PHONE = Manifest.permission.CALL_PHONE;
        public static final String READ_CALL_LOG = Manifest.permission.READ_CALL_LOG;
        public static final String WRITE_CALL_LOG = Manifest.permission.WRITE_CALL_LOG;
        public static final String ADD_VOICEMAIL = Manifest.permission.ADD_VOICEMAIL;
        public static final String USE_SIP = Manifest.permission.USE_SIP;
        public static final String PROCESS_OUTGOING_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS;
    }

    public class Sensors {
        public static final String BODY_SENSORS = Manifest.permission.BODY_SENSORS;
    }

    public class Sms {
        public static final String SEND_SMS = Manifest.permission.SEND_SMS;
        public static final String RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
        public static final String READ_SMS = Manifest.permission.READ_SMS;
        public static final String RECEIVE_WAP_PUSH = Manifest.permission.RECEIVE_WAP_PUSH;
        public static final String RECEIVE_MMS = Manifest.permission.RECEIVE_MMS;
        public static final String READ_CELL_BROADCASTS = "android.permission.READ_CELL_BROADCASTS";
    }

    public class Storage {
        public static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
        public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    }

    public interface PermissionManagerListener {

        /**
         * 获得权限
         */
        void onGranted();

        /**
         * 未获得权限
         */
        void onUngranted(String msg);

        /**
         * 获得权限失败
         */
        void onError(String msg);

    }

}
