CREATE DATABASE IF NOT EXISTS sistema_hotel;
USE sistema_hotel;

-- Etapa 1: Desativar verificação de chaves estrangeiras
SET FOREIGN_KEY_CHECKS=0;

-- Etapa 2: Remover tabelas existentes
DROP TABLE IF EXISTS `consumo`;
DROP TABLE IF EXISTS `consumoservicos`;
DROP TABLE IF EXISTS `pagamentos`;
DROP TABLE IF EXISTS `manutencao`;
DROP TABLE IF EXISTS `reservas`;
DROP TABLE IF EXISTS `hospedes`;
DROP TABLE IF EXISTS `funcionarios`;
DROP TABLE IF EXISTS `quartos`;
DROP TABLE IF EXISTS `produtos`;
DROP TABLE IF EXISTS `servicos`;

-- Etapa 3: Criar a estrutura das tabelas (Estrutura original mantida)
CREATE TABLE `produtos` (
  `ProdutoID` int NOT NULL AUTO_INCREMENT, `Nome` varchar(100) NOT NULL, `Descricao` varchar(255) DEFAULT NULL, `Preco` decimal(10,2) NOT NULL, `Estoque` int NOT NULL, `Categoria` enum('FRIGOBAR','RESTAURANTE','BEBIDA','OUTROS') NOT NULL, `DataCriacao` datetime DEFAULT CURRENT_TIMESTAMP, `DataAtualizacao` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY (`ProdutoID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `servicos` (
  `ServicoID` int NOT NULL AUTO_INCREMENT, `Nome` varchar(100) NOT NULL, `Descricao` varchar(255) DEFAULT NULL, `Preco` decimal(10,2) NOT NULL, `Disponivel` tinyint(1) DEFAULT '1', `DataCriacao` datetime DEFAULT CURRENT_TIMESTAMP, `DataAtualizacao` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY (`ServicoID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `quartos` (
  `QuartoID` int NOT NULL AUTO_INCREMENT, `NumeroQuarto` int NOT NULL, `Tipo` enum('SOLTEIRO','CASAL','FAMILIA','LUXO') NOT NULL, `PrecoDiaria` decimal(10,2) NOT NULL, `Status` enum('DISPONIVEL','OCUPADO','MANUTENCAO') DEFAULT 'DISPONIVEL', `DataCriacao` datetime DEFAULT CURRENT_TIMESTAMP, `DataAtualizacao` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY (`QuartoID`), UNIQUE KEY `NumeroQuarto` (`NumeroQuarto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `funcionarios` (
  `FuncionarioID` int NOT NULL AUTO_INCREMENT, `Nome` varchar(100) NOT NULL, `CPF` varchar(11) NOT NULL, `Telefone` varchar(15) DEFAULT NULL, `Cargo` varchar(50) DEFAULT NULL, `Endereco` varchar(200) DEFAULT NULL, `Email` varchar(100) DEFAULT NULL, `Salario` decimal(10,2) DEFAULT NULL, `DataAdmissao` date DEFAULT (curdate()), `DataCriacao` datetime DEFAULT CURRENT_TIMESTAMP, `DataAtualizacao` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY (`FuncionarioID`), UNIQUE KEY `CPF` (`CPF`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `hospedes` (
  `HospedeID` int NOT NULL AUTO_INCREMENT, `Nome` varchar(100) NOT NULL, `CPF` varchar(11) NOT NULL, `Telefone` varchar(20) DEFAULT NULL, `Email` varchar(100) DEFAULT NULL, `Endereco` varchar(200) DEFAULT NULL, `DataNascimento` date DEFAULT NULL, `DataCriacao` datetime DEFAULT CURRENT_TIMESTAMP, `DataAtualizacao` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY (`HospedeID`), UNIQUE KEY `CPF` (`CPF`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `reservas` (
  `ReservaID` int NOT NULL AUTO_INCREMENT, `HospedeID` int NOT NULL, `QuartoID` int NOT NULL, `DataCheckIn` date NOT NULL, `DataCheckOut` date NOT NULL, `Status` enum('CONFIRMADA','CANCELADA','CONCLUIDA','PENDENTE','HOSPEDADO') DEFAULT 'PENDENTE', `ValorTotal` decimal(10,2) NOT NULL, `DataCriacao` datetime DEFAULT CURRENT_TIMESTAMP, `DataAtualizacao` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY (`ReservaID`), KEY `HospedeID` (`HospedeID`), KEY `QuartoID` (`QuartoID`), CONSTRAINT `reservas_ibfk_1` FOREIGN KEY (`HospedeID`) REFERENCES `hospedes` (`HospedeID`), CONSTRAINT `reservas_ibfk_2` FOREIGN KEY (`QuartoID`) REFERENCES `quartos` (`QuartoID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `manutencao` (
  `ManutencaoID` int NOT NULL AUTO_INCREMENT, `QuartoID` int NOT NULL, `DataInicio` date NOT NULL, `DataFim` date DEFAULT NULL, `Descricao` text, `Status` enum('PENDENTE','EM_ANDAMENTO','CONCLUIDA') DEFAULT 'PENDENTE', `FuncionarioID` int DEFAULT NULL, `DataCriacao` datetime DEFAULT CURRENT_TIMESTAMP, `DataAtualizacao` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY (`ManutencaoID`), KEY `QuartoID` (`QuartoID`), KEY `FuncionarioID` (`FuncionarioID`), CONSTRAINT `manutencao_ibfk_1` FOREIGN KEY (`QuartoID`) REFERENCES `quartos` (`QuartoID`), CONSTRAINT `manutencao_ibfk_2` FOREIGN KEY (`FuncionarioID`) REFERENCES `funcionarios` (`FuncionarioID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `pagamentos` (
  `PagamentoID` int NOT NULL AUTO_INCREMENT, `ReservaID` int NOT NULL, `ValorPago` decimal(10,2) NOT NULL, `DataPagamento` date DEFAULT (curdate()), `MetodoPagamento` enum('DINHEIRO','CARTAO_CREDITO','CARTAO_DEBITO','PIX','TRANSFERENCIA') DEFAULT 'DINHEIRO', `StatusPagamento` enum('PENDENTE','PAGO','CANCELADO') DEFAULT 'PENDENTE', `DataCriacao` datetime DEFAULT CURRENT_TIMESTAMP, `DataAtualizacao` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY (`PagamentoID`), KEY `ReservaID` (`ReservaID`), CONSTRAINT `pagamentos_ibfk_1` FOREIGN KEY (`ReservaID`) REFERENCES `reservas` (`ReservaID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `consumoservicos` (
  `ConsumoServicoID` int NOT NULL AUTO_INCREMENT, `ReservaID` int NOT NULL, `ServicoID` int NOT NULL, `Quantidade` int NOT NULL, `DataConsumo` date DEFAULT (curdate()), `DataCriacao` datetime DEFAULT CURRENT_TIMESTAMP, `DataAtualizacao` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY (`ConsumoServicoID`), KEY `ReservaID` (`ReservaID`), KEY `ServicoID` (`ServicoID`), CONSTRAINT `consumoservicos_ibfk_1` FOREIGN KEY (`ReservaID`) REFERENCES `reservas` (`ReservaID`), CONSTRAINT `consumoservicos_ibfk_2` FOREIGN KEY (`ServicoID`) REFERENCES `servicos` (`ServicoID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `consumo` (
  `ConsumoID` int NOT NULL AUTO_INCREMENT, `ReservaID` int NOT NULL, `ProdutoID` int NOT NULL, `Quantidade` int NOT NULL, `Valor` decimal(10,2) NOT NULL, `DataConsumo` date NOT NULL, `DataCriacao` datetime DEFAULT CURRENT_TIMESTAMP, `DataAtualizacao` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY (`ConsumoID`), KEY `ReservaID` (`ReservaID`), KEY `ProdutoID` (`ProdutoID`), CONSTRAINT `consumo_ibfk_1` FOREIGN KEY (`ReservaID`) REFERENCES `reservas` (`ReservaID`), CONSTRAINT `consumo_ibfk_2` FOREIGN KEY (`ProdutoID`) REFERENCES `produtos` (`ProdutoID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Etapa 4: Reativar verificação de chaves estrangeiras
SET FOREIGN_KEY_CHECKS=1;

-- Etapa 5: Inserir dados de amostra com IDs dos quartos corrigidos
-- As inserções em produtos, servicos, hospedes e funcionarios permanecem as mesmas.
INSERT INTO `produtos` (`ProdutoID`, `Nome`, `Descricao`, `Preco`, `Estoque`, `Categoria`, `DataCriacao`, `DataAtualizacao`) VALUES
(1, 'Água Mineral 500ml', 'Água mineral sem gás, garrafa PET.', 5.00, 100, 'FRIGOBAR', '2025-06-02 10:40:00', '2025-06-02 10:40:00'),
(2, 'Refrigerante Lata 350ml', 'Coca-Cola, Guaraná, etc.', 7.00, 80, 'FRIGOBAR', '2025-06-02 10:40:00', '2025-06-02 10:40:00'),
(3, 'Chocolate Barra 90g', 'Chocolate ao leite Hershey\'s.', 10.00, 50, 'FRIGOBAR', '2025-06-02 10:40:00', '2025-06-02 10:40:00'),
(4, 'Sanduíche de Queijo e Presunto', 'Pão de forma, queijo prato e presunto.', 25.00, 20, 'RESTAURANTE', '2025-06-02 10:40:00', '2025-06-02 10:40:00'),
(5, 'Porção de Batata Frita', 'Batata palito frita com sal.', 30.00, 15, 'RESTAURANTE', '2025-06-02 10:40:00', '2025-06-02 10:40:00'),
(6, 'Suco Natural 300ml', 'Suco de laranja ou uva, garrafa de vidro.', 12.00, 50, 'BEBIDA', '2025-06-05 08:30:00', '2025-06-05 08:30:00'),
(7, 'Cerveja Artesanal 500ml', 'Cerveja IPA, garrafa de vidro.', 18.00, 30, 'BEBIDA', '2025-06-05 08:30:00', '2025-06-05 08:30:00'),
(8, 'Salada Caesar', 'Salada com frango grelhado, croutons e molho Caesar.', 35.00, 15, 'RESTAURANTE', '2025-06-05 08:30:00', '2025-06-05 08:30:00'),
(9, 'Barrinha de Cereal', 'Barrinha de cereal integral com frutas secas.', 8.00, 60, 'FRIGOBAR', '2025-06-05 08:30:00', '2025-06-05 08:30:00'),
(10, 'Água com Gás 500ml', 'Água mineral com gás, garrafa PET.', 6.00, 70, 'FRIGOBAR', '2025-06-05 08:30:00', '2025-06-05 08:30:00');

INSERT INTO `servicos` (`ServicoID`, `Nome`, `Descricao`, `Preco`, `Disponivel`, `DataCriacao`, `DataAtualizacao`) VALUES
(1, 'Café da Manhã Completo', 'Buffet de café da manhã.', 45.00, 1, '2025-06-02 10:40:00', '2025-06-02 10:40:00'),
(2, 'Lavanderia Express', 'Lavagem e secagem de até 5 peças.', 70.00, 1, '2025-06-02 10:40:00', '2025-06-02 10:40:00'),
(3, 'Uso da Piscina (Day Use)', 'Acesso à piscina para não hóspedes.', 100.00, 1, '2025-06-02 10:40:00', '2025-06-02 10:40:00'),
(4, 'Massagem Relaxante (1h)', 'Sessão de massagem de uma hora.', 180.00, 1, '2025-06-02 10:40:00', '2025-06-02 10:40:00'),
(5, 'Aluguel de Toalha Extra', 'Toalha adicional para uso na piscina.', 25.00, 1, '2025-06-02 10:40:00', '2025-06-02 10:40:00'),
(6, 'Serviço de Quarto (24h)', 'Limpeza e organização do quarto.', 50.00, 1, '2025-06-05 08:45:00', '2025-06-05 08:45:00'),
(7, 'Translado Aeroporto-Hotel', 'Transporte privativo.', 120.00, 1, '2025-06-05 08:45:00', '2025-06-05 08:45:00'),
(8, 'Aluguel de Notebook', 'Notebook para trabalho.', 80.00, 1, '2025-06-05 08:45:00', '2025-06-05 08:45:00'),
(9, 'Spa Completo (2h)', 'Pacote completo no spa.', 350.00, 1, '2025-06-05 08:45:00', '2025-06-05 08:45:00'),
(10, 'Guia Turístico (4h)', 'Acompanhamento para passeios locais.', 200.00, 1, '2025-06-05 08:45:00', '2025-06-05 08:45:00');

INSERT INTO `hospedes` (`HospedeID`, `Nome`, `CPF`, `Telefone`, `Email`, `Endereco`, `DataNascimento`, `DataCriacao`, `DataAtualizacao`) VALUES
(1, 'Fernanda Souza', '28518933036', '(11) 99999-1111', 'fernanda.souza@email.com', 'Rua Teste, 1, Cidade A', '1990-03-15', '2025-06-02 10:00:00', '2025-06-02 10:00:00'),
(2, 'Gustavo Pereira', '72965431051', '(22) 98888-2222', 'gustavo.pereira@email.com', 'Avenida Exemplo, 2, Cidade B', '1985-07-22', '2025-06-02 10:00:00', '2025-06-02 10:00:00'),
(3, 'Helena Almeida', '81734567089', '(33) 97777-3333', 'helena.almeida@email.com', 'Praça Modelo, 3, Cidade C', '1995-11-30', '2025-06-02 10:00:00', '2025-06-02 10:00:00'),
(4, 'Igor Santos', '50684291048', '(44) 96666-4444', 'igor.santos@email.com', 'Travessa Piloto, 4, Cidade D', '1980-01-05', '2025-06-02 10:00:00', '2025-06-02 10:00:00'),
(5, 'Julia Rocha', '92847561063', '(55) 95555-5555', 'julia.rocha@email.com', 'Rua dos Hóspedes, 5, Cidade E', '2000-09-10', '2025-06-02 10:00:00', '2025-06-02 10:00:00'),
(6, 'Bernardo Ribeiro', '04132868001', '(55) 99628-3243', 'bernardo.ribeiro@outlook.com.br', 'Rua Augustin Peniza 433', '2003-07-22', '2025-06-02 11:35:00', '2025-06-02 11:35:00'),
(7, 'Lucas Mendes', '34967812003', '(11) 97777-1111', 'lucas.mendes@email.com', 'Rua das Orquídeas, 67', '1992-04-18', '2025-06-05 08:12:00', '2025-06-05 08:12:00'),
(8, 'Mariana Costa', '45678912061', '(21) 96666-2222', 'mariana.costa@email.com', 'Avenida das Palmeiras, 89', '1988-12-25', '2025-06-05 08:12:00', '2025-06-05 08:12:00'),
(9, 'Otávio Rocha', '67891234080', '(31) 95555-3333', 'otavio.rocha@email.com', 'Praça Central, 10', '1998-06-30', '2025-06-05 08:12:00', '2025-06-05 08:12:00'),
(10, 'Patrícia Dias', '01234567087', '(41) 94444-4444', 'patricia.dias@email.com', 'Travessa dos Pinheiros, 23', '1975-10-15', '2025-06-05 08:12:00', '2025-06-05 08:12:00'),
(11, 'Ricardo Alves', '23456789091', '(51) 93333-5555', 'ricardo.alves@email.com', 'Alameda dos Ipês, 45', '2002-03-08', '2025-06-05 08:12:00', '2025-06-05 08:12:00'),
(12, 'Carlos Eduardo', '31850346437', '(11) 91234-5678', 'carlos.edu@email.com', 'Rua Nova, 12, Cidade F', '1987-05-10', '2025-06-12 10:00:00', '2025-06-12 10:00:00'),
(13, 'Paula Martins', '42075726515', '(21) 92345-6789', 'paula.martins@email.com', 'Av. Central, 45, Cidade G', '1992-08-22', '2025-06-12 10:00:00', '2025-06-12 10:00:00'),
(14, 'Rafael Souza', '83776284650', '(31) 93456-7890', 'rafael.souza@email.com', 'Rua das Palmeiras, 78, Cidade H', '1985-12-01', '2025-06-12 10:00:00', '2025-06-12 10:00:00'),
(15, 'Juliana Alves', '39252473432', '(41) 94567-8901', 'juliana.alves@email.com', 'Travessa das Flores, 23, Cidade I', '1990-03-15', '2025-06-12 10:00:00', '2025-06-12 10:00:00'),
(16, 'Marcos Lima', '56281553497', '(51) 95678-9012', 'marcos.lima@email.com', 'Alameda dos Ipês, 56, Cidade J', '1998-11-20', '2025-06-12 10:00:00', '2025-06-12 10:00:00');

INSERT INTO `funcionarios` (`FuncionarioID`, `Nome`, `CPF`, `Telefone`, `Cargo`, `Endereco`, `Email`, `Salario`, `DataAdmissao`, `DataCriacao`, `DataAtualizacao`) VALUES
(1, 'Ana Silva', '24066273815', '(11) 98765-4321', 'Recepcionista', 'Rua das Flores, 123', 'ana.silva@emailhotel.com', 2500.00, '2023-01-15', '2025-06-02 10:40:00', '2025-06-04 09:28:00'),
(2, 'Bruno Costa', '26115962048', '(21) 91234-5678', 'Gerente', 'Avenida Principal, 456', 'bruno.costa@emailhotel.com', 7500.00, '2020-05-10', '2025-06-02 10:40:00', '2025-06-04 09:30:00'),
(3, 'Carla Dias', '33342273674', '(31) 95678-1234', 'Camareira', 'Travessa das Palmeiras, 789', 'carla.dias@emailhotel.com', 1800.00, '2024-02-01', '2025-06-02 10:40:00', '2025-06-04 09:28:00'),
(4, 'Daniel Farias', '51499368712', '(41) 98888-7777', 'Cozinheiro', 'Alameda dos Sabores, 101', 'daniel.farias@emailhotel.com', 3200.00, '2022-07-20', '2025-06-02 10:40:00', '2025-06-04 09:28:00'),
(5, 'Eduarda Lima', '20373183593', '(51) 97777-8888', 'Recepcionista', 'Rua da Estação, 202', 'eduarda.lima@emailhotel.com', 2600.00, '2023-08-01', '2025-06-02 10:40:00', '2025-06-04 09:28:00'),
(6, 'Bernardo Ribeiro', '39472852050', '55996283243', 'Gerente', 'Rua Augustin Peniza 433', 'bernardo.ribeiro@outlook.com.br', 10500.00, '2025-06-04', '2025-06-04 09:35:00', '2025-06-04 09:35:00'),
(7, 'Fernando Gomes', '47393851474', '(11) 95555-6666', 'Camareiro', 'Rua das Acácias, 55', 'fernando.gomes@emailhotel.com', 1900.00, '2025-01-10', '2025-06-05 08:10:00', '2025-06-05 08:10:00'),
(8, 'Gabriela Martins', '30979762561', '(21) 94444-7777', 'Recepcionista', 'Avenida Beira-Mar, 78', 'gabriela.martins@emailhotel.com', 2700.00, '2024-03-15', '2025-06-05 08:10:00', '2025-06-05 08:10:00'),
(9, 'Hugo Nunes', '99452297611', '(31) 93333-8888', 'Segurança', 'Rua Diamante, 90', 'hugo.nunes@emailhotel.com', 2200.00, '2023-11-20', '2025-06-05 08:10:00', '2025-06-05 08:10:00'),
(10, 'Isabela Castro', '50135608880', '(41) 92222-9999', 'Cozinheira', 'Alameda das Flores, 12', 'isabela.castro@emailhotel.com', 3100.00, '2022-09-05', '2025-06-05 08:10:00', '2025-06-05 08:10:00'),
(11, 'João Pedro Lima', '58851550700', '(51) 91111-0000', 'Garçom', 'Rua dos Coqueiros, 34', 'joao.lima@emailhotel.com', 2000.00, '2025-02-28', '2025-06-05 08:10:00', '2025-06-05 08:10:00');

-- CORREÇÃO: IDs dos quartos reordenados para seguir a ordem do NumeroQuarto.
INSERT INTO `quartos` (`QuartoID`, `NumeroQuarto`, `Tipo`, `PrecoDiaria`, `Status`, `DataCriacao`, `DataAtualizacao`) VALUES
(1, 101, 'SOLTEIRO', 150.00, 'OCUPADO', '2025-06-02 10:00:00', '2025-06-12 09:16:00'),
(2, 102, 'CASAL', 250.00, 'OCUPADO', '2025-06-02 10:00:00', '2025-06-12 10:40:00'),
(3, 103, 'SOLTEIRO', 160.00, 'DISPONIVEL', '2025-06-05 08:35:00', '2025-06-05 08:35:00'),
(4, 104, 'SOLTEIRO', 155.00, 'OCUPADO', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(5, 105, 'CASAL', 255.00, 'OCUPADO', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(6, 106, 'FAMILIA', 355.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(7, 107, 'LUXO', 505.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(8, 108, 'SOLTEIRO', 160.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(9, 109, 'CASAL', 265.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(10, 110, 'FAMILIA', 360.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(11, 201, 'FAMILIA', 350.00, 'MANUTENCAO', '2025-06-02 10:00:00', '2025-06-06 10:00:00'),
(12, 202, 'LUXO', 500.00, 'DISPONIVEL', '2025-06-02 10:00:00', '2025-06-09 10:00:00'),
(13, 203, 'FAMILIA', 370.00, 'DISPONIVEL', '2025-06-05 08:35:00', '2025-06-05 08:35:00'),
(14, 204, 'LUXO', 510.00, 'OCUPADO', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(15, 205, 'SOLTEIRO', 165.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(16, 206, 'CASAL', 270.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(17, 207, 'FAMILIA', 365.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(18, 208, 'LUXO', 515.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(19, 209, 'SOLTEIRO', 170.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(20, 210, 'CASAL', 275.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(21, 301, 'CASAL', 260.00, 'OCUPADO', '2025-06-02 10:00:00', '2025-06-07 11:08:00'),
(22, 302, 'LUXO', 520.00, 'DISPONIVEL', '2025-06-05 08:35:00', '2025-06-05 08:35:00'),
(23, 303, 'CASAL', 270.00, 'DISPONIVEL', '2025-06-05 08:35:00', '2025-06-05 08:35:00'),
(24, 304, 'FAMILIA', 370.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(25, 305, 'LUXO', 520.00, 'OCUPADO', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(26, 306, 'SOLTEIRO', 175.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(27, 307, 'CASAL', 280.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(28, 308, 'FAMILIA', 375.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(29, 309, 'LUXO', 525.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(30, 310, 'SOLTEIRO', 180.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(31, 401, 'LUXO', 550.00, 'DISPONIVEL', '2025-06-05 08:35:00', '2025-06-05 08:35:00'),
(32, 402, 'CASAL', 285.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(33, 403, 'FAMILIA', 380.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(34, 404, 'LUXO', 530.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(35, 405, 'SOLTEIRO', 185.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(36, 406, 'CASAL', 290.00, 'OCUPADO', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(37, 407, 'FAMILIA', 385.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(38, 408, 'LUXO', 535.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(39, 409, 'SOLTEIRO', 190.00, 'DISPONIVEL', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
(40, 410, 'CASAL', 295.00, 'OCUPADO', '2025-06-05 09:00:00', '2025-06-05 09:00:00');

-- CORREÇÃO: QuartoID na tabela de reservas atualizado para corresponder aos novos IDs da tabela de quartos.
INSERT INTO `reservas` (`ReservaID`, `HospedeID`, `QuartoID`, `DataCheckIn`, `DataCheckOut`, `Status`, `ValorTotal`, `DataCriacao`, `DataAtualizacao`) VALUES
(1, 1, 1, '2025-06-10', '2025-06-12', 'HOSPEDADO', 300.00, '2025-06-02 11:07:00', '2025-06-12 09:15:00'), -- Quarto 101 -> ID 1 (sem mudança)
(2, 2, 2, '2025-06-08', '2025-06-11', 'CONCLUIDA', 750.00, '2025-06-02 11:07:00', '2025-06-11 11:07:00'), -- Quarto 102 -> ID 2 (sem mudança)
(3, 3, 11, '2025-06-01', '2025-06-05', 'CONCLUIDA', 1400.00, '2025-06-01 11:07:00', '2025-06-05 14:00:00'), -- Quarto 201 -> ID 3 mudou para 11
(4, 4, 21, '2025-06-01', '2025-06-03', 'CONCLUIDA', 520.00, '2025-06-01 11:07:00', '2025-06-03 11:07:00'), -- Quarto 301 -> ID 5 mudou para 21
(5, 5, 1, '2025-06-05', '2025-06-07', 'CONCLUIDA', 300.00, '2025-06-05 11:07:00', '2025-06-07 11:07:00'), -- Quarto 101 -> ID 1 (sem mudança)
(6, 6, 2, '2025-06-15', '2025-06-18', 'CONFIRMADA', 750.00, '2025-06-12 10:00:00', '2025-06-12 10:00:00'), -- Quarto 102 -> ID 2 (sem mudança)
(7, 7, 3, '2025-06-15', '2025-06-17', 'CONFIRMADA', 320.00, '2025-06-12 10:00:00', '2025-06-12 10:00:00'), -- Quarto 103 -> ID 6 mudou para 3
(8, 8, 13, '2025-06-20', '2025-06-25', 'PENDENTE', 1850.00, '2025-06-12 10:00:00', '2025-06-12 10:00:00'), -- Quarto 203 -> ID 7 mudou para 13
(9, 9, 22, '2025-07-01', '2025-07-05', 'CONFIRMADA', 2080.00, '2025-06-12 10:00:00', '2025-06-12 10:00:00'), -- Quarto 302 -> ID 8 mudou para 22
(10, 10, 23, '2025-07-10', '2025-07-12', 'PENDENTE', 540.00, '2025-06-12 10:00:00', '2025-06-12 10:00:00'), -- Quarto 303 -> ID 9 mudou para 23
(11, 12, 4, '2025-06-12', '2025-06-15', 'HOSPEDADO', 465.00, '2025-06-12 10:00:00', '2025-06-12 10:00:00'), -- Quarto 104 -> ID 11 mudou para 4
(12, 13, 5, '2025-06-12', '2025-06-16', 'HOSPEDADO', 1020.00, '2025-06-12 10:00:00', '2025-06-12 10:00:00'), -- Quarto 105 -> ID 12 mudou para 5
(13, 14, 14, '2025-06-11', '2025-06-14', 'HOSPEDADO', 1530.00, '2025-06-11 10:00:00', '2025-06-11 10:00:00'), -- Quarto 204 -> ID 18 mudou para 14
(14, 15, 25, '2025-06-10', '2025-06-13', 'HOSPEDADO', 1560.00, '2025-06-10 10:00:00', '2025-06-10 10:00:00'), -- Quarto 305 -> ID 26 mudou para 25
(15, 16, 36, '2025-06-12', '2025-06-17', 'HOSPEDADO', 1450.00, '2025-06-12 10:00:00', '2025-06-12 10:00:00'), -- Quarto 406 -> ID 36 (sem mudança)
(16, 12, 40, '2025-06-13', '2025-06-16', 'HOSPEDADO', 885.00, '2025-06-13 10:00:00', '2025-06-13 10:00:00'); -- Quarto 410 -> ID 40 (sem mudança)

-- CORREÇÃO: QuartoID na tabela de pagamentos não precisa de alteração, pois a relação é com ReservaID.
INSERT INTO `pagamentos` (`PagamentoID`, `ReservaID`, `ValorPago`, `DataPagamento`, `MetodoPagamento`, `StatusPagamento`, `DataCriacao`, `DataAtualizacao`) VALUES
(1, 1, 300.00, '2025-06-10', 'CARTAO_CREDITO', 'PAGO', '2025-06-10 10:40:00', '2025-06-10 10:40:00'),
(2, 2, 750.00, '2025-06-08', 'PIX', 'PAGO', '2025-06-08 10:40:00', '2025-06-08 10:40:00'),
(3, 3, 1400.00, '2025-06-01', 'DINHEIRO', 'PAGO', '2025-06-01 10:40:00', '2025-06-01 10:40:00'),
(4, 4, 520.00, '2025-06-01', 'CARTAO_DEBITO', 'PAGO', '2025-06-01 10:40:00', '2025-06-01 10:40:00'),
(5, 5, 150.00, '2025-06-05', 'TRANSFERENCIA', 'PENDENTE', '2025-06-05 10:40:00', '2025-06-05 10:40:00'),
(6, 6, 300.00, '2025-06-12', 'PIX', 'PENDENTE', '2025-06-12 10:00:00', '2025-06-12 10:00:00');

-- CORREÇÃO: QuartoID na tabela de manutenção atualizado para corresponder aos novos IDs.
INSERT INTO `manutencao` (`ManutencaoID`, `QuartoID`, `DataInicio`, `DataFim`, `Descricao`, `Status`, `FuncionarioID`, `DataCriacao`, `DataAtualizacao`) VALUES
(1, 12, '2025-06-01', '2025-06-03', 'Reparo no ar condicionado.', 'CONCLUIDA', 1, '2025-06-01 10:40:00', '2025-06-03 10:40:00'), -- Quarto 202 -> ID 4 mudou para 12
(2, 2, '2025-06-05', '2025-06-07', 'Vazamento na pia do banheiro.', 'CONCLUIDA', 3, '2025-06-05 10:40:00', '2025-06-07 10:40:00'), -- Quarto 102 -> ID 2 (sem mudança)
(3, 11, '2025-06-10', NULL, 'Pintura geral do quarto.', 'PENDENTE', 2, '2025-06-10 10:00:00', '2025-06-10 10:00:00'); -- Quarto 201 -> ID 3 mudou para 11

-- CORREÇÃO: QuartoID nas tabelas de consumo não precisa de alteração, pois a relação é com ReservaID.
INSERT INTO `consumo` (`ConsumoID`, `ReservaID`, `ProdutoID`, `Quantidade`, `Valor`, `DataConsumo`, `DataCriacao`, `DataAtualizacao`) VALUES
(1, 1, 1, 2, 5.00, '2025-06-11', '2025-06-11 10:00:00', '2025-06-11 10:00:00'),
(2, 2, 2, 1, 7.00, '2025-06-09', '2025-06-09 10:00:00', '2025-06-09 10:00:00'),
(3, 3, 3, 1, 10.00, '2025-06-03', '2025-06-03 10:00:00', '2025-06-03 10:00:00'),
(4, 4, 4, 1, 25.00, '2025-06-02', '2025-06-02 10:00:00', '2025-06-02 10:00:00'),
(5, 5, 5, 1, 30.00, '2025-06-06', '2025-06-06 10:00:00', '2025-06-06 10:00:00'),
(6, 6, 1, 1, 5.00, '2025-06-12', '2025-06-12 10:30:00', '2025-06-12 10:30:00');

INSERT INTO `consumoservicos` (`ConsumoServicoID`, `ReservaID`, `ServicoID`, `Quantidade`, `DataConsumo`, `DataCriacao`, `DataAtualizacao`) VALUES
(1, 1, 1, 2, '2025-06-11', '2025-06-11 10:00:00', '2025-06-11 10:00:00'),
(2, 2, 2, 1, '2025-06-10', '2025-06-10 10:00:00', '2025-06-10 10:00:00'),
(3, 3, 4, 1, '2025-06-03', '2025-06-03 10:00:00', '2025-06-03 10:00:00'),
(4, 4, 5, 2, '2025-06-02', '2025-06-02 10:00:00', '2025-06-02 10:00:00'),
(5, 5, 1, 1, '2025-06-06', '2025-06-06 10:00:00', '2025-06-06 10:00:00'),
(6, 6, 2, 1, '2025-06-12', '2025-06-12 10:30:00', '2025-06-12 10:30:00');
