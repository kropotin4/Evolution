package view.gui;

import control.ControllerGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Date;

public class Chat {

    ControllerGUI controller;

    TextArea chat;
    TextField messageTextField;
    Button sendMessageButton;

    public Chat(ControllerGUI controller, TextArea chat, TextField messageTextField, Button sendMessageButton){
        this.controller = controller;
        this.chat = chat;
        this.messageTextField = messageTextField;
        this.sendMessageButton = sendMessageButton;

        chat.setWrapText(true);

//        messageTextField.textProperty().addListener((observable, oldValue, newValue) -> {
//
//        });

        sendMessageButton.setOnMouseClicked(event -> {
            if(!messageTextField.getText().isEmpty()){
                //addMessage("Your", messageTextField.getText());
                controller.sendChatMessage(messageTextField.getText());
            }
        });

    }

    public void addMessage(String login, String message){
        Date date = new Date();

        if(!chat.getText().isEmpty())
            chat.setText(chat.getText() + "\n" + login + ": " + message);
        else
            chat.setText(login + ": " + message);
    }
    public void addMessage(String message){
        if(!chat.getText().isEmpty())
            chat.setText(chat.getText() + "\n" + message);
        else
            chat.setText(message);

        chat.positionCaret(chat.getText().length());
    }

    public void sendMessage(String message){

    }
}
