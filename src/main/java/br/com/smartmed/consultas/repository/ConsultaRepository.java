package br.com.smartmed.consultas.repository;

import br.com.smartmed.consultas.model.ConsultaModel;
import br.com.smartmed.consultas.rest.dto.FaturamentoPorItemDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

}


