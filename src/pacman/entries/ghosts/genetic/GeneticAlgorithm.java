package pacman.entries.ghosts.genetic;

import static pacman.game.Constants.DELAY;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterPacMan;
import pacman.entries.ghosts.MyGhosts;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

// Crear la primera generación 
public class GeneticAlgorithm {
	ArrayList <Individuo> poblacion;
	private int numeroDeIndividuos;
	
	public GeneticAlgorithm(Game game, int numeroDeIndividuos){
		this.poblacion = new ArrayList<Individuo>();
		this.numeroDeIndividuos = numeroDeIndividuos;
		crearPoblacion(numeroDeIndividuos, game);
	}
	
	public void inicializarPoblacion(){
		for(int i = 0; i < numeroDeIndividuos; i++){
			poblacion.get(i).setAfinidad(entrenar(new StarterPacMan(), new MyGhosts(poblacion.get(i).getQ()), 100));
			System.out.println(poblacion.get(i).toString() + poblacion.get(i).getAfinidad());
		}
			
	}
	private void crearPoblacion(int numeroDeIndividuos, Game game){
		for(int i = 0; i < numeroDeIndividuos; i++){
			double value = generarValorQ(20);
			double eps = Math.random();
			double alpha = Math.random();
			double gamma = Math.random();
			poblacion.add(new Individuo(game, value, eps, alpha, gamma));
			
		}
		
		//for(int i = 0; i < poblacion.size(); i++)
			//System.out.println(poblacion.get(i).getAfinidad());
	}
	
	private int generarValorQ(int max){
		int random = (int) (Math.random() * max);
		random -= max/2;
		return random;
	}
	
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
			return avgScore;

	    }


}
