package br.com.smartmed.consultas.service;

import br.com.smartmed.consultas.exception.*;
import br.com.smartmed.consultas.model.ConsultaModel;
import br.com.smartmed.consultas.model.MedicoModel;
import br.com.smartmed.consultas.repository.ConsultaRepository;
import br.com.smartmed.consultas.repository.MedicoRepository;
import br.com.smartmed.consultas.rest.dto.AgendaMedicoRequestDTO;
import br.com.smartmed.consultas.rest.dto.AgendaMedicoResponseDTO;
import br.com.smartmed.consultas.rest.dto.MedicoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MedicoService {
    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Transactional(readOnly = true)
    public MedicoDTO obterPorId(int id) {
        MedicoModel medico = medicoRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Médico com ID " + id + "Não encontrado"));
//        return medico.toDTO();
        return modelMapper.map(medico, MedicoDTO.class);
    }

    @Transactional(readOnly = true)
    public List<MedicoDTO> obterTodos() {
        List<MedicoModel> medicos = medicoRepository.findAll();
        return medicos.stream().map(medico -> modelMapper.map(medico, MedicoDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public MedicoDTO salvar(MedicoModel novoMedico) {
        try {
            if (medicoRepository.existsByCrm(novoMedico.getCrm())) {
                throw new ConstraintException("Já existe um médico com esse CRM " + novoMedico.getCrm() + " na base de dados.");
            }
//            return medicoRepository.save(novoMedico).toDTO();
            return modelMapper.map(medicoRepository.save(novoMedico), MedicoDTO.class);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível salvar o médico " + novoMedico.getNome() + " !");
        } catch (ConstraintException e) {

            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro de restrição de integridade ao salvar o médico " + novoMedico.getNome() + ".");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível salvar o médico " + novoMedico.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível salvar o médico " + novoMedico.getNome() + ". Falha na conexão com o banco de dados!");
        }
    }

    @Transactional
    public MedicoDTO atualizar(MedicoModel medicoExistente) {
        try {
            if (!medicoRepository.existsByCrm(medicoExistente.getCrm())) {
                throw new ConstraintException("Não existe médico com esse CRM " + medicoExistente.getCrm() + " na base de dados");
            }
//            return medicoRepository.save(medicoExistente).toDTO();
            return modelMapper.map(medicoRepository.save(medicoExistente), MedicoDTO.class);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! não foi possivel salvar o médico " + medicoExistente.getNome() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro de restrição de integridade ao salvar o médico " + medicoExistente.getNome() + ".");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possivel salvar o médico " + medicoExistente.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possivel salvar o médico " + medicoExistente.getNome() + ". Falha na conexão com o bando de dados!");
        } catch (ObjectNotFoundException e){
            throw new ObjectNotFoundException("Erro! Não foi possível atualizar o médico" + medicoExistente.getNome() + ". Não encontrado no banco de dados!");
        }
    }

    @Transactional
    public void deletar (MedicoModel medicoExistente){

        try {
            if (!medicoRepository.existsByCrm(medicoExistente.getCrm())){
                throw new ConstraintException("Medico inexistente na base de dados");
            }
            medicoRepository.delete(medicoExistente);
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível deletar o médico " + medicoExistente.getNome() + " !");
        } catch (ConstraintException e) {

            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao deletar o médico " + medicoExistente.getNome() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível deletar o médico " + medicoExistente.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível atualizar o deletar " + medicoExistente.getNome() + ". Falha na conexão com o banco de dados!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível deletar o médico" + medicoExistente.getNome() + ". Não encontrado no banco de dados!");
        }
    }

    @Transactional(readOnly = true)
    public AgendaMedicoResponseDTO gerenciarAgenda(AgendaMedicoRequestDTO request){
        MedicoModel medico = medicoRepository.findById(request.getMedicoID()).orElseThrow(()-> new ObjectNotFoundException("Médico com ID " + request.getMedicoID() + " não encontrado"));

        if (!medico.isAtivo()){
            throw new BusinessRuleException("Médico com ID " + request.getMedicoID() + " está inativo. Agenda não pode ser consultada.");
            
        }
        LocalDateTime inicioDoDia = request.getData().atStartOfDay();
        LocalDateTime fimDoDia = request.getData().atTime(LocalTime.MAX);

        List<ConsultaModel> consultasDoDia = consultaRepository.findByMedicoIDAndDataHoraConsultaBetween(
                request.getMedicoID(), inicioDoDia, fimDoDia);

        Set<String> horariosOcupadosSet = new HashSet<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        int duracaoPadraoConsultaMinutos = 30;

        for (ConsultaModel consulta : consultasDoDia) {
            LocalDateTime inicioConsulta = consulta.getDataHoraConsulta();
            LocalDateTime fimConsulta = inicioConsulta.plusMinutes(duracaoPadraoConsultaMinutos); // Assumindo duração fixa

            LocalDateTime currentSlot = inicioConsulta;
            while (currentSlot.isBefore(fimConsulta)) {
                horariosOcupadosSet.add(currentSlot.format(formatter));
                currentSlot = currentSlot.plusMinutes(duracaoPadraoConsultaMinutos);
            }
        }

        List<String> horariosOcupados = new ArrayList<>(horariosOcupadosSet);
        Collections.sort(horariosOcupados); // Ordena os horários ocupados

        List<String> todosOsSlotsPossiveis = new ArrayList<>();
        LocalTime horarioInicioExpediente = LocalTime.of(8, 0);
        LocalTime horarioFimExpediente = LocalTime.of(18, 0);

        LocalTime slotAtual = horarioInicioExpediente;
        while (slotAtual.isBefore(horarioFimExpediente)) {
            todosOsSlotsPossiveis.add(slotAtual.format(formatter));
            slotAtual = slotAtual.plusMinutes(duracaoPadraoConsultaMinutos);
        }

        List<String> horariosDisponiveis = new ArrayList<>();
        LocalDateTime agora = LocalDateTime.now();

        for (String slot : todosOsSlotsPossiveis) {
            LocalTime tempoDoSlot = LocalTime.parse(slot, formatter);
            LocalDateTime dataHoraSlot = request.getData().atTime(tempoDoSlot);

            if (request.getData().isEqual(agora.toLocalDate()) && dataHoraSlot.isBefore(agora)) {
                continue;
            }

            if (!horariosOcupadosSet.contains(slot)) {
                horariosDisponiveis.add(slot);
            }
        }
        
        AgendaMedicoResponseDTO response = new AgendaMedicoResponseDTO();
        response.setMedico(medico.getNome());
        response.setData(request.getData());
        response.setHorariosOcupados(horariosOcupados);
        response.setHorariosDisponiveis(horariosDisponiveis);

        return response;
    }

}