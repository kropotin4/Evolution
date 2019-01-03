package view.gui;

import control.ControllerGUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

public class Chat extends VBox {

    String headerHTML = "";
    String text = "";
    String endHTML = "<script>window.scrollTo(0, document.body.scrollHeight);</script></body></html>";

    @FXML WebView text_chat_rp;
    @FXML TextField text_input_rp;
    @FXML Button send_button_chat_rp;

    public Chat(){

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/Chat.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    @FXML
    private void initialize(){
        String css = null;
        try {
            css = new String(Files.readAllBytes(Paths.get(getClass().getResource("/chat_style.css").toURI())), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        headerHTML =
                "<html>" +
                        "<head>" +
                        "<style>\n" +
                        css +
                        "</style>" +
                        "</head>" +
                    "<body contentEditable=\"false\">";
    }

    public Button getSendButton(){
        return send_button_chat_rp;
    }
    public TextField getTextInputField(){
        return text_input_rp;
    }


    public void addMessage(String login, String message){

        text = text.concat("<xmp>" + login + ": " + message + "</xmp>");
        update();

        //text_chat_rp.positionCaret(text_chat_rp.getHtmlText().length());
    }
    public void addMessage(String message){

        text = text.concat("<font color=\"anton\"><xmp>" + message + "</xmp></font>");
        update();

        //text_chat_rp.positionCaret(text_chat_rp.getHtmlText().length());
    }

    public void update(){
        Platform.runLater(() -> text_chat_rp.getEngine().loadContent(headerHTML + text + endHTML));
    }

}
