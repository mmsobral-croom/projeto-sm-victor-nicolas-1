package sm;

import esd.ListaSequencial;
import esd.TabHash;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CacheBuscas {
    private final String arquivo;
    private final TabHash<String, ListaSequencial<Produto>> tabela = new TabHash<>();

    public CacheBuscas(String nomeMercado) {
        this.arquivo = "cache_" + nomeMercado.toLowerCase().trim() + ".json";
        carregar();
    }

    public void carregar() {
        Path caminho = Paths.get(arquivo);
        if (!Files.exists(caminho)) {
            return;
        }
        try {
            String conteudo = Files.readString(caminho);
            if (conteudo.trim().isEmpty()) {
                return;
            }
            JSONObject json = new JSONObject(conteudo);
            for (String chave : json.keySet()) {
                JSONArray arr = json.getJSONArray(chave);
                ListaSequencial<Produto> lista = new ListaSequencial<>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject pJson = arr.getJSONObject(i);
                    Produto p = Produto.builder()
                            .nome(pJson.getString("nome"))
                            .id(pJson.getString("id"))
                            .marca(pJson.optString("marca", ""))
                            .preco(0.0f)
                            .ean(pJson.optString("ean", ""))
                            .disponivel(false)
                            .build();
                    lista.adiciona(p);
                }
                tabela.adiciona(chave, lista);
            }
        } catch (Exception e) {
            IO.println("Erro: " + e.getMessage());
        }
    }

    public void salvar() {
        try {
            JSONObject json = new JSONObject();
            ListaSequencial<String> chaves = tabela.chaves();
            for (int i = 0; i < chaves.comprimento(); i++) {
                String chave = chaves.obtem(i);
                ListaSequencial<Produto> lista = tabela.obtem(chave);
                JSONArray jsonArray = new JSONArray();
                for (int j = 0; j < lista.comprimento(); j++) {
                    Produto p = lista.obtem(j);
                    JSONObject pJson = new JSONObject();
                    pJson.put("nome", p.getNome());
                    pJson.put("id", p.getId());
                    pJson.put("marca", p.getMarca());
                    pJson.put("ean", p.getEan());
                    jsonArray.put(pJson);
                }
                json.put(chave, jsonArray);
            }
            Files.writeString(Paths.get(arquivo), json.toString(2));
        } catch (Exception e) {
            IO.println("Erro: " + e.getMessage());
        }
    }

    public ListaSequencial<Produto> obtem(String chave) {
        if (tabela.contem(chave)) {
            return tabela.obtem(chave);
        }
        return null;
    }

    public void adiciona(String chave, ListaSequencial<Produto> lista) {
        tabela.adiciona(chave, lista);
        salvar();
    }
}
