package ProjetoTioPatinhas;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = ConnectionFactory.getConnection()) {
            // Vamos controlar a transação aqui
            conn.setAutoCommit(false);

            AtivoDao dao = new AtivoDao(conn);

            // INSERTs (valores em USD)
            Ativo bnb = new Ativo("BNB", "BNB", new BigDecimal("844.80"));
            int idBnb = dao.inserir(bnb);

            Ativo sol = new Ativo("Solana", "SOL", new BigDecimal("183.05"));
            int idSol = dao.inserir(sol);

            Ativo ada = new Ativo("Cardano", "ADA", new BigDecimal("0.92"));
            int idAda = dao.inserir(ada);

            System.out.println("IDs gerados -> BNB: " + idBnb + ", SOL: " + idSol + ", ADA: " + idAda);

            // SELECT pelo id_ativo (ex.: buscar BNB)
            Ativo selecionado = dao.buscarPorId(idBnb);
            System.out.println("Selecionado: " + selecionado);

            // UPDATE na cotação do que será deletado (ex.: atualizar ADA)
            int rowsUpd = dao.atualizarCotacao(idAda, new BigDecimal("0.95"));
            System.out.println("Atualizações na cotação: " + rowsUpd);

            // DELETE pelo id_ativo (ex.: deletar ADA)
            int rowsDel = dao.deletarPorId(idAda);
            System.out.println("Registros deletados: " + rowsDel);

            conn.commit();
            System.out.println("Transação confirmada com sucesso.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro de SQL: " + e.getMessage());
        }
    }
}