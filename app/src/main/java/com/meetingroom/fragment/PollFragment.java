package com.meetingroom.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.meetingroom.adapter.PollsAdapter;
import com.meetingroom.bean.poll.summary.Options;
import com.meetingroom.bean.poll.summary.Questions;
import com.meetingroom.callback.PollOptionSelectCallback;

import java.util.ArrayList;
import java.util.List;

import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.ViewUtil;

public class PollFragment extends BaseFragment {
    private boolean firstLoad = true;
    public PollsAdapter adapter;
    public List<Options> optionses = new ArrayList<>();
    public Questions question;
    private TextView tvQuestion;
    private ListView listView;

    public PollFragment() {
    }

    public static PollFragment newInstance(Questions question) {
        PollFragment fragment = new PollFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("question", question);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof PollFragmentCallback){
            pollFragmentCallback = (PollFragmentCallback) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //执行一次
            question = (Questions) getArguments().getSerializable("question");
            optionses.addAll(question.getOptions());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (firstLoad) {
            view = inflater.inflate(R.layout.fragment_poll_question, container, false);
            tvQuestion = ViewUtil.findViewById(view, R.id.tv_question);
            listView = ViewUtil.findViewById(view, R.id.lv);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (firstLoad) {
            tvQuestion.setText(question.getText());
            adapter = new PollsAdapter(mContext, R.layout.item_listformat_poll_question, optionses, true, new PollOptionSelectCallback() {
                @Override
                public void callback() {
                    if(pollFragmentCallback!=null){
                        pollFragmentCallback.onPollFragmentCallback();
                    }
                }
            });
            listView.setAdapter(adapter);
            firstLoad = false;
        }
    }
    private PollFragmentCallback pollFragmentCallback;
    public interface PollFragmentCallback{
        void onPollFragmentCallback();
    }
}
