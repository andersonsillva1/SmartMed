package br.com.smartmed.consultas.rest.controller;

import br.com.smartmed.consultas.rest.dto.FaturamentoRelatorioDTO;
import br.com.smartmed.consultas.rest.dto.PageResponseDTO;
import br.com.smartmed.consultas.rest.dto.RankingMedicosAtendimentosResponseDTO;
import br.com.smartmed.consultas.rest.dto.RankingMedicosRequestDTO;
import br.com.smartmed.consultas.service.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private ConsultaService consultaService;

    @GetMapping("/faturamento")
    public ResponseEntity<FaturamentoRelatorioDTO> gerarRelatorioFaturamento(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        FaturamentoRelatorioDTO relatorio = consultaService.gerarRelatorioFaturamento(dataInicio, dataFim);
        return ResponseEntity.status(HttpStatus.OK).body(relatorio);
    }

    @PostMapping("/medicos-mais-ativos")
    public ResponseEntity<PageResponseDTO<RankingMedicosAtendimentosResponseDTO>> gerarRankingMedicos(
            @Valid @RequestBody RankingMedicosRequestDTO request) {
        PageResponseDTO<RankingMedicosAtendimentosResponseDTO> ranking = consultaService.gerarRankingMedicos(request);
        return ResponseEntity.status(HttpStatus.OK).body(ranking);
    }
}