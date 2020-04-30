/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 *
 * @author Tomáš
 */
// vlakno, ktere prijima tahy poslane serverem a podle nich aktualizuje sachovnici
public class Receiver extends Thread {

    ArrayList<ImageView> tiles;
    ArrayList<Image> images;
    BufferedReader in;
    ImageView victoryw;
    ImageView victoryb;
    ImageView pat;
    String temp1;
    String temp2;
    HBox ndLine;
    HBox ndLine2;
    String IP;
    Timer tb;
    Timer tw;
    Label time1;
    Label time2;
    int moveLimit;
    Button exit;

    public Receiver(ArrayList<ImageView> tiles, ArrayList<Image> images, BufferedReader in, ImageView victoryw, ImageView victoryb,ImageView pat, HBox ndLine, HBox ndLine2, String IP, Timer tb, Timer tw, int moveLimit, Label time1, Label time2, Button exit) {
        this.tiles = tiles;
        this.victoryw = victoryw;
        this.victoryb = victoryb;
        this.pat = pat;
        this.in = in;
        this.images = images;
        this.ndLine = ndLine;
        this.ndLine2 = ndLine2;
        this.IP = IP;
        this.tb = tb;
        this.tw = tw;
        this.time1 = time1;
        this.time2 = time2;
        this.moveLimit = moveLimit;
    }

    public void run() {

        while (true) {
            try {
                
                // nacteni zpravy od serveru, prvni je tah, druhy je stav hry
                temp1 = in.readLine();
                temp2 = in.readLine();

            } catch (IOException ex) {
                Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
            }
            Platform.runLater(new Runnable() {
                public void run() {

                    String[] change = temp1.split(" ");

                    if (tiles.get(Integer.parseInt(change[1])).getId().contains("w")) {
                        ndLine.getChildren().add(new ImageView(tiles.get(Integer.parseInt(change[1])).getImage()));
                    } else if (tiles.get(Integer.parseInt(change[1])).getId().contains("b")) {
                        ndLine2.getChildren().add(new ImageView(tiles.get(Integer.parseInt(change[1])).getImage()));
                    }

                    // provedeni tahu
                    tiles.get(Integer.parseInt(change[1])).setId(tiles.get(Integer.parseInt(change[0])).getId().substring(0, 2) + Integer.toString(Integer.parseInt(change[1])));
                    tiles.get(Integer.parseInt(change[1])).setImage(tiles.get(Integer.parseInt(change[0])).getImage());
                    tiles.get(Integer.parseInt(change[1])).setVisible(true);
                    tiles.get(Integer.parseInt(change[0])).setImage(images.get(8));
                    tiles.get(Integer.parseInt(change[0])).setVisible(false);
                    tiles.get(Integer.parseInt(change[0])).setId("fs" + tiles.get(Integer.parseInt(change[0])).getId().substring(2));

                    // provede se, pokud byla rosada, jelikoz se hybou vice nez dve figurky
                    if (change.length > 2 && change[3].matches("[0-9]+")) {
                        tiles.get(Integer.parseInt(change[3])).setId(tiles.get(Integer.parseInt(change[2])).getId().substring(0, 2) + Integer.toString(Integer.parseInt(change[3])));
                        tiles.get(Integer.parseInt(change[3])).setImage(tiles.get(Integer.parseInt(change[2])).getImage());
                        tiles.get(Integer.parseInt(change[3])).setVisible(true);
                        tiles.get(Integer.parseInt(change[2])).setImage(images.get(8));
                        tiles.get(Integer.parseInt(change[2])).setId("fs" + tiles.get(Integer.parseInt(change[2])).getId().substring(2));
                        tiles.get(Integer.parseInt(change[2])).setVisible(false);
                        // provede se, pokud pesec dosel na druhy konec sachovnice a byl povysen, zajistuje zmenu pesce na jinou figurku
                    } else if (change.length > 2) {
                        tiles.get(Integer.parseInt(change[2])).setId(change[3] + tiles.get(Integer.parseInt(change[2])).getId().substring(2));

                        if (change[3].equals("wq")) {
                            tiles.get(Integer.parseInt(change[2])).setImage(images.get(0));
                        }
                        if (change[3].equals("wt")) {
                            tiles.get(Integer.parseInt(change[2])).setImage(images.get(1));
                        }
                        if (change[3].equals("wg")) {
                            tiles.get(Integer.parseInt(change[2])).setImage(images.get(2));
                        }
                        if (change[3].equals("wh")) {
                            tiles.get(Integer.parseInt(change[2])).setImage(images.get(3));
                        }

                        if (change[3].equals("bq")) {
                            tiles.get(Integer.parseInt(change[2])).setImage(images.get(4));
                        }
                        if (change[3].equals("bt")) {
                            tiles.get(Integer.parseInt(change[2])).setImage(images.get(5));
                        }
                        if (change[3].equals("bg")) {
                            tiles.get(Integer.parseInt(change[2])).setImage(images.get(6));
                        }
                        if (change[3].equals("bh")) {
                            tiles.get(Integer.parseInt(change[2])).setImage(images.get(7));
                        }
                    }
                    
                    // zapsani vysledku hry do statistiky
                    if (temp2.contains("victory")) {
                        File f = new File("statistics.txt");
                        try {
                            BufferedReader br = new BufferedReader(new FileReader(f));

                            int ratio = Integer.parseInt(br.readLine());
                            int victory = Integer.parseInt(br.readLine());
                            int defeat = Integer.parseInt(br.readLine());
                            defeat++;
                            FileOutputStream fos = new FileOutputStream(f);
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                            if (defeat == 0) {
                                bw.write(Integer.toString(100));
                            } else {
                                bw.write(Integer.toString(victory / defeat * 100));
                            }
                            bw.newLine();
                            bw.write(Integer.toString(victory));
                            bw.newLine();
                            bw.write(Integer.toString(defeat));
                            bw.newLine();
                            bw.flush();
                            bw.close();
                        } catch (FileNotFoundException e) {

                            try {
                                f.createNewFile();
                                FileOutputStream fos = new FileOutputStream(f);
                                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                                bw.write("0");
                                bw.newLine();
                                bw.write("0");
                                bw.newLine();
                                bw.write("1");
                                bw.newLine();
                                bw.flush();
                                bw.close();

                            } catch (IOException ex) {
                                Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } catch (IOException ex) {
                            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    // zobrazeni napisu, jak hra skoncila
                    if ("victoryb".equals(temp2)) {
                        victoryb.setVisible(true);

                    } else if ("victoryw".equals(temp2)) {
                        victoryw.setVisible(true);

                    } else if ("pat".equals(temp2)) {
                        pat.setVisible(true);
                    }

                    // odblokovani a zablokovani figurek podle toho, kdo je na tahu
                    for (int j = 0; j < 64; j++) {
                        if (!IP.toUpperCase().equals("HOST") && tiles.get(j).getId().contains("b")) {
                            tiles.get(j).setDisable(false);
                        }
                        if (tiles.get(j).getId().contains("w") && IP.toUpperCase().equals("HOST")) {
                            tiles.get(j).setDisable(false);
                        }
                    }

                    // pozastavovani a spousteni timeru podle toho, kdo je na tahu
                    if ("HOST".equals(IP.toUpperCase())) {

                        tb.suspend();
                        tw.resume();
                        tw.time += 15;
                    } else {

                        tw.suspend();
                        tb.resume();
                        tb.time += 15;

                    }
                    // zastaveni timeru, kdyz hra skoncila
                    if (!temp2.equals("false")) {
                        tb.stop();
                        tw.stop();
                        exit.setVisible(true);

                    }

                }

            }
            );

        }

    }

}
