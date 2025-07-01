package com.hotel.gerenciador.dao;

import com.hotel.gerenciador.model.Hospede;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HospedeDAO extends BaseDAO<Hospede> {

    @Override
    protected String getTableName() {
        return "Hospedes";
    }
    @Override
    protected String getIdColumnName() {
        return "HospedeID";
    }

    @Override
    protected Hospede fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("HospedeID");
        String nome = rs.getString("Nome");
        String cpf = rs.getString("CPF");
        String telefone = rs.getString("Telefone");
        String email = rs.getString("Email");
        String endereco = rs.getString("Endereco");
        LocalDate dataNascimento = rs.getDate("DataNascimento") != null ? rs.getDate("DataNascimento").toLocalDate() : null;
        LocalDateTime dataCriacao = rs.getTimestamp("DataCriacao").toLocalDateTime();
        LocalDateTime dataAtualizacao = rs.getTimestamp("DataAtualizacao").toLocalDateTime();

        return new Hospede(id, nome, cpf, telefone, email, endereco, dataNascimento, dataCriacao, dataAtualizacao);
    }

    public boolean insert(Hospede hospede) throws SQLException {
        String sql = "INSERT INTO Hospedes (Nome, CPF, Telefone, Email, Endereco, DataNascimento) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, hospede.getNome());
            stmt.setString(2, hospede.getCpf());
            stmt.setString(3, hospede.getTelefone());
            stmt.setString(4, hospede.getEmail());
            stmt.setString(5, hospede.getEndereco());
            stmt.setDate(6, hospede.getDataNascimento() != null ? Date.valueOf(hospede.getDataNascimento()) : null);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    hospede.setId(keys.getInt(1));
                }
                return true;
            }
        }

        return false;
    }

    public boolean update(Hospede hospede) throws SQLException {
        String sql = "UPDATE Hospedes SET Nome = ?, CPF = ?, Telefone = ?, Email = ?, Endereco = ?, DataNascimento = ? WHERE HospedeID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hospede.getNome());
            stmt.setString(2, hospede.getCpf());
            stmt.setString(3, hospede.getTelefone());
            stmt.setString(4, hospede.getEmail());
            stmt.setString(5, hospede.getEndereco());
            stmt.setDate(6, hospede.getDataNascimento() != null ? Date.valueOf(hospede.getDataNascimento()) : null);
            stmt.setInt(7, hospede.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    public List<Hospede> findByName(String nome) throws SQLException {
        String sql = "SELECT * FROM Hospedes WHERE Nome LIKE ?";
        List<Hospede> hospedes = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                hospedes.add(fromResultSet(rs));
            }
        }

        return hospedes;
    }

    public Hospede findByCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM Hospedes WHERE CPF = ?";
        Hospede hospede = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                hospede = fromResultSet(rs);
            }
        }

        return hospede;
    }

    public List<Hospede> findWithActiveReservas() throws SQLException {
        String sql = "SELECT DISTINCT h.* FROM Hospedes h " +
                    "JOIN Reservas r ON h.HospedeID = r.HospedeID " +
                    "WHERE r.Status NOT IN ('CANCELADA', 'CONCLUIDA')";
        
        List<Hospede> hospedes = new ArrayList<>();
        
        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                hospedes.add(fromResultSet(rs));
            }
        }
        return hospedes;
    }

    public boolean isCpfInUse(String cpf, int excludeId) throws SQLException {
        String sql = "SELECT 1 FROM Hospedes WHERE CPF = ? AND HospedeID != ?";
        
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cpf);
            stmt.setInt(2, excludeId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    public List<Hospede> findByNameOrCPF(String termoBusca) throws SQLException {
        List<Hospede> hospedes = new ArrayList<>();
        String termoLike = "%" + termoBusca + "%"; 

        String cpfBusca = termoBusca; 

        String sql = "SELECT * FROM " + getTableName() + " WHERE Nome LIKE ? OR CPF = ?";
        
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, termoLike); // Para a busca por Nome
            stmt.setString(2, cpfBusca);  // Para a busca por CPF exato
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    hospedes.add(fromResultSet(rs)); // Reutiliza seu método existente
                }
            }
        }
        return hospedes;
    }
}