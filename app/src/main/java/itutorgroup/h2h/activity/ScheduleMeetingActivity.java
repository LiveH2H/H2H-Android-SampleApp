package itutorgroup.h2h.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.itutorgroup.h2hmodel.H2HHttpRequest;
import com.itutorgroup.h2hmodel.H2HResponse;
import com.itutorgroup.h2hmodel.H2HScheduleMeetingCallback;
import com.itutorgroup.h2hmodel.H2HScheduleMeetingParamEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import itutorgroup.h2h.R;

public class ScheduleMeetingActivity extends MeetingRoomBaseActivity {
    private static final String[] DURATION_ARRAY = {"30", "60", "90", "120", "180", "240"};
    Button btnStartDate, btnStartTime, btnDuration;
    AppCompatSpinner meetingTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initDatas() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, 1);
        c.set(Calendar.MINUTE, 0);
        btnStartDate.setText(DateFormat.format("yyyy-MM-dd", c));
        btnStartDate.setTag(new int[]{c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)});
        btnStartTime.setText(DateFormat.format("HH:mm", c));
        btnStartTime.setTag(new int[]{c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)});

        btnDuration.setText(DURATION_ARRAY[1]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.meeting_type_names));
        meetingTypeSpinner.setAdapter(adapter);
        meetingTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemSelected() " + getMeetingType());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected int setContent() {
        return R.layout.activity_schedule_meeting;
    }

    @Override
    protected void initView() {
        btnStartDate = (Button) findViewById(R.id.btnStartDate);
        btnStartTime = (Button) findViewById(R.id.btnStartTime);
        btnDuration = (Button) findViewById(R.id.btnDuration);
        meetingTypeSpinner = (AppCompatSpinner) findViewById(R.id.meetingTypeSpinner);
    }

    @Override
    protected void addListener() {
    }

    public void startDateClicked(View view) {
        int[] date = (int[]) view.getTag();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth);
                btnStartDate.setText(DateFormat.format("yyyy-MM-dd", c));
                btnStartDate.setTag(new int[]{year, month, dayOfMonth});
            }
        }, date[0], date[1], date[2]).show();
    }

    public void startTimeClicked(View view) {
        int[] time = (int[]) view.getTag();
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, minute);
                btnStartTime.setText(DateFormat.format("HH:mm", c));
                btnStartTime.setTag(new int[]{hourOfDay, minute});
            }
        }, time[0], time[1], true).show();
    }

    public void durationClicked(View view) {
        new AlertDialog.Builder(this)
                .setItems(DURATION_ARRAY, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btnDuration.setText(DURATION_ARRAY[which]);
                    }
                }).create().show();

    }

    public void scheduleMeetingClicked(View view) {
        H2HScheduleMeetingParamEntity param = new H2HScheduleMeetingParamEntity();

        param.subject = ((EditText) findViewById(R.id.subjectEditText)).getText().toString().trim();
        if (param.subject.isEmpty()) {
            showToast("Please fill subject and description");
            return;
        }

        int[] date = (int[]) btnStartDate.getTag();
        int[] time = (int[]) btnStartTime.getTag();
        Calendar c = Calendar.getInstance();
        c.set(date[0], date[1], date[2], time[0], time[1], 0);
        c.set(Calendar.MILLISECOND, 0);
        param.startTime = c.getTimeInMillis();
        Log.i(this.getClass().getSimpleName(), "start time: " + DateFormat.format("yyyy-MM-dd HH:mm:ss", param.startTime));
        if (param.startTime <= System.currentTimeMillis()) {
            showToast("Meeting startTime must be in the future");
            return;
        }

        param.length = Integer.parseInt(btnDuration.getText().toString()); // 60分钟

        param.description = ((EditText) findViewById(R.id.descriptionEditText)).getText().toString().trim();

        String attendeesStr = ((EditText) findViewById(R.id.attendeesEditText)).getText().toString().trim();
        if (!attendeesStr.isEmpty()) {
            String[] attendees = attendeesStr.split(",");
            param.emailList = Arrays.asList(attendees);
        }

        param.meetingType = getMeetingType();

        param.recordMeeting = ((ToggleButton) findViewById(R.id.recordToggle)).isChecked();

        String translatorStr = ((EditText) findViewById(R.id.translatorEditText)).getText().toString().trim();
        if (!translatorStr.isEmpty()) {
            param.translatorList = new ArrayList<>();
            H2HScheduleMeetingParamEntity.TranslatorListBean bean;
            String[] translators = translatorStr.split(",");
            for (String translator : translators) {
                String[] temp = translator.trim().split(":");
                if (temp.length > 1) {
                    bean = new H2HScheduleMeetingParamEntity.TranslatorListBean();
                    bean.email = temp[0];
                    bean.lang = temp[1];
                    param.translatorList.add(bean);
                }
            }
        }

        showLoadingDialog();
        H2HHttpRequest.getInstance().scheduleMeeting(param, new H2HScheduleMeetingCallback() {
            @Override
            public void onCompleted(final Exception ex, final H2HCallBackStatus status, final H2HResponse response) {
                if (isFinishing()) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoadingDialog();
                        if (status == H2HCallBackStatus.H2HCallBackStatusOK) {
                            showToast("Schedule a meeting success: " + meetingID);
                            AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleMeetingActivity.this)
                                    .setMessage("Do you want to join the meeting now?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            Intent i = new Intent(ScheduleMeetingActivity.this, JoinMeetingActivity.class);
                                            i.putExtra("meetingId", meetingID);
                                            startActivity(i);
                                        }
                                    })
                                    .setNegativeButton("No", null);
                            builder.create().show();
                        } else {
                            final String message = "Failed to schedule a meeting"
                                    + response != null && !TextUtils.isEmpty(response.message)
                                    ? ": " + response.message : "";
                            showToast(message);
                        }
                    }
                });
            }
        });
    }

    private int getMeetingType() {
        switch (meetingTypeSpinner.getSelectedItemPosition()) {
            case 0:
                return H2HScheduleMeetingParamEntity.MEETING_TYPE_GROUP_MEETING;
            case 1:
                return H2HScheduleMeetingParamEntity.MEETING_TYPE_WEBINAR;
            default:
                return H2HScheduleMeetingParamEntity.MEETING_TYPE_LIVE_SHARE;
        }
    }
}
