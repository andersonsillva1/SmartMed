package br.com.smartmed.consultas.rest.controller;

import br.com.smartmed.consultas.rest.dto.FaturamentoRelatorioDTO;
import br.com.smartmed.consultas.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
}