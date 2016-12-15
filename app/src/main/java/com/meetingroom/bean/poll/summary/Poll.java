package com.meetingroom.bean.poll.summary;

import java.io.Serializable;
import java.util.List;

public class Poll implements Serializable{
    private boolean started;
    private long duration;
    private String pollId;

    private String title;

    private String description;

    private List<Questions> questions;

    private long startTime;

    private long endTime;

    private int totalSubmission;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

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

    public void setQuestions(List<Questions> questions) {
        this.questions = questions;
    }

    public List<Questions> getQuestions() {
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

    public void setTotalSubmission(int totalSubmission) {
        this.totalSubmission = totalSubmission;
    }

    public int getTotalSubmission() {
        return this.totalSubmission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) return false;

        Poll poll = (Poll) o;

        return pollId.equals(poll.pollId);

    }


    @Override
    public int hashCode() {
        return pollId.hashCode();
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
