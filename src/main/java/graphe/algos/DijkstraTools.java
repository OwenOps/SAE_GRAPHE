package graphe.algos;

import graphe.Dijkstra.Dijkstra;
import graphe.core.IGrapheConst;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DijkstraTools extends IGrapheTest {
        public static long time(IGrapheConst g, String source,
                                Map<String, Integer> dist, Map<String, String> prev) {
        long debut = System.nanoTime();
        Dijkstra.dijkstra(g, source, dist, prev);
        long fin = System.nanoTime();
        return (fin - debut)/1000000;
    }

    // reconstruction du plus court chemin (dans prev) de source vers dest en partant de dest
    public static List<String> getPath(String source, String dest, Map<String, String> prev) {
        LinkedList<String> path = new LinkedList<>();
        for (String sommet = dest; sommet != null; sommet = prev.get(sommet))
            path.addFirst(sommet);
        return path;
    }
}
