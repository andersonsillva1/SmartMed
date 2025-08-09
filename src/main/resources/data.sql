-- Script para popular o banco de dados H2 com dados de teste na inicialização.
-- Este arquivo deve ser colocado em src/main/resources.

-- ===============================================
-- 1. Inserir Formas de Pagamento (4 registros)
-- ===============================================
INSERT INTO FORMA_PAGAMENTO (descricao) VALUES ('Dinheiro');
INSERT INTO FORMA_PAGAMENTO (descricao) VALUES ('Cartão de Crédito');
INSERT INTO FORMA_PAGAMENTO (descricao) VALUES ('Pix');
INSERT INTO FORMA_PAGAMENTO (descricao) VALUES ('Convênio');

-- ===============================================
-- 2. Inserir Convênios (3 registros)
-- ===============================================
INSERT INTO CONVENIO (nome, cnpj, telefone, email, ativo) VALUES ('Unimed', '11222333000144', '79981112233', 'unimed@email.com.br', TRUE);
INSERT INTO CONVENIO (nome, cnpj, telefone, email, ativo) VALUES ('Bradesco Saúde', '44555666000177', '79984445566', 'bradesco@email.com.br', TRUE);
INSERT INTO CONVENIO (nome, cnpj, telefone, email, ativo) VALUES ('SulAmérica', '77888999000155', '79987778899', 'sulamerica@email.com.br', TRUE);

-- ===============================================
-- 3. Inserir Especialidades (5 registros)
-- ===============================================
INSERT INTO ESPECIALIDADE (nome, descricao) VALUES ('Cardiologia', 'Especialidade do coração');
INSERT INTO ESPECIALIDADE (nome, descricao) VALUES ('Dermatologia', 'Especialidade da pele');
INSERT INTO ESPECIALIDADE (nome, descricao) VALUES ('Clínico Geral', 'Atendimento primário e geral');
INSERT INTO ESPECIALIDADE (nome, descricao) VALUES ('Oftalmologia', 'Especialidade da visão');
INSERT INTO ESPECIALIDADE (nome, descricao) VALUES ('Ortopedia', 'Especialidade do aparelho locomotor');

-- ===============================================
-- 4. Inserir Recepcionistas (5 registros)
-- ===============================================
INSERT INTO RECEPCIONISTA (nome, cpf, data_nascimento, data_admissao, data_demissao, telefone, email, ativo) VALUES ('Bruna Lopes', '11111111111', '1995-01-20', '2023-05-10', NULL, '79911223344', 'bruna@smartmed.com', TRUE);
INSERT INTO RECEPCIONISTA (nome, cpf, data_nascimento, data_admissao, data_demissao, telefone, email, ativo) VALUES ('Ricardo Alves', '22222222222', '1998-08-15', '2024-02-20', NULL, '79955667788', 'ricardo@smartmed.com', TRUE);
INSERT INTO RECEPCIONISTA (nome, cpf, data_nascimento, data_admissao, data_demissao, telefone, email, ativo) VALUES ('Leticia Ferreira', '33333333333', '1993-03-30', '2022-11-05', '2025-11-05', '79933445566', 'leticia@smartmed.com', FALSE); -- Inativa
INSERT INTO RECEPCIONISTA (nome, cpf, data_nascimento, data_admissao, data_demissao, telefone, email, ativo) VALUES ('Felipe Costa', '44444444444', '1996-06-12', '2024-01-15', NULL, '79922113344', 'felipe@smartmed.com', TRUE);
INSERT INTO RECEPCIONISTA (nome, cpf, data_nascimento, data_admissao, data_demissao, telefone, email, ativo) VALUES ('Juliana Pires', '55555555555', '1997-04-10', '2023-09-20', NULL, '79911112233', 'juliana@smartmed.com', TRUE);

-- ===============================================
-- 5. Inserir Médicos (5 registros)
-- Inclui médicos ativos e inativos em diferentes especialidades.
-- ===============================================
INSERT INTO MEDICO (nome, crm, telefone, email, valor_consulta_referencia, ativo, especialidadeid) VALUES ('Dr. João Silva', 'CRM12345', '79987654321', 'joao.s@medico.com', 150.00, TRUE, 1);
INSERT INTO MEDICO (nome, crm, telefone, email, valor_consulta_referencia, ativo, especialidadeid) VALUES ('Dra. Maria Souza', 'CRM54321', '79981234567', 'maria.s@medico.com', 180.00, TRUE, 2);
INSERT INTO MEDICO (nome, crm, telefone, email, valor_consulta_referencia, ativo, especialidadeid) VALUES ('Dr. Carlos Oliveira', 'CRM98765', '79988998877', 'carlos.o@medico.com', 120.00, TRUE, 3);
INSERT INTO MEDICO (nome, crm, telefone, email, valor_consulta_referencia, ativo, especialidadeid) VALUES ('Dra. Ana Costa', 'CRM11223', '79987776655', 'ana.c@medico.com', 160.00, TRUE, 4);
INSERT INTO MEDICO (nome, crm, telefone, email, valor_consulta_referencia, ativo, especialidadeid) VALUES ('Dr. Pedro Lima', 'CRM33445', '79985554433', 'pedro.l@medico.com', 200.00, FALSE, 5); -- Médico INATIVO

-- ===============================================
-- 6. Inserir Pacientes (5 registros)
-- Inclui paciente ativo e inativo.
-- ===============================================
INSERT INTO PACIENTE (nome, cpf, data_nascimento, telefone, email, ativo) VALUES ('Ana Paula', '11122233344', '1990-05-10', '79998887766', 'ana@paciente.com', TRUE);
INSERT INTO PACIENTE (nome, cpf, data_nascimento, telefone, email, ativo) VALUES ('Carlos Eduardo', '55566677788', '1985-11-20', '79991112233', 'carlos@paciente.com', TRUE);
INSERT INTO PACIENTE (nome, cpf, data_nascimento, telefone, email, ativo) VALUES ('Fernanda Gomes', '99988877766', '1992-03-25', '79997776655', 'fernanda@paciente.com', FALSE); -- Paciente INATIVO
INSERT INTO PACIENTE (nome, cpf, data_nascimento, telefone, email, ativo) VALUES ('Bruno Santos', '33344455566', '1988-07-15', '79996665544', 'bruno@paciente.com', TRUE);
INSERT INTO PACIENTE (nome, cpf, data_nascimento, telefone, email, ativo) VALUES ('Juliana Lima', '00011122233', '1991-01-01', '79995554433', 'juliana@paciente.com', TRUE);


-- ===============================================
-- 7. Inserir Consultas (10 registros)
-- Criado para testar todos os cenários.
-- ===============================================
-- Consultas REALIZADAS para o Relatório de Faturamento e Especialidades
INSERT INTO CONSULTA (data_hora_consulta, status, valor, observacoes, pacienteid, medicoid, forma_pagamentoid, convenioid, recepcionistaid)
VALUES ('2025-01-10 10:00:00', 'REALIZADA', 150.00, 'Consulta de rotina', 1, 1, 1, 0, 1); -- Dr. João (Cardiologia), Dinheiro
INSERT INTO CONSULTA (data_hora_consulta, status, valor, observacoes, pacienteid, medicoid, forma_pagamentoid, convenioid, recepcionistaid)
VALUES ('2025-01-15 11:00:00', 'REALIZADA', 180.00, 'Consulta de acompanhamento', 2, 2, 2, 0, 1); -- Dra. Maria (Dermatologia), Cartão
INSERT INTO CONSULTA (data_hora_consulta, status, valor, observacoes, pacienteid, medicoid, forma_pagamentoid, convenioid, recepcionistaid)
VALUES ('2025-01-20 09:00:00', 'REALIZADA', 75.00, 'Consulta via Unimed', 1, 1, 4, 1, 1); -- Dr. João (Cardiologia), Convênio Unimed (valor 50%)
INSERT INTO CONSULTA (data_hora_consulta, status, valor, observacoes, pacienteid, medicoid, forma_pagamentoid, convenioid, recepcionistaid)
VALUES ('2025-02-05 14:00:00', 'REALIZADA', 160.00, 'Revisão', 2, 4, 3, 0, 2); -- Dra. Ana (Oftalmologia), Pix
-- Consultas AGENDADAS para o Agendamento Inteligente, Histórico e Cancelamento
INSERT INTO CONSULTA (data_hora_consulta, status, valor, observacoes, pacienteid, medicoid, forma_pagamentoid, convenioid, recepcionistaid)
VALUES ('2025-08-05 09:00:00', 'AGENDADA', 150.00, 'Primeiro agendamento', 1, 1, 1, 0, 2); -- Dr. João (Cardiologia)
INSERT INTO CONSULTA (data_hora_consulta, status, valor, observacoes, pacienteid, medicoid, forma_pagamentoid, convenioid, recepcionistaid)
VALUES ('2025-08-05 10:00:00', 'AGENDADA', 90.00, 'Agendamento por convênio', 3, 3, 4, 2, 1); -- Dr. Carlos (Clínico Geral)
-- Consultas CANCELADAS para o Histórico
INSERT INTO CONSULTA (data_hora_consulta, status, valor, observacoes, pacienteid, medicoid, forma_pagamentoid, convenioid, recepcionistaid)
VALUES ('2025-08-10 10:00:00', 'CANCELADA', 150.00, 'Cancelada pelo paciente', 1, 1, 1, 0, 1);
-- Consultas para o Histórico do Paciente 'Ana Paula'
INSERT INTO CONSULTA (data_hora_consulta, status, valor, observacoes, pacienteid, medicoid, forma_pagamentoid, convenioid, recepcionistaid)
VALUES ('2024-12-20 15:30:00', 'REALIZADA', 120.00, 'Consulta de acompanhamento', 1, 3, 1, 0, 2);
INSERT INTO CONSULTA (data_hora_consulta, status, valor, observacoes, pacienteid, medicoid, forma_pagamentoid, convenioid, recepcionistaid)
VALUES ('2025-03-10 08:30:00', 'REALIZADA', 150.00, 'Retorno', 1, 1, 2, 0, 1);
INSERT INTO CONSULTA (data_hora_consulta, status, valor, observacoes, pacienteid, medicoid, forma_pagamentoid, convenioid, recepcionistaid)
VALUES ('2025-09-01 16:00:00', 'AGENDADA', 180.00, 'Nova consulta', 1, 2, 1, 0, 2);
