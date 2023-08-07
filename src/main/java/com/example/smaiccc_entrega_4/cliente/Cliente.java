package com.example.smaiccc_entrega_4.cliente;

import com.example.smaiccc_entrega_4.exceptions.ExcecaoErroComunicacaoServidor;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoObjetoNuloCliente;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoRespostaNulaServidor;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {
    public String serverHostName = "";
    private Socket clienteSocket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    BufferedReader stdIn = null;
    int numPorta;


    public Cliente(int numPorta, String serverHostName) {
        this.numPorta = numPorta;
        this.serverHostName = serverHostName;
    }

    public Cliente(int numPorta) {
        this.numPorta = numPorta;
        this.serverHostName = serverHostName;
    }

    public Cliente() {

    }

    public void iniciarCliente() throws ExcecaoObjetoNuloCliente {
        if(this.serverHostName == null){
            throw new ExcecaoObjetoNuloCliente();
        }
        criarSocket();
        if(clienteSocket != null) {
            inicializarEntradaSaida();
        }
    }

    public JSONObject enviarDadosServidor(JSONObject json) throws ExcecaoObjetoNuloCliente, ExcecaoRespostaNulaServidor, ExcecaoErroComunicacaoServidor{
        String s = "";
        if (json == (null) || in == (null)) {
            throw new ExcecaoObjetoNuloCliente();
        }
        try {
            out.println(json);
            s = in.readLine();

            if(s == null){
                throw new ExcecaoRespostaNulaServidor();
            } else {
                json = new JSONObject(s);
            }
        }
        catch (IOException e) {
            throw new ExcecaoErroComunicacaoServidor(e.getMessage());
        } catch (JSONException e) {
            System.out.println("RECEBIDO: " + s);
            throw new ExcecaoErroComunicacaoServidor("json inválido");
        } catch (RuntimeException e) {
            throw new ExcecaoErroComunicacaoServidor(e.getMessage());
        }
        return new JSONObject(s);
    }

    private void criarSocket(){
        try {
            clienteSocket = new Socket(serverHostName, numPorta);

        } catch (UnknownHostException e) {
            System.err.println("Host desconhecido: " + serverHostName);
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Erro ao criar socket!");
        }
    }

    private void inicializarEntradaSaida() throws ExcecaoObjetoNuloCliente {
        try {
            out = new PrintWriter(clienteSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    clienteSocket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Não foi possível obter objetos de entrada e saída"
                    + "para conectar-se ao host: " + serverHostName);
            System.exit(1);
        }
    }

    public void encerrarSocket() throws ExcecaoObjetoNuloCliente {
        try {
            if(out == null || in == null || clienteSocket == null){
              throw new ExcecaoObjetoNuloCliente();
            }
            out.close();
            in.close();
            clienteSocket.close();
        } catch (IOException e) {
            System.err.println("Erro ao encerrar os objetos de conexão de entrada e saída");
        }
    }
}