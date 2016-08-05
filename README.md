H2H Sample Android App uses H2H SDK
===

H2H Video Conference APIs
---

###WebRTC Library is a multithreading environment, but the H2HSDK and WebRTC API have dispatched the background threads to do what needs to be done. The UI/Clinet app should always call H2HConference SDK on Main/UI thread. ###


###How to Create a Meeting or Join a Meeting###

If you are a host, you need to be a logged in H2HUser to schedule a meeting. If you are an invitee, you should have the meetingId already, just join the meeting directly. 

####H2HUser Login and Signup API:
http://developer.liveh2h.com/account-management/####


####Create A Meeting REST API:####
```
URL:https://app.liveh2h.com/tutormeetweb/corpmeeting.do?action=createMeeting&json=
    {           "recordMeeting":true,            "subject":"Global warming",           "meetingSn":"",           "description":" Causes and effects of global warming.",           "groupList":[],           "emailList":["h2h1@sharklasers.com"],           "translatorList":[{"lang":"fr","email":"h2h3@sharklasers.com"}],          "meetingType":6,          "action":"createMeeting",          "location":"TC"  }
HTTP Method: POST
HTTP Header: 
apiToken:”apiToken you get from Login api”
Content-Type:application/json
```

####Join A Meeting REST API(No need apiToken):####
```
URL:https://app.liveh2h.com/tutormeetweb/corpmeeting.do?json={"meetingId”:”MEETING_ID”,”locale":"en","action":"joinMeeting","app":"meet","name”:”YOUR_NAME”,”email”:”YOUR_EMAIL”}
HTTP Method: POST
```

####If Create A Meeting API or Join A Meeting API call is successful, you should get the JSON response:####
```
{  
   "returnCode":0,
   "referer":"web1.liveh2h.com",
   "meetingPassword":"",
   "serverURL":"https://meet1.liveh2h.com",
   "origin":"TME",
   "meetingUri":"JTdCJTIydXNlcl9pZCUyMiUzQSUyMnBHNEpGYSUyRk9mUVlMT0hHUVBiJTJGU240MnRLaGFpJTIyJTJDJTIybWVldGluZ19pZCUyMiUzQSUyMjE3OTAyMTU3MiUyMiUyQyUyMm9yaWdpbiUyMiUzQSUyMlRNRSUyMiU3RA==",
   "meetingId":"179021572",
   "meetingURL":"https://meet1.liveh2h.com/launcher.html?p=JTdCJTIydXNlcl9pZCUyMiUzQSUyMnBHNEpGYSUyRk9mUVlMT0hHUVBiJTJGU240MnRLaGFpJTIyJTJDJTIybWVldGluZ19pZCUyMiUzQSUyMjE3OTAyMTU3MiUyMiUyQyUyMm9yaWdpbiUyMiUzQSUyMlRNRSUyMiU3RA==&b=true"
}
```
####Pass origin, serverURL and userToken to the H2HSDK####



```
final H2HModel model = H2HModel.getInstance();
model.getLaunchParameters(origin, serverURL, userToken, new H2HCallback() {
	@Override
	public void onCompleted(Exception ex, H2HCallBackStatus status) {
		if (status == H2HCallBackStatus.H2HCallBackStatusOK){
			H2HConference.getInstance().connect(MainActivity.this, new H2HCallback() {
				@Override
				public void onCompleted(Exception ex, H2HCallBackStatus status) {
					if (ex != null){
						Log.e("App Level",ex.getMessage());
					}else{
						if (status == H2HCallBackStatus.H2HCallBackStatusOK){
							Log.d("App Level","Launch a meeting");
							Intent intent = new Intent(MainActivity.this, VideoConferenceActivity.class);
							startActivity(intent);
						}
					}
				}
			});

			H2HChat.getInstance().connect(MainActivity.this, new H2HCallback() {
				@Override
				public void onCompleted(Exception ex, H2HCallBackStatus status) {
					if (ex != null){
						Log.e("App Level",ex.getMessage());
					}else{
						if (status == H2HCallBackStatus.H2HCallBackStatusOK){
						}else {
							Log.d("App Level","something went wrong");
						}
					}
				}
			});
		}else if(status == H2HCallBackStatus.H2HCallBackStatusFail){
			Toast.makeText(MainActivity.this,"Unable to join meeting",Toast.LENGTH_SHORT).show();
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
		if (ex != null){
			Log.e("App Level",ex.getMessage());
		}else{
			if (status == H2HCallBackStatus.H2HCallBackStatusOK){
				Log.d("App Level","connect to server, ready to chat");
				Intent intent = new Intent(MainActivity.this, ChatActivity.class);
				startActivity(intent);
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

H2H Whiteboard URL
---
Currently, we use webview, we can get the url:
```
H2HModel.getInstance().getWhiteboardUrl();

webView.getSettings().setJavaScriptEnabled(true);
webView.setInitialScale(99);
MyBrowser myBrowser = new MyBrowser();
myBrowser.shouldOverrideUrlLoading(webView,whiteboardURL);
webView.setWebViewClient(myBrowser);

private class MyBrowser extends WebViewClient {
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	}

	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
		handler.proceed();
	}
}
```

