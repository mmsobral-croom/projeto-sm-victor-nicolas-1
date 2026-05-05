import esd.ListaProdutos;
import esd.ListaSequencial;
import sm.*;

import java.util.Scanner;

    void main() {
        Scanner teclado = new Scanner(System.in);

        Giassi giassi = new Giassi();
        Bistek bistek = new Bistek();
        Fort fort = new Fort();

        ListaSequencial<String> cesta = new ListaSequencial<>();
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

                ListaProdutos listaProdutos = new ListaProdutos(resultadosBistek, resultadosGiassi, resultadosFort);
                ListaSequencial<Produto> produtosNosTres = listaProdutos.produtosComEANNosTres();

                mostraMenuProdutos(produtosNosTres, listaProdutos);

                if (! produtosNosTres.esta_vazia()) {
                    IO.print("Escolha uma opção: ");
                    int opcao = teclado.nextInt();
                    teclado.nextLine();

                    if (opcao < 1 || opcao > produtosNosTres.comprimento()) {
                        IO.println("Opção inválida.");
                    } else {
                        Produto produtoEscolhido = produtosNosTres.obtem(opcao - 1);
                        String descricaoProduto = listaProdutos.descricaoComPrecosNosTres(produtoEscolhido);
                        cesta.adiciona(descricaoProduto);
                        IO.println("Produto adicionado na cesta:");
                        IO.println(descricaoProduto);
                    }
                }
            }

            IO.print("Deseja adicionar mais produtos? (s/n): ");
            continuar = teclado.nextLine().trim();
        }

        mostraCesta(cesta);
    }

    static Supermercado.Resultado procura(Supermercado supermercado, String nome) {
        return supermercado.busca(nome);
    }

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

    static void mostraCesta(ListaSequencial<String> cesta) {
        IO.println();
        IO.println("Cesta:");

        if (cesta.esta_vazia()) {
            IO.println("Nenhum produto adicionado.");
            return;
        }

        for (int i = 0; i < cesta.comprimento(); i++) {
            IO.println((i + 1) + " - " + cesta.obtem(i));
        }
    }
