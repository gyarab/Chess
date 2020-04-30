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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 *
 * @author Tomáš
 */
public class ChessboardMP {

    // promenne, ve kterych je ulozeno, jestli jeste lze provest rosadu
    boolean castlingWhite = true;
    boolean castlingBlack = true;
    boolean castlingWhiteLeft = true;
    boolean castlingBlackLeft = true;
    boolean castlingWhiteRight = true;
    boolean castlingBlackRight = true;

    // tlacitko pro opusteni hry
    Button exit;

    // promenne a tridy potrebne pro cas
    int timeBlack = 900;
    int timeWhite = 900;
    int moveLimit = 15;
    Label time = new Label();
    Label time2 = new Label();

    Timer tb;
    Timer tw;

    // potrebne pro komunikaci se serverem
    Socket clientSocket;
    BufferedWriter out;
    BufferedReader in;
    String end = "false";
    String changes = "";

    int previousTile = -1;
    MovesCheck check = new MovesCheck();

    ArrayList<ImageView> tiles = new ArrayList();
    ArrayList<Image> images = new ArrayList();
    ArrayList<ImageView> moves = new ArrayList();
    ArrayList<ImageView> promotion = new ArrayList();
    Class<?> clazz = this.getClass();
    VBox finalRoot = new VBox();
    StackPane rootFinal = new StackPane();
    HBox reallyFinalRoot = new HBox();

    ChessboardMP(int size, int tBlack, int tWhite, int moveLim, String IP) {
        // prijeti nastaveni kdyz se hrac pripojuje, nebo spusteni serveru a odeslani nastaveni druhemu hraci
        if ("HOST".equals(IP.toUpperCase())) {
            timeBlack = tBlack * 60;
            timeWhite = tWhite * 60;
            moveLimit = moveLim;
            try {
                ServerThread serverThread = new ServerThread();
                serverThread.start();
                clientSocket = new Socket("localhost", 8080);
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out.write(timeBlack + "\n");
                out.flush();

                out.write(timeWhite + "\n");
                out.flush();

                out.write(moveLimit + "\n");
                out.flush();

            } catch (IOException ex) {
                System.out.println("Server není spuštěný");
            }
        } else {

            try {
                clientSocket = new Socket(IP, 8080);
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                timeBlack = Integer.parseInt(in.readLine());
                timeWhite = Integer.parseInt(in.readLine());
                moveLimit = Integer.parseInt(in.readLine());
            } catch (IOException ex) {
                System.exit(0);
            }

        }

        // vytvoreni sachovnice
        Canvas canvas = new Canvas(410, 410);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BROWN);
        gc.setLineWidth(5.0);
        gc.strokeRect(5, 5, 400, 400);
        for (int j = 0; j < 8; j++) {
            for (int k = 0; k < 8; k++) {
                if ((k + j) % 2 == 0) {
                    gc.setFill(Color.WHITE);
                }
                if ((k + j) % 2 != 0) {
                    gc.setFill(Color.BLACK);
                }
                gc.fillRect(5 + k * 50, 5 + j * 50, 50, 50);

            }
        }

        // nacteni obrazku a vlozeni do Image
        InputStream inPossMove = clazz.getResourceAsStream("images/PossibleMove.png");
        InputStream inBishopBlack = clazz.getResourceAsStream("images/BishopBlack.png");
        InputStream inKingBlack = clazz.getResourceAsStream("images/KingBlack.png");
        InputStream inKnightBlack = clazz.getResourceAsStream("images/KnightBlack.png");
        InputStream inPinchBlack = clazz.getResourceAsStream("images/PinchBlack.png");
        InputStream inQueenBlack = clazz.getResourceAsStream("images/QueenBlack.png");
        InputStream inTowerBlack = clazz.getResourceAsStream("images/TowerBlack.png");
        InputStream inBishopWhite = clazz.getResourceAsStream("images/BishopWhite.png");
        InputStream inKingWhite = clazz.getResourceAsStream("images/KingWhite.png");
        InputStream inKnightWhite = clazz.getResourceAsStream("images/KnightWhite.png");
        InputStream inPinchWhite = clazz.getResourceAsStream("images/PinchWhite.png");
        InputStream inQueenWhite = clazz.getResourceAsStream("images/QueenWhite.png");
        InputStream inTowerWhite = clazz.getResourceAsStream("images/TowerWhite.png");
        InputStream inWhiteVictory = clazz.getResourceAsStream("images/WhiteVictory.png");
        InputStream inBlackVictory = clazz.getResourceAsStream("images/BlackVictory.png");
        InputStream inPat = clazz.getResourceAsStream("images/Pat.png");

        Image imPossMove = new Image(inPossMove);
        Image imBishopBlack = new Image(inBishopBlack);
        Image imKingBlack = new Image(inKingBlack);
        Image imKnightBlack = new Image(inKnightBlack);
        Image imPinchBlack = new Image(inPinchBlack);
        Image imQueenBlack = new Image(inQueenBlack);
        Image imTowerBlack = new Image(inTowerBlack);
        Image imBishopWhite = new Image(inBishopWhite);
        Image imKingWhite = new Image(inKingWhite);
        Image imKnightWhite = new Image(inKnightWhite);
        Image imPinchWhite = new Image(inPinchWhite);
        Image imQueenWhite = new Image(inQueenWhite);
        Image imTowerWhite = new Image(inTowerWhite);
        Image imWhiteVictory = new Image(inWhiteVictory);
        Image imBlackVictory = new Image(inBlackVictory);
        Image imPat = new Image(inPat);

        images.add(imQueenWhite);
        images.add(imTowerWhite);
        images.add(imBishopWhite);
        images.add(imKnightWhite);
        images.add(imQueenBlack);
        images.add(imTowerBlack);
        images.add(imBishopBlack);
        images.add(imKnightBlack);
        images.add(imPossMove);

        // vytvoreni casti, ktera zobrazuje vyhozene figurky a cas jednotlivych hracu
        VBox statsBlack = new VBox();
        statsBlack.setSpacing(20);
        HBox stLine = new HBox();
        HBox ndLine = new HBox();
        stLine.setAlignment(Pos.CENTER);
        ndLine.setAlignment(Pos.CENTER);
        Label name = new Label();
        name.setFont(new Font("Arial", 30));
        name.setText("Black Player:");
        time.setFont(new Font("Arial", 30));
        time.setText(Integer.toString(timeBlack / 60) + ":0" + Integer.toString(timeBlack % 60));
        stLine.getChildren().add(name);
        stLine.getChildren().add(time);
        statsBlack.getChildren().add(stLine);
        statsBlack.getChildren().add(ndLine);
        statsBlack.setAlignment(Pos.CENTER);

        VBox statsWhite = new VBox();
        statsWhite.setSpacing(20);
        HBox stLine2 = new HBox();
        HBox ndLine2 = new HBox();
        stLine2.setAlignment(Pos.CENTER);
        ndLine2.setAlignment(Pos.CENTER);
        Label name2 = new Label();
        name2.setFont(new Font("Arial", 30));
        name2.setText("White Player:");
        time2.setFont(new Font("Arial", 30));
        time2.setText(Integer.toString(timeWhite / 60) + ":0" + Integer.toString(timeWhite % 60));
        stLine2.getChildren().add(name2);
        stLine2.getChildren().add(time2);
        statsWhite.getChildren().add(stLine2);
        statsWhite.getChildren().add(ndLine2);
        statsWhite.setAlignment(Pos.CENTER);

        // vytvoreni cislovani a pismenkovani sachovnice
        HBox root = new HBox();
        HBox columns = new HBox(41);
        HBox columns2 = new HBox(41);
        VBox lines = new VBox(33);
        VBox lines2 = new VBox(33);

        for (int k = 0; k < 8; k++) {
            lines.getChildren().add(new Label(Integer.toString(8 - k)));
            lines2.getChildren().add(new Label(Integer.toString(8 - k)));
            columns.getChildren().add(new Label(String.valueOf((char) (k + 65))));
            columns2.getChildren().add(new Label(String.valueOf((char) (k + 65))));

        }

        // spojeni jednotlivych prvku sachovnice bez figurek
        lines.setAlignment(Pos.CENTER);
        lines2.setAlignment(Pos.CENTER);
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(lines);
        root.getChildren().add(canvas);
        root.getChildren().add(lines2);

        VBox movesChessboard = new VBox();
        VBox rootFinalChessboard = new VBox();

        columns.setAlignment(Pos.CENTER);
        columns2.setAlignment(Pos.CENTER);
        rootFinalChessboard.getChildren().add(columns);
        rootFinalChessboard.getChildren().add(root);
        rootFinalChessboard.getChildren().add(columns2);

        VBox chessboardFigures = new VBox();

        ImageView imvWhiteVictory = new ImageView(imWhiteVictory);
        ImageView imvBlackVictory = new ImageView(imBlackVictory);
        ImageView imvPat = new ImageView(imPat);
        imvWhiteVictory.setVisible(false);
        imvBlackVictory.setVisible(false);
        imvPat.setVisible(false);

        ImageView padding = new ImageView(imBishopWhite);
        padding.setVisible(false);
        ndLine.getChildren().add(padding);
        ImageView padding2 = new ImageView(imBishopWhite);
        padding2.setVisible(false);
        ndLine2.getChildren().add(padding2);

        // tlacitko pro opusteni hry, ktere se zobrazi po skonceni hry
        exit = new Button();
        exit.setText("Ukončit hru");
        exit.setVisible(false);

        // vytvoreni vlaken pro mereni casu a prijem tahu ze serveru
        tb = new Timer(time, moveLimit, imvWhiteVictory, tiles, exit);
        tw = new Timer(time2, moveLimit, imvBlackVictory, tiles, exit);
        Receiver r = new Receiver(tiles, images, in, imvWhiteVictory, imvBlackVictory, imvPat, ndLine, ndLine2, IP, tb, tw, moveLimit, time, time2, exit);
        if (!IP.toUpperCase().equals("HOST")) {
            r.start();
        }
        tw.start();
        tb.start();
        tw.suspend();
        tb.suspend();

        // vytvoreni tlacitek pro povyseni pesce, jejich pridani do arrayListu a spravce zobrazeni a nastaveni jejich akce
        promotion.add(new ImageView(imQueenWhite));
        promotion.add(new ImageView(imTowerWhite));
        promotion.add(new ImageView(imBishopWhite));
        promotion.add(new ImageView(imKnightWhite));
        promotion.add(new ImageView(imQueenWhite));
        promotion.add(new ImageView(imTowerWhite));
        promotion.add(new ImageView(imBishopWhite));
        promotion.add(new ImageView(imKnightWhite));
        promotion.add(new ImageView(imQueenBlack));
        promotion.add(new ImageView(imTowerBlack));
        promotion.add(new ImageView(imBishopBlack));
        promotion.add(new ImageView(imKnightBlack));
        promotion.add(new ImageView(imQueenBlack));
        promotion.add(new ImageView(imTowerBlack));
        promotion.add(new ImageView(imBishopBlack));
        promotion.add(new ImageView(imKnightBlack));

        VBox leftPromotion = new VBox(300);
        HBox leftTopPromotion = new HBox();
        HBox leftBottomPromotion = new HBox();
        VBox rightPromotion = new VBox(300);
        HBox rightTopPromotion = new HBox();
        HBox rightBottomPromotion = new HBox();
        for (int i = 0; i < 4; i++) {
            leftTopPromotion.getChildren().add(promotion.get(i));
            rightTopPromotion.getChildren().add(promotion.get(7 - i));
            leftBottomPromotion.getChildren().add(promotion.get(i + 8));
            rightBottomPromotion.getChildren().add(promotion.get(15 - i));
            leftTopPromotion.setVisible(false);
            rightTopPromotion.setVisible(false);
            leftBottomPromotion.setVisible(false);
            rightBottomPromotion.setVisible(false);
        }
        for (int i = 0; i < 16; i++) {

            promotion.get(i).addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                ImageView v = (ImageView) event.getSource();
                int a = promotion.indexOf(v);
                int b = 0;
                if (a < 8) {
                    for (int j = 0; j < 8; j++) {
                        if (tiles.get(j).getId().contains("wp")) {
                            b = j;
                            break;
                        }
                    }
                    if (a % 4 == 0) {
                        tiles.get(b).setImage(imQueenWhite);
                        tiles.get(b).setId("wq" + tiles.get(b).getId().substring(2));
                        changes += " wq";
                    }
                    if (a % 4 == 1) {
                        tiles.get(b).setImage(imTowerWhite);
                        tiles.get(b).setId("wt" + tiles.get(b).getId().substring(2));
                        changes += " wt";
                    }
                    if (a % 4 == 2) {
                        tiles.get(b).setImage(imBishopWhite);
                        tiles.get(b).setId("wg" + tiles.get(b).getId().substring(2));
                        changes += " wg";
                    }
                    if (a % 4 == 3) {
                        tiles.get(b).setImage(imKnightWhite);
                        tiles.get(b).setId("wh" + tiles.get(b).getId().substring(2));
                        changes += " wh";
                    }

                } else {
                    for (int j = 56; j < 64; j++) {
                        if (tiles.get(j).getId().contains("bp")) {
                            b = j;
                            break;
                        }
                    }
                    if (a % 4 == 0) {
                        tiles.get(b).setImage(imQueenBlack);
                        tiles.get(b).setId("bq" + tiles.get(b).getId().substring(2));
                        changes += " bq";
                    }
                    if (a % 4 == 1) {
                        tiles.get(b).setImage(imTowerBlack);
                        tiles.get(b).setId("bt" + tiles.get(b).getId().substring(2));
                        changes += " bt";
                    }
                    if (a % 4 == 2) {
                        tiles.get(b).setImage(imBishopBlack);
                        tiles.get(b).setId("bg" + tiles.get(b).getId().substring(2));
                        changes += " bg";
                    }
                    if (a % 4 == 3) {
                        tiles.get(b).setImage(imKnightBlack);
                        tiles.get(b).setId("bh" + tiles.get(b).getId().substring(2));
                        changes += " bh";
                    }

                }

                try {
                    out.write(changes + "\n");
                    out.flush();
                    out.write(end + "\n");
                    out.flush();
                    changes = "";
                    if (!r.isAlive()) {
                        r.start();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ChessboardMP.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (!end.equals("false")) {
                    tb.stop();
                    tw.stop();
                    exit.setVisible(true);
                }
                // zapsani do statistiky pokud hra skoncila vcetne vytvoreni noveho souboru
                if (end.contains("victory")) {
                    File f = new File("src/statistics.txt");
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(f));

                        int ratio = Integer.parseInt(br.readLine());
                        int victory = Integer.parseInt(br.readLine());
                        int defeat = Integer.parseInt(br.readLine());
                        victory++;
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
                            bw.write("100");
                            bw.newLine();
                            bw.write("1");
                            bw.newLine();
                            bw.write("0");
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

                // zmena aktivniho timeru
                if ("HOST".equals(IP.toUpperCase())) {

                    tw.suspend();
                    tb.resume();
                    tb.time += moveLimit;
                } else {

                    tb.suspend();
                    tw.resume();
                    tw.time += moveLimit;
                }

                leftTopPromotion.setVisible(false);
                rightTopPromotion.setVisible(false);
                leftBottomPromotion.setVisible(false);
                rightBottomPromotion.setVisible(false);
            });

        }
        leftPromotion.getChildren().add(leftTopPromotion);
        leftPromotion.getChildren().add(leftBottomPromotion);
        rightPromotion.getChildren().add(rightTopPromotion);
        rightPromotion.getChildren().add(rightBottomPromotion);
        rightPromotion.setAlignment(Pos.CENTER);
        leftPromotion.setAlignment(Pos.CENTER);

        // vytvoreni ctverce 8X8 z obrazku mozneho tahu, slouzi k zobrazeni toho, kam muze hrac tahnout, nachazi se mezi vrstvou figurek a sachovnici
        for (int j = 0; j < 8; j++) {
            HBox boardLine = new HBox();
            for (int k = 0; k < 8; k++) {
                ImageView tile = new ImageView(imPossMove);
                tile.setVisible(false);
                boardLine.getChildren().add(tile);
                moves.add(tile);

            }
            boardLine.setAlignment(Pos.CENTER);
            movesChessboard.getChildren().add(boardLine);
        }
        movesChessboard.setAlignment(Pos.CENTER);

        // vytvoreni vrstvy, ktera jiz obsahuje samotne sachove figurky
        HBox boardLine1 = new HBox();
        HBox boardLine2 = new HBox();
        HBox boardLine7 = new HBox();
        HBox boardLine8 = new HBox();
        tiles.add(new ImageView(imTowerBlack));
        tiles.add(new ImageView(imKnightBlack));
        tiles.add(new ImageView(imBishopBlack));
        tiles.add(new ImageView(imQueenBlack));
        tiles.add(new ImageView(imKingBlack));
        tiles.add(new ImageView(imBishopBlack));
        tiles.add(new ImageView(imKnightBlack));
        tiles.add(new ImageView(imTowerBlack));

        for (int i = 0; i < 8; i++) {
            boardLine1.getChildren().add(tiles.get(i));

        }
        boardLine1.setAlignment(Pos.CENTER);
        chessboardFigures.getChildren().add(boardLine1);

        for (int j = 0; j < 8; j++) {
            ImageView tile;

            tile = new ImageView(imPinchBlack);
            tile.setId("bp");
            boardLine2.getChildren().add(tile);
            tiles.add(tile);
        }
        boardLine2.setAlignment(Pos.CENTER);

        chessboardFigures.getChildren().add(boardLine2);

        for (int j = 0; j < 4; j++) {
            HBox boardLines = new HBox();
            for (int k = 0; k < 8; k++) {

                ImageView tile = new ImageView(imPossMove);
                tile.setId("fs");
                tile.setVisible(false);
                boardLines.getChildren().add(tile);
                tiles.add(tile);

            }
            boardLines.setAlignment(Pos.CENTER);

            chessboardFigures.getChildren().add(boardLines);
            chessboardFigures.setAlignment(Pos.CENTER);

        }
        for (int j = 0; j < 8; j++) {
            ImageView tile;

            tile = new ImageView(imPinchWhite);
            tile.setId("wp");
            boardLine7.getChildren().add(tile);
            tiles.add(tile);

        }
        tiles.add(new ImageView(imTowerWhite));
        tiles.add(new ImageView(imKnightWhite));
        tiles.add(new ImageView(imBishopWhite));
        tiles.add(new ImageView(imQueenWhite));
        tiles.add(new ImageView(imKingWhite));
        tiles.add(new ImageView(imBishopWhite));
        tiles.add(new ImageView(imKnightWhite));
        tiles.add(new ImageView(imTowerWhite));

        for (int i = 56; i < 64; i++) {
            boardLine8.getChildren().add(tiles.get(i));

        }

        // nastaveni prvni casti ID
        tiles.get(0).setId("bt");
        tiles.get(1).setId("bh");
        tiles.get(2).setId("bg");
        tiles.get(3).setId("bq");
        tiles.get(4).setId("bk");
        tiles.get(5).setId("bg");
        tiles.get(6).setId("bh");
        tiles.get(7).setId("bt");
        tiles.get(56).setId("wt");
        tiles.get(57).setId("wh");
        tiles.get(58).setId("wg");
        tiles.get(59).setId("wq");
        tiles.get(60).setId("wk");
        tiles.get(61).setId("wg");
        tiles.get(62).setId("wh");
        tiles.get(63).setId("wt");

        for (int i = 0; i < 64; i++) {

            tiles.get(i).setId(tiles.get(i).getId() + Integer.toString(i));
            tiles.get(i).addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                // ID figurky, na kterou se kliknulo
                int Id = Integer.parseInt(event.getPickResult().getIntersectedNode().getId().substring(2));
                // akce, ktera se stane, kdyz se klikne na figurku a ne na moznost, kam se muze pohnout
                if (Id == previousTile) {
                    // akce, ktera se stane, kdyz se klikne na figurku a ne na moznost, kam se muze pohnout
                } else if (previousTile == -1 || (!tiles.get(Id).getId().contains("fs") && (tiles.get(previousTile).getId().contains("b") == tiles.get(Id).getId().contains("b") || tiles.get(previousTile).getId().contains("w") == tiles.get(Id).getId().contains("w")))) {

                    for (int j = 0; j < 64; j++) {
                        if (tiles.get(j).getId().contains("fs")) {
                            tiles.get(j).setVisible(false);
                        }
                        moves.get(j).setVisible(false);
                    }

                    MovesCheck c = new MovesCheck();

                    ArrayList<Integer> poss = c.checkMoves(Id, tiles);

                    // kod, ktery overuje, jestli do moznych tahu lze pridat i rosadu
                    if (tiles.get(Id).getId().contains("wk") && castlingWhite == true) {
                        if (castlingWhiteLeft) {
                            if (tiles.get(57).getId().contains("fs") && tiles.get(58).getId().contains("fs") && tiles.get(59).getId().contains("fs")) {
                                ArrayList<Integer> arr = new ArrayList();
                                arr.add(58);
                                arr.add(59);
                                arr.add(60);
                                if (c.notCheckWhite(60, tiles, arr).size() == 3) {
                                    poss.add(58);
                                }
                            }
                        }

                        if (castlingWhiteRight) {
                            if (tiles.get(61).getId().contains("fs") && tiles.get(62).getId().contains("fs")) {
                                ArrayList<Integer> arr = new ArrayList();
                                arr.add(60);
                                arr.add(61);
                                arr.add(62);
                                if (c.notCheckWhite(60, tiles, arr).size() == 3) {
                                    poss.add(62);
                                }
                            }
                        }
                    }
                    if (tiles.get(Id).getId().contains("bk") && castlingBlack == true) {
                        if (castlingBlackLeft) {
                            if (tiles.get(1).getId().contains("fs") && tiles.get(2).getId().contains("fs") && tiles.get(3).getId().contains("fs")) {
                                ArrayList<Integer> arr = new ArrayList();
                                arr.add(2);
                                arr.add(3);
                                arr.add(4);
                                if (c.notCheckBlack(4, tiles, arr).size() == 3) {
                                    poss.add(2);
                                }
                            }
                        }
                        if (castlingBlackRight) {
                            if (tiles.get(5).getId().contains("fs") && tiles.get(6).getId().contains("fs")) {
                                ArrayList<Integer> arr = new ArrayList();
                                arr.add(4);
                                arr.add(5);
                                arr.add(6);
                                if (c.notCheckBlack(4, tiles, arr).size() == 3) {
                                    poss.add(6);
                                }
                            }
                        }
                    }
                    // zobrazeni toho, kam lze tahnout
                    if (poss.size() > 0) {

                        for (int j = 0; j < poss.size(); j++) {
                            moves.get(poss.get(j)).setVisible(true);
                            tiles.get(poss.get(j)).setVisible(true);
                            tiles.get(poss.get(j)).setDisable(false);
                        }
                        previousTile = Id;

                    }
                    // co se stane, kdyz hrac neklikne na figurku, ale jiz na pozici, kam lze s oznacenou figurkou tahnout
                } else {
                    for (int j = 0; j < tiles.size(); j++) {
                        tiles.get(j).setDisable(true);
                    }
                    if (tiles.get(Id).getId().contains("w")) {
                        ndLine.getChildren().add(new ImageView(tiles.get(Id).getImage()));
                    } else if (tiles.get(Id).getId().contains("b")) {
                        ndLine2.getChildren().add(new ImageView(tiles.get(Id).getImage()));
                    }

                    // provedeni rosady
                    if (tiles.get(previousTile).getId().contains("wk") && previousTile == 60 && (Id == 58 || Id == 62)) {
                        tiles.get(previousTile).setImage(imPossMove);
                        tiles.get(previousTile).setId("fs" + Integer.toString(previousTile));
                        tiles.get(Id).setImage(imKingWhite);
                        tiles.get(Id).setId("wk" + Integer.toString(Id));
                        changes += "60";

                        if (Id == 58) {
                            tiles.get(59).setImage(imTowerWhite);
                            tiles.get(59).setId("wt" + Integer.toString(59));
                            tiles.get(56).setImage(imPossMove);
                            tiles.get(56).setId("fs" + Integer.toString(56));
                            changes += " 58";
                            changes += " 56";
                            changes += " 59";
                        }
                        if (Id == 62) {
                            tiles.get(61).setImage(imTowerWhite);
                            tiles.get(61).setId("wt" + Integer.toString(61));
                            tiles.get(63).setImage(imPossMove);
                            tiles.get(63).setId("fs" + Integer.toString(63));
                            changes += " 62";
                            changes += " 63";
                            changes += " 61";
                        }
                    } else if (tiles.get(previousTile).getId().contains("bk") && previousTile == 4 && (Id == 6 || Id == 2)) {
                        tiles.get(previousTile).setImage(imPossMove);
                        tiles.get(previousTile).setId("fs" + Integer.toString(previousTile));
                        tiles.get(Id).setImage(imKingBlack);
                        tiles.get(Id).setId("bk" + Integer.toString(Id));
                        changes += "4";
                        if (Id == 6) {
                            tiles.get(5).setImage(imTowerBlack);
                            tiles.get(5).setId("bt" + Integer.toString(5));
                            tiles.get(7).setImage(imPossMove);
                            tiles.get(7).setId("fs" + Integer.toString(7));
                            changes += " 6";
                            changes += " 7";
                            changes += " 5";
                        }
                        if (Id == 2) {
                            tiles.get(3).setImage(imTowerBlack);
                            tiles.get(3).setId("bt" + Integer.toString(3));
                            tiles.get(0).setImage(imPossMove);
                            tiles.get(0).setId("fs" + Integer.toString(0));
                            changes += " 2";
                            changes += " 0";
                            changes += " 3";
                        }
                        // provedeni standardniho tahu
                    } else {
                        tiles.get(Id).setId(tiles.get(previousTile).getId().substring(0, 2) + Integer.toString(Id));
                        tiles.get(Id).setImage(tiles.get(previousTile).getImage());
                        tiles.get(previousTile).setImage(imPossMove);
                        tiles.get(previousTile).setId("fs" + tiles.get(previousTile).getId().substring(2));
                        changes += Integer.toString(previousTile);
                        changes += " " + Integer.toString(Id);
                    }

                    // zneviditelneni zobrazenych moznych tahu
                    for (int j = 0; j < 64; j++) {
                        if (tiles.get(j).getId().contains("fs")) {
                            tiles.get(j).setVisible(false);
                        }
                        moves.get(j).setVisible(false);
                    }

                    // kontrola jestli neskončila hra - mat, pat
                    ArrayList<Integer> a = new ArrayList();
                    MovesCheck c = new MovesCheck();
                    for (int j = 0; j < 64; j++) {
                        if (tiles.get(Id).getId().contains("b") && tiles.get(j).getId().contains("w")) {
                            a.addAll(c.checkMoves(j, tiles));
                        } else if (tiles.get(Id).getId().contains("w") && tiles.get(j).getId().contains("b")) {
                            a.addAll(c.checkMoves(j, tiles));
                        }
                    }
                    ArrayList<Integer> arr = new ArrayList();
                    arr.add(1);
                    if (a.isEmpty() && tiles.get(Id).getId().contains("b")) {
                        if (!c.notCheckWhite(1, tiles, arr).isEmpty()) {
                            end = "pat";
                            imvPat.setVisible(true);
                        } else {
                            imvBlackVictory.setVisible(true);
                            end = "victoryb";
                        }
                    } else if (a.isEmpty() && tiles.get(Id).getId().contains("w")) {
                        if (!c.notCheckBlack(1, tiles, arr).isEmpty()) {
                            end = "pat";
                            imvPat.setVisible(true);
                        } else {
                            imvWhiteVictory.setVisible(true);
                            end = "victoryw";
                        }
                    }

                    // kontrola, jestli se v dalsich tazich jeste smi provest rosada
                    if (!tiles.get(60).getId().contains("wk")) {
                        castlingWhite = false;
                    }
                    if (!tiles.get(63).getId().contains("wt")) {
                        castlingWhiteRight = false;
                    }
                    if (!tiles.get(56).getId().contains("wt")) {
                        castlingWhiteLeft = false;
                    }
                    if (!tiles.get(4).getId().contains("bk")) {
                        castlingBlack = false;
                    }
                    if (!tiles.get(7).getId().contains("bt")) {
                        castlingBlackRight = false;
                    }
                    if (!tiles.get(0).getId().contains("bt")) {
                        castlingBlackLeft = false;
                    }

                    previousTile = -1;

                    // kontrola jestli nelze povysit pesce a pripadne zobrazeni tlacitek
                    if (tiles.get(Id).getId().contains("wp") && Id < 8 && "HOST".equals(IP.toUpperCase())) {
                        if (Id < 4) {
                            leftTopPromotion.setVisible(true);
                        } else {
                            rightTopPromotion.setVisible(true);
                        }

                        changes += " " + Integer.toString(Id);

                    }
                    if (tiles.get(Id).getId().contains("bp") && Id > 55 && !"HOST".equals(IP.toUpperCase())) {
                        if (Id < 60) {
                            leftBottomPromotion.setVisible(true);
                        } else {
                            rightBottomPromotion.setVisible(true);
                        }

                        changes += " " + Integer.toString(Id);
                    }

                    // odeslani zmen a stavu hry druhemu hraci
                    if (changes.split(" ").length != 3) {
                        try {
                            out.write(changes + "\n");
                            out.flush();
                            out.write(end + "\n");
                            out.flush();
                            changes = "";
                            if (!r.isAlive()) {
                                r.start();
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(ChessboardMP.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (!end.equals("false")) {
                            tb.stop();
                            tw.stop();
                            exit.setVisible(true);
                        }
                        // zapsani do statistiky pokud hra skoncila vcetne vytvoreni noveho souboru
                        if (end.contains("victory")) {
                            File f = new File("statistics.txt");
                            try {
                                BufferedReader br = new BufferedReader(new FileReader(f));

                                int ratio = Integer.parseInt(br.readLine());
                                int victory = Integer.parseInt(br.readLine());
                                int defeat = Integer.parseInt(br.readLine());
                                victory++;
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
                                    bw.write("100");
                                    bw.newLine();
                                    bw.write("1");
                                    bw.newLine();
                                    bw.write("0");
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

                        // zmena aktivniho timeru
                        if ("HOST".equals(IP.toUpperCase())) {

                            tw.suspend();
                            tb.resume();
                            tb.time += moveLimit;
                        } else {

                            tb.suspend();
                            tw.resume();
                            tw.time += moveLimit;
                        }

                    }
                }
                event.consume();

            });
        }

        // nastaveni toho, na ktere figurky lze kliknout (cerne/bile)
        for (int i = 0; i < 16; i++) {

            if ("HOST".equals(IP.toUpperCase())) {
                tiles.get(63 - i).setDisable(false);
            } else {
                tiles.get(63 - i).setDisable(true);
            }
            tiles.get(i).setDisable(true);
        }

        // finalni spojeni vsech grafickych casti dohromady
        boardLine8.setAlignment(Pos.CENTER);

        boardLine7.setAlignment(Pos.CENTER);
        chessboardFigures.getChildren().add(boardLine7);
        chessboardFigures.getChildren().add(boardLine8);

        rootFinal.getChildren().add(rootFinalChessboard);
        rootFinal.getChildren().add(movesChessboard);
        rootFinal.getChildren().add(chessboardFigures);
        rootFinal.getChildren().add(imvBlackVictory);
        rootFinal.getChildren().add(imvWhiteVictory);
        rootFinal.getChildren().add(imvPat);

        rootFinal.setAlignment(Pos.CENTER);

        finalRoot.getChildren().add(exit);
        finalRoot.getChildren().add(statsBlack);
        finalRoot.getChildren().add(rootFinal);
        finalRoot.getChildren().add(statsWhite);
        finalRoot.setAlignment(Pos.CENTER_LEFT);
        reallyFinalRoot.getChildren().add(leftPromotion);
        reallyFinalRoot.getChildren().add(finalRoot);
        reallyFinalRoot.getChildren().add(rightPromotion);

    }

}
