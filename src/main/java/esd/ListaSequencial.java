package esd;


import java.util.Objects;
import java.util.Random;

public class ListaSequencial<T> {

    T[] area;
    int len = 0;
    final int defcap = 8;

    @SuppressWarnings("unchecked")
    public ListaSequencial() {
        area = (T[]) new Object[defcap];
    }

    @SuppressWarnings("unchecked")
    void expande() {
        int novacapacidade = area.length * 2;
        T[] novaArea = (T[]) new Object[novacapacidade];

        for (int i = 0; i < len; i++) {
            novaArea[i] = area[i];
        }

        area = novaArea;

        // expande a capacidade da lista: nova capacidade deve ser o dobro da atual
    }

    public boolean esta_vazia() {
        // retorna true se lista estiver vazia, ou false caso contrário
        return len == 0;
    }

    public int capacidade() {
        // retorna um inteiro que representa a capacidade da lista
        return area.length;
    }

    public void adiciona(T elemento) {
        // adiciona um valor ao final da lista
        if (len == area.length) {
            expande();
        }

        area[len++] = elemento;
    }

    public void insere(int indice, T elemento) {
        // insere um valor na posição indicada por "indice"
        // move uma posição para frente os valores a partir dessa posição
        // dispara IndexOutOfBoundsException se "indice" for inválido
        if (indice > len || indice < 0) {
            throw new IndexOutOfBoundsException("Posição inválida");
        }
        if (len == area.length) {
            expande();
        }
        for (int i = len; i > indice; i--) {
            area[i] = area[i - 1];
        }
        area[indice] = elemento;
        len++;
    }

    public void insere_rapido(int indice, T elemento) {
        // insere um valor na posição indicada por "indice"
        // usa a abordagem de ListaSequencialSimples
        // dispara IndexOutOfBoundsException se "indice" for inválido
        if (indice >= len || indice < 0) {
            throw new IndexOutOfBoundsException("Posição inválida");
        }
        if (len == area.length) {
            expande();
        }
        for (int i = len; i > indice; i--) {
            area[i] = area[i - 1];
        }
        area[indice] = elemento;
        len++;
    }

    public T remove(int indice) {
        // remove um valor da posição indicada pelo parâmetro "indice"
        // move uma posição para trás os valores das próximas posições
        // disparar uma exceção IndexOutOfBoundsException caso posição seja inválida
        // retorna o valor que foi removido da lista
        if (indice >= len || indice < 0) {
            throw new IndexOutOfBoundsException("Posição inválida");
        }

        T valorRemovido = area[indice];

        for (int i = indice; i < len - 1; i++) {
            area[i] = area[i + 1];
        }
        area[len - 1] = null;
        len--;
        return valorRemovido;
    }

    public T remove_rapido(int indice) {
        // remove um valor da posição indica pelo parãmetro índice
        // move o último dado da lista para essa posição
        // dispara IndexOutOfBoundsException se indice for inválido
        // retorna o valor que ofi removido da lista
        if (indice >= len || indice < 0) {
            throw new IndexOutOfBoundsException("Posição inválida");
        }
        T valorRemovido = area[indice];
        len--;

        if (indice < len) {
            area[indice] = area[len];
        }

        area[len] = null;
        return valorRemovido;
    }

    public T remove_ultimo() {
        // remove o último valor da lista
        // disparar uma exceção IndexOutOfBoundsException caso lista vazia
        // retorna o valor que foi removido da lista
        if (len == 0) {
            throw new IndexOutOfBoundsException("Lista vazia");
        }

        T valorRemovido = area[len - 1];
        area[len - 1] = null;
        len--;
        return valorRemovido;
    }

    public int procura(T valor) {
        // retorna um inteiro que representa aposição onde valor foi encontrado pela primeira vez (contando do início da lista)
        // retorna -1 se não o encontrar !
        for (int i = 0; i < len; i++) {
            if (Objects.equals(area[i], valor)) {
                return i;
            }
        }
        return -1;
    }

    public T obtem(int indice) {
        // retorna o valor armazenado na posição indica pelo parâmetro "indice"
        // disparar uma exceção IndexOutOfBoundsException caso posição seja inválida
        if (indice >= len || indice < 0) {
            throw new IndexOutOfBoundsException("Posição inválida");
        }

        return area[indice];
    }

    public T primeiro() {
        // retorna o valor armazenado no início da lista
        // disparar uma exceção IndexOutOfBoundsException caso posição seja
        if (len == 0) {
            throw new IndexOutOfBoundsException("Lista vazia");
        }
        return area[0];
    }

    public T ultimo() {
        // retorna o valor armazenado no final da lista
        // disparar uma exceção IndexOutOfBoundsException caso posição seja inválida
        if (len == 0) {
            throw new IndexOutOfBoundsException("Lista vazia");
        }

        return area[len - 1];
    }

    public void substitui(int indice, T valor) {
        // armazena o valor na posição indicada por "indice", substituindo o valor lá armazenado atualmente
        // disparar uma exceção IndexOutOfBoundsException caso posição seja inválida
        if (indice >= len || indice < 0) {
            throw new IndexOutOfBoundsException("Posição inválida");
        }
        area[indice] = valor;
    }

    public int comprimento() {
        // retorna um inteiro que representa o comprimento da lista (quantos valores estão armazenados)
        return len;
    }

    public void limpa() {
        // esvazia a lista
        for (int i = 0; i < len; i++) {
            area[i] = null;
        }
        len = 0;
    }

    public boolean remove(T valor) {
        int pos = procura(valor);

        if (pos == -1) {
            return false;
        }

        remove(pos);
        return true;
    }

    public void insere_ordenado(Comparable valor) {
        if (len == area.length) {
            expande();
        }

        int i = 0;

        while (i < len && ((Comparable) area[i]).compareTo(valor) < 0) {
            i++;
        }

        for (int j = len; j > i; j--) {
            area[j] = area[j - 1];
        }

        area[i] = (T) valor;
        len++;
    }

    public int busca_binaria(Comparable valor) {

        int esquerda = 0;
        int direita = len - 1;

        while (esquerda <= direita) {
            int meio = (esquerda + direita) / 2;
            int comparacao = ((Comparable) area[meio]).compareTo(valor);

            if (comparacao == 0) {
                return meio;
            } else if (comparacao < 0) {
                esquerda = meio + 1;
            } else {
                direita = meio - 1;
            }
        }

        return -1;
    }

    public void ordena() {
        for (int i = 1; i < len; i++) {
            T chave = area[i];
            int j = i - 1;

            while (j >= 0 && ((Comparable) area[j]).compareTo(chave) > 0) {
                area[j + 1] = area[j];
                j--;
            }

            area[j + 1] = chave;
        }
    }

    public boolean esta_ordenada() {
        boolean certo = true;

        if (len > 1) {
            for (int i = 1; certo && i < len; i++) {
                Comparable val = (Comparable) area[i - 1];
                certo = val.compareTo(area[i]) <= 0;
            }
        }
        return certo;
    }

    public void embaralha() {
        if (len <= 1) return;
        Random rng = new Random();
        for (int i = len - 1; i > 0; i--) {
            int j = rng.nextInt(0, i + 1); // [0, i]
            T temp = area[i];
            area[i] = area[j];
            area[j] = temp;
        }
    }
}