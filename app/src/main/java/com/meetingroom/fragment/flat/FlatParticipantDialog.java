package com.meetingroom.fragment.flat;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.itutorgroup.h2hconference.H2HConference;
import com.itutorgroup.h2hconference.H2HPeer;
import com.itutorgroup.h2hmodel.H2HModel;
import com.meetingroom.adapter.ParticipantAdapter;
import com.meetingroom.bean.Participant;
import com.meetingroom.bean.ServerConfig;
import com.meetingroom.utils.MRUtils;
import com.meetingroom.utils.ToastUtils;
import com.meetingroom.utils.Tools;
import com.meetingroom.view.ActionSheetDialogForView;
import com.meetingroom.view.RelativeDialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.ViewUtil;

public class FlatParticipantDialog extends RelativeDialog {
	private H2HConference conference;
	private LinearLayout llInvite;
	private ListView lv;
	private RelativeLayout rlBottom;
	private ActionSheetDialogForView asdInvite, asdManage;

	private ServerConfig serverConfig;
	private List<Participant> participants = new ArrayList<>();
	private ParticipantAdapter adapter;
	private Set<String> raisePerson = new HashSet<>();
	private FlatParticipantPopupWindowCallback flatParticipantPopupWindowCallback;
	public FlatParticipantDialog(Context context, ServerConfig serverConfig, FlatParticipantPopupWindowCallback flatParticipantPopupWindowCallback){
		super(context);
		this.serverConfig = serverConfig;
		init();
		initConfig();
		this.flatParticipantPopupWindowCallback = flatParticipantPopupWindowCallback;
	}

	private void init(){
		conference = H2HConference.getInstance();
		width = Tools.getAtyWidth(mContext)/3;
		height = Tools.getAtyHeight(mContext)/3*2;
		view = View.inflate(mContext, R.layout.layout_popupwidow_participants,null);
		llInvite = ViewUtil.findViewById(view,R.id.ll_invite);
		lv = ViewUtil.findViewById(view, R.id.iv_participants);
		rlBottom = ViewUtil.findViewById(view,R.id.rl_bottom);
		adapter = new ParticipantAdapter(mContext, participants);
		lv.setAdapter(adapter);
		addListener();
	}

	private void addListener() {
		llInvite.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
//				((Activity)mContext).startActivityForResult(new Intent(mContext, InviteContactsActivity.class),0);
			}
		});
		adapter.setContactorOperateCallback(new ParticipantAdapter.ContactorOperateCallback() {
			@Override
			public void operateAudio(int position, Participant participant) {
				if (H2HModel.getInstance().getUserRole()!= H2HPeer.UserRole.Host && !participant.isLocalUser) {
					Log.e("App Level", "You don't have permission to toggle this user");
					ToastUtils.showToast(mContext, mContext.getString(R.string.forbit_permission));
					return;
				}
				if (participant.isAudio) {
					participant.turnOffAudio();
				} else {
					participant.turnOnAudio();
				}
			}

			@Override
			public void operateVideo(int position, Participant participant) {
				if (H2HModel.getInstance().getUserRole()!= H2HPeer.UserRole.Host && !participant.isLocalUser) {
					Log.e("App Level", "You don't have permission to toggle this user");
					ToastUtils.showToast(mContext, mContext.getString(R.string.forbit_permission));
					return;
				}
				if (participant.isVideo) {
					participant.turnOffVideo();
				} else {
					participant.turnOnVideo();
				}
			}

			@Override
			public void operateWhiteboard(int position, Participant participant) {
			}

			@Override
			public void operateRaiseHand(int position, Participant participant) {
				if(raisePerson.contains(participant.displayName)){
					raisePerson.remove(participant.displayName);
				}
				if(flatParticipantPopupWindowCallback!=null){
					flatParticipantPopupWindowCallback.isRaiseHandCallback(raisePerson.size()!=0);
				}
			}
		});
		view.findViewById(R.id.tv_copy_link).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//                ShareUtil.meetingRoomShareLinks(mContext);
			}
		});
	}

	private void checkPermission() {
		llInvite.setVisibility(View.VISIBLE);
		inviteOperate();
		if (MRUtils.isHost()) {
			manageOperate();
		}
	}

	private void inviteOperate() {
		asdInvite = new ActionSheetDialogForView(mContext,rlBottom)
				.builder()
				.addSheetItem(mContext.getString(R.string.invite_contact_byEmail), ActionSheetDialogForView.SheetItemColor.Black,
						new ActionSheetDialogForView.OnSheetItemClickListener() {
							@Override
							public void onClick(int which, String srcItem) {
							}

						})
				.addSheetItem(mContext.getString(R.string.send_sms), ActionSheetDialogForView.SheetItemColor.Black,
						new ActionSheetDialogForView.OnSheetItemClickListener() {
							@Override
							public void onClick(int which, String srcItem) {
							}

						});
		llInvite.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				asdInvite.show();
			}
		});
		rlBottom.removeAllViews();
		rlBottom.addView(asdInvite.view);
	}

	private void manageOperate() {
		asdManage = new ActionSheetDialogForView(mContext,rlBottom)
				.builder()
				.addSheetItem(mContext.getString(R.string.make_presenter), ActionSheetDialogForView.SheetItemColor.Black,
						new ActionSheetDialogForView.OnSheetItemClickListener() {
							@Override
							public void onClick(int which, String srcItem) {
							}

						})
				.addSheetItem(mContext.getString(R.string.mute_audio), ActionSheetDialogForView.SheetItemColor.Black,
						new ActionSheetDialogForView.OnSheetItemClickListener() {
							@Override
							public void onClick(int which, String srcItem) {
							}

						})
				.addSheetItem(mContext.getString(R.string.turnoff_video), ActionSheetDialogForView.SheetItemColor.Black,
						new ActionSheetDialogForView.OnSheetItemClickListener() {
							@Override
							public void onClick(int which, String srcItem) {
							}

						})
				.addSheetItem(mContext.getString(R.string.disable_audio), ActionSheetDialogForView.SheetItemColor.Black,
						new ActionSheetDialogForView.OnSheetItemClickListener() {
							@Override
							public void onClick(int which, String srcItem) {
							}

						})
				.addSheetItem(mContext.getString(R.string.disable_video), ActionSheetDialogForView.SheetItemColor.Black,
						new ActionSheetDialogForView.OnSheetItemClickListener() {
							@Override
							public void onClick(int which, String srcItem) {
							}

						})
				.addSheetItem(mContext.getString(R.string.disable_liveboard_tool), ActionSheetDialogForView.SheetItemColor.Black,
						new ActionSheetDialogForView.OnSheetItemClickListener() {
							@Override
							public void onClick(int which, String srcItem) {
							}

						})
				.addSheetItem(mContext.getString(R.string.remove_participant), ActionSheetDialogForView.SheetItemColor.Black,
						new ActionSheetDialogForView.OnSheetItemClickListener() {
							@Override
							public void onClick(int which, String srcItem) {
							}

						});

	}

	public void raiseHand(H2HPeer h2HPeer) {
		raisePerson.add(h2HPeer.getId());
		loadDatas();
	}

	public void loadDatas() {
		if (conference.getClass() == null) {
			return;
		}
		List<H2HPeer> peers = conference.getPeers();
		participants.clear();

		for (H2HPeer peer : peers) {
			Participant participant = new Participant();
			participant.displayName = peer.getId();
			participant.isVideo = peer.isVideoOn();
			participant.isAudio = peer.isAudioOn();
			participant.isLocalUser = peer.isLocalUser();
			participant.userRole = peer.userRole();
			if (participant.userRole == H2HPeer.UserRole.Translator){
				participant.displayName = peer.getTranslator().getLanguage();
			}
			//判断是否有人举手
			if(MRUtils.isHost()){
				Iterator<String> iterator = raisePerson.iterator();
				while (iterator.hasNext()){
					String id = iterator.next();
					if(TextUtils.equals(participant.displayName,id)){
						participant.isRaisingHand = true;
						break;
					}
				}
			}

			participants.add(participant);
		}
		adapter.notifyDataSetChanged();
	}

	public void reset(){
		rlBottom.removeAllViews();
		rlBottom.setVisibility(View.GONE);
	}
	public interface FlatParticipantPopupWindowCallback{
		void isRaiseHandCallback(boolean hasRaiseHand);
	}
}