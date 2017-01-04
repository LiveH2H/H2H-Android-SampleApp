package com.meetingroom.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.itutorgroup.h2hSupport.H2HSupportManager;
import com.itutorgroup.h2hSupport.SupportCallbackListener;
import com.itutorgroup.h2hchat.H2HChat;
import com.itutorgroup.h2hconference.H2HConference;
import com.itutorgroup.h2hmodel.H2HFeatures;
import com.meetingroom.activity.MeetingActivity;
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
import com.meetingroom.bean.ServerConfig;
import com.meetingroom.utils.NotificationUtil;
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
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends BaseFragment {
    private TextView tvGroupType;
    private ListView lvChat, lvSupport;
    private ChatMessageAdapter adapter;
    private ArrayList<ChatMessage> messages = new ArrayList<>();
    private SupportMessageAdapter supportMessageAdapter;
    private Button btnSend;
    private EditText etText;
    private ImageView ivBack;
    private ChatFragmentCallback chatFragmentCallback;
    private NiceSpinner niceSpinner;

    public static ChatFragment newInstance(ServerConfig serverConfig) {
        ChatFragment fragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("serverConfig", serverConfig);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChatFragmentCallback) {
            chatFragmentCallback = (ChatFragmentCallback) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        lvChat = ViewUtil.findViewById(view, R.id.lv_chatroom);
        btnSend = ViewUtil.findViewById(view, R.id.btnSend);
        etText = ViewUtil.findViewById(view, R.id.etText);
        ivBack = ViewUtil.findViewById(view, R.id.iv_back);
        niceSpinner = ViewUtil.findViewById(view, R.id.spinner);
        lvSupport = ViewUtil.findViewById(view, R.id.lv_support);
        tvGroupType = ViewUtil.findViewById(view,R.id.tv_group_type);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ChatMessageAdapter(mContext, messages);
        adapter.addItemViewDelegate(new MsgComingItemDelegate(mContext));
        adapter.addItemViewDelegate(new MsgSendItemDelegate(mContext));
        adapter.addItemViewDelegate(new MsgTipsItemDelegate());
        adapter.addItemViewDelegate(new MsgComingImgItemDelegate(mContext));
        adapter.addItemViewDelegate(new MsgSendImgItemDelegate(mContext));
        lvChat.setAdapter(adapter);
        addListener();
        addSpinner();
        if (chatFragmentCallback != null) {
            isCreated = true;
            chatFragmentCallback.chatFragmentviewCreated();
        }
    }

    private void addListener() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
//        ViewUtil.setEdittextLabelVisibility(etText, btnSend);
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
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chatFragmentCallback != null) {
                    chatFragmentCallback.onBack();
                }
            }
        });
    }

    public void sendMessage() {
        if (TextUtils.isEmpty(etText.getText().toString().trim())) {
            ToastUtils.showToast(mContext, getString(R.string.msg_empty_tip));
        } else {
            if (lvChat.getVisibility() == View.VISIBLE) {
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
                    ToastUtils.showToast(mContext, getString(R.string.tip_offline));
                }
            }
            //support
            else if (lvSupport.getVisibility() == View.VISIBLE) {
                H2HSupportManager.getInstance().sendMessage(etText.getText().toString());
                Tools.hideSoftInput(etText);
                etText.setText("");
            }

        }
    }

    public void receiveUserConnectionStatus(ChatMessage chatMessage) {
        Log.e("App Level", "receiveUserConnectionStatus");
        messages.add(chatMessage);
        adapter.notifyDataSetChanged();
        lvChat.setSelection(messages.size() - 1);
    }

    /*public void receiveUserState(ChatMessage message) {
        Log.e("App Level", "receiveUserState");
        messages.add(message);
        adapter.notifyDataSetChanged();
        lvChat.setSelection(messages.size() - 1);
    }*/

    public void receiveUserState() {
        selectGroupType(niceSpinner.getSelectedIndex());
    }

    public void receiveMessage(ChatMessage message) {
        messages.add(message);
        adapter.notifyDataSetChanged();
        lvChat.setSelection(messages.size() - 1);
        if (!MyApplication.INSTANCE.isHome() && TextUtils.equals(MeetingActivity.class.getSimpleName(), AppManager.getAppManager().currentActivity().getClass().getSimpleName()))
            NotificationUtil.getInstance().showNotification(mContext, MeetingActivity.class, true, message.content);
        if (chatFragmentCallback != null) {
            if (isHide && message.msgComing) {
                chatFragmentCallback.getNewMessage();
            }
        }
    }

    public void receiveClearMessage() {
        messages.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (adapter != null) {
                new Handler(mContext.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lvChat.setSelection(messages.size() - 1);
                        //support
                        lvSupport.setSelection(H2HSupportManager.getInstance().getMessages().size() - 1);
                    }
                }, 50);

            }
        } else {
            Tools.hideSoftInput(etText);
        }
    }

    public interface ChatFragmentCallback {
        void getNewMessage();

        void chatFragmentviewCreated();

        void onBack();
    }

    private void setListViewVisibility(int position) {
        lvChat.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        lvSupport.setVisibility(position != 0 ? View.VISIBLE : View.GONE);
    }

    private void addSpinner() {
        supportMessageAdapter = new SupportMessageAdapter(mContext, H2HSupportManager.getInstance().getMessages());
        supportMessageAdapter.addItemViewDelegate(new STMsgComingItemDelegate(mContext));
        supportMessageAdapter.addItemViewDelegate(new STMsgSendItemDelegate(mContext));
        lvSupport.setAdapter(supportMessageAdapter);

        groupType.add(getString(R.string.group));
        groupType.add(getString(R.string.customer_service));

        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectGroupType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        niceSpinner.setShowInCenter(true, groupType, 0);
        niceSpinner.setClickable(H2HFeatures.isLiveSupportEnabled());

        H2HSupportManager.getInstance().attachListener(new SupportCallbackListener() {
            @Override
            public void updateMessage(boolean isComing, String message) {
                supportMessageAdapter.notifyDataSetChanged();
                lvSupport.setSelection(H2HSupportManager.getInstance().getMessages().size() - 1);
                if (!MyApplication.INSTANCE.isHome()
                        && TextUtils.equals(MeetingActivity.class.getSimpleName(), AppManager.getAppManager().currentActivity().getClass().getSimpleName())
                        && isComing)
                    NotificationUtil.getInstance().showNotification(mContext, MeetingActivity.class, true, message);
                if (chatFragmentCallback != null) {
                    if (isHide && isComing) {
                        chatFragmentCallback.getNewMessage();
                    }
                }

            }
        });
    }

    public void showSupportMessage() {
        if (niceSpinner != null) {
            niceSpinner.setSelectedIndex(1);
        }
    }

    private List<String> groupType = new ArrayList<>();

    private void selectGroupType(int pos){
        setListViewVisibility(pos);
        String text = groupType.get(pos) + (pos == 0 ? String.format(Locale.getDefault(), "(%d)", H2HChat.getInstance().getOnlineCount()) : "");
        tvGroupType.setText(text);
    }
}
