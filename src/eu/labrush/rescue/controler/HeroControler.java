/**
 * 
 */
package eu.labrush.rescue.controler;

import java.awt.event.KeyEvent;

import eu.labrush.rescue.core.graphic.GraphicCore;
import eu.labrush.rescue.core.graphic.KeyboardListener;
import eu.labrush.rescue.level.Level;
import eu.labrush.rescue.model.Personnage;
import eu.labrush.rescue.model.Trajectoire;
import eu.labrush.rescue.utils.Listener;

/**
 * @author adrienbocquet
 *
 */
public class HeroControler extends AbstractControler {

	private Personnage model;
	boolean moving;

	private double vx = .3, vy = .9;

	KeyboardListener keyboard = GraphicCore.getKeyboard();
	Listener<KeyEvent> listener = new Listener<KeyEvent>() {

		@Override
		public void update(KeyEvent req) {
			Trajectoire t = model.getTrajectoire();

			if (req.getID() == KeyEvent.KEY_PRESSED) {
				switch (req.getKeyCode()) {
					case KeyEvent.VK_UP:
						if (t.getVitesse().getY() == 0)
							t.getVitesse().setY(vy);
						break;
					case KeyEvent.VK_LEFT:
						t.getVitesse().setX(-vx);
						break;
					case KeyEvent.VK_RIGHT:
						t.getVitesse().setX(vx);
						break;

				}
			}

			else if (req.getID() == KeyEvent.KEY_RELEASED) {

				switch (req.getKeyCode()) {
					case KeyEvent.VK_LEFT:
					case KeyEvent.VK_RIGHT:
						t.getVitesse().setX(0);
						break;
				}
			}

			moving = !(t.getVitesse().norme() == 0 && t.getAcceleration().norme() == 0);
		}

	};

	public HeroControler(Level level) {
		super(level);
	}

	/**
	 * @param hero
	 */
	public void setPersonnage(Personnage hero) {
		keyboard.delObserver(listener);
		this.model = hero;

		if (this.model != null) {
			keyboard.addObserver(listener);
		}
	}
}