package eu.labrush.rescue.level;

import java.awt.Color;
import java.util.ArrayList;

import eu.labrush.rescue.IA.behaviour.BossBehaviour;
import eu.labrush.rescue.IA.behaviour.FlyBotBehaviour;
import eu.labrush.rescue.IA.behaviour.ToucherBotBehaviour;
import eu.labrush.rescue.IA.path.AStar;
import eu.labrush.rescue.IA.path.Point;
import eu.labrush.rescue.controler.BotControler;
import eu.labrush.rescue.core.graphic.DrawRequest;
import eu.labrush.rescue.core.graphic.GraphicCore;
import eu.labrush.rescue.core.physics.PhysicCore;
import eu.labrush.rescue.model.ArmeItem;
import eu.labrush.rescue.model.Bloc;
import eu.labrush.rescue.model.Bot;
import eu.labrush.rescue.model.Personnage;
import eu.labrush.rescue.model.arme.Arme;
import eu.labrush.rescue.utils.Listener;

/**
 *
 * Gère tous les controlleurs, vues et modèles d'un niveau
 * 
 * TODO: ajouter un destructeur pour enlever les listener des coeurs graphique et physique
 *
 * @author adrienbocquet
 */
public class SampleLevel extends Level {

	ArrayList<Point> trajet;
	AStar star;

	/**
	 * @param graphics
	 *            le coeur graphique
	 * @param physics
	 *            le coeur physique
	 */
	public SampleLevel(GraphicCore graphics, PhysicCore physics) {
		super(graphics, physics);

		Personnage hero = new Personnage(15, 15);
		hero.addArme(new Arme("Resistance", 10, 200));
		hero.addArme(new Arme("Transistor", 10, 200));

		heroControler.setPersonnage(hero);

		ArrayList<Bloc> blocs = new ArrayList<Bloc>();
		blocs.add(new Bloc(60, 100, 80, 20));
		blocs.add(new Bloc(200, 140, 80, 20));
		blocs.add(new Bloc(300, 180, 80, 20));
		blocs.add(new Bloc(480, 80, 20, 20));
		blocs.add(new Bloc(400, 80, 40, 20, true));

		this.addBorders();
		for (Bloc b : blocs) {
			blocControler.add(b);
		}

		botControler = new BotControler(this, personnageControler, tirControler, heroControler);

		Bot aerien = new Bot(300, 300);
		aerien.addArme(new Arme("Resistance", 10, 750));
		botControler.add(aerien, new FlyBotBehaviour());

		botControler.add(new Bot(260, 175), new BossBehaviour(blocControler.getBlocs()));
		botControler.add(new Bot(260, 50), new ToucherBotBehaviour(blocs.get(1)));
		botControler.add(new Bot(320, 215), new ToucherBotBehaviour(blocs.get(2)));

		itemControler.add(new ArmeItem(220, 161, new Arme("Transistor", 34, 200)));

		// TEST ASTAR
		star = new AStar(10, getBlocControler().getBlocs());
		try {
			trajet = star.findPath(new Point(2, 2), new Point(30, 24));
			
			graphics.addObserver(new Listener<DrawRequest>() {
				@Override
				public void update(DrawRequest req) {
					for (Point p : trajet) {
						req.rect(p.x * 10, p.y * 10, 1, 1);
					}
				}
			});
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Personnage p = heroControler.getPersonnage();
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {
						trajet = star.findPath(new Point((int) ((p.getX()+p.getWidth()/2) / 10), (int) ((p.getY()+ p.getHeight()/2)/10)), new Point(35, 24));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		thread.start();
	}

	/**
	 * Ajoute des blocs aux quatres coins de l'écran afin d'éviter tout débordement du panneau
	 */
	private void addBorders() {

		int width = this.graphics.getPan().getWidth(), height = this.graphics.getPan().getHeight();

		blocControler.add(new Bloc(0, height + 1, width, 10)); // mur en haut
		blocControler.add(new Bloc(-1, 0, 0, height + 100)); // mur à gauche
		blocControler.add(new Bloc(width, 0, 10, height + 100)); // mur à droite
		blocControler.add(new Bloc(-1, 0, width + 1, 10)); // mur en bas
	}

	/**
	 * @return the graphic core
	 */
	public GraphicCore getGraphics() {
		return graphics;
	}

	/**
	 * @return the physic core
	 */
	public PhysicCore getPhysics() {
		return physics;
	}

}
