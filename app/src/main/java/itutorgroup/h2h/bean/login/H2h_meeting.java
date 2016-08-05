package itutorgroup.h2h.bean.login;

import java.io.Serializable;
import java.util.List;

public class H2h_meeting implements Serializable {
private int meeting_id;

private String origin;

private String origin_meeting_id;

private String origin_job_id;

private String origin_meeting_host_user_id;

private String meeting_host_name;

private String meeting_host_email;

private String meeting_status;

private String meeting_type;

private String meeting_subject;

private String meeting_description;

private String meeting_location;

private String meeting_upload_folder;

private String meeting_timezone;

private String meeting_scheduled_start_time;

private String meeting_scheduled_end_time;

private String meeting_actual_start_time;

private String meeting_actual_end_time;

private String meeting_record;

private List<Meeting_whiteboards> meeting_whiteboards ;

private String meeting_translators;

private String meeting_job_questions;

private String meeting_attendees;

private String meeting_invitees;

private String meeting_feedback;

private String meeting_issues;

public void setMeeting_id(int meeting_id){
this.meeting_id = meeting_id;
}
public int getMeeting_id(){
return this.meeting_id;
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
public void setOrigin_job_id(String origin_job_id){
this.origin_job_id = origin_job_id;
}
public String getOrigin_job_id(){
return this.origin_job_id;
}
public void setOrigin_meeting_host_user_id(String origin_meeting_host_user_id){
this.origin_meeting_host_user_id = origin_meeting_host_user_id;
}
public String getOrigin_meeting_host_user_id(){
return this.origin_meeting_host_user_id;
}
public void setMeeting_host_name(String meeting_host_name){
this.meeting_host_name = meeting_host_name;
}
public String getMeeting_host_name(){
return this.meeting_host_name;
}
public void setMeeting_host_email(String meeting_host_email){
this.meeting_host_email = meeting_host_email;
}
public String getMeeting_host_email(){
return this.meeting_host_email;
}
public void setMeeting_status(String meeting_status){
this.meeting_status = meeting_status;
}
public String getMeeting_status(){
return this.meeting_status;
}
public void setMeeting_type(String meeting_type){
this.meeting_type = meeting_type;
}
public String getMeeting_type(){
return this.meeting_type;
}
public void setMeeting_subject(String meeting_subject){
this.meeting_subject = meeting_subject;
}
public String getMeeting_subject(){
return this.meeting_subject;
}
public void setMeeting_description(String meeting_description){
this.meeting_description = meeting_description;
}
public String getMeeting_description(){
return this.meeting_description;
}
public void setMeeting_location(String meeting_location){
this.meeting_location = meeting_location;
}
public String getMeeting_location(){
return this.meeting_location;
}
public void setMeeting_upload_folder(String meeting_upload_folder){
this.meeting_upload_folder = meeting_upload_folder;
}
public String getMeeting_upload_folder(){
return this.meeting_upload_folder;
}
public void setMeeting_timezone(String meeting_timezone){
this.meeting_timezone = meeting_timezone;
}
public String getMeeting_timezone(){
return this.meeting_timezone;
}
public void setMeeting_scheduled_start_time(String meeting_scheduled_start_time){
this.meeting_scheduled_start_time = meeting_scheduled_start_time;
}
public String getMeeting_scheduled_start_time(){
return this.meeting_scheduled_start_time;
}
public void setMeeting_scheduled_end_time(String meeting_scheduled_end_time){
this.meeting_scheduled_end_time = meeting_scheduled_end_time;
}
public String getMeeting_scheduled_end_time(){
return this.meeting_scheduled_end_time;
}
public void setMeeting_actual_start_time(String meeting_actual_start_time){
this.meeting_actual_start_time = meeting_actual_start_time;
}
public String getMeeting_actual_start_time(){
return this.meeting_actual_start_time;
}
public void setMeeting_actual_end_time(String meeting_actual_end_time){
this.meeting_actual_end_time = meeting_actual_end_time;
}
public String getMeeting_actual_end_time(){
return this.meeting_actual_end_time;
}
public void setMeeting_record(String meeting_record){
this.meeting_record = meeting_record;
}
public String getMeeting_record(){
return this.meeting_record;
}
public void setMeeting_whiteboards(List<Meeting_whiteboards> meeting_whiteboards){
this.meeting_whiteboards = meeting_whiteboards;
}
public List<Meeting_whiteboards> getMeeting_whiteboards(){
return this.meeting_whiteboards;
}
public void setMeeting_translators(String meeting_translators){
this.meeting_translators = meeting_translators;
}
public String getMeeting_translators(){
return this.meeting_translators;
}
public void setMeeting_job_questions(String meeting_job_questions){
this.meeting_job_questions = meeting_job_questions;
}
public String getMeeting_job_questions(){
return this.meeting_job_questions;
}
public void setMeeting_attendees(String meeting_attendees){
this.meeting_attendees = meeting_attendees;
}
public String getMeeting_attendees(){
return this.meeting_attendees;
}
public void setMeeting_invitees(String meeting_invitees){
this.meeting_invitees = meeting_invitees;
}
public String getMeeting_invitees(){
return this.meeting_invitees;
}
public void setMeeting_feedback(String meeting_feedback){
this.meeting_feedback = meeting_feedback;
}
public String getMeeting_feedback(){
return this.meeting_feedback;
}
public void setMeeting_issues(String meeting_issues){
this.meeting_issues = meeting_issues;
}
public String getMeeting_issues(){
return this.meeting_issues;
}

}