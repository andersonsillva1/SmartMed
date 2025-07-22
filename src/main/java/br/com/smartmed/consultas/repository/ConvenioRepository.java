package br.com.smartmed.consultas.repository;

import br.com.smartmed.consultas.model.ConvenioModel;
import org.hibernate.validator.constraints.br.CNPJ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConvenioRepository extends JpaRepository<ConvenioModel, Integer> {

    List<ConvenioModel> findByNome(String pNome);
    Optional<ConvenioModel> findByCnpj(String pCnpj);
    List<ConvenioModel> findByTelefone(String pTelefone);
    List<ConvenioModel> findByEmail(String pEmail);
    List<ConvenioModel> findByAtivo(boolean pAtivo);
    boolean existsByCnpj(@CNPJ(message = "CNPJ Inválido.") String pCnpj);
}