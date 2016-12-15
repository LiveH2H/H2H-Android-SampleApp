package com.meetingroom.callback;

import com.meetingroom.bean.poll.summary.Poll;

import java.util.Comparator;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年10月31日 11:33
 * 邮箱：nianbin@mosainet.com
 */
public class PollComparator implements Comparator<Poll> {
    @Override
    public int compare(Poll o1, Poll o2) {
        if(o1.getEndTime()-o2.getEndTime()>0){
            return 1;
        }else if(o1.getEndTime()-o2.getEndTime()<0){
            return -1;
        }
        return 0;
    }
}
