package pacman.entries.ghosts.genetic;

import static pacman.game.Constants.DELAY;

import java.util.ArrayList;
import java.util.Collections;
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
	private ArrayList <Individuo> poblacion;
	private int numeroDeIndividuos;
	private double []genotipo;
	
	public GeneticAlgorithm(Game game, int numeroDeIndividuos){
		this.poblacion = new ArrayList<Individuo>();
		this.numeroDeIndividuos = numeroDeIndividuos;
		genotipo = new double[4];
		crearPoblacion(numeroDeIndividuos, game);
	}
	
	
	private void crearPoblacion(int numeroDeIndividuos, Game game){
		for(int i = 0; i < numeroDeIndividuos; i++){
			genotipo[0] = generarValorQ(20);
			genotipo[1] = Math.random();
			genotipo[2] = Math.random();
			genotipo[3] = Math.random();
			poblacion.add(new Individuo(game, genotipo[0], genotipo[1], genotipo[2], genotipo[3]));		
		}
		inicializarPoblacion();
		//for(int i = 0; i < poblacion.size(); i++)
			//System.out.println(poblacion.get(i).getAfinidad());
	}
	
	private void inicializarPoblacion(){
		for(int i = 0; i < numeroDeIndividuos; i++)
			poblacion.get(i).setAfinidad(entrenar(new StarterPacMan(), new MyGhosts(poblacion.get(i).getQ()), 1000));
		Collections.sort(poblacion);
		for(int i = 0; i < numeroDeIndividuos; i++)
			System.out.println(poblacion.get(i).toString() + poblacion.get(i).getAfinidad());
	}
	
	public void generarDescendencia(){
		ArrayList<Individuo>descendientes = new ArrayList<Individuo>();
		
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
			return avgScore/trials;

	    }


}
