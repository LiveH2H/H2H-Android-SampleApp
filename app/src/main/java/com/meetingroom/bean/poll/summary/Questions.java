package com.meetingroom.bean.poll.summary;

import java.io.Serializable;
import java.util.List;

public class Questions implements Serializable{
private String questionId;

private String text;

private List<Options> options ;

public void setQuestionId(String questionId){
this.questionId = questionId;
}
public String getQuestionId(){
return this.questionId;
}
public void setText(String text){
this.text = text;
}
public String getText(){
return this.text;
}
public void setOptions(List<Options> options){
this.options = options;
}
public List<Options> getOptions(){
return this.options;
}

}