package itutorgroup.h2h.bean.login;

import java.io.Serializable;
import java.util.List;

public class Attendee_roles implements Serializable {
private String role_name;

private List<Actions> actions ;

public void setRole_name(String role_name){
this.role_name = role_name;
}
public String getRole_name(){
return this.role_name;
}
public void setActions(List<Actions> actions){
this.actions = actions;
}
public List<Actions> getActions(){
return this.actions;
}

}