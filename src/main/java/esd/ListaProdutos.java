package esd;

import sm.Produto;
import sm.Supermercado;

public class ListaProdutos {

    private final ListaSequencial<Produto> bistek;

    private final ListaSequencial<Produto> giassi;

    private final ListaSequencial<Produto> fort;

    public ListaProdutos(Supermercado.Resultado bistek, Supermercado.Resultado giassi, Supermercado.Resultado fort) {
        this.bistek = new ListaSequencial<>();
        bistek.stream().forEach(this.bistek::adiciona);

        this.giassi = new ListaSequencial<>();
        giassi.stream().forEach(this.giassi::adiciona);

        this.fort = new ListaSequencial<>();
        fort.stream().forEach(this.fort::adiciona);
    }

    public ListaSequencial<Produto> produtosComEANNosTres() {
        ListaSequencial<Produto> produtos = new ListaSequencial<>();
        ListaSequencial<String> eansAdicionados = new ListaSequencial<>();

        for (int i = 0; i < bistek.comprimento(); i++) {
            Produto produto = bistek.obtem(i);
            String ean = produto.getEan();

            if (produto.isDisponivel()
                    && eanDisponivelEm(ean, giassi)
                    && eanDisponivelEm(ean, fort)
                    && ! eanExisteEm(ean, eansAdicionados)) {
                produtos.adiciona(produto);
                eansAdicionados.adiciona(ean);
            }
        }

        return produtos;
    }

    public String descricaoComPrecosNosTres(Produto produto) {
        Produto produtoGiassi = produtoComEAN(produto.getEan(), giassi);
        Produto produtoFort = produtoComEAN(produto.getEan(), fort);

        return String.format(
                "%s | Bistek: R$ %.2f | Giassi: R$ %.2f | Fort: R$ %.2f",
                produto.getNome(),
                produto.getPreco(),
                produtoGiassi.getPreco(),
                produtoFort.getPreco()
        );
    }

    private boolean eanExisteEm(String ean, ListaSequencial<String> eans) {
        if (ean == null || ean.isEmpty()) {
            return false;
        }

        for (int i = 0; i < eans.comprimento(); i++) {
            if (ean.equals(eans.obtem(i))) {
                return true;
            }
        }

        return false;
    }

    private boolean eanDisponivelEm(String ean, ListaSequencial<Produto> produtos) {
        if (ean == null || ean.isEmpty()) {
            return false;
        }

        for (int i = 0; i < produtos.comprimento(); i++) {
            Produto produto = produtos.obtem(i);

            if (ean.equals(produto.getEan()) && produto.isDisponivel()) {
                return true;
            }
        }

        return false;
    }

    private Produto produtoComEAN(String ean, ListaSequencial<Produto> produtos) {
        for (int i = 0; i < produtos.comprimento(); i++) {
            Produto produto = produtos.obtem(i);

            if (ean.equals(produto.getEan())) {
                return produto;
            }
        }

        throw new IllegalArgumentException("EAN nao encontrado: " + ean);
    }
}
