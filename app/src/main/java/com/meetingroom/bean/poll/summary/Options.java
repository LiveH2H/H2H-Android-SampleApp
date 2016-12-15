package com.meetingroom.bean.poll.summary;

import java.io.Serializable;

public class Options implements Serializable{
private String optionId;

private String text;

private int totalPickedCount;

private String color;

private double percentage;

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
public void setTotalPickedCount(int totalPickedCount){
this.totalPickedCount = totalPickedCount;
}
public int getTotalPickedCount(){
return this.totalPickedCount;
}
public void setColor(String color){
this.color = color;
}
public String getColor(){
return this.color;
}
public void setPercentage(double percentage){
this.percentage = percentage;
}
public double getPercentage(){
return this.percentage;
}

}