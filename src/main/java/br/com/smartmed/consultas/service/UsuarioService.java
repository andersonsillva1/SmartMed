package br.com.smartmed.consultas.service;

import br.com.smartmed.consultas.exception.ConstraintException;
import br.com.smartmed.consultas.exception.ObjectNotFoundException;
import br.com.smartmed.consultas.model.UsuarioModel;
import br.com.smartmed.consultas.repository.UsuarioRepository;
import br.com.smartmed.consultas.rest.dto.UsuarioCadastroRequestDTO;
import br.com.smartmed.consultas.rest.dto.UsuarioLoginRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioModel cadastrar(UsuarioCadastroRequestDTO dados){
        if (usuarioRepository.existsByEmail(dados.getEmail())){
            throw new ConstraintException("Já existe um usuário com o e-mail " + dados.getEmail());
        }
        var novoUsuario = new UsuarioModel();
        novoUsuario.setNome(dados.getNome());
        novoUsuario.setEmail(dados.getEmail());
        novoUsuario.setPerfil(dados.getPerfil());

        String senhaCriptografada = passwordEncoder.encode(dados.getSenha());
        novoUsuario.setSenha(senhaCriptografada);

        return usuarioRepository.save(novoUsuario);
    }

    // novo metodo para login
    @Transactional(readOnly = true)
    public UsuarioModel login(UsuarioLoginRequestDTO dados) {
        // Regra: "Buscar usuário pelo e-mail informado."
        var usuario = usuarioRepository.findByEmail(dados.getEmail())
                .orElseThrow(() -> new ObjectNotFoundException("Usuário ou senha inválidos."));

        // Regra: "Comparar senha enviada com a senha criptografada no banco usando matches()"
        // O primeiro argumento é a senha pura (que veio na requisição)
        // O segundo é a senha criptografada (que está no banco)
        if (passwordEncoder.matches(dados.getSenha(), usuario.getSenha())) {
            // Regra: "Se válido, retornar informações do usuário"
            return usuario;
        } else {
            // Regra: "Caso falhe, retornar erro de autenticação."
            throw new ObjectNotFoundException("Usuário ou senha inválidos.");
        }
    }
}
