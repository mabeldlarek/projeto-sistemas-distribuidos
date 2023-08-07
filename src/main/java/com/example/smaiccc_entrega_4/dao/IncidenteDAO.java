package com.example.smaiccc_entrega_4.dao;

import com.example.smaiccc_entrega_4.banco.ConexaoBanco;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IncidenteDAO {
    private ConexaoBanco conexaoBanco;
    private String sqlConsulta;
    private PreparedStatement ps;
    private String erro;

    public IncidenteDAO() {
        this.conexaoBanco = new ConexaoBanco();
    }

    public void inserir(JSONObject objJson) {
        String sql = "INSERT INTO incidente(data, hora, estado, cidade, bairro, rua, tipo_incidente, id) VALUES(?,?,?,?,?,?,?,?);";
        try {
            conexaoBanco.conectar();
            ps = conexaoBanco.getConexao().prepareStatement(sql);
            ps.setString(1, objJson.getString("data"));
            ps.setString(2, objJson.getString("hora"));
            ps.setString(3, objJson.getString("estado"));
            ps.setString(4, objJson.getString("cidade"));
            ps.setString(5, objJson.getString("bairro"));
            ps.setString(6, objJson.getString("rua"));
            ps.setString(7, String.valueOf(objJson.getInt("tipo_incidente")));
            ps.setString(8, String.valueOf(objJson.getInt("id")));
            ps.execute();
            ps.close();
            conexaoBanco.fecharConexao();
        } catch (SQLException e) {
            setErro("Erro ao inserir incidente");
        }
    }
    public JSONObject consultarListaIncidentes(JSONObject objJson){
        int i = 0;
        JSONObject json = null;
        JSONArray incidentesArray = new JSONArray();
        String sql = "SELECT *FROM incidente WHERE data = ? AND cidade = ? AND estado = ?;";
        try {
            conexaoBanco.conectar();
            ps = conexaoBanco.getConexao().prepareStatement(sql);
            ps.setString(1, objJson.getString("data"));
            ps.setString(2, objJson.getString("cidade"));
            ps.setString(3, objJson.getString("estado"));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                JSONObject incidenteJson = new JSONObject();
                incidenteJson.put("data", rs.getString("data"));
                incidenteJson.put("hora", rs.getString("hora"));
                incidenteJson.put("estado", rs.getString("estado"));
                incidenteJson.put("cidade", rs.getString("cidade"));
                incidenteJson.put("bairro", rs.getString("bairro"));
                incidenteJson.put("rua", rs.getString("rua"));
                incidenteJson.put("tipo_incidente", rs.getInt("tipo_incidente"));
                incidenteJson.put("id_incidente", rs.getInt("id_incidente"));
                incidentesArray.put(incidenteJson);
                i++;
            }
                json = new JSONObject();
                json.put("incidentes", incidentesArray == null? "" : incidentesArray);
                json.put("operacao", 4);
                json.put("status", "OK");

            return json;
        } catch (SQLException e) {
            setErro("Erro ao consultar incidente");
            return json;
        }
    }

    public JSONObject consultarListaIncidentesUsuario(JSONObject objJson){
        int i = 0;
        JSONObject json = null;
        JSONArray incidentesArray = new JSONArray();
        String sql = "SELECT *FROM incidente WHERE id = ? ;";
        try {
            conexaoBanco.conectar();
            ps = conexaoBanco.getConexao().prepareStatement(sql);
            ps.setString(1, String.valueOf(objJson.getInt("id")));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                JSONObject incidenteJson = new JSONObject();
                incidenteJson.put("data", rs.getString("data"));
                incidenteJson.put("hora", rs.getString("hora"));
                incidenteJson.put("estado", rs.getString("estado"));
                incidenteJson.put("cidade", rs.getString("cidade"));
                incidenteJson.put("bairro", rs.getString("bairro"));
                incidenteJson.put("rua", rs.getString("rua"));
                incidenteJson.put("tipo_incidente", rs.getInt("tipo_incidente"));
                incidenteJson.put("id_incidente", rs.getInt("id_incidente"));
                incidentesArray.put(incidenteJson);
                i++;
            }
                json = new JSONObject();
                json.put("incidentes", incidentesArray == null? "" : incidentesArray);
                json.put("operacao", 5);
                json.put("status", "OK");

            return json;
        } catch (SQLException e) {
            setErro("Erro ao consultar incidente");
            return json;
        }
    }

    public void removerIncidente(JSONObject objJson) {
        String sql = "DELETE FROM incidente WHERE id_incidente = ?;";
        try {
            conexaoBanco.conectar();
            ps = conexaoBanco.getConexao().prepareStatement(sql);
            ps.setString(1, String.valueOf(objJson.getInt("id_incidente")));
            ps.execute();
            ps.close();
            conexaoBanco.fecharConexao();
        } catch (SQLException e) {
            setErro("Erro ao remover incidente");
        }
    }

    public String getErro() {
        return erro;
    }

    private void setErro(String erro) {
        this.erro = erro;
    }
}
