package view.gui;

import com.jfoenix.controls.JFXMasonryPane;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class SoundPane extends JFXMasonryPane {
    private static class Sound{
        private final String defaultName;
        private URI uri;
        private boolean isDefault;
        private boolean mute;
        private float level;
        private float old;

        Sound(String defaultName){
            this.defaultName = defaultName;
            this.uri = null;
            this.isDefault = true;
            this.mute = false;
            this.level = 1;
            this.old = 1;
        }

        void setDefault() {
            isDefault = true;
            mute = false;
        }

        void setOther(URI uri){
            this.uri = uri;
            isDefault = false;
            mute = false;
        }

        void mute(){ mute = true; }
        void unmute(){mute = false; }
        void setLevel(double level) {
            this.old = this.level;
            if (level < 0) level = 0;
            if (level > 1) level = 1;
            this.level = (float)level;
            unmute();
        }

        void playSound() {
            if (mute || masterMute) return;
            try {
                System.out.println("Sound is playing: " + (isDefault? defaultName : uri.toString()));
                AudioInputStream audioInputStream = null;
                if (!isDefault) {
                    try {
                        audioInputStream = AudioSystem.getAudioInputStream(uri.toURL());
                    } catch (Exception ex) {
                        isDefault = true;
                    }
                }
                if (isDefault) {
                    audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/sounds/" + defaultName));
                }
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                FloatControl volumeControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                float realSoundLevel = (volumeControl.getMaximum() - volumeControl.getMinimum()) * level * master + volumeControl.getMinimum();
                System.out.println("realSoundLevel = " + realSoundLevel);
                volumeControl.setValue(realSoundLevel);
                class AudioLineListener implements LineListener{
                    private boolean done = false;
                    @Override
                    public void update(LineEvent event) {
                        LineEvent.Type eventType = event.getType();
                        if (eventType == LineEvent.Type.STOP || eventType == LineEvent.Type.CLOSE) {
                            done = true;
                            notifyAll();
                        }
                    }
                    synchronized void waitUntilDone() throws InterruptedException {
                        while (!done) {
                            wait();
                        }
                    }
                }
                AudioLineListener listener = new AudioLineListener();
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        clip.addLineListener(listener);
                        clip.start();
                        try (clip) {
                            listener.waitUntilDone();
                        } catch (InterruptedException ignored) { }
                    }
                };
                thread.start();
                audioInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private HashMap<String, Sound> sounds = new HashMap<>(6);

    private static float master = 1;
    private static float masterOld = 1;
    private static boolean masterMute = false;

    private Stage soundStage = new Stage();
    private boolean isShow = false;

    private Image icon = new Image("/images/Equalizer-icon.png");

    /////////////////////////

    public SoundPane(){
        ///region default sounds
        sounds.put("eating", new Sound("mouse16.wav"));
        sounds.put("attack", new Sound("rar16.wav"));
        sounds.put("pirating", new Sound("seagull16.wav"));
        sounds.put("dying", new Sound("death16.wav"));
        sounds.put("grazing", new Sound("grazing16.wav"));
        sounds.put("sleeping", new Sound("shh16.wav"));
        ///endregion

        setPrefSize(470, 400);
        setMinSize(getPrefWidth(), getPrefHeight());
        setMaxSize(getPrefWidth(), getPrefHeight());
        setStage();
    }

    private void setDefault(){
        for (Sound sound : sounds.values()){
            sound.setDefault();
        }
    }

    private boolean getDefault(){
        for (Sound sound : sounds.values()){
            if (!sound.isDefault) return false;
        } return true;
    }

    public void playSound(String sound){
        System.out.println("sound is playing");
        sounds.get(sound).playSound();
    }

    public void show(){
    if (!isShow) {
        isShow = true;
        soundStage.show();
    }
    soundStage.setAlwaysOnTop(true);
    soundStage.setAlwaysOnTop(false);
}

    public void close(){
        isShow = false;
        soundStage.close();
    }

    private void setStage(){
        soundStage.setTitle("Настройки звука");
        soundStage.getIcons().add(icon);
        soundStage.setHeight(this.getPrefHeight());
        soundStage.setWidth(this.getPrefWidth());
        soundStage.setMinHeight(this.getMinHeight());
        soundStage.setMinWidth(this.getMinWidth());
        soundStage.setMaxHeight(this.getMaxHeight());
        soundStage.setMaxWidth(this.getMaxWidth());
        soundStage.setScene(new Scene(this, Color.TRANSPARENT));
        soundStage.setOnCloseRequest(event -> close());

        ///region images
        HashMap<String, Image> images = new HashMap<>();
        images.put( "no", new Image("/images/mute.png"));
        images.put( "mute", new Image("/images/no-audio.png"));
        images.put( "low", new Image("/images/low-volume.png"));
        images.put( "medium", new Image("/images/medium-volume.png"));
        images.put( "high", new Image("/images/high-volume.png"));
        images.put( "reset", new Image("/images/restart.png"));
        images.put( "file", new Image("/images/audio-file.png"));
        ///endregion

        ///region cursor
        Image lizardTailImage = new Image("/images/lizard_tail.png");
        Image lizardImage = new Image("/images/lizard_cursor.png");
        Cursor lizardCursor = new ImageCursor(lizardImage, lizardImage.getWidth() / 2, lizardImage.getHeight() / 2);
        Cursor lizardTailCursor = new ImageCursor(lizardTailImage, lizardTailImage.getWidth() / 2, lizardTailImage.getHeight() / 2);
        setCursor(lizardCursor);
        ///endregion

        VBox masterBox = new VBox(10);
        masterBox.setAlignment(Pos.CENTER_LEFT);

        ///region master
        Button masterReset;
        {
            HashMap<String, ImageView> imageViews = new HashMap<>();
            for (HashMap.Entry<String, Image> e: images.entrySet()){
                ImageView cur = new ImageView(e.getValue());
                cur.setFitWidth(30);
                cur.setFitHeight(30);
                imageViews.put(e.getKey(), cur);
            }

            HBox hBox = new HBox(10);
            hBox.setAlignment(Pos.CENTER);
            Label label = new Label("Master");
            label.setMinWidth(120);
            Slider volume = new Slider(0, 1, .70);
            volume.setMinSize(120, 30);
            Button mute = new Button("", imageViews.get("medium"));
            Button defaulter = new Button("Reset all", imageViews.get("reset"));
            masterReset = defaulter;
            ///region cursor
            volume.setOnMousePressed(event -> setCursor(lizardTailCursor));
            defaulter.setOnMousePressed(event -> setCursor(lizardTailCursor));
            mute.setOnMousePressed(event -> setCursor(lizardTailCursor));
            volume.setOnMouseReleased(event -> setCursor(lizardCursor));
            defaulter.setOnMouseReleased(event -> setCursor(lizardCursor));
            mute.setOnMouseReleased(event -> setCursor(lizardCursor));
            ///endregion
            volume.valueProperty().addListener((observable, oldValue, newValue) -> {
                master = (float)(Math.pow(newValue.doubleValue(), 0.1));
                masterOld = (float)(Math.pow(oldValue.doubleValue(), 0.1));
                masterMute = false;
                if (newValue.doubleValue() > .75) mute.setGraphic(imageViews.get("high"));
                else if (newValue.doubleValue() > .50) mute.setGraphic(imageViews.get("medium"));
                else if (newValue.doubleValue() > .25) mute.setGraphic(imageViews.get("low"));
                else mute.setGraphic(imageViews.get("no"));
            });
            defaulter.setOnAction(event -> {
                setDefault();
                defaulter.setDisable(true);
            });
            mute.setOnAction(event -> {
                if (!masterMute) {
                    volume.setValue(0);
                    masterMute = true;
                    mute.setGraphic(imageViews.get("mute"));
                } else {
                    volume.setValue(Math.pow(masterOld, 10));
                    masterMute = false;
                }
            });
            hBox.getChildren().addAll(label, volume, mute, defaulter);
            masterBox.getChildren().addAll(hBox, new HBox());

        }
        ///endregion

        for (Map.Entry<String, Sound> entry : sounds.entrySet()){
            HashMap<String, ImageView> imageViews = new HashMap<>();
            for (HashMap.Entry<String, Image> e: images.entrySet()){
                ImageView cur = new ImageView(e.getValue());
                cur.setFitWidth(30);
                cur.setFitHeight(30);
                imageViews.put(e.getKey(), cur);
            }

            HBox hBox = new HBox(10);
            hBox.setAlignment(Pos.CENTER);
            Label label = new Label("Sound of " + entry.getKey());
            label.setMinWidth(120);
            Slider volume = new Slider(0, 1, 1);
            Button mute = new Button("", imageViews.get("high"));
            Button sound = new Button("", imageViews.get("file"));
            Button defaulter = new Button("", imageViews.get("reset"));
            ///region cursor
            volume.setOnMousePressed(event -> setCursor(lizardTailCursor));
            defaulter.setOnMousePressed(event -> setCursor(lizardTailCursor));
            mute.setOnMousePressed(event -> setCursor(lizardTailCursor));
            volume.setOnMouseReleased(event -> setCursor(lizardCursor));
            defaulter.setOnMouseReleased(event -> setCursor(lizardCursor));
            mute.setOnMouseReleased(event -> setCursor(lizardCursor));
            ///endregion
            volume.valueProperty().addListener((observable, oldValue, newValue) -> {
                entry.getValue().setLevel(Math.pow(newValue.doubleValue(), 0.1));
                if (entry.getValue().mute){
                    mute.fire();
                }
                if (newValue.doubleValue() > .75) mute.setGraphic(imageViews.get("high"));
                else if (newValue.doubleValue() > .50) mute.setGraphic(imageViews.get("medium"));
                else if (newValue.doubleValue() > .25) mute.setGraphic(imageViews.get("low"));
                else mute.setGraphic(imageViews.get("no"));
            });
            mute.setOnAction(event -> {
                if (!entry.getValue().mute) {
                    volume.setValue(0);
                    entry.getValue().mute();
                    mute.setGraphic(imageViews.get("mute"));
                } else {
                    volume.setValue(Math.pow(entry.getValue().old, 10));
                    entry.getValue().unmute();
                }
            });
            defaulter.setDisable(true);
            sound.setOnAction(event -> {
                FileChooser chooser = new FileChooser();
                FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Java supported", "*.wav", ".au", ".aiff");
                chooser.getExtensionFilters().add(extensionFilter);
                File file = chooser.showOpenDialog(soundStage);
                try {
                    entry.getValue().setOther(file.toURI());
                    masterReset.setDisable(false);
                    defaulter.setDisable(false);
                } catch (Exception e){
                    entry.getValue().setDefault();
                    defaulter.setDisable(true);
                }
            });
            defaulter.setOnAction(event -> {
                entry.getValue().setDefault();
                defaulter.setDisable(true);
                if (getDefault()) masterReset.setDisable(true);
            });
            masterReset.disabledProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) defaulter.setDisable(true);
            });
            hBox.getChildren().addAll(label, volume, mute, sound, defaulter);
            masterBox.getChildren().add(hBox);
        }

        this.getChildren().add(masterBox);
    }
}
