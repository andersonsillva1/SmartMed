package br.com.smartmed.consultas.repository;

import br.com.smartmed.consultas.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {
    boolean existsByEmail(String email);


    Optional<UsuarioModel> findByEmail(String email);

}
