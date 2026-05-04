import esd.ListaSequencial;
import sm.*;

import java.net.URISyntaxException;
import java.util.Scanner;

public class Main {
    static Giassi giassi = new Giassi();
    static Fort fort = new Fort();
    static Bistek bistek = new Bistek();

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
                    IO.println("Aqui vai ser chamado o metodo");
                    continuar = false;
                }
                default -> {
                    listaNomeProdutos.adiciona(cmd);
                }
            }
        }
    }

    private static ListaSequencial<Produto> criarCestaCompra(Supermercado mercado, ListaSequencial<String> listaNomeProdutos) throws URISyntaxException {
        ListaSequencial<Produto> cestaCompra = new ListaSequencial<>();

        for (int i = 0; i < listaNomeProdutos.comprimento(); i++) {
            String nomeProduto = listaNomeProdutos.obtem(i);
            ListaSequencial<Produto> listaProdutosEncontrados = mercado.busca(nomeProduto);

            Produto maisBarato = null;

            for (int j = 0; j < listaProdutosEncontrados.comprimento(); j++) {
                Produto atual = listaProdutosEncontrados.obtem(j);

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
}
