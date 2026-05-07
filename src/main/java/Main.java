import entidades.ItemCesta;
import entidades.ProdutoComparado;
import entidades.RankingCesta;
import esd.ListaSequencial;
import regras.ComparadorProdutos;
import sm.*;

import java.util.Scanner;

void main() {
    Scanner teclado = new Scanner(System.in);

    Giassi giassi = new Giassi();
    Bistek bistek = new Bistek();
    Fort fort = new Fort();

    // Cesta armazena itens estruturados (nome + preços por supermercado)
    ListaSequencial<ItemCesta> cesta = new ListaSequencial<>();
    String continuar = "s";

    while (continuar.equalsIgnoreCase("s")) {
        IO.print("Digite um produto: ");
        String nomeProduto = teclado.nextLine().trim();

        if (nomeProduto.isEmpty()) {
            IO.println("Produto não informado.");
        } else {
//            trecho comentado para verificação de performance
//            long inicio = System.nanoTime();

            // Agrupa os resultados dos 3 supermercados para comparar por EAN
            ComparadorProdutos comparador = new ComparadorProdutos(
                    bistek.busca(nomeProduto),
                    giassi.busca(nomeProduto),
                    fort.busca(nomeProduto)
            );

//            long fim = System.nanoTime();
//            double tempoSegundos = (fim - inicio) / 1_000_000_000.0;
//            IO.println("Tempo: " + tempoSegundos + "s");

            ListaSequencial<ProdutoComparado> produtos = comparador.produtosComEANNosTres();

            mostraMenuProdutos(produtos);

            if (!produtos.esta_vazia()) {
                IO.print("Escolha as opções (ex: 1,3,5 ou 'todos'): ");
                String entrada = teclado.nextLine().trim();

                ListaSequencial<Integer> opcoes = new ListaSequencial<>();

                if (entrada.equalsIgnoreCase("todos")) {
                    for (int i = 1; i <= produtos.comprimento(); i++) opcoes.adiciona(i);
                } else {
                    String[] partes = entrada.split(",");
                    for (String parte : partes) {
                        try {
                            int op = Integer.parseInt(parte.trim());
                            if (op >= 1 && op <= produtos.comprimento()) opcoes.adiciona(op);
                            else IO.println("Opção inválida ignorada: " + op);
                        } catch (NumberFormatException e) {
                            IO.println("Valor inválido ignorado: " + parte.trim());
                        }
                    }
                }

                if (opcoes.esta_vazia()) {
                    IO.println("Nenhuma opção válida selecionada.");
                } else {
                    for (int i = 0; i < opcoes.comprimento(); i++) {
                        ItemCesta item = produtos.obtem(opcoes.obtem(i) - 1).toItemCesta();
                        cesta.adiciona(item);
                        IO.println("Adicionado: " + item.descricao());
                    }
                }
            }
        }

        IO.print("Deseja adicionar mais produtos? (s/n): ");
        continuar = teclado.nextLine().trim();
    }

    mostraCesta(cesta);
}

// Exibe a lista de produtos encontrados nos 3 supermercados com seus preços
static void mostraMenuProdutos(ListaSequencial<ProdutoComparado> produtos) {
    if (produtos.esta_vazia()) {
        IO.println("Nenhum produto encontrado.");
        return;
    }

    IO.println();
    IO.println("Produtos disponíveis:");

    for (int i = 0; i < produtos.comprimento(); i++) IO.println((i + 1) + " - " + produtos.obtem(i).descricao());
}

// Exibe os itens da cesta e o ranking de supermercados pelo preço total
static void mostraCesta(ListaSequencial<ItemCesta> cesta) {
    IO.println();
    IO.println("=== Cesta ===");

    if (cesta.esta_vazia()) {
        IO.println("Nenhum produto adicionado.");
        return;
    }

    // Lista todos os itens adicionados
    for (int i = 0; i < cesta.comprimento(); i++)
        IO.println((i + 1) + " - " + cesta.obtem(i).descricao());

    // Deixa o cálculo e ordenação do ranking para RankingCesta
    ListaSequencial<RankingCesta> ranking = RankingCesta.calcular(cesta);

    // Exibe o ranking do mais barato ao mais caro
    IO.println();
    IO.println("=== Ranking por preço total da cesta ===");
    for (int i = 0; i < ranking.comprimento(); i++) IO.println(ranking.obtem(i).descricao(i + 1));
}
