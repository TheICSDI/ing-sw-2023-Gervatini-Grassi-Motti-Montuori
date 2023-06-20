package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.client.Client;

import java.io.IOException;

public class Launcher{
    public static void main(final String[] args) {
        try {
            Client.main(args);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}