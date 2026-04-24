import esd.ListaSequencial;
import sm.Bistek;
import sm.Fort;
import sm.Giassi;
import sm.Produto;

public class Main {
    static void main() {

        // cria um acessador para o Giassi
        Giassi sm = new Giassi();
        Fort fort = new Fort();
        Bistek bistek = new Bistek();

        // procura todos produtos cujo nome contenha "tapioca"
        ListaSequencial<Produto> produtos = sm.busca("leite");
        ListaSequencial<Produto> produtosFort = fort.busca("leite");
        ListaSequencial<Produto> produtosBistek = bistek.busca("leite");

        // Mostra cada um dos produtos encontrados
        for (int pos=0; pos < produtos.comprimento(); pos++) {
            IO.println(produtos.obtem(pos));
        }

        // Mostra cada um dos produtos encontrados
        for (int pos=0; pos < produtosFort.comprimento(); pos++) {
            IO.println(produtosFort.obtem(pos));
        }
        // Mostra cada um dos produtos encontrados
        for (int pos=0; pos < produtosBistek.comprimento(); pos++) {
            IO.println(produtosBistek.obtem(pos));
        }
    }
}
