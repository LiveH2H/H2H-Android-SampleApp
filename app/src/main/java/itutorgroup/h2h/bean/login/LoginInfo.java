package itutorgroup.h2h.bean.login;

import java.io.Serializable;

public class LoginInfo implements Serializable {
private String h2h_api_version;

private H2h_return h2h_return;

public void setH2h_api_version(String h2h_api_version){
this.h2h_api_version = h2h_api_version;
}
public String getH2h_api_version(){
return this.h2h_api_version;
}
public void setH2h_return(H2h_return h2h_return){
this.h2h_return = h2h_return;
}
public H2h_return getH2h_return(){
return this.h2h_return;
}

}