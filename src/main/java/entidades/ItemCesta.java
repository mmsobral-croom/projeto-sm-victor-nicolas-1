package entidades;

import lombok.Getter;
import lombok.Setter;

// Representa um item da cesta com o nome e o preço em cada supermercado
@Setter
@Getter
public class ItemCesta {
    private String nome;
    private float precoBistek;
    private float precoGiassi;
    private float precoFort;

    public ItemCesta(String nome, float precoBistek, float precoGiassi, float precoFort) {
        this.nome = nome;
        this.precoBistek = precoBistek;
        this.precoGiassi = precoGiassi;
        this.precoFort = precoFort;
    }

    // Retorna a descrição formatada do item com preços nos 3 supermercados
    public String descricao() {
        return String.format(
                "%s | Bistek: R$ %.2f | Giassi: R$ %.2f | Fort: R$ %.2f",
                nome, precoBistek, precoGiassi, precoFort
        );
    }
}
