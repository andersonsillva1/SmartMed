package br.com.smartmed.consultas.repository;

import br.com.smartmed.consultas.model.RecepcionistaModel;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecepcionistaRepository extends JpaRepository<RecepcionistaModel, Integer> {

    List<RecepcionistaModel> findByNome(String pNome);
    Optional<RecepcionistaModel> findByCpf(String pCpf);
    List<RecepcionistaModel> findByTelefone(String pTelefone);
    List<RecepcionistaModel> findByEmail(String pEmail);
    List<RecepcionistaModel> findByAtivo(boolean pAtivo);
    boolean existsByCpf(@CPF(message = "CPF Inválido.") String pCpf);
    boolean existsByTelefone(String pTelefone);
    boolean existsByEmail(String pEmail);
}
