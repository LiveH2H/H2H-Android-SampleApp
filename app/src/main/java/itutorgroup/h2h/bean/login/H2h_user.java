package itutorgroup.h2h.bean.login;

import java.io.Serializable;

public class H2h_user implements Serializable {
    private int user_id;

    private String origin;

    private String user_display_name;

    private String origin_user_id;

    private String h2h_display_locale_code;

    private String first_name;

    private String last_name;

    private String login_email;

    private String email_address;

    private String avatar;

    private String session_id;

    private String user_actions;

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {
        return this.user_id;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOrigin() {
        return this.origin;
    }

    public void setUser_display_name(String user_display_name) {
        this.user_display_name = user_display_name;
    }

    public String getUser_display_name() {
        return this.user_display_name;
    }

    public void setOrigin_user_id(String origin_user_id) {
        this.origin_user_id = origin_user_id;
    }

    public String getOrigin_user_id() {
        return this.origin_user_id;
    }

    public void setH2h_display_locale_code(String h2h_display_locale_code) {
        this.h2h_display_locale_code = h2h_display_locale_code;
    }

    public String getH2h_display_locale_code() {
        return this.h2h_display_locale_code;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getFirst_name() {
        return this.first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getLast_name() {
        return this.last_name;
    }

    public void setLogin_email(String login_email) {
        this.login_email = login_email;
    }

    public String getLogin_email() {
        return this.login_email;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getEmail_address() {
        return this.email_address;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getSession_id() {
        return this.session_id;
    }

    public void setUser_actions(String user_actions) {
        this.user_actions = user_actions;
    }

    public String getUser_actions() {
        return this.user_actions;
    }

}