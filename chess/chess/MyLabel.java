/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import javafx.scene.control.Label;
import javafx.scene.text.Font;

/**
 *
 * @author 2016-e-prusek
 */
public class MyLabel {

    Label label = new Label();

    public MyLabel(String text) {

        label.setText(text);
        label.setFont(new Font("Cambria", 32));
    }
     public MyLabel(String text, int font) {

        label.setText(text);
        label.setFont(new Font("Cambria", font));
    }
}
