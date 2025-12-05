package grafossocial;

import java.util.*;

public class GrafoRotas {

    private Map<String, List<Aresta>> mapa = new HashMap<>();

    static class Aresta {
        String destino;
        int distancia;

        Aresta(String destino, int distancia) {
            this.destino = destino;
            this.distancia = distancia;
        }
    }

    public void adicionarCidade(String nome) {
        mapa.putIfAbsent(nome, new ArrayList<>());
    }

    public void adicionarRota(String origem, String destino, int distancia) {
        adicionarCidade(origem);
        adicionarCidade(destino);

        mapa.get(origem).add(new Aresta(destino, distancia));
        mapa.get(destino).add(new Aresta(origem, distancia)); // grafo não direcionado
    }

    public void exibirMapa() {
        System.out.println("\n===== MAPA DE ROTAS =====");
        for (String cidade : mapa.keySet()) {
            System.out.print(cidade + " -> ");
            for (Aresta a : mapa.get(cidade)) {
                System.out.print(a.destino + "(" + a.distancia + "km) ");
            }
            System.out.println();
        }
    }

    public void dijkstra(String origem, String destino) {
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> anterior = new HashMap<>();
        PriorityQueue<String> fila = new PriorityQueue<>(Comparator.comparingInt(dist::get));

        for (String cidade : mapa.keySet()) {
            dist.put(cidade, Integer.MAX_VALUE);
            anterior.put(cidade, null);
        }

        dist.put(origem, 0);
        fila.add(origem);

        while (!fila.isEmpty()) {
            String atual = fila.poll();

            for (Aresta a : mapa.get(atual)) {
                int novaDist = dist.get(atual) + a.distancia;

                if (novaDist < dist.get(a.destino)) {
                    dist.put(a.destino, novaDist);
                    anterior.put(a.destino, atual);
                    fila.add(a.destino);
                }
            }
        }

        // Reconstrói o caminho
        List<String> caminho = new ArrayList<>();
        String atual = destino;

        while (atual != null) {
            caminho.add(atual);
            atual = anterior.get(atual);
        }

        Collections.reverse(caminho);

        System.out.println("\n===== MENOR CAMINHO =====");
        System.out.println("De " + origem + " para " + destino + ": " + caminho);
        System.out.println("Distância total: " + dist.get(destino) + " km");
    }

    public static void main(String[] args) {
        GrafoRotas grafo = new GrafoRotas();

        grafo.adicionarRota("A", "B", 10);
        grafo.adicionarRota("A", "C", 15);
        grafo.adicionarRota("B", "D", 12);
        grafo.adicionarRota("C", "E", 10);
        grafo.adicionarRota("D", "E", 2);
        grafo.adicionarRota("D", "F", 1);
        grafo.adicionarRota("E", "F", 5);

        grafo.exibirMapa();
        grafo.dijkstra("A", "F");
    }
}