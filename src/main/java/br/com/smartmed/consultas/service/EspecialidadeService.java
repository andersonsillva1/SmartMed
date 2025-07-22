package br.com.smartmed.consultas.service;

import br.com.smartmed.consultas.exception.*;
import br.com.smartmed.consultas.model.EspecialidadeModel;
import br.com.smartmed.consultas.repository.EspecialidadeRepository;
import br.com.smartmed.consultas.rest.dto.EspecialidadeDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EspecialidadeService {
    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public EspecialidadeDTO obterPorId(int id) {
        EspecialidadeModel especialidade = especialidadeRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Especialidade com ID " + id + " não encontrado."));
//        return especialidade.toDTO();
        return modelMapper.map(especialidade, EspecialidadeDTO.class);
    }

    @Transactional(readOnly = true)
    public List<EspecialidadeDTO> obterTodos() {
        List<EspecialidadeModel> especialidades = especialidadeRepository.findAll();
        return especialidades.stream().map(especialidade -> modelMapper.map(especialidade, EspecialidadeDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public EspecialidadeDTO salvar(EspecialidadeModel novaEspecialidade) {

        try {

            if (especialidadeRepository.existsByNome(novaEspecialidade.getNome())) {
                throw new ConstraintException("Já existe uma especialidade com esse Nome " + novaEspecialidade.getNome() + " na base de dados!");
            }

//            return especialidadeRepository.save(novaEspecialidade).toDTO();
            return modelMapper.map(especialidadeRepository.save(novaEspecialidade), EspecialidadeDTO.class);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível salvar a especialidade " + novaEspecialidade.getNome() + " !");
        } catch (ConstraintException e) {

            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro de restrição de integridade ao salvar a especialidade " + novaEspecialidade.getNome() + ".");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível salvar a especialidade " + novaEspecialidade.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível salvar a especialidade " + novaEspecialidade.getNome() + ". Falha na conexão com o banco de dados!");
        }
    }

    @Transactional
    public EspecialidadeDTO atualizar(EspecialidadeModel especialidadeExistente) {

        try {
            if (!especialidadeRepository.existsById(especialidadeExistente.getId())) {
                throw new ConstraintException("A especialidade com ID " + especialidadeExistente.getId() + " não existe na base de dados!");
            }

//            return especialidadeRepository.save(especialidadeExistente).toDTO();
            return modelMapper.map(especialidadeRepository.save(especialidadeExistente), EspecialidadeDTO.class);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível atualizar a especialidade " + especialidadeExistente.getNome() + " !");
        } catch (ConstraintException e) {

            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao atualizar a especialidade " + especialidadeExistente.getNome() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível atualizar a especialidade " + especialidadeExistente.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível atualizar a especialidade " + especialidadeExistente.getNome() + ". Falha na conexão com o banco de dados!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível atualizar a especialidade " + especialidadeExistente.getNome() + ". Não encontrado no banco de dados!");
        }
    }

    @Transactional
    public void deletar(EspecialidadeModel especialidadeExistente) {

        try {

            if (!especialidadeRepository.existsById(especialidadeExistente.getId())) {
                throw new ConstraintException("Especialidade inexistente na base de dados!");
            }

            especialidadeRepository.delete(especialidadeExistente);

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Não foi possível deletar a especialidade " + especialidadeExistente.getNome() + " !");
        } catch (ConstraintException e) {

            if (e.getMessage() == null || e.getMessage().isBlank()) {
                throw new ConstraintException("Erro ao deletar a especialidade " + especialidadeExistente.getNome() + ": Restrição de integridade de dados.");
            }
            throw e;
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Não foi possível deletar a especialidade " + especialidadeExistente.getNome() + ". Violação de regra de negócio!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Não foi possível atualizar a especialidade " + especialidadeExistente.getNome() + ". Falha na conexão com o banco de dados!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Não foi possível deletar a especialidade " + especialidadeExistente.getNome() + ". Não encontrado no banco de dados!");
        }
    }
}