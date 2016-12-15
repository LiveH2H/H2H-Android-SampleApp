package itutorgroup.h2h.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.itutorgroup.h2hchat.H2HBroadcastReceiver;
import com.itutorgroup.h2hchat.H2HChat;
import com.itutorgroup.h2hchat.H2HChatConstant;
import com.itutorgroup.h2hchat.H2HChatMessage;
import com.itutorgroup.h2hchat.H2HChatUser;
import com.mosai.utils.Tools;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import itutorgroup.h2h.AppManager;
import itutorgroup.h2h.MyApplication;
import itutorgroup.h2h.R;
import itutorgroup.h2h.activity.MeetingActivity;
import itutorgroup.h2h.adapter.ChatMessageAdapter;
import itutorgroup.h2h.adapter.delegate.MsgComingItemDelegate;
import itutorgroup.h2h.adapter.delegate.MsgSendItemDelegate;
import itutorgroup.h2h.adapter.delegate.MsgTipsItemDelegate;
import itutorgroup.h2h.bean.ChatMessage;
import itutorgroup.h2h.bean.ServerConfig;
import itutorgroup.h2h.bean.event.Event;
import itutorgroup.h2h.utils.NotificationUtil;
import itutorgroup.h2h.utils.ViewUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends BaseFragment {
    private ImageButton btnSend;
    private EditText etMsg;
    private H2HBroadcastReceiver chatMessageReceiver;
    private H2HBroadcastReceiver chatUserStatusReceiver;
    private H2HBroadcastReceiver userConnectionStatusReceiver;
    private ListView lv;
    private ChatMessageAdapter adapter;
    private ArrayList<ChatMessage> messages = new ArrayList<>();
    private Button btnAddChat;


    public static ChatFragment newInstance(ServerConfig serverConfig) {
        ChatFragment fragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("serverConfig", serverConfig);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroadcast();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
//        lv = ViewUtil.findViewById(view, R.id.iv_chatroom);
//        etMsg = ViewUtil.findViewById(view, R.id.et_msg);
//        btnSend = ViewUtil.findViewById(view, R.id.btn_sendmsg);
//        btnAddChat = ViewUtil.findViewById(view, R.id.btn_addChat);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ChatMessageAdapter(mContext, messages);
        adapter.addItemViewDelegate(new MsgComingItemDelegate());
        adapter.addItemViewDelegate(new MsgSendItemDelegate());
        adapter.addItemViewDelegate(new MsgTipsItemDelegate());
        lv.setAdapter(adapter);
        addListener();
    }

    private void addListener() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        btnAddChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterBroadcast();
    }

    private void registerBroadcast() {
        //Register notifications
        chatMessageReceiver = new H2HBroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Receive a new message
                H2HChatMessage chatMessage = (H2HChatMessage) intent.getSerializableExtra(H2HChatConstant.H2HChatMessage);
                receiveMessage(chatMessage);
            }
        };
        mContext.registerReceiver(chatMessageReceiver, new IntentFilter(H2HChatConstant.RECEIVE_CHAT));

        chatUserStatusReceiver = new H2HBroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //A user status changed
                H2HChatUser chatUser = (H2HChatUser) intent.getSerializableExtra(H2HChatConstant.H2HChatUser);
                receiveUserState(chatUser);
            }
        };
        mContext.registerReceiver(chatUserStatusReceiver, new IntentFilter(H2HChatConstant.CHAT_USER_STATUS_CHANGE));

        //Register notifications
        userConnectionStatusReceiver = new H2HBroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isConnected = intent.getBooleanExtra(H2HChatConstant.CONNECTION_STATUS, false);
               receiveUserConnectionStatus(isConnected);
            }
        };
        mContext.registerReceiver(userConnectionStatusReceiver, new IntentFilter(H2HChatConstant.CONNECTION_STATUS_CHANGE));
        H2HChat.getInstance().startToReceiveMessage();

    }

    private void unRegisterBroadcast() {
        mContext.unregisterReceiver(chatMessageReceiver);
        mContext.unregisterReceiver(chatUserStatusReceiver);
        mContext.unregisterReceiver(userConnectionStatusReceiver);
    }

    public void sendMessage() {
        if (TextUtils.isEmpty(etMsg.getText().toString().trim())) {
            Log.e("App Level Chat","You are offline");
        } else {
            if (H2HChat.getInstance().isConnected()) {
                H2HChat.getInstance().sendTextToGroup(etMsg.getText().toString());
                Tools.hideSoftInput(etMsg);
                etMsg.setText("");
            } else {
                Log.e("App Level Chat","You are offline");
            }
        }
    }
    private void receiveUserConnectionStatus(boolean isConnected){
        Log.e("App Level","receiveUserConnectionStatus");
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.content = mContext.getString(isConnected?R.string.chat_me_join_tip:R.string.chat_me_left_tip);
        messages.add(chatMessage);
        adapter.notifyDataSetChanged();
        lv.setSelection(messages.size() - 1);
        Log.e("App Level Chat","You are " + (isConnected ? "online" : "offline"));
    }

    private void receiveUserState(H2HChatUser chatUser) {
        Log.e("App Level","receiveUserState");
        String displayName = chatUser.getDisplayName();
        Boolean isOnline = chatUser.isOnline();
        ChatMessage message = new ChatMessage();
        String content = (TextUtils.equals(displayName, H2HChat.getInstance().displayName) ? "You" : displayName) + (isOnline ? " joined" : " left");
        message.content = content;
        messages.add(message);
        adapter.notifyDataSetChanged();
        lv.setSelection(messages.size() - 1);
        if(!MyApplication.INSTANCE.isHome()
                && TextUtils.equals(MeetingActivity.class.getSimpleName(),AppManager.getAppManager().currentActivity().getClass().getSimpleName())
                && !TextUtils.equals(H2HChat.getInstance().displayName,chatUser.getDisplayName()))
        NotificationUtil.getInstance().showNotification(mContext, true, content);
    }

    private void receiveMessage(H2HChatMessage chatMessage) {
        ChatMessage message = new ChatMessage();
        String messageBody = chatMessage.getMessageBody();
        String from = chatMessage.getFrom();
        String fromId = from.split("/")[1];
        if (TextUtils.equals(fromId, H2HChat.getInstance().getUserId())) {
            message.msgSend = true;
        } else {
            message.msgComing = true;
        }

        String[] strings = fromId.split("_");
        String displayName = fromId.substring(new String(strings[0]+"_"+strings[1]+"_").length());
        message.name = displayName;
        message.content = messageBody;
        messages.add(message);
        adapter.notifyDataSetChanged();
        lv.setSelection(messages.size() - 1);
        if(!MyApplication.INSTANCE.isHome()
                && TextUtils.equals(MeetingActivity.class.getSimpleName(), AppManager.getAppManager().currentActivity().getClass().getSimpleName())
                && !TextUtils.equals(H2HChat.getInstance().displayName,displayName))
        NotificationUtil.getInstance().showNotification(mContext, true, messageBody);
    }

    @Override
    protected boolean openEventBus() {
        return true;
    }
    @Subscribe
    public void updateMeetingEvent(Event.UpdateParticipants updateParticipants){
    }
}
