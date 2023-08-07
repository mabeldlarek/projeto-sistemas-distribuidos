package controller;

import com.example.smaiccc_entrega_4.cliente.Cliente;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoErroComunicacaoServidor;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoObjetoNuloCliente;
import com.example.smaiccc_entrega_4.exceptions.ExcecaoRespostaNulaServidor;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrocaDeMensagensTest {
    private Cliente cliente = new Cliente(20015, "localhost");

    private JSONObject iniciarComunicacao(String dadosDeTeste) {
        JSONObject resposta = new JSONObject();
        try{
            cliente.iniciarCliente();
            resposta = cliente.enviarDadosServidor(new JSONObject(dadosDeTeste));
        } catch (ExcecaoObjetoNuloCliente e) {
            resposta = null;
        } catch (ExcecaoRespostaNulaServidor e) {
            resposta = null;
        }
        catch (RuntimeException e) {
            resposta = null;
        } catch (ExcecaoErroComunicacaoServidor e) {
            resposta = null;
        }
        return resposta;
    }

    @Test
    void enviarJsonVazio(){
        assertEquals(null, iniciarComunicacao(null));
        iniciarComunicacao(null);
    }

    @Test
    void enviarJsonEstruturaErrada(){
        assertEquals(null, iniciarComunicacao("teste:erro"));
        iniciarComunicacao(null);
    }

}
