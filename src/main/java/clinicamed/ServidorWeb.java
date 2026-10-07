package clinicamed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ServidorWeb {

    public static void main(String[] args) {
        SpringApplication.run(ServidorWeb.class, args);
    }

    @PostMapping("/login")
    public String processarLogin(@RequestBody Map<String, String> dados) {
        // Captura os dados vindos do formulário HTML
        String identificador = dados.get("usuario");
        String senha = dados.get("senha");

        // Como o método da classe Auth exige um perfil, vamos tentar autenticar
        // primeiro como PACIENTE. Se falhar, tenta como FUNCIONARIO e depois MEDICO.
        String[] perfis = {"PACIENTE", "FUNCIONARIO", "MEDICO"};

        for (String perfil : perfis) {
            try {

                Sessao sessao = Auth.autenticar(perfil, identificador, senha);

                // Se retornou uma sessão válida, o login funcionou!
                if (sessao != null) {
                    return "SUCCESS";
                }
            } catch (SQLException e) {
                System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            }
        }

        // Se passou por todos os perfis e não achou, o login falhou
        return "ERROR";
    }
}
