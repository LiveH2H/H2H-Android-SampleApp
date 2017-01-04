package com.meetingroom.fragment.flat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.itutorgroup.h2hSupport.H2HSupportManager;
import com.itutorgroup.h2hSupport.SupportCallbackListener;
import com.itutorgroup.h2hchat.H2HChat;
import com.itutorgroup.h2hconference.H2HConference;
import com.itutorgroup.h2hmodel.H2HFeatures;
import com.meetingroom.activity.flat.FlatMeetingActivity;
import com.meetingroom.adapter.ChatMessageAdapter;
import com.meetingroom.adapter.SupportMessageAdapter;
import com.meetingroom.adapter.delegate.MsgComingImgItemDelegate;
import com.meetingroom.adapter.delegate.MsgComingItemDelegate;
import com.meetingroom.adapter.delegate.MsgSendImgItemDelegate;
import com.meetingroom.adapter.delegate.MsgSendItemDelegate;
import com.meetingroom.adapter.delegate.MsgTipsItemDelegate;
import com.meetingroom.adapter.delegate.STMsgComingItemDelegate;
import com.meetingroom.adapter.delegate.STMsgSendItemDelegate;
import com.meetingroom.bean.ChatMessage;
import com.meetingroom.callback.MDialogmissCallback;
import com.meetingroom.utils.NotificationUtil;
import com.meetingroom.utils.SystemUtil;
import com.meetingroom.utils.ToastUtils;
import com.meetingroom.utils.Tools;
import com.meetingroom.view.NiceSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import itutorgroup.h2h.AppManager;
import itutorgroup.h2h.MyApplication;
import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.ViewUtil;

/**
 * 修改头像弹出框
 *
 * @author Rays 2016年4月12日
 */
public class FlatChatRoomDialog extends Dialog {
    private TextView tvGroupType;
    private MDialogmissCallback mDialogmissCallback;
    private Context mContext;
    private ListView lvChat,lvSupport;
    private ChatMessageAdapter adapter;
    private ArrayList<ChatMessage> messages = new ArrayList<>();
    private Button btnSend;
    private EditText etText;
    private FlatChatRoomDialogCallback flatChatRoomDialogCallback;
    private SupportMessageAdapter supportMessageAdapter;
    private NiceSpinner niceSpinner;
    public FlatChatRoomDialog(Context context) {
        this(context, R.style.chatDialog);
    }

    private FlatChatRoomDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        if(mContext instanceof MDialogmissCallback){
            mDialogmissCallback = (MDialogmissCallback) mContext;
        }
        initViews(context);
        addListener();
    }

    private void initViews(Context mContext) {
        LinearLayout container = new LinearLayout(mContext);
        container.setOrientation(LinearLayout.VERTICAL);
        View view = View.inflate(mContext, R.layout.layout_chat, null);
        int width = Tools.getAtyWidth(mContext) / 2;
        lvChat = ViewUtil.findViewById(view, R.id.lv_chatroom);
        lvSupport = ViewUtil.findViewById(view, R.id.lv_support);
        niceSpinner = ViewUtil.findViewById(view,R.id.spinner);
        btnSend = ViewUtil.findViewById(view, R.id.btnSend);
        etText = ViewUtil.findViewById(view, R.id.etText);
        tvGroupType = ViewUtil.findViewById(view,R.id.tv_group_type);
        View top = new View(mContext);
        top.setBackgroundColor(Color.TRANSPARENT);
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowing()) {
                    dismiss();
                }
            }
        });
        container.addView(top, new ViewGroup.LayoutParams(width, (int) mContext.getResources().getDimension(R.dimen.titleheight)));
        container.addView(view, new ViewGroup.LayoutParams(Tools.getAtyWidth(mContext) / 2, Tools.getAtyHeight(mContext) - (int) mContext.getResources().getDimension(R.dimen.titleheight)));
        setContentView(container);

        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = width;
            layoutParams.height = Tools.getAtyHeight(mContext);
            layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
            layoutParams.x = 0;
            layoutParams.windowAnimations = R.style.dialogWindowAnim;
            try {
                layoutParams.y = (int) (Tools.getStateBarHeight(mContext) + mContext.getResources().getDimension(R.dimen.titleheight));
            } catch (Exception e) {
                e.printStackTrace();
            }
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }

    private void addListener() {
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
        adapter = new ChatMessageAdapter(mContext, messages);
        adapter.addItemViewDelegate(new MsgComingItemDelegate(mContext));
        adapter.addItemViewDelegate(new MsgSendItemDelegate(mContext));
        adapter.addItemViewDelegate(new MsgTipsItemDelegate());
        adapter.addItemViewDelegate(new MsgComingImgItemDelegate(mContext));
        adapter.addItemViewDelegate(new MsgSendImgItemDelegate(mContext));
        lvChat.setAdapter(adapter);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
//        ViewUtil.setEdittextLabelVisibility(etText, btnSend);
        setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                Tools.hideSoftInput(etText);
                SystemUtil.hideSoftInput((Activity) mContext);
                etText.clearFocus();
                if(mDialogmissCallback!=null){
                    mDialogmissCallback.callback();
                }
            }
        });
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                SystemUtil.hideSoftInput((Activity) mContext);
            }
        });
        etText.setImeOptions(EditorInfo.IME_ACTION_SEND);
        etText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //do something;
                    btnSend.performClick();
                    return true;
                }
                return false;
            }
        });
        addSpinner();

    }


    private void sendMessage() {
        if (TextUtils.isEmpty(etText.getText().toString().trim())) {
            ToastUtils.showToast(mContext, mContext.getString(R.string.msg_empty_tip));
        } else {
            if(lvChat.getVisibility()==View.VISIBLE){
                if (H2HChat.getInstance().isConnected()) {
                    if (H2HConference.getInstance().isToggleChatEnabled()) {
                        H2HChat.getInstance().sendTextToGroup(etText.getText().toString());
                        Tools.hideSoftInput(etText);
                        etText.setText("");
                    } else {
                        ToastUtils.showToast(mContext, mContext.getString(R.string.chat_not_allow));
                    }
                } else {
                    Log.e("App Level Chat", "You are offline");
                    ToastUtils.showToast(mContext, mContext.getString(R.string.tip_offline));
                }
            }
            //support
            else if(lvSupport.getVisibility()==View.VISIBLE){
                H2HSupportManager.getInstance().sendMessage(etText.getText().toString());
                Tools.hideSoftInput(etText);
                etText.setText("");
            }

        }
    }

    public void receiveUserConnectionStatus(ChatMessage chatMessage) {
        messages.add(chatMessage);
        adapter.notifyDataSetChanged();
        lvChat.setSelection(messages.size() - 1);
    }

    public void receiveUserState(){
        selectGroupType(niceSpinner.getSelectedIndex());
    }
    public void receiveMessage(ChatMessage message) {
        messages.add(message);
        adapter.notifyDataSetChanged();
        lvChat.setSelection(messages.size() - 1);
        if (!MyApplication.INSTANCE.isHome() && TextUtils.equals(FlatMeetingActivity.class.getSimpleName(), AppManager.getAppManager().currentActivity().getClass().getSimpleName()))
            NotificationUtil.getInstance().showNotification(mContext, FlatMeetingActivity.class, true, message.content);
        if (flatChatRoomDialogCallback != null) {
            if (!isShowing() && message.msgComing) {
                flatChatRoomDialogCallback.getNewMessage(message);
            }
        }
    }

    public void receiveClearMessage() {
        messages.clear();
        adapter.notifyDataSetChanged();
    }

    public void setFlatChatRoomDialogCallback(FlatChatRoomDialogCallback flatChatRoomDialogCallback) {
        this.flatChatRoomDialogCallback = flatChatRoomDialogCallback;
    }

    public interface FlatChatRoomDialogCallback {
        void getNewMessage(ChatMessage message);
    }
    private void addSpinner(){
        supportMessageAdapter = new SupportMessageAdapter(mContext, H2HSupportManager.getInstance().getMessages());
        supportMessageAdapter.addItemViewDelegate(new STMsgComingItemDelegate(mContext));
        supportMessageAdapter.addItemViewDelegate(new STMsgSendItemDelegate(mContext));
        lvSupport.setAdapter(supportMessageAdapter);

        groupType.add(getContext().getString(R.string.group));
        groupType.add(getContext().getString(R.string.customer_service));

        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectGroupType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        niceSpinner.setShowInCenter(true,groupType,0);
        niceSpinner.setClickable(H2HFeatures.isLiveSupportEnabled());

        H2HSupportManager.getInstance().attachListener(new SupportCallbackListener(){
            @Override
            public void updateMessage(boolean isComing,String message) {
                supportMessageAdapter.notifyDataSetChanged();
                lvSupport.setSelection(H2HSupportManager.getInstance().getMessages().size()-1);
                if (!MyApplication.INSTANCE.isHome()
                        && TextUtils.equals(FlatMeetingActivity.class.getSimpleName(), AppManager.getAppManager().currentActivity().getClass().getSimpleName())
                        && isComing)
                    NotificationUtil.getInstance().showNotification(mContext, FlatMeetingActivity.class, true, message);
                if (flatChatRoomDialogCallback != null) {
                    if (!isShowing() && isComing) {
                        flatChatRoomDialogCallback.getNewMessage(null);
                    }
                }
            }
        });
    }
    public void showSupportMessage(){
        if(niceSpinner!=null){
            niceSpinner.setSelectedIndex(1);
        }
    }
    private void setListViewVisibility(int position){
        lvChat.setVisibility(position==0?View.VISIBLE:View.GONE);
        lvSupport.setVisibility(position!=0?View.VISIBLE:View.GONE);
    }
    private List<String> groupType = new ArrayList<>();

    private void selectGroupType(int pos){
        setListViewVisibility(pos);
        String text = groupType.get(pos) + (pos == 0 ? String.format(Locale.getDefault(), "(%d)", H2HChat.getInstance().getOnlineCount()) : "");
        tvGroupType.setText(text);
    }
}
