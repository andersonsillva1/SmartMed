package br.com.smartmed.consultas.rest.controller;

import br.com.smartmed.consultas.rest.dto.*;
import br.com.smartmed.consultas.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioCadastroResponseDTO> cadastrar(@RequestBody @Valid UsuarioCadastroRequestDTO dados) {
        // @Valid -> Seu ControllerAdvice já trata os erros de validação (senha<8, email inválido...)
        var usuarioSalvo = usuarioService.cadastrar(dados);

        // Se o email já existir, o service vai lançar ConstraintException e seu
        // ControllerAdvice vai retornar o erro 400 Bad Request automaticamente.
        // O código abaixo só executa se tudo der certo.

        var response = new UsuarioCadastroResponseDTO(
                "Usuário cadastrado com sucesso",
                usuarioSalvo.getId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // NOVO ENDPOINT PARA O LOGIN
    @PostMapping("/login")
    public ResponseEntity<UsuarioLoginResponseDTO> login(@RequestBody @Valid UsuarioLoginRequestDTO dados) {
        var usuarioAutenticado = usuarioService.login(dados);
        // Se o login falhar, o service lança a exceção e seu @ControllerAdvice
        // já vai retornar o erro 404 Not Found (que está mapeado para ObjectNotFoundException).

        // Se o código chegou até aqui, o login foi um sucesso.

        // Regra: "retornar um token fictício"
        String tokenFicticio = "eyJh..." + UUID.randomUUID().toString(); // Gerando um token falso

        // Montando a resposta esperada
        var usuarioInfo = new UsuarioInfoDTO(
                usuarioAutenticado.getId(),
                usuarioAutenticado.getNome(),
                usuarioAutenticado.getPerfil()
        );

        var response = new UsuarioLoginResponseDTO(
                "Login realizado com sucesso",
                usuarioInfo,
                tokenFicticio
        );

        return ResponseEntity.ok(response);
    }
}