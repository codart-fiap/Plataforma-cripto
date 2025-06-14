package ProjetoTioPatinhas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    // Nome do arquivo para persistência de dados das contas (incluindo carteira)
    private static final String ARQUIVO_CONTAS = "contas.txt";
    // O arquivo de ativos globais não será mais usado, pois os ativos são por conta.
    // private static final String ARQUIVO_ATIVOS = "ativos.txt";

    public static void main(String[] args) {

        // Exibe o banner de boas-vindas ao iniciar a aplicação
        System.out.println("" +
                "+-------------------------------------+\n" +
                "|                                     |\n" +
                "| M     M   III   DDDD   AAAAA  SSSSS |\n" +
                "| MM   MM    I    D   D  A   A  S     |\n" +
                "| M M M M    I    D   D  AAAAA  SSSSS |\n" +
                "| M  M  M    I    D   D  A   A      S |\n" +
                "| M     M   III   DDDD   A   A  SSSSS |\n" +
                "|                                     |\n" +
                "+-------------------------------------+");

        // Inicializa a coleção para armazenar dados em memória.
        // HashMap para contas (ID da Conta -> Objeto Conta)
        Map<Integer, Conta> mapaDeContas = new HashMap<>();

        // As coleções adicionais para cumprir o requisito de "pelo menos 2 classes"
        // Serão populadas APÓS o carregamento das contas, a partir de `mapaDeContas`.
        // ArrayList para listar todas as contas
        List<Conta> listaDeContas = new ArrayList<>();
        // HashMap para criptoativos por símbolo (Exemplo de ativos do mercado, não do usuário)
        Map<String, CriptoAtivo> criptosDisponiveisNoMercado = new HashMap<>();

        // Adicionando criptoativos de exemplo ao mercado (estes são fixos, não por usuário)
        CriptoAtivo btc = new CriptoAtivo(1, "Bitcoin", "BTC", new BigDecimal("200000.00"));
        CriptoAtivo eth = new CriptoAtivo(2, "Ethereum", "ETH", new BigDecimal("90000.00"));
        criptosDisponiveisNoMercado.put(btc.getSimbolo(), btc);
        criptosDisponiveisNoMercado.put(eth.getSimbolo(), eth);


        // 1. Carrega dados existentes das contas (incluindo carteiras) do arquivo.
        carregarContasDoArquivo(mapaDeContas);

        // Popula a listaDeContas com base nas contas carregadas/existentes
        listaDeContas.addAll(mapaDeContas.values());

        // Instancia o menu principal da aplicação, passando o mapa de contas.
        MenusAplicacao telaInicial = new MenusAplicacao(mapaDeContas);

        try {
            // Inicia o fluxo de sessão (criação/login) através do MenusAplicacao.
            telaInicial.iniciarSessao();

            // Exemplo de como você pode interagir com as coleções (após iniciar a sessão)
            System.out.println("\n--- Resumo das Coleções ---");
            System.out.println("Total de contas em mapaDeContas: " + mapaDeContas.size());
            System.out.println("Total de contas em listaDeContas: " + listaDeContas.size());
            System.out.println("Total de criptoativos disponíveis no mercado (exemplo): " + criptosDisponiveisNoMercado.size());


        } catch (NumberFormatException e) {
            System.err.println("Erro: Formato numérico inválido - " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Erro: Argumento inválido ao popular objeto: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado durante a execução: " + e.getMessage());
            e.printStackTrace(); // Imprime o rastreamento completo da pilha para depuração
        } finally {
            // 2. Salva todos os dados das contas (incluindo carteiras) de volta no arquivo ao finalizar.
            System.out.println("\nFinalizando e salvando dados...");
            salvarContasEmArquivo(mapaDeContas);
            System.out.println("Dados salvos com sucesso em " + ARQUIVO_CONTAS + ".");
        }
    }

    /**
     * Salva as informações de todas as contas em um arquivo de texto.
     * Cada linha do arquivo representa uma conta, com seus atributos separados por vírgula,
     * incluindo a carteira de ativos do usuário serializada.
     * @param contas O mapa de contas (ID -> Conta) a serem salvas.
     */
    private static void salvarContasEmArquivo(Map<Integer, Conta> contas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_CONTAS))) {
            for (Conta conta : contas.values()) {
                String tipoConta = "Desconhecido";
                if (conta instanceof ContaPessoal) {
                    tipoConta = "Pessoal";
                } else if (conta instanceof ContaEmpresarial) {
                    tipoConta = "Empresarial";
                }
                // Converte o mapa de ativos da carteira para uma string para salvar
                String carteiraSerializada = mapToString(conta.carteiraDeAtivos);

                String senhaParaSalvar = (conta.senhaHash != null) ? conta.senhaHash : "";

                // Salva ID, Nome, Email, Saldo, Tipo da Conta, Senha Hash e Carteira de Ativos
                writer.write(conta.getIdConta() + "," + conta.nome + "," + conta.email + "," +
                        conta.saldo.toPlainString() + "," + tipoConta + "," +
                        senhaParaSalvar + "," + carteiraSerializada);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar contas no arquivo " + ARQUIVO_CONTAS + ": " + e.getMessage());
        }
    }

    /**
     * Carrega as informações das contas de um arquivo de texto para o mapa de contas.
     * Lê cada linha, separa os atributos e reconstrói os objetos Conta,
     * incluindo a deserialização da carteira de ativos.
     * Também atualiza o contador de próximo ID de conta para evitar colisões.
     * @param contas O mapa onde as contas carregadas serão armazenadas.
     */
    private static void carregarContasDoArquivo(Map<Integer, Conta> contas) {
        int maxId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_CONTAS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",", 7); // Limite de 7 para pegar a carteira completa
                // Espera 7 campos: id, nome, email, saldo, tipo, senhaHash, carteiraDeAtivos
                if (dados.length == 7) {
                    int id = Integer.parseInt(dados[0]);
                    String nome = dados[1];
                    String email = dados[2];
                    BigDecimal saldo = new BigDecimal(dados[3]);
                    String tipoContaStr = dados[4];
                    String senhaHash = dados[5];
                    String carteiraSerializada = dados[6]; // Lendo a carteira serializada

                    Conta contaCarregada;
                    if ("Pessoal".equals(tipoContaStr)) {
                        contaCarregada = new ContaPessoal();
                    } else if ("Empresarial".equals(tipoContaStr)) {
                        contaCarregada = new ContaEmpresarial();
                    } else {
                        System.err.println("Tipo de conta desconhecido ('" + tipoContaStr + "') encontrado na linha: " + linha + ". Ignorando esta conta.");
                        continue;
                    }

                    // Define os atributos da conta carregada
                    contaCarregada.setIdConta(id);
                    contaCarregada.nome = nome;
                    contaCarregada.email = email;
                    contaCarregada.saldo = saldo;
                    contaCarregada.senhaHash = senhaHash;
                    contaCarregada.carteiraDeAtivos = stringToMap(carteiraSerializada); // Deserializando a carteira

                    contas.put(id, contaCarregada);

                    if (id > maxId) {
                        maxId = id;
                    }
                } else {
                    System.err.println("Linha mal formatada em " + ARQUIVO_CONTAS + ": '" + linha + "'. Esperado 7 campos, encontrado " + dados.length + ". Ignorando.");
                }
            }
            Conta.setProximoIdConta(maxId + 1);

        } catch (IOException e) {
            System.out.println("Arquivo de contas '" + ARQUIVO_CONTAS + "' não encontrado ou erro de leitura: " + e.getMessage() + ". Um novo será criado, se necessário.");
        } catch (NumberFormatException e) {
            System.err.println("Erro de formato de número ao carregar contas de '" + ARQUIVO_CONTAS + "': " + e.getMessage() + ". Verifique o conteúdo do arquivo.");
        }
    }

    /**
     * Converte um mapa de String para BigDecimal em uma única string serializada.
     * Formato: "chave1:valor1;chave2:valor2"
     * @param map O mapa a ser serializado.
     * @return A string serializada.
     */
    private static String mapToString(Map<String, BigDecimal> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(":").append(entry.getValue().toPlainString()).append(";");
        }
        // Remove o último ponto e vírgula se existir
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * Converte uma string serializada de volta para um mapa de String para BigDecimal.
     * Formato esperado: "chave1:valor1;chave2:valor2"
     * @param data A string serializada.
     * @return O mapa deserializado.
     */
    private static Map<String, BigDecimal> stringToMap(String data) {
        Map<String, BigDecimal> map = new HashMap<>();
        if (data == null || data.isEmpty()) {
            return map;
        }
        String[] entries = data.split(";");
        for (String entry : entries) {
            String[] parts = entry.split(":");
            if (parts.length == 2) {
                try {
                    String key = parts[0];
                    BigDecimal value = new BigDecimal(parts[1]);
                    map.put(key, value);
                } catch (NumberFormatException e) {
                    System.err.println("Erro ao converter valor da carteira para BigDecimal: " + parts[1] + ". Ignorando entrada.");
                }
            }
        }
        return map;
    }
}
