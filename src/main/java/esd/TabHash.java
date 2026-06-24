package esd;

import java.lang.reflect.Array;
import java.util.Objects;

public class TabHash <K, V> {
    public class Par {
        K chave;
        V valor;

        Par(K chave, V valor) {
            this.chave = chave;
            this.valor = valor;
        }

        public K obtemChave() {
            return chave;
        }

        public V obtemValor() {
            return valor;
        }

        @Override
        public int hashCode() {
            return Objects.hash(chave, valor);
        }

        @Override
        public boolean equals(Object outro) {
            Par _outro = (Par)outro;
            return chave.equals(_outro.chave);
        }
    }

    Par[] tab;
    int len = 0; // quantos pares estao armazenados
    final int defcap = 31;
    private final Par REMOVIDO = new Par(null, null);

    public TabHash() {
        // dimensiona a tabela
        tab = inicia_tabela(defcap);
    }

    @SuppressWarnings("unchecked")
    Par[] inicia_tabela(int linhas) {
        Par[] nova = (Par[]) Array.newInstance(Par.class, linhas);

        // inicia a lista com essa quantidade de linhas
        return nova;
    }

    // Calcula o valor de hash para a chave fornecida, garantindo que o indice seja positivo.
    private int hash(K chave) {
        return Math.abs(chave.hashCode()) % tab.length;
    }

    // Busca linear por um slot correspondente a chave dada.
    // Retorna o indice do slot se a chave for encontrada, ou -1 caso contrario.
    private int procuraSlot(K chave) {
        int h = hash(chave);
        int i = h;
        do {
            Par p = tab[i];
            // Se encontrar uma posicao vazia (null), a chave com certeza nao esta na tabela
            if (p == null) {
                return -1;
            }
            // Se a posicao nao for nula e nao for uma marcacao de remoção
            // e as chaves forem iguais, encontramos o slot correto.
            if (p != REMOVIDO && p.chave.equals(chave)) {
                return i;
            }
            // move para o proximo slot de forma circular
            i = (i + 1) % tab.length;
        } while (i != h); // Evita loop infinito se a tabela inteira for percorrida
        return -1;
    }

    // Redimensiona a tabela quando o fator de carga limite (70%) e atingido.
    // Cria uma nova tabela maior e reinsere todos os elementos ativos.
    private void rehash() {
        Par[] antigo = tab;
        // Nova capacidade de tamanho impar para ajudar na distribuicao de chaves
        tab = inicia_tabela(antigo.length * 2 + 1);
        len = 0; // O contador de elementos e reiniciado e reincrementado no 'adiciona'
        for (Par p : antigo) {
            // Reinsere apenas chaves validas, descartando slots vazios e marcas de remocao (tombstones)
            if (p != null && p != REMOVIDO) {
                adiciona(p.chave, p.valor);
            }
        }
    }

    // Adiciona um par chave-valor a tabela. Se a chave ja existir, substitui o valor.
    public void adiciona(K chave, V valor) throws IndexOutOfBoundsException {
        // Se a chave ja existe, apenas atualiza o seu valor associado
        int index = procuraSlot(chave);
        if (index != -1) {
            tab[index].valor = valor;
            return;
        }

        // Se o numero de elementos atingir 70% da capacidade, redimensiona a tabela
        if (len >= tab.length * 0.7) {
            rehash();
        }

        int h = hash(chave);
        int i = h;
        // Procura a primeira posicao disponivel (nula ou marcada como removida)
        while (tab[i] != null && tab[i] != REMOVIDO) {
            i = (i + 1) % tab.length;
        }

        tab[i] = new Par(chave, valor);
        len++;
    }

    // Obtem o valor associado a uma chave. Lanca excecao se nao existir.
    public V obtem(K chave) {
        int index = procuraSlot(chave);
        if (index == -1) {
            throw new IndexOutOfBoundsException("chave inexistente");
        }
        return tab[index].valor;
    }

    // Remove um elemento da tabela marcando seu slot com a constante REMOVIDO (tombstone).
    // Isso mantem o fluxo de busca linear intacto para colisoes que foram inseridas depois.
    public void remove(K chave) {
        int index = procuraSlot(chave);
        if (index == -1) {
            throw new IndexOutOfBoundsException("chave inexistente");
        }
        tab[index] = REMOVIDO;
        len--;
    }

    // Verifica se uma chave existe na tabela
    public boolean contem(K chave) {
        return procuraSlot(chave) != -1;
    }

    // Retorna se a tabela esta vazia
    public boolean esta_vazia() {
        return len == 0;
    }

    // Obtem o valor associado a chave, ou retorna um valor padrao se a chave nao existir.
    public V obtem_ou_default(K chave, V defval) {
        int index = procuraSlot(chave);
        if (index == -1) {
            return defval;
        }
        return tab[index].valor;
    }

    // Retorna uma lista sequencial contendo todas as chaves ativas da tabela
    public ListaSequencial<K> chaves() {
        ListaSequencial<K> lk = new ListaSequencial<>();
        for (Par p : tab) {
            if (p != null && p != REMOVIDO) {
                lk.adiciona(p.chave);
            }
        }
        return lk;
    }

    // Retorna uma lista sequencial contendo todos os valores ativos da tabela
    public ListaSequencial<V> valores() {
        ListaSequencial<V> lv = new ListaSequencial<>();
        for (Par p : tab) {
            if (p != null && p != REMOVIDO) {
                lv.adiciona(p.valor);
            }
        }
        return lv;
    }

    // Retorna uma lista sequencial contendo todos os pares (itens) ativos da tabela
    public ListaSequencial<Par> items() {
        ListaSequencial<Par> lp = new ListaSequencial<>();
        for (Par p : tab) {
            if (p != null && p != REMOVIDO) {
                lp.adiciona(p);
            }
        }
        return lp;
    }

    // Retorna a quantidade de pares armazenados
    public int comprimento() {
        return len;
    }

    // Limpa a tabela reinicializando com o tamanho default e limpando as referencias
    public void limpa() {
        tab = inicia_tabela(defcap);
        len = 0;
    }

    // Primeiro Caractere Único: Encontra o primeiro caractere que não se repete na palavra e retorna seu índice
    public static int primeiroCaractereUnico(String s) {
        if (s == null || s.isEmpty()) {
            return -1;
        }

        TabHash<Character, Integer> frequencias = new TabHash<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int count = frequencias.obtem_ou_default(c, 0);
            frequencias.adiciona(c, count + 1);
        }

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (frequencias.obtem(c) == 1) {
                return i;
            }
        }

        return -1;
    }

    public int primeiro_caractere_unico(String s) {
        return primeiroCaractereUnico(s);
    }

    public static <E> ListaSequencial<E> intersecao(ListaSequencial<E> l1, ListaSequencial<E> l2) {
        if (l1 == null || l2 == null || l1.esta_vazia() || l2.esta_vazia()) {
            return new ListaSequencial<>();
        }

        TabHash<E, Boolean> visto = new TabHash<>();
        for (int i = 0; i < l1.comprimento(); i++) {
            visto.adiciona(l1.obtem(i), true);
        }

        TabHash<E, Boolean> interseccao = new TabHash<>();
        for (int i = 0; i < l2.comprimento(); i++) {
            E val = l2.obtem(i);
            if (visto.contem(val)) {
                interseccao.adiciona(val, true);
            }
        }

        return interseccao.chaves();
    }
}