package br.com.smartmed.consultas.rest.controller;

import br.com.smartmed.consultas.rest.dto.HistoricoConsultaRequestDTO;
import br.com.smartmed.consultas.rest.dto.HistoricoConsultaResponseDTO;
import br.com.smartmed.consultas.service.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/consultas")
public class HistoricoController {

    @Autowired
    private ConsultaService consultaService;

    @PostMapping("/historico")
    public ResponseEntity<List<HistoricoConsultaResponseDTO>> obterHistoricoConsultas(
            @Valid @RequestBody HistoricoConsultaRequestDTO request) {
        List<HistoricoConsultaResponseDTO> historico = consultaService.obterHistoricoConsultas(request);
        return ResponseEntity.status(HttpStatus.OK).body(historico);
    }

}
