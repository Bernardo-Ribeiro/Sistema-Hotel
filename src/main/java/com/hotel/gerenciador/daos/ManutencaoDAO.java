package com.hotel.gerenciador.daos;

import com.hotel.gerenciador.models.Manutencao;
import com.hotel.gerenciador.utils.StatusManutencao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManutencaoDAO extends BaseDAO<Manutencao> {

    @Override
    protected String getTableName() {
        return "manutencao";
    }
    protected String getIdColumnName() {
        return "ManutencaoID";
    }

    @Override
    protected Manutencao fromResultSet(ResultSet rs) throws SQLException {
        Manutencao manutencao = new Manutencao(
            rs.getInt("QuartoID"),
            rs.getDate("DataInicio").toLocalDate(),
            rs.getDate("DataFim") != null ? rs.getDate("DataFim").toLocalDate() : null,
            rs.getString("Descricao"),
            StatusManutencao.valueOf(rs.getString("Status")),
            rs.getInt("FuncionarioID")
        );

        manutencao.setId(rs.getInt("ManutencaoID"));

        return manutencao;
    }

    public boolean insert(Manutencao manutencao) throws SQLException {
        String sql = "INSERT INTO manutencao (QuartoID, DataInicio, DataFim, Descricao, Status, FuncionarioID) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, manutencao.getIdQuarto());
            stmt.setDate(2, Date.valueOf(manutencao.getDataInicio()));
            stmt.setDate(3, manutencao.getDataFim() != null ? Date.valueOf(manutencao.getDataFim()) : null);
            stmt.setString(4, manutencao.getDescricao());
            stmt.setString(5, manutencao.getStatus().name());
            stmt.setInt(6, manutencao.getIdFuncionario());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        manutencao.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean update(Manutencao manutencao) throws SQLException {
        String sql = "UPDATE manutencao SET QuartoID = ?, DataInicio = ?, DataFim = ?, Descricao = ?, " +
                     "Status = ?, FuncionarioID = ? WHERE ManutencaoID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, manutencao.getIdQuarto());
            stmt.setDate(2, Date.valueOf(manutencao.getDataInicio()));
            stmt.setDate(3, manutencao.getDataFim() != null ? Date.valueOf(manutencao.getDataFim()) : null);
            stmt.setString(4, manutencao.getDescricao());
            stmt.setString(5, manutencao.getStatus().name());
            stmt.setInt(6, manutencao.getIdFuncionario());
            stmt.setInt(7, manutencao.getId());

            return stmt.executeUpdate() > 0;
        }
    }
    public List<Manutencao> findByStatus(StatusManutencao status) throws SQLException {
        String sql = "SELECT * FROM manutencao WHERE Status = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());
            ResultSet rs = stmt.executeQuery();

            List<Manutencao> manutencoes = new ArrayList<>();
            while (rs.next()) {
                Manutencao manutencao = fromResultSet(rs);
                manutencoes.add(manutencao);
            }
            return manutencoes;
        }
    }
}
