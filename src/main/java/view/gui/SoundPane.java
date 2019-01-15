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

        Sound(String defaultName){
            this.defaultName = defaultName;
            this.uri = null;
            this.isDefault = true;
            this.mute = false;
            this.level = 1;
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
                    public synchronized void waitUntilDone() throws InterruptedException {
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
                        try { listener.waitUntilDone(); }
                        catch (InterruptedException e) { }
                        finally { clip.close(); }
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
    private static boolean masterMute = false;

    private Stage soundStage = new Stage();
    private boolean isShow = false;

    private Image icon = new Image("/images/Equalizer-icon.png");

    public SoundPane(){
        ///region default sounds
        sounds.put("eating", new Sound("mouse16.wav"));
        sounds.put("attack", new Sound("rar16.wav"));
        sounds.put("pirating", new Sound("seagull16.wav"));
        sounds.put("dying", new Sound("death16.wav"));
        sounds.put("grazing", new Sound("grazing16.wav"));
        sounds.put("sleeping", new Sound("shh16.wav"));
        ///endregion

        setPrefSize(500, 400);
        setMinSize(getPrefWidth(), getPrefHeight());
        setMaxSize(getPrefWidth(), getPrefHeight());
        setStage();
    }

    private void setDefault(){
        for (Sound sound : sounds.values()){
            sound.setDefault();
        }
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
            Slider slider = new Slider(0, 1, .70);
            slider.setMinSize(120, 30);
            Button button = new Button("", imageViews.get("medium"));
            Button defaulter = new Button("Reset all", imageViews.get("reset"));
            slider.setOnMousePressed(event -> setCursor(lizardTailCursor));
            defaulter.setOnMouseEntered(event -> setCursor(lizardTailCursor));
            button.setOnMouseEntered(event -> setCursor(lizardTailCursor));
            defaulter.setOnMouseExited(event -> setCursor(lizardCursor));
            button.setOnMouseExited(event -> setCursor(lizardCursor));
            slider.setOnMouseReleased(event -> {
                setCursor(lizardCursor);
                master = (float)Math.pow(slider.getValue(), 0.1);
                if (masterMute) button.fire();
                /**/ if (slider.getValue() > .75) button.setGraphic(imageViews.get("high"));
                else if (slider.getValue() > .50) button.setGraphic(imageViews.get("medium"));
                else if (slider.getValue() > .25) button.setGraphic(imageViews.get("low"));
                else if (slider.getValue() > .00) button.setGraphic(imageViews.get("no"));
            });
            defaulter.setOnAction(event -> setDefault());
            button.setOnAction(event -> {
                if (!masterMute) {
                    slider.setValue(0);
                    masterMute = true;
                    button.setGraphic(imageViews.get("mute"));
                } else {
                    slider.setValue(Math.pow(master, 10));
                    masterMute = false;
                    /**/ if (slider.getValue() > .75) button.setGraphic(imageViews.get("high"));
                    else if (slider.getValue() > .50) button.setGraphic(imageViews.get("medium"));
                    else if (slider.getValue() > .25) button.setGraphic(imageViews.get("low"));
                    else if (slider.getValue() > .00) button.setGraphic(imageViews.get("no"));
                }
            });
            hBox.getChildren().addAll(label, slider, button, defaulter);
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
            volume.setOnMouseReleased(event -> {
                entry.getValue().setLevel(Math.pow(volume.getValue(), 0.1));
                if (entry.getValue().mute){
                    mute.fire();
                }
                /**/ if (volume.getValue() > .75) mute.setGraphic(imageViews.get("high"));
                else if (volume.getValue() > .50) mute.setGraphic(imageViews.get("medium"));
                else if (volume.getValue() > .25) mute.setGraphic(imageViews.get("low"));
                else if (volume.getValue() > .00) mute.setGraphic(imageViews.get("no"));
            });
            mute.setOnAction(event -> {
                if (!entry.getValue().mute) {
                    volume.setValue(0);
                    entry.getValue().mute();
                    mute.setGraphic(imageViews.get("mute"));
                } else {
                    volume.setValue(Math.pow(entry.getValue().level, 10));
                    entry.getValue().unmute();
                    /**/ if (entry.getValue().level > .75) mute.setGraphic(imageViews.get("high"));
                    else if (entry.getValue().level > .50) mute.setGraphic(imageViews.get("medium"));
                    else if (entry.getValue().level > .25) mute.setGraphic(imageViews.get("low"));
                    else if (entry.getValue().level > .00) mute.setGraphic(imageViews.get("no"));
                }
            });
            Button sound = new Button("", imageViews.get("file"));
            sound.setOnAction(event -> {
                FileChooser chooser = new FileChooser();
                FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Java supported", "*.wav", ".au", ".aiff");
                chooser.getExtensionFilters().add(extensionFilter);
                File file = chooser.showOpenDialog(soundStage);
                entry.getValue().setOther(file.toURI());
            });
            Button defaulter = new Button("", imageViews.get("reset"));
            defaulter.setOnAction(event -> entry.getValue().setDefault());
            hBox.getChildren().addAll(label, volume, mute, sound, defaulter);
            masterBox.getChildren().add(hBox);
        }

        this.getChildren().add(masterBox);
    }
}
