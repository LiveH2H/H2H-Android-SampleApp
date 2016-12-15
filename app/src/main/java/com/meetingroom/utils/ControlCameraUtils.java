package com.meetingroom.utils;

import android.content.Context;

import com.meetingroom.view.ActionSheetDialog;

import itutorgroup.h2h.R;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年08月18日 19:05
 * 邮箱：nianbin@mosainet.com
 */
public class ControlCameraUtils {
    private ActionSheetDialog asdStop,asdFront,asdBack;
    private Context context;
    public CameraState cameraState;
    private void initAsd(){
        asdStop = new ActionSheetDialog(context).builder()
                .setCanceledOnTouchOutside(false)
                .addSheetItem(context.getString(R.string.front_camera), ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which, String srcItem) {
                                stopOperate();
                            }

                        })
                .addSheetItem(context.getString(R.string.back_camera), ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which, String srcItem) {
                                backOperate();
                            }

                        });
        asdFront = new ActionSheetDialog(context).builder()
                .setCanceledOnTouchOutside(false)
                .addSheetItem(context.getString(R.string.front_camera), ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which, String srcItem) {
                                frontOperate();
                            }

                        })
                .addSheetItem(context.getString(R.string.camera_stop), ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which, String srcItem) {
                                stopOperate();
                            }

                        });
        asdBack = new ActionSheetDialog(context).builder()
                .setCanceledOnTouchOutside(false)
                .addSheetItem(context.getString(R.string.back_camera), ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which, String srcItem) {
                                backOperate();
                            }

                        })
                .addSheetItem(context.getString(R.string.camera_stop), ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which, String srcItem) {
                                stopOperate();
                            }

                        });
    }
    public ControlCameraUtils (Context context) {
        context = context.getApplicationContext();
        initAsd();

    }
    public enum CameraState{
        stop,front,back;
    }
    public void showDialog(Object object){
        if(cameraState== CameraState.stop){
            asdStop.show();
        }else if(cameraState== CameraState.back){
            asdBack.show();
        }else if(cameraState== CameraState.front){
            asdFront.show();
        }
    }
    private void stopOperate(){

    }
    private void frontOperate(){

    }
    private void backOperate(){

    }
    public void checkCameraState(Object object){

    }
}
