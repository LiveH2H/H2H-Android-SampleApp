package com.meetingroom.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itutorgroup.h2hconference.H2HPeer;
import com.meetingroom.bean.Participant;
import com.meetingroom.utils.CommonAdapter;
import com.meetingroom.utils.CommonViewHolder;
import com.meetingroom.utils.MRUtils;

import java.util.List;

import itutorgroup.h2h.R;


/**
 * 描述:
 * 作者：znb
 * 时间：2016年07月12日 17:03
 * 邮箱：nianbin@mosainet.com
 */
public class ParticipantAdapter extends CommonAdapter<Participant> implements View.OnClickListener{
    public ParticipantAdapter(Context context, List<Participant> listDatas) {
        super(context, listDatas, R.layout.item_listformat_participant);
    }

    @Override
    protected void fillData(CommonViewHolder holder, final int position) {
        ImageView ivAudio = holder.getView(R.id.iv_audio);
        ImageView ivVideo = holder.getView(R.id.iv_video);
        ImageView ivWhiteboard = holder.getView(R.id.iv_whiteboard);
        ImageView ivRaiseHand = holder.getView(R.id.iv_raise_hand);
        TextView tvName = holder.getView(R.id.tv_name);
        final Participant participant = listDatas.get(position);
        ivRaiseHand.setVisibility(participant.isRaisingHand?View.VISIBLE:View.GONE);
        tvName.setText(participant.displayName+(!MRUtils.isHost(participant.displayName)?"":" (host)"));
        setSelectState(ivAudio, participant.isAudio);
        setSelectState(ivVideo, participant.isVideo);
        setSelectState(ivWhiteboard, participant.isWhiteboard);
        ivAudio.setTag(position);
        ivVideo.setTag(position);
        ivWhiteboard.setTag(position);
        ivRaiseHand.setTag(position);
        ivAudio.setOnClickListener(this);
        ivVideo.setOnClickListener(this);
        ivWhiteboard.setOnClickListener(this);
        ivRaiseHand.setOnClickListener(this);
        ivRaiseHand.setVisibility(participant.isRaisingHand?View.VISIBLE:View.GONE);
        ivAudio.setVisibility(participant.userRole== H2HPeer.UserRole.Translator?View.GONE:View.VISIBLE);
//        ivWhiteboard.setVisibility(participant.userRole== H2HPeer.UserRole.Translator?View.GONE:View.VISIBLE);
        ivVideo.setVisibility(participant.userRole== H2HPeer.UserRole.Translator?View.GONE:View.VISIBLE);
    }
    private void setSelectState(ImageView iv, boolean select) {
        iv.setSelected(select);
    }
    private ContactorOperateCallback contactorOperateCallback;

    public void setContactorOperateCallback(ContactorOperateCallback contactorOperateCallback) {
        this.contactorOperateCallback = contactorOperateCallback;
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        int id = view.getId();
        if(id==R.id.iv_audio){
            if(contactorOperateCallback!=null)
                contactorOperateCallback.operateAudio(position,listDatas.get(position));
        }else if(id==R.id.iv_video){
            if(contactorOperateCallback!=null)
                contactorOperateCallback.operateVideo(position,listDatas.get(position));
        }else if(id==R.id.iv_whiteboard){
            if(contactorOperateCallback!=null)
                contactorOperateCallback.operateWhiteboard(position,listDatas.get(position));
        }else if(id==R.id.iv_raise_hand){
            listDatas.get(position).isRaisingHand=false;
            notifyDataSetChanged();
            if(contactorOperateCallback!=null)
                contactorOperateCallback.operateRaiseHand(position,listDatas.get(position));
        }
    }
    public void setDatas(List<Participant> listDatas){
        this.listDatas.clear();
        this.listDatas.addAll(listDatas);
        notifyDataSetChanged();
    }
    public interface ContactorOperateCallback{
        void operateAudio(int position, Participant participant);
        void operateVideo(int position, Participant participant);
        void operateWhiteboard(int position, Participant participant);
        void operateRaiseHand(int position, Participant participant);
    }
}
