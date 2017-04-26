package com.piaofire.datechooselibrary.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.piaofire.datechooselibrary.R;
import com.piaofire.datechooselibrary.Wheel.OnWheelChangedListener;
import com.piaofire.datechooselibrary.Wheel.OnWheelScrollListener;
import com.piaofire.datechooselibrary.Wheel.WheelView;
import com.piaofire.datechooselibrary.Wheel.adapters.AbstractWheelTextAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by gjc on 2017/4/24.
 */

public class DateChooseDialog extends Dialog implements View.OnClickListener {

    public enum Type {
        ALL, YEAR_MONTH_DAY, HOURS_MINS, MONTH_DAY_HOUR_MIN, YEAR_MONTH;
    }// 五种选择模式，年月日时分，年月日，时分，月日时分，年月

    WheelView year_wv;

    WheelView month_wv;
    WheelView day_wv;
    WheelView hour_wv;
    WheelView minute_wv;
    Button cancel_btn;
    Button ok_btn;
    public static final int DEFULT_START_YEAR = 1900;

    public static final int DEFULT_END_YEAR = 2100;
    private int startYear = DEFULT_START_YEAR;
    private int endYear = DEFULT_END_YEAR;
    //常量
    private final int MAX_TEXT_SIZE = 18;
    private final int MIN_TEXT_SIZE = 16;
    //变量
    private ArrayList<String> arry_day = new ArrayList<String>();
    private ArrayList<String> arry_hour = new ArrayList<String>();
    private ArrayList<String> arry_minute = new ArrayList<String>();
    private ArrayList<String> arry_year = new ArrayList<String>();
    private ArrayList<String> arry_month = new ArrayList<String>();
    private DateChooseInterface dateChooseInterface;

    private CalendarTextAdapter mDayAdapter;
    private CalendarTextAdapter mHourAdapter;
    private CalendarTextAdapter mMinuteAdapter;
    private CalendarTextAdapter mYearAdapter;
    private CalendarTextAdapter mMonthAdapter;

    private int nowDayId = 0;
    private int nowHourId = 0;
    private int nowMinuteId = 0;
    private int nowYearId = 0;
    private int nowMonthId = 0;
    private String mYearStr;
    private String mMonthStr;
    private String mDayStr;
    private String mHourStr;
    private String mMinuteStr;

    private Context context;
    private Dialog mDialog;
    private Type type;

    public DateChooseDialog(Context context, Type type, DateChooseInterface dateChooseInterface) {
        super(context);
        this.context = context;
        this.dateChooseInterface = dateChooseInterface;
        mDialog = new Dialog(context, R.style.dialog);
        this.type = type;
        initView();
        //默认选中当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        initData(year, month, day, hours, minute);
    }

    /**
     * 设置选中时间
     *
     * @param date
     */
    public void setTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null)
            calendar.setTimeInMillis(System.currentTimeMillis());
        else
            calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        initData(year, month, day, hours, minute);
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_data_choose, null);
        mDialog.setContentView(view);
        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用

        lp.width = (int) (d.widthPixels * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(lp);
        year_wv = (WheelView) view.findViewById(R.id.year_wv);
        month_wv = (WheelView) view.findViewById(R.id.month_wv);
        day_wv = (WheelView) view.findViewById(R.id.day_wv);
        hour_wv = (WheelView) view.findViewById(R.id.hour_wv);
        minute_wv = (WheelView) view.findViewById(R.id.minute_wv);
        cancel_btn = (Button) view.findViewById(R.id.cancle_btn);
        ok_btn = (Button) view.findViewById(R.id.ok_btn);
        cancel_btn.setOnClickListener(this);
        ok_btn.setOnClickListener(this);

        switch (type) {
            case ALL: {
                year_wv.setVisibility(View.VISIBLE);
                month_wv.setVisibility(View.VISIBLE);
                day_wv.setVisibility(View.VISIBLE);
                hour_wv.setVisibility(View.VISIBLE);
                minute_wv.setVisibility(View.VISIBLE);
                break;
            }
            case YEAR_MONTH_DAY: {
                year_wv.setVisibility(View.VISIBLE);
                month_wv.setVisibility(View.VISIBLE);
                day_wv.setVisibility(View.VISIBLE);
                hour_wv.setVisibility(View.GONE);
                minute_wv.setVisibility(View.GONE);
                break;
            }
            case HOURS_MINS: {
                year_wv.setVisibility(View.GONE);
                month_wv.setVisibility(View.GONE);
                day_wv.setVisibility(View.GONE);
                hour_wv.setVisibility(View.VISIBLE);
                minute_wv.setVisibility(View.VISIBLE);
                break;
            }
            case MONTH_DAY_HOUR_MIN: {
                year_wv.setVisibility(View.GONE);
                month_wv.setVisibility(View.VISIBLE);
                day_wv.setVisibility(View.VISIBLE);
                hour_wv.setVisibility(View.VISIBLE);
                minute_wv.setVisibility(View.VISIBLE);
                break;
            }
            case YEAR_MONTH: {
                year_wv.setVisibility(View.VISIBLE);
                month_wv.setVisibility(View.VISIBLE);
                day_wv.setVisibility(View.GONE);
                hour_wv.setVisibility(View.GONE);
                minute_wv.setVisibility(View.GONE);
                break;
            }
        }
    }

    private void initData(int year, int monthOfYear, int dayOfMonth, int h, int m) {
        initYear(year);
        initMonth(monthOfYear + 1);
        initDay(year, monthOfYear + 1, dayOfMonth);
        initHour(h);
        initMinute(m);
        initListener();
    }

    /**
     * 初始化年
     */
    private void initYear(int year) {
        arry_year.clear();
        for (int i = startYear; i <= endYear; i++) {
            arry_year.add("" + i);
            if (year == i) {
                nowYearId = arry_year.size() - 1;
            }
        }
        mYearAdapter = new CalendarTextAdapter(context, arry_year, nowYearId, MAX_TEXT_SIZE, MIN_TEXT_SIZE);
        year_wv.setVisibleItems(5);
        year_wv.setViewAdapter(mYearAdapter);
        year_wv.setCurrentItem(nowYearId);
        year_wv.setCyclic(true);
        mYearStr = arry_year.get(nowYearId);
        setTextViewStyle(mYearStr, mYearAdapter);
    }

    /**
     * 初始化月
     */
    private void initMonth(int month) {
        arry_month.clear();
        for (int i = 1; i <= 12; i++) {
            if (i < 10) {
                arry_month.add("0" + i);
            } else {
                arry_month.add("" + i);
            }
            if (i == month) {
                nowMonthId = arry_month.size() - 1;
            }
        }
        mMonthAdapter = new CalendarTextAdapter(context, arry_month, nowMonthId, MAX_TEXT_SIZE, MIN_TEXT_SIZE);
        month_wv.setVisibleItems(5);
        month_wv.setViewAdapter(mMonthAdapter);
        month_wv.setCurrentItem(nowMonthId);
        month_wv.setCyclic(true);
        mMonthStr = arry_month.get(nowMonthId);
        setTextViewStyle(mMonthStr, mMonthAdapter);
    }

    /**
     * 初始化日期
     */
    private void initDay(int year, int month, int day) {
        arry_day.clear();
        setDate(year, month, day);
        mDayAdapter = new CalendarTextAdapter(context, arry_day, nowDayId, MAX_TEXT_SIZE, MIN_TEXT_SIZE);
        day_wv.setVisibleItems(5);
        day_wv.setViewAdapter(mDayAdapter);
        day_wv.setCurrentItem(nowDayId);
        day_wv.setCyclic(true);

        mDayStr = arry_day.get(nowDayId);
        setTextViewStyle(mDayStr, mDayAdapter);
    }

    private void initHour(int h) {
        arry_hour.clear();
        for (int i = 0; i <= 23; i++) {
            if (i < 10) {
                arry_hour.add("0" + i);
            } else {
                arry_hour.add("" + i);
            }
            if (h == i) {
                nowHourId = arry_hour.size() - 1;
            }
        }

        mHourAdapter = new CalendarTextAdapter(context, arry_hour, nowHourId, MAX_TEXT_SIZE, MIN_TEXT_SIZE);
        hour_wv.setVisibleItems(5);
        hour_wv.setViewAdapter(mHourAdapter);
        hour_wv.setCurrentItem(nowHourId);
        hour_wv.setCyclic(true);
        mHourStr = arry_hour.get(nowHourId) + "";
        setTextViewStyle(mHourStr, mHourAdapter);
    }

    private void initMinute(int m) {
        arry_minute.clear();
        for (int i = 0; i <= 59; i++) {
            if (i < 10) {
                arry_minute.add("0" + i);
            } else {
                arry_minute.add("" + i);
            }
            if (m == i) {
                nowMinuteId = arry_minute.size() - 1;
            }
        }

        mMinuteAdapter = new CalendarTextAdapter(context, arry_minute, nowMinuteId, MAX_TEXT_SIZE, MIN_TEXT_SIZE);
        minute_wv.setVisibleItems(5);
        minute_wv.setViewAdapter(mMinuteAdapter);
        minute_wv.setCurrentItem(nowMinuteId);
        minute_wv.setCyclic(true);
        mMinuteStr = arry_minute.get(nowMinuteId) + "";
        setTextViewStyle(mMinuteStr, mMinuteAdapter);
    }

    private void initListener() {
        //年份*****************************
        year_wv.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mYearAdapter);
                mYearStr = arry_year.get(wheel.getCurrentItem()) + "";
            }
        });

        year_wv.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mYearAdapter);
                arry_day.clear();
                int nowYear = Integer.parseInt(currentText);
                int nowMonth = Integer.parseInt(mMonthStr);
                int nowDay = Integer.parseInt(mDayStr);
                setDate(nowYear, nowMonth, nowDay);
                mDayAdapter = new CalendarTextAdapter(context, arry_day, nowDayId, MAX_TEXT_SIZE, MIN_TEXT_SIZE);
                day_wv.setViewAdapter(mDayAdapter);
                day_wv.setCurrentItem(nowDayId);
                mDayStr = arry_day.get(nowDayId);
            }
        });
        //月***************************
        month_wv.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mMonthAdapter);
                mMonthStr = arry_month.get(wheel.getCurrentItem()) + "";
            }
        });

        month_wv.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mMonthAdapter);

                arry_day.clear();
                int nowYear = Integer.parseInt(mYearStr);
                int nowMonth = Integer.parseInt(currentText);
                int nowDay = Integer.parseInt(mDayStr);
                setDate(nowYear, nowMonth, nowDay);
                mDayAdapter = new CalendarTextAdapter(context, arry_day, nowDayId, MAX_TEXT_SIZE, MIN_TEXT_SIZE);
                day_wv.setViewAdapter(mDayAdapter);
                day_wv.setCurrentItem(nowDayId);
                mDayStr = arry_day.get(nowDayId);
            }
        });

        //日期********************
        day_wv.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) mDayAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mDayAdapter);
                mDayStr = arry_day.get(wheel.getCurrentItem());
            }
        });

        day_wv.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) mDayAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mDayAdapter);
            }
        });

        //小时***********************************
        hour_wv.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) mHourAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mHourAdapter);
                mHourStr = arry_hour.get(wheel.getCurrentItem()) + "";
            }
        });

        hour_wv.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) mHourAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mHourAdapter);
            }
        });

        //分钟********************************************
        minute_wv.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) mMinuteAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mMinuteAdapter);
                mMinuteStr = arry_minute.get(wheel.getCurrentItem()) + "";
            }
        });

        minute_wv.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) mMinuteAdapter.getItemText(wheel.getCurrentItem());
                setTextViewStyle(currentText, mMinuteAdapter);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ok_btn) {//确定选择按钮监听
            dateChooseInterface.getDateTime(strTimeToDateFormat(mYearStr, mMonthStr, mDayStr, mHourStr, mMinuteStr, type));
            dismissDialog();
        } else if (id == R.id.cancle_btn) {//关闭日期选择对话框
            dismissDialog();
        }
    }

    /**
     * xx年xx月xx日xx时xx分转成yyyy-MM-dd HH:mm
     *
     * @param yearStr
     * @param monthStr
     * @param dayStr
     * @param hourStr
     * @param minuteStr
     * @return
     */
    private String strTimeToDateFormat(String yearStr, String monthStr, String dayStr, String hourStr, String minuteStr, Type type) {
        if (type == Type.YEAR_MONTH) {
            return yearStr.replace("年", "") + "-" + monthStr.replace("月", "");
        } else if (type == Type.MONTH_DAY_HOUR_MIN) {
            return monthStr.replace("月", "") + "-" + dayStr.replace("日", " ") + " "
                    + hourStr.replace("时", "") + ":" + minuteStr.replace("分", "");
        } else if (type == Type.HOURS_MINS) {
            return hourStr.replace("时", "") + ":" + minuteStr.replace("分", "");
        } else if (type == Type.YEAR_MONTH_DAY) {
            return yearStr.replace("年", "") + "-" + monthStr.replace("月", "") + "-" + dayStr.replace("日", " ");
        }
        return yearStr.replace("年", "") + "-" + monthStr.replace("月", "") + "-" + dayStr.replace("日", "")
                + " " + hourStr.replace("时", "") + ":" + minuteStr.replace("分", "");
    }

    /**
     * 将改年的所有日期写入数组
     *
     * @param year
     */
    private void setDate(int year, int month, int nowDay) {
        boolean isRun = isRunNian(year);
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                for (int day = 1; day <= 31; day++) {
                    if (day < 10) {
                        arry_day.add("0" + day);
                    } else {
                        arry_day.add("" + day);
                    }
                    if (day == nowDay) {
                        nowDayId = arry_day.size() - 1;
                    }
                }
                break;
            case 2:
                if (isRun) {
                    for (int day = 1; day <= 29; day++) {
                        if (day < 10) {
                            arry_day.add("0" + day);
                        } else {
                            arry_day.add("" + day);
                        }
                        if (day == nowDay) {
                            nowDayId = arry_day.size() - 1;
                        }
                        if (nowDay > 29) {
                            nowDayId = arry_day.size() - 1;
                        }
                    }
                } else {
                    for (int day = 1; day <= 28; day++) {
                        if (day < 10) {
                            arry_day.add("0" + day);
                        } else {
                            arry_day.add("" + day);
                        }
                        if (day == nowDay) {
                            nowDayId = arry_day.size() - 1;
                        }
                    }
                    if (nowDay > 28) {
                        nowDayId = arry_day.size() - 1;
                    }
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                for (int day = 1; day <= 30; day++) {
                    if (day < 10) {
                        arry_day.add("0" + day);
                    } else {
                        arry_day.add("" + day);
                    }
                    if (day == nowDay) {
                        nowDayId = arry_day.size() - 1;
                    }
                }
                if (nowDay > 30) {
                    nowDayId = arry_day.size() - 1;
                }
                break;
            default:
                break;
        }
//        }
    }

    /**
     * 判断是否是闰年
     *
     * @param year
     * @return
     */
    private boolean isRunNian(int year) {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 回调选中的时间（默认时间格式"yyyy-MM-dd HH:mm:ss"）
     */
    public interface DateChooseInterface {
        void getDateTime(String time);
    }

    /**
     * 滚轮的adapter
     */
    private class CalendarTextAdapter extends AbstractWheelTextAdapter {
        ArrayList<String> list;

        protected CalendarTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_date_choose, R.id.tempValue, currentItem, maxsize, minsize);
            this.list = list;
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            String str = list.get(index) + "";
            return str;
        }
    }

    /**
     * 对话框消失
     */
    private void dismissDialog() {

        if (Looper.myLooper() != Looper.getMainLooper()) {

            return;
        }

        if (null == mDialog || !mDialog.isShowing() || null == context
                || ((Activity) context).isFinishing()) {

            return;
        }

        mDialog.dismiss();
        this.dismiss();
    }

    /**
     * 显示日期选择dialog
     */
    public void showDateChooseDialog() {

        if (Looper.myLooper() != Looper.getMainLooper()) {

            return;
        }

        if (null == context || ((Activity) context).isFinishing()) {

            // 界面已被销毁
            return;
        }

        if (null != mDialog) {

            mDialog.show();
            return;
        }

        if (null == mDialog) {

            return;
        }

        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
    }

    /**
     * 设置文字的大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextViewStyle(String curriteItemText, CalendarTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(MAX_TEXT_SIZE);
                textvew.setTextColor(context.getResources().getColor(R.color.text_10));
            } else {
                textvew.setTextSize(MIN_TEXT_SIZE);
                textvew.setTextColor(context.getResources().getColor(R.color.text_11));
            }
        }
    }

}
