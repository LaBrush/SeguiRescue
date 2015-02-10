/**
 * 
 */
package eu.labrush.rescue.core.physics;

import java.util.HashSet;

import eu.labrush.rescue.model.AbstractObject;
import eu.labrush.rescue.model.Bloc;
import eu.labrush.rescue.model.Trajectoire;
import eu.labrush.rescue.model.Vecteur;

/**
 * @author adrienbocquet
 *
 */
public class PersonnagePhysicBehaviour extends AbstractPhysicBehaviour {

	public PersonnagePhysicBehaviour(AbstractObject obj) {
		super(obj);
	}

	@Override
	public void updateTrajectoire(HashSet<? extends AbstractObject> obstacles, int delta_t) {

		this.moves = new LibertyDegree();
		Trajectoire t = this.getTarget().getTrajectoire();
		Vecteur deplacement = t.distanceParcourue(delta_t);

		// On calcul la distance de chaque coté à chaque obstacle potentiel
		for (AbstractObject obstacle : obstacles) {
			if (obstacle instanceof Bloc) {
				this.calcMargin(obstacle, deplacement);
			}
		}

		// La distance parcourue pendant la durée delta_t est supérieure à la distance, on adapte la
		// vitesse
		
		if (deplacement.getX() > 0 && deplacement.getX() >= this.moves.right) {
			t.getVitesse().setX(this.moves.right / delta_t);
			t.getAcceleration().setX(0);
		}
		else if (deplacement.getX() < 0 && - deplacement.getX() >= this.moves.left) {
			t.getVitesse().setX(-this.moves.left / delta_t);
			t.getAcceleration().setX(0);
		}

		if (deplacement.getY() > 0 && deplacement.getY() >= this.moves.top) {
			t.getVitesse().setY(this.moves.top / delta_t);
			t.getAcceleration().setX(0);
		}
		else if (deplacement.getY() < 0 && -deplacement.getY() >= this.moves.bottom) {
			t.getVitesse().setY(-this.moves.bottom / delta_t);
			t.getAcceleration().setY(0);
		}
		else if (this.moves.bottom > 0 && t.hasGravity()) { // Si on est en l'air, on rajoute la
															// gravité
			t.getAcceleration().setY(PhysicCore.GRAVITY);
		}

		// Enfin on met à jour la trajectoire
		t.update(delta_t);
	}

}
