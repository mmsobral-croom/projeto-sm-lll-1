[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/0aiXsnlU)
# Projeto 1: Melhor Preço

No projeto 1 sua equipe deve desenvolver um programa que descubra em qual supermercado se pode comprar um conjunto de produtos pelo melhor preço.

O usuário deve criar uma cesta de compras, e então o preço total dessa cesta deve ser calculado para cada um dos supermercados cadastrados. Ao final, o programa deve mostrar os supermercados e respectivos preços de cesta ordenados pelo preço.

Os supermercados cadastrados até o momento são:
* Giassi
* Bistek
* Fort Atacadista

Para desenvolver esse software, usem os buscadores de preço disponibilizados neste repositório inicial. Existe um buscador para cada supermercado implementado em uma classe na package _sm_. A interface dos buscadores é a mesma:

* __ListaSequencial\<Produto\> busca(String nome)__: busca todos produtos cujos nomes contenham _nome_. O resultado é uma lista de objetos _Produto_.
* __Produto obtem(String productId)__: busca a descrição de um produto identificado pelo _productId_. Os valores de _productId_ são específicos de cada supermercado.

A classe Produto contém a descrição de um produto, e possui os seguintes métodos para acessar as informações:
* __String nome()__: o nome do produto, conforme definido pelo supermercado 
* __String id()__: o valor do _productId_ definido pelo supermercado
* __String marca()__: a marca do produto
* __float preco()__: o preço do produto
* __boolean disponivel()__: se o produto está disponível no supermercado

Um exemplo de consulta a produtos de um supermercado está contido em _Main.java_:

```java
public class Main {
    static void main() {

        // cria um acessador para o Giassi
        Giassi sm = new Giassi();

        // procura todos produtos cujo nome contenha "tapioca"
        ListaSequencial<Produto> produtos = sm.busca("tapioca");

        // Mostra cada um dos produtos encontrados
        for (int pos=0; pos < produtos.comprimento(); pos++) {
            IO.println(produtos.obtem(pos));
        }

    }
}
```

## Ordem de implementação

1. Usuário informa todos os produtos desejados no seguinte método -> **Feito**
```java
   void main() {
    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    Scanner inp = new Scanner(System.in);
    String[] fila = new String[32];
    int clientes = 0;
    boolean continuar = true;

    while (continuar) {
        IO.print("> ");
        String cmd = inp.nextLine();
        cmd = cmd.strip();
        switch (cmd) {
            case "" -> {}
            case "sair" -> continuar = false;
            case "?" -> {
                if (clientes > 0) {
                    IO.println(fila[0]);
                    for (int pos=1; pos < clientes; pos++) {
                        fila[pos-1] = fila[pos];
                    }
                    fila[--clientes] = null;
                }
            }
            default -> {
                if (clientes < fila.length) {
                    fila[clientes++] = cmd;
                }
            }
        }
    }
}
```
2. Sistema efetua um `loop` para buscar todos os produtos da lista em **cada** mercado
   - Se produtos achados > 1 pegar o de menor preço entre os listados
3. Somar o preço de todos os produtos na lista
4. Fazer a comparação
5. Informar o mercado com o menor preço para aquela cesta de compras
