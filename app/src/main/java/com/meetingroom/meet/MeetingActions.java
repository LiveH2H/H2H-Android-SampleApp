package com.meetingroom.meet;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.itutorgroup.h2hconference.H2HConference;
import com.itutorgroup.h2hmodel.H2HModel;
import com.itutorgroup.h2hwhiteboard.H2HWhiteboardManager;
import com.meetingroom.activity.BaseActivity;
import com.meetingroom.activity.MRPollActivity;
import com.meetingroom.bean.ServerConfig;
import com.meetingroom.bean.poll.getResult.PollResult;
import com.meetingroom.bean.poll.summary.Poll;
import com.meetingroom.callback.WebinarHostCallback;
import com.meetingroom.constants.MeetingConstants;
import com.meetingroom.fragment.BaseFragment;
import com.meetingroom.utils.ConferenceUtils;
import com.meetingroom.utils.LogUtils;
import com.meetingroom.utils.MRUtils;
import com.meetingroom.utils.SystemUtil;
import com.meetingroom.utils.ToastUtils;
import com.meetingroom.widget.TimerMaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.ViewUtil;
import me.drakeet.materialdialog.MaterialDialog;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Meeting相关操作封装类
 * <p>
 * Created by linchaolong on 2016/8/17.
 */
public class MeetingActions {

    public static boolean isHost(int hostSn){
//        return UserPF.getInstance().getInt(UserPF.USER_ID, 0) == hostSn;
        return hostSn > 0;
    }

//    /**
//     * Security Meeting passcode dialog
//     *
//     * @param context
//     * @param callback
//     */
//    private static void showPasscodeDialog(final BaseActivity context, final OnInputedPasscode callback){
//        final AlertDialog dialog = new AlertDialog.Builder(context)
//                .setTitle(R.string.enter_live_share_password_hint)
//                .setView(R.layout.dialog_security_meet_password)
//                .setPositiveButton(R.string.submit, null)
//                .setNegativeButton(R.string.cancel, null)
//                .create();
//        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialogInterface) {
//                final EditText edit = (EditText) dialog.findViewById(R.id.passcode);
//                ViewUtil.setEditorAction(edit, dialog.getButton(AlertDialog.BUTTON_POSITIVE));
//                SystemUtil.showSoftInput(edit, context);
//
//                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.gray_));
//                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        String passcode = edit.getText().toString();
//                        if(TextUtils.isEmpty(passcode)){
//                            context.showToast(R.string.passcode_cannot_be_empty);
//                            return;
//                        }
//                        callback.onSubmit(passcode);
//                        dialog.cancel();
//                    }
//                });
//            }
//        });
//        dialog.show();
//    }
//
//    /**
//     * 更新会议
//     *
//     * @param context
//     * @param meeting
//     * @param attendeesList
//     * @param responseHandler
//     */
//    public static void updateMeeting(BaseActivity context, Meeting meeting, List<String> attendeesList, HttpResponseHandler responseHandler) {
//        AppAction.updateMeeting(context,
//                meeting.subject,
//                meeting.description,
//                String.valueOf(meeting.meetingType),
//                meeting.meetingSn,
//                attendeesList,
//                meeting.location,
//                meeting.startTime,
//                meeting.recordMeeting, meeting.duration, responseHandler);
//    }
//
//    /**
//     * 请求Meeting与会者列表
//     *
//     * @param context
//     * @param includeTranslator
//     * @param meeting
//     * @param onGetedMeetingAttendeesList
//     */
//    public static void requestMeetingAttendeesList(final BaseActivity context, final boolean includeTranslator, final Meeting meeting, final OnGetedMeetingAttendeesList onGetedMeetingAttendeesList) {
//        AppAction.getAttendeesList(context, meeting.meetingSn, new HttpResponseHandler(context, AttendeesList.class) {
//            @Override
//            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
//                final AttendeesList attendeesList = (AttendeesList) response;
//                if (includeTranslator) {
//                    AppAction.getTranslatorList(context, meeting.meetingSn, new HttpResponseHandler(context, TranslatorResponse.class) {
//                        @Override
//                        public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
//                            TranslatorResponse translatorResponse = (TranslatorResponse) response;
//                            onGetedMeetingAttendeesList.onGeted(meeting, attendeesList, translatorResponse);
//                        }
//                    });
//                } else {
//                    onGetedMeetingAttendeesList.onGeted(meeting, attendeesList, null);
//                }
//            }
//        });
//    }
//
//    /**
//     * 显示会议详情
//     *
//     * @param context
//     * @param meetingId
//     */
//    public static void startMeetingDetailActivity(final BaseActivity context, final String meetingId) {
//        AppAction.searchMeetingById(context, meetingId, new SearchMeetingResponseHandler(context, Meeting.class) {
//            @Override
//            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
//                final Meeting meeting = (Meeting) response;
//                startMeetingDetailActivity(context, meeting);
//            }
//        });
//    }
//
//    /**
//     * 显示会议详情
//     *
//     * @param context
//     * @param meeting
//     */
//    public static void startMeetingDetailActivity(final BaseActivity context, Meeting meeting) {
//        requestMeetingAttendeesList(context, true, meeting, new OnGetedMeetingAttendeesList() {
//            @Override
//            public void onGeted(Meeting meeting, AttendeesList attendeesList, TranslatorResponse translatorResponse) {
//                Intent intent = new Intent(context, MeetingDetailActivity.class);
//                intent.putExtra(MeetingDetailActivity.INTENT_MEETING, meeting);
//                intent.putExtra(MeetingDetailActivity.INTENT_ATTENDEES, attendeesList);
//                intent.putExtra(MeetingDetailActivity.INTENT_TRANSLATORS, translatorResponse);
//                context.startActivityForResult(intent, MeetingDetailActivity.REQUEST_CODE_MEETING_DETAIL);
//            }
//        });
//    }
//
//    /**
//     * 启动会议
//     *
//     * @param context
//     */
//    public static void instantMeeting(final BaseActivity context) {
//        instantMeeting(context, null, null);
//    }
//
//    public static void instantMeeting(final BaseActivity context, String name, String email) {
//        UserPF userPF = UserPF.getInstance();
//        if(TextUtils.isEmpty(name)){
//            name = userPF.getString(UserPF.USER_NAME, "");
//        }
//        if(TextUtils.isEmpty(email)){
//            email = userPF.getString(UserPF.USER_EMAIL, "");
//        }
//        AppAction.instantMeeting(context, name, email, new HttpResponseHandler(context, JoinMeetingInfo.class) {
//            @Override
//            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
//                JoinMeetingInfo joinMeetingInfo = (JoinMeetingInfo) response;
//                launchMeeting(context, joinMeetingInfo.serverURL, joinMeetingInfo.meetingUri, joinMeetingInfo.origin);
//            }
//        });
//    }
//
//    /**
//     * 加入/开始会议
//     *
//     * @param context
//     * @param meeting
//     */
//    public static void joinMeeting(final BaseActivity context, final Meeting meeting) {
//        joinMeeting(context, meeting.meetingId);
//    }
//
//    /**
//     * 加入/开始会议
//     *
//     * @param context
//     * @param meetingId
//     */
//    public static void joinMeeting(final BaseActivity context, String meetingId) {
//        joinMeeting(context, meetingId, null, null, null);
//    }
//
//    /**
//     * 加入/开始会议
//     *
//     * @param context
//     * @param meetingId
//     * @param name
//     * @param email
//     * @param passcode
//     */
//    public static void joinMeeting(final BaseActivity context, final String meetingId, final String name, final String email, String passcode) {
//
//        AppAction.joinMeeting(context, meetingId, name, email, passcode, new HttpResponseHandler(context, JoinMeetingInfo.class) {
//            @Override
//            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
//                JoinMeetingInfo joinMeetingInfo = (JoinMeetingInfo) response;
//                launchMeeting(context, joinMeetingInfo.serverURL, joinMeetingInfo.meetingUri, joinMeetingInfo.origin);
//            }
//            @Override
//            public void onResponeseFail(int statusCode, HttpResponse response, String responseString) {
//                super.onResponeseFail(statusCode, response, responseString);
//                //ErrorCodes info:
//                //-200: invalid shareType passed.
//                //-201: fail to join a private meeting because user is not invited.
//                //-202: fail to join a secure meeting because user didn't provide meeting password.
//                //-203: fail to join a secure meeting because meeting password doesn't match.
//                //-204: fail to join a internal meeting because user doesn't belong to the same company.
//                //-205: fail to create a secure meeting because user didn't provide password.
//                //-206: fail to create a secure meeting because user didn't provide valid password.
//                //-207: fail to create an internal meeting because user doesn't belong to a company.
//                switch (response.returnCode){
//                    case -200:
//                        showToast(R.string.meeting_is_passed);
//                        break;
//                    case -201:
//                        showToast(R.string.you_are_not_invited);
//                        break;
//                    case -202:
//                        // 输入 Secure Meeting 密码
//                        MeetingActions.showPasscodeDialog(context, new MeetingActions.OnInputedPasscode() {
//                            @Override
//                            public void onSubmit(String password) {
//                                joinMeeting(context, meetingId, name, email, password);
//                            }
//                        });
//                        break;
//                    case -203:
//                        showToast(R.string.the_meeting_password_does_not_match);
//                        break;
//                    case -204:
//                        showToast(R.string.you_do_not_belong_same_company);
//                        break;
//                    case -205:
//
//                        break;
//                    case -206:
//                        showToast(R.string.you_do_not_provide_valid_password);
//                        break;
//                    case -207:
//                        showToast(R.string.you_do_not_belong_a_company);
//                        break;
//                    case -11:
//                        // 检测到输入的是 Host 的 email 跳转到输入密码界面
//                        Intent intent = new Intent(context, LoginToStartActivity.class);
//                        intent.putExtra("email", email);
//                        intent.putExtra("meetingId", meetingId);
//                        intent.putExtra("name", name);
//                        context.startActivityForResult(intent, 0);
//                        context.overridePendingTransition(R.anim.bottom_in, 0);
//                        break;
//                    // 状态码：200 返回值：{"returnCode":-4,"message":"Meeting not found"}
//                    case -4:
//                        showToast(R.string.meeting_not_found);
//                        break;
//                    // 状态码：200 返回值：{"returnCode":-5,"message":"internal error"}
//                    case -5:
//                        showToast(R.string.internal_error);
//                        break;
//                    case -3:
//                        showToast(R.string.error_hint_meeting_has_ended);
//                        break;
//                    default:
//                        showToast(response.message);
//                        break;
//                }
//
//            }
//        });
//    }
//
//    /**
//     * 启动会议
//     *
//     * @param context
//     * @param serverURL
//     * @param userToken
//     * @param origin
//     */
//    public static void launchMeeting(final BaseActivity context, final String serverURL, final String userToken, final String origin) {
//        Intent intent;
//        if (SystemUtil.isTablet(context)) {
//            intent = new Intent(context, com.meetingroom.activity.flat.FlatMeetingActivity.class);
//        } else {
//            intent = new Intent(context, com.meetingroom.activity.MeetingActivity.class);
//        }
//        ServerConfig serverConfig = new ServerConfig();
//        serverConfig.userToken = userToken;
//        serverConfig.origin = origin;
//        serverConfig.serverURL = serverURL;
//        intent.putExtra("serverConfig", serverConfig);
//        context.startActivityForResult(intent, 0);
//    }
//
//    /**
//     * 安排会议界面
//     *
//     * @param context
//     */
//    public static void startScheduleMeetingActivity(BaseActivity context) {
//        context.startActivityForResult(new Intent(context, ScheduleMeetingActivity.class), ScheduleMeetingActivity.REQUEST_CODE_SCHEDULE_MEETING);
//    }
//
//    /**
//     * create share
//     *
//     * @param context
//     */
//    public static void startCreateShareActivity(BaseActivity context){
//        context.startActivityForResult(new Intent(context, CreateShareActivity.class), ScheduleMeetingActivity.REQUEST_CODE_SCHEDULE_MEETING);
//    }
//
//    /**
//     * 加入会议界面
//     *
//     * @param context
//     */
//    public static void startJoinMeetingActivity(Context context) {
//        context.startActivity(new Intent(context, LoginedJoinMeetingActivity.class));
//    }
//
//    /**
//     * 邀请联系人界面
//     *
//     * @param context
//     * @param meeting           meeting将会在 {@link AddContactsEvent#meeting} 中原样返回，可以传null
//     * @param attendeesList
//     */
//    public static void startInviteContactsActivity(BaseActivity context, @Nullable Meeting meeting, List<Contact> attendeesList) {
//        Intent intentContacts = new Intent(context, InviteContactsActivity.class);
//        intentContacts.putExtra(InviteContactsActivity.INTENT_MEETING, meeting);
//        intentContacts.putExtra(InviteContactsActivity.INTENT_ATTENDEESLIST, (Serializable) attendeesList);
//        context.startActivity(intentContacts);
//    }
//
//    /**
//     * 更多操作界面,目前主要是添加事件到日历操作
//     *
//     * @param context
//     * @param subject
//     * @param startTime
//     * @param endTime
//     */
//    public static void startMoreOptionsActivity(Context context, String subject, long startTime, long endTime) {
//        Intent intent = new Intent(context, MoreOptionsActivity.class);
//        intent.putExtra("subject", subject);
//        intent.putExtra("startTime", startTime);
//        intent.putExtra("endTime", endTime);
//        context.startActivity(intent);
//    }


    public static void updateView(Context mContext, int rank, boolean isFullScreen, boolean hideLayout, RadioGroup rpRank, GLSurfaceView videoView, AbsoluteLayout altAvatars, AbsoluteLayout altNames, WebinarHostCallback callback) {
        //Special Handling for Webinar
        if (ConferenceUtils.filterWebinar(mContext, videoView, altAvatars, altNames, isFullScreen, hideLayout, callback)) {
            rpRank.setVisibility(hideLayout ? View.GONE : View.VISIBLE);
            return;
        }
        if (rank == BaseFragment.RANK_POINT) {
            ConferenceUtils.updateVideoViewLayoutByAdding3(H2HConference.getInstance());
            ConferenceUtils.updateAvatars3(mContext, H2HConference.getInstance(), altAvatars, isFullScreen);
            ConferenceUtils.updateNames3(mContext, H2HConference.getInstance(), altNames, isFullScreen, hideLayout);
        } else {
            ConferenceUtils.updateVideoViewLayoutByAdding2(H2HConference.getInstance());
            ConferenceUtils.updateAvatars2(mContext, H2HConference.getInstance(), altAvatars, isFullScreen);
            ConferenceUtils.updateNames2(mContext, H2HConference.getInstance(), altNames, isFullScreen, hideLayout);
        }
        rpRank.setVisibility(hideLayout ? View.GONE : View.VISIBLE);
    }

//    public static void inviteParticipants(Context mContext, int resultCode, Intent data) {
//        if (resultCode == Activity.RESULT_OK && data != null) {
//            if (data.hasExtra("meetingroom")) {
//                ArrayList<Contact> contacts = (ArrayList<Contact>) data.getSerializableExtra("contacts");
//                if (contacts != null && contacts.size() != 0) {
//                    String[] emails = new String[contacts.size()];
//                    for (int i = 0; i < contacts.size(); i++) {
//                        emails[i] = contacts.get(i).email;
//                    }
//                    ShareUtil.meetingRoomInviteParticipants(mContext, emails);
//                }
//            }
//        }
//    }
//    public static void startPoll(BaseActivity context, Poll poll, int tempIndex){
//        if(poll.getEndTime()- MRUtils.getCurrentTimeMillis()>=0){
//            Intent intent = new Intent(context, MRPollActivity.class);
//            intent.putExtra("poll", poll);
//            intent.putExtra(MeetingConstants.tempIndex,tempIndex);
//            context.startActivityForResult(intent, MeetingConstants.startPoll);
//        }else{
//            ToastUtils.showToast(context,context.getString(R.string.polling_ends));
//        }
//    }
//    public static  void startPoll(final BaseActivity context,String pollId) {
//        AppAction.getMRPoll(context, H2HConference.getInstance().getMeetingId(), pollId, new HttpResponseHandler(context,PollResult.class) {
//            @Override
//            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
//                LogUtils.e(responseString);
//                PollResult result = (PollResult) response;
//                if(result.getPoll().getEndTime()-System.currentTimeMillis()>=0){
//                    Intent intent = new Intent(context, MRPollActivity.class);
//                    intent.putExtra("pollResult", result);
//                    context.startActivityForResult(intent, 0);
//                }else{
//                    ToastUtils.showToast(context,context.getString(R.string.polling_ends));
//                }
//            }
//        });
//    }
//    public static void backLoginActivity(Context context, int resultCode, Intent data) {
//        if (!BuildConfig.DEBUG) {
//            if (resultCode == Activity.RESULT_OK && data != null) {
//                if (data.hasExtra("hasLogin")) {
//                    if (!data.getBooleanExtra("hasLogin", false)) {
//                        Intent intent = new Intent();
//                        intent.putExtra("hasLogin", false);
//                        ((Activity) context).setResult(Activity.RESULT_OK, intent);
//                        ((Activity) context).finish();
//                    }
//                }
//            }
//        }
//    }
//
//    public interface OnGetedMeetingAttendeesList {
//        void onGeted(Meeting meeting, AttendeesList attendeesList, TranslatorResponse translatorResponse);
//    }
//
//    public interface OnInputedPasscode {
//        void onSubmit(String passcode);
//    }

    public static void showPresenceCheckDialog(Context context,Object... args){
        final MaterialDialog dialog = new MaterialDialog(context);
        dialog.setMessage(context.getString(R.string.take_attendance))
                .setPositiveButton(context.getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("name", H2HModel.getInstance().getRealDisplayName());
                            LogUtils.e(jsonObject.toString());
                            H2HWhiteboardManager.getInstance().sendPresenceToHost(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(context.getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void checkMeetingPermission(Activity context, List<String> perms, int requestCode) {
        if (EasyPermissions.somePermissionPermanentlyDenied(context, perms)) {
            new AppSettingsDialog.Builder(context, context.getString(R.string.rationale_ask_again))
                    .setTitle(context.getString(R.string.title_settings_dialog))
                    .setPositiveButton(context.getString(R.string.setting))
                    .setNegativeButton(context.getString(R.string.cancel), null /* click listener */)
                    .setRequestCode(requestCode)
                    .build()
                    .show();
        }
    }
    public static void showHostLeaveDialog(final Activity activity) {
        TimerMaterialDialog dialog = new TimerMaterialDialog(activity, 5000);
        dialog.setMessage(R.string.host_left_the_meeting)
                .setCanceledOnTouchOutside(false)
                .setPositiveButton(R.string.ok, new TimerMaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(MaterialDialog dialog) {
                        dialog.dismiss();
                        activity.finish();
                    }
                });
        dialog.show();
    }

    /**
     * 被主持人踢出房间
     * @param activity
     */
    public static void showLeftRoomDialog(final Activity activity) {
        TimerMaterialDialog dialog = new TimerMaterialDialog(activity, 5000);
        dialog.setMessage(R.string.left_room_the_meeting)
                .setTitle(R.string.warning)
                .setCanceledOnTouchOutside(false)
                .setPositiveButton(R.string.left_room_button, new TimerMaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(MaterialDialog dialog) {
                        dialog.dismiss();
                        activity.finish();
                    }
                });
        dialog.show();
    }

    /**
     * 被主持人踢出房间
     * @param activity
     */
    public static void showBlockedUserDialog(final Activity activity) {
        TimerMaterialDialog dialog = new TimerMaterialDialog(activity, 10000);
        dialog.setMessage(R.string.blocked_user_the_meeting)
                .setTitle(R.string.warning)
                .setCanceledOnTouchOutside(false)
                .setPositiveButton(R.string.left_room_button, new TimerMaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(MaterialDialog dialog) {
                        dialog.dismiss();
                        activity.finish();
                    }
                });
        dialog.show();
    }
}
