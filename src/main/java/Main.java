import esd.ListaSequencial;
import sm.*;

import java.util.Scanner;

public class Main {
    static Giassi giassi = new Giassi();
    static Fort fort = new Fort();
    static Bistek bistek = new Bistek();
    static ListaSequencial<Produto>[] arrayListas;
    static String[] nomesMercados = {"Giassi", "Fort", "Bistek"};

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
    }

    private static ListaSequencial<Produto> criarCestaCompra(Supermercado mercado, ListaSequencial<String> listaNomeProdutos) {
        ListaSequencial<Produto> cestaCompra = new ListaSequencial<>();

        for (int i = 0; i < listaNomeProdutos.comprimento(); i++) {
            String nomeProduto = listaNomeProdutos.obtem(i);

            Supermercado.Resultado resultado = mercado.busca(nomeProduto);

            if (resultado == null) continue;

            Produto maisBarato = null;

            for (Produto atual : resultado) {
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

    private static void criarListasSupermercados(ListaSequencial<String> listaNomeProdutos) {
        Supermercado[] mercados = {giassi, fort, bistek};
        for (int i = 0; i < mercados.length; i++) {
            arrayListas[i] = criarCestaCompra(mercados[i], listaNomeProdutos);
        }
    }

    private static void ordenarPorPreco(ListaSequencial<Produto>[] arrayListas, String[] mercados) {
        for (int i = 0; i < arrayListas.length - 1; i++) {
            for (int j = 0; j < arrayListas.length - 1 - i; j++) {

                float totalAtual = calcularTotalLista(arrayListas[j]);
                float totalProximo = calcularTotalLista(arrayListas[j + 1]);

                if (totalAtual > totalProximo) {

                    ListaSequencial<Produto> tempLista = arrayListas[j];
                    arrayListas[j] = arrayListas[j + 1];
                    arrayListas[j + 1] = tempLista;

                    String tempNome = mercados[j];
                    mercados[j] = mercados[j + 1];
                    mercados[j + 1] = tempNome;
                }
            }
        }
    }

    private static void mostrarOrdenado(ListaSequencial<Produto>[] arrayListas, String[] nomes) {
        for (int i = 0; i < arrayListas.length; i++) {
            float total = calcularTotalLista(arrayListas[i]);
            System.out.println((i + 1) + " lugar: " + nomes[i] + " - Total: R$" + total);
        }
    }
}