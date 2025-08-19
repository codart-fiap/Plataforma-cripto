package ProjetoTioPatinhas;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            AtivoDao dao = new AtivoDao(conn);
            
            while (true) {
                System.out.println("\n=== SISTEMA DE GERENCIAMENTO DE ATIVOS ===");
                System.out.println("1 - Adicionar ativo");
                System.out.println("2 - Selecionar ativo");
                System.out.println("3 - Atualizar cotação");
                System.out.println("4 - Excluir ativo");
                System.out.println("0 - Sair");
                System.out.print("Escolha uma opção: ");
                
                int opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar buffer
                
                switch (opcao) {
                    case 1:
                        adicionarAtivo(dao, scanner);
                        break;
                    case 2:
                        selecionarAtivo(dao, scanner);
                        break;
                    case 3:
                        atualizarCotacao(dao, scanner);
                        break;
                    case 4:
                        excluirAtivo(dao, scanner);
                        break;
                    case 0:
                        conn.commit();
                        System.out.println("Transação confirmada. Saindo...");
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro de SQL: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
    
    private static void adicionarAtivo(AtivoDao dao, Scanner scanner) {
        try {
            System.out.print("Nome do ativo: ");
            String nome = scanner.nextLine();
            
            System.out.print("Símbolo do ativo: ");
            String simbolo = scanner.nextLine();
            
            System.out.print("Cotação (USD): ");
            BigDecimal cotacao = new BigDecimal(scanner.nextLine());
            
            Ativo ativo = new Ativo(nome, simbolo, cotacao);
            int id = dao.inserir(ativo);
            
            System.out.println("Ativo adicionado com sucesso! ID: " + id);
            
        } catch (Exception e) {
            System.err.println("Erro ao adicionar ativo: " + e.getMessage());
        }
    }
    
    private static void selecionarAtivo(AtivoDao dao, Scanner scanner) {
        try {
            System.out.print("ID do ativo para buscar: ");
            int id = scanner.nextInt();
            
            Ativo ativo = dao.buscarPorId(id);
            if (ativo != null) {
                System.out.println("Ativo encontrado: " + ativo);
            } else {
                System.out.println("Ativo não encontrado!");
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar ativo: " + e.getMessage());
        }
    }
    
    private static void atualizarCotacao(AtivoDao dao, Scanner scanner) {
        try {
            System.out.print("ID do ativo para atualizar: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer
            
            System.out.print("Nova cotação (USD): ");
            BigDecimal novaCotacao = new BigDecimal(scanner.nextLine());
            
            int rowsUpdated = dao.atualizarCotacao(id, novaCotacao);
            if (rowsUpdated > 0) {
                System.out.println("Cotação atualizada com sucesso!");
            } else {
                System.out.println("Nenhum ativo foi atualizado. Verifique o ID.");
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao atualizar cotação: " + e.getMessage());
        }
    }
    
    private static void excluirAtivo(AtivoDao dao, Scanner scanner) {
        try {
            System.out.print("ID do ativo para excluir: ");
            int id = scanner.nextInt();
            
            int rowsDeleted = dao.deletarPorId(id);
            if (rowsDeleted > 0) {
                System.out.println("Ativo excluído com sucesso!");
            } else {
                System.out.println("Nenhum ativo foi excluído. Verifique o ID.");
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao excluir ativo: " + e.getMessage());
        }
    }
}