-- Inserções na tabela Funcionarios
INSERT INTO Funcionarios (Nome, CPF, Telefone, Cargo, Salario, DataAdmissao)
VALUES 
('João Silva', '123.456.789-00', '11987654321', 'Recepcionista', 2500.00, '2020-01-01'),
('Maria Oliveira', '987.654.321-00', '11912345678', 'Gerente', 5000.00, '2019-05-15');

-- Inserções na tabela Hospedes
INSERT INTO Hospedes (Nome, CPF, Telefone, Email, DataNascimento)
VALUES 
('Carlos Souza', '111.222.333-44', '11999998888', 'carlos.souza@email.com', '1985-07-20'),
('Ana Lima', '555.666.777-88', '11988887777', 'ana.lima@email.com', '1990-11-15');

-- Inserções na tabela Quartos
INSERT INTO Quartos (NumeroQuarto, Tipo, PrecoDiaria, Status)
VALUES 
(101, 'SOLTEIRO', 150.00, 'DISPONIVEL'),
(102, 'CASAL', 250.00, 'DISPONIVEL'),
(201, 'LUXO', 500.00, 'MANUTENCAO');

-- Inserções na tabela Reservas
INSERT INTO Reservas (HospedeID, QuartoID, DataCheckIn, DataCheckOut, Status)
VALUES 
(1, 1, '2023-10-01', '2023-10-05', 'CONFIRMADA'),
(2, 2, '2023-10-10', '2023-10-15', 'PENDENTE');

-- Inserções na tabela Pagamentos
INSERT INTO Pagamentos (ReservaID, ValorPago, MetodoPagamento)
VALUES 
(1, 750.00, 'CARTAO_CREDITO'),
(2, 1250.00, 'PIX');

-- Inserções na tabela Produtos
INSERT INTO Produtos (NomeProduto, Preco, Estoque, Categoria)
VALUES 
('Água Mineral', 5.00, 100, 'FRIGOBAR'),
('Refrigerante', 8.00, 50, 'FRIGOBAR'),
('Pizza', 35.00, 20, 'RESTAURANTE');

-- Inserções na tabela Consumo
INSERT INTO Consumo (ReservaID, ProdutoID, Quantidade, Valor)
VALUES 
(1, 1, 2, 10.00),  -- 2 águas minerais
(1, 3, 1, 35.00);  -- 1 pizza

-- Inserções na tabela Servicos
INSERT INTO Servicos (NomeServico, Preco)
VALUES 
('Lavanderia', 20.00),
('Massagem', 100.00);

-- Inserções na tabela ConsumoServicos
INSERT INTO ConsumoServicos (ReservaID, ServicoID, Quantidade)
VALUES 
(1, 1, 1),  -- 1 serviço de lavanderia
(2, 2, 2);  -- 2 serviços de massagem

-- Inserções na tabela Manutencao
INSERT INTO Manutencao (QuartoID, DataInicio, Descricao, Status, FuncionarioID)
VALUES 
(3, '2023-09-25', 'Manutenção preventiva no ar-condicionado', 'EM_ANDAMENTO', 1);