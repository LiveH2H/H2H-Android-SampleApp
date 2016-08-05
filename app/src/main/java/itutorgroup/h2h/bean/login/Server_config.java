package itutorgroup.h2h.bean.login;

import java.io.Serializable;
import java.util.List;

public class Server_config implements Serializable {
private String countries;

private String kms_turnurl;

private boolean relay_only;

private List<Stun_servers> stun_servers ;

private List<Turn_servers> turn_servers ;

public void setCountries(String countries){
this.countries = countries;
}
public String getCountries(){
return this.countries;
}
public void setKms_turnurl(String kms_turnurl){
this.kms_turnurl = kms_turnurl;
}
public String getKms_turnurl(){
return this.kms_turnurl;
}
public void setRelay_only(boolean relay_only){
this.relay_only = relay_only;
}
public boolean getRelay_only(){
return this.relay_only;
}
public void setStun_servers(List<Stun_servers> stun_servers){
this.stun_servers = stun_servers;
}
public List<Stun_servers> getStun_servers(){
return this.stun_servers;
}
public void setTurn_servers(List<Turn_servers> turn_servers){
this.turn_servers = turn_servers;
}
public List<Turn_servers> getTurn_servers(){
return this.turn_servers;
}

}