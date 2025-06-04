-- Usar o banco de dados SistemaHotel
USE SistemaHotel;

-- -----------------------------------------------------
-- Table Funcionarios
-- -----------------------------------------------------
INSERT INTO Funcionarios (Nome, CPF, Telefone, Cargo, Endereco, Email, Salario, DataAdmissao) VALUES
('Ana Silva', '11122233344', '(11) 98765-4321', 'Recepcionista', 'Rua das Flores, 123, São Paulo, SP', 'ana.silva@emailhotel.com', 2500.00, '2023-01-15'),
('Bruno Costa', '22233344455', '(21) 91234-5678', 'Gerente', 'Avenida Principal, 456, Rio de Janeiro, RJ', 'bruno.costa@emailhotel.com', 7500.00, '2020-05-10'),
('Carla Dias', '33344455566', '(31) 95678-1234', 'Camareira', 'Travessa das Palmeiras, 789, Belo Horizonte, MG', 'carla.dias@emailhotel.com', 1800.00, '2024-02-01'),
('Daniel Farias', '44455566677', '(41) 98888-7777', 'Cozinheiro', 'Alameda dos Sabores, 101, Curitiba, PR', 'daniel.farias@emailhotel.com', 3200.00, '2022-07-20'),
('Eduarda Lima', '55566677788', '(51) 97777-8888', 'Recepcionista', 'Rua da Estação, 202, Porto Alegre, RS', 'eduarda.lima@emailhotel.com', 2600.00, '2023-08-01');

-- -----------------------------------------------------
-- Table Hospedes
-- -----------------------------------------------------
INSERT INTO Hospedes (Nome, CPF, Telefone, Email, Endereco, DataNascimento) VALUES
('Fernanda Souza', '12345678901', '(11) 99999-1111', 'fernanda.souza@email.com', 'Rua Teste, 1, Cidade A', '1990-03-15'),
('Gustavo Pereira', '23456789012', '(22) 98888-2222', 'gustavo.pereira@email.com', 'Avenida Exemplo, 2, Cidade B', '1985-07-22'),
('Helena Almeida', '34567890123', '(33) 97777-3333', 'helena.almeida@email.com', 'Praça Modelo, 3, Cidade C', '1995-11-30'),
('Igor Santos', '45678901234', '(44) 96666-4444', 'igor.santos@email.com', 'Travessa Piloto, 4, Cidade D', '1980-01-05'),
('Julia Rocha', '56789012345', '(55) 95555-5555', 'julia.rocha@email.com', 'Rua dos Hóspedes, 5, Cidade E', '2000-09-10');

-- -----------------------------------------------------
-- Table Quartos
-- -----------------------------------------------------
INSERT INTO Quartos (NumeroQuarto, Tipo, PrecoDiaria, Status) VALUES
(101, 'SOLTEIRO', 150.00, 'DISPONIVEL'),
(102, 'CASAL', 250.00, 'DISPONIVEL'),
(201, 'FAMILIA', 350.00, 'OCUPADO'),
(202, 'LUXO', 500.00, 'MANUTENCAO'),
(301, 'CASAL', 260.00, 'DISPONIVEL');

-- -----------------------------------------------------
-- Table Reservas
-- (Assume HospedeID e QuartoID 1-5 existem das inserções anteriores)
-- (Datas e ValorTotal são exemplos)
-- -----------------------------------------------------
INSERT INTO Reservas (HospedeID, QuartoID, DataCheckIn, DataCheckOut, Status, ValorTotal) VALUES
(1, 1, '2025-06-10', '2025-06-12', 'CONFIRMADA', 300.00), -- Fernanda no Quarto 101
(2, 2, '2025-06-15', '2025-06-18', 'PENDENTE', 750.00),   -- Gustavo no Quarto 102
(3, 3, '2025-06-01', '2025-06-05', 'HOSPEDADO', 1400.00), -- Helena no Quarto 201 (já ocupado)
(4, 5, '2025-07-01', '2025-07-03', 'CONFIRMADA', 520.00),  -- Igor no Quarto 301
(5, 1, '2025-07-05', '2025-07-07', 'PENDENTE', 300.00);   -- Julia no Quarto 101 (futura)

-- -----------------------------------------------------
-- Table Pagamentos
-- (Assume ReservaID 1-5 existem das inserções anteriores)
-- -----------------------------------------------------
INSERT INTO Pagamentos (ReservaID, ValorPago, DataPagamento, MetodoPagamento, StatusPagamento) VALUES
(1, 300.00, '2025-06-09', 'CARTAO_CREDITO', 'PAGO'),
(2, 300.00, '2025-06-14', 'PIX', 'PENDENTE'), -- Pagamento parcial para reserva pendente
(3, 1400.00, '2025-06-01', 'DINHEIRO', 'PAGO'), -- Pagamento no check-in
(4, 520.00, '2025-06-20', 'CARTAO_DEBITO', 'PAGO'),
(5, 100.00, '2025-06-25', 'TRANSFERENCIA', 'PENDENTE'); -- Sinal para reserva futura

-- -----------------------------------------------------
-- Table Produtos
-- -----------------------------------------------------
INSERT INTO Produtos (Nome, Descricao, Preco, Estoque, Categoria) VALUES
('Água Mineral 500ml', 'Água mineral sem gás, garrafa PET.', 5.00, 100, 'FRIGOBAR'),
('Refrigerante Lata 350ml', 'Coca-Cola, Guaraná, etc.', 7.00, 80, 'FRIGOBAR'),
('Chocolate Barra 90g', 'Chocolate ao leite Hershey''s.', 10.00, 50, 'FRIGOBAR'),
('Sanduíche de Queijo e Presunto', 'Pão de forma, queijo prato e presunto.', 25.00, 20, 'RESTAURANTE'),
('Porção de Batata Frita', 'Batata palito frita com sal.', 30.00, 15, 'RESTAURANTE');

-- -----------------------------------------------------
-- Table Consumo (de Produtos)
-- (Assume ReservaID 1, 3, 4 e ProdutoID 1-5 existem)
-- -----------------------------------------------------
INSERT INTO Consumo (ReservaID, ProdutoID, Quantidade, Valor, DataConsumo) VALUES
(1, 1, 2, 5.00, '2025-06-10'),  -- Reserva 1 consumiu 2 Águas Minerais (Valor é unitário do produto no momento do consumo)
(3, 2, 1, 7.00, '2025-06-02'),  -- Reserva 3 consumiu 1 Refrigerante
(3, 3, 1, 10.00, '2025-06-03'), -- Reserva 3 consumiu 1 Chocolate
(4, 4, 1, 25.00, '2025-07-01'), -- Reserva 4 consumiu 1 Sanduíche
(1, 5, 1, 30.00, '2025-06-11'); -- Reserva 1 consumiu 1 Porção de Batata

-- -----------------------------------------------------
-- Table Servicos
-- -----------------------------------------------------
INSERT INTO Servicos (Nome, Descricao, Preco, Disponivel) VALUES
('Café da Manhã Completo', 'Buffet de café da manhã com pães, frutas, frios, bolos e bebidas.', 45.00, TRUE),
('Lavanderia Express', 'Lavagem e secagem de até 5 peças de roupa em 24h.', 70.00, TRUE),
('Uso da Piscina (Day Use)', 'Acesso à piscina para não hóspedes.', 100.00, TRUE),
('Massagem Relaxante (1h)', 'Sessão de massagem de uma hora com óleos essenciais.', 180.00, TRUE),
('Aluguel de Toalha Extra Piscina', 'Toalha adicional para uso na piscina.', 25.00, TRUE);

-- -----------------------------------------------------
-- Table ConsumoServicos
-- (Assume ReservaID 1, 3, 4 e ServicoID 1-5 existem)
-- -----------------------------------------------------
INSERT INTO ConsumoServicos (ReservaID, ServicoID, Quantidade, DataConsumo) VALUES
(1, 1, 2, '2025-06-11'), -- Reserva 1, 2x Café da Manhã
(3, 2, 1, '2025-06-03'), -- Reserva 3, 1x Lavanderia Express
(4, 4, 1, '2025-07-02'), -- Reserva 4, 1x Massagem Relaxante
(3, 5, 2, '2025-06-04'), -- Reserva 3, 2x Aluguel de Toalha
(1, 1, 1, '2025-06-12'); -- Reserva 1, 1x Café da Manhã

-- -----------------------------------------------------
-- Table Manutencao
-- (Assume QuartoID 1-5 e FuncionarioID 1-5 existem)
-- -----------------------------------------------------
INSERT INTO Manutencao (QuartoID, DataInicio, DataFim, Descricao, Status, FuncionarioID) VALUES
(4, '2025-06-01', '2025-06-03', 'Reparo no ar condicionado.', 'CONCLUIDA', 1), -- Quarto 202 (Luxo)
(2, '2025-06-05', NULL, 'Vazamento na pia do banheiro.', 'EM_ANDAMENTO', 3), -- Quarto 102 (Casal)
(5, '2025-06-10', NULL, 'Pintura geral do quarto.', 'PENDENTE', NULL), -- Quarto 301 (Casal)
(1, '2025-05-20', '2025-05-21', 'Troca de lâmpada queimada.', 'CONCLUIDA', 1), -- Quarto 101 (Solteiro)
(4, '2025-06-15', NULL, 'Verificação de rotina pós-reparo do ar.', 'PENDENTE', 1); -- Quarto 202 (Luxo)
