package com.meetingroom.bean.poll.getResult;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable{
private String questionId;

private String text;

private List<Option> options ;

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
public void setOptions(List<Option> options){
this.options = options;
}
public List<Option> getOptions(){
return this.options;
}

}