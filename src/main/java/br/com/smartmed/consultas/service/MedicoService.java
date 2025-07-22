package br.com.smartmed.consultas.service;

import br.com.smartmed.consultas.exception.*;
import br.com.smartmed.consultas.model.MedicoModel;
import br.com.smartmed.consultas.repository.MedicoRepository;
import br.com.smartmed.consultas.rest.dto.MedicoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicoService {
    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private ModelMapper modelMapper;

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

}