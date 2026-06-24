package entidades;

import esd.ListaSequencial;

// Representa a posição de um supermercado no ranking de preço total da cesta
public record RankingCesta(String nomeSupermercado, float total) implements Comparable<RankingCesta> {

    @Override
    public int compareTo(RankingCesta outro) {
        return Float.compare(this.total, outro.total);
    }

    // Descrição formatada para exibição no ranking
    public String descricao(int posicao) {
        return String.format("%dº %s: R$ %.2f", posicao, nomeSupermercado, total);
    }

    // Calcula e ordena o ranking dos 3 supermercados pelo preço total da cesta
    public static ListaSequencial<RankingCesta> calcular(ListaSequencial<ItemCesta> cesta) {
        // Acumula o total de cada supermercado somando os preços de todos os itens
        float totalBistek = 0, totalGiassi = 0, totalFort = 0;

        for (int i = 0; i < cesta.comprimento(); i++) {
            ItemCesta item = cesta.obtem(i);
            totalBistek += item.getPrecoBistek();
            totalGiassi += item.getPrecoGiassi();
            totalFort += item.getPrecoFort();
        }

        ListaSequencial<RankingCesta> resultado = new ListaSequencial<>();
        resultado.adiciona(new RankingCesta("Bistek", totalBistek));
        resultado.adiciona(new RankingCesta("Giassi", totalGiassi));
        resultado.adiciona(new RankingCesta("Fort Atacadista", totalFort));

        // Ordena pelo preço total usando o método ordena de ListaSequencial
        resultado.ordena();

        return resultado;
    }
}