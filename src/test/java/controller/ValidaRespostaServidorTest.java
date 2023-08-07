package controller;

import com.example.smaiccc_entrega_4.validacao.ValidaRespostaServidor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidaRespostaServidorTest {

    @Test
    void respostaOperacaoNula(){
        String dados = "{\"operacao\": null, \"status\": \"OK\"}";
        ValidaRespostaServidor validaResposta = new ValidaRespostaServidor(dados);
        assertEquals("Resposta do servidor é inválida. Erro em: status/operação", validaResposta.getErro());
    }

    @Test
    void respostaStatusNulo(){
        String dados = "{\"operacao\": 1, \"status\": null}";
        ValidaRespostaServidor validaResposta = new ValidaRespostaServidor(dados);
        assertEquals("Resposta do servidor é inválida. Erro em: status/operação", validaResposta.getErro());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 10, 11, 12})
    void respostaOperacaoInválida(int operacaoInvalida){
        String dados = "{\"operacao\":" + operacaoInvalida +", \"status\": \"OK\"}";
        ValidaRespostaServidor validaResposta = new ValidaRespostaServidor(dados);
        assertEquals("Operação inválida", validaResposta.getErro());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"operacao\": 4 , \"status\": \"OK\", \"incidentes\": [ { \"data\": \"2020-55-10\", \"hora\": \"00:00\", \"estado\": \"PR\", \"cidade\": \"PONTA GROSSA\", \"bairro\": \"TESTE\", \"rua\": \"RUA TESTE\", \"tipo_incidente\":1, \"id_incidente\": 1}]}",
            "{\"operacao\": 4 , \"status\": \"OK\", \"incidentes\": [{\"data\": \"2023-01-10\", \"hora\": \"25:00\", \"estado\": \"PR\", \"cidade\": \"PONTA GROSSA\", \"bairro\": \"TESTE\", \"rua\": \"RUA TESTE\", \"tipo_incidente\":1, \"id_incidente\": 1}] }",
            "{\"operacao\": 4 , \"status\": \"OK\", \"incidentes\":[{\"data\": \"2023-01-10\", \"hora\": \"15:00\", \"estado\": \"\", \"cidade\": \"PONTA GROSSA\", \"bairro\": \"TESTE\", \"rua\": \"RUA TESTE\", \"tipo_incidente\":1, \"id_incidente\": 1}]}",
            "{\"operacao\": 4 , \"status\": \"OK\", \"incidentes\":[{\"data\": \"2023-01-10\", \"hora\": \"15:00\", \"estado\": \"PR\", \"cidade\": \"\", \"bairro\": \"TESTE\", \"rua\": \"RUA TESTE\", \"tipo_incidente\":1, \"id_incidente\": 1}]}",
            "{\"operacao\": 4 , \"status\": \"OK\", \"incidentes\":[{\"data\": \"2023-01-10\", \"hora\": \"15:00\", \"estado\": \"PR\", \"cidade\": \"PONTA GROSSA\", \"bairro\": \"TESTE\", \"rua\": \"RUA TESTE\", \"tipo_incidente\":null, \"id_incidente\": 1}]}",
            "{\"operacao\": 4 , \"status\": \"OK\", \"incidentes\":[{\"data\": \"2023-01-10\", \"hora\": \"15:00\", \"estado\": \"PR\", \"cidade\": \"PONTA GROSSA\", \"bairro\": \"TESTE\", \"rua\": \"RUA TESTE\", \"tipo_incidente\":1, \"id_incidente\": null}]}"
    })
    void respostaListagemInválida(String dadosTeste){
        String dados = dadosTeste;
        ValidaRespostaServidor validaResposta = new ValidaRespostaServidor(dados);
        assertEquals("Resposta do servidor é inválida. Erro em: lista de incidentes", validaResposta.getErro());
    }
}
