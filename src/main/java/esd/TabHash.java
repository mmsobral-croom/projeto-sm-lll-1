package esd;

public class TabHash<K, V> {
    public class Par {
        private K chave;
        private V valor;

        Par(K chave, V valor) {
            this.chave = chave;
            this.valor = valor;
        }

        public K obtemChave() { return chave; }
        public V obtemValor() { return valor; }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object outro) {
            Par _outro = (Par) outro;
            return chave.equals(_outro.chave);
        }
    }

    ListaSequencial<Par>[] tab;
    int len = 0;
    final int defcap = 31;

    public TabHash() {
        tab = inicia_tabela(defcap);
    }

    private Par obtemParLista(ListaSequencial<Par> lista, K chave) {
        for (int pos = 0; pos < lista.comprimento(); pos++) {
            Par p = lista.obtem(pos);
            if (chave.equals(p.chave)) {
                return p;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private ListaSequencial<Par>[] inicia_tabela(int tamanho) {
        ListaSequencial<Par>[] nova = new ListaSequencial[tamanho];
        for (int i = 0; i < tamanho; i++) {
            nova[i] = new ListaSequencial<Par>();
        }
        return nova;
    }

    public void adiciona(K chave, V valor) {
        int linha = Math.abs(chave.hashCode()) % tab.length;
        ListaSequencial<Par> pares = tab[linha];

        Par parAchado = obtemParLista(pares, chave);
        if (parAchado != null) {
            parAchado.valor = valor;
            return;
        }

        pares.adiciona(new Par(chave, valor));
        len++;
    }

    public V obtem(K chave) {
        int linha = Math.abs(chave.hashCode()) % tab.length;
        ListaSequencial<Par> pares = tab[linha];

        Par parAchado = obtemParLista(pares, chave);
        if (parAchado != null) return parAchado.valor;

        throw new IndexOutOfBoundsException("Não tem nada com essa chave");
    }

    public void remove(K chave) {
        int linha = Math.abs(chave.hashCode()) % tab.length;
        ListaSequencial<Par> pares = tab[linha];


        for (int pos = 0; pos < pares.comprimento(); pos++) {
            Par p = pares.obtem(pos);
            if (chave.equals(p.chave)) {
                pares.remove(p);
                len--;
                return;
            }
        }

        throw new IndexOutOfBoundsException("Não tem nada com essa chave");
    }

    public boolean contem(K chave) {
        int linha = Math.abs(chave.hashCode()) % tab.length;
        ListaSequencial<Par> pares = tab[linha];

        return obtemParLista(pares, chave) != null;
    }

    public boolean esta_vazia() {
        return len == 0;
    }

    public V obtem_ou_default(K chave, V defval) {
        try {
            return obtem(chave);
        } catch (IndexOutOfBoundsException e) {
            return defval;
        }
    }

    public ListaSequencial<K> chaves() {
        ListaSequencial<K> lk = new ListaSequencial<>();
        for (ListaSequencial<Par> lista : tab) {
            if (lista == null) continue;
            for (int i = 0; i < lista.comprimento(); i++) {
                Par p = lista.obtem(i);
                if (p != null) lk.adiciona(p.chave);
            }
        }
        return lk;
    }

    public ListaSequencial<V> valores() {
        ListaSequencial<V> lv = new ListaSequencial<>();
        for (ListaSequencial<Par> lista : tab) {
            if (lista == null) continue;
            for (int i = 0; i < lista.comprimento(); i++) {
                Par p = lista.obtem(i);
                if (p != null) lv.adiciona(p.valor);
            }
        }
        return lv;
    }

    public ListaSequencial<Par> items() {
        ListaSequencial<Par> lp = new ListaSequencial<>();
        for (ListaSequencial<Par> lista : tab) {
            if (lista == null) continue;
            for (int i = 0; i < lista.comprimento(); i++) {
                Par p = lista.obtem(i);
                if (p != null) lp.adiciona(p);
            }
        }
        return lp;
    }

    public int comprimento() { return len; }

    public void limpaTabela() {
        len = 0;
        tab = inicia_tabela(tab.length); // FIX 7: reinitialise buckets instead of nulling them
    }

    // expande a tabela hash, de forma que dobre a quantidade de linhas
    // os pares deve ser redistribuídos na tabela (rehashing)
    void expande() {
        ListaSequencial<Par>[] velha = tab;
        tab = inicia_tabela( velha.length * 2);
        len = 0;


        for (ListaSequencial<Par> lista : velha) {
            if (lista.esta_vazia()) continue;

            for (int i = 0; i < lista.comprimento(); i++) {
                Par p = lista.obtem(i);
                adiciona(p.chave, p.valor);
            }
        }
    }
}