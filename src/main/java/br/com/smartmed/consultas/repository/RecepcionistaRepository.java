package br.com.smartmed.consultas.repository;

import br.com.smartmed.consultas.model.RecepcionistaModel;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    @Query("SELECT r FROM RecepcionistaModel r WHERE " +
            "(:status IS NULL OR r.ativo = :status) AND " +
            "(:dataInicio IS NULL OR r.dataAdmissao >= :dataInicio) AND " +
            "(:dataFim IS NULL OR r.dataAdmissao <= :dataFim) " +
            "ORDER BY r.nome ASC")
    Page<RecepcionistaModel> findRecepcionistasWithFilters(
            @Param("status") Boolean status,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable);
}
