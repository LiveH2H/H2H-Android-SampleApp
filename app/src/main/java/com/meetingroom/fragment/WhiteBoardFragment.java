package com.meetingroom.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.itutorgroup.h2hmodel.H2HMeetingWhiteboard;
import com.itutorgroup.h2hwhiteboard.H2HWhiteboardItem;
import com.itutorgroup.h2hwhiteboard.H2HWhiteboardListener;
import com.itutorgroup.h2hwhiteboard.H2HWhiteboardManager;
import com.meetingroom.bean.poll.summary.Poll;
import com.meetingroom.constants.MeetingConstants;
import com.meetingroom.view.PlusMinButton;
import com.meetingroom.view.PollView;

import java.util.ArrayList;
import java.util.List;

import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.FastJsonUtils;
import itutorgroup.h2h.utils.ViewUtil;

public class WhiteBoardFragment extends BaseFragment {
    private List<Poll> polls = new ArrayList<>();
    private List<PollView> pollViews = new ArrayList<>();
    private ViewPager pollViewPager;
    private PlusMinButton plusMinButton;
    private FrameLayout whiteboardContainer;
    private PagerAdapter pollPagerAdapter;
    private Handler handler = new Handler(Looper.getMainLooper());

    public WhiteBoardFragment() {
    }

    public static WhiteBoardFragment newInstance(ArrayList<Poll> polls, boolean isShowWhiteboardLocatingPoll) {
        WhiteBoardFragment whiteBoardFragment = new WhiteBoardFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("polls", polls);
        bundle.putBoolean(MeetingConstants.isShowWhiteboardLocatingPoll, isShowWhiteboardLocatingPoll);
        whiteBoardFragment.setArguments(bundle);
        return whiteBoardFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_white_board, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pollViewPager = ViewUtil.findViewById(view, R.id.viewPager2);
        plusMinButton = ViewUtil.findViewById(view, R.id.ll_plus_min_button);
        whiteboardContainer = ViewUtil.findViewById(view, R.id.whiteboardContainer);

        showLoadingDialog();
        H2HWhiteboardManager.getInstance().attachWhiteboardListener(new H2HWhiteboardListener() {
            @Override
            public void onWhiteboardInitFinish() {
                dismissLoadingDialog();
            }

            @Override
            public void onAddWhiteboard(final H2HMeetingWhiteboard meetingWhiteboard) {
                if (isFinishing()) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        H2HWhiteboardManager.getInstance().addWhiteboardSocketAndChange(getActivity(), meetingWhiteboard, 1000);
                    }
                });
            }

            @Override
            public void onRemoveWhiteboard(final String whiteboardId) {
                if (isFinishing()) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        H2HWhiteboardManager.getInstance().removeWhiteboardSocket(whiteboardId);
                    }
                });
            }

            @Override
            public void onSelected(final int position) {
                if (isFinishing()) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showCurrentWhiteboardView(position);
                    }
                });
            }

            @Override
            public void onSelectedPoll(String pollId) {
                if (isFinishing() || pollPagerAdapter == null) {
                    return;
                }
                for (int i = 0; i < pollViews.size(); i++) {
                    if (TextUtils.equals(pollId, pollViews.get(i).getPoll().getPollId())) {
                        if (i < pollPagerAdapter.getCount()) {
                            selectedPollItem(i);
                        }
                        break;
                    }
                }
            }

            private void selectedPollItem(final int index) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWhiteboardViewPager(false);
                        pollViewPager.setCurrentItem(index, false);
                    }
                });
            }

            @Override
            public void onGetPoll(final Object... args) {
                if (pollPagerAdapter == null || isFinishing()) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Poll poll = FastJsonUtils.parseObject(args[0].toString(), Poll.class);
                        if (poll != null) {
                            poll.setEndTime(poll.getDuration() + System.currentTimeMillis());
                            addPoll(poll);
                            pollPagerAdapter.notifyDataSetChanged();
                        }
                    }
                });

            }

           public void onUpdatePoll(final Object object) {
                if (pollPagerAdapter == null || isFinishing()) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Poll poll = (Poll) object;
                        addPoll(poll);
                        pollPagerAdapter.notifyDataSetChanged();
                    }
                });

            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                H2HWhiteboardManager.getInstance().initWhiteboardSocket(mContext);
                showCurrentWhiteboardView(0);
            }
        }, 3000);

        initPollViewPager();
        initPoll();
        showWhiteboardViewPager(true);
    }

    private void initPoll() {
        if (getArguments() != null && getArguments().containsKey("polls")) {
            @SuppressWarnings("unchecked") List<Poll> polls = (List<Poll>) getArguments().getSerializable("polls");
            if (polls != null && !polls.isEmpty()) {
                for (Poll poll : polls) {
                    addPoll(poll);
                }
                pollPagerAdapter.notifyDataSetChanged();
            }
        }
    }

    private void addPoll(Poll poll) {
        int index = polls.indexOf(poll);
        if (index < 0) {
            polls.add(poll);
            PollView pollView = new PollView(mContext, poll, new PollView.RefreshListener() {
                @Override
                public void onRefresh(boolean isRefresh) {
                    pollPagerAdapter.notifyDataSetChanged();
                }
            });
            pollViews.add(pollView);
        } else {
            polls.set(index, poll);
            pollViews.get(index).setPoll(poll);
        }
    }

    private void showWhiteboardViewPager(boolean isShow) {
        if (isShow) {
            ViewUtil.setVisibility(whiteboardContainer, View.VISIBLE);
            ViewUtil.setVisibility(plusMinButton, View.VISIBLE);
            ViewUtil.setVisibility(pollViewPager, View.GONE);
        } else {
            ViewUtil.setVisibility(whiteboardContainer, View.GONE);
            ViewUtil.setVisibility(plusMinButton, View.GONE);
            ViewUtil.setVisibility(pollViewPager, View.VISIBLE);
        }
        whiteboardContainer.refreshDrawableState();
        pollViewPager.refreshDrawableState();
    }

    private void initPollViewPager() {
        pollViewPager.setOffscreenPageLimit(100);
        pollPagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return pollViews.size();
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(pollViews.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(pollViews.get(position));
                return pollViews.get(position);
            }
        };
        pollViewPager.setAdapter(pollPagerAdapter);
    }

    private void showCurrentWhiteboardView(int position) {
        showWhiteboardViewPager(true);
        H2HWhiteboardManager whiteboardManager = H2HWhiteboardManager.getInstance();
        if (position >= 0 && position < whiteboardManager.itemList.size()) {
            H2HWhiteboardItem item = whiteboardManager.itemList.get(position);
            plusMinButton.setCountPage(Integer.toString(item.pageList.size()));
            plusMinButton.setCurrentPage(Integer.toString(item.getCurrentPageNumber() + 1));

            if (item.currentPage != null && item.currentPage.currentWebview != null) {
                if (item.currentPage.currentWebview != whiteboardContainer.getChildAt(0)) {
                    whiteboardContainer.removeAllViews();
                    whiteboardContainer.addView(item.currentPage.currentWebview, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    whiteboardContainer.setTag(item.whiteboardId);
                }
                item.currentPage.currentWebview.refreshDrawableState();
                item.currentPage.currentWebview.webView.refreshDrawableState();
                item.currentPage.reload();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        H2HWhiteboardManager.getInstance().clearWhiteboardSocket();
    }

    public void locatePoll(final Poll poll) {
        if (poll == null) {
            if (getArguments().getBoolean(MeetingConstants.isShowWhiteboardLocatingPoll)) {
                //处理第一次进来的定位poll item
                showWhiteboardViewPager(false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pollViewPager.setCurrentItem(polls.size() - 1, false);
                        pollPagerAdapter.notifyDataSetChanged();
                    }
                }, 30);
            }
        } else {
            //处理非第一次进来的定位poll item
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showWhiteboardViewPager(false);
                    pollViewPager.setCurrentItem(polls.indexOf(poll), false);
                    pollPagerAdapter.notifyDataSetChanged();
                }
            }, 30);

        }
    }
}
