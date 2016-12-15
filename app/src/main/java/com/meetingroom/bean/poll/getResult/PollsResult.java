package com.meetingroom.bean.poll.getResult;

import java.io.Serializable;
import java.util.List;

public class PollsResult implements Serializable {

private List<com.meetingroom.bean.poll.summary.Poll> pollList;

    public List<com.meetingroom.bean.poll.summary.Poll> getPollList() {
        return pollList;
    }

    public void setPollList(List<com.meetingroom.bean.poll.summary.Poll> pollList) {
        this.pollList = pollList;
    }
}