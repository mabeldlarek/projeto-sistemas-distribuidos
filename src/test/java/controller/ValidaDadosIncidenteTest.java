package controller;

import com.example.smaiccc_entrega_4.Login;
import com.example.smaiccc_entrega_4.validacao.ValidaDadosIncidente;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ValidaDadosIncidenteTest {
    private Login login;
    private JSONObject autenticacao;

    @BeforeAll
    void iniciarComponentes(){
        String dados = "{\"operacao\": 2,\"senha\":"+ "\"345345\""+ ",\"email\":"+ "\"maria@teste0.com\"" + "}";
        JSONObject jsonLogin = new JSONObject(dados);
        login = new Login(jsonLogin);
        autenticacao = new JSONObject(login.validarLogin());
    }

    @Test
    void cadastrarDataVazia(){
        String dados ="{\"data\":"+ "\"\"" + ",\"hora\":" + "\""+ "10:45" + "\",\"estado\":" +"\"" + "pr" + "\",\"cidade\":" +"\"" + "ponta grossa" + "\",\"bairro\":" +"\"" + "uvaranas" +  "\",\"rua\":" +"\"" +
                "rua do bem" + "\",\"tipo_incidente\":" + "1" + ",\"token\":" +"\"" + autenticacao.getString("token") + "\",\"id\":" + autenticacao.getInt("id") + ",\"operacao\": 7}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosIncidente validacao = new ValidaDadosIncidente(jsonCadastro);
        assertEquals(false, validacao.validarData());
        assertEquals("Data inválida", validacao.getErro());
    }

    @Test
    void cadastrarDataNula(){
        String dados ="{\"data\":"+ "null" + ",\"hora\":" + "\""+ "10:45" + "\",\"estado\":" +"\"" + "pr" + "\",\"cidade\":" +"\"" + "ponta grossa" + "\",\"bairro\":" +"\"" + "uvaranas" +  "\",\"rua\":" +"\"" +
                "rua do bem" + "\",\"tipo_incidente\":" + "1" + ",\"token\":" +"\"" + autenticacao.getString("token") + "\",\"id\":" + autenticacao.getInt("id") + ",\"operacao\": 7}";        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosIncidente validacao = new ValidaDadosIncidente(jsonCadastro);
        assertEquals(false, validacao.validarData());
        assertEquals("Data inválida", validacao.getErro());
    }

    @Test
    void cadastrarDataFormatoInvalido(){
        String dados ="{\"data\":"+ "\"24/06/2022\"" + ",\"hora\":" + "\""+ "10:45" + "\",\"estado\":" +"\"" + "pr" + "\",\"cidade\":" +"\"" + "ponta grossa" + "\",\"bairro\":" +"\"" + "uvaranas" +  "\",\"rua\":" +"\"" +
                "rua do bem" + "\",\"tipo_incidente\":" + "1" + ",\"token\":" +"\"" + autenticacao.getString("token") + "\",\"id\":" + autenticacao.getInt("id") + ",\"operacao\": 7}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosIncidente validacao = new ValidaDadosIncidente(jsonCadastro);
        assertEquals(false, validacao.validarData());
        assertEquals("Data inválida", validacao.getErro());
    }

    @ParameterizedTest
    @ValueSource(strings = {"24/06/2002", "2009-1-24", "2023-20-01"})
    void cadastrarDataInválida(String dataInvalida){
        String dados ="{\"data\":"+ "\""+ dataInvalida +"\"" + ",\"hora\":" + "\""+ "10:45" + "\",\"estado\":" +"\"" + "PR" + "\",\"cidade\":" +"\"" + "ponta grossa" + "\",\"bairro\":" +"\"" + "uvaranas" +  "\",\"rua\":" +"\"" +
                "rua do bem" + "\",\"tipo_incidente\":" + 11 + ",\"token\":" +"\"" + autenticacao.getString("token") + "\",\"id\":" + autenticacao.getInt("id") + ",\"operacao\": 7}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosIncidente validacao = new ValidaDadosIncidente(jsonCadastro);
        assertEquals(false, validacao.validarData());
        assertEquals("Data inválida", validacao.getErro());
    }

    @Test
    void cadastrarHoraVazia(){
        String dados ="{\"data\":"+ "\"\"" + ",\"hora\":" + "\""+ "" + "\",\"estado\":" +"\"" + "pr" + "\",\"cidade\":" +"\"" + "ponta grossa" + "\",\"bairro\":" +"\"" + "uvaranas" +  "\",\"rua\":" +"\"" +
                "rua do bem" + "\",\"tipo_incidente\":" + "1" + ",\"token\":" +"\"" + autenticacao.getString("token") + "\",\"id\":" + autenticacao.getInt("id") + ",\"operacao\": 7}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosIncidente validacao = new ValidaDadosIncidente(jsonCadastro);
        assertEquals(false, validacao.validarHora());
        assertEquals("Hora inválida", validacao.getErro());
    }

    @Test
    void cadastrarHoraFormatoInválido(){
        String dados ="{\"data\":"+ "\"\"" + ",\"hora\":" + "\""+ "10:70" + "\",\"estado\":" +"\"" + "pr" + "\",\"cidade\":" +"\"" + "ponta grossa" + "\",\"bairro\":" +"\"" + "uvaranas" +  "\",\"rua\":" +"\"" +
                "rua do bem" + "\",\"tipo_incidente\":" + "1" + ",\"token\":" +"\"" + autenticacao.getString("token") + "\",\"id\":" + autenticacao.getInt("id") + ",\"operacao\": 7}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosIncidente validacao = new ValidaDadosIncidente(jsonCadastro);
        assertEquals(false, validacao.validarHora());
        assertEquals("Hora inválida", validacao.getErro());
    }

    @ParameterizedTest
    @ValueSource(strings = {"24:00", "23:80"})
    void cadastrarHoraFormatoInválido(String horaInvalida){
        String dados ="{\"data\":"+ "\"\"" + ",\"hora\":" + "\""+ horaInvalida + "\",\"estado\":" +"\"" + "PR" + "\",\"cidade\":" +"\"" + "ponta grossa" + "\",\"bairro\":" +"\"" + "uvaranas" +  "\",\"rua\":" +"\"" +
                "rua do bem" + "\",\"tipo_incidente\":" + 1 + ",\"token\":" +"\"" + autenticacao.getString("token") + "\",\"id\":" + autenticacao.getInt("id") + ",\"operacao\": 7}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosIncidente validacao = new ValidaDadosIncidente(jsonCadastro);
        assertEquals(false, validacao.validarHora());
        assertEquals("Hora inválida", validacao.getErro());
    }

    @Test
    void cadastrarHoraNula(){
        String dados ="{\"data\":"+ "null" + ",\"hora\":" + "\""+ "null" + "\",\"estado\":" +"\"" + "pr" + "\",\"cidade\":" +"\"" + "ponta grossa" + "\",\"bairro\":" +"\"" + "uvaranas" +  "\",\"rua\":" +"\"" +
                "rua do bem" + "\",\"tipo_incidente\":" + "1" + ",\"token\":" +"\"" + autenticacao.getString("token") + "\",\"id\":" + autenticacao.getInt("id") + ",\"operacao\": 7}";        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosIncidente validacao = new ValidaDadosIncidente(jsonCadastro);
        assertEquals(false, validacao.validarHora());
        assertEquals("Hora inválida", validacao.getErro());
    }

    @ParameterizedTest
    @ValueSource(ints = {0,11,12,13,14,15})
    void cadastrarTipoIncidenteNumeroInválido(int incidenteInvalido){
        String dados ="{\"data\":"+ "\"\"" + ",\"hora\":" + "\""+ "10:45" + "\",\"estado\":" +"\"" + "PR" + "\",\"cidade\":" +"\"" + "ponta grossa" + "\",\"bairro\":" +"\"" + "uvaranas" +  "\",\"rua\":" +"\"" +
                "rua do bem" + "\",\"tipo_incidente\":" + incidenteInvalido + ",\"token\":" +"\"" + autenticacao.getString("token") + "\",\"id\":" + autenticacao.getInt("id") + ",\"operacao\": 7}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosIncidente validacao = new ValidaDadosIncidente(jsonCadastro);
        assertEquals(false, validacao.validarRua());
        assertEquals("Rua inválida", validacao.getErro());
    }

    @Test
    void cadastrarTipoIncidenteNulo(){
        String dados ="{\"data\":"+ "null" + ",\"hora\":" + "\""+ "10:45" + "\",\"estado\":" +"\"" + "pr" + "\",\"cidade\":" +"\"" + "ponta grossa" + "\",\"bairro\":" +"\"" + "uvaranas" +  "\",\"rua\":" +"\"" +
                "rua do bem" + "\",\"tipo_incidente\":" + "null" + ",\"token\":" +"\"" + autenticacao.getString("token") + "\",\"id\":" + autenticacao.getInt("id") + ",\"operacao\": 7}";        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosIncidente validacao = new ValidaDadosIncidente(jsonCadastro);
        assertEquals(false, validacao.validarIncidente());
        assertEquals("Tipo de Incidente inválido", validacao.getErro());
    }

    @Test
    void cadastrarEstadoNulo(){
        String dados ="{\"data\":"+ "null" + ",\"hora\":" + "\""+ "null" + "\",\"estado\":" +"\"" + "null" + "\",\"cidade\":" +"\"" + "ponta grossa" + "\",\"bairro\":" +"\"" + "uvaranas" +  "\",\"rua\":" +"\"" +
                "rua do bem" + "\",\"tipo_incidente\":" + "1" + ",\"token\":" +"\"" + autenticacao.getString("token") + "\",\"id\":" + autenticacao.getInt("id") + ",\"operacao\": 7}";        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosIncidente validacao = new ValidaDadosIncidente(jsonCadastro);
        assertEquals(false, validacao.validarEstado());
        assertEquals("Estado inválido", validacao.getErro());
    }

    @Test
    void cadastrarEstadoInválido(){
        String dados ="{\"data\":"+ "\"\"" + ",\"hora\":" + "\""+ "10:45" + "\",\"estado\":" +"\"" + "par" + "\",\"cidade\":" +"\"" + "ponta grossa" + "\",\"bairro\":" +"\"" + "uvaranas" +  "\",\"rua\":" +"\"" +
                "rua do bem" + "\",\"tipo_incidente\":" + 11 + ",\"token\":" +"\"" + autenticacao.getString("token") + "\",\"id\":" + autenticacao.getInt("id") + ",\"operacao\": 7}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosIncidente validacao = new ValidaDadosIncidente(jsonCadastro);
        assertEquals(false, validacao.validarEstado());
        assertEquals("Estado inválido", validacao.getErro());
    }

    @Test
    void cadastrarEstadoVazio(){
        String dados ="{\"data\":"+ "null" + ",\"hora\":" + "\""+ "10:45" + "\",\"estado\":" +"\"" + "" + "\",\"cidade\":" +"\"" + "ponta grossa" + "\",\"bairro\":" +"\"" + "uvaranas" +  "\",\"rua\":" +"\"" +
                "rua do bem" + "\",\"tipo_incidente\":" + 1 + ",\"token\":" +"\"" + autenticacao.getString("token") + "\",\"id\":" + autenticacao.getInt("id") + ",\"operacao\": 7}";        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosIncidente validacao = new ValidaDadosIncidente(jsonCadastro);
        assertEquals(false, validacao.validarEstado());
        assertEquals("Estado inválido", validacao.getErro());
    }

    @Test
    void cadastrarCidadeNula(){
        String dados ="{\"data\":"+ "null" + ",\"hora\":" + "\""+ "null" + "\",\"estado\":" +"\"" + "PR" + "\",\"cidade\":" +"\"" + "null" + "\",\"bairro\":" +"\"" + "uvaranas" +  "\",\"rua\":" +"\"" +
                "rua do bem" + "\",\"tipo_incidente\":" + "1" + ",\"token\":" +"\"" + autenticacao.getString("token") + "\",\"id\":" + autenticacao.getInt("id") + ",\"operacao\": 7}";        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosIncidente validacao = new ValidaDadosIncidente(jsonCadastro);
        assertEquals(false, validacao.validarCidade());
        assertEquals("Cidade inválida", validacao.getErro());
    }


    @ParameterizedTest
    @ValueSource(strings = {"Téste", "Caças Novas"})
    void cadastrarCidadeInválida(String cidadeInvalida){
        String dados ="{\"data\":"+ "\"\"" + ",\"hora\":" + "\""+ "10:45" + "\",\"estado\":" +"\"" + "PR" + "\",\"cidade\":" +"\"" + cidadeInvalida + "\",\"bairro\":" +"\"" + "uvaranas" +  "\",\"rua\":" +"\"" +
                "rua do bem" + "\",\"tipo_incidente\":" + 11 + ",\"token\":" +"\"" + autenticacao.getString("token") + "\",\"id\":" + autenticacao.getInt("id") + ",\"operacao\": 7}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosIncidente validacao = new ValidaDadosIncidente(jsonCadastro);
        assertEquals(false, validacao.validarCidade());
        assertEquals("Cidade inválida", validacao.getErro());
    }

    @Test
    void cadastrarCidadeVazio(){
        String dados ="{\"data\":"+ "null" + ",\"hora\":" + "\""+ "10:45" + "\",\"estado\":" +"\"" + "PR" + "\",\"cidade\":" +"\"" + "" + "\",\"bairro\":" +"\"" + "uvaranas" +  "\",\"rua\":" +"\"" +
                "rua do bem" + "\",\"tipo_incidente\":" + 1 + ",\"token\":" +"\"" + autenticacao.getString("token") + "\",\"id\":" + autenticacao.getInt("id") + ",\"operacao\": 7}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosIncidente validacao = new ValidaDadosIncidente(jsonCadastro);
        assertEquals(false, validacao.validarCidade());
        assertEquals("Cidade inválida", validacao.getErro());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Téste", "Caças", "", "teste de validacao bairro com mais de cinquenta caracteres"})
    void cadastrarBairroInválido(String bairroInvalido){
        String dados ="{\"data\":"+ "\"\"" + ",\"hora\":" + "\""+ "10:45" + "\",\"estado\":" +"\"" + "PR" + "\",\"cidade\":" +"\"" + "ponta grossa" + "\",\"bairro\":" +"\"" + "uvaranas" +  "\",\"rua\":" +"\"" +
                bairroInvalido + "\",\"tipo_incidente\":" + 11 + ",\"token\":" +"\"" + autenticacao.getString("token") + "\",\"id\":" + autenticacao.getInt("id") + ",\"operacao\": 7}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosIncidente validacao = new ValidaDadosIncidente(jsonCadastro);
        assertEquals(false, validacao.validarBairro());
        assertEquals("Bairro inválido", validacao.getErro());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Téste", "Caças", "", "teste de validacao rua com mais de cinquenta caracteres"})
    void cadastrarRuaInválida(String ruaInvalida){
        String dados ="{\"data\":"+ "\"\"" + ",\"hora\":" + "\""+ "10:45" + "\",\"estado\":" +"\"" + "PR" + "\",\"cidade\":" +"\"" + "ponta grossa" + "\",\"bairro\":" +"\"" + "uvaranas" +  "\",\"rua\":" +"\"" +
                ruaInvalida + "\",\"tipo_incidente\":" + 11 + ",\"token\":" +"\"" + autenticacao.getString("token") + "\",\"id\":" + autenticacao.getInt("id") + ",\"operacao\": 7}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosIncidente validacao = new ValidaDadosIncidente(jsonCadastro);
        assertEquals(false, validacao.validarRua());
        assertEquals("Rua inválida", validacao.getErro());
    }



}
