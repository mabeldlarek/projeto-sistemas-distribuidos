package com.example.smaiccc_entrega_4;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class ServerManager {
    private static final Map<Integer, ServerSocket> serverSockets = new HashMap<>();
    private static boolean isAtivo = false;
    private static boolean isEncerrado = false;

    public ServerManager() {

    }

    public void addServer(ServerSocket serverSocket, int port) {
        serverSockets.put(port, serverSocket);
    }

    public void stopServer(int port) {
        ServerSocket serverSocket = serverSockets.get(port);
        if (serverSocket != null) {
            try {
                serverSocket.close();
                if (serverSocket.isClosed()) {
                    setAtivo(true);
                    setEncerrado(true);
                }
                serverSockets.remove(port);
                setAtivo(false);
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    public void setAtivo(boolean ativo) {
        isAtivo = ativo;
    }

    public static boolean isIsAtivo() {
        return isAtivo;
    }

    public boolean isEncerrado() {
        return isEncerrado;
    }

    public void setEncerrado(boolean encerrado) {
        isEncerrado = encerrado;
    }
}
