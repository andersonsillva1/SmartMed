
package br.com.smartmed.consultas.rest.controller;

import br.com.smartmed.consultas.model.EspecialidadeModel;
import br.com.smartmed.consultas.rest.dto.EspecialidadeAtendimentosDTO;
import br.com.smartmed.consultas.rest.dto.EspecialidadeDTO;
import br.com.smartmed.consultas.rest.dto.RelatorioEspecialidadesRequestDTO;
import br.com.smartmed.consultas.service.ConsultaService;
import br.com.smartmed.consultas.service.EspecialidadeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/especialidade")
public class EspecialidadeController {

    @Autowired
    private EspecialidadeService especialidadeService;

    @Autowired
    private ConsultaService consultaService;

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadeDTO> obterPorId(@PathVariable int id) {
        EspecialidadeDTO especialidadeDTO = especialidadeService.obterPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(especialidadeDTO);
    }

    @GetMapping()
    public ResponseEntity<List<EspecialidadeDTO>> obterTodos() {
        List<EspecialidadeDTO> especialidadeDTOList = especialidadeService.obterTodos();
        return ResponseEntity.ok(especialidadeDTOList);
    }

    @PostMapping()
    public ResponseEntity<EspecialidadeDTO> salvar(@Valid @RequestBody EspecialidadeModel novaEspecialidade) {
        EspecialidadeDTO novaEspecialidadeDTO = especialidadeService.salvar(novaEspecialidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaEspecialidadeDTO);
    }

    @PutMapping
    public ResponseEntity<EspecialidadeDTO> atualizar(@Valid @RequestBody EspecialidadeModel especialidadeExistente) {
        EspecialidadeDTO especialidadeExistenteDTO = especialidadeService.atualizar(especialidadeExistente);
        return ResponseEntity.status(HttpStatus.OK).body(especialidadeExistenteDTO);
    }

    @DeleteMapping
    public void deletar(@Valid @RequestBody EspecialidadeModel especialidadeExistente) {
        especialidadeService.deletar(especialidadeExistente);
    }

    @PostMapping("/especialidades-frequentes")
    public ResponseEntity<List<EspecialidadeAtendimentosDTO>> gerarRelatorioEspecialidades(@Valid @RequestBody RelatorioEspecialidadesRequestDTO request){
        List<EspecialidadeAtendimentosDTO> relatorio = consultaService.gerarRelatoriosEspecialidades(request);
        return ResponseEntity.status(HttpStatus.OK).body(relatorio);
    }
}