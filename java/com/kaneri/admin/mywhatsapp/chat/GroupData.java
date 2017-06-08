package com.kaneri.admin.mywhatsapp.chat;

/**
 * Created by karan on 3/28/2017.
 */

public class GroupData {

    public String groupname;
    public String members[] = new String[100];

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String[] getMembers() {
        return members;
    }

    public void setMembers(String[] members) {
        this.members = members;
    }
}
