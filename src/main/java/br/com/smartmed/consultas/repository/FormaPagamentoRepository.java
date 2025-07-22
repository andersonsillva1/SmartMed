package br.com.smartmed.consultas.repository;

import br.com.smartmed.consultas.model.FormaPagamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormaPagamentoRepository extends JpaRepository<FormaPagamentoModel, Integer> {

    List<FormaPagamentoModel> findByDescricao(String pDescricao);
    boolean existsByDescricao(String pDescricao);

}
