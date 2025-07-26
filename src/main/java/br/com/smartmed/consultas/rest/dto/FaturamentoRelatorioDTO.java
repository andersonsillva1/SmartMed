package br.com.smartmed.consultas.rest.dto;

import lombok.Data;
import java.util.List;

@Data
public class FaturamentoRelatorioDTO {
    private Double totalGeral;
    private List<FaturamentoPorItemDTO> porFormaPagamento;
    private List<FaturamentoPorItemDTO> porConvenio;
}