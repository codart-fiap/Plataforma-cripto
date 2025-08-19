package ProjetoTioPatinhas;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AtivoDao {
    private final Connection conn;

    public AtivoDao(Connection conn) {
        this.conn = conn;
    }

    // INSERT: retorna o ID gerado e atualiza o objeto
    public int inserir(Ativo ativo) throws SQLException {
    String sql = "INSERT INTO T_ATIVO (nome, simbolo, cotacao) VALUES (?, ?, ?)";
    // peça explicitamente a coluna de chave gerada
    try (PreparedStatement ps = conn.prepareStatement(sql, new String[] { "ID_ATIVO" })) {
        ps.setString(1, ativo.getNome());
        ps.setString(2, ativo.getSimbolo());
        ps.setBigDecimal(3, ativo.getCotacao());
        ps.executeUpdate();
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                int id = rs.getInt(1); // agora vem o ID numérico
                ativo.setId(id);
                return id;
            }
        }
    }
    throw new SQLException("Não foi possível obter o ID gerado.");
}

    // SELECT por ID
    public Ativo buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_ativo, nome, simbolo, cotacao FROM T_ATIVO WHERE id_ativo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    // UPDATE da cotação
    public int atualizarCotacao(int id, BigDecimal novaCotacao) throws SQLException {
        String sql = "UPDATE T_ATIVO SET cotacao = ? WHERE id_ativo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, novaCotacao);
            ps.setInt(2, id);
            return ps.executeUpdate();
        }
    }

    // DELETE por ID
    public int deletarPorId(int id) throws SQLException {
        String sql = "DELETE FROM T_ATIVO WHERE id_ativo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    // SELECT *
    public List<Ativo> listarTodos() throws SQLException {
        String sql = "SELECT id_ativo, nome, simbolo, cotacao FROM T_ATIVO ORDER BY id_ativo";
        List<Ativo> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    private Ativo mapRow(ResultSet rs) throws SQLException {
        return new Ativo(
                rs.getInt("id_ativo"),
                rs.getString("nome"),
                rs.getString("simbolo"),
                rs.getBigDecimal("cotacao")
        );
    }
}