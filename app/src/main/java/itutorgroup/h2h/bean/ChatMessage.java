package itutorgroup.h2h.bean;

import java.io.Serializable;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年07月13日 10:09
 * 邮箱：nianbin@mosainet.com
 */
public class ChatMessage implements Serializable{
    public boolean msgComing;
    public boolean msgSend;
    public String content;
    public String name;
    public String meetingId;
}
