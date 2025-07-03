package com.hotel.gerenciador.daos;

import com.hotel.gerenciador.models.Funcionario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO extends BaseDAO<Funcionario> {

    @Override
    protected String getTableName() {
        return "funcionarios";
    }
    protected String getIdColumnName() {
        return "FuncionarioID";
    }

    @Override
    protected Funcionario fromResultSet(ResultSet rs) throws SQLException {
        try {
            return new Funcionario(
                rs.getInt("FuncionarioID"),
                rs.getString("Nome"),
                rs.getString("Cargo"),
                rs.getDouble("Salario"),
                rs.getString("Telefone"),
                rs.getString("CPF"),
                rs.getString("Email"),
                rs.getString("Endereco"),
                rs.getDate("DataAdmissao").toLocalDate(),
                rs.getTimestamp("DataCriacao").toLocalDateTime(),
                rs.getTimestamp("DataAtualizacao").toLocalDateTime()
            );
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public boolean insert(Funcionario funcionario) throws SQLException {
        String sql = "INSERT INTO funcionarios (Nome, Cargo, Salario, Telefone, CPF, Email, Endereco, DataAdmissao) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCargo());
            stmt.setDouble(3, funcionario.getSalario());
            stmt.setString(4, funcionario.getTelefone());
            stmt.setString(5, funcionario.getCpf());
            stmt.setString(6, funcionario.getEmail());
            stmt.setString(7, funcionario.getEndereco());
            stmt.setDate(8, Date.valueOf(funcionario.getDataAdmissao()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        funcionario.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean update(Funcionario funcionario) throws SQLException {
        String sql = "UPDATE funcionarios SET Nome = ?, Cargo = ?, Salario = ?, Telefone = ?, CPF = ?, Email = ?, Endereco = ?, DataAdmissao = ? " +
                     "WHERE FuncionarioID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCargo());
            stmt.setDouble(3, funcionario.getSalario());
            stmt.setString(4, funcionario.getTelefone());
            stmt.setString(5, funcionario.getCpf());
            stmt.setString(6, funcionario.getEmail());
            stmt.setString(7, funcionario.getEndereco());
            stmt.setDate(8, Date.valueOf(funcionario.getDataAdmissao()));
            stmt.setInt(9, funcionario.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    public Funcionario findByCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM funcionarios WHERE CPF = ?";
        Funcionario funcionario = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                funcionario = fromResultSet(rs);
            }
        }

        return funcionario;
    }
    public List<Funcionario> findByName(String nome) throws SQLException {
        String sql = "SELECT * FROM funcionarios WHERE Nome LIKE ?";
        List<Funcionario> funcionarios = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                funcionarios.add(fromResultSet(rs));
            }
        }

        return funcionarios;
    }
}