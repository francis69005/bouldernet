package com.open.dojo.bouldernet.gui;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;

import javax.swing.JFrame;

import com.open.dojo.bouldernet.BoulderCellEnum;
import com.open.dojo.bouldernet.BoulderMap;
import com.open.dojo.bouldernet.gui.themes.ThemeManagerImpl;
import com.open.dojo.bouldernet.server.BoulderMapServer;
/*
 * TODO:
 * - Affichage des scores (J1 XX - J2 XX - ...) avec couleur du joueur
 * - Affichage disctinction joueurs (1 couleur par joueur)
 * - Gestion des pertes de vie par écrasement par rocher
 * - Gestion fin de map avec lancement nouvelle map
 * - Gestion génération aléatoire de pièges
 * - Gestion éventuelle porte de sortie
 * - Gestion création d'un diamant par réalisation d'un pattern avec rochers
 * - Amélioration des graphismes
 * - Amélioration fluidité
 * - Gestion touche de respawn en cas de blocage avec perte de 1 vie
 * - Ajout sons
 * 
 */
public class BoulderGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public BoulderGUI(BoulderMapProxy proxy) throws IOException {
		setTitle("MMO BoulderNet");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GrillePanel panel = new GrillePanel(proxy, new ThemeManagerImpl());
		panel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				pack();
			}
		});
		
		getContentPane().add(panel, BorderLayout.CENTER);
		
	}
	
	public static void main(String[] args) throws IOException {
		boolean parameterError = false;
		
		if ((args == null) || (args.length == 0)) {
			parameterError = true;
		} else if ("serveur".equals(args[0])){
				// Si paramètre = serveur alors on est serveur ET client
	
				// <---- Code server
				BoulderCellEnum [][] grilleDeDepart = getRandomMap(40, 25, 25, 75);
				BoulderMapServer server = new BoulderMapServer(new BoulderMap(grilleDeDepart));
				// ---->
				
				// Bridge
				BoulderMapProxy proxy = new NetworkBoulderMapProxyImpl("localhost");
				
				// <--- Client
				BoulderGUI gui = new BoulderGUI(proxy);
				gui.setVisible(true);
				// -->
			} else if (("client".equals(args[0])) && (args[1].matches("[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}"))){
				// Si paramètre = adresse IP, on est client
				
				// Bridge
				BoulderMapProxy proxy = new NetworkBoulderMapProxyImpl(args[1]);
				
				// <--- Client 2
				BoulderGUI guiLux = new BoulderGUI(proxy);
				guiLux.setVisible(true);
				// -->
			} else {
				parameterError = true;
			}
		
		if (parameterError) {
			// Erreur de paramètre
			System.out.println("Erreur de paramètres !");
			System.exit(1);
		}
	}
	
	private static BoulderCellEnum [][] getRandomMap(int horizontalSize, int verticalSize, int diamondNb, int rockNb) {
		BoulderCellEnum [][] grille = new BoulderCellEnum[verticalSize][horizontalSize];
		// Remplissage Terre
		for (int y = 0; y < verticalSize; y++) {
			for (int x = 0; x < horizontalSize; x++) {
				grille[y][x] = BoulderCellEnum.T;
			}
		}

		// Remplissage Diamants
		for (int count = 0; count < diamondNb; count++) {
			setRandomMapCell(grille, BoulderCellEnum.D);
		}
		
		// Remplissage Rochers
		for (int count = 0; count < rockNb; count++) {
			setRandomMapCell(grille, BoulderCellEnum.R);
		}
		
		return grille;
	}
	
	private static void setRandomMapCell(BoulderCellEnum [][] grille, BoulderCellEnum cellType) {
		while (true) {
			int y = (int)(Math.random()*grille.length);
			int x = (int)(Math.random()*grille[0].length);
			if (grille[y][x] == BoulderCellEnum.T) {
				grille[y][x] = cellType;
				break;
			}
		}
	}
}
