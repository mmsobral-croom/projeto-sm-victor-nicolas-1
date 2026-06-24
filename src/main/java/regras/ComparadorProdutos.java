package regras;

import entidades.ProdutoComparado;
import esd.ListaSequencial;
import esd.TabHash;
import sm.Produto;
import sm.Supermercado;

// Agrupa os resultados dos 3 supermercados e realiza as comparações por EAN
public class ComparadorProdutos {

    private final ListaSequencial<ProdutoComparado> produtos;

    // Popula a lista de produtos comparados diretamente no construtor
    public ComparadorProdutos(
            Supermercado.Resultado bistek,
            Supermercado.Resultado giassi,
            Supermercado.Resultado fort
    ) {
        this.produtos = new ListaSequencial<>();
        
        TabHash<String, Produto> hashGiassi = new TabHash<>();
        TabHash<String, Produto> hashFort = new TabHash<>();

        popula(hashGiassi, giassi.getProdutos());
        popula(hashFort, fort.getProdutos());

        ListaSequencial<Produto> listaBistek = bistek.getProdutos();
        TabHash<String, Boolean> eansAdicionados = new TabHash<>();

        // Percorre os produtos do Bistek como base de comparação
        for (int i = 0; i < listaBistek.comprimento(); i++) {
            Produto pb = listaBistek.obtem(i);
            String ean = pb.getEan();

            if (ean == null || ean.isEmpty() || !pb.isDisponivel()) {
                continue;
            }

            // Verifica se o EAN já foi processado
            if (eansAdicionados.contem(ean)) {
                continue;
            }

            // Busca rápida em O(1) no Giassi e Fort utilizando as tabelas hash locais
            if (!hashGiassi.contem(ean) || !hashFort.contem(ean)) {
                continue;
            }

            Produto pg = hashGiassi.obtem(ean);
            Produto pf = hashFort.obtem(ean);

            // Verifica se estão disponíveis em ambos os mercados
            if (!pg.isDisponivel() || !pf.isDisponivel()) {
                continue;
            }

            this.produtos.adiciona(new ProdutoComparado(
                    ean, pb.getNome(),
                    pb.getPreco(), pg.getPreco(), pf.getPreco()
            ));
            eansAdicionados.adiciona(ean, true);
        }
    }

    // Retorna produtos com mesmo EAN disponíveis nos 3 mercados já com os preços embutidos em ProdutoComparado
    public ListaSequencial<ProdutoComparado> produtosComEANNosTres() {
        return this.produtos;
    }

    private void popula(TabHash<String, Produto> tabelaHash, ListaSequencial<Produto> listaOrigem) {
        for (int i = 0; i < listaOrigem.comprimento(); i++) {
            Produto p = listaOrigem.obtem(i);
            String ean = p.getEan();
            if (ean != null && !ean.isEmpty()) {
                tabelaHash.adiciona(ean, p);
            }
        }
    }
}