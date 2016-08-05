package itutorgroup.h2h.bean.login;

import java.io.Serializable;
import java.util.List;

public class H2h_attendee implements Serializable {
private int attendee_id;

private String origin;

private String origin_meeting_id;

private String origin_user_id;

private String attendee_display_name;

private String attendee_email;

private String attendee_ip_address;

private String attendee_device_id;

private String attendee_token;

private String attendee_status;

private int attendee_minutes;

private String attendee_leave_reason;

private String attendee_user;

private String attendee_translator;

private Attendee_join_details attendee_join_details;

private List<Attendee_roles> attendee_roles ;

private Server_config server_config;

public void setAttendee_id(int attendee_id){
this.attendee_id = attendee_id;
}
public int getAttendee_id(){
return this.attendee_id;
}
public void setOrigin(String origin){
this.origin = origin;
}
public String getOrigin(){
return this.origin;
}
public void setOrigin_meeting_id(String origin_meeting_id){
this.origin_meeting_id = origin_meeting_id;
}
public String getOrigin_meeting_id(){
return this.origin_meeting_id;
}
public void setOrigin_user_id(String origin_user_id){
this.origin_user_id = origin_user_id;
}
public String getOrigin_user_id(){
return this.origin_user_id;
}
public void setAttendee_display_name(String attendee_display_name){
this.attendee_display_name = attendee_display_name;
}
public String getAttendee_display_name(){
return this.attendee_display_name;
}
public void setAttendee_email(String attendee_email){
this.attendee_email = attendee_email;
}
public String getAttendee_email(){
return this.attendee_email;
}
public void setAttendee_ip_address(String attendee_ip_address){
this.attendee_ip_address = attendee_ip_address;
}
public String getAttendee_ip_address(){
return this.attendee_ip_address;
}
public void setAttendee_device_id(String attendee_device_id){
this.attendee_device_id = attendee_device_id;
}
public String getAttendee_device_id(){
return this.attendee_device_id;
}
public void setAttendee_token(String attendee_token){
this.attendee_token = attendee_token;
}
public String getAttendee_token(){
return this.attendee_token;
}
public void setAttendee_status(String attendee_status){
this.attendee_status = attendee_status;
}
public String getAttendee_status(){
return this.attendee_status;
}
public void setAttendee_minutes(int attendee_minutes){
this.attendee_minutes = attendee_minutes;
}
public int getAttendee_minutes(){
return this.attendee_minutes;
}
public void setAttendee_leave_reason(String attendee_leave_reason){
this.attendee_leave_reason = attendee_leave_reason;
}
public String getAttendee_leave_reason(){
return this.attendee_leave_reason;
}
public void setAttendee_user(String attendee_user){
this.attendee_user = attendee_user;
}
public String getAttendee_user(){
return this.attendee_user;
}
public void setAttendee_translator(String attendee_translator){
this.attendee_translator = attendee_translator;
}
public String getAttendee_translator(){
return this.attendee_translator;
}
public void setAttendee_join_details(Attendee_join_details attendee_join_details){
this.attendee_join_details = attendee_join_details;
}
public Attendee_join_details getAttendee_join_details(){
return this.attendee_join_details;
}
public void setAttendee_roles(List<Attendee_roles> attendee_roles){
this.attendee_roles = attendee_roles;
}
public List<Attendee_roles> getAttendee_roles(){
return this.attendee_roles;
}
public void setServer_config(Server_config server_config){
this.server_config = server_config;
}
public Server_config getServer_config(){
return this.server_config;
}

}