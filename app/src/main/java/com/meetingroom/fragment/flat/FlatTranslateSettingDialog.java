package com.meetingroom.fragment.flat;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.itutorgroup.h2hconference.H2HConference;
import com.itutorgroup.h2hconference.H2HPeer;
import com.itutorgroup.h2hconference.H2HTranslator;
import com.kyleduo.switchbutton.SwitchButton;
import com.meetingroom.adapter.TranslateLanguageAdapter;
import com.meetingroom.bean.ServerConfig;
import com.meetingroom.utils.Tools;
import com.meetingroom.view.RelativeDialog;

import java.util.ArrayList;
import java.util.List;

import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.ViewUtil;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年08月23日 16:21
 * 邮箱：nianbin@mosainet.com
 */
public class FlatTranslateSettingDialog extends RelativeDialog {
    public String lastLanguageCode;
    private ListView lvLanguage;
    private SwitchButton switchPresenterVoice;
    private TranslateLanguageAdapter adapter;
    private List<H2HTranslator> languages = new ArrayList<>();
    private ServerConfig serverConfig;
    private H2HConference conference;

    public FlatTranslateSettingDialog(Context context, ServerConfig serverConfig){
        super(context);
        this.serverConfig = serverConfig;
        init();
        initConfig();
        addListener();
    }

    private void addListener() {
        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastLanguageCode = languages.get(adapter.position).getLanguageCode();
                String translatorName = languages.get(adapter.position).getTranslatorName();
                if (adapter.position == languages.size() - 1) {
                    H2HConference.getInstance().switchToHostChannel();
                } else {
                    H2HConference.getInstance().switchIntoLanguage(lastLanguageCode, translatorName);
                }
                dismiss();
            }
        });
    }

    private void init(){
        conference = H2HConference.getInstance();
        width = Tools.getAtyWidth(mContext)/3;
        height = Tools.getAtyHeight(mContext)/3*2;
        view = View.inflate(mContext, R.layout.activity_translate_setting,null);
        switchPresenterVoice = ViewUtil.findViewById(view,R.id.switchPresenterVoice);
        lvLanguage = ViewUtil.findViewById(view,R.id.lv_language);
        adapter = new TranslateLanguageAdapter(mContext, R.layout.item_listformat_translate_language, languages);
        lvLanguage.setAdapter(adapter);
    }

    @Override
    public void show() {
        getLanguages();
        super.show();
    }

    private void getLanguages(){
        languages.clear();
        for(H2HPeer h2HPeer: H2HConference.getInstance().getPeers()){
            if(h2HPeer.userRole()== H2HPeer.UserRole.Translator){
                languages.add(h2HPeer.getTranslator());
            }
        }
        languages.add(new H2HTranslator(mContext.getString(R.string.language_default),"default"));
        if (TextUtils.isEmpty(lastLanguageCode)) {
            adapter.position = languages.size() - 1;
        } else {
            for (int i = 0; i < languages.size(); i++) {
                if (TextUtils.equals(languages.get(i).getLanguageCode(), lastLanguageCode)) {
                    adapter.position = i;
                    break;
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
