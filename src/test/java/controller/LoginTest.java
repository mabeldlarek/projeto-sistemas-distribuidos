package controller;

import com.example.smaiccc_entrega_4.Login;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {

    @Test
    void realizarLoginValido(){
        String dados = "{\"operacao\": 2,\"senha\":"+ "\"345345\""+ ",\"email\":"+ "\"maria@teste0.com\"" + "}";
        JSONObject jsonLogin = new JSONObject(dados);
        Login login = new Login(jsonLogin);
        assertEquals("OK", new JSONObject(login.validarLogin()).getString("status"));
    }
}
