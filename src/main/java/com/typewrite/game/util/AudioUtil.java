package com.typewrite.game.util;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/** Utility class for managing audio playback in the application. */
public class AudioUtil {

  private static MediaPlayer mediaPlayer;
  private static MediaPlayer soundEffectPlayer;
  private static boolean soundEffect = true;

  /**
   * Plays the background music starting with an initial track followed by a looped background
   * track. The initial track is played once and upon completion, the background track begins and
   * loops indefinitely.
   */
  public static void playBackgroundMusic() {
    playInitialTrack(
        "/com/typewrite/game/audio/f1_car.mp3",
        () -> loopBackgroundMusic("/com/typewrite/game/audio/background_home.mp3"));
  }

  /** Plays an explosion sound effect once for bomb feature. */
  public static void playExplosionSoundEffect() {
    try {
      Media media =
          new Media(
              AudioUtil.class
                  .getResource("/com/typewrite/game/audio/explosion.mp3")
                  .toExternalForm());
      soundEffectPlayer = new MediaPlayer(media);
      soundEffectPlayer.setOnEndOfMedia(
          () -> {
            if (soundEffectPlayer != null) {
              soundEffectPlayer.dispose();
              soundEffectPlayer = null;
            }
          });
      soundEffectPlayer.play();
    } catch (Exception e) {
      LogUtil.error("Error playing sound effect: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Stops any currently playing music with a fade-out effect over 2 seconds, then releases the
   * resources.
   */
  public static void stopMusic() {
    if (mediaPlayer != null) {
      final Timeline timeline =
          new Timeline(
              new KeyFrame(
                  Duration.seconds(0),
                  new KeyValue(mediaPlayer.volumeProperty(), mediaPlayer.getVolume())),
              new KeyFrame(Duration.seconds(2), new KeyValue(mediaPlayer.volumeProperty(), 0)));
      timeline.setOnFinished(
          event -> {
            if (mediaPlayer != null) {
              mediaPlayer.stop();
              mediaPlayer.dispose();
              mediaPlayer = null;
            }
          });
      timeline.play();
    } else {
      LogUtil.info("Music is already stopped.");
    }
  }

  /** Indicates if music is currently playing for audio toggling. */
  public static boolean isMusicPlaying() {
    return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
  }

  private static void playInitialTrack(String track, Runnable onEndAction) {
    try {
      Media media = new Media(AudioUtil.class.getResource(track).toExternalForm());
      mediaPlayer = new MediaPlayer(media);
      mediaPlayer.play();
      mediaPlayer.setOnEndOfMedia(onEndAction);
    } catch (Exception e) {
      LogUtil.error("Error playing initial track: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private static void loopBackgroundMusic(String track) {
    try {
      Media media = new Media(AudioUtil.class.getResource(track).toExternalForm());
      mediaPlayer = new MediaPlayer(media);
      mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
      mediaPlayer.play();
    } catch (Exception e) {
      LogUtil.error("Error playing background music: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Plays a sound effect when a button is hovered over.
   *
   * <p>The sound effect file must be in a format supported by JavaFX's MediaPlayer, such as MP3 or
   * WAV.
   */
  public static void playHoverSoundEffect() {
    if (!soundEffect) {
      return;
    }
    try {
      Media media =
          new Media(
              AudioUtil.class
                  .getResource("/com/typewrite/game/audio/sound-effects/hover_button.mp3")
                  .toExternalForm());
      soundEffectPlayer = new MediaPlayer(media);
      soundEffectPlayer.setOnEndOfMedia(
          () -> {
            if (soundEffectPlayer != null) {
              soundEffectPlayer.dispose();
              soundEffectPlayer = null;
            }
          });
      soundEffectPlayer.play();
    } catch (Exception e) {
      LogUtil.error("Error playing hover sound effect: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Plays a sound effect when a button is clicked.
   *
   * <p>The sound effect file must be in a format supported by JavaFX's MediaPlayer, such as MP3 or
   * WAV.
   */
  public static void playClickSoundEffect() {
    if (!soundEffect) {
      return;
    }
    try {
      Media media =
          new Media(
              AudioUtil.class
                  .getResource("/com/typewrite/game/audio/sound-effects/button_click.mp3")
                  .toExternalForm());
      soundEffectPlayer = new MediaPlayer(media);
      soundEffectPlayer.setOnEndOfMedia(
          () -> {
            if (soundEffectPlayer != null) {
              soundEffectPlayer.dispose();
              soundEffectPlayer = null;
            }
          });
      soundEffectPlayer.play();
    } catch (Exception e) {
      LogUtil.error("Error playing click sound effect: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /** Plays a sound effect when a message is shown in the chat. */
  public static void playMessageSoundEffect() {
    if (!soundEffect) {
      return;
    }
    try {
      Media media =
          new Media(
              AudioUtil.class
                  .getResource("/com/typewrite/game/audio/message_alert.mp3")
                  .toExternalForm());
      soundEffectPlayer = new MediaPlayer(media);
      soundEffectPlayer.setOnEndOfMedia(
          () -> {
            if (soundEffectPlayer != null) {
              soundEffectPlayer.dispose();
              soundEffectPlayer = null;
            }
          });
      soundEffectPlayer.play();
    } catch (Exception e) {
      LogUtil.error("Error playing message sound effect: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /** Plays a sound effect when a server message is shown in the chat. */
  public static void playServerSoundEffect() {
    if (!soundEffect) {
      return;
    }
    try {
      Media media =
          new Media(
              AudioUtil.class
                  .getResource("/com/typewrite/game/audio/server_notif.mp3")
                  .toExternalForm());
      soundEffectPlayer = new MediaPlayer(media);
      soundEffectPlayer.setOnEndOfMedia(
          () -> {
            if (soundEffectPlayer != null) {
              soundEffectPlayer.dispose();
              soundEffectPlayer = null;
            }
          });
    } catch (Exception e) {
      LogUtil.error("Error playing message sound effect: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /** Plays a sound effect when a server message is shown in the chat. */
  public static void playMessageSentEffect() {
    if (!soundEffect) {
      return;
    }
    try {
      Media media =
          new Media(
              AudioUtil.class
                  .getResource("/com/typewrite/game/audio/msg_sent.mp3")
                  .toExternalForm());
      soundEffectPlayer = new MediaPlayer(media);
      soundEffectPlayer.setOnEndOfMedia(
          () -> {
            if (soundEffectPlayer != null) {
              soundEffectPlayer.dispose();
              soundEffectPlayer = null;
            }
          });
      soundEffectPlayer.play();
    } catch (Exception e) {
      LogUtil.error("Error playing message sound effect: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public static boolean isSoundEffect() {
    return soundEffect;
  }

  public static void setSoundEffect(boolean soundEffect) {
    AudioUtil.soundEffect = soundEffect;
  }
}
