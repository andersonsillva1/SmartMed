
package br.com.smartmed.consultas.repository;

import br.com.smartmed.consultas.model.MedicoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<MedicoModel, Integer> {

    List<MedicoModel> findByNome(String pNome);
    Optional<MedicoModel> findByCrm(String pCrm);
    List<MedicoModel> findByTelefone(String pTelefone);
    List<MedicoModel> findByEmail(String pEmail);
    List<MedicoModel> findByValorConsultaReferencia(Float pValorConsultaReferencia);
    List<MedicoModel> findByAtivo(boolean pAtivo);
    boolean existsByCrm(String pCrm);
    boolean existsByTelefone(String pTelefone);
    boolean existsByEmail(String pEmail);

    List<MedicoModel> findByEspecialidadeIDAndAtivoTrue(Integer especialidadeId);
}
