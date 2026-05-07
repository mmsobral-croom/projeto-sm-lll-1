[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/0aiXsnlU)
# Projeto 1: Melhor Preço

## Descrição

Este programa permite que o usuário monte uma cesta de compras e descubra em qual supermercado ela sai mais barata. O sistema busca os preços dos produtos em cada supermercado cadastrado, seleciona o item mais barato disponível em cada um, calcula o total da cesta e exibe os mercados ordenados do mais barato ao mais caro.
Os supermercados cadastrados são:

* Giassi — giassi.com.br

* Bistek — bistek.com.br

* Fort Atacadista — deliveryfort.com.br

## Estrutura do projeto

### `Main.java`

Classe responsável pela interação com o usuário e pela lógica de cálculo dos preços. Suas funções são:

* Receber os produtos digitados pelo usuário e armazená-los em uma `ListaSequencial<String>`

* Para cada supermercado, buscar o produto mais barato disponível dentre os resultados e montar uma cesta de compras - `criarCestaCompra`
  
* Calcular o preço total de cada cesta - `calcularTotalLista`
  
* Ordenar os supermercados pelo preço total - `ordenarPorPreco`
  
* Exibir o ranking final - `mostrarOrdenado`

### `Supermercado.java`

Classe base que realiza as requisições HTTP na API dos mercados. Seus principais métodos são:

* `Resultado busca(String nome)`: Busca produtos pelo nome, retornando um objeto `Resultado` iterável

* `Produto obtem(String productID)`: Busca um produto por seu ID pelo

### Lógica de seleção do produto mais barato

Para cada produto da cesta, o sistema busca todos os resultados retornados pelo supermercado e seleciona o de menor preço que esteja disponível. Caso nenhum resultado disponível seja encontrado para um produto em determinado mercado, ele simplesmente não é adicionado à cesta daquele mercado.

```java
for (Produto atual : resultado) {
    if (!atual.isDisponivel()) continue;
    if (maisBarato == null || atual.getPreco() < maisBarato.getPreco()) {
        maisBarato = atual;
    }
}
```

## Como usar

| Comando | Descrição  |
| :---: | :---: |
| (nome do produto) | Adiciona produto à cesta |
| produtos | Lista os produtos adicionados |
| ? | Remove o último produto adicionado |
| calcular | Busca os valores e exibe o resultado ordenado |
| sair | Encerra o programa |

### Exemplo de uso:
```
> arroz
> feijao
> azeite
> produtos
    arroz
    feijao
    azeite
> calcular
    1 lugar: Giassi - Total: R$9.65
    2 lugar: Fort - Total: R$11.25
    3 lugar: Bistek - Total: R$18.17
```

## Dependências

* Java 11+
* `Lombok`
* `org.json`
* Classe `ListaSequencial`
* Classe `IO`
