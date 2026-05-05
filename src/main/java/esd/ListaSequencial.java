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

    public int capacidade() {
        return area.length;
    }

    // adiciona um valor ao final da lista
    public void adiciona(T elemento) {
        if (len == capacidade()) {
            expande();
        }

        area[len++] = elemento;
    }

    // insere "elemento" na posição "indice"
    // o valor que estava na posição "indice" deve ser movido para o final da lista
    // valores válidos de "indice" vão de 0 até comprimento da lista (inclusive)
    // se "indice" for o comprimento da lista, insere faz o mesmo que "adiciona"
    // dispara IndexOutOfBoundsException se "indice" for inválido
    public void insere_rapido(int indice, T elemento) {
        if (indice < 0 || indice > len) {
            throw new IndexOutOfBoundsException();
        }

        if (len == capacidade()) {
            expande();
        }

        if (indice < len) {
            area[len] = area[indice];
        }

        area[indice] = elemento;
        len++;
    }

    // remove um valor da posição indicada pelo parâmetro "indice"
    // move para essa posição o valor que está no final da lista
    // disparar uma exceção IndexOutOfBoundsException caso posição seja inválida
    public void remove_rapido(int indice) {
        if (indice < 0 || indice >= len) {
            throw new IndexOutOfBoundsException();
        }

        area[indice] = area[len - 1];
        area[len - 1] = null;
        len--;
    }

    // insere um valor na posição indicada por "indice"
    // move uma posição para frente os valores a partir dessa posição
    // dispara IndexOutOfBoundsException se "indice" for inválido
    public void insere(int indice, T elemento) {
        if (indice < 0 || indice > len) {
            throw new IndexOutOfBoundsException();
        }

        if (len == capacidade()) {
            expande();
        }

        for (int i = len; i > indice; i--) {
            area[i] = area[i - 1];
        }

        area[indice] = elemento;
        len++;
    }

    // remove um valor da posição indicada pelo parâmetro "indice"
    // move uma posição para trás os valores das próximas posições
    // disparar uma exceção IndexOutOfBoundsException caso posição seja inválida
    // retorna o valor que foi removido da lista
    public T remove(int indice) {
        if (indice < 0 || indice >= len) {
            throw new IndexOutOfBoundsException();
        }

        T valor = area[indice];

        for (int i = indice; i < len - 1; i++) {
            area[i] = area[i + 1];
        }

        area[len - 1] = null;
        len--;

        return valor;
    }

    // remove o último valor da lista
    // disparar uma exceção IndexOutOfBoundsException caso lista vazia
    // retorna o valor que foi removido da lista
    public T remove_ultimo() {
        if (esta_vazia()) throw new IndexOutOfBoundsException();

        T valor = area[len - 1];
        area[len - 1] = null;
        len--;
        return valor;
    }

    // retorna o valor armazenado no início da lista
    // disparar uma exceção IndexOutOfBoundsException caso posição seja inválida
    public T primeiro() {
        if (esta_vazia()) throw new IndexOutOfBoundsException();

        return area[0];
    }

    // retorna o valor armazenado no final da lista
    // disparar uma exceção IndexOutOfBoundsException caso posição seja inválida
    public T ultimo() {
        if (esta_vazia()) throw new IndexOutOfBoundsException();

        return area[len - 1];
    }

    // retorna um inteiro que representa a posição onde valor foi encontrado pela primeira vez (contando do início da lista)
    // retorna -1 se não o encontrar !
    public int procura(T valor) {
        for (int i = 0; i < len; i++) {
            if (area[i] != null && area[i].equals(valor)) return i;
            if (area[i] == null && valor == null) return i;
        }

        return -1;
    }

    // retorna o valor armazenado na posição indica pelo parâmetro "indice"
    // disparar uma exceção IndexOutOfBoundsException caso posição seja inválida
    public T obtem(int indice) {
        if (indice < 0 || indice >= len) {
            throw new IndexOutOfBoundsException();
        }

        return area[indice];
    }

    // armazena o valor na posição indicada por "indice", substituindo o valor lá armazenado atualmente
    // disparar uma exceção IndexOutOfBoundsException caso posição seja inválida
    public void substitui(int indice, T valor) {
        if (indice < 0 || indice >= len) {
            throw new IndexOutOfBoundsException();
        }

        area[indice] = valor;
    }

    public int comprimento() {
        return len;
    }

    @SuppressWarnings("unchecked")
    public void limpa() {
        area = (T[])new Object[defcap];
        len = 0;
    }
}