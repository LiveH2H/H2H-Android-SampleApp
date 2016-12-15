package com.meetingroom.bean.poll.getResult;

import java.io.Serializable;
import java.util.List;

public class Poll implements Serializable {
    private String pollId;

    private String title;

    private String description;

    private List<Question> questions;

    private long startTime;

    private long endTime;

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getPollId() {
        return this.pollId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Question> getQuestions() {
        return this.questions;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

}