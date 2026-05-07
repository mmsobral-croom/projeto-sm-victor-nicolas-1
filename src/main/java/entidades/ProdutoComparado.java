package entidades;

// Representa um produto encontrado nos 3 supermercados com seus preços
public record ProdutoComparado(String ean, String nome, float precoBistek, float precoGiassi, float precoFort) {

    // Descrição formatada com preços nos 3 supermercados
    public String descricao() {
        return String.format("%s | Bistek: R$ %.2f | Giassi: R$ %.2f | Fort: R$ %.2f",
                nome, precoBistek, precoGiassi, precoFort);
    }

    // Converte para ItemCesta ao adicionar na cesta
    public ItemCesta toItemCesta() {
        return new ItemCesta(nome, precoBistek, precoGiassi, precoFort);
    }
}