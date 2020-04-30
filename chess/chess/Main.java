/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Tomáš
 */
public class Main extends Application {

    public static String StylTlacitka = "-fx-min-width: 200px;";
    public int size;
    Statistics statistics;

    @Override
    public void start(Stage primaryStage) throws IOException {

        // vytvoreni tlacitek pro menu a nastaveni jejich stylu
        Button gameButton1 = new Button();
        gameButton1.setStyle(StylTlacitka);
        Button gameButton2 = new Button();
        gameButton2.setStyle(StylTlacitka);
        Button statisticsButton = new Button();
        statisticsButton.setStyle(StylTlacitka);
        Button backButton = new Button();
        backButton.setStyle(StylTlacitka);
        Button exitButton = new Button();
        exitButton.setStyle(StylTlacitka);

        // pridani tlacitek do spravce zobrazeni
        VBox menuRoot = new VBox(20);
        menuRoot.getChildren().add(gameButton1);
        menuRoot.getChildren().add(gameButton2);
        menuRoot.getChildren().add(statisticsButton);
        menuRoot.getChildren().add(exitButton);
        menuRoot.setAlignment(Pos.CENTER);

        VBox statisticsRoot = new VBox(400);

        VBox gameRoot1 = new VBox(20);

        // nastaveni velikosti sceny
        Scene scene = new Scene(menuRoot, 1000, 750);
        
        // nastaveni akci tlacitek
        gameButton1.setText("Hra");
        gameButton1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                Settings settings = new Settings(false);
                scene.setRoot(settings.settings);
                settings.back.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        scene.setRoot(menuRoot);
                    }
                });
                settings.submit.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        Chessboard chessboard = new Chessboard(size, settings.tfTimeBlack.getValue(), settings.tfTimeWhite.getValue(), settings.tfTimeMove.getValue());
                        scene.setRoot(chessboard.reallyFinalRoot);
                        chessboard.exit.setOnAction(new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                System.exit(0);

                            }
                        });
                    }
                });

            }
        });
        gameButton2.setText("Hra přes internet");
        gameButton2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                Settings settings = new Settings(true);
                scene.setRoot(settings.settings);
                settings.back.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        scene.setRoot(menuRoot);
                    }
                });
                settings.submit.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {

                        ChessboardMP chessboardMP = new ChessboardMP(size, settings.tfTimeBlack.getValue(), settings.tfTimeWhite.getValue(), settings.tfTimeMove.getValue(), settings.tfIP.getText());
                        scene.setRoot(chessboardMP.reallyFinalRoot);

                        chessboardMP.exit.setOnAction(new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                scene.setRoot(menuRoot);

                            }
                        });
                    }
                });

            }
        });

        statisticsButton.setText("Statistika");
        statisticsButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    statistics = new Statistics("statistics.txt");
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                statisticsRoot.getChildren().add(statistics.statistics);
                statisticsRoot.getChildren().add(backButton);
                statisticsRoot.setAlignment(Pos.CENTER);
                scene.setRoot(statisticsRoot);
            }
        });
        backButton.setText("Zpět");
        backButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                statisticsRoot.getChildren().remove(backButton);
                statisticsRoot.getChildren().remove(statistics.statistics);

                scene.setRoot(menuRoot);
            }
        });
        exitButton.setText("Ukončit hru");
        exitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });

        primaryStage.setTitle("Chess");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
