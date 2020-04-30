/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Tomáš
 */
public class Settings {

    Button submit = new Button();
    Button back = new Button();
    HBox settings = new HBox();

    // vytvoreni policek pro zadani pouze celociselnych hodnot
    Spinner<Integer> tfTimeBlack = new Spinner(1, 1000, 15, 1);
    Spinner<Integer> tfTimeWhite = new Spinner(1, 1000, 15, 1);
    Spinner<Integer> tfTimeMove = new Spinner(1, 1000, 15, 1);

    // textove pole pro zadani IP, kdyz se hrac chce k nekomu pripojit do hry
    TextField tfIP = new TextField();

    boolean mp;

    Settings(boolean mp) {
        // nastaveni stylu policek
        tfTimeBlack.setMinHeight(16);
        tfTimeWhite.setMinHeight(16);
        tfTimeMove.setMinHeight(16);

        this.mp = mp;

        // vytvoreni dvou sloupcu, v prvnim je, co ma hrac zadat a v druhem jsou policka do kterych to zada
        VBox column1 = new VBox(15);
        VBox column2 = new VBox(15);
        column1.getChildren().add(new MyLabel("Čas černého hráče (min):", 20).label);
        column2.getChildren().add(tfTimeBlack);
        column1.getChildren().add(new MyLabel("Čas bílého hráče (min):", 20).label);
        column2.getChildren().add(tfTimeWhite);
        column1.getChildren().add(new MyLabel("Čas na tah (s):", 20).label);
        column2.getChildren().add(tfTimeMove);

        // pokud je hra na dvou ruznych pocitacich, tak se vytvori policko pro IP
        if (mp == true) {

            column1.getChildren().add(new MyLabel("IP (pokud chcete hru hostovat napište HOST, host má bílé figurky a nastavuje hru)", 20).label);
            column2.getChildren().add(tfIP);
        }
        settings.getChildren().add(column1);
        settings.getChildren().add(column2);

        submit.setText("Potvrdit");
        back.setText("Zpět");
        column2.getChildren().add(submit);
        column1.getChildren().add(back);
        column1.setAlignment(Pos.CENTER);
        column2.setAlignment(Pos.CENTER);
        settings.setAlignment(Pos.CENTER);

    }

}
