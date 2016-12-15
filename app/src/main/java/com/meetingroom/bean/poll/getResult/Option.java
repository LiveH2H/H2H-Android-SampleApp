package com.meetingroom.bean.poll.getResult;

import java.io.Serializable;

public class Option implements Serializable{
private String optionId;
    private int percentage;
private String text;

public void setOptionId(String optionId){
this.optionId = optionId;
}
public String getOptionId(){
return this.optionId;
}
public void setText(String text){
this.text = text;
}
public String getText(){
return this.text;
}

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}