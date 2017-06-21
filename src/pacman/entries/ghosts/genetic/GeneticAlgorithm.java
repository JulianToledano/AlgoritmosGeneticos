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
	
	public GeneticAlgorithm(Game game, int numeroDeIndividuos){
		this.poblacion = new ArrayList<Individuo>();
		this.numeroDeIndividuos = numeroDeIndividuos;
		crearPoblacion(numeroDeIndividuos);
	}
	
	
	private void crearPoblacion(int numeroDeIndividuos){
		for(int i = 0; i < numeroDeIndividuos; i++){
			double value = generarValorQ(20);
			double eps = Math.random();
			double alpha = Math.random();
			double gamma = Math.random();
			poblacion.add(new Individuo(value, eps, alpha, gamma));		
		}
		inicializarPoblacion();
		//for(int i = 0; i < poblacion.size(); i++)
			//System.out.println(poblacion.get(i).getAfinidad());
	}
	
	private void inicializarPoblacion(){
		for(int i = 0; i < numeroDeIndividuos; i++)
			poblacion.get(i).setAfinidad(entrenar(new StarterPacMan(), new MyGhosts(poblacion.get(i).getQ()), 100));
		Collections.sort(poblacion);
		for(int i = 0; i < numeroDeIndividuos; i++)
			System.out.println(poblacion.get(i).toString() + poblacion.get(i).getAfinidad());
	}
	
	public void generarDescendencia(){
		ArrayList<Individuo>descendientes = new ArrayList<Individuo>();
		// Sacamos 5 descendientes a través del cruce aritmético
		cruceAritmetico(descendientes);
		for(int i = 0; i < descendientes.size(); i++)
			System.out.println(descendientes.get(i).toString() + descendientes.get(i).getAfinidad());
	}
	
	private void cruceAritmetico(ArrayList<Individuo> descendientes){
		// Por cada dos se combinan
		for(int i = 0; i < numeroDeIndividuos/3; i++){
			calcularCruceAritmetico(descendientes, poblacion.get(0),poblacion.get(1),0.4);
			poblacion.remove(0);
			poblacion.remove(0);
		}
		
	}
	
	private void calcularCruceAritmetico(ArrayList<Individuo>descendientes, Individuo progenitor, Individuo progenitor1, double r){
		double [] genesProgenitor = progenitor.getGenotipo();
		double [] genesProgenitor1 = progenitor1.getGenotipo();
		// Primer desdenciente
		double []genes = new double[4];
		double []genes1 = new double[4];
		for(int i = 0; i < 4; i++){
			genes[i] = r * genesProgenitor[i] + (1 - r) * genesProgenitor1[i];
			genes1[i] = r * genesProgenitor1[i] + (1 - r) * genesProgenitor[i];
		}
		descendientes.add(new Individuo(genes[0], genes[1], genes[2], genes[3]));
		descendientes.add(new Individuo(genes1[0], genes1[1], genes1[2], genes1[3]));
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
