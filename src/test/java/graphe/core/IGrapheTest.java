package graphe.core;

import graphe.ihm.GraphImporter;
import graphe.implems.GrapheHHAdj;
import graphe.implems.GrapheLAdj;
import graphe.implems.GrapheLArcs;
import graphe.implems.GrapheMAdj;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class IGrapheTest {
	private final IGraphe[] graphes = {
			new GrapheLAdj(), new GrapheMAdj(),
			new GrapheHHAdj(), new GrapheLArcs()

	};
	
	// graphe de l'exercice 3.1 du poly de maths
	// avec en plus un noeud isole : J
	private final String g31 = 
			"A-C(2), A-D(1), "
			+ "B-G(3), "
			+ "C-H(2), "
			+ "D-B(3), D-C(5), D-E(3), "
			+ "E-C(1), E-G(3), E-H(7), "
			+ "F:, "
			+ "G-B(2), G-F(1), "
			+ "H-F(4), H-G(2), "
			+ "I-H(10), "
			+ "J:";
	
	private final String g31a = ""       // arcs non tries
			+ "D-C(5), D-E(3), D-B(3), "
			+ "E-G(3), E-C(1), E-H(7), "
			+ "I-H(10), "
			+ "J:,"
			+ "G-B(2), G-F(1), "
			+ "F:, "
			+ "H-G(2), H-F(4), "
			+ "A-C(2), A-D(1), "
			+ "B-G(3), "
			+ "C-H(2) ";

	/*
	* Ordre pour fonction personnelle : OterArc - OterSommet - testerAutre
	*
	* */
	@Test
	void exo3_1Maths() {
		for (IGraphe g : graphes) {
			g.peupler(g31a);
			tester3_1(g);
//			testerFonctionOterArc(g);
//			testerFonctionOterSommet(g);
//			testerAutre(g);
		}
	}
	
	void tester3_1(IGraphe g) {
		List<String> sommets_exp = List.of("A","B","C","D","E","F","G","H","I","J");
		List<String> sommets = new ArrayList<String>(g.getSommets()); // pas forcement triee
		Collections.sort(sommets);
		assertEquals(sommets_exp, sommets);
		assertTrue(g.contientSommet("C"));
		assertFalse(g.contientSommet("c"));
		assertTrue(g.contientArc("C","H"));
		assertFalse(g.contientArc("H","C"));
		assertEquals(7,g.getValuation("E", "H"));
		List<String> successeurs = new ArrayList<String>(g.getSucc("D")); // pas forcement triee
		Collections.sort(successeurs);
		assertEquals(List.of("B","C", "E"), successeurs);
		assertEquals(g31, g.toString());
		
		g.ajouterSommet("A"); // ne fait rien car A est deja present
		assertEquals(g31, g.toString());
		assertThrows(IllegalArgumentException.class,  
				() -> g.ajouterArc("G", "B", 1));		// deja present
		g.oterSommet("X"); // ne fait rien si le sommet n'est pas present
		assertEquals(g31, g.toString());
		assertThrows(IllegalArgumentException.class,
				() -> g.oterArc("X", "Y"));  // n'existe pas
		
		assertThrows(IllegalArgumentException.class,
				() -> g.ajouterArc("A", "B", -1)); // valuation negative
	}
	
	void petiteImporation(IGraphe g, String filename) {
		Arc a = GraphImporter.importer(filename, g);
		System.out.println(filename+ " : "+ g.toString());
		assertEquals("1-3(5), "
				+ "10-3(3), 2-1(5), 2-3(5), 2-5(4), "
				+ "3-4(4), 3-5(4), 4-10(1), 4-2(1), 4-7(3), "
				+ "5-9(4), 6-2(3), 6-3(4), 7-3(2),"
				+ " 8-2(4), 8-6(1), 9-2(4)",
				g.toString());
		assertEquals("5", a.getSource());
		assertEquals("7", a.getDestination());		
	}
	
	@Test
	void petitTestImportation() {
		for (IGraphe g : graphes)
			petiteImporation(g, "src/test/java/graphe/grapheImporter/g-10-1.txt");
	}

	/**
	 * Teste la fonction oterArc de l'interface IGraphe sur l'instance de graphe fournie en paramètre.
	 * Effectue une série d'opérations d'oterArc et vérifie les résultats attendus à l'aide des assertions.
	 * @param g l'instance de graphe sur laquelle tester la fonction oterArc.
	 */
	void testerFonctionOterArc(IGraphe g) {
		// Opérations d'oterArc sur le graphe
		g.oterArc("A","C");
		g.oterArc("A","D");
		g.oterArc("B","G");
		g.oterArc("C","H");
		g.oterArc("D","B");
		g.oterArc("D","C");
		g.oterArc("D","E");
		g.oterArc("E","C");
		g.oterArc("E","G");
		g.oterArc("E","H");
		g.oterArc("G","B");
		g.oterArc("H","F");
		g.oterArc("H","G");
		g.oterArc("I","H");

		// Assertions pour vérifier les résultats attendus
		List<String> som2 = List.of();
		List<String> som3 = List.of("F");
		assertEquals(som2,g.getSucc("A"));
		assertEquals(som2,g.getSucc("J"));
		assertEquals(som3,g.getSucc("G"));
		assertEquals(som2,g.getSucc("F"));
		assertEquals(-1,g.getValuation("A","C"));
		assertEquals(-1,g.getValuation("H","I"));
		assertEquals(1,g.getValuation("G","F"));

		g.oterArc("G","F");

		String fina = "A:, B:, C:, D:, E:, F:, G:, H:, I:, J:";
		assertEquals(fina,g.toString());
	}

	/**
	 * Teste la fonction oterSommet de l'interface IGraphe sur l'instance de graphe fournie en paramètre.
	 * Effectue une série d'opérations d'oterSommet et vérifie les résultats attendus à l'aide des assertions.
	 * @param g l'instance de graphe sur laquelle tester la fonction oterSommet.
	 */
	void testerFonctionOterSommet(IGraphe g) {
		// Listes des sommets attendus après les opérations d'oterSommet
		List<String> som4 = List.of("F");
		List<String> sommets_exp = List.of("B","C","D","E","F","G","H","I","J");
		List<String> sommets_exp2 = List.of("B","E","F","H","J");

		// Opérations d'oterSommet sur le graphe
		g.oterSommet("A");
		assertEquals(sommets_exp,g.getSommets());
		g.oterSommet("I");
		g.oterSommet("C");
		g.oterSommet("D");
		g.oterSommet("G");
		assertEquals(sommets_exp2,g.getSommets());
		g.oterSommet("G");
		g.oterSommet("C");
		g.oterSommet("H");
		g.oterSommet("I");
		g.oterSommet("J");
		g.oterSommet("Z");
		g.oterSommet("B");
		g.oterSommet("E");
		assertEquals(som4, g.getSommets());
	}

	/**
	 * Teste d'autres fonctionnalités de l'interface IGraphe sur l'instance de graphe fournie en paramètre.
	 * Effectue une série d'opérations d'ajout, de suppression et de vérification, et vérifie les résultats attendus à l'aide des assertions.
	 * @param g l'instance de graphe sur laquelle tester les autres fonctionnalités.
	 */
	void testerAutre(IGraphe g) {
		// Listes des sommets attendus
		List<String> sommets_exp2 = List.of("Z");
		List<String> sommets_exp3 = new ArrayList<>();

		// Opérations d'ajout d'arcs
		g.ajouterArc("Z","Z",4);
		g.ajouterArc("Z","V", 10);
		g.ajouterArc("Z","A",50);
		g.ajouterArc("A","Z",30);

		// Vérification des valuations
		assertEquals(10,g.getValuation("Z","V"));
		assertEquals(4,g.getValuation("Z","Z"));
		assertEquals(50,g.getValuation("Z","A"));
		assertEquals(30,g.getValuation("A","Z"));
		assertEquals(-1,g.getValuation("A","W"));
		assertEquals(-1,g.getValuation("",""));

		g.ajouterSommet("z");
		g.oterSommet("W");

		assertEquals(sommets_exp2, g.getSucc("A"));
		assertFalse(g.contientArc("A",""));

		g.oterSommet("X");
		g.oterSommet("Z");

		assertEquals(sommets_exp3, g.getSucc("$"));
		assertEquals(-1,g.getValuation("$","A"));

		assertThrows(IllegalArgumentException.class, () -> g.oterArc("$", "A")); // n'existe pas
		assertThrows(IllegalArgumentException.class, () -> g.oterArc("A", "B"));

		// Ajout et suppression d'un arc
		g.ajouterArc("P", "Z", 6);
		List<String> som = List.of("Z");
		assertEquals(som,g.getSucc("P"));
		g.oterArc("P", "Z");

		g.oterSommet("F");
		g.oterSommet("V");
		g.oterSommet("Z");
		g.oterSommet("z");
	}

	void petitTestImportation3() {
		for (IGraphe g : graphes)
			petiteImporation3(g, "src/test/java/AutreGraphe/g-6-1.txt");
	}

	void petiteImporation3(IGraphe g, String filename) {
		Arc a = GraphImporter.importer(filename, g);
		System.out.println(filename+ " : "+ g.toString());
		assertEquals("1-2(6), 1-3(2), 1-4(1), 2-5(1), 3-2(1), 4-5(4), 5-6(1), 6:",
				g.toString());
	}

	void testImportation2() {
		for (IGraphe g : graphes)
			testImportation2(g, "src/test/java/graphe/grapheImporter/g-100-5.txt");
	}

	void testImportation2(IGraphe g, String filename) {
		Arc b = GraphImporter.importer(filename,g);
		assertEquals("1-12(5), 1-21(4), 1-3(1), 1-30(3), 1-31(4), 1-35(4), 1-48(1), 1-5(3), 1-52(5), 1-57(2), " +
				"1-6(5), 1-68(3), 1-69(5), 1-7(1), 1-71(4), 1-79(3), 10-11(2), 10-17(2), 10-19(3), 10-38(4), 10-5(2), 10-85(4), " +
				"100-37(4), 11-17(3), 11-81(1), 12-21(2), 12-24(2), 12-5(2), 12-52(2), 13-49(2), 13-6(2), 13-9(4), 14-22(2), 14-77(1), " +
				"15-99(3), 16-1(2), 16-44(5), 16-5(4), 17:, 18-1(1), 18-6(2), 19-23(1), 19-26(4), 2-1(1), 2-28(4), 2-3(3), 2-5(3), 2-76(2), 2-8(1), 20-15(2), " +
				"21-29(1), 21-47(4), 22:, 23-10(3), 24-21(5), 25-1(4), 25-6(3), 26-10(3), 27-2(2), 27-8(2), 28-63(2), 28-64(3), 28-76(3), 28-90(2), 28-97(2), 29-12(4), 3-14(1), 3-15(1), " +
				"3-20(1), 3-32(1), 3-4(4), 3-42(5), 3-43(3), 3-51(5), 3-57(3), 3-7(2), 3-77(4), 3-80(5), 30-65(1), 30-71(1), 31-16(5), 31-83(1), 31-96(1), 32:, 33-3(1), 33-32(4), 33-80(5), " +
				"34-6(2), 34-9(1), 35-74(4), 35-9(1), 36-21(3), 36-29(5), 37-6(2), 38-17(3), 39-1(5), 39-16(5), 4-1(3), 4-43(4), 40-11(1), 40-6(4), 41-37(5), 42-1(5), 43:, 44-1(5), 44-48(4), " +
				"45-40(2), 46-1(1), 46-44(3), 47-12(4), 48:, 49-6(2), 5-19(2), 5-28(4), 5-37(4), 5-41(1), 5-6(5), 50-11(1), 50-70(1), 51-14(4), 52-69(2), 53-32(2), 53-60(3), 53-66(3), 53-7(5), " +
				"54-17(5), 54-38(2), 55-44(2), 55-48(4), 56-3(1), 56-8(3), 57-73(3), 58-15(4), 58-20(5), 58-59(1), 59-15(4), 6-10(3), 6-100(5), 6-11(1), 6-45(2), 6-50(5), 6-62(2), 60-66(5), 60-7(5), " +
				"60-94(5), 61-27(4), 61-8(3), 62-37(3), 62-84(2), 63-5(1), 63-86(4), 63-95(5), 64-5(4), 65-9(5), 66:, 67-1(3), 67-25(2), 68-44(4), 69-79(1), 7-14(4), 7-22(3), 7-32(4), 70-6(5), 71:, " +
				"72-1(4), 72-3(5), 73-1(3), 74-88(3), 75-44(2), 75-68(1), 76-90(1), 77:, 78-2(4), 78-5(2), 79:, 8-15(3), 8-3(5), 80:, 81-10(3), 82-11(1), 82-50(3), 83-16(3), 84-6(5), 85-38(2), 86-28(4), " +
				"87-28(2), 87-76(4), 88-9(5), 89-44(4), 89-48(4), 9-1(1), 9-30(3), 9-6(3), 9-74(2), 90-97(5), 91-3(3), 91-32(2), 91-93(3), 92-30(5), 92-71(4), 93-3(1), 94-66(2), 95-5(2), 96-16(5), 97:, " +
				"98-1(1), 98-9(4), 99-20(4)", g.toString());

		// Vérification de la présence de sommets dans le graphe
		assertTrue(g.contientSommet("1"));
		assertTrue(g.contientSommet("58"));
		assertFalse(g.contientSommet("500"));

		// Vérification de la présence d'arcs dans le graphe
		assertTrue(g.contientArc("1","35"));
		assertFalse(g.contientArc("35","1"));
		assertTrue(g.contientArc("53","7"));
		assertFalse(g.contientArc("7","53"));

		// Vérification des valuations des arcs
		assertEquals(5, g.getValuation("1", "12"));
		assertEquals(4, g.getValuation("1", "21"));
		assertEquals(1, g.getValuation("1", "3"));
		assertEquals(3, g.getValuation("1", "30"));
		assertEquals(4, g.getValuation("1", "31"));
		assertEquals(4, g.getValuation("1", "35"));
		assertEquals(1, g.getValuation("1", "48"));
		assertEquals(3, g.getValuation("1", "5"));
		assertEquals(5, g.getValuation("1", "52"));
		assertEquals(2, g.getValuation("1", "57"));
		assertEquals(5, g.getValuation("1", "6"));
		assertEquals(3, g.getValuation("1", "68"));
		assertEquals(5, g.getValuation("1", "69"));
		assertEquals(1, g.getValuation("1", "7"));
		assertEquals(4, g.getValuation("1", "71"));
		assertEquals(3, g.getValuation("1", "79"));
		assertEquals(2, g.getValuation("10", "11"));
		assertEquals(2, g.getValuation("10", "17"));
		assertEquals(3, g.getValuation("10", "19"));
		assertEquals(4, g.getValuation("10", "38"));
		assertEquals(2, g.getValuation("10", "5"));
		assertEquals(4, g.getValuation("10", "85"));
		assertEquals(4, g.getValuation("100", "37"));
		assertEquals(3, g.getValuation("11", "17"));
		assertEquals(1, g.getValuation("11", "81"));
		assertEquals(2, g.getValuation("12", "21"));
		assertEquals(2, g.getValuation("12", "24"));
		assertEquals(2, g.getValuation("12", "5"));
		assertEquals(2, g.getValuation("12", "52"));
	}
}
