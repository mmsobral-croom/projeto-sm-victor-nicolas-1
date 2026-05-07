package entidades;

import esd.ListaSequencial;

// Representa a posição de um supermercado no ranking de preço total da cesta
public record RankingCesta(String nomeSupermercado, float total) {

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

        // Cria array com os 3 supermercados para ordenar juntos
        RankingCesta[] ranking = {
                new RankingCesta("Bistek", totalBistek),
                new RankingCesta("Giassi", totalGiassi),
                new RankingCesta("Fort Atacadista", totalFort)
        };

        // Ordena pelo preço total (bubble sort – só 3 elementos)
        for (int i = 0; i < ranking.length - 1; i++) {
            for (int j = 0; j < ranking.length - 1 - i; j++) {
                if (ranking[j].total() > ranking[j + 1].total()) {
                    RankingCesta tmp = ranking[j];
                    ranking[j] = ranking[j + 1];
                    ranking[j + 1] = tmp;
                }
            }
        }

        // Converte para ListaSequencial
        ListaSequencial<RankingCesta> resultado = new ListaSequencial<>();
        for (RankingCesta r : ranking) resultado.adiciona(r);
        return resultado;
    }
}