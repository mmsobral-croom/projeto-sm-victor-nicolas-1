package regras;

import entidades.ProdutoComparado;
import esd.ListaSequencial;
import sm.Produto;
import sm.Supermercado;

// Agrupa os resultados dos 3 supermercados e realiza as comparações por EAN
public class ComparadorProdutos {

    private final ListaSequencial<Produto> bistek;
    private final ListaSequencial<Produto> giassi;
    private final ListaSequencial<Produto> fort;

    // Converte os resultados da busca para listas sequenciais
    public ComparadorProdutos(
            Supermercado.Resultado bistek,
            Supermercado.Resultado giassi,
            Supermercado.Resultado fort
    ) {
        this.bistek = new ListaSequencial<>();
        bistek.stream().forEach(this.bistek::adiciona);

        this.giassi = new ListaSequencial<>();
        giassi.stream().forEach(this.giassi::adiciona);

        this.fort = new ListaSequencial<>();
        fort.stream().forEach(this.fort::adiciona);
    }

    // Retorna produtos com mesmo EAN disponíveis nos 3 mercados já com os preços embutidos em ProdutoComparado
    public ListaSequencial<ProdutoComparado> produtosComEANNosTres() {
        ListaSequencial<ProdutoComparado> produtos = new ListaSequencial<>();
        ListaSequencial<String> eansAdicionados = new ListaSequencial<>();

        // Percorre os produtos do Bistek como base de comparação
        for (int i = 0; i < bistek.comprimento(); i++) {
            Produto pb = bistek.obtem(i);
            String ean = pb.getEan();

            // Verifica se o produto existe e está disponível nos 3 mercados
            if (!pb.isDisponivel() || eanDisponivelEm(ean, giassi) || eanDisponivelEm(ean, fort) || eanExisteEm(ean, eansAdicionados)) continue;

            // Busca o mesmo produto nos outros mercados para obter os preços
            Produto pg = produtoComEAN(ean, giassi);
            Produto pf = produtoComEAN(ean, fort);

            produtos.adiciona(new ProdutoComparado(
                    ean, pb.getNome(),
                    pb.getPreco(), pg.getPreco(), pf.getPreco()
            ));
            eansAdicionados.adiciona(ean);
        }

        return produtos;
    }

    // Verifica se um EAN já foi adicionado na lista de controle
    private boolean eanExisteEm(String ean, ListaSequencial<String> eans) {
        if (ean == null || ean.isEmpty()) return false;
        for (int i = 0; i < eans.comprimento(); i++)
            if (ean.equals(eans.obtem(i))) return true;
        return false;
    }

    // Verifica se um produto com o EAN existe e está disponível na lista
    private boolean eanDisponivelEm(String ean, ListaSequencial<Produto> produtos) {
        if (ean == null || ean.isEmpty()) return true;
        for (int i = 0; i < produtos.comprimento(); i++) {
            Produto p = produtos.obtem(i);
            if (ean.equals(p.getEan()) && p.isDisponivel()) return false;
        }
        return true;
    }

    // Busca e retorna um produto pelo EAN na lista informada
    private Produto produtoComEAN(String ean, ListaSequencial<Produto> produtos) {
        for (int i = 0; i < produtos.comprimento(); i++) {
            Produto p = produtos.obtem(i);
            if (ean.equals(p.getEan())) return p;
        }

        throw new IllegalArgumentException("EAN não encontrado: " + ean);
    }
}