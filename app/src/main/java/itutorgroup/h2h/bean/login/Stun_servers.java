package itutorgroup.h2h.bean.login;

import java.io.Serializable;
import java.util.List;

public class Stun_servers implements Serializable {
private List<Urls> urls ;

public void setUrls(List<Urls> urls){
this.urls = urls;
}
public List<Urls> getUrls(){
return this.urls;
}

}