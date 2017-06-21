package pacman.entries.ghosts.genetic;

import pacman.entries.ghosts.Qlearning;
import pacman.game.Game;

public class Individuo {
	private Qlearning q;
	private double afinidad;
	
	public Individuo(Game game, double value, double eps, double alpha, double gamma){
		this.q = new Qlearning(game, value,eps,alpha,gamma);
		this.afinidad = 0;
	}
	
	public double getAfinidad(){return afinidad;}
	public void setAfinidad(double afinidad){this.afinidad = afinidad;}
}
