import esd.ListaSequencial;
import sm.Bistek;
import sm.Fort;
import sm.Giassi;

import java.util.Scanner;

public class Main {
    static Giassi sm = new Giassi();
    static Fort fort = new Fort();
    static Bistek bistek = new Bistek();

    static void main(String[] args) {
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
        // procura todos produtos cujo nome contenha "tapioca"
//        ListaSequencial<Produto> produtos = sm.busca("leite");
//        ListaSequencial<Produto> produtosFort = fort.busca("leite");
//        ListaSequencial<Produto> produtosBistek = bistek.busca("leite");

    }
}
