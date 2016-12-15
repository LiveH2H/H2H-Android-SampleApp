package com.meetingroom.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.itutorgroup.h2hmodel.H2HModel;
import com.meetingroom.bean.poll.summary.Options;
import com.meetingroom.bean.poll.summary.Poll;
import com.meetingroom.bean.poll.summary.Questions;
import com.meetingroom.bean.poll.summary.Summary;
import com.meetingroom.utils.MRUtils;
import com.meetingroom.utils.StringUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.ViewUtil;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年10月07日 17:21
 * 邮箱：nianbin@mosainet.com
 */
public class PollView extends FrameLayout {
    private TextView tvTitle, tvDescription, tvEndline, tvNotPoll;
    private PieChart mChart;
    private NiceSpinner niceSpinner;
    private Poll poll;
    private View view, llRefresh;

    public PollView setPoll(Poll poll) {
        this.poll = poll;
        setData(0);
        return this;
    }

    public Poll getPoll() {
        return poll;
    }

    public PollView(Context context) {
        this(context, null, null);
    }

    public PollView(Context context, Poll poll, RefreshListener refreshListener) {
        super(context);
        this.refreshListener = refreshListener;
        this.poll = poll;
        initView();
        tvTitle.setText(poll.getTitle());
        tvDescription.setText(poll.getDescription());
        tvTitle.setVisibility(TextUtils.isEmpty(poll.getTitle())?View.GONE:View.VISIBLE);
        tvDescription.setVisibility(TextUtils.isEmpty(poll.getDescription())?View.GONE:View.VISIBLE);
        niceSpinner.setSelectedIndex(0);
        setData(0);
        checkTime();
    }

    private void initView(){
        view = View.inflate(getContext(), R.layout.view_mrpoll_result, null);
        tvEndline = ViewUtil.findViewById(view,R.id.tv_endline);
        tvDescription = ViewUtil.findViewById(view,R.id.tv_description);
        tvTitle = ViewUtil.findViewById(view, R.id.tv_title);
        mChart = ViewUtil.findViewById(view,R.id.chart);
        tvNotPoll = ViewUtil.findViewById(view,R.id.tvNotPoll);
        llRefresh = ViewUtil.findViewById(view,R.id.ll_refresh);
        llRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        initSpinner();
        initChart();
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(view);
    }
    private void initSpinner(){
        niceSpinner = ViewUtil.findViewById(view,R.id.spinner);
        niceSpinner.setVisibility(poll.getQuestions().size()==1?View.GONE:View.VISIBLE);
//        niceSpinner.setArrowHide(poll.getQuestions().size()==1);
        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setData(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        List<String> questions = new ArrayList<>();
        for(Questions question: poll.getQuestions()){
            questions.add(question.getText());
        }
        niceSpinner.attachDataSource(questions);
    }
    private void initChart(){
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
    public void setData(int position) {
        tvTitle.setText(poll.getTitle());
        tvDescription.setText(poll.getQuestions().get(position).getText());
        tvTitle.setVisibility(TextUtils.isEmpty(tvTitle.getText()) ? View.GONE : View.VISIBLE);
        tvDescription.setVisibility(TextUtils.isEmpty(tvDescription.getText()) ? View.GONE : View.VISIBLE);
        if (poll.getTotalSubmission() == 0) {
            showPollChart(false);
            return;
        }
        showPollChart(true);

        ArrayList<PieEntry> entries = new ArrayList<>();

        int maxLength=0;
        for(int j = 0; j < poll.getQuestions().get(position).getOptions().size(); j++){
            Options options = poll.getQuestions().get(position).getOptions().get(j);
            int length = String.format(Locale.getDefault(), "%s%s(%d)",new DecimalFormat("###,###,##0.0").format(options.getPercentage()),"%",options.getTotalPickedCount()).length();
            if(length>maxLength){
                maxLength = length;
            }
        }
        for (int i = 0; i < poll.getQuestions().get(position).getOptions().size(); i++) {
            Options options = poll.getQuestions().get(position).getOptions().get(i);
            int length = String.format(Locale.getDefault(), "%s%s(%d)",new DecimalFormat("###,###,##0.0").format(options.getPercentage()),"%",options.getTotalPickedCount()).length();
            String result = StringUtil.ToDBC(String.format(Locale.getDefault(), "%s%s(%d)%s %s",new DecimalFormat("###,###,##0.0").format(options.getPercentage()),"%",options.getTotalPickedCount(), StringUtil.addTab(maxLength,length), StringUtil.limitStringLength(options.getText(),18)));
            entries.add(new PieEntry((float) options.getPercentage(), result));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(4f);

        ArrayList<Integer> colors = new ArrayList<>();
        for(Options options :poll.getQuestions().get(0).getOptions()){
            colors.add(Color.parseColor(options.getColor()!=null?options.getColor():"#ffff0000"));
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

    private void showPollChart(boolean isShow) {
        ViewUtil.setVisibility(mChart, isShow ? View.VISIBLE : View.GONE);
        ViewUtil.setVisibility(tvNotPoll, isShow ? View.GONE : View.VISIBLE);
    }

    private void refresh(){
        if (TextUtils.isEmpty(poll.getPollId())) {
            return;
        }
//        AppAction.getMRPollSummary(getContext(), H2HModel.getInstance().getMeetingId(), poll.getPollId(), new HttpResponseHandler(getContext(), Summary.class) {
//            @Override
//            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
//                Summary summary = (Summary) response;
//                setPoll(summary.getPoll());
//                if (refreshListener!=null) {
//                    refreshListener.onRefresh(true);
//                }
//            }
//        });
    }
    public interface RefreshListener{
        void onRefresh(boolean isRefresh);
    }
    private RefreshListener refreshListener;

    long dTime;
    long cTime;
    private Timer timer;
    private void checkTime() {
        dTime =  poll.getEndTime()- MRUtils.getCurrentTimeMillis();
        if(dTime<=0){
            llRefresh.setVisibility(View.GONE);
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
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
                            String result = simpleDateFormat.format(dTime-cTime);
                            tvEndline.setText(result);
                        }
                    });
                } else {
                    timer.cancel();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            tvEndline.setText(getContext().getString(R.string.already_over));
                            llRefresh.performClick();
                            llRefresh.setVisibility(View.GONE);
                        }
                    });
                }
            }
        };
        timer.schedule(task, new Date(System.currentTimeMillis()), 1000);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(timer!=null){
            timer.cancel();
        }
    }
}
