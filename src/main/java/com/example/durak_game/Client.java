package com.example.durak_game;

import com.example.durak_game.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class Client {

    public Socket socket;
    private BufferedReader in;
    public PrintWriter out;
    public String username;
    public boolean isTurn;
    public boolean usedTrump;
    private WaitController wc;
    private Game2Controller gc;
    private MenuController mc;
    private EndController ec;
    private ArrayList<Card> table;
    private Hand2 hand;
    private Card trumpCard;

    public Client(){
        isTurn = false;
        try {
            socket = new Socket(InetAddress.getLocalHost(), 1234);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(this::receive,"client receive thread").start();
    }


    private void receive() {
        while(true){
            String data = readData();
            System.out.println(data);
            Platform.runLater(
                    () -> parseData(data)
            );
        }
    }

    public void createSession(String name, String s_id) throws NumberFormatException {
        this.username = name;
        String data = "create_session" + "#" + name + "#" + s_id;
        sendData(data);
        System.out.println(s_id);
    }

    public void joinSession(String username, String session_id) throws ClassCastException {
        this.username = username;
        String data = "join_session#" + this.username + "#" + session_id;
        sendData(data);
    }
    private void sendData(String data) {
        this.out.println(data);
        this.out.flush();
    }

    private String readData() {
        try {
            return this.in.readLine();
        } catch (IOException ex) {
            Helper.sendError("Error Occurred in readObject in ClientHandler: " + ex.toString());
        }
        return null;
    }

    private void parseData(String data){
        String[] params = data.split("#");
        String command = params[0];
        switch (command) {
            case "waiting", "join_session_success": {
                openWait();
                break;
            }
            case "started": {
                openGame();
                break;
            }
            case "join_session_failed": {
                Helper.sendError("Невозможно войти в комнату.");
                Platform.exit();
                closeStreams();
                break;
            }
            case "start_game": {
                // start_game#hand#trumpCard#permission
                String[] cards_hand = params[1].split(",");
                setHandFromData(cards_hand);
                String strTrumpCard = params[2];
                Card trumpCard = new Card(strTrumpCard);
                gc.setTrumpCardImage(trumpCard);
                String turn = params[3];
                isTurn = Boolean.parseBoolean(turn);
                gc.setDisableActions(isTurn);
                break;
            }
            case "read_game": {
                //read_game#card1, card2, card3 ...#usedTrump#sizeDeck#endGame,nickname#hand#textAction#permission
                if(!Objects.equals(params[1], "null")) {
                    String[] cards_table = params[1].split(",");
                    setTableFromData(cards_table);
                }
                else {
                    table = null;
                    gc.setTable(new ArrayList<>());
                }
                String bool = params[2];
                usedTrump = Boolean.parseBoolean(bool);
                if(usedTrump) {
                    gc.trumpCardUsed();
                }
                int size = Integer.parseInt(params[3]);
                gc.setSize(size);
                String[] end = params[4].split(",");
                boolean endGame = Boolean.parseBoolean(end[0]);
                if(endGame) {
                    openEnd();
                    boolean iWin = Boolean.parseBoolean(end[1]);
                    if(iWin) {
                        String ending = "Вы победили";
                        ec.setTextState(ending);
                    }
                    else {
                        String ending = "Вы проиграли";
                        ec.setTextState(ending);
                    }
                    closeStreams();
                }
                if(!Objects.equals(params[5], "null")) {
                    String[] cards_hand = params[5].split(",");
                    setHandFromData(cards_hand);
                }
                else {
                    hand = null;
                    gc.setCardsInHand(hand);
                }
                String textAction = params[6];
                if(Objects.equals(textAction, "null")) {
                    gc.setTextAction("");
                }
                else {
                    gc.setTextAction(textAction);
                }
                String turn = params[7];
                isTurn = Boolean.parseBoolean(turn);
                gc.setDisableActions(isTurn);
                break;
            }
//            case "permission": {
//                // permission#true/false
//                String bool = params[1];
//                isTurn = Boolean.getBoolean(bool);
//                gc.setDisableAction(isTurn);
//                break;
//            }
        }
    }

    private void setHandFromData(String[] data) {
        ArrayList<Card> cards = new ArrayList<>();
        for(String str: data) {
            cards.add(new Card(str));
        }
        hand = new Hand2(cards);
        gc.setCardsInHand(hand);
    }

    private void setTableFromData(String[] data) {
        for(int i = 0; i < data.length; i++) {
            data[i] = data[i].strip();
        }
        ArrayList<Card> cards = new ArrayList<>();
        for(String str: data) {
            cards.add(new Card(str));
        }
        table = cards;
        gc.setTable(table);
    }


    private void openWait(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("waiting.fxml"));
            Parent root = loader.load();

            WaitController wc = loader.getController();
            setWaitController(wc);

            Game2Application.stage.close();
            Game2Application.stage.setTitle("WAITING ROOM");
            Game2Application.stage.setScene(new Scene(root));
            Game2Application.stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
            System.err.println("error in loading wait");
        }
    }

    public void closeStreams() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openGame(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game2.fxml"));
            Parent root = loader.load();
            Game2Controller gc = loader.getController();
            setGameController(gc);

            Game2Application.stage.close();
            Game2Application.stage.setTitle("GAME");
            Game2Application.stage.setScene(new Scene(root));
            Game2Application.stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
            System.err.println("error in loading table");
        }
    }

    private void openEnd() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("end.fxml"));
            Parent root = loader.load();
            EndController ec = loader.getController();
            setEndController(ec);

            Game2Application.stage.close();
            Game2Application.stage.setTitle("ENDING");
            Game2Application.stage.setScene(new Scene(root));
            Game2Application.stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
            System.err.println("error in loading table");
        }
    }

    public void sendGameMove(String operation, Card card) {
        String data = operation;
        if(card != null) {
            data += "#" + card.toString();
        }
        sendData(data);
    }

    public void setGameController(Game2Controller tc){
        this.gc = tc;
    }

    public void setWaitController(WaitController wc){
        this.wc = wc;
    }

    public void setMenuController(MenuController mc) {
        this.mc = mc;
    }

    public void setEndController(EndController ec) {
        this.ec = ec;
    }
}
