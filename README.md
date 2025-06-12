# Sistema de Gerenciamento de Hotel

## Integrantes
Bernardo Ribeiro, Fabricio Del Rio, Felipe de Avila e Vagner Rosa.

## 1. Introdução
O **Sistema de Gerenciamento de Hotel** é um software desenvolvido para automatizar e facilitar a administração das operações diárias de um hotel. O sistema permite o controle eficiente de reservas, check-ins e check-outs, gerenciamento de funcionários, manutenção e limpeza dos quartos.

### Tecnologias Utilizadas:
- **Backend:** Java
- **Interface Gráfica:** JavaFX
- **Banco de Dados:** MySQL
- **Arquitetura:** Padrão MVC (Model-View-Controller)
- **Controle de Versão:** GitHub

## 2. Objetivo do Projeto
Nosso objetivo é criar um sistema intuitivo e eficiente para os funcionários do hotel, reduzindo erros manuais e otimizando as operações diárias. O sistema permitirá:
- **Gestão de reservas**: Registro e controle de reservas de hóspedes.
- **Check-in e check-out**: Processo rápido e seguro.
- **Status dos quartos**: Livre, ocupado, manutenção ou sujo.
- **Gerenciamento de funcionários**: Controle de recepcionistas, camareiras e equipe de manutenção.
- **Controle de manutenções**: Registro e acompanhamento de ordens de serviço.
- **Processamento de pagamentos** e geração de relatórios financeiros.

## 3. Perfis de Usuário e Funcionalidades
### **Administrador**
- Gerencia todos os módulos do sistema.
- Cadastra e gerencia funcionários.
- Acessa relatórios financeiros e operacionais.

### **Recepcionista**
- Registra reservas e gerencia check-in/check-out.
- Consulta e edita informações de hóspedes.
- Processa pagamentos e emite recibos.

### **Camareira**
- Atualiza status de limpeza dos quartos.
- Registra observações sobre o estado dos quartos.

### **Funcionário de Manutenção**
- Visualiza e gerencia ordens de serviço.
- Atualiza status de manutenções realizadas.

### **Gerente**
- Consulta relatórios de ocupação do hotel e faturamento.
- Supervisiona reservas e controle de quartos.

## 4. Diferenciais do Sistema
- **Login seguro** com diferentes níveis de acesso.
- **Interface intuitiva e fácil de usar** (JavaFX).
- **Banco de dados robusto e otimizado** (MySQL).
- **Geração automática de relatórios** para gestão do hotel.
- **Histórico de hóspedes** para atendimento personalizado.

## 5. Cronograma
O projeto será desenvolvido em **3 meses**, com a seguinte divisão:

### **Mês 1**
- Modelagem do banco de dados.
- Desenvolvimento do backend e conexão com MySQL.

### **Mês 2**
- Implementação da interface gráfica (JavaFX).
- Desenvolvimento das principais funcionalidades.

### **Mês 3**
- Testes, correções e otimizações.

---
Este repositório será atualizado conforme o progresso do projeto. Fique à vontade para contribuir e acompanhar nosso desenvolvimento!




# Para rodar o sistema, use o comando a baixo:
%mvn% exec:java -Dexec.mainClass="com.hotel.gerenciador.Main"

* Obs: Tem que ter o maven instalado nas variaveis de ambiente do sistema e executar na pasta raiz que é o diretório: c:\Users\bernardoribeiro\Documents\GitHub\Sistema-Hotel\sistema-hotel\

# Ao fim do projeto será feito um tutorial mais completo de como rodar o script

