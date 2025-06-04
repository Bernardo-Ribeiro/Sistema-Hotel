-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: sistemahotel
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `consumo`
--

DROP TABLE IF EXISTS `consumo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consumo` (
  `ConsumoID` int NOT NULL AUTO_INCREMENT,
  `ReservaID` int NOT NULL,
  `ProdutoID` int NOT NULL,
  `Quantidade` int NOT NULL,
  `Valor` decimal(10,2) NOT NULL,
  `DataConsumo` date NOT NULL,
  `DataCriacao` datetime DEFAULT CURRENT_TIMESTAMP,
  `DataAtualizacao` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ConsumoID`),
  KEY `ReservaID` (`ReservaID`),
  KEY `ProdutoID` (`ProdutoID`),
  CONSTRAINT `consumo_ibfk_1` FOREIGN KEY (`ReservaID`) REFERENCES `reservas` (`ReservaID`),
  CONSTRAINT `consumo_ibfk_2` FOREIGN KEY (`ProdutoID`) REFERENCES `produtos` (`ProdutoID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consumo`
--

/*!40000 ALTER TABLE `consumo` DISABLE KEYS */;
INSERT INTO `consumo` VALUES (1,1,1,2,5.00,'2024-05-10','2025-06-02 10:40:50','2025-06-02 10:40:50'),(2,2,2,1,7.00,'2024-05-16','2025-06-02 10:40:50','2025-06-02 10:40:50'),(3,3,3,1,10.00,'2025-05-29','2025-06-02 10:40:50','2025-06-02 10:40:50'),(4,4,4,1,25.00,'2024-07-01','2025-06-02 10:40:50','2025-06-02 10:40:50'),(5,3,5,1,30.00,'2025-05-30','2025-06-02 10:40:50','2025-06-02 10:40:50'),(6,3,1,1,5.00,'2025-06-02','2025-06-02 10:53:42','2025-06-02 10:53:42'),(7,3,1,1,5.00,'2025-06-02','2025-06-02 11:00:34','2025-06-02 11:00:34');
/*!40000 ALTER TABLE `consumo` ENABLE KEYS */;

--
-- Table structure for table `consumoservicos`
--

DROP TABLE IF EXISTS `consumoservicos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consumoservicos` (
  `ConsumoServicoID` int NOT NULL AUTO_INCREMENT,
  `ReservaID` int NOT NULL,
  `ServicoID` int NOT NULL,
  `Quantidade` int NOT NULL,
  `DataConsumo` date DEFAULT (curdate()),
  `DataCriacao` datetime DEFAULT CURRENT_TIMESTAMP,
  `DataAtualizacao` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ConsumoServicoID`),
  KEY `ReservaID` (`ReservaID`),
  KEY `ServicoID` (`ServicoID`),
  CONSTRAINT `consumoservicos_ibfk_1` FOREIGN KEY (`ReservaID`) REFERENCES `reservas` (`ReservaID`),
  CONSTRAINT `consumoservicos_ibfk_2` FOREIGN KEY (`ServicoID`) REFERENCES `servicos` (`ServicoID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consumoservicos`
--

/*!40000 ALTER TABLE `consumoservicos` DISABLE KEYS */;
INSERT INTO `consumoservicos` VALUES (1,1,1,2,'2024-05-11','2025-06-02 10:40:51','2025-06-02 10:40:51'),(2,2,2,1,'2024-05-17','2025-06-02 10:40:51','2025-06-02 10:40:51'),(3,3,4,1,'2025-05-30','2025-06-02 10:40:51','2025-06-02 10:40:51'),(4,4,5,2,'2024-07-02','2025-06-02 10:40:51','2025-06-02 10:40:51'),(5,3,1,1,'2025-05-31','2025-06-02 10:40:51','2025-06-02 10:40:51');
/*!40000 ALTER TABLE `consumoservicos` ENABLE KEYS */;

--
-- Table structure for table `funcionarios`
--

DROP TABLE IF EXISTS `funcionarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `funcionarios` (
  `FuncionarioID` int NOT NULL AUTO_INCREMENT,
  `Nome` varchar(100) NOT NULL,
  `CPF` varchar(11) NOT NULL,
  `Telefone` varchar(15) DEFAULT NULL,
  `Cargo` varchar(50) DEFAULT NULL,
  `Endereco` varchar(200) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `Salario` decimal(10,2) DEFAULT NULL,
  `DataAdmissao` date DEFAULT (curdate()),
  `DataCriacao` datetime DEFAULT CURRENT_TIMESTAMP,
  `DataAtualizacao` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`FuncionarioID`),
  UNIQUE KEY `CPF` (`CPF`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `funcionarios`
--

/*!40000 ALTER TABLE `funcionarios` DISABLE KEYS */;
INSERT INTO `funcionarios` VALUES (1,'Ana Silva','12345678909','(11) 98765-4321','Recepcionista','Rua das Flores, 123, São Paulo, SP','ana.silva@emailhotel.com',2500.00,'2023-01-15','2025-06-02 10:40:50','2025-06-04 09:28:37'),(2,'Bruno Costa','98765432100','(21) 91234-5678','Gerente','Avenida Principal, 456, Rio de Janeiro, RJ','bruno.costa@emailhotel.com',7500.00,'2020-05-10','2025-06-02 10:40:50','2025-06-04 09:30:00'),(3,'Carla Dias','25836914737','(31) 95678-1234','Camareira','Travessa das Palmeiras, 789, Belo Horizonte, MG','carla.dias@emailhotel.com',1800.00,'2024-02-01','2025-06-02 10:40:50','2025-06-04 09:28:37'),(4,'Daniel Farias','55544433380','(41) 98888-7777','Cozinheiro','Alameda dos Sabores, 101, Curitiba, PR','daniel.farias@emailhotel.com',3200.00,'2022-07-20','2025-06-02 10:40:50','2025-06-04 09:28:37'),(5,'Eduarda Lima','01001001044','(51) 97777-8888','Recepcionista','Rua da Estação, 202, Porto Alegre, RS','eduarda.lima@emailhotel.com',2600.00,'2023-08-01','2025-06-02 10:40:50','2025-06-04 09:28:37'),(6,'Bernardo Ribeiro','04132868001','55996283243','Gerente','Rua Augustin Peniza 433, Armour, Sanatana do LIvramento /RS, 97575140','bernardo.ribeiro@outlook.com.br',10500.00,'2025-06-04','2025-06-04 09:35:24','2025-06-04 09:35:44');
/*!40000 ALTER TABLE `funcionarios` ENABLE KEYS */;

--
-- Table structure for table `hospedes`
--

DROP TABLE IF EXISTS `hospedes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hospedes` (
  `HospedeID` int NOT NULL AUTO_INCREMENT,
  `Nome` varchar(100) NOT NULL,
  `CPF` varchar(11) NOT NULL,
  `Telefone` varchar(20) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `Endereco` varchar(200) DEFAULT NULL,
  `DataNascimento` date DEFAULT NULL,
  `DataCriacao` datetime DEFAULT CURRENT_TIMESTAMP,
  `DataAtualizacao` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`HospedeID`),
  UNIQUE KEY `CPF` (`CPF`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hospedes`
--

/*!40000 ALTER TABLE `hospedes` DISABLE KEYS */;
INSERT INTO `hospedes` VALUES (1,'Fernanda Souza','12345678901','(11) 99999-1111','fernanda.souza@email.com','Rua Teste, 1, Cidade A','1990-03-15','2025-06-02 10:40:50','2025-06-02 10:40:50'),(2,'Gustavo Pereira','23456789012','(22) 98888-2222','gustavo.pereira@email.com','Avenida Exemplo, 2, Cidade B','1985-07-22','2025-06-02 10:40:50','2025-06-02 10:40:50'),(3,'Helena Almeida','34567890123','(33) 97777-3333','helena.almeida@email.com','Praça Modelo, 3, Cidade C','1995-11-30','2025-06-02 10:40:50','2025-06-02 10:40:50'),(4,'Igor Santos','45678901234','(44) 96666-4444','igor.santos@email.com','Travessa Piloto, 4, Cidade D','1980-01-05','2025-06-02 10:40:50','2025-06-02 10:40:50'),(5,'Julia Rocha','56789012345','(55) 95555-5555','julia.rocha@email.com','Rua dos Hóspedes, 5, Cidade E','2000-09-10','2025-06-02 10:40:50','2025-06-02 10:40:50'),(7,'Bernardo Ribeiro','04132868001','(55) 99628-3243','bernardo.ribeiro@outlook.com.br','Rua Augustin Peniza 433, Armour, Santana do Livramento/RS, 97575140','2003-07-22','2025-06-02 11:35:49','2025-06-02 11:35:49');
/*!40000 ALTER TABLE `hospedes` ENABLE KEYS */;

--
-- Table structure for table `manutencao`
--

DROP TABLE IF EXISTS `manutencao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `manutencao` (
  `ManutencaoID` int NOT NULL AUTO_INCREMENT,
  `QuartoID` int NOT NULL,
  `DataInicio` date NOT NULL,
  `DataFim` date DEFAULT NULL,
  `Descricao` text,
  `Status` enum('PENDENTE','EM_ANDAMENTO','CONCLUIDA') DEFAULT 'PENDENTE',
  `FuncionarioID` int DEFAULT NULL,
  `DataCriacao` datetime DEFAULT CURRENT_TIMESTAMP,
  `DataAtualizacao` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ManutencaoID`),
  KEY `QuartoID` (`QuartoID`),
  KEY `FuncionarioID` (`FuncionarioID`),
  CONSTRAINT `manutencao_ibfk_1` FOREIGN KEY (`QuartoID`) REFERENCES `quartos` (`QuartoID`),
  CONSTRAINT `manutencao_ibfk_2` FOREIGN KEY (`FuncionarioID`) REFERENCES `funcionarios` (`FuncionarioID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manutencao`
--

/*!40000 ALTER TABLE `manutencao` DISABLE KEYS */;
INSERT INTO `manutencao` VALUES (1,4,'2024-06-01','2024-06-03','Reparo no ar condicionado.','CONCLUIDA',1,'2025-06-02 10:40:51','2025-06-02 10:40:51'),(2,2,'2025-05-05',NULL,'Vazamento na pia do banheiro.','EM_ANDAMENTO',3,'2025-06-02 10:40:51','2025-06-02 10:40:51'),(3,5,'2024-06-10','2025-06-04','Pintura geral do quarto.','CONCLUIDA',2,'2025-06-02 10:40:51','2025-06-04 10:05:00'),(4,1,'2024-05-20','2024-05-21','Troca de lâmpada queimada.','CONCLUIDA',1,'2025-06-02 10:40:51','2025-06-02 10:40:51'),(5,4,'2024-06-15',NULL,'Verificação de rotina pós-reparo do ar.','PENDENTE',1,'2025-06-02 10:40:51','2025-06-02 10:40:51'),(6,3,'2025-06-04',NULL,'Ta caindo a internet, pfvr arruma ai...','PENDENTE',6,'2025-06-04 10:05:42','2025-06-04 10:05:42');
/*!40000 ALTER TABLE `manutencao` ENABLE KEYS */;

--
-- Table structure for table `pagamentos`
--

DROP TABLE IF EXISTS `pagamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pagamentos` (
  `PagamentoID` int NOT NULL AUTO_INCREMENT,
  `ReservaID` int NOT NULL,
  `ValorPago` decimal(10,2) NOT NULL,
  `DataPagamento` date DEFAULT (curdate()),
  `MetodoPagamento` enum('DINHEIRO','CARTAO_CREDITO','CARTAO_DEBITO','PIX','TRANSFERENCIA') DEFAULT 'DINHEIRO',
  `StatusPagamento` enum('PENDENTE','PAGO','CANCELADO') DEFAULT 'PENDENTE',
  `DataCriacao` datetime DEFAULT CURRENT_TIMESTAMP,
  `DataAtualizacao` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`PagamentoID`),
  KEY `ReservaID` (`ReservaID`),
  CONSTRAINT `pagamentos_ibfk_1` FOREIGN KEY (`ReservaID`) REFERENCES `reservas` (`ReservaID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pagamentos`
--

/*!40000 ALTER TABLE `pagamentos` DISABLE KEYS */;
INSERT INTO `pagamentos` VALUES (1,1,300.00,'2024-05-09','CARTAO_CREDITO','PAGO','2025-06-02 10:40:50','2025-06-02 10:40:50'),(2,2,750.00,'2024-05-14','PIX','PAGO','2025-06-02 10:40:50','2025-06-02 10:40:50'),(3,3,700.00,'2025-05-28','DINHEIRO','PAGO','2025-06-02 10:40:50','2025-06-02 10:40:50'),(4,4,520.00,'2024-06-20','CARTAO_DEBITO','PAGO','2025-06-02 10:40:50','2025-06-02 10:40:50'),(5,5,100.00,'2024-06-25','TRANSFERENCIA','PENDENTE','2025-06-02 10:40:50','2025-06-02 10:40:50'),(6,3,975.00,'2025-06-02','PIX','PAGO','2025-06-02 11:04:33','2025-06-02 11:04:33');
/*!40000 ALTER TABLE `pagamentos` ENABLE KEYS */;

--
-- Table structure for table `produtos`
--

DROP TABLE IF EXISTS `produtos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produtos` (
  `ProdutoID` int NOT NULL AUTO_INCREMENT,
  `Nome` varchar(100) NOT NULL,
  `Descricao` varchar(255) DEFAULT NULL,
  `Preco` decimal(10,2) NOT NULL,
  `Estoque` int NOT NULL,
  `Categoria` enum('FRIGOBAR','RESTAURANTE','BEBIDA','OUTROS') NOT NULL,
  `DataCriacao` datetime DEFAULT CURRENT_TIMESTAMP,
  `DataAtualizacao` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ProdutoID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produtos`
--

/*!40000 ALTER TABLE `produtos` DISABLE KEYS */;
INSERT INTO `produtos` VALUES (1,'Água Mineral 500ml','Água mineral sem gás, garrafa PET.',5.00,100,'FRIGOBAR','2025-06-02 10:40:50','2025-06-02 10:40:50'),(2,'Refrigerante Lata 350ml','Coca-Cola, Guaraná, etc.',7.00,80,'FRIGOBAR','2025-06-02 10:40:50','2025-06-02 10:40:50'),(3,'Chocolate Barra 90g','Chocolate ao leite Hershey\'s.',10.00,50,'FRIGOBAR','2025-06-02 10:40:50','2025-06-02 10:40:50'),(4,'Sanduíche de Queijo e Presunto','Pão de forma, queijo prato e presunto.',25.00,20,'RESTAURANTE','2025-06-02 10:40:50','2025-06-02 10:40:50'),(5,'Porção de Batata Frita','Batata palito frita com sal.',30.00,15,'RESTAURANTE','2025-06-02 10:40:50','2025-06-02 10:40:50');
/*!40000 ALTER TABLE `produtos` ENABLE KEYS */;

--
-- Table structure for table `quartos`
--

DROP TABLE IF EXISTS `quartos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quartos` (
  `QuartoID` int NOT NULL AUTO_INCREMENT,
  `NumeroQuarto` int NOT NULL,
  `Tipo` enum('SOLTEIRO','CASAL','FAMILIA','LUXO') NOT NULL,
  `PrecoDiaria` decimal(10,2) NOT NULL,
  `Status` enum('DISPONIVEL','OCUPADO','MANUTENCAO') DEFAULT 'DISPONIVEL',
  `DataCriacao` datetime DEFAULT CURRENT_TIMESTAMP,
  `DataAtualizacao` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`QuartoID`),
  UNIQUE KEY `NumeroQuarto` (`NumeroQuarto`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quartos`
--

/*!40000 ALTER TABLE `quartos` DISABLE KEYS */;
INSERT INTO `quartos` VALUES (1,101,'SOLTEIRO',150.00,'DISPONIVEL','2025-06-02 10:40:50','2025-06-02 10:40:50'),(2,102,'CASAL',250.00,'DISPONIVEL','2025-06-02 10:40:50','2025-06-02 10:40:50'),(3,201,'FAMILIA',350.00,'MANUTENCAO','2025-06-02 10:40:50','2025-06-04 10:05:42'),(4,202,'LUXO',500.00,'MANUTENCAO','2025-06-02 10:40:50','2025-06-02 10:40:50'),(5,301,'CASAL',260.00,'DISPONIVEL','2025-06-02 10:40:50','2025-06-02 10:40:50');
/*!40000 ALTER TABLE `quartos` ENABLE KEYS */;

--
-- Table structure for table `reservas`
--

DROP TABLE IF EXISTS `reservas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservas` (
  `ReservaID` int NOT NULL AUTO_INCREMENT,
  `HospedeID` int NOT NULL,
  `QuartoID` int NOT NULL,
  `DataCheckIn` date NOT NULL,
  `DataCheckOut` date NOT NULL,
  `Status` enum('CONFIRMADA','CANCELADA','CONCLUIDA','PENDENTE','HOSPEDADO') DEFAULT 'PENDENTE',
  `ValorTotal` decimal(10,2) NOT NULL,
  `DataCriacao` datetime DEFAULT CURRENT_TIMESTAMP,
  `DataAtualizacao` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ReservaID`),
  KEY `HospedeID` (`HospedeID`),
  KEY `QuartoID` (`QuartoID`),
  CONSTRAINT `reservas_ibfk_1` FOREIGN KEY (`HospedeID`) REFERENCES `hospedes` (`HospedeID`),
  CONSTRAINT `reservas_ibfk_2` FOREIGN KEY (`QuartoID`) REFERENCES `quartos` (`QuartoID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservas`
--

/*!40000 ALTER TABLE `reservas` DISABLE KEYS */;
INSERT INTO `reservas` VALUES (1,1,1,'2025-06-10','2025-06-12','CONFIRMADA',300.00,'2025-06-02 11:07:02','2025-06-02 11:07:21'),(2,2,2,'2025-06-15','2025-06-18','PENDENTE',750.00,'2025-06-02 11:07:02','2025-06-02 11:07:21'),(3,3,3,'2025-06-01','2025-06-05','HOSPEDADO',1400.00,'2025-06-02 11:07:02','2025-06-02 11:07:21'),(4,4,5,'2025-07-01','2025-07-03','CONFIRMADA',520.00,'2025-06-02 11:07:02','2025-06-02 11:07:21'),(5,5,1,'2025-07-05','2025-07-07','PENDENTE',300.00,'2025-06-02 11:07:02','2025-06-02 11:07:21');
/*!40000 ALTER TABLE `reservas` ENABLE KEYS */;

--
-- Table structure for table `servicos`
--

DROP TABLE IF EXISTS `servicos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `servicos` (
  `ServicoID` int NOT NULL AUTO_INCREMENT,
  `Nome` varchar(100) NOT NULL,
  `Descricao` varchar(255) DEFAULT NULL,
  `Preco` decimal(10,2) NOT NULL,
  `Disponivel` tinyint(1) DEFAULT '1',
  `DataCriacao` datetime DEFAULT CURRENT_TIMESTAMP,
  `DataAtualizacao` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ServicoID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servicos`
--

/*!40000 ALTER TABLE `servicos` DISABLE KEYS */;
INSERT INTO `servicos` VALUES (1,'Café da Manhã Completo','Buffet de café da manhã com pães, frutas, frios, bolos e bebidas.',45.00,1,'2025-06-02 10:40:50','2025-06-02 10:40:50'),(2,'Lavanderia Express','Lavagem e secagem de até 5 peças de roupa em 24h.',70.00,1,'2025-06-02 10:40:50','2025-06-02 10:40:50'),(3,'Uso da Piscina (Day Use)','Acesso à piscina para não hóspedes.',100.00,1,'2025-06-02 10:40:50','2025-06-02 10:40:50'),(4,'Massagem Relaxante (1h)','Sessão de massagem de uma hora com óleos essenciais.',180.00,1,'2025-06-02 10:40:50','2025-06-02 10:40:50'),(5,'Aluguel de Toalha Extra Piscina','Toalha adicional para uso na piscina.',25.00,1,'2025-06-02 10:40:50','2025-06-02 10:40:50');
/*!40000 ALTER TABLE `servicos` ENABLE KEYS */;

--
-- Dumping routines for database 'sistemahotel'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-04 10:14:11
