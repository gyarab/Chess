/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

/**
 *
 * @author Tomáš
 */
public class ServerThread extends Thread {

    public void run() {
         ChessServer server = new ChessServer();
    }
}
