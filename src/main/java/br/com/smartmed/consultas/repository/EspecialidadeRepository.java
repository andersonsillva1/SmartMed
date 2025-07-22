package br.com.smartmed.consultas.repository;

import br.com.smartmed.consultas.model.EspecialidadeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspecialidadeRepository extends JpaRepository<EspecialidadeModel, Integer> {

    List<EspecialidadeModel> findByNome(String pNome);
    List<EspecialidadeModel> findByDescricao(String pDescricao);
    boolean existsByNome(String pNome);
    boolean existsByDescricao(String pDescricao);
}