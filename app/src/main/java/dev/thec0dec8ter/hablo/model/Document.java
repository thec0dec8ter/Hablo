package dev.thec0dec8ter.hablo.model;

import java.io.Serializable;

public class Document implements Serializable {
    private String id;
    private String sender = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
