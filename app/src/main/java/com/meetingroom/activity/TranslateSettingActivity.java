package com.meetingroom.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.itutorgroup.h2hconference.H2HConference;
import com.itutorgroup.h2hconference.H2HPeer;
import com.itutorgroup.h2hconference.H2HTranslator;
import com.kyleduo.switchbutton.SwitchButton;
import com.meetingroom.adapter.TranslateLanguageAdapter;
import com.meetingroom.constants.MeetingConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itutorgroup.h2h.R;

public class TranslateSettingActivity extends MeetingRoomBaseActivity {
    @BindView(R.id.lv_language)
    ListView lvLanguage;
    @BindView(R.id.switchPresenterVoice)
    SwitchButton switchPresenterVoice;

    private TranslateLanguageAdapter adapter;
    private List<H2HTranslator> translators = new ArrayList<>();
    private String languageCode;
    @Override
    protected void initDatas() {
//        translators = Arrays.asList(context.getResources().getStringArray(R.array.translate_language));
        adapter = new TranslateLanguageAdapter(context, R.layout.item_listformat_translate_language, translators);
        lvLanguage.setAdapter(adapter);
        getLanguages();
    }

    @Override
    protected int setContent() {
        return R.layout.activity_translate_setting;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }
    @Override
    protected void addListener() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageCode = translators.get(adapter.position).getLanguageCode();
                if (adapter.position == translators.size() - 1) {
                    H2HConference.getInstance().switchToHostChannel();
                } else {
                    H2HConference.getInstance().switchIntoLanguage(translators.get(adapter.position).getLanguageCode(), translators.get(adapter.position).getTranslatorName());
                }
                Intent intent = new Intent();
                intent.putExtra(MeetingConstants.languageCode, languageCode);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
    private void getLanguages(){
        translators.clear();
        for(H2HPeer h2HPeer: H2HConference.getInstance().getPeers()){
            if(h2HPeer.userRole()== H2HPeer.UserRole.Translator){
                translators.add(h2HPeer.getTranslator());
            }
        }
        translators.add(new H2HTranslator(getString(R.string.language_default),"default"));
        languageCode = getIntent().getStringExtra(MeetingConstants.languageCode);
        if (TextUtils.isEmpty(languageCode)) {
            adapter.position = translators.size() - 1;
        } else {
            for (int i = 0; i < translators.size(); i++) {
                if (TextUtils.equals(translators.get(i).getLanguageCode(), languageCode)) {
                    adapter.position = i;
                    break;
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
