/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 *
 * @author Tomáš
 */
// vlakno, ktere se stara o aktualizaci casu
public class Timer extends Thread {

    Button exit;
    Label x;
    int time;
    int move;
    ImageView victory;
    ArrayList<ImageView> tiles;

    public Timer(Label x, int move, ImageView victory, ArrayList<ImageView> tiles, Button exit) {
        this.x = x;
        String[] t = x.getText().split(":");
        this.time = Integer.parseInt(t[0]) * 60 + Integer.parseInt(t[1]);//Integer.parseInt(x.getText());
        this.move = move;
        this.victory = victory;
        this.tiles = tiles;
        this.exit = exit;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                time--;
                Platform.runLater(new Runnable() {// tento radek je tu proto, aby toto vlakno mohlo zasahovat do vlakna graficke aplikace
                    public void run() {
                        if (time % 60 < 10) {
                            x.setText(Integer.toString(time / 60) + ":0" + Integer.toString(time % 60));
                        } else {
                            x.setText(Integer.toString(time / 60) + ":" + Integer.toString(time % 60));
                        }
                        if (time == 0) {
                            victory.setVisible(true);
                            for (int i = 0; i < 64; i++) {
                                tiles.get(i).setDisable(true);
                                exit.setVisible(true);
                            }

                        }
                    }
                });

            } catch (InterruptedException ex) {
                Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
