

Step 1.

  Download Android Studio and SDK Tools | Android Studio

Step 2.

  Copy the following code into your app level build.gradle file, under dependencies:


	compile 'com.liveh2h:h2h-video-conference:1.0.37'
	compile 'io.pristine:libjingle:9694@aar'
	compile 'com.koushikdutta.async:androidasync:2.1.8'
Step 3.

   See how you can use LiveH2H on Android:

	Follow along with these detailed guides to learn set up your environment and start using H2H concepts.


H2H Sample Android App uses H2H SDK
===

How to Get H2H SDK
---
Copy the following code into your app level build.gradle file, under dependencies:
```
compile 'com.liveh2h:h2h-video-conference:1.0.37'
compile 'io.pristine:libjingle:9694@aar'
compile 'com.koushikdutta.async:androidasync:2.1.8'
```

H2H Video Conference APIs
---

###WebRTC Library is a multithreading environment, but the H2HSDK and WebRTC API have dispatched the background threads to do what needs to be done. The UI/Clinet app should always call H2HConference SDK on Main/UI thread. ###


###User Management###
####How to login####
```
H2HHttpRequest.getInstance().loginH2H(email, pwd, new H2HCallback() {
    @Override
    public void onCompleted(Exception ex, final H2HCallBackStatus status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();
                if (status == H2HCallBackStatus.H2HCallBackStatusOK) {
                    showToast("Login Success");
                } else {
                    showToast("Failed to Login");
                }
            }
        });
    }
});
```

####How to signup####
```
H2HHttpRequest.getInstance().signUpH2HUser(firstName, lastName, email, pwd, new H2HCallback() {

@Override
public void onCompleted(Exception ex, final H2HCallBackStatus status) {
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            dismissLoadingDialog();
            if (status == H2HCallBackStatus.H2HCallBackStatusOK) {
                showToast("User Sign Up Success");
            } else {
                showToast("Sign Up Failed");
            }
        }
    });
});
```

###Meetings###
If you are the host, you need to schedule a meeting or create an instant meeting. If you are an invitee, you should already have the meeting id, you need to join a meeting. Please note that you need to signin to H2H in order to schedule a meeting. 

####Schedule a Meeting####
```
H2HHttpRequest.getInstance().scheduleMeeting(param, new H2HScheduleMeetingCallback() {
    @Override
    public void onCompleted(final Exception ex, final H2HCallBackStatus status) {
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
                    showToast("Failed to schedule a meeting");
                }
            }
        });
    }
});
```
####Join a Meeting####

```
final H2HHttpRequest h2HHttpRequest = H2HHttpRequest.getInstance();
h2HHttpRequest.joinMeeting(meetingId, email, new H2HCallback() {
    @Override
    public void onCompleted(Exception ex, final H2HCallBackStatus status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (status == H2HCallBackStatus.H2HCallBackStatusOK) {
                launchMeeting(h2HHttpRequest.getOrigin(), h2HHttpRequest.getServerURL(), h2HHttpRequest.getUserToken());
                } else {
                    showToast("Meeting Not Found, Please Enter an Exiting Meeting id");
                }
                dismissLoadingDialog();
                cancelJoinMeeting();
            }
        });
    }
});
```

####Instant Meeting####
```
H2HConference.getInstance().connect(InstantMeetingActivity.this, new H2HCallback() {
    @Override
        public void onCompleted(Exception ex, H2HCallBackStatus status) {
        if (ex != null) {
            Log.e("App Level", ex.getMessage());
        } else {
            if (status == H2HCallBackStatus.H2HCallBackStatusOK) {
                Log.d("App Level", "Launch a Meeting");
            if(TextUtils.equals(AppManager.getAppManager().currentActivity().getClass().getSimpleName(),InstantMeetingActivity.class.getSimpleName())){
                    Intent intent = new Intent(InstantMeetingActivity.this, MeetingActivity.class);
                    ServerConfig serverConfig = new ServerConfig();
                    serverConfig.userToken = userToken;
                    intent.putExtra("serverConfig", serverConfig);
                    startActivityForResult(intent,0);
                }
            }
        }
    }
});
```

####Launch a Meeting: Pass origin, serverURL and userToken to the H2HSDK####
```
final H2HModel model = H2HModel.getInstance();
model.getLaunchParameters(origin, serverURL, userToken, this, new H2HCallback() {
    @Override
    public void onCompleted(Exception ex, H2HCallBackStatus status) {
        if (status == H2HCallBackStatus.H2HCallBackStatusOK) {
            H2HConference.getInstance().connect(InstantMeetingActivity.this, new H2HCallback() {
                @Override
                public void onCompleted(Exception ex, H2HCallBackStatus status) {
                    if (ex != null) {
                        Log.e("App Level", ex.getMessage());
                    } else {
                        if (status == H2HCallBackStatus.H2HCallBackStatusOK) {
                        Log.d("App Level", "Launch a Meeting");
                        if(TextUtils.equals(AppManager.getAppManager().currentActivity().getClass().getSimpleName(),InstantMeeting  Activity.class.getSimpleName())){
                                Intent intent = new Intent(InstantMeetingActivity.this, MeetingActivity.class);
                                ServerConfig serverConfig = new ServerConfig();
                                serverConfig.userToken = userToken;
                                intent.putExtra("serverConfig", serverConfig);
                                startActivityForResult(intent,0);
                            }
                        }
                    }
                }
            });
            H2HChat.getInstance().connect(InstantMeetingActivity.this, new H2HCallback() {
                @Override
                public void onCompleted(Exception ex, H2HCallBackStatus status) {
                    if (ex != null) {
                        Log.e("App Level", ex.getMessage());
                    } else {
                        if (status == H2HCallBackStatus.H2HCallBackStatusOK) {
                            Log.e("App Level", "start chat");
                        } else {
                            Log.e("App Level", "something went wrong");
                        }
                    }
                }
            });
        } else if (status == H2HCallBackStatus.H2HCallBackStatusFail) {
            Toast.makeText(InstantMeetingActivity.this, "Unable to join a meeting", Toast.LENGTH_SHORT).show();
        }
    }
});
```

In your Activity or Fragment that displays the videos, you should have a GLSurfaceView that contains Video Renders. First, you should set your GLSurfaceView as VideoRendererGui: VideoRendererGui.setView(videoView, null), then pass a H2HRTCListener to H2HConference.java to receive callbacks, finally init the WebRTC connection. 
```
videoView = (GLSurfaceView) findViewById(R.id.gl_surface);
VideoRendererGui.setView(videoView, null);
conference = H2HConference.getInstance();
conference.listener = new RTCListener();
conference.initRTCConnection();
```

If your connection is successful, you should receive H2HRTCListener callbacks. onAddRemoteStream is called when you established a real video streaming connection between a user, it can be yourself or other users. Therefore you should update your UI - the video renders accordingtly. In H2HConference.java, you can have access to localRender and remoteRenders, remoteRenders is a HashTable whose key is the userId. In VideoRendererGui.java:
```
public static void update(Callbacks renderer, int x, int y, int width, int height, VideoRendererGui.ScalingType scalingType, boolean mirror); 
```
is the method you can use to update the location and the shape of a video render. 
```
VideoRendererGui.update(conference.remoteRenders.get(userid), 0, 0, 100, 75, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, false);
VideoRendererGui.update(conference.localRender, 37, 75, 25, 25, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
```

Callback: onPeerConnectionClosed is called when someone is offline, you should remove the remote render which associates with the userId. 
```
VideoRendererGui.remove(conference.remoteRenders.get(userid));
```

Get Participants
```
conference = H2HConference.getInstance();
List<H2HPeer> peers =  conference.getPeers();
```

Toggle Video/Audio, if you are host, you can toggle other participants' video and audio; if you are parcicipant, you can only toggle your own. Please check your permission before you toggle 
```
if(!H2HModel.getInstance().getUserRole().equals("H") && !participant.isLocalUser()) {
    Log.e("App Level","You don't have permission to toggle this user");
    return;
}
H2HConference.getInstance().turnOffVideoForUser(userName);
```


H2H Chat APIs
---


Connect to the Server
```
H2HChat.getInstance().connect(context, new H2HCallback() {
    @Override
    public void onCompleted(Exception ex, H2HCallBackStatus status) {

        if (status == H2HCallBackStatus.H2HCallBackStatusOK) {
            Log.e("App Level", "start chat");
        } else {
            if (ex != null) {
                ex.printStackTrace();
            }
        }
    }
});
```


Receive Chat Message Notification
```
broadcastReceiver = new H2HBroadcastReceiver(){
	@Override
	public void onReceive(Context context, Intent intent) {
		//Receive a new message
		String message = intent.getStringExtra(H2HChatConstant.MESSAGE_BODY);
		String from    = intent.getStringExtra(H2HChatConstant.FROM);
		long timeStamp = intent.getLongExtra(H2HChatConstant.TIMESTAMP,System.currentTimeMillis()/1000);
		Toast.makeText(ChatActivity.this,message + " "+ from ,Toast.LENGTH_SHORT).show();

		//How to distinguish my own message or other people's message
		//Compare the real user id, the only way, don't relay on userId
		String realUserId = from.substring(from.lastIndexOf("/") + 1);
		if (chatClient.getRealUserId().equals(realUserId)){
			Toast.makeText(ChatActivity.this,"This is a message from myself",Toast.LENGTH_SHORT).show();
		}else {
			Toast.makeText(ChatActivity.this,"This is a message from other people",Toast.LENGTH_SHORT).show();
		}
	}
};
registerReceiver(broadcastReceiver,new IntentFilter(H2HChatConstant.RECEIVE_CHAT));
```

Receive User Connection Status Change Notification
```
userConnectionStatusReceiver = new H2HBroadcastReceiver(){
    @Override
    public void onReceive(Context context, Intent intent) {
        isConnected = intent.getBooleanExtra(H2HChatConstant.CONNECTION_STATUS,false);
        Toast.makeText(ChatActivity.this, "I am "+      (isConnected?"online":"offline"),Toast.LENGTH_SHORT).show();
    }
};
registerReceiver(userConnectionStatusReceiver,new IntentFilter(H2HChatConstant.CONNECTION_STATUS_CHANGE));
```

Receive User Status Change Notification
```
chatUserStatusReceiver = new H2HBroadcastReceiver(){
	@Override
	public void onReceive(Context context, Intent intent) {
		//A user status changed
		String name = intent.getStringExtra(H2HChatConstant.DISPLAY_NAME);
		Boolean isOnline = intent.getBooleanExtra(H2HChatConstant.IS_ONLINE,false);
		if (isOnline){
			Toast.makeText(ChatActivity.this,name+" joined",Toast.LENGTH_SHORT).show();
		}else {
			Toast.makeText(ChatActivity.this,name+" is offline",Toast.LENGTH_SHORT).show();
		}
	}
};
registerReceiver(chatUserStatusReceiver,new IntentFilter(H2HChatConstant.CHAT_USER_STATUS_CHANGE));
```

After register all the receivers, you must call:
```
H2HChat.getInstance().startToReceiveMessage();
```

Other Public Apis
```
public void sendTextToGroup(String text)
public void sendTextToUser(String text, H2HChatUser user)
public void leaveRoom()
public ArrayList<H2HChatMessage> getAllChatList()
public ArrayList<H2HChatUser> getAllChatUsers()
//If we have duplicated userNames, realUserId = userId_1/userId_2
public String getRealUserId()
public String getUserId()
public String getMeetingId()
public String getConferenceAddress()
public String getUserAddress()
public String getDomain()
public H2HChatUser getSelfChatUser()
public String getJid()

```

