package com.meetingroom.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.itutorgroup.h2hwhiteboard.H2HWhiteboardManager;
import com.meetingroom.bean.poll.summary.Options;
import com.meetingroom.bean.poll.summary.Questions;
import com.meetingroom.bean.poll.summary.Summary;
import com.meetingroom.constants.MeetingConstants;
import com.meetingroom.utils.StringUtil;
import com.meetingroom.view.NiceSpinner;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itutorgroup.h2h.R;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年10月06日 14:36
 * 邮箱：nianbin@mosainet.com
 */
public class PollResultActivity extends MeetingRoomBaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.chart)
    PieChart mChart;
    @BindView(R.id.spinner)
    NiceSpinner niceSpinner;
    @BindView(R.id.tv_endline)
    TextView tvEndline;

    private Summary summary;


    @Override
    protected void initDatas() {
        summary = (Summary) getIntent().getSerializableExtra("summary");
        List<String> questions = new ArrayList<>();
        for(Questions question: summary.getPoll().getQuestions()){
            questions.add(question.getText());
        }

        niceSpinner.attachDataSource(questions);
        niceSpinner.setVisibility(summary.getPoll().getQuestions().size()==1?View.GONE:View.VISIBLE);
        niceSpinner.setSelectedIndex(0);
        setData(0);
        // TODO: Fixme
//        H2HWhiteboardManager.getInstance().updatePolls(summary.getPoll());
    }

    @Override
    protected int setContent() {
        return R.layout.activity_mrpoll_result;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(25f);
        mChart.setTransparentCircleRadius(27f);
        mChart.setDrawCenterText(false);
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        mChart.setDrawEntryLabels(false);
        mChart.setExtraOffsets(5, 10, 5, 5);
    }

    @Override
    protected void addListener() {
         niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 setData(position);
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });
        checkTime();
    }

    @Override
    public void onBackPressed() {
       back();
    }

    private void back(){
        Intent intent = new Intent();
        intent.putExtra(MeetingConstants.tempIndex,getIntent().getIntExtra(MeetingConstants.tempIndex,-1));
        setResult(RESULT_OK,intent);
        finish();
    }
    @OnClick(R.id.iv_back)
    public void onClick() {
        back();
    }
    private void setData(int position) {
        tvDescription.setText(summary.getPoll().getQuestions().size()!=1?summary.getPoll().getDescription():summary.getPoll().getQuestions().get(position).getText());
        tvTitle.setText(summary.getPoll().getTitle());
        tvTitle.setVisibility(TextUtils.isEmpty(tvTitle.getText().toString())?View.GONE:View.VISIBLE);
        tvDescription.setVisibility(TextUtils.isEmpty(tvDescription.getText().toString())?View.GONE:View.VISIBLE);


        List<Options> optionses = summary.getPoll().getQuestions().get(position).getOptions();
        int count = optionses.size();

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        int totalPickedCount=0;
        int maxLength=0;
        for(int j = 0; j < count; j++){
            totalPickedCount+=optionses.get(j).getTotalPickedCount();
            Options options = optionses.get(j);
            int length = String.format("%s%s(%d)",new DecimalFormat("###,###,##0.0").format(options.getPercentage()),"%",options.getTotalPickedCount()).length();
            if(length>maxLength){
                maxLength = length;
            }
        }

        for (int i = 0; i < count; i++) {
            Options options = optionses.get(i);
            int length = String.format("%s%s(%d)",new DecimalFormat("###,###,##0.0").format(options.getPercentage()),"%",options.getTotalPickedCount()).length();
            String result = String.format("%s%s(%d)%s  %s",new DecimalFormat("###,###,##0.0").format(options.getPercentage()),"%",options.getTotalPickedCount(),StringUtil.addTab(maxLength,length),options.getText());
//            String result = String.format("%d/%d %s",options.getTotalPickedCount(),totalPickedCount,options.getText());
            entries.add(new PieEntry((float) options.getPercentage(), result));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(4f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();
        for(Options options :summary.getPoll().getQuestions().get(position).getOptions()){
            colors.add(Color.parseColor(options.getColor()));
        }
        dataSet.setColors(colors);
        dataSet.setValueLinePart1OffsetPercentage(10.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setDrawValues(false);
        mChart.setData(data);
        mChart.highlightValues(null);
        mChart.invalidate();
    }

    @Override
    protected boolean isDialogTheme() {
        return true;
    }


    long dTime;
    long cTime;
    private Timer timer;
    private void checkTime() {
        dTime =  summary.getPoll().getEndTime()-System.currentTimeMillis();
        if(dTime<=0){
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
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
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                            String result = simpleDateFormat.format(dTime-cTime);
                            tvEndline.setText(result);
                        }
                    });
                } else {
                    timer.cancel();
                    new Handler(context.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            tvEndline.setText(getString(R.string.already_over));
                        }
                    });
                }
            }
        };
        timer.schedule(task, new Date(System.currentTimeMillis()), 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        new Handler(context.getMainLooper()).removeCallbacksAndMessages(null);
    }
}
