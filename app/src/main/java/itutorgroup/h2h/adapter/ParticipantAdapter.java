package itutorgroup.h2h.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itutorgroup.h2hconference.H2HPeer;
import com.mosai.utils.CommonAdapter;
import com.mosai.utils.CommonViewHolder;

import java.util.List;

import itutorgroup.h2h.R;
import itutorgroup.h2h.bean.Participant;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年07月12日 17:03
 * 邮箱：nianbin@mosainet.com
 */
public class ParticipantAdapter extends CommonAdapter<Participant> implements View.OnClickListener{
    public ParticipantAdapter(Context context, List<Participant> listDatas, int layoutId) {
        super(context, listDatas, layoutId);
    }

    @Override
    protected void fillData(CommonViewHolder holder, final int position) {
        ImageView ivAudio = holder.getView(R.id.iv_audio);
        ImageView ivVideo = holder.getView(R.id.iv_video);
        ImageView ivWhiteboard = holder.getView(R.id.iv_whiteboard);
        TextView tvName = holder.getView(R.id.tv_name);
        final Participant participant = listDatas.get(position);
        if (participant.userRole == H2HPeer.UserRole.Host){
            tvName.setText(participant.displayName+ " (Host)");
        }else if (participant.userRole == H2HPeer.UserRole.Translator) {
            tvName.setText(participant.displayName);
        }else {
            tvName.setText(participant.displayName);
        }
        setSelectState(ivAudio, participant.isAudio);
        setSelectState(ivVideo, participant.isVideo);
        setSelectState(ivWhiteboard, participant.isWhiteboard);
        ivAudio.setTag(position);
        ivVideo.setTag(position);
        ivWhiteboard.setTag(position);
        ivAudio.setOnClickListener(this);
        ivVideo.setOnClickListener(this);
        ivWhiteboard.setOnClickListener(this);
    }
    private void setSelectState(ImageView iv,boolean select) {
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
        }
    }

    public interface ContactorOperateCallback{
        void operateAudio(int position,Participant participant);
        void operateVideo(int position,Participant participant);
        void operateWhiteboard(int position,Participant participant);
    }
}
