import esd.ListaProdutos;
import esd.ListaSequencial;
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
            IO.println("Produto nao informado.");
        } else {
            Supermercado.Resultado resultadosGiassi = procura(giassi, nomeProduto);
            Supermercado.Resultado resultadosBistek = procura(bistek, nomeProduto);
            Supermercado.Resultado resultadosFort = procura(fort, nomeProduto);

            // Agrupa os resultados dos 3 supermercados para comparar por EAN
            ListaProdutos listaProdutos = new ListaProdutos(resultadosBistek, resultadosGiassi, resultadosFort);
            ListaSequencial<Produto> produtosNosTres = listaProdutos.produtosComEANNosTres();

            mostraMenuProdutos(produtosNosTres, listaProdutos);

            if (!produtosNosTres.esta_vazia()) {
                IO.print("Escolha uma opção: ");
                int opcao = teclado.nextInt();
                teclado.nextLine();

                if (opcao < 1 || opcao > produtosNosTres.comprimento()) {
                    IO.println("Opção inválida.");
                } else {
                    Produto produtoEscolhido = produtosNosTres.obtem(opcao - 1);

                    // Obtém os preços do produto nos 3 supermercados: [bistek, giassi, fort]
                    float[] precos = listaProdutos.precosNosTres(produtoEscolhido);

                    // Cria o item da cesta com nome e preços individuais
                    ItemCesta item = new ItemCesta(produtoEscolhido.getNome(), precos[0], precos[1], precos[2]);
                    cesta.adiciona(item);

                    IO.println("Produto adicionado na cesta:");
                    IO.println(item.descricao());
                }
            }
        }

        IO.print("Deseja adicionar mais produtos? (s/n): ");
        continuar = teclado.nextLine().trim();
    }

    mostraCesta(cesta);
}

// Busca os produtos de um supermercado pelo nome
static Supermercado.Resultado procura(Supermercado supermercado, String nome) {
    return supermercado.busca(nome);
}

// Exibe a lista de produtos encontrados nos 3 supermercados com seus preços
static void mostraMenuProdutos(ListaSequencial<Produto> produtos, ListaProdutos listaProdutos) {
    IO.println();
    IO.println("Produtos disponíveis:");

    if (produtos.esta_vazia()) {
        IO.println("Nenhum produto encontrado.");
        return;
    }

    for (int i = 0; i < produtos.comprimento(); i++) {
        IO.println((i + 1) + " - " + listaProdutos.descricaoComPrecosNosTres(produtos.obtem(i)));
    }
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
    for (int i = 0; i < cesta.comprimento(); i++) {
        IO.println((i + 1) + " - " + cesta.obtem(i).descricao());
    }

    // Acumula o total de cada supermercado somando os preços de todos os itens
    float totalBistek = 0;
    float totalGiassi = 0;
    float totalFort = 0;

    for (int i = 0; i < cesta.comprimento(); i++) {
        ItemCesta item = cesta.obtem(i);
        totalBistek += item.precoBistek;
        totalGiassi += item.precoGiassi;
        totalFort += item.precoFort;
    }

    // Cria arrays com supermercados e totais para ordenar juntos
    String[] nomes = { "Bistek", "Giassi", "Fort Atacadista" };
    float[] totais = { totalBistek, totalGiassi, totalFort };

    // Ordena pelo preço total (bubble sort — só 3 elementos)
    for (int i = 0; i < totais.length - 1; i++) {
        for (int j = 0; j < totais.length - 1 - i; j++) {
            if (totais[j] > totais[j + 1]) {
                float tmpF = totais[j];
                totais[j] = totais[j + 1];
                totais[j + 1] = tmpF;
                String tmpS = nomes[j];
                nomes[j] = nomes[j + 1];
                nomes[j + 1] = tmpS;
            }
        }
    }

    // Exibe o ranking do mais barato ao mais caro
    IO.println();
    IO.println("=== Ranking por preço total da cesta ===");
    for (int i = 0; i < nomes.length; i++) {
        IO.println((i + 1) + "º " + nomes[i] + ": R$ " + String.format("%.2f", totais[i]));
    }
}

// Representa um item da cesta com o nome e o preço em cada supermercado
class ItemCesta {
    String nome;
    float precoBistek;
    float precoGiassi;
    float precoFort;

    ItemCesta(String nome, float precoBistek, float precoGiassi, float precoFort) {
        this.nome = nome;
        this.precoBistek = precoBistek;
        this.precoGiassi = precoGiassi;
        this.precoFort = precoFort;
    }

    // Retorna a descrição formatada do item com preços nos 3 supermercados
    String descricao() {
        return String.format(
                "%s | Bistek: R$ %.2f | Giassi: R$ %.2f | Fort: R$ %.2f",
                nome, precoBistek, precoGiassi, precoFort);
    }
}
