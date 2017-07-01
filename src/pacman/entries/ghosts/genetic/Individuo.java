package pacman.entries.ghosts.genetic;

import static pacman.game.Constants.DELAY;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterPacMan;
import pacman.entries.ghosts.MyGhosts;
import pacman.entries.ghosts.Qlearning;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class Individuo implements Comparable<Individuo>{
//	private Qlearning q;
	private double [] genotipo;
	private Double afinidad;
	
	public Individuo( double value, double eps, double alpha, double gamma){
		genotipo = new double[4];
		genotipo[0] = value;
		genotipo[1] = eps;
		genotipo[2] = alpha;
		genotipo[3] = gamma;
		this.afinidad = 0.0;
	}
	
	public Qlearning getQ(){
		Game game = new Game(0);
		return new Qlearning(game, genotipo[0], genotipo[1], genotipo[2], genotipo[3]);
	}
	public double getAfinidad(){return afinidad;}
	public double[] getGenotipo(){return genotipo;}
	public String toString(){return "inicializado Q a " + genotipo[0] + "; Epsilon: " + genotipo[1] + "; alpha:" + genotipo[2] + "; gamma:" + genotipo[3] + "; afinidad: " + afinidad;}
	
	public void setGenotipo(double []gen) {this.genotipo = gen;}
	public void setAfinidad(){
		Game game = new Game(0);
		Qlearning q = new Qlearning(game, genotipo[0], genotipo[1], genotipo[2], genotipo[3]);
		this.afinidad = entrenar(new StarterPacMan(), new MyGhosts(q), 10);
		this.afinidad = entrenar(new StarterPacMan(), new MyGhosts(q), 10);
	}
	@Override
	public int compareTo(Individuo individuo) {
		// TODO Auto-generated method stub
		return this.afinidad.compareTo(individuo.getAfinidad());
	}
	
	/**
	 * COPIA  de la clase executor
	 * @param pacManController
	 * @param ghostController
	 * @param trials
	 * @return
	 */
	public double entrenar(Controller<MOVE> pacManController,Controller<EnumMap<GHOST,MOVE>> ghostController,int trials)
	    {
	    	double avgScore=0;
	    	
	    	Random rnd=new Random(0);
			Game game;

			for(int i=0;i<trials;i++)
			{
				game=new Game(rnd.nextLong());
				
				while(game.getMazeIndex() == 0 && !game.gameOver())
				{
			        game.advanceGame(pacManController.getMove(game.copy(),System.currentTimeMillis()+DELAY),
			        		ghostController.getMove(game.copy(),System.currentTimeMillis()+DELAY));
				}
				
				avgScore+=game.getScore();
				//System.out.println(i+"\t"+game.getScore());
			}
			
			//System.out.println(avgScore/trials);
			return avgScore/trials;

	    }
}
