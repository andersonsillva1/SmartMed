package br.com.smartmed.consultas.repository;

import br.com.smartmed.consultas.model.ConsultaModel;
import br.com.smartmed.consultas.rest.dto.EspecialidadeAtendimentosDTO;
import br.com.smartmed.consultas.rest.dto.FaturamentoPorItemDTO;
import br.com.smartmed.consultas.rest.dto.HistoricoConsultaResponseDTO;
import br.com.smartmed.consultas.rest.dto.RankingMedicosAtendimentosResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<ConsultaModel, Integer> {

    List<ConsultaModel> findByDataHoraConsulta(LocalDateTime pDataHoraConsulta);
    List<ConsultaModel> findByStatus(String pStatus);
    List<ConsultaModel> findByValor(Double pValor);
    List<ConsultaModel> findByObservacoes(String pObservacoes);
    boolean existsByDataHoraConsulta(LocalDateTime pDataHoraConsulta);

    @Query("SELECT NEW br.com.smartmed.consultas.rest.dto.FaturamentoPorItemDTO(fp.descricao, SUM(c.valor)) " +
            "FROM ConsultaModel c JOIN FormaPagamentoModel fp ON c.formaPagamentoID = fp.id " +
            "WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :dataInicio AND :dataFim " +
            "GROUP BY fp.descricao")
    List<FaturamentoPorItemDTO> findFaturamentoPorFormaPagamento(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);

    @Query("SELECT NEW br.com.smartmed.consultas.rest.dto.FaturamentoPorItemDTO(conv.nome, SUM(c.valor)) " +
            "FROM ConsultaModel c JOIN ConvenioModel conv ON c.convenioID = conv.id " +
            "WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :dataInicio AND :dataFim " +
            "GROUP BY conv.nome")
    List<FaturamentoPorItemDTO> findFaturamentoPorConvenio(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);

    @Query("SELECT SUM(c.valor) FROM ConsultaModel c " +
            "WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :dataInicio AND :dataFim")
    Double findTotalFaturamento(@Param("dataInicio") LocalDateTime dataInicio,
                                @Param("dataFim") LocalDateTime dataFim);

    @Query("SELECT c FROM ConsultaModel c WHERE c.medicoID = :medicoId " +
            "AND ((c.dataHoraConsulta BETWEEN :inicioPeriodo AND :fimPeriodo) " +
            "OR (FUNCTION('ADD_MINUTES', c.dataHoraConsulta, 30) BETWEEN :inicioPeriodo AND :fimPeriodo)) " +
            "ORDER BY c.dataHoraConsulta ASC")
    List<ConsultaModel> findConsultasByMedicoAndPeriodo(
            @Param("medicoId") Integer medicoId,
            @Param("inicioPeriodo") LocalDateTime inicioPeriodo,
            @Param("fimPeriodo") LocalDateTime fimPeriodo);

    @Query("SELECT NEW br.com.smartmed.consultas.rest.dto.HistoricoConsultaResponseDTO(" +
            "c.dataHoraConsulta, m.nome, e.nome, c.valor, c.status, c.observacoes) " +
            "FROM ConsultaModel c " +
            "JOIN MedicoModel m ON c.medicoID = m.id " +
            "JOIN EspecialidadeModel e ON m.especialidadeID = e.id " +
            "WHERE c.pacienteID = :pacienteID " +
            "AND (:dataInicio IS NULL OR c.dataHoraConsulta >= :dataInicio) " +
            "AND (:dataFim IS NULL OR c.dataHoraConsulta <= :dataFim) " +
            "AND (:medicoID IS NULL OR c.medicoID = :medicoID) " +
            "AND (:status IS NULL OR c.status = :status) " +
            "AND (:especialidadeID IS NULL OR m.especialidadeID = :especialidadeID) " +
            "ORDER BY c.dataHoraConsulta DESC")
    List<HistoricoConsultaResponseDTO> findHistoricoConsultas(
            @Param("pacienteId") Integer pacienteId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            @Param("medicoId") Integer medicoId,
            @Param("status") String status,
            @Param("especialidadeId") Integer especialidadeId);

    List<ConsultaModel> findByMedicoIDAndDataHoraConsultaBetween(Integer medicoId, LocalDateTime inicioDoDia, LocalDateTime fimDoDia);

    @Query("SELECT NEW br.com.smartmed.consultas.rest.dto.EspecialidadeAtendimentosDTO(e.nome, COUNT(c.id)) " +
    "FROM ConsultaModel c " +
    "JOIN MedicoModel m ON c.medicoID = m.id " +
    "JOIN EspecialidadeModel e ON  m.especialidadeID = e.id " +
    "WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :dataInicio AND :dataFim " +
    "GROUP BY e.nome " +
    "ORDER BY COUNT(c.id) DESC")
    List<EspecialidadeAtendimentosDTO> findEspecialidadesMaisAtendidas(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);


    @Query(value = "SELECT NEW br.com.smartmed.consultas.rest.dto.RankingMedicosAtendimentosResponseDTO(m.nome, COUNT(c.id)) " +
            "FROM ConsultaModel c " +
            "JOIN MedicoModel m ON c.medicoID = m.id " +
            "WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :dataInicio AND :dataFim " +
            "GROUP BY m.nome " +
            "ORDER BY COUNT(c.id) DESC",
            countQuery = "SELECT COUNT(DISTINCT m.nome) FROM ConsultaModel c JOIN MedicoModel m ON c.medicoID = m.id WHERE c.status = 'REALIZADA' AND c.dataHoraConsulta BETWEEN :dataInicio AND :dataFim")
    Page<RankingMedicosAtendimentosResponseDTO> findRankingMedicos(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            Pageable pageable);

}


