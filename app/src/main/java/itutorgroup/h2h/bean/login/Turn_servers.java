package itutorgroup.h2h.bean.login;

import java.io.Serializable;
import java.util.List;

public class Turn_servers implements Serializable {
private List<Urls> urls ;

private String username;

private String credential;

public void setUrls(List<Urls> urls){
this.urls = urls;
}
public List<Urls> getUrls(){
return this.urls;
}
public void setUsername(String username){
this.username = username;
}
public String getUsername(){
return this.username;
}
public void setCredential(String credential){
this.credential = credential;
}
public String getCredential(){
return this.credential;
}

}