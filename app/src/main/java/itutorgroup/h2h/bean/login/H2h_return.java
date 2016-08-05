package itutorgroup.h2h.bean.login;

import java.io.Serializable;

public class H2h_return implements Serializable {
private H2h_user h2h_user;

private H2h_meeting h2h_meeting;

private H2h_attendee h2h_attendee;

public void setH2h_user(H2h_user h2h_user){
this.h2h_user = h2h_user;
}
public H2h_user getH2h_user(){
return this.h2h_user;
}
public void setH2h_meeting(H2h_meeting h2h_meeting){
this.h2h_meeting = h2h_meeting;
}
public H2h_meeting getH2h_meeting(){
return this.h2h_meeting;
}
public void setH2h_attendee(H2h_attendee h2h_attendee){
this.h2h_attendee = h2h_attendee;
}
public H2h_attendee getH2h_attendee(){
return this.h2h_attendee;
}

}