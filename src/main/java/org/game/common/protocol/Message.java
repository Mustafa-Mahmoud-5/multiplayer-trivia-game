package org.game.common.protocol;

import org.game.common.protocol.enums.MessageType;

public class Message {
    String type;
    String[] parts;




    public String getPart(int idx) {
        if(idx >= parts.length) throw new ArrayIndexOutOfBoundsException();
        return this.parts[idx];
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getParts() {
        return parts;
    }

    public void setParts(String[] parts) {
        this.parts = parts;
    }

    public MessageType getMessageType() {
        return MessageType.valueOf(type.toUpperCase());
    }
}
