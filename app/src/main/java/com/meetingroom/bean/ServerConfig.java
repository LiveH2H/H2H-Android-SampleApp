package com.meetingroom.bean;

import java.io.Serializable;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年07月18日 15:22
 * 邮箱：nianbin@mosainet.com
 */
public class ServerConfig implements Serializable {
    public String origin = "";
    public String serverURL = "";
    public String userToken = "";
    public String email ="";
    public String meetingId = "";
    public String name="";
    public Meeting meeting;
}
