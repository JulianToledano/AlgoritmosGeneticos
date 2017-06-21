package pacman.entries.ghosts.genetic;

import pacman.entries.ghosts.Qlearning;
import pacman.game.Game;

public class Individuo implements Comparable<Individuo>{
	private Qlearning q;
	private double [] genotipo;
	private Double afinidad;
	
	public Individuo( double value, double eps, double alpha, double gamma){
		genotipo = new double[4];
		genotipo[0] = value;
		genotipo[1] = eps;
		genotipo[2] = alpha;
		genotipo[3] = gamma;
		Game game = new Game(0);
		this.q = new Qlearning(game, genotipo[0], genotipo[1], genotipo[2], genotipo[3]);
		this.afinidad = 0.0;
	}
	
	public Qlearning getQ(){return q;}
	public double getAfinidad(){return afinidad;}
	public void setAfinidad(double afinidad){this.afinidad = afinidad;}
	public double[] getGenotipo(){return genotipo;}
	public String toString(){return "inicializado Q a " + genotipo[0] + "; Epsilod: " + genotipo[1] + "\t\t\t; alpha:" + genotipo[2] + "\t\t; gamma:" + genotipo[3] + ";\t\t";}

	@Override
	public int compareTo(Individuo individuo) {
		// TODO Auto-generated method stub
		return this.afinidad.compareTo(individuo.getAfinidad());
	}
}
