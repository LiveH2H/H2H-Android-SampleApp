package com.meetingroom.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itutorgroup.h2hconference.H2HConference;
import com.itutorgroup.h2hwhiteboard.H2HWhiteboardManager;
import com.meetingroom.bean.poll.SubmitAnswer;
import com.meetingroom.bean.poll.summary.Poll;
import com.meetingroom.bean.poll.summary.Questions;
import com.meetingroom.bean.poll.summary.Summary;
import com.meetingroom.constants.MeetingConstants;
import com.meetingroom.fragment.PollFragment;
import com.meetingroom.utils.MRUtils;
import com.meetingroom.view.CantScrollViewPager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itutorgroup.h2h.R;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年09月30日 10:41
 * 邮箱：nianbin@mosainet.com
 */
public class MRPollActivity extends MeetingRoomBaseActivity implements PollFragment.PollFragmentCallback{

    @BindView(R.id.tv_page)
    TextView tvPage;
    @BindView(R.id.viewPager)
    CantScrollViewPager viewPager;
    @BindView(R.id.iv_last_step)
    ImageView ivLast;
    @BindView(R.id.ll_next)
    LinearLayout llNext;
    @BindView(R.id.tv_endline)
    TextView tvEndline;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private MaterialDialog forceEndDialog;
    private MaterialDialog closeDialog;

//    private PollResult pollResult;
    private Poll poll;
    private int count;
    private int index;
    private List<PollFragment> fragments = new ArrayList<>();
    private Timer timer;
//    private Meeting meeting;
    @Override
    protected void initDatas() {

    }

    @Override
    protected int setContent() {
        return R.layout.activity_mrpoll;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
//        pollResult = (PollResult) getIntent().getSerializableExtra("pollResult");
        poll = (Poll) getIntent().getSerializableExtra("poll");
//        meeting = (Meeting) getIntent().getSerializableExtra("meeting");
        count = poll.getQuestions().size();
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        for (Questions question : poll.getQuestions()) {
            PollFragment fragment = PollFragment.newInstance(question);
            fragments.add(fragment);
        }
        tvPage.setText(String.format(Locale.getDefault(), "%d/%d", index + 1, count));
        ivLast.setVisibility(View.GONE);
    }

    @Override
    protected void addListener() {
        checkTime();
    }

    @OnClick({R.id.iv_back, R.id.ll_next, R.id.iv_last_step,R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                back();
                break;
            case R.id.ll_next:
                nextPage();
                break;
            case R.id.iv_last_step:
                lastPage();
                break;
            case R.id.btn_submit:
                if(btnSubmit.isSelected()){
                    nextPage();
                }
                break;
        }
    }

    @Override
    public void onPollFragmentCallback() {
        btnSubmit.setSelected(true);
    }

    private class TabAdapter extends FragmentPagerAdapter {
        TabAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragments.get(arg0);
        }

        @Override
        public int getCount() {
            return count;
        }
    }

    private void nextPage() {
        index += 1;
        checkIndex();
    }

    private void lastPage() {
        index -= 1;
        checkIndex();
    }

    private void checkIndex() {
        if (index >= count) {
            if (count == index) {
                //提交答案
                for (PollFragment fragment : fragments) {
                    if(fragment.adapter.singleSelect){
                        if (fragment.adapter.index == -1) {
                            index -= 1;
                            showToast(getString(R.string.poll_answer_unfinished));
                            return;
                        }
                    }else{
                        if (fragment.adapter.indexs.size() == 0) {
                            index -= 1;
                            showToast(getString(R.string.poll_answer_unfinished));
                            return;
                        }
                    }
                }
                submitPollAnswers();
            } else {
                index -= 1;
            }
            return;
        }
        String page = String.format(Locale.getDefault(), "%d/%d", index + 1, count);
        tvPage.setText(page);
        viewPager.setCurrentItem(index);
        if (index == 0) {
            ivLast.setVisibility(View.GONE);
            llNext.setVisibility(View.VISIBLE);
        } else if (index == count - 1) {
            ivLast.setVisibility(View.VISIBLE);
            llNext.setVisibility(View.VISIBLE);
        } else {
            ivLast.setVisibility(View.VISIBLE);
            llNext.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        if (forceEndDialog!=null) {
            forceEndDialog.dismiss();
        }
        if (closeDialog!=null) {
            closeDialog.dismiss();
        }
        new Handler(context.getMainLooper()).removeCallbacksAndMessages(null);
    }

    long dTime;
    long cTime;
    private void checkTime() {
        dTime =  poll.getEndTime()- MRUtils.getCurrentTimeMillis();
        if(dTime<=0){
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
        String result = simpleDateFormat.format(dTime);
        tvEndline.setText(result);
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                cTime += 1000;
                if (cTime <= dTime) {
                    new Handler(context.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
                            String result = simpleDateFormat.format(dTime-cTime);
                            tvEndline.setText(result);
                        }
                    });
                } else {
                    timer.cancel();
                    new Handler(context.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            forceEndDialog = new MaterialDialog(context);
                            forceEndDialog.setMessage(getString(R.string.deadline))
                                    .setCanceledOnTouchOutside(false)
                                    .setPositiveButton(getString(R.string.ok), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            forceEndDialog.dismiss();
                                           finishCheckTempIndex();
                                        }
                                    })
                                    .setNegativeButton(null, null);
                            forceEndDialog.show();
                        }
                    });


                }
            }
        };
        timer.schedule(task, new Date(System.currentTimeMillis()), 1000);
    }

    private void submitPollAnswers() {
        List<SubmitAnswer> submitAnswers = new ArrayList<>();
        for(PollFragment pollFragment : fragments){
            SubmitAnswer submitAnswer = new SubmitAnswer();
            submitAnswer.questionId = pollFragment.question.getQuestionId();
            if(pollFragment.adapter.singleSelect){
                submitAnswer.answers = new String[1];
                submitAnswer.answers[0] = pollFragment.optionses.get(pollFragment.adapter.index).getOptionId();
            }else{
                submitAnswer.answers = new String[pollFragment.adapter.indexs.size()];
                for(int i=0;i<pollFragment.adapter.indexs.size();i++){
                    submitAnswer.answers[i] = pollFragment.optionses.get(i).getOptionId();
                }
            }
            submitAnswers.add(submitAnswer);
        }
//        AppAction.submitMRPoll(context, H2HConference.getInstance().getMeetingId(),poll.getPollId(),submitAnswers, new HttpResponseHandler(context,Summary.class) {
//            @Override
//            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
//                LogUtils.e("submit poll success");
//                Summary summary = (Summary) response;
////                Intent intent = new Intent(context,PollResultActivity.class);
////                summary.getPoll().setEndTime(poll.getEndTime());
////                intent.putExtra("summary",summary);
////                intent.putExtra(MeetingConstants.tempIndex,getIntent().getIntExtra(MeetingConstants.tempIndex,-1));
////                startActivityForResult(intent,0);
//                index-=1;
//                H2HWhiteboardManager.getInstance().updatePolls(summary.getPoll());
//                finishCheckTempIndex();
////                finish();
//            }
//
//            @Override
//            public void onResponeseFail(int statusCode, HttpResponse response, String responseString) {
//                super.onResponeseFail(statusCode, response, responseString);
//                dismissLoadingDialog();
//                index-=1;
//            }
//        });
    }

    @Override
    protected boolean isDialogTheme() {
        return true;
    }

    @Override
    public void onBackPressed() {
       back();
    }
    private void back(){
        if (closeDialog==null) {
            closeDialog = new MaterialDialog(context);
            closeDialog.setMessage(getString(R.string.close_poll_tips))
                    .setPositiveButton(getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            closeDialog.dismiss();
                            finishCheckTempIndex();
                        }
                    }).setNegativeButton(getString(R.string.cancel), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeDialog.dismiss();
                }
            });
        }
        closeDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finishCheckTempIndex();
    }
    private void finishCheckTempIndex(){
        Intent intent = new Intent();
        intent.putExtra(MeetingConstants.tempIndex,getIntent().getIntExtra(MeetingConstants.tempIndex,-1));
        setResult(RESULT_OK,intent);
        finish();
    }
}
