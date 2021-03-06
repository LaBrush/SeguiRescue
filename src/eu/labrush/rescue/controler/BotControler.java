package eu.labrush.rescue.controler;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import eu.labrush.rescue.IA.behaviour.BotBehaviour;
import eu.labrush.rescue.IA.behaviour.ToucherBotBehaviour;
import eu.labrush.rescue.core.physics.PhysicCore;
import eu.labrush.rescue.level.Level;
import eu.labrush.rescue.model.Bloc;
import eu.labrush.rescue.model.Bot;
import eu.labrush.rescue.model.Personnage;
import eu.labrush.rescue.utils.Listener;

/**
 * @author adrienbocquet
 *
 */
public class BotControler extends AbstractControler {

	HashMap<Bot, BotBehaviour> bots = new HashMap<Bot, BotBehaviour>();
	
	Personnage hero ;
	PersonnageControler personnageControler ;
	TirControler tirControler ;
	
	Observer deadObserver = new Observer(){

		@Override
		public void update(Observable o, Object arg) {
			if(arg == "dead"){
				removeBot((Bot) o);
			}
		}
		
	};
	
	
	public BotControler(Level level, PersonnageControler personnageControler, TirControler tirControler, HeroControler heroControler) {
		super(level);
	
		this.hero = heroControler.getPersonnage();
		this.personnageControler = personnageControler;
		this.tirControler = tirControler ;
		
		heroControler.addObserver(new Observer(){
			@Override
			public void update(Observable o, Object arg) {
				hero = ((HeroControler) o).getPersonnage();
			}
		});
		
		this.physics.addObserver(new Listener<PhysicCore>() {
			@Override
			public void update(PhysicCore req) {
				for (Bot b : bots.keySet()) {
					bots.get(b).update(hero);
				}
			}
		});
		
	}
	
	public void add(Bot ennemi, Bloc bloc) {
		bots.put(ennemi, new ToucherBotBehaviour(ennemi, bloc));
		
		ennemi.setTirManager(tirControler.getTirInterface());
		personnageControler.add(ennemi);
		
		ennemi.addObserver(this.deadObserver);
	}
	
	public void add(Bot ennemi, BotBehaviour behaviour) {
		behaviour.setBot(ennemi);
		bots.put(ennemi, behaviour);
		
		ennemi.setTirManager(tirControler.getTirInterface());
		personnageControler.add(ennemi);
		
		ennemi.addObserver(this.deadObserver);
	}
	
	public void removeBot(Bot b){
		bots.remove(b);
	}
}
