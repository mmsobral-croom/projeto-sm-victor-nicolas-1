package esd;

import java.lang.reflect.Array;

public class TabHash <K, V> {
    public class Par {
        K chave;
        V valor;

        Par(K chave, V valor) {
            this.chave = chave;
            this.valor = valor;
        }
    }

    Par[] tab;
    int len = 0; // quantos pares estao armazenados
    final int defcap = 31;
    private final Par REMOVIDO = new Par(null, null);

    public TabHash() {
        tab = inicia_tabela(defcap);
    }

    @SuppressWarnings("unchecked")
    Par[] inicia_tabela(int linhas) {
        return (Par[]) Array.newInstance(Par.class, linhas);
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
            if (p == null) {
                return -1;
            }
            if (p != REMOVIDO && p.chave.equals(chave)) {
                return i;
            }
            i = (i + 1) % tab.length;
        } while (i != h);
        return -1;
    }

    // Redimensiona a tabela quando o fator de carga limite (70%) e atingido.
    private void rehash() {
        Par[] antigo = tab;
        tab = inicia_tabela(antigo.length * 2 + 1);
        len = 0;
        for (Par p : antigo) {
            if (p != null && p != REMOVIDO) {
                adiciona(p.chave, p.valor);
            }
        }
    }

    // Adiciona um par chave-valor a tabela. Se a chave ja existir, substitui o valor.
    public void adiciona(K chave, V valor) throws IndexOutOfBoundsException {
        int index = procuraSlot(chave);
        if (index != -1) {
            tab[index].valor = valor;
            return;
        }

        if (len >= tab.length * 0.7) {
            rehash();
        }

        int h = hash(chave);
        int i = h;
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

    // Verifica se uma chave existe na tabela
    public boolean contem(K chave) {
        return procuraSlot(chave) != -1;
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
}