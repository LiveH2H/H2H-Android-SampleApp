package com.meetingroom.bean.poll.summary;

import android.text.TextUtils;

import java.io.Serializable;

public class Summary implements Serializable{

private Poll poll;

public void setPoll(Poll poll){
this.poll = poll;
}
public Poll getPoll(){
return this.poll;
}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Summary summary = (Summary) o;

        return TextUtils.equals(summary.getPoll().getPollId(),getPoll().getPollId());

    }

    @Override
    public int hashCode() {
        return poll.hashCode();
    }
}