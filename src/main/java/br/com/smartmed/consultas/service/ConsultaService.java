package br.com.smartmed.consultas.service;

import br.com.smartmed.consultas.exception.*;
import br.com.smartmed.consultas.model.ConsultaModel;
import br.com.smartmed.consultas.repository.ConsultaRepository;
import br.com.smartmed.consultas.repository.ConvenioRepository;
import br.com.smartmed.consultas.repository.FormaPagamentoRepository;
import br.com.smartmed.consultas.repository.MedicoRepository;
import br.com.smartmed.consultas.rest.dto.*;
import br.com.smartmed.consultas.model.MedicoModel;
import br.com.smartmed.consultas.model.PacienteModel;
import br.com.smartmed.consultas.model.ConvenioModel;
import br.com.smartmed.consultas.repository.PacienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;
    @Autowired
    private ConvenioRepository convenioRepository;

    @Transactional(readOnly = true)
    public ConsultaDTO obterPorId(int id){
        ConsultaModel consulta = consultaRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Consulta com ID " + id + " não encontrada."));
        return modelMapper.map(consulta, ConsultaDTO.class);
    }

    @Transactional(readOnly = true)
    public List<ConsultaDTO> obterTodasConsulta(){
        List<ConsultaModel> consultas = consultaRepository.findAll();
        return consultas.stream().map(consulta -> modelMapper.map(consulta, ConsultaDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public ConsultaDTO salvar(ConsultaModel novaConsulta) {
        try {
            if (novaConsulta.getId() != 0 && consultaRepository.existsById(novaConsulta.getId())) {
                throw new ConstraintException("Já existe uma consulta com esse ID " + novaConsulta.getId() + " na base de dados!");
            }
            return modelMapper.map(consultaRepository.save(novaConsulta), ConsultaDTO.class);
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível salvar a consulta " + novaConsulta.getId() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro de restrição de integridade ao salvar a consulta " + novaConsulta.getId() + ".");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível salvar a consulta " + novaConsulta.getId() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível salvar a consulta " + novaConsulta.getId() + ". Falha na conexão com o banco de dados!");
        }
    }

    @Transactional
    public ConsultaDTO atualizar(ConsultaModel consultaExistente) {
        try {
            if (!consultaRepository.existsById(consultaExistente.getId())) {
                throw new ObjectNotFoundException("A consulta com ID " + consultaExistente.getId() + " não existe na base de dados!");
            }
            return modelMapper.map(consultaRepository.save(consultaExistente), ConsultaDTO.class);
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível atualizar a consulta " + consultaExistente.getId() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao atualizar a consulta " + consultaExistente.getId() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível atualizar a consulta " + consultaExistente.getId() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível atualizar a consulta " + consultaExistente.getId() + ". Falha na conexão com o banco de dados!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível atualizar a consulta " + consultaExistente.getId() + ". Não encontrada no banco de dados!");
        }
    }

    @Transactional
    public void deletar(ConsultaModel consultaExistente) {
        try {
            if (!consultaRepository.existsById(consultaExistente.getId())) {
                throw new ObjectNotFoundException("Consulta inexistente na base de dados!");
            }
            consultaRepository.delete(consultaExistente);
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível deletar a consulta " + consultaExistente.getId() + " !");
        } catch (ConstraintException e) {
            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao deletar a consulta " + consultaExistente.getId() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível deletar a consulta " + consultaExistente.getId() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível deletar a consulta " + consultaExistente.getId() + ". Falha na conexão com o banco de dados!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível deletar a consulta " + consultaExistente.getId() + ". Não encontrada no banco de dados!");
        }
    }


    @Transactional(readOnly = true)
    public FaturamentoRelatorioDTO gerarRelatorioFaturamento(LocalDate dataInicio, LocalDate dataFim) {

        LocalDateTime inicioDoDia = dataInicio.atStartOfDay();
        LocalDateTime fimDoDia = dataFim.atTime(LocalTime.MAX);

        Double totalGeral = consultaRepository.findTotalFaturamento(inicioDoDia, fimDoDia);
        List<FaturamentoPorItemDTO> porFormaPagamento = consultaRepository.findFaturamentoPorFormaPagamento(inicioDoDia, fimDoDia);
        List<FaturamentoPorItemDTO> porConvenio = consultaRepository.findFaturamentoPorConvenio(inicioDoDia, fimDoDia);

        if (totalGeral == null) {
            totalGeral = 0.0;
        }
        if (porFormaPagamento == null) {
            porFormaPagamento = List.of();
        }
        if (porConvenio == null) {
            porConvenio = List.of();
        }

        FaturamentoRelatorioDTO relatorio = new FaturamentoRelatorioDTO();
        relatorio.setTotalGeral(totalGeral);
        relatorio.setPorFormaPagamento(porFormaPagamento);
        relatorio.setPorConvenio(porConvenio);

        return relatorio;
    }

    @Transactional
    public ConsultaDTO agendarAutomaticamente(AgendamentoInteligenteRequestDTO request) {

        if (request.getMedicoID() == null && request.getEspecialidadeID() == null) {
            throw new BusinessRuleException("É necessário informar um Médico ou uma Especialidade para o agendamento inteligente.");
        }
        if (request.getMedicoID() != null && request.getEspecialidadeID() != null) {
            throw new BusinessRuleException("Não é possível informar Médico e Especialidade simultaneamente. Escolha apenas um.");
        }

        List<MedicoModel> medicosElegiveis;
        if (request.getMedicoID() != null) {
            MedicoModel medico = medicoRepository.findById(request.getMedicoID())
                    .orElseThrow(() -> new ObjectNotFoundException("Médico com ID " + request.getMedicoID() + " não encontrado."));
            if (!medico.isAtivo()) {
                throw new BusinessRuleException("O médico selecionado não está ativo e não pode agendar consultas.");
            }
            medicosElegiveis = List.of(medico);
        } else {
            medicosElegiveis = medicoRepository.findByEspecialidadeIDAndAtivoTrue(request.getEspecialidadeID());
            if (medicosElegiveis.isEmpty()) {
                throw new ObjectNotFoundException("Nenhum médico ativo encontrado para a especialidade com ID " + request.getEspecialidadeID() + ".");
            }

        }

        PacienteModel paciente = pacienteRepository.findById(request.getPacienteID())
                .orElseThrow(() -> new ObjectNotFoundException("Paciente com ID " + request.getPacienteID() + " não encontrado."));
        formaPagamentoRepository.findById(request.getFormaPagamentoID())
                .orElseThrow(() -> new ObjectNotFoundException("Forma de Pagamento com ID " + request.getFormaPagamentoID() + " não encontrada."));

        ConvenioModel convenio = null;
        if (request.getConvenioID() != null) {
            convenio = convenioRepository.findById(request.getConvenioID())
                    .orElseThrow(() -> new ObjectNotFoundException("Convênio com ID " + request.getConvenioID() + " não encontrado."));
        }

        LocalDateTime dataHoraAtual = request.getDataHoraInicial();
        LocalDateTime limiteBusca = dataHoraAtual.plusDays(7);
        int duracaoMinutos = request.getDuracaoConsultaMinutos();

        ConsultaModel novaConsulta = null;
        MedicoModel medicoEncontrado = null;
        LocalDateTime horarioDisponivel = null;

        for (MedicoModel medico : medicosElegiveis) {
            LocalDateTime tentativaHorario = dataHoraAtual;
            while (tentativaHorario.isBefore(limiteBusca)) {
                if (tentativaHorario.getDayOfWeek().getValue() >= 6) {
                    tentativaHorario = tentativaHorario.plusDays(1).withHour(8).withMinute(0);
                    continue;
                }
                if (tentativaHorario.getHour() < 8 || tentativaHorario.getHour() >= 18) {
                    tentativaHorario = tentativaHorario.plusHours(1).withMinute(0);
                    continue;
                }

                LocalDateTime fimTentativaHorario = tentativaHorario.plusMinutes(duracaoMinutos);

                List<ConsultaModel> consultasOcupadas = consultaRepository.findConsultasByMedicoAndPeriodo(
                        medico.getId(), tentativaHorario, fimTentativaHorario);

                boolean horarioLivre = true;
                for (ConsultaModel consultaOcupada : consultasOcupadas) {

                    LocalDateTime inicioOcupado = consultaOcupada.getDataHoraConsulta();
                    LocalDateTime fimOcupado = inicioOcupado.plusMinutes(30);

                    if ( (tentativaHorario.isBefore(fimOcupado) && fimTentativaHorario.isAfter(inicioOcupado)) ||
                            (tentativaHorario.isEqual(inicioOcupado)) ||
                            (fimTentativaHorario.isEqual(fimOcupado))
                    ) {
                        horarioLivre = false;
                        break;
                    }
                }

                if (horarioLivre) {
                    horarioDisponivel = tentativaHorario;
                    medicoEncontrado = medico;
                    break;
                }

                tentativaHorario = tentativaHorario.plusMinutes(15);
            }
            if (horarioDisponivel != null) {
                break;
            }
        }

        if (horarioDisponivel == null) {
            throw new BusinessRuleException("Não foi encontrado nenhum horário disponível para os critérios especificados.");
        }

        Double valorConsulta;
        if (convenio != null) {
            valorConsulta = medicoEncontrado.getValorConsultaReferencia() * 0.5;
        } else {
            valorConsulta = (double) medicoEncontrado.getValorConsultaReferencia();
        }

        novaConsulta = new ConsultaModel();
        novaConsulta.setDataHoraConsulta(horarioDisponivel);
        novaConsulta.setStatus("AGENDADA");
        novaConsulta.setValor(valorConsulta);
        novaConsulta.setObservacoes("Agendamento inteligente automático.");
        novaConsulta.setPacienteID(paciente.getId());
        novaConsulta.setMedicoID(medicoEncontrado.getId());
        novaConsulta.setFormaPagamentoID(request.getFormaPagamentoID());
        novaConsulta.setConvenioID(request.getConvenioID() != null ? request.getConvenioID() : 0);

        consultaRepository.save(novaConsulta);

        return modelMapper.map(novaConsulta, ConsultaDTO.class);
    }

    @Transactional(readOnly = true)
    public List<HistoricoConsultaResponseDTO> obterHistoricoConsultas(HistoricoConsultaRequestDTO request){
        PacienteModel paciente = pacienteRepository.findById(request.getPacienteID())
                .orElseThrow(()-> new ObjectNotFoundException("Paciente com ID " + request.getPacienteID() + " não encontrado. "));

        if (!paciente.getAtivo()){
            throw new BusinessRuleException("Paciente com ID " + request.getPacienteID() + " está inativo. Histórico não pode ser consultado.");

        }

        LocalDateTime dataInicio = null;
        if (request.getDataInicio() != null){
            dataInicio= request.getDataInicio().atStartOfDay();
        }
        LocalDateTime dataFim = null;
        if (request.getDataFim() != null){
            dataFim = request.getDataFim().atTime(LocalTime.MAX);

        }
        if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
            throw new BusinessRuleException("A data inicial não pode ser posterior à data final no filtro");
        }

        List<HistoricoConsultaResponseDTO> historico = consultaRepository.findHistoricoConsultas(
                request.getPacienteID(),
                dataInicio,
                dataFim,
                request.getMedicoID(),
                request.getStatus(),
                request.getEspecialidadeID()
        );
        return historico;
    }

    @Transactional
    public CancelamentoResponseDTO cancelarConsulta(CancelamentoRequestDTO request){
        ConsultaModel consulta = consultaRepository.findById(request.getConsultaID()).orElseThrow(()->new ObjectNotFoundException("Consulta com ID " + request.getConsultaID() + " não encontrada."));
        if (!consulta.getStatus().equalsIgnoreCase("AGENDADA")){
            throw new BusinessRuleException("A consulta com ID " + consulta.getId() + " não pode ser cancelada, pois o status atual é '" + consulta.getStatus() + "'." );
        }
        if (consulta.getDataHoraConsulta().isBefore(LocalDateTime.now())){
            throw new BusinessRuleException("Não é possivel cancelar uma consulta que já ocorreu.");
        }
        consulta.setStatus("CANCELADA");

        String observacoesAtuais = consulta.getObservacoes() != null ? consulta.getObservacoes() : "";
        consulta.setObservacoes(observacoesAtuais + "\n[CANCELADA] motivo: " + request.getMotivo());

        consultaRepository.save(consulta);

        return new CancelamentoResponseDTO("Consulta cancelado com sucesso", consulta.getStatus());
    }

    @Transactional(readOnly = true)
    public List<EspecialidadeAtendimentosDTO> gerarRelatoriosEspecialidades(RelatorioEspecialidadesRequestDTO request){
        if  (request.getDataInicio().isAfter(request.getDataFim())){
            throw new BusinessRuleException("A data inicia não pode ser posterior á data final.");
        }
        LocalDateTime inicioDoDia = request.getDataInicio().atStartOfDay();
        LocalDateTime fimDoDia = request.getDataFim().atTime(LocalTime.MAX);

        return consultaRepository.findEspecialidadesMaisAtendidas(inicioDoDia, fimDoDia);

    }
}
