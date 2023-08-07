package com.example.smaiccc_entrega_4.banco;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexaoBanco {
    private String url;
    private String usuario = "root";
    private String senha = "12345678";
    private Connection conexao;

    public ConexaoBanco(){

    }
    public void conectar() throws SQLException {
        try {
            if (conexao == null) {
                url = "jdbc:mysql://localhost:3306/smaicc";
                conexao = DriverManager.getConnection(url, usuario, senha);
            } else if (conexao.isClosed()) {
                conexao = null;
                conectar();
                return;
            }
        } catch (SQLException e) {
            throw new SQLException();
           // System.out.println("Falha ao conectar-se com o banco " + e.getMessage());
        }
    }

    public void fecharConexao() {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
                System.out.println("Falha ao fechar conexão com o banco");
            }
        }
    }


    public Connection getConexao() throws SQLException {
        if(conexao == null) throw new SQLException();
        return conexao;
    }
}
