package view.gui;

import javafx.scene.control.TextArea;

import java.util.Date;

public class Chat {

    TextArea chat;

    public Chat(TextArea chat){
        this.chat = chat;
        chat.setWrapText(true);
    }

    public void addMessage(String login, String message){
        Date date = new Date();

        if(!chat.getText().isEmpty())
            chat.setText(chat.getText() + "\n" + login + ": " + message);
        else
            chat.setText(login + ": " + message);
    }
}
