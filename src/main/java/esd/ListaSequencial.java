package esd;

public class ListaSequencial<T> {

    T[] area;
    int len = 0;
    int defcap = 8;

    @SuppressWarnings("unchecked")
    public ListaSequencial() {
        area = (T[])new Object[defcap];
    }

    // isto será usado quando for necessário expandir a capacidade da lista
    @SuppressWarnings("unchecked")
    void expande() {
        defcap = defcap * 2;
        T[] novoArea = (T[])new Object[defcap];

        int contador = 0;
        for (T item : area) {
            novoArea[contador] = item;
            contador++;
        }

        area = novoArea;
    }

    public boolean esta_vazia() {
        return len == 0;
    }

    // adiciona um valor ao final da lista
    public void adiciona(T elemento) {
        if (len == area.length) {
            expande();
        }

        area[len++] = elemento;
    }

    // retorna o valor armazenado na posição indica pelo parâmetro "indice"
    // disparar uma exceção IndexOutOfBoundsException caso posição seja inválida
    public T obtem(int indice) {
        if (indice < 0 || indice >= len) {
            throw new IndexOutOfBoundsException();
        }

        return area[indice];
    }

    public int comprimento() {
        return len;
    }

    // ordena a lista usando bubble sort
    @SuppressWarnings("unchecked")
    public void ordena() {
        for (int i = 0; i < len - 1; i++) {
            for (int j = 0; j < len - 1 - i; j++) {
                Comparable<T> c1 = (Comparable<T>) area[j];
                if (c1.compareTo(area[j + 1]) > 0) {
                    T tmp = area[j];
                    area[j] = area[j + 1];
                    area[j + 1] = tmp;
                }
            }
        }
    }
}