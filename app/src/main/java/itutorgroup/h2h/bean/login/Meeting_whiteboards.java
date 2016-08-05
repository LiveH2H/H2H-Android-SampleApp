package itutorgroup.h2h.bean.login;

import java.io.Serializable;
import java.util.List;

public class Meeting_whiteboards implements Serializable {
private int whiteboard_id;

private String whiteboard_presentation;

private String whiteboard_desc;

private String whiteboard_status;

private String whiteboard_blank;

private List<Whiteboard_pages> whiteboard_pages ;

public void setWhiteboard_id(int whiteboard_id){
this.whiteboard_id = whiteboard_id;
}
public int getWhiteboard_id(){
return this.whiteboard_id;
}
public void setWhiteboard_presentation(String whiteboard_presentation){
this.whiteboard_presentation = whiteboard_presentation;
}
public String getWhiteboard_presentation(){
return this.whiteboard_presentation;
}
public void setWhiteboard_desc(String whiteboard_desc){
this.whiteboard_desc = whiteboard_desc;
}
public String getWhiteboard_desc(){
return this.whiteboard_desc;
}
public void setWhiteboard_status(String whiteboard_status){
this.whiteboard_status = whiteboard_status;
}
public String getWhiteboard_status(){
return this.whiteboard_status;
}
public void setWhiteboard_blank(String whiteboard_blank){
this.whiteboard_blank = whiteboard_blank;
}
public String getWhiteboard_blank(){
return this.whiteboard_blank;
}
public void setWhiteboard_pages(List<Whiteboard_pages> whiteboard_pages){
this.whiteboard_pages = whiteboard_pages;
}
public List<Whiteboard_pages> getWhiteboard_pages(){
return this.whiteboard_pages;
}

}