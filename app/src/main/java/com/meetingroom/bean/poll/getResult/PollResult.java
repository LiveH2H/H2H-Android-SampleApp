package com.meetingroom.bean.poll.getResult;

import java.io.Serializable;

public class PollResult implements Serializable {

private Poll poll;

public void setPoll(Poll poll){
this.poll = poll;
}
public Poll getPoll(){
return this.poll;
}

}