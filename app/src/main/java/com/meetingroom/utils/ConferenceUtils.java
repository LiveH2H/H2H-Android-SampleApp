package com.meetingroom.utils;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.itutorgroup.h2hconference.H2HConference;
import com.itutorgroup.h2hconference.H2HPeer;
import com.itutorgroup.h2hmodel.H2HModel;
import com.meetingroom.bean.MeetingState;
import com.meetingroom.callback.WebinarHostCallback;
import com.meetingroom.view.MarqueeTextView;
import com.mosai.utils.DensityUtil;

import org.litepal.crud.DataSupport;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import itutorgroup.h2h.R;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年08月09日 14:28
 * 邮箱：nianbin@mosainet.com
 */
public class ConferenceUtils {
    private static final int dash = 5;
    private static final int tv_name_margintop = 10;
    private static int getLocalHeight(int base) {
        return 100 / base + 100 % base;
    }
    public static int transformHeight(Context context,int heightPercent,boolean isFullScreen){
        if(isFullScreen){
            return (int) (Tools.getAtyHeight(context) * 0.01*heightPercent);
        }else{
            return transformHeight(context,heightPercent);
        }
    }
    public static int transformHeight(Context context,int heightPercent) {
        int totalHeight = (int) (Tools.getAtyHeight(context) - context.getResources().getDimension(R.dimen.titleheight) - (SystemUtil.isTablet(context)?0:context.getResources().getDimension(R.dimen.bottomheight)));
        return (int) (heightPercent * 0.01 * totalHeight);
    }
    public static int transformWidth(Context context,int widthPercent) {
        return (int) (widthPercent * 0.01 * Tools.getAtyWidth(context));
    }

    private static boolean isHost(String peerId){
        for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
            if(TextUtils.equals(peerId,h2HPeer.getId())){
                if(h2HPeer.userRole() == H2HPeer.UserRole.Host){
                   return true;
                }
            }
        }
        return false;
    }
    private static String getName(String peerId){
        return peerId;
    }
    public static void turnOnVideo() {
        if (H2HModel.getInstance().getMeetingType()== H2HModel.H2H_MEETINGTYPE.H2H_BROADCAST) {
            return;
        }
        H2HConference.getInstance().turnOnVideoForUser(H2HModel.getInstance().getRealDisplayName());
    }
    public static void turnOffVideo() {
        if (H2HModel.getInstance().getMeetingType()== H2HModel.H2H_MEETINGTYPE.H2H_BROADCAST) {
            return;
        }
        H2HConference.getInstance().turnOffVideoForUser(H2HModel.getInstance().getRealDisplayName());
    }

    public static void turnOnAudio() {
        if (H2HModel.getInstance().getMeetingType()== H2HModel.H2H_MEETINGTYPE.H2H_BROADCAST) {
            return;
        }
        H2HConference.getInstance().turnOnAudioForUser(H2HModel.getInstance().getRealDisplayName());
    }
    public static void turnOffAudio() {
        if (H2HModel.getInstance().getMeetingType()== H2HModel.H2H_MEETINGTYPE.H2H_BROADCAST) {
            return;
        }
        H2HConference.getInstance().turnOffAudioForUser(H2HModel.getInstance().getRealDisplayName());
    }
    public static void updateVideoViewLayoutByAdding(H2HConference conference) {
        if (conference.getRemoteRenders().size() == 0) {
            if (conference.getLocalRender() != null) {
                updateAysn(conference.getLocalRender(), 37, 75, 25, 25, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
            }
        } else if (conference.getRemoteRenders().size() == 1) {
            try {
                updateAysn(conference.getLocalRender(), 37, 75, 25, 25, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
//                if(!isClosing){
//                    updateAysn(conference.getRemoteRenders().get(peerId), 0, 0, 100, 75, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
//                }else{
                    Set<String> keys = conference.getRemoteRenders().keySet();
                    for (String key : keys) {
                        updateAysn(conference.getRemoteRenders().get(key), 0, 0, 100, 75, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                    }
//                }
                //Update the size and location of each video view
            } catch (Exception e) {
                Log.e("onAddRemoteStream", e.getMessage());
            }
        } else {
            try {
                //Update the size and location of each video view
                updateAysn(conference.getLocalRender(), 0, 0, 100, 75, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                Set<String> keys = conference.getRemoteRenders().keySet();
                int i = 0;
                for (String key : keys) {
                    updateAysn(conference.getRemoteRenders().get(key), 25 * i, 75, 25, 25, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                    i++;
                }
            } catch (Exception e) {
                Log.e("onAddRemoteStream", e.getMessage());
            }
        }
    }
    private static void addIvAvatar(Context mContext,AbsoluteLayout altAvatars,int x,int y,int width,int height,boolean isFullScreen){
        ImageView ivSmall = new ImageView(mContext);
        ivSmall.setImageResource(R.drawable.ic_blank_avatar);
        ivSmall.setScaleType(ImageView.ScaleType.FIT_XY);
        AbsoluteLayout.LayoutParams ivSmallParams = new AbsoluteLayout.LayoutParams(ConferenceUtils.transformWidth(mContext,width)
                + dash, ConferenceUtils.transformHeight(mContext,height,isFullScreen) + dash, ConferenceUtils.transformWidth(mContext,x),
                ConferenceUtils.transformHeight(mContext,y,isFullScreen));
        ivSmall.setLayoutParams(ivSmallParams);
        altAvatars.addView(ivSmall);
    }
    public static void updateAvatars(Context mContext, H2HConference conference, AbsoluteLayout altAvatars, boolean isScreen) {
        altAvatars.removeAllViews();
        if (conference.getRemoteRenders().size() == 0) {
            for (H2HPeer h2HPeer : conference.getPeersIgnoreTranslators()) {
                    if (TextUtils.equals(conference.getLocalUserName(), h2HPeer.getId())) {
                        if (!h2HPeer.isVideoOn()) {
                            addIvAvatar(mContext,altAvatars,37,75,25,25,isScreen);
                        }
                    }
                }

        } else if (conference.getRemoteRenders().size() == 1) {
            for (H2HPeer h2HPeer1 : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                if (TextUtils.equals(H2HModel.getInstance().getRealDisplayName(), h2HPeer1.getId())) {
                    if (!h2HPeer1.isVideoOn()) {
                        addIvAvatar(mContext,altAvatars,37,75,25,25,isScreen);
                    }
                } else {
                    if (!h2HPeer1.isVideoOn()) {
                        addIvAvatar(mContext,altAvatars,0,0,100,75,isScreen);
                    }
                }
            }
        } else {
            for (H2HPeer h2HPeer : conference.getPeersIgnoreTranslators()) {
                    if (TextUtils.equals(conference.getLocalUserName(), h2HPeer.getId())) {
                        if (!h2HPeer.isVideoOn()) {
                            addIvAvatar(mContext,altAvatars,0,0,100,75,isScreen);
                        }
                    }
                }
            Set<String> keys = conference.getRemoteRenders().keySet();
            int i = 0;
            for (String key : keys) {
                for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                    if (TextUtils.equals(key, h2HPeer.getId())) {
                        if (!h2HPeer.isVideoOn()) {
                            addIvAvatar(mContext,altAvatars,25 * i,75,25,25,isScreen);
                        }
                    }
                }
                i++;
            }
        }
    }

    private static void addRlName(Context mContext, AbsoluteLayout altNames, String peerId, int x, int y, int width, int height, boolean isFullScreen, boolean hideLayout) {
        RelativeLayout rl = new RelativeLayout(mContext);
        AbsoluteLayout.LayoutParams rlParams = new AbsoluteLayout.LayoutParams(ConferenceUtils.transformWidth(mContext,width)
                + dash, ConferenceUtils.transformHeight(mContext, height, isFullScreen) + dash, ConferenceUtils.transformWidth(mContext, x),
                ConferenceUtils.transformHeight(mContext, y, isFullScreen));
        rl.setLayoutParams(rlParams);
        addTvName(mContext,rl,getName(peerId));
        altNames.addView(rl);
        rl.setVisibility(hideLayout?View.GONE:View.VISIBLE);
    }
    private static void addTvName(Context context,RelativeLayout relativeLayout,String name){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams llParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margint = DensityUtil.dip2px(context,5);
        llParams.setMargins(margint,margint,margint,margint);
        llParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        linearLayout.setBackgroundResource(R.drawable.shape_participant_name);
        linearLayout.setBaselineAligned(true);
        relativeLayout.addView(linearLayout,llParams);

        MarqueeTextView tvName = new MarqueeTextView(context);
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvParams.gravity = Gravity.CENTER;
        tvName.setLayoutParams(tvParams);
        tvName.setText(name);
        tvName.setTextColor(isHost(name)?Color.RED:Color.WHITE);
        tvName.setSingleLine(true);
        tvName.setMaxEms(10);
        tvName.setGravity(Gravity.CENTER);
        int padding = DensityUtil.dip2px(context,5);
        tvName.setPadding(padding,padding,padding,padding);
        linearLayout.addView(tvName);
        addMediaState(context,relativeLayout,name);

    }
    private static void addMediaState(Context context,RelativeLayout relativeLayout,String name){
        if(TextUtils.equals(name, H2HConference.getInstance().getLocalUserName())){
            return;
        }
        ImageView ivMic = new ImageView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        int margin = DensityUtil.dip2px(context,5);
        layoutParams.setMargins(0,0,margin,margin);
        ivMic.setImageResource(R.drawable.selector_operate_audio);
        ivMic.setLayoutParams(layoutParams);
        relativeLayout.addView(ivMic);
        for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
            if(TextUtils.equals(h2HPeer.getId(),name)){
                ivMic.setSelected(h2HPeer.isAudioOn());
            }
        }

    }
    public static void updateNames(Context mContext, H2HConference conference, AbsoluteLayout altNames, boolean isFullSceen, boolean hideLayout) {
        altNames.removeAllViews();
        if (conference.getRemoteRenders().size() == 0) {
            addRlName(mContext,altNames,conference.getLocalUserName(),37,75,25,25,isFullSceen,hideLayout);
        } else if (conference.getRemoteRenders().size() == 1) {
            addRlName(mContext,altNames,conference.getLocalUserName(),37,75,25,25,isFullSceen,hideLayout);
            for (H2HPeer h2HPeer : conference.getPeersIgnoreTranslators()) {
                if(!TextUtils.equals(conference.getLocalUserName(),h2HPeer.getId())){
                    addRlName(mContext,altNames,h2HPeer.getId(),0,0,100,75,isFullSceen,hideLayout);
                }
            }
        } else {
            addRlName(mContext,altNames,conference.getLocalUserName(),0,0,100,75,isFullSceen,hideLayout);
            Set<String> keys = conference.getRemoteRenders().keySet();
            int i = 0;
            for (String key : keys) {
                addRlName(mContext,altNames,key,25*i,75,25,25,isFullSceen,hideLayout);
                i++;
            }
        }
    }
    public static void updateNames2(Context mContext, H2HConference conference, AbsoluteLayout altNames, boolean isFullSceen, boolean hideLayout) {
        altNames.removeAllViews();
        int remoteRenders = conference.getRemoteRenders().size();
        if (remoteRenders == 0) {
            if (conference.getLocalRender() != null) {
                addRlName(mContext,altNames,conference.getLocalUserName(),0, 0, 100, 100,isFullSceen,hideLayout);
            }
        } else if (remoteRenders <= 2) {
            try {
                int localHeight = getLocalHeight(remoteRenders + 1);
                int average = (100 - localHeight) / remoteRenders;
                addRlName(mContext,altNames,conference.getLocalUserName(),0, 0, 100, localHeight,isFullSceen,hideLayout);
                Set<String> keys = conference.getRemoteRenders().keySet();
                int i = 0;
                for (String key : keys) {
                    addRlName(mContext,altNames,key,0, localHeight + average * i, 100, average,isFullSceen,hideLayout);
                    i++;
                }
                //Update the size and location of each video view
            } catch (Exception e) {
                Log.e("onAddRemoteStream", e.getMessage());
            }
        } else {
            try {
                if (remoteRenders % 2 == 0) {
                    int base = remoteRenders / 2 + 1;
                    int localHeight = getLocalHeight(base);
                    int average = (100 - localHeight) / (base - 1);
                    //Update the size and location of each video view
                    addRlName(mContext,altNames,conference.getLocalUserName(),0, 0, 100, localHeight,isFullSceen,hideLayout);
                    Set<String> keys = conference.getRemoteRenders().keySet();
                    int i = 0;
                    for (String key : keys) {
                        if (i % 2 == 0) {
                            addRlName(mContext,altNames,key,0, localHeight + average * (i / 2), 50, average,isFullSceen,hideLayout);
                        } else {
                            addRlName(mContext,altNames,key,50, localHeight + average * ((i - 1) / 2), 50, average,isFullSceen,hideLayout);
                        }
                        i++;
                    }
                } else {
                    int base = (remoteRenders + 1) / 2;
                    int localHeight = getLocalHeight(base);
                    int average = (100 - localHeight) / (base - 1);
                    addRlName(mContext,altNames,conference.getLocalUserName(),0, 0, 50, localHeight,isFullSceen,hideLayout);
                    Set<String> keys = conference.getRemoteRenders().keySet();
                    int i = -1;
                    for (String key : keys) {
                        if (i == -1) {
                            addRlName(mContext,altNames,key,50, 0, 50, localHeight,isFullSceen,hideLayout);
                        } else {
                            if (i % 2 == 0) {
                                addRlName(mContext,altNames,key, 0, localHeight + average * (i / 2), 50, average,isFullSceen,hideLayout);
                            } else {
                                addRlName(mContext,altNames,key, 50, localHeight + average * ((i - 1) / 2), 50, average,isFullSceen,hideLayout);
                            }
                        }
                        i++;
                    }
                }

            } catch (Exception e) {
                Log.e("onAddRemoteStream", e.getMessage());
            }
        }
    }

    private static boolean isTranslator(H2HPeer h2HPeer) {
        return h2HPeer.userRole() == H2HPeer.UserRole.Translator;
    }
    public static void updateAvatars2(Context mContext, H2HConference conference, AbsoluteLayout altAvatars, boolean isFullScreen) {
        altAvatars.removeAllViews();
        int remoteRenders = conference.getRemoteRenders().size();
        if (remoteRenders == 0) {
            if (conference.getLocalRender() != null) {
                for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                    if (TextUtils.equals(conference.getLocalUserName(), h2HPeer.getId())) {
                        if (!h2HPeer.isVideoOn()) {
                            addIvAvatar(mContext, altAvatars, 0, 0, 100, 100,isFullScreen);
                        }
                    }
                }
            }
        } else if (remoteRenders <= 2) {
            try {
                int localHeight = getLocalHeight(remoteRenders+1);
                int average = (100 - localHeight) / remoteRenders;
                for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                    if (TextUtils.equals(conference.getLocalUserName(), h2HPeer.getId())) {
                        if (!h2HPeer.isVideoOn()) {
                            addIvAvatar(mContext, altAvatars, 0, 0, 100, localHeight,isFullScreen);
                        }
                    }
                }
                Set<String> keys = conference.getRemoteRenders().keySet();
                int i = 0;
                for (String key : keys) {
                    for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                        if (TextUtils.equals(key, h2HPeer.getId())) {
                            if (!h2HPeer.isVideoOn()) {
                                addIvAvatar(mContext, altAvatars, 0, localHeight + average * i, 100, average,isFullScreen);
                            }
                        }
                    }
                    i++;
                }
                //Update the size and location of each video view
            } catch (Exception e) {
                Log.e("onAddRemoteStream", e.getMessage());
            }
        } else {
            try {
                if (remoteRenders % 2 == 0) {
                    int base = remoteRenders / 2 + 1;
                    int localHeight = getLocalHeight(base);
                    int average = (100 - localHeight) / (base - 1);
                    //Update the size and location of each video view
                    for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                        if (TextUtils.equals(conference.getLocalUserName(), h2HPeer.getId())) {
                            if (!h2HPeer.isVideoOn()) {
                                addIvAvatar(mContext, altAvatars, 0, 0, 100, localHeight,isFullScreen);
                            }
                        }
                    }
                    Set<String> keys = conference.getRemoteRenders().keySet();
                    int i = 0;
                    for (String key : keys) {
                        if (i % 2 == 0) {
                            for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                                if (TextUtils.equals(key, h2HPeer.getId())) {
                                    if (!h2HPeer.isVideoOn()) {
                                        addIvAvatar(mContext, altAvatars, 0, localHeight + average * i / 2, 50, average,isFullScreen);
                                    }
                                }
                            }
                        } else {
                            for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                                if (TextUtils.equals(key, h2HPeer.getId())) {
                                    if (!h2HPeer.isVideoOn()) {
                                        addIvAvatar(mContext, altAvatars, 50, localHeight + average * ((i - 1) / 2), 50, average,isFullScreen);
                                    }
                                }
                            }
                        }
                        i++;
                    }
                } else {
                    int base = (remoteRenders + 1) / 2;
                    int localHeight = getLocalHeight(base);
                    int average = (100 - localHeight) / (base - 1);
                    for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                        if (TextUtils.equals(conference.getLocalUserName(), h2HPeer.getId())) {
                            if (!h2HPeer.isVideoOn()) {
                                addIvAvatar(mContext, altAvatars, 0, 0, 50, localHeight,isFullScreen);
                            }
                        }
                    }
                    Set<String> keys = conference.getRemoteRenders().keySet();
                    int i = -1;
                    for (String key : keys) {
                        if (i == -1) {
                            for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                                if (TextUtils.equals(key, h2HPeer.getId())) {
                                    if (!h2HPeer.isVideoOn()) {
                                        addIvAvatar(mContext, altAvatars, 50, 0, 50, localHeight,isFullScreen);
                                    }
                                }
                            }
                        } else {
                            if (i % 2 == 0) {
                                for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                                    if (TextUtils.equals(key, h2HPeer.getId())) {
                                        if (!h2HPeer.isVideoOn()) {
                                            addIvAvatar(mContext, altAvatars, 0, localHeight + average * i / 2, 50, average,isFullScreen);
                                        }
                                    }
                                }
                            } else {
                                for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                                    if (TextUtils.equals(key, h2HPeer.getId())) {
                                        if (!h2HPeer.isVideoOn()) {
                                            addIvAvatar(mContext, altAvatars, 50, localHeight + average * ((i - 1) / 2), 50, average,isFullScreen);
                                        }
                                    }
                                }
                            }
                        }
                        i++;
                    }
                }

            } catch (Exception e) {
                Log.e("onAddRemoteStream", e.getMessage());
            }
        }
    }
    public static void updateVideoViewLayoutByAdding2(H2HConference conference) {
        int remoteRenders = conference.getRemoteRenders().size();
        if (remoteRenders == 0) {
            if (conference.getLocalRender() != null) {
                updateAysn(conference.getLocalRender(), 0, 0, 100, 100, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
            }
        } else if (remoteRenders <= 2) {
            try {
                int localHeight = getLocalHeight(remoteRenders + 1);
                int average = (100 - localHeight) / remoteRenders;
                updateAysn(conference.getLocalRender(), 0, 0, 100, localHeight, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                Set<String> keys = conference.getRemoteRenders().keySet();
                int i = 0;
                for (String key : keys) {
                    updateAysn(conference.getRemoteRenders().get(key), 0, localHeight + average * i, 100, average, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                    i++;
                }
                //Update the size and location of each video view
            } catch (Exception e) {
                Log.e("onAddRemoteStream", e.getMessage());
            }
        } else {
            try {
                if (remoteRenders % 2 == 0) {
                    int base = remoteRenders / 2 + 1;
                    int localHeight = getLocalHeight(base);
                    int average = (100 - localHeight) / (base - 1);
                    //Update the size and location of each video view
                    updateAysn(conference.getLocalRender(), 0, 0, 100, localHeight, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                    Set<String> keys = conference.getRemoteRenders().keySet();
                    int i = 0;
                    for (String key : keys) {
                        if (i % 2 == 0) {
                            updateAysn(conference.getRemoteRenders().get(key), 0, localHeight + average * (i / 2), 50, average, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                        } else {
                            updateAysn(conference.getRemoteRenders().get(key), 50, localHeight + average * ((i - 1) / 2), 50, average, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                        }
                        i++;
                    }
                } else {
                    int base = (remoteRenders + 1) / 2;
                    int localHeight = getLocalHeight(base);
                    int average = (100 - localHeight) / (base - 1);
                    updateAysn(conference.getLocalRender(), 0, 0, 50, localHeight, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                    Set<String> keys = conference.getRemoteRenders().keySet();
                    int i = -1;
                    for (String key : keys) {
                        if (i == -1) {
                            updateAysn(conference.getRemoteRenders().get(key), 50, 0, 50, localHeight, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                        } else {
                            if (i % 2 == 0) {
                                updateAysn(conference.getRemoteRenders().get(key), 0, localHeight + average * (i / 2), 50, average, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                            } else {
                                updateAysn(conference.getRemoteRenders().get(key), 50, localHeight + average * ((i - 1) / 2), 50, average, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                            }
                        }
                        i++;
                    }
                }

            } catch (Exception e) {
                Log.e("onAddRemoteStream", e.getMessage());
            }
        }
    }
    public static void updateVideoViewLayoutByAdding4(H2HConference conference) {
        int remoteRenders = conference.getRemoteRenders().size();
        if (remoteRenders == 0) {
            if (conference.getLocalRender() != null) {
                updateAysn(conference.getLocalRender(), 0, 0, 100, 100, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
            }
        } else if (remoteRenders == 1) {
            try {
                updateAysn(conference.getLocalRender(), 0, 0, 50, 100, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                Set<String> keys = conference.getRemoteRenders().keySet();
                int i = 0;
                for (String key : keys) {
                    updateAysn(conference.getRemoteRenders().get(key), 50, 0, 50, 100, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                    i++;
                }
                //Update the size and location of each video view
            } catch (Exception e) {
                Log.e("onAddRemoteStream", e.getMessage());
            }
        } else {
            try {
                if (remoteRenders % 2 == 0) {
                    int base = remoteRenders / 2 + 1;
                    int localHeight = getLocalHeight(base);
                    int average = (100 - localHeight) / (base - 1);
                    //Update the size and location of each video view
                    updateAysn(conference.getLocalRender(), 0, 0, 100, localHeight, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                    Set<String> keys = conference.getRemoteRenders().keySet();
                    int i = 0;
                    for (String key : keys) {
                        if (i % 2 == 0) {
                            updateAysn(conference.getRemoteRenders().get(key), 0, localHeight + average * (i / 2), 50, average, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                        } else {
                            updateAysn(conference.getRemoteRenders().get(key), 50, localHeight + average * ((i - 1) / 2), 50, average, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                        }
                        i++;
                    }
                } else {
                    int base = (remoteRenders + 1) / 2;
                    int localHeight = getLocalHeight(base);
                    int average = (100 - localHeight) / (base - 1);
                    updateAysn(conference.getLocalRender(), 0, 0, 50, localHeight, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                    Set<String> keys = conference.getRemoteRenders().keySet();
                    int i = -1;
                    for (String key : keys) {
                        if (i == -1) {
                            updateAysn(conference.getRemoteRenders().get(key), 50, 0, 50, localHeight, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                        } else {
                            if (i % 2 == 0) {
                                updateAysn(conference.getRemoteRenders().get(key), 0, localHeight + average * (i / 2), 50, average, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                            } else {
                                updateAysn(conference.getRemoteRenders().get(key), 50, localHeight + average * ((i - 1) / 2), 50, average, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                            }
                        }
                        i++;
                    }
                }

            } catch (Exception e) {
                Log.e("onAddRemoteStream", e.getMessage());
            }
        }
    }
    public static void updateAvatars4(Context mContext, H2HConference conference, AbsoluteLayout altAvatars, boolean isFullScreen) {
        altAvatars.removeAllViews();
        int remoteRenders = conference.getRemoteRenders().size();
        if (remoteRenders == 0) {
            if (conference.getLocalRender() != null) {
                for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                    if (TextUtils.equals(conference.getLocalUserName(), h2HPeer.getId())) {
                        if (!h2HPeer.isVideoOn()) {
                            addIvAvatar(mContext, altAvatars, 0, 0, 100, 100,isFullScreen);
                        }
                    }
                }
            }
        } else if (remoteRenders == 1) {
            try {
                for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                    if (TextUtils.equals(conference.getLocalUserName(), h2HPeer.getId())) {
                        if (!h2HPeer.isVideoOn()) {
                            addIvAvatar(mContext, altAvatars, 0, 0, 50, 100,isFullScreen);
                        }
                    }
                }
                Set<String> keys = conference.getRemoteRenders().keySet();
                int i = 0;
                for (String key : keys) {
                    for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                        if (TextUtils.equals(key, h2HPeer.getId())) {
                            if (!h2HPeer.isVideoOn()) {
                                addIvAvatar(mContext, altAvatars, 50, 0, 50, 100,isFullScreen);
                            }
                        }
                    }
                    i++;
                }
                //Update the size and location of each video view
            } catch (Exception e) {
                Log.e("onAddRemoteStream", e.getMessage());
            }
        } else {
            try {
                if (remoteRenders % 2 == 0) {
                    int base = remoteRenders / 2 + 1;
                    int localHeight = getLocalHeight(base);
                    int average = (100 - localHeight) / (base - 1);
                    //Update the size and location of each video view
                    for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                        if (TextUtils.equals(conference.getLocalUserName(), h2HPeer.getId())) {
                            if (!h2HPeer.isVideoOn()) {
                                addIvAvatar(mContext, altAvatars, 0, 0, 100, localHeight,isFullScreen);
                            }
                        }
                    }
                    Set<String> keys = conference.getRemoteRenders().keySet();
                    int i = 0;
                    for (String key : keys) {
                        if (i % 2 == 0) {
                            for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                                if (TextUtils.equals(key, h2HPeer.getId())) {
                                    if (!h2HPeer.isVideoOn()) {
                                        addIvAvatar(mContext, altAvatars, 0, localHeight + average * i / 2, 50, average,isFullScreen);
                                    }
                                }
                            }
                        } else {
                            for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                                if (TextUtils.equals(key, h2HPeer.getId())) {
                                    if (!h2HPeer.isVideoOn()) {
                                        addIvAvatar(mContext, altAvatars, 50, localHeight + average * ((i - 1) / 2), 50, average,isFullScreen);
                                    }
                                }
                            }
                        }
                        i++;
                    }
                } else {
                    int base = (remoteRenders + 1) / 2;
                    int localHeight = getLocalHeight(base);
                    int average = (100 - localHeight) / (base - 1);
                    for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                        if (TextUtils.equals(conference.getLocalUserName(), h2HPeer.getId())) {
                            if (!h2HPeer.isVideoOn()) {
                                addIvAvatar(mContext, altAvatars, 0, 0, 50, localHeight,isFullScreen);
                            }
                        }
                    }
                    Set<String> keys = conference.getRemoteRenders().keySet();
                    int i = -1;
                    for (String key : keys) {
                        if (i == -1) {
                            for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                                if (TextUtils.equals(key, h2HPeer.getId())) {
                                    if (!h2HPeer.isVideoOn()) {
                                        addIvAvatar(mContext, altAvatars, 50, 0, 50, localHeight,isFullScreen);
                                    }
                                }
                            }
                        } else {
                            if (i % 2 == 0) {
                                for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                                    if (TextUtils.equals(key, h2HPeer.getId())) {
                                        if (!h2HPeer.isVideoOn()) {
                                            addIvAvatar(mContext, altAvatars, 0, localHeight + average * i / 2, 50, average,isFullScreen);
                                        }
                                    }
                                }
                            } else {
                                for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                                    if (TextUtils.equals(key, h2HPeer.getId())) {
                                        if (!h2HPeer.isVideoOn()) {
                                            addIvAvatar(mContext, altAvatars, 50, localHeight + average * ((i - 1) / 2), 50, average,isFullScreen);
                                        }
                                    }
                                }
                            }
                        }
                        i++;
                    }
                }

            } catch (Exception e) {
                Log.e("onAddRemoteStream", e.getMessage());
            }
        }
    }
    public static void updateNames4(Context mContext, H2HConference conference, AbsoluteLayout altNames, boolean isFullSceen, boolean hideLayout) {
        altNames.removeAllViews();
        int remoteRenders = conference.getRemoteRenders().size();
        if (remoteRenders == 0) {
            if (conference.getLocalRender() != null) {
                addRlName(mContext,altNames,conference.getLocalUserName(),0, 0, 100, 100,isFullSceen,hideLayout);
            }
        } else if (remoteRenders == 1) {
            try {
                addRlName(mContext,altNames,conference.getLocalUserName(),0, 0, 50, 100,isFullSceen,hideLayout);
                Set<String> keys = conference.getRemoteRenders().keySet();
                int i = 0;
                for (String key : keys) {
                    addRlName(mContext,altNames,key,50, 0, 50, 100,isFullSceen,hideLayout);
                    i++;
                }
                //Update the size and location of each video view
            } catch (Exception e) {
                Log.e("onAddRemoteStream", e.getMessage());
            }
        } else {
            try {
                if (remoteRenders % 2 == 0) {
                    int base = remoteRenders / 2 + 1;
                    int localHeight = getLocalHeight(base);
                    int average = (100 - localHeight) / (base - 1);
                    //Update the size and location of each video view
                    addRlName(mContext,altNames,conference.getLocalUserName(),0, 0, 100, localHeight,isFullSceen,hideLayout);
                    Set<String> keys = conference.getRemoteRenders().keySet();
                    int i = 0;
                    for (String key : keys) {
                        if (i % 2 == 0) {
                            addRlName(mContext,altNames,key,0, localHeight + average * (i / 2), 50, average,isFullSceen,hideLayout);
                        } else {
                            addRlName(mContext,altNames,key,50, localHeight + average * ((i - 1) / 2), 50, average,isFullSceen,hideLayout);
                        }
                        i++;
                    }
                } else {
                    int base = (remoteRenders + 1) / 2;
                    int localHeight = getLocalHeight(base);
                    int average = (100 - localHeight) / (base - 1);
                    addRlName(mContext,altNames,conference.getLocalUserName(),0, 0, 50, localHeight,isFullSceen,hideLayout);
                    Set<String> keys = conference.getRemoteRenders().keySet();
                    int i = -1;
                    for (String key : keys) {
                        if (i == -1) {
                            addRlName(mContext,altNames,key,50, 0, 50, localHeight,isFullSceen,hideLayout);
                        } else {
                            if (i % 2 == 0) {
                                addRlName(mContext,altNames,key, 0, localHeight + average * (i / 2), 50, average,isFullSceen,hideLayout);
                            } else {
                                addRlName(mContext,altNames,key, 50, localHeight + average * ((i - 1) / 2), 50, average,isFullSceen,hideLayout);
                            }
                        }
                        i++;
                    }
                }

            } catch (Exception e) {
                Log.e("onAddRemoteStream", e.getMessage());
            }
        }
    }
    private static void updateLocalVideo3(H2HConference conference){
        updateAysn(conference.getLocalRender(), 0, 0, 100, 75, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
    }
    public static void updateVideoViewLayoutByAdding3(H2HConference conference) {
        if (conference.getRemoteRenders().size() == 0) {
            if (conference.getLocalRender() != null) {
                updateAysn(conference.getLocalRender(), 0, 0, 100, 100, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
            }
        } else if (conference.getRemoteRenders().size() == 1) {
            try {
                updateLocalVideo3(conference);
                Set<String> keys = conference.getRemoteRenders().keySet();
                for (String key : keys) {
                    updateAysn(conference.getRemoteRenders().get(key), 37, 75, 25, 25, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                }
//                }
                //Update the size and location of each video view
            } catch (Exception e) {
                Log.e("onAddRemoteStream", e.getMessage());
            }
        } else {
            try {
                //Update the size and location of each video view
                updateLocalVideo3(conference);
                Set<String> keys = conference.getRemoteRenders().keySet();
                int i = 0;
                for (String key : keys) {
                    updateAysn(conference.getRemoteRenders().get(key), 25 * i, 75, 25, 25, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                    i++;
                }
            } catch (Exception e) {
                Log.e("onAddRemoteStream", e.getMessage());
            }
        }
    }
    private static void updateLocalAvatar3(Context mContext,AbsoluteLayout altAvatars,boolean isScreen){
        addIvAvatar(mContext,altAvatars,0,0,100,75,isScreen);
    }
    public static void updateAvatars3(Context mContext, H2HConference conference, AbsoluteLayout altAvatars, boolean isScreen) {
        altAvatars.removeAllViews();
        if (conference.getRemoteRenders().size() == 0) {
            for (H2HPeer h2HPeer : conference.getPeersIgnoreTranslators()) {
                if (TextUtils.equals(conference.getLocalUserName(), h2HPeer.getId())) {
                    if (!h2HPeer.isVideoOn()) {
                        addIvAvatar(mContext,altAvatars,0,0,100,100,isScreen);
                    }
                }
            }

        } else if (conference.getRemoteRenders().size() == 1) {
            for (H2HPeer h2HPeer1 : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                if (TextUtils.equals(H2HModel.getInstance().getRealDisplayName(), h2HPeer1.getId())) {
                    if (!h2HPeer1.isVideoOn()) {
                        updateLocalAvatar3(mContext,altAvatars,isScreen);
                    }
                } else {
                    if (!h2HPeer1.isVideoOn()) {
                        addIvAvatar(mContext,altAvatars,37,75,25,25,isScreen);
                    }
                }
            }
        } else {
            for (H2HPeer h2HPeer : conference.getPeersIgnoreTranslators()) {
                if (TextUtils.equals(conference.getLocalUserName(), h2HPeer.getId())) {
                    if (!h2HPeer.isVideoOn()) {
                        updateLocalAvatar3(mContext,altAvatars,isScreen);
                    }
                }
            }
            Set<String> keys = conference.getRemoteRenders().keySet();
            int i = 0;
            for (String key : keys) {
                for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                    if (TextUtils.equals(key, h2HPeer.getId())) {
                        if (!h2HPeer.isVideoOn()) {
                            addIvAvatar(mContext,altAvatars,25 * i,75,25,25,isScreen);
                        }
                    }
                }
                i++;
            }
        }
    }
    private static void updateLocalNames3(Context mContext, AbsoluteLayout altNames, H2HConference conference, boolean isFullSceen, boolean hideLayout){
        addRlName(mContext,altNames,conference.getLocalUserName(),0,0,100,75,isFullSceen,hideLayout);
    }
    public static void updateNames3(Context mContext, H2HConference conference, AbsoluteLayout altNames, boolean isFullSceen, boolean hideLayout) {
        altNames.removeAllViews();
        if (conference.getRemoteRenders().size() == 0) {
            addRlName(mContext,altNames,conference.getLocalUserName(),0,0,100,100,isFullSceen,hideLayout);
        } else if (conference.getRemoteRenders().size() == 1) {
            updateLocalNames3(mContext,altNames,conference,isFullSceen,hideLayout);
            for (H2HPeer h2HPeer : conference.getPeersIgnoreTranslators()) {
                if(!TextUtils.equals(conference.getLocalUserName(),h2HPeer.getId())){
                    addRlName(mContext,altNames,h2HPeer.getId(),37,75,25,25,isFullSceen,hideLayout);
                }
            }
        } else {
            updateLocalNames3(mContext,altNames,conference,isFullSceen,hideLayout);
            Set<String> keys = conference.getRemoteRenders().keySet();
            int i = 0;
            for (String key : keys) {
                addRlName(mContext,altNames,key,25*i,75,25,25,isFullSceen,hideLayout);
                i++;
            }
        }
    }

    private static void updateAysn(final VideoRenderer.Callbacks renderer, final int x, final int y, final int width, final int height, final VideoRendererGui.ScalingType scalingType, final boolean mirror) {
        LogUtils.i("updateAysn()");
        new Thread(new Runnable() {
            @Override
            public void run() {
                VideoRendererGui.update(renderer, x, y, width, height, scalingType, mirror);
            }
        }).start();

    }
    //result is "true" should be return while calling
    public static boolean filterWebinar(Context context, GLSurfaceView videoView, AbsoluteLayout altAvatars, AbsoluteLayout altNames, boolean isFullScreen, boolean hideLayout, WebinarHostCallback callback) {
        if (H2HModel.getInstance().getMeetingType() == H2HModel.H2H_MEETINGTYPE.H2H_WEBINAR){
            altAvatars.removeAllViews();
            altNames.removeAllViews();
            if(MRUtils.isHost()){
                updateAysn(H2HConference.getInstance().getLocalRender(), 0, 0, 100, 100, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                    if (TextUtils.equals(H2HConference.getInstance().getLocalUserName(), h2HPeer.getId())) {
                        if (!h2HPeer.isVideoOn()) {
                            addIvAvatar(context, altAvatars, 0, 0, 100, 100, isFullScreen);
                        }
                    }
                }
            }else{
                if (H2HConference.getInstance().getRemoteRenders().size() ==1 ){
                    VideoRendererGui.remove(H2HConference.getInstance().getLocalRender());
                    String key = H2HConference.getInstance().getRemoteRenders().keySet().iterator().next();
                    updateAysn(H2HConference.getInstance().getRemoteRenders().get(key), 0, 0, 100, 100, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true);
                    for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
                        if (TextUtils.equals(key, h2HPeer.getId())) {
                            if (!h2HPeer.isVideoOn()) {
                                addIvAvatar(context, altAvatars, 0, 0, 100, 100, isFullScreen);
                            }
                        }
                    }
                    addRlName(context, altNames, key, 0, 0, 100, 100, isFullScreen, hideLayout);
                    callback.callback(true);
                } else {
                    videoView.requestRender();
                    if (callback != null) {
                        callback.callback(false);
                    }
                }
            }
            return true;
        }
        return false;
    }
    public static void saveMeetingState() {
        MeetingState meetingState = new MeetingState();
        meetingState.setMeetingId(H2HConference.getInstance().getMeetingId());
        meetingState.setUserId(H2HConference.getInstance().getLocalUserName());
        for (H2HPeer h2HPeer : H2HConference.getInstance().getPeersIgnoreTranslators()) {
            if (TextUtils.equals(H2HConference.getInstance().getLocalUserName(), h2HPeer.getId())) {
                meetingState.setAudioOn(h2HPeer.isAudioOn());
                meetingState.setVideoOn(h2HPeer.isVideoOn());
                break;
            }
        }
        meetingState.save();
    }
    public static void getMeetingState() {
        List<MeetingState> meetingStates = DataSupport.findAll(MeetingState.class);
        if (meetingStates != null && !meetingStates.isEmpty()) {
            Collections.reverse(meetingStates);
            for (MeetingState ms : meetingStates) {
                if (TextUtils.equals(ms.getMeetingId(), H2HConference.getInstance().getMeetingId())
                        && TextUtils.equals(ms.getUserId(), H2HConference.getInstance().getLocalUserName())) {
                    if (!ms.isVideoOn()) {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ConferenceUtils.turnOffVideo();
                            }
                        }, 100);

                    }
                    if (!ms.isAudioOn()) {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ConferenceUtils.turnOffAudio();
                            }
                        }, 100);
                    }
                    break;
                }
            }
        }
    }
}
