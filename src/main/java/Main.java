import cache.CacheManager;
import esd.ListaSequencial;
import sm.*;
import esd.TabHash;

import java.util.Scanner;

public class Main {
    static Giassi giassi = new Giassi();
    static Fort fort = new Fort();
    static Bistek bistek = new Bistek();
    static ListaSequencial<Produto>[] arrayListas;
    static String[] nomesMercados = {"Giassi", "Fort", "Bistek"};

    static TabHash<String, ListaSequencial<Produto>> cacheGiassi = CacheManager.carregar("src/main/java/cache/cache_giassi.json");
    static TabHash<String, ListaSequencial<Produto>> cacheFort = CacheManager.carregar("src/main/java/cache/cache_fort.json");
    static TabHash<String, ListaSequencial<Produto>> cacheBistek = CacheManager.carregar("src/main/java/cache/cache_bistek.json");

    TabHash<String, Boolean> adicionados = new TabHash<>();

    public static void main(String[] args) {
        Scanner inp = new Scanner(System.in);
        ListaSequencial<String> listaNomeProdutos = new ListaSequencial<>();
        boolean continuar = true;

        while (continuar) {
            IO.print("> ");
            String cmd = inp.nextLine();
            cmd = cmd.strip();
            switch (cmd) {
                case "" -> {
                }
                case "sair" -> { // sair sem executar nada e zerando os produtos
                    continuar = false;
                    listaNomeProdutos.limpa();
                }
                case "?" -> { // Retira último produto inserido
                    if (!listaNomeProdutos.esta_vazia()) {
                        IO.println(listaNomeProdutos.remove_ultimo());
                    }
                }
                case "produtos" -> { // lista todos os produtos
                    listaNomeProdutos.listarElementos();
                }
                case "calcular" -> { // calcula o mercado com menor preço de produtos
                    arrayListas = new ListaSequencial[3];
                    String[] nomesOrdenados = nomesMercados.clone();

                    criarListasSupermercados(listaNomeProdutos);
                    ordenarPorPreco(arrayListas, nomesOrdenados);
                    mostrarOrdenado(arrayListas, nomesOrdenados);

                    continuar = false;
                }
                default -> {
                    listaNomeProdutos.adiciona(cmd);
                }
            }
        }

        //depois de tudo salva no cache json o conteudo das TabHash
        CacheManager.salvar("src/main/java/cache/cache_giassi.json",  cacheGiassi);
        CacheManager.salvar("src/main/java/cache/cache_fort.json",    cacheFort);
        CacheManager.salvar("src/main/java/cache/cache_bistek.json",  cacheBistek);
    }

    private static ListaSequencial<Produto> criarCestaCompra(Supermercado mercado, ListaSequencial<String> listaNomeProdutos, TabHash<String, ListaSequencial<Produto>> cache) {
        ListaSequencial<Produto> cestaCompra = new ListaSequencial<>();

        for (int i = 0; i < listaNomeProdutos.comprimento(); i++) {
            String nomeProduto = listaNomeProdutos.obtem(i);
            String[] termos = obterTermos(nomeProduto); //separa os termos da busca: "chocolate branco" vira "chocolate" e "branco"
            ListaSequencial<Produto> produtosEncontrados = null;
            for (String termo : termos) { //busca no cache ou api cada termo da busca
                ListaSequencial<Produto> listaTermo;
                if (cache.contem(termo)) { //verifica no cache se existe senao busca na API
                    listaTermo = cache.obtem(termo);
                } else {
                    Supermercado.Resultado resultado = mercado.busca(termo);
                    if (resultado == null) {
                        continue;
                    }
                    listaTermo = new ListaSequencial<>();
                    TabHash<String, Boolean> ids = new TabHash<>();
                    for (Produto p : resultado) {
                        if (!ids.contem(p.getId())) { //verifica se o id nao é igual
                            ids.adiciona(p.getId(), true);
                            listaTermo.adiciona(p);
                        }
                    }
                    cache.adiciona(termo, listaTermo); //adiciona para o cache os itens encontrados
                }
                if (produtosEncontrados == null) {
                    produtosEncontrados = listaTermo;
                } else {
                    produtosEncontrados = intersecao(produtosEncontrados, listaTermo);//faz a intersecao dos termos de busca
                }
            }

            Produto maisBarato = null; //parte de produto mais barato nao foi alterada
            for (int j = 0; j < produtosEncontrados.comprimento(); j++) {
                Produto atual = produtosEncontrados.obtem(j);
                if (!atual.isDisponivel()) continue;
                if (maisBarato == null || atual.getPreco() < maisBarato.getPreco()) {
                    maisBarato = atual;
                }
            }
            if (maisBarato != null) {
                cestaCompra.adiciona(maisBarato);
            }
        }

        return cestaCompra;
    }

    private static float calcularTotalLista(ListaSequencial<Produto> listaProdutos){
        float total = 0;
        for (int i = 0; i < listaProdutos.comprimento(); i++) {
            total += listaProdutos.obtem(i).getPreco();
        }
        return total;
    }

    private static void criarListasSupermercados(ListaSequencial<String> listaNomeProdutos ) {
        Supermercado[] mercados = {giassi, fort, bistek};
        TabHash<String, ListaSequencial<Produto>>[] caches = new TabHash[]{cacheGiassi, cacheFort, cacheBistek};
        for (int i = 0; i < mercados.length; i++) {
            arrayListas[i] = criarCestaCompra(mercados[i], listaNomeProdutos, caches[i]);
        }
    }

    private static void ordenarPorPreco(ListaSequencial<Produto>[] arrayListas, String[] mercados) {
        float[] totais = new float[arrayListas.length];

        for (int i = 0; i < arrayListas.length; i++) {
            totais[i] = calcularTotalLista(arrayListas[i]);
        }
        for (int i = 0; i < totais.length - 1; i++) {
            for (int j = 0; j < totais.length - 1 - i; j++) {
                if (totais[j] > totais[j + 1]) {
                    float tmpTotal = totais[j];
                    totais[j] = totais[j + 1];
                    totais[j + 1] = tmpTotal;

                    ListaSequencial<Produto> tmpLista = arrayListas[j];
                    arrayListas[j] = arrayListas[j + 1];
                    arrayListas[j + 1] = tmpLista;

                    String tmpNome = mercados[j];
                    mercados[j] = mercados[j + 1];
                    mercados[j + 1] = tmpNome;
                }
            }
        }
    }

    private static void mostrarOrdenado(ListaSequencial<Produto>[] arrayListas, String[] nomes) {
        for (int i = 0; i < arrayListas.length; i++) {
            float total = calcularTotalLista(arrayListas[i]);
            IO.println("\n" + (i + 1) + " lugar: " + nomes[i] + " - Total: R$ " + String.format("%.2f", total));
            for (int j = 0; j < arrayListas[i].comprimento(); j++) {
                Produto p = arrayListas[i].obtem(j);
                IO.println("  - " + p.getNome() + " (R$ " + String.format("%.2f", p.getPreco()) + ")");
            }
        }
    }

    private static boolean verificaEAN(String string){
        return string.matches("\\d+");
    }

    private static String[] obterTermos(String busca) {
        return busca
                .toLowerCase()
                .trim()
                .split("\\s+");
    }

    private static ListaSequencial<Produto> intersecao(ListaSequencial<Produto> a, ListaSequencial<Produto> b) {
        ListaSequencial<Produto> resultado = new ListaSequencial<>();
        TabHash<String, Boolean> ids = new TabHash<>();
        for (int i = 0; i < a.comprimento(); i++) {
            ids.adiciona(a.obtem(i).getId(), true);
        }
        for (int i = 0; i < b.comprimento(); i++) {
            Produto p = b.obtem(i);
            if (ids.contem(p.getId())) {
                resultado.adiciona(p);
            }
        }
        return resultado;
    }
}