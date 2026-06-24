package cache;

import esd.TabHash;
import esd.ListaSequencial;
import org.json.JSONArray;
import org.json.JSONObject;
import sm.Produto;

import java.nio.file.Files;
import java.nio.file.Path;

public class CacheManager {

    //salva como JSON os produtos da TabHash
    public static void salvar(String arquivo, TabHash<String, ListaSequencial<Produto>> cache) {
        try {
            JSONObject root = new JSONObject();
            ListaSequencial<String> chaves = cache.chaves();
            for (int i = 0; i < chaves.comprimento(); i++) {
                String termo = chaves.obtem(i);
                ListaSequencial<Produto> produtos = cache.obtem(termo);
                JSONArray listaProdutos = new JSONArray();
                for (int j = 0; j < produtos.comprimento(); j++) {
                    listaProdutos.put(produtoParaJson(produtos.obtem(j)));
                }
                root.put(termo, listaProdutos);
            }
            Path path = Path.of(arquivo);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Files.writeString(path, root.toString(2));
            Files.writeString(Path.of(arquivo), root.toString(2));
            IO.println("Cache salva: " + arquivo);
        } catch (Exception e) {
            IO.println("Erro ao salvar cache " + arquivo + ": " + e.getMessage());
        }
    }

    //transforma o produto em um JSON
    private static JSONObject produtoParaJson(Produto p) {
        JSONObject obj = new JSONObject();
        obj.put("nome",       p.getNome());
        obj.put("id",         p.getId());
        obj.put("marca",      p.getMarca() != null ? p.getMarca() : "");
        obj.put("preco",      p.getPreco());
        obj.put("ean",        p.getEan()   != null ? p.getEan()   : "");
        obj.put("disponivel", p.isDisponivel());
        return obj;
    }

    //cria a TabHash com o cache em JSON
    public static TabHash<String, ListaSequencial<Produto>> carregar(String arquivo) {
        TabHash<String, ListaSequencial<Produto>> cache = new TabHash<>();
        Path path = Path.of(arquivo);
        if (!Files.exists(path)) {
            IO.println("Cache não encontrada, iniciando vazia: " + arquivo);
            return cache;
        }
        try {
            String json = Files.readString(path);
            JSONObject root = new JSONObject(json);
            for (String termo : root.keySet()) {
                JSONArray listaProdutos = root.getJSONArray(termo);
                ListaSequencial<Produto> produtos = new ListaSequencial<>();
                for (int i = 0; i < listaProdutos.length(); i++) {
                    produtos.adiciona(produtoDeJson(listaProdutos.getJSONObject(i)));
                }
                cache.adiciona(termo, produtos);
            }
            IO.println("Cache carregada com sucesso!");
        } catch (Exception e) {
            IO.println("Erro ao carregar cache " + arquivo + ": " + e.getMessage());
        }
        return cache;
    }

    //transforma o JSON de produto em um objeto Produto
    private static Produto produtoDeJson(JSONObject obj) {
        return Produto.builder()
                .nome(obj.optString("nome", ""))
                .id(obj.optString("id", ""))
                .marca(obj.optString("marca", ""))
                .preco((float) obj.optDouble("preco", 0.0))
                .ean(obj.optString("ean", null))
                .disponivel(obj.optBoolean("disponivel", false))
                .build();
    }
}
