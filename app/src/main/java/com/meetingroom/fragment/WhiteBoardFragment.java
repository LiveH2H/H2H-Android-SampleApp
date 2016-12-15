package com.meetingroom.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.itutorgroup.h2hmodel.H2HMeetingWhiteboard;
import com.itutorgroup.h2hwhiteboard.H2HWhiteboardItem;
import com.itutorgroup.h2hwhiteboard.H2HWhiteboardListener;
import com.itutorgroup.h2hwhiteboard.H2HWhiteboardManager;
import com.itutorgroup.h2hwhiteboard.widget.MyWebView;
import com.meetingroom.bean.poll.summary.Poll;
import com.meetingroom.constants.MeetingConstants;
import com.meetingroom.view.PlusMinButton;
import com.meetingroom.view.PollView;
import com.meetingroom.view.RelativeDialog;
import com.meetingroom.view.WhiteBoardHorizontalDialog;

import java.util.ArrayList;
import java.util.List;

import itutorgroup.h2h.R;
import itutorgroup.h2h.utils.FastJsonUtils;
import itutorgroup.h2h.utils.LogUtils;
import itutorgroup.h2h.utils.ViewUtil;


public class WhiteBoardFragment extends BaseFragment {
    private List<Poll> polls = new ArrayList<>();
    private List<PollView> pollViews = new ArrayList<>();
    private WhiteBoardHorizontalDialog whiteBoardHorizontalDialog;
    private ImageView ivVideo, ivChangeWhiteboard;
    private ViewPager viewPager, viewPager2;
    private PlusMinButton plusMinButton;
    private boolean isChange;

    public WhiteBoardFragment() {
    }

    private void initPoll() {
        if (getArguments().getSerializable("polls") != null) {
            List<Poll> polls = (List<Poll>) getArguments().getSerializable("polls");
            if (polls != null) {
                for (Poll poll : polls) {
                    getPoll(poll);
                }
                if (polls.size() != 0) {
                    pagerAdapter2.notifyDataSetChanged();
                }
            }
        }

    }

    private void getPoll(Poll poll) {
        if (!polls.contains(poll)) {
            polls.add(poll);
            PollView pollView = new PollView(mContext, poll, new PollView.RefreshListener() {
                @Override
                public void onRefresh(boolean isRefresh) {
                    pagerAdapter2.notifyDataSetChanged();
                }
            });
            pollViews.add(pollView);
        } else {
            int index = polls.indexOf(poll);
            polls.remove(index);
            polls.add(index, poll);
            pollViews.get(index).setPoll(poll);
        }
    }

    private WhiteBoardFragmentCallback whiteBoardFragmentCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WhiteBoardFragmentCallback) {
            whiteBoardFragmentCallback = (WhiteBoardFragmentCallback) context;
        }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_white_board, container, false);
        viewPager = ViewUtil.findViewById(view, R.id.viewPager);
        viewPager2 = ViewUtil.findViewById(view, R.id.viewPager2);
        ivVideo = ViewUtil.findViewById(view, R.id.iv_video);
        ivChangeWhiteboard = ViewUtil.findViewById(view, R.id.iv_changewhiteboard);
        plusMinButton = ViewUtil.findViewById(view, R.id.ll_plus_min_button);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showLoadingDialog();
        H2HWhiteboardManager.getInstance().initWhiteboardSocket(mContext);
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
                        H2HWhiteboardManager.getInstance().addWhiteboardSocket(getActivity(), meetingWhiteboard, 2000);
                        if (pagerAdapter != null) {
                            showWhiteboardViewPager(true);
                            pagerAdapter.notifyDataSetChanged();
                        }
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
                        if (pagerAdapter != null) {
                            showWhiteboardViewPager(true);
                            pagerAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void onSelected(final int position, final boolean isRefresh) {
                if (isFinishing() || viewPager == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWhiteboardViewPager(true);
                        if (position < pagerAdapter.getCount()) {
                            isChange = true;
                            viewPager.setCurrentItem(position, false);
                            isChange = false;
                        }
                        setPlusMinButtonText(position, isRefresh);
                    }
                });
            }

            @Override
            public void onSelectedPoll(String pollId) {
                if (isFinishing() || pagerAdapter2 == null) {
                    return;
                }
                for (int i = 0; i < pollViews.size(); i++) {
                    if (TextUtils.equals(pollId, pollViews.get(i).getPoll().getPollId())) {
                        if (i < pagerAdapter2.getCount()) {
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
                        viewPager2.setCurrentItem(index, false);
                    }
                });
            }

            @Override
            public void onGetPoll(final Object... args) {
                if (pagerAdapter2 == null || isFinishing()) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Poll poll = FastJsonUtils.parseObject(args[0].toString(), Poll.class);
                        if (poll != null) {
                            poll.setEndTime(poll.getEndTime() - poll.getStartTime() + System.currentTimeMillis());
                            getPoll(poll);
                            pagerAdapter2.notifyDataSetChanged();
                        }
                    }
                });

            }

            @Override
            public void onUpdatePoll(final Object object) {
                if (pagerAdapter2 == null || isFinishing()) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Poll poll = (Poll) object;
                        getPoll(poll);
                        pagerAdapter2.notifyDataSetChanged();
                    }
                });

            }
        });

        ivVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (whiteBoardFragmentCallback != null) {
                    whiteBoardFragmentCallback.turnToConference();
                }
            }
        });

        ivChangeWhiteboard.setVisibility(View.GONE);
        ivChangeWhiteboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                views.clear();
                for (int i = 0; i < pollViews.size(); i++) {
                    pollViews.get(i).setTag(polls.get(i).getTitle());
                    views.add(pollViews.get(i));
                }
                for (int j = 0; j < H2HWhiteboardManager.getInstance().itemList.size(); j++) {
                    getCurrentWebView(j).webView.setTag(H2HWhiteboardManager.getInstance().itemList.get(j).whiteboard_presentation);
                    views.add(getCurrentWebView(j).webView);
                }
                int position;
                if (viewPager.getVisibility() == View.VISIBLE) {
                    position = pollViews.size() + viewPager.getCurrentItem();
                } else {
                    position = viewPager2.getCurrentItem();
                }
                whiteBoardHorizontalDialog.refresh(views, position);
                whiteBoardHorizontalDialog.showOnAnchor(v, RelativeDialog.Orientation.right);
            }
        });

        plusMinButton.setOnPageChangeListener(new PlusMinButton.OnPageChangeListener() {

            @Override
            public void onPageChange(int currentPage) {
                H2HWhiteboardItem item = H2HWhiteboardManager.getInstance().itemList.get(viewPager.getCurrentItem());
                if (currentPage - 1 < item.pageList.size()) {
                    item.currentPage = item.pageList.get(currentPage - 1);
                    H2HWhiteboardManager.getInstance().onChange(item);
                }
            }
        });

        initWhitboard();
        initHorizontalView();
        viewPager2.setOffscreenPageLimit(100);
        showWhiteboardViewPager(true);
        initPoll();
        locatePoll(null);
    }

    private void showWhiteboardViewPager(boolean isShow) {
        if (isShow) {
            ViewUtil.setVisibility(viewPager, View.VISIBLE);
            ViewUtil.setVisibility(viewPager2, View.GONE);
        } else {
            ViewUtil.setVisibility(viewPager, View.GONE);
            ViewUtil.setVisibility(viewPager2, View.VISIBLE);
        }
        viewPager.refreshDrawableState();
        viewPager2.refreshDrawableState();
    }

    private void initHorizontalView() {
        whiteBoardHorizontalDialog = new WhiteBoardHorizontalDialog(mContext, views, new WhiteBoardHorizontalDialog.onItemClickListener() {
            @Override
            public void onItemclick(int position) {
                if (whiteBoardHorizontalDialog.isShowing()) {
                    whiteBoardHorizontalDialog.dismiss();
                    int pollviewCount = pollViews.size();
                    if (pollviewCount > 0 && position <= pollviewCount - 1) {
                        viewPager.setVisibility(View.GONE);
                        viewPager2.setVisibility(View.VISIBLE);
                        viewPager2.setCurrentItem(position, false);
                    } else {
                        viewPager.setVisibility(View.VISIBLE);
                        viewPager2.setVisibility(View.GONE);
                        position -= pollviewCount;
                        H2HWhiteboardManager.getInstance().onChange(position);
                    }
                }
            }
        });
    }

    private List<View> views = new ArrayList<>();
    private PagerAdapter pagerAdapter, pagerAdapter2;

    private void initWhitboard() {
        viewPager.setAdapter(pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return H2HWhiteboardManager.getInstance().itemList.size();
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                LogUtils.i("WhiteBoardFragment destroyItem() position=" + position);
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                LogUtils.i("WhiteBoardFragment instantiateItem() position=" + position);
                View view = getPageViewpager(container, position);
                container.addView(view);
                return view;
            }

            @Override
            public int getItemPosition(Object object) {//重写此方法 强制adapter.notify刷新
                return POSITION_NONE;
            }
        });
        viewPager2.setAdapter(pagerAdapter2 = new PagerAdapter() {
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
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.i(this.toString() + " onPageSelected() potition=" + position);
                if (!isChange) {
                    setPlusMinButtonText(position, false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private View getPageViewpager(ViewGroup container, final int position) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_viewpager_whiteboard, container, false);
        view.setTag(position);
        ViewPager viewpager = (ViewPager) view.findViewById(R.id.item_whiteboard_page);
        viewpager.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                H2HWhiteboardManager whiteboardManager = H2HWhiteboardManager.getInstance();
                return position < whiteboardManager.itemList.size() ? whiteboardManager.itemList.get(position).pageList.size() : 0;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                LogUtils.i("getPageViewpager destroyItem() position=" + position);
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container1, int position1) {
                LogUtils.i("getPageViewpager instantiateItem() position=" + position1);

                MyWebView view = H2HWhiteboardManager.getInstance().itemList.get(position).pageList.get(position1).currentWebview;
                if (view.getParent() != null) {
                    ((ViewGroup) view.getParent()).removeView(view);
                }
                container1.addView(view);
                return view;
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        });
        return view;
    }

    private void setPlusMinButtonText(int position, boolean isRefresh) {
        H2HWhiteboardManager whiteboardManager = H2HWhiteboardManager.getInstance();
        if (position < whiteboardManager.itemList.size()) {
            H2HWhiteboardItem item = whiteboardManager.itemList.get(position);
            plusMinButton.setCountPage(Integer.toString(item.pageList.size()));
            plusMinButton.setCurrentPage(Integer.toString(item.currentPage.pageNumber + 1));
            View view = null;
            for (int i = 0; i < viewPager.getChildCount(); i++) {
                view = viewPager.getChildAt(i);
                if (((int) view.getTag()) == position) {
                    break;
                }
                view = null;
            }
            if (view != null) {
                ViewPager pager = ViewUtil.findViewById(view, R.id.item_whiteboard_page);
                if (isRefresh) {
                    pager.getAdapter().notifyDataSetChanged();
                }
                if (item.currentPage.pageNumber < pager.getAdapter().getCount()) {
                    pager.setCurrentItem(item.currentPage.pageNumber, false);
                }
                item.currentPage.currentWebview.refreshDrawableState();
                item.currentPage.currentWebview.webView.refreshDrawableState();
                item.currentPage.reload();
            }
        }
    }

    public MyWebView getCurrentWebView(int i) {
        return H2HWhiteboardManager.getInstance().itemList.get(i).currentPage.currentWebview;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        H2HWhiteboardManager.getInstance().clearWhiteboardSocket();
    }

    public interface WhiteBoardFragmentCallback {
        void turnToConference();
    }

    public void locatePoll(final Poll poll) {
        if (poll == null) {
            if (getArguments().getBoolean(MeetingConstants.isShowWhiteboardLocatingPoll)) {
                //处理第一次进来的定位poll item
                showWhiteboardViewPager(false);
                new Handler(mContext.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewPager2.setCurrentItem(polls.size() - 1, false);
                        pagerAdapter2.notifyDataSetChanged();
                    }
                }, 30);

            }
        } else {
            //处理非第一次进来的定位poll item
            new Handler(mContext.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    showWhiteboardViewPager(false);
                    viewPager2.setCurrentItem(polls.indexOf(poll), false);
                    pagerAdapter2.notifyDataSetChanged();
                }
            }, 30);

        }
    }
}
