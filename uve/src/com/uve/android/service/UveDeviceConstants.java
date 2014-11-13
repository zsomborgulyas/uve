package com.uve.android.service;

import java.util.UUID;

public class UveDeviceConstants {
	public static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");

	// question commands
	public static final int QUE_SERIAL = 0;
	public static final int QUE_MEASURE_UV = 1;
	public static final int QUE_DAILY_DOSE = 2;
	public static final int QUE_DAILY_INTENSE = 3;
	public static final int QUE_PREVIOUS_MELANIN = 4;
	public static final int QUE_MESURE_MELANIN = 5;
	public static final int QUE_PREVIOUS_ERITEMA = 6;
	public static final int QUE_MESURE_ERITEMA = 7;
	public static final int QUE_BATTERY = 9;
	public static final int QUE_TIME = 16;
	public static final int QUE_PLANNED_MEASURE = 28;
	public static final int QUE_ALTER_PLANNED_MEASURE = 29;
	public static final int QUE_PING = 34;
	public static final int QUE_WAKEUP_DUMP = 36;
	public static final int QUE_STATUSES = 39;
	
	// answer constants
	public static final String ANS_SERIAL = "ans_serial";
	public static final String ANS_MEASURE_UV = "ans_measureuv";
	public static final String ANS_DAILY_DOSE_COUNT = "ans_dosec";
	public static final String ANS_DAILY_DOSE_1 = "ans_dose1";
	public static final String ANS_DAILY_DOSE_2 = "ans_dose2";
	public static final String ANS_DAILY_DOSE_3 = "ans_dose3";
	public static final String ANS_DAILY_DOSE_4 = "ans_dose4";
	public static final String ANS_DAILY_DOSE_FROM = "ans_dose_from";
	public static final String ANS_DAILY_INTENSE_COUNT = "ans_intc";
	public static final String ANS_DAILY_INTENSE_PREFIX = "ans_int";
	public static final String ANS_PREVIOUS_MELANIN = "ans_pmel";
	public static final String ANS_MESURE_MELANIN = "ans_mel";
	public static final String ANS_PREVIOUS_ERITEMA = "ans_peri";
	public static final String ANS_MESURE_ERITEMA = "ans_eri";
	public static final String ANS_BATTERY_LP = "ans_batlp";
	public static final String ANS_BATTERY_SC = "ans_batsc";
	public static final String ANS_TIME_DAY = "ans_td";
	public static final String ANS_TIME_HOUR = "ans_th";
	public static final String ANS_TIME_MIN = "ans_tm";
	public static final String ANS_TIME_SEC = "ans_ts";

	public static final String ANS_ALTER_DOSE = "ans_mado";
	public static final String ANS_ALTER_MODE = "ans_mamo";
	public static final String ANS_ALTER_MELANIN_FRONT = "ans_mafr";
	public static final String ANS_ALTER_MELANIN_BACK = "ans_maba";

	public static final String ANS_PING = "ans_ping";

	public static final String ANS_DUMP_TIME_DAY = "ans_wudtd";
	public static final String ANS_DUMP_TIME_HOUR = "ans_wudth";
	public static final String ANS_DUMP_TIME_MIN = "ans_wudtm";
	public static final String ANS_DUMP_TIME_SEC = "ans_wudts";

	public static final String ANS_ST_MELANIN = "ans_st_1";
	public static final String ANS_ST_ERITEMA = "ans_st_2";
	public static final String ANS_ST_DAILY_DOSE = "ans_st_2";
	public static final String ANS_ST_DAILY_DOSE_LIMIT = "ans_st_2";
	public static final String ANS_ST_UV_LIMIT = "ans_st_2";
	public static final String ANS_ST_SKIN_REG = "ans_st_2";
	public static final String ANS_ST_MEASURE_START = "ans_st_2";
	public static final String ANS_ST_TIME_BETWEEN_MEASURES = "ans_st_2";
	public static final String ANS_ST_MEASURE_MODE = "ans_st_2";
	public static final String ANS_ST_MANUAL = "ans_st_2";
	public static final String ANS_ST_REALTIME = "ans_st_2";
	public static final String ANS_ST_ALERT_TYPE = "ans_st_2";
	public static final String ANS_ST_MORNING_ALERT_TYPE = "ans_st_2";
	public static final String ANS_ST_SNOOZE = "ans_st_2";
	public static final String ANS_ST_MORNING_ALERT = "ans_st_2";
	public static final String ANS_ST_MORNING_UVA_ALERT = "ans_st_2";
	public static final String ANS_ST_CHILD_PROTECTION = "ans_st_2";
	public static final String ANS_ST_PLANNED_MODE = "ans_st_2";
	public static final String ANS_ST_ENERGY_SAVER = "ans_st_2";
	public static final String ANS_ST_DELAYED_MEASURE = "ans_st_2";
	public static final String ANS_ST_ILLNESS = "ans_st_2";
	
	
	
	// commands, that are not waiting for response
	public static final int COMS_ENERGY = 10;
	public static final String COM_ENERGY = "com_energy";

	public static final int COMS_TIMEOUT = 11;
	public static final String COM_TIMEOUT = "com_timeout";

	public static final int COMS_MEASURETYPE = 12;
	public static final String COM_MEASURETYPE = "com_mtype";

	public static final int COMS_MEASUREMANUAL = 13;
	public static final String COM_MEASUREMANUAL = "com_mman";

	public static final int COMS_RESTART_MEASURE = 14;

	public static final int COMS_DELETE_MESAURES = 15;

	public static final int COMS_TIME = 17;
	public static final String COM_TIME_DAY = "com_td";
	public static final String COM_TIME_HOUR = "com_th";
	public static final String COM_TIME_MIN = "com_tm";
	public static final String COM_TIME_SEC = "com_ts";

	public static final int COMS_TIMED_TIME = 18;
	public static final String COM_TIMED_TIME_DAY = "com_ttd";
	public static final String COM_TIMED_TIME_HOUR = "com_tth";
	public static final String COM_TIMED_TIME_MIN = "com_ttm";
	public static final String COM_TIMED_TIME_SEC = "com_tts";

	public static final int COMS_SOFT_RESET = 19;

	public static final int COMS_DELETE_UV_DOSE = 20;

	public static final int COMS_FEEDBACK = 22;
	public static final String COM_FEEDBACK = "com_feedback";

	public static final int COMS_ILLNESS = 23;
	public static final String COM_ILLNESS_1 = "com_ill1";
	public static final String COM_ILLNESS_2 = "com_ill2";
	public static final String COM_ILLNESS_3 = "com_ill3";
	public static final String COM_ILLNESS_4 = "com_ill4";
	public static final String COM_ILLNESS_INTENSE = "com_illi";
	public static final String COM_ILLNESS_REGEN = "com_illr";

	public static final int COMS_ALERTTYPE = 24;
	public static final String COM_ALERTTYPE = "com_alerttype";

	public static final int COMS_WAKEUP = 25;
	public static final String COM_WAKEUP = "com_wu";

	public static final int COMS_WAKEUP_PARAMS = 26;
	public static final String COM_WAKEUP_DAY = "com_wud";
	public static final String COM_WAKEUP_HOUR = "com_wuh";
	public static final String COM_WAKEUP_MIN = "com_wum";
	public static final String COM_WAKEUP_SEC = "com_wus";
	public static final String COM_WAKEUP_ALERTTYPE = "com_wualerttype";
	public static final String COM_WAKEUP_REPEATTYPE = "com_wurepeat";
	public static final String COM_WAKEUP_SNOOZE5SEC = "com_wusnooze";

	public static final int COMS_CHILD = 27;
	public static final String COM_CHILD = "com_child";

	public static final String COM_MELANIN_PRE_FRONT = "com_mpfront";
	public static final String COM_MELANIN_PRE_BACK = "com_mpback";
	public static final String COM_MODE = "com_mpmode";

	public static final int COMS_NIGHT = 30;
	public static final String COM_NIGHT = "com_night";

	public static final int COMS_VIBRATE = 31;
	public static final String COM_VIBRATE = "com_vibrate";

	public static final int COMS_RGB = 32;
	public static final String COM_RGB_R = "com_rgbr";
	public static final String COM_RGB_G = "com_rgbg";
	public static final String COM_RGB_B = "com_rgbb";
	public static final String COM_RGB_TIME = "com_rgbt";

	public static final int COMS_BUZZER = 33;
	public static final String COM_BUZZER_FREQ = "com_buzzerf";
	public static final String COM_BUZZER_TIME = "com_buzzert";

	public static final int COMS_TORCH = 35;
	public static final String COM_TORCH = "com_buzzerf";

	public static final int COMS_DISABLE_WAKEUPS = 37;
}