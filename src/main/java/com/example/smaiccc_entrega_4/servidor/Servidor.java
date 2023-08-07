package com.example.smaiccc_entrega_4.servidor;

import com.example.smaiccc_entrega_4.ServerManager;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoConexaoClienteNula;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoIniciarServidor;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoJsonInvalido;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoObjetoNuloServidor;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Servidor extends Thread {
    protected Socket clientSocket;
    private int porta;
    private ServerSocket serverSocket = null;
    private boolean ativo = false;

    private PrintWriter out = null;
    private BufferedReader in = null;
    private JSONObject jsonObject = null;
    private ServerManager sm = new ServerManager();


    Servidor(Socket clientSoc) throws ExcecaoConexaoClienteNula {
        ativo = true;
        if(clientSoc != null) {
            clientSocket = clientSoc;
            start();
        } else {
            throw new ExcecaoConexaoClienteNula();
        }
    }

    public Servidor(int porta) {
        this.porta = porta;
    }

    public Servidor(ServerSocket ss) {
        this.serverSocket = ss;
    }

    private void iniciarSocket() throws ExcecaoIniciarServidor, IOException {
        serverSocket = new ServerSocket(porta);
        if(serverSocket != null) {
            sm.addServer(serverSocket, porta);
            this.ativo = true;
            sm.setAtivo(true);
        } else
            throw new ExcecaoIniciarServidor("Não foi possível iniciar o socket na porta: " + porta);
    }

    private void iniciarEsperaComunicacao() throws ExcecaoConexaoClienteNula, IOException {
        try {
            while (isAtivo())
            {
                System.out.println ("Aguardando conexao");
                new Servidor(serverSocket.accept());
            }
        }
        catch (IOException e)
        {
            ativo = false;
            sm.setAtivo(false);
            throw new IOException();
        }
    }

    private void iniciarEntradaSaidaDados() throws ExcecaoObjetoNuloServidor {
        try {
            if(clientSocket == null){
                throw  new ExcecaoObjetoNuloServidor();
            }
            out = new PrintWriter(clientSocket.getOutputStream(),
                        true);
            in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Erro ao inicializar entrada e saída de dados do cliente.");
        }
    }

    private void realizarLeituraSaidaDados() throws ExcecaoObjetoNuloServidor {
        String inputLine = null;
        while (true)
        {
            try {
                if(in!=null) {
                    if (!((inputLine = in.readLine()) != null)) break;
                    System.out.println("RECEBIDO: " + inputLine);
                    ServidorRequisicao sc = new ServidorRequisicao(inputLine);
                    JSONObject jsonObject = sc.getRespostaJSON();
                    System.out.println("ENVIADO: " + jsonObject.toString());
                    out.println(jsonObject);
                }
                else{
                    throw new ExcecaoObjetoNuloServidor();
                }
            } catch (SocketException e) {
                System.out.println("Erro de Socket durante a leitura");
                return;
            } catch (IOException e) {
                System.out.println("Erro de Execução durante a leitura");
                return;
            } catch (ExcecaoJsonInvalido e) {
                System.out.println("ENVIADO: \"\"");
                out.println("");
            }
        }
    }

    public void iniciarServidor() throws ExcecaoIniciarServidor {
        try {
            iniciarSocket();
            iniciarEsperaComunicacao();
        } catch (IOException e) {
            throw new ExcecaoIniciarServidor(sm.isEncerrado()? "Servidor Encerrado" : "Erro no servidor:" + e.getMessage());
        } catch (ExcecaoConexaoClienteNula e) {
            throw new ExcecaoIniciarServidor(e.getMessage());
        } catch (ExcecaoIniciarServidor e) {
            throw new ExcecaoIniciarServidor();
        }
    }

    public void run() {
        if(ativo == false) {
            try {
                iniciarServidor();
            } catch (ExcecaoIniciarServidor e) {
                System.out.println (e.getMessage());
            }
        } else if (ativo == true) {
            try {
                iniciarEntradaSaidaDados();
                realizarLeituraSaidaDados();
            } catch (ExcecaoObjetoNuloServidor e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean isAtivo() {
        return ativo;
    }

    private void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
