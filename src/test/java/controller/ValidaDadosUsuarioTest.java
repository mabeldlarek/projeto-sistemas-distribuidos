package controller;

import com.example.smaiccc_entrega_4.validacao.ValidaDadosUsuario;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidaDadosUsuarioTest {

    // valores vazios
    @Test
    void cadastrarNomeVazio(){
        String dados = "{\"nome\":\""+ "" + "\",\"senha\":\""+ "123teste" +"\",\"email\":\""+ "teste@teste" +"\",\"operacao\":" + 1 + "}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosUsuario validacao = new ValidaDadosUsuario(jsonCadastro);
        assertEquals(false, validacao.validarNome());
        assertEquals("Nome inválido", validacao.getErro());
    }

    @Test
    void cadastrarEmailVazio(){
        String dados = "{\"nome\":\""+ "Maria" + "\",\"senha\":\""+ "123teste" +"\",\"email\":\""+ "" +"\",\"operacao\":" + 1 + "}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosUsuario validacao = new ValidaDadosUsuario(jsonCadastro);
        assertEquals(false, validacao.validarEmail());
        assertEquals("E-mail inválido", validacao.getErro());
    }

    @Test
    void cadastrarSenhaVazia(){
        String dados = "{\"nome\":\""+ "Maria" + "\",\"senha\":\""+ "" +"\",\"email\":\""+ "teste@teste" +"\",\"operacao\":" + 1 + "}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosUsuario validacao = new ValidaDadosUsuario(jsonCadastro);
        assertEquals(false, validacao.validarSenha());
        assertEquals("Senha inválida", validacao.getErro());
    }
    //---------------------------------------------------------------------------------//
    // valores menores
    @Test
    void cadastrarNomeMenorQueTresCaracteres(){
        String dados = "{\"nome\":\""+ "te" + "\",\"senha\":\""+ "123teste" +"\",\"email\":\""+ "teste@teste" +"\",\"operacao\":" + 1 + "}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosUsuario validacao = new ValidaDadosUsuario(jsonCadastro);
        assertEquals(false, validacao.validarNome());
        assertEquals("Nome inválido", validacao.getErro());
    }

    @Test
    void cadastrarEmailMenorQueTresCaracteresAntes(){
        String dados = "{\"nome\":\""+ "te" + "\",\"senha\":\""+ "123teste" +"\",\"email\":\""+ "te@teste" +"\",\"operacao\":" + 1 + "}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosUsuario validacao = new ValidaDadosUsuario(jsonCadastro);
        assertEquals(false, validacao.validarEmail());
        assertEquals("E-mail inválido", validacao.getErro());
    }

    @Test
    void cadastrarEmailMenorQueTresCaracteresDepois(){
        String dados = "{\"nome\":\""+ "Maria" + "\",\"senha\":\""+ "123teste" +"\",\"email\":\""+ "teste@te" +"\",\"operacao\":" + 1 + "}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosUsuario validacao = new ValidaDadosUsuario(jsonCadastro);
        assertEquals(false, validacao.validarEmail());
        assertEquals("E-mail inválido", validacao.getErro());
    }

    @Test
    void cadastrarSenhaMenorQueCincoCaracteres(){
        String dados = "{\"nome\":\""+ "te" + "\",\"senha\":\""+ "1234" +"\",\"email\":\""+ "teste@teste" +"\",\"operacao\":" + 1 + "}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosUsuario validacao = new ValidaDadosUsuario(jsonCadastro);
        assertEquals(false, validacao.validarSenha());
        assertEquals("Senha inválida", validacao.getErro());
    }
    //---------------------------------------------------------------------------------//
    // valores maiores
    @Test
    void cadastrarNomeMaiorQue50Caracteres(){
        String dados = "{\"nome\":\""+ "teste de validacao nome com mais de cinquenta caracteres" + "\",\"senha\":\""+ "123teste" +"\",\"email\":\""+ "teste@teste" +"\",\"operacao\":" + 1 + "}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosUsuario validacao = new ValidaDadosUsuario(jsonCadastro);
        assertEquals(false, validacao.validarNome());
        assertEquals("Nome inválido", validacao.getErro());
    }

    @Test
    void cadastrarEmailMaiorQue50CaracteresAntes(){
        String dados = "{\"nome\":\""+ "Maria" + "\",\"senha\":\""+ "123teste" +"\",\"email\":\""+ "testeDeValidacaoEmailComMaisDeCinquentaCaracteresAn@teste@teste" +"\",\"operacao\":" + 1 + "}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosUsuario validacao = new ValidaDadosUsuario(jsonCadastro);
        assertEquals(false, validacao.validarEmail());
        assertEquals("E-mail inválido", validacao.getErro());
    }

    @Test
    void cadastrarEmailMaiorQue10CaracteresDepois(){
        String dados = "{\"nome\":\""+ "Maria" + "\",\"senha\":\""+ "123teste" +"\",\"email\":\""+ "teste@teste123456" +"\",\"operacao\":" + 1 + "}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosUsuario validacao = new ValidaDadosUsuario(jsonCadastro);
        assertEquals(false, validacao.validarEmail());
        assertEquals("E-mail inválido", validacao.getErro());
    }

    @Test
    void cadastrarSenhaMaiorQue10Caracteres(){
        String dados = "{\"nome\":\""+ "Maria" + "\",\"senha\":\""+ "123456teste" +"\",\"email\":\""+ "teste@teste" +"\",\"operacao\":" + 1 + "}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosUsuario validacao = new ValidaDadosUsuario(jsonCadastro);
        assertEquals(false, validacao.validarSenha());
        assertEquals("Senha inválida", validacao.getErro());
    }

    @Test
    void cadastrarSenhaCaracteresInvalidos(){
        String dados = "{\"nome\":\""+ "Maria" + "\",\"senha\":\""+ "!._tst´" +"\",\"email\":\""+ "teste@teste" +"\",\"operacao\":" + 1 + "}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosUsuario validacao = new ValidaDadosUsuario(jsonCadastro);
        assertEquals(false, validacao.validarSenha());
        assertEquals("Senha inválida", validacao.getErro());
    }

    @Test
    void cadastrarEmailExistente(){
        String dados = "{\"nome\":\""+ "Maria" + "\",\"senha\":\""+ "123123" +"\",\"email\":\""+ "maria@teste0.com" +"\",\"operacao\":" + 1 + "}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosUsuario validacao = new ValidaDadosUsuario(jsonCadastro);
        assertEquals(1, validacao.verificarEmailExistente());
        assertEquals("E-mail já existe", validacao.getErro());
    }

    @Test
    void validaDadosLoginUsuario(){
        String dados = "{\"nome\":\""+ "Maria" + "\",\"senha\":\""+ "" +"\",\"email\":\""+ "maria@teste0.com" +"\",\"operacao\":" + 2 + "}";
        JSONObject jsonCadastro = new JSONObject(dados);
        ValidaDadosUsuario validacao = new ValidaDadosUsuario(jsonCadastro);
        assertEquals(false, validacao.validaDadosLoginUsuario());
        assertEquals("Senha inválida", validacao.getErro());
    }
}
