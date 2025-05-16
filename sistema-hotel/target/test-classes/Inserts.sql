-- Inserções na tabela Funcionarios
INSERT INTO Funcionarios (Nome, CPF, Telefone, Cargo, Salario, DataAdmissao, DataCriacao, DataAtualizacao)
VALUES 
('João Silva', '459.456.789-00', '11987654321', 'Recepcionista', 2500.00, '2020-01-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Maria Oliveira', '987.654.321-00', '11912345678', 'Gerente', 5000.00, '2019-05-15', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Inserções na tabela Hospedes
INSERT INTO Hospedes (Nome, CPF, Telefone, Email, DataNascimento, DataCriacao, DataAtualizacao)
VALUES 
('Carlos Souza', '111.222.333-44', '11999998888', 'carlos.souza@email.com', '1985-07-20', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Ana Lima', '555.666.777-88', '11988887777', 'ana.lima@email.com', '1990-11-15', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Inserções na tabela Quartos
INSERT INTO Quartos (NumeroQuarto, Tipo, PrecoDiaria, Status, DataCriacao, DataAtualizacao)
VALUES 
(101, 'SOLTEIRO', 150.00, 'DISPONIVEL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(102, 'CASAL', 250.00, 'DISPONIVEL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(201, 'LUXO', 500.00, 'MANUTENCAO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Inserções na tabela Reservas
INSERT INTO Reservas (HospedeID, QuartoID, DataCheckIn, DataCheckOut, Status, ValorTotal, DataCriacao, DataAtualizacao)
VALUES 
(1, 1, '2023-10-01', '2023-10-05', 'CONFIRMADA', 800.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, '2023-10-10', '2023-10-15', 'PENDENTE', 1250.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- Inserções na tabela Pagamentos
INSERT INTO Pagamentos (ReservaID, ValorPago, MetodoPagamento, DataPagamento, DataCriacao, DataAtualizacao)
VALUES 
(1, 750.00, 'CARTAO_CREDITO', '2023-10-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1250.00, 'PIX', '2023-10-10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Inserções na tabela Produtos
INSERT INTO Produtos (NomeProduto, Preco, Estoque, Categoria, DataCriacao, DataAtualizacao)
VALUES 
('Água Mineral', 5.00, 100, 'FRIGOBAR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Refrigerante', 8.00, 50, 'FRIGOBAR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Pizza', 35.00, 20, 'RESTAURANTE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Inserções na tabela Consumo
INSERT INTO Consumo (ReservaID, ProdutoID, Quantidade, Valor, DataConsumo, DataCriacao, DataAtualizacao)
VALUES 
(1, 2, 3, 150.00, CURRENT_DATE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Inserções na tabela Servicos
INSERT INTO Servicos (NomeServico, Preco, DataCriacao, DataAtualizacao)
VALUES 
('Lavanderia', 20.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Massagem', 100.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Inserções na tabela ConsumoServicos
INSERT INTO ConsumoServicos (ReservaID, ServicoID, Quantidade, DataConsumo, DataCriacao, DataAtualizacao)
VALUES 
(1, 1, 1, CURRENT_DATE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- 1 serviço de lavanderia
(2, 2, 2, CURRENT_DATE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);  -- 2 serviços de massagem

-- Inserções na tabela Manutencao
INSERT INTO Manutencao (QuartoID, DataInicio, DataFim, Descricao, Status, FuncionarioID, DataCriacao, DataAtualizacao)
VALUES 
(3, '2023-09-25', NULL, 'Manutenção preventiva no ar-condicionado', 'EM_ANDAMENTO', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
