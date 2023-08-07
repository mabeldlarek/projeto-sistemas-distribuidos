package com.example.smaiccc_entrega_4.dao;

import com.example.smaiccc_entrega_4.banco.ConexaoBanco;
import com.example.smaiccc_entrega_4.model.Usuario;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    private ConexaoBanco conexaoBanco;
    private String sqlConsulta;
    private PreparedStatement ps;
    private String erroBanco = "";

    public UsuarioDAO() {
        this.conexaoBanco = new ConexaoBanco();
    }

    public void inserir(JSONObject objJson) {
        String sql = "INSERT INTO usuario(nome, senha, email) VALUES(?,?,?);";
        try {
            conexaoBanco.conectar();
            ps = conexaoBanco.getConexao().prepareStatement(sql);
            ps.setString(1, objJson.getString("nome"));
            ps.setString(2, objJson.getString("senha"));
            ps.setString(3, objJson.getString("email"));
            ps.execute();
            ps.close();
            conexaoBanco.fecharConexao();
        } catch (SQLException e) {
            setErroBanco("Erro ao inserir usuário");
        }
    }

    public void editar(JSONObject objJson) {
        String sql = "UPDATE usuario SET nome = ?, senha = ?, email = ? WHERE id = ?;";
        try {
            conexaoBanco.conectar();
            ps = conexaoBanco.getConexao().prepareStatement(sql);
            ps.setString(1, objJson.getString("nome"));
            ps.setString(2, objJson.getString("senha"));
            ps.setString(3, objJson.getString("email"));
            ps.setInt(4, objJson.getInt("id"));
            ps.execute();
            ps.close();
            conexaoBanco.fecharConexao();
        } catch (SQLException e) {
            setErroBanco("Erro ao editar usuário");
        }
    }

    public void remover(JSONObject objJson) {
        String sql = "DELETE FROM usuario WHERE id = ?;";
        try {
            conexaoBanco.conectar();
            ps = conexaoBanco.getConexao().prepareStatement(sql);
            ps.setInt(1, objJson.getInt("id"));
            ps.execute();
            ps.close();
            conexaoBanco.fecharConexao();
        } catch (SQLException e) {
            setErroBanco("Erro ao remover usuário" + e);
        }

    }

    public int getUsuarioPorEmail(String email){
        String sql = "SELECT * FROM usuario WHERE email LIKE BINARY ?;";
        ResultSet rs = null;
        int i = 0;
        try {
            conexaoBanco.conectar();
            ps = conexaoBanco.getConexao().prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            while(rs.next()){
                i++;
            }
        } catch (SQLException e) {
            setErroBanco("Erro ao obter usuário");
        }
        return i;
    }

    public Usuario getUsuarioPorId(String id) {
        String sql = "SELECT * FROM usuario WHERE id=?;";
        ResultSet rs = null;
        Usuario usuario = null;
        try {
            conexaoBanco.conectar();
            ps = conexaoBanco.getConexao().prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String senha = rs.getString("email");
                usuario = new Usuario(nome, email, senha, id);
            }
        } catch (SQLException e) {
            setErroBanco("Erro ao obter usuário");
        }
        return usuario;
    }
    public int getSenhaPorId(String senha, int id){
        String sql = "SELECT *FROM usuario WHERE senha LIKE BINARY ? AND id=?";
        ResultSet rs = null;
        int i = 0;
        try {
            conexaoBanco.conectar();
            ps = conexaoBanco.getConexao().prepareStatement(sql);
            ps.setString(1, senha);
            ps.setInt(2, id);
            rs = ps.executeQuery();
            while(rs.next()){
                i++;
            }
        } catch (SQLException e) {
            setErroBanco("Erro ao editar usuário.");
        }
        return i;
    }
    public int getUsuarioPorEmailEdicao(String email, int id){
        String sql = "SELECT *FROM usuario WHERE email=? AND id<>?";
        ResultSet rs = null;
        int i = 0;
        try {
            conexaoBanco.conectar();
            ps = conexaoBanco.getConexao().prepareStatement(sql);
            ps.setString(1, email);
            ps.setInt(2, id);
            rs = ps.executeQuery();
            while(rs.next()){
                i++;
            }
        } catch (SQLException e) {
            setErroBanco("Erro ao editar usuário.");
        }
        return i;
    }

    public Usuario getUsuarioPorEmailSenha(String email, String senha)  {
        String sql = "SELECT *FROM usuario WHERE email LIKE BINARY ? and senha LIKE BINARY ?;";
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            conexaoBanco.conectar();
            ps = conexaoBanco.getConexao().prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, senha);
            rs= ps.executeQuery();

            while(rs.next()){
                usuario = new Usuario(rs.getString("nome"), email, senha,
                        rs.getString("id"));
            }
            conexaoBanco.fecharConexao();
        } catch (SQLException e) {
            setErroBanco("Erro ao obter usuário");
        }

        return usuario;
    }

    public String getErroBanco() {
        return erroBanco;
    }

    private void setErroBanco(String erroBanco) {
        this.erroBanco = erroBanco;
    }
}
