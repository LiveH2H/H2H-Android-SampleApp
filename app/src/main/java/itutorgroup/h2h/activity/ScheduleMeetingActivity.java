package itutorgroup.h2h.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.itutorgroup.h2hmodel.H2HHttpRequest;
import com.itutorgroup.h2hmodel.H2HScheduleMeetingCallback;

import itutorgroup.h2h.R;

public class ScheduleMeetingActivity extends MeetingRoomBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initDatas() {
    }

    @Override
    protected int setContent() {
        return R.layout.activity_schedule_meeting;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void addListener() {
    }

    public void scheduleMeetingClicked(View view) {

        String subject = ((EditText)findViewById(R.id.subjectEditText)).getText().toString().trim();
        String description = ((EditText)findViewById(R.id.descriptionEditText)).getText().toString().trim();
        String[] invitees = ((EditText)findViewById(R.id.attendeesEditText)).getText().toString().trim().split(",");
        String[] translators = ((EditText)findViewById(R.id.translatorEditText)).getText().toString().trim().split(",");
        Boolean isGroupMeeting = ((ToggleButton) findViewById(R.id.meetingTypeToggle)).isChecked();
        Boolean shouldRecord = ((ToggleButton) findViewById(R.id.recordToggle)).isChecked();
        TimePicker timePicker = (TimePicker)findViewById(R.id.timePicker);
        DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);
        int hour = timePicker.getCurrentHour();
        int min  = timePicker.getCurrentMinute();
        int date = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Time time = new Time();
        time.set(0,min,hour,date, month, year);
        long startTime = time.toMillis(true);

        if (subject.length()>0 && description.length()>0) {
            H2HHttpRequest.getInstance().scheduleMeeting(subject, description, startTime,invitees, translators, isGroupMeeting, shouldRecord, new H2HScheduleMeetingCallback() {
                @Override
                public void onCompleted(final Exception ex, final H2HCallBackStatus status) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (status == H2HCallBackStatus.H2HCallBackStatusOK){
                                Toast.makeText(ScheduleMeetingActivity.this,"Schedule a meeting success: "+meetingID,Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ScheduleMeetingActivity.this);
                                alertDialogBuilder.setMessage("Do you want to join the meeting now?");

                                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent i = new Intent(ScheduleMeetingActivity.this, JoinMeetingActivity.class);
                                        i.putExtra("meetingId",meetingID);
                                        startActivity(i);
                                    }
                                });
                                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ScheduleMeetingActivity.this,"Failed to schedule a meeting",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }else {
            Toast.makeText(this,"Please fill subject and description",Toast.LENGTH_SHORT).show();
        }
    }
}
