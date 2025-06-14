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
    // Nomes dos arquivos para persistência de dados
    private static final String ARQUIVO_CONTAS = "contas.txt";
    private static final String ARQUIVO_ATIVOS = "ativos.txt";

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

        // Inicializa as coleções para armazenar dados em memória.
        // 1. HashMap para contas (ID da Conta -> Objeto Conta)
        Map<Integer, Conta> mapaDeContas = new HashMap<>();
        // 2. ArrayList para ativos (lista de objetos Ativo)
        List<Ativo> ativosDisponiveis = new ArrayList<>();

        // Adicionando as novas coleções para cumprir o requisito de "pelo menos 2 classes"
        // 3. ArrayList para listar todas as contas (ID da Conta -> Objeto Conta)
        List<Conta> listaDeContas = new ArrayList<>();
        // 4. HashMap para criptoativos por símbolo (Símbolo -> Objeto CriptoAtivo)
        Map<String, CriptoAtivo> criptosPorSimbolo = new HashMap<>();

        // 1. Carrega dados existentes dos arquivos no início da aplicação.
        carregarContasDoArquivo(mapaDeContas);
        carregarAtivosDoArquivo(ativosDisponiveis);

        // Popula as novas coleções com base nos dados carregados/existentes
        // Preenchendo listaDeContas a partir de mapaDeContas
        listaDeContas.addAll(mapaDeContas.values());

        // Preenchendo criptosPorSimbolo a partir de ativosDisponiveis
        for (Ativo ativo : ativosDisponiveis) {
            if (ativo instanceof CriptoAtivo) {
                criptosPorSimbolo.put(ativo.getSimbolo(), (CriptoAtivo) ativo);
            }
        }

        // Instancia o menu principal da aplicação, passando o mapa de contas.
        MenusAplicacao telaInicial = new MenusAplicacao(mapaDeContas);

        try {
            // Inicia o fluxo de sessão (criação/login) através do MenusAplicacao.
            telaInicial.iniciarSessao();

            // Adiciona um ativo de exemplo se a lista de ativos carregados estiver vazia.
            // Este ativo será salvo ao final da execução.
            if (ativosDisponiveis.isEmpty()) {
                CriptoAtivo btc = new CriptoAtivo(1, "Bitcoin", "BTC", new BigDecimal("200000.00"));
                CriptoAtivo eth = new CriptoAtivo(2, "Ethereum", "ETH", new BigDecimal("90000.00"));
                ativosDisponiveis.add(btc);
                ativosDisponiveis.add(eth);
                criptosPorSimbolo.put(btc.getSimbolo(), btc);
                criptosPorSimbolo.put(eth.getSimbolo(), eth);
                System.out.println("\nAdicionados ativos de exemplo (Bitcoin, Ethereum) aos ativos disponíveis (serão salvos ao sair).");
            }

            // Exemplo de como você pode interagir com as novas coleções
            System.out.println("\n--- Resumo das Coleções ---");
            System.out.println("Total de contas em mapaDeContas: " + mapaDeContas.size());
            System.out.println("Total de contas em listaDeContas: " + listaDeContas.size());
            System.out.println("Total de ativos em ativosDisponiveis: " + ativosDisponiveis.size());
            System.out.println("Total de criptoativos em criptosPorSimbolo: " + criptosPorSimbolo.size());


        } catch (NumberFormatException e) {
            System.err.println("Erro: Formato numérico inválido - " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Erro: Argumento inválido ao popular objeto - " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado durante a execução: " + e.getMessage());
            e.printStackTrace(); // Imprime o rastreamento completo da pilha para depuração
        } finally {
            // 2. Salva todos os dados (contas e ativos) de volta nos arquivos ao finalizar a aplicação.
            System.out.println("\nFinalizando e salvando dados...");
            salvarContasEmArquivo(mapaDeContas);
            salvarAtivosEmArquivo(ativosDisponiveis); // Salva ativos que foram adicionados/modificados
            System.out.println("Dados salvos com sucesso em " + ARQUIVO_CONTAS + " e " + ARQUIVO_ATIVOS + ".");
        }
    }

    /**
     * Salva as informações de todas as contas em um arquivo de texto.
     * Cada linha do arquivo representa uma conta, com seus atributos separados por vírgula.
     * Inclui o tipo de conta (Pessoal/Empresarial) e AGORA A SENHA para recriação correta.
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
                // Salva ID, Nome, Email, Saldo, Tipo da Conta e SENHA HASH
                // A senhaHash pode ser null se a conta for criada sem um fluxo de cadastro completo,
                // então adicionamos um valor padrão ou uma verificação.
                String senhaParaSalvar = (conta.senhaHash != null) ? conta.senhaHash : "";
                writer.write(conta.getIdConta() + "," + conta.nome + "," + conta.email + "," + conta.saldo.toPlainString() + "," + tipoConta + "," + senhaParaSalvar);
                writer.newLine(); // Adiciona uma nova linha para a próxima conta
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar contas no arquivo " + ARQUIVO_CONTAS + ": " + e.getMessage());
        }
    }

    /**
     * Carrega as informações das contas de um arquivo de texto para o mapa de contas.
     * Lê cada linha, separa os atributos e reconstrói os objetos Conta.
     * Também atualiza o contador de próximo ID de conta para evitar colisões.
     * @param contas O mapa onde as contas carregadas serão armazenadas.
     */
    private static void carregarContasDoArquivo(Map<Integer, Conta> contas) {
        int maxId = 0; // Variável para rastrear o maior ID encontrado nos arquivos
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_CONTAS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                // AGORA espera 6 campos: id, nome, email, saldo, tipo, senhaHash
                if (dados.length == 6) {
                    int id = Integer.parseInt(dados[0]);
                    String nome = dados[1];
                    String email = dados[2];
                    BigDecimal saldo = new BigDecimal(dados[3]);
                    String tipoContaStr = dados[4];
                    String senhaHash = dados[5]; // Lendo a senhaHash do arquivo

                    Conta contaCarregada;
                    // Instancia o tipo correto de conta baseado no campo 'tipoContaStr'
                    if ("Pessoal".equals(tipoContaStr)) {
                        contaCarregada = new ContaPessoal();
                    } else if ("Empresarial".equals(tipoContaStr)) {
                        contaCarregada = new ContaEmpresarial();
                    } else {
                        System.err.println("Tipo de conta desconhecido ('" + tipoContaStr + "') encontrado na linha: " + linha + ". Ignorando esta conta.");
                        continue; // Pula para a próxima linha se o tipo for desconhecido
                    }

                    // Define os atributos da conta carregada usando os dados do arquivo
                    contaCarregada.setIdConta(id); // Usa o setter para definir o ID lido
                    contaCarregada.nome = nome;
                    contaCarregada.email = email;
                    contaCarregada.saldo = saldo;
                    contaCarregada.senhaHash = senhaHash; // Definindo a senhaHash lida do arquivo

                    contas.put(id, contaCarregada); // Adiciona a conta reconstruída ao mapa

                    // Atualiza o maior ID encontrado para garantir que novos IDs sejam únicos
                    if (id > maxId) {
                        maxId = id;
                    }
                } else {
                    System.err.println("Linha mal formatada em " + ARQUIVO_CONTAS + ": '" + linha + "'. Esperado 6 campos, encontrado " + dados.length + ". Ignorando.");
                }
            }
            // Após carregar todas as contas, atualiza o gerador de IDs na classe Conta
            Conta.setProximoIdConta(maxId + 1);

        } catch (IOException e) {
            System.out.println("Arquivo de contas '" + ARQUIVO_CONTAS + "' não encontrado ou erro de leitura: " + e.getMessage() + ". Um novo será criado, se necessário.");
        } catch (NumberFormatException e) {
            System.err.println("Erro de formato de número ao carregar contas de '" + ARQUIVO_CONTAS + "': " + e.getMessage() + ". Verifique o conteúdo do arquivo.");
        }
    }

    /**
     * Salva as informações de todos os ativos em um arquivo de texto.
     * Cada linha do arquivo representa um ativo com seus atributos separados por vírgula.
     * @param ativos A lista de ativos a serem salvas.
     */
    private static void salvarAtivosEmArquivo(List<Ativo> ativos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_ATIVOS))) {
            for (Ativo ativo : ativos) {
                // Salva ID, Nome, Símbolo e Cotação (em formato plain string para BigDecimal)
                writer.write(ativo.getId() + "," + ativo.getNome() + "," + ativo.getSimbolo() + "," + ativo.getCotacao().toPlainString());
                writer.newLine(); // Adiciona uma nova linha para o próximo ativo
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar ativos no arquivo " + ARQUIVO_ATIVOS + ": " + e.getMessage());
        }
    }

    /**
     * Carrega as informações dos ativos de um arquivo de texto para uma lista.
     * Lê cada linha, separa os atributos e reconstrói os objetos Ativo (assumindo CriptoAtivo).
     * @param ativos A lista onde os ativos carregados serão armazenados.
     */
    private static void carregarAtivosDoArquivo(List<Ativo> ativos) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_ATIVOS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                // Espera 4 campos: id, nome, simbolo, cotacao
                if (dados.length == 4) {
                    int id = Integer.parseInt(dados[0]);
                    String nome = dados[1];
                    String simbolo = dados[2];
                    BigDecimal cotacao = new BigDecimal(dados[3]);

                    // Para este exemplo, assumimos que todos os ativos salvos são CriptoAtivos.
                    // Em um cenário mais complexo, você poderia ter um campo de 'tipo' para diferenciar.
                    CriptoAtivo criptoAtivo = new CriptoAtivo(id, nome, simbolo, cotacao);
                    ativos.add(criptoAtivo); // Adiciona o ativo reconstruído à lista
                } else {
                    System.err.println("Linha mal formatada em " + ARQUIVO_ATIVOS + ": '" + linha + "'. Ignorando.");
                }
            }
        } catch (IOException e) {
            System.out.println("Arquivo de ativos '" + ARQUIVO_ATIVOS + "' não encontrado ou erro de leitura: " + e.getMessage() + ". Um novo será criado, se necessário.");
        } catch (NumberFormatException e) {
            System.err.println("Erro de formato de número ao carregar ativos de '" + ARQUIVO_ATIVOS + "': " + e.getMessage() + ". Verifique o conteúdo do arquivo.");
        }
    }
}
