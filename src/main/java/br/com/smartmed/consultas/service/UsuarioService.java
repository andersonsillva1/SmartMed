package br.com.smartmed.consultas.service;

import br.com.smartmed.consultas.exception.ConstraintException;
import br.com.smartmed.consultas.model.UsuarioModel;
import br.com.smartmed.consultas.repository.UsuarioRepository;
import br.com.smartmed.consultas.rest.dto.UsuarioCadastroRequestDTO;
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
}
