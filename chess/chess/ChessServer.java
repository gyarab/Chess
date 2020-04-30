
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Tomáš
 */
public class ChessServer {

    private ServerSocket serverSocket;

    // konstruktor třídy Server
    public ChessServer() {
        try {
            this.serverSocket = new ServerSocket(8080); // inicializace ServerSocketu
            System.out.println("Spuštění serveru proběhlo úspěšně."); // vypsani hlasky pri uspesnem spusteni k serveru
            Socket clientSocket1 = this.serverSocket.accept(); // vytvoreni socketu pro klienta 1
            System.out.println("Klient1 se připojil z adresy " + clientSocket1.getInetAddress().getHostAddress() + "."); // vypsani zpravy o pripojeni klienta
            BufferedReader in1 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream())); // vytvoreni BufferReaderu, se vstupnim proudem ze socketu klienta
            BufferedWriter out1 = new BufferedWriter(new OutputStreamWriter(clientSocket1.getOutputStream())); // vytvoreni BufferWriteru, se vystupnim proudem do socketu klienta

            Socket clientSocket2 = this.serverSocket.accept(); // vytvoreni socketu pro klienta 2
            System.out.println("Klient2 se připojil z adresy " + clientSocket2.getInetAddress().getHostAddress() + "."); // vypsani zpravy o pripojeni klienta
            BufferedReader in2 = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream())); // vytvoreni BufferReaderu, se vstupnim proudem se socketu klienta
            BufferedWriter out2 = new BufferedWriter(new OutputStreamWriter(clientSocket2.getOutputStream()));  // vytvoreni BufferWriteru, se vystupnim proudem do socketu klienta
            // odeslani nastaveni hraci, ktery se pripojil
            for (int i = 0; i < 3; i++) {
                String temp = in1.readLine();
                out2.write(temp + "\n");
                out2.flush();
            }

            // nekonečný while cyklus pro posilani tahu mezi klienty, vzdy se nacte tah a jestli hra skoncila a posle se to druhemu hraci
            while (true) {
                String temp11 = in1.readLine(); // nacteni tahu od klienta 1 do promenne temp1 
                out2.write(temp11 + "\n");
                out2.flush();
                String temp12 = in1.readLine(); // nacteni tahu od klienta 1 do promenne temp1 
                out2.write(temp12 + "\n");
                out2.flush();
                String temp21 = in2.readLine(); // nacteni tahu od klienta 2 do promenne temp2
                out1.write(temp21 + "\n");
                out1.flush();
                String temp22 = in2.readLine(); // nacteni tahu od klienta 2 do promenne temp2
                out1.write(temp22 + "\n");
                out1.flush();
                
                // pokud hra skoncila, tak while-cyklus skonci
                if (temp12.equals("victory") || temp22.equals("victory") || temp12.equals("pat") || temp22.equals("pat")) {
                    break;
                }

            }
            
            // uzavreni BufferedReaderu a Writeru
            out1.close();
            out2.close();
            in1.close();
            in2.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error");
        }
    }

}
