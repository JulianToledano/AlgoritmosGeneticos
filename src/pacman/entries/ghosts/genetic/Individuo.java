package pacman.entries.ghosts.genetic;

import pacman.entries.ghosts.Qlearning;
import pacman.game.Game;

public class Individuo {
	private Qlearning q;
	private double value;
	private double eps;
	private double alpha;
	private double gamma;
	private double afinidad;
	
	public Individuo(Game game, double value, double eps, double alpha, double gamma){
		this.value = value;
		this.eps = eps;
		this.alpha = alpha;
		this.gamma = gamma;
		this.q = new Qlearning(game, value, eps, alpha, gamma);
		this.afinidad = 0;
	}
	
	public Qlearning getQ(){return q;}
	public double getAfinidad(){return afinidad;}
	public void setAfinidad(double afinidad){this.afinidad = afinidad;}
	public String toString(){return value + ";" + eps + ";" + alpha + ";" + gamma + ";";}
}
