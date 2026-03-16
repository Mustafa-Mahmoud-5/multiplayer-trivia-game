package org.game.common.protocol;

public class MessageParser {
    public static Message parse(String text) {
        Message msg = new Message();
        String[] parts = text.split("\\|");
        msg.setParts(parts);
        msg.setType(parts[0]);
        return msg;
    }

    public static String generate(String ... parts) {
        return String.join("|", parts);
    }
}
