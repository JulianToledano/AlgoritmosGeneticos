package pacman.entries.ghosts.genetic;

import static pacman.game.Constants.DELAY;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterPacMan;
import pacman.entries.ghosts.MyGhosts;
import pacman.entries.ghosts.Qlearning;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

// Crear la primera generación 
public class GeneticAlgorithm {
	
	private ArrayList <Individuo> poblacion;
	private int numeroDeIndividuos;
	/**
	 * Constructor
	 * @param numeroDeIndividuos número de individuos en la población
	 */
	public GeneticAlgorithm(int numeroDeIndividuos){
		this.poblacion = new ArrayList<Individuo>();
		this.numeroDeIndividuos = numeroDeIndividuos;
		crearPoblacion(poblacion,numeroDeIndividuos);
		inicializarPoblacion();
	}
	
	/**
	 * Se crean individuos con valores completamente aleatorios.
	 * @param poblacion ArrayList en el que se guardad los individuos creados
	 * @param numeroDeIndividuos número total de individuos que se quiere crear
	 */
	private void crearPoblacion(ArrayList <Individuo> poblacion, int numeroDeIndividuos){
		for(int i = 0; i < numeroDeIndividuos; i++){
			double value = generarValorQ(20);
			double eps = Math.random() * 0.5;
			double alpha = Math.random();
			double gamma = Math.random();
			poblacion.add(new Individuo(value, eps, alpha, gamma));		
		}
	}
	
	/**
	 * Dentro del conjunto de poblacion se juegan 100 partidas y se determina su fitness
	 * como la media de esas 100 partidas
	 */
	public void inicializarPoblacion(){
		// Por cada inidividuo dentro de poblacion
		for(int i = 0; i < poblacion.size(); i++)
			poblacion.get(i).setAfinidad();		
		// Se ordena la población según su fitness
		Collections.sort(poblacion);
	}
	
	/*
	 * Genera la descendencia a partir de la población
	 */
	public void generarDescendencia(){
		ArrayList<Individuo>descendientes = new ArrayList<Individuo>();
		// El mejor individuo pasa a la siguiente generación
		conservarAlpha(descendientes);
		// Se cruzan dos progenitores hasta obtener 9 descendientes
		crucePlano(descendientes, 17);
		// Se crean otros dos individuos de forma aleatoria
		crearPoblacion(descendientes,2);
		// Se muta a cinco individuos de forma aleatoria
		//mutacion(descendientes, 5);
		mutacion(descendientes);
		
		
		
		poblacion.clear();		
		poblacion = new ArrayList<>(descendientes);		
		descendientes.clear();
		inicializarPoblacion();
		for(int i = 0; i < poblacion.size(); i++)
			System.out.println(poblacion.get(i).toString());
	}
	/**
	 * Guarda el individuo con mejor fitness para la siguiente generación
	 * @param descendientes poblacion de descendientes
	 */
	private void conservarAlpha(ArrayList<Individuo>descendientes){
		double[] genesAlpha = poblacion.get(0).getGenotipo();
		descendientes.add(new Individuo(genesAlpha[0],genesAlpha[1],genesAlpha[2],genesAlpha[3]));
	}
	
	/**
	 * Realiza el cruce plano entre dos individuos de la población.
	 * Se genera un único descendiente por cada dos inidividuos. El proceso se repite numCruces veces y cada vez
	 * se eligen dos progenitores por torneo.
	 * @param descendientes arraylist en el que se introducen los descendientes
	 * @param numCruces número de individuos que se quiere generar
	 */
	private void crucePlano(ArrayList<Individuo> descendientes, int numCruces){
		// Se generan tantos descendientes como se quiera
		for(int j = 0; j < numCruces; j++){
			// Se obtienen los padres por torneo.
			double [] genesPadre = torneo();
			double [] genesMadre = torneo();
			double[] genesDescendiente = new double[4];
			// Por cada gen se genera un número aleatorio dentro del límite entre los genes de los padres.
			for(int i = 0; i < 4; i++){
				if(genesPadre[i] > genesMadre[i])
					genesDescendiente[i] = genesMadre[i] + Math.random() * (genesPadre[i] - genesMadre[i]);
				else
					genesDescendiente[i] = genesPadre[i] + Math.random() * (genesMadre[i] - genesPadre[i]);
			}
			descendientes.add(new Individuo(genesDescendiente[0], genesDescendiente[1], genesDescendiente[2], genesDescendiente[3]));
		}
	}
	/**
	 * Elige un progenitor por sorteo dentro de un conjunto de 5 individuos 
	 * de la población elegidos por sorteo.
	 * @return genotipo del progenitor
	 */
	private double[] torneo(){
		double mejor = 10000;
		int individuo = -1;
		for(int i = 0; i < 5; i++){
			int random = (int) (Math.random() * poblacion.size());
			if(poblacion.get(random).getAfinidad() < mejor) {
				mejor = poblacion.get(random).getAfinidad();
				individuo = random;
			}
		}
		 return poblacion.get(individuo).getGenotipo();
	}
	
	/**
	 * Muta un gen aleatorio de un individuo aleatorio
	 * @param descendientes arraylist donde se guardan los descendientes
	 * @param numMutaciones número total de individuos dentro de población que se quieren mutar
	 */
	/*private void mutacion(ArrayList<Individuo>descendientes, int numMutaciones){	
		for(int i = 0; i < numMutaciones; i++){
			int random = (int) (Math.random() * poblacion.size());
			double [] genotipo = poblacion.get(random).getGenotipo();
			int genomaACambiar = (int) (Math.random() * 4);
			System.out.println("Dentro de la mutacion. Individuo a cambiar: " + random + ". Gen a cambiar: " + genomaACambiar);
			if(genomaACambiar == 0)
				genotipo[0] = generarValorQ(20);
			else
				genotipo[genomaACambiar] = Math.random();
			descendientes.add(new Individuo(genotipo[0],genotipo[1],genotipo[2],genotipo[3]));	
		}
	}*/
	private void mutacion(ArrayList<Individuo>descendientes){	
		for(int i = 0; i < descendientes.size(); i++){
			double p = Math.random();
			// Si se da el caso en el que se debe mutar
			if(p < 0.01) {
				double [] genotipo = poblacion.get(i).getGenotipo();
				int genomaACambiar = (int) (Math.random() * 4);
				System.out.println("Dentro de la mutacion. Individuo a cambiar: " + i + ". Gen a cambiar: " + genomaACambiar);
				if(genomaACambiar == 0)
					genotipo[0] = generarValorQ(20);
				else
					genotipo[genomaACambiar] = Math.random();
				descendientes.get(i).setGenotipo(genotipo);
			}
		}
	}
	/**
	 * 
	 * @param max valor máximo aleatorio
	 * @return número aleatorio entre -max y max
	 */
	private int generarValorQ(int max){
		int random = (int) (Math.random() * max);
		random -= max/2;
		return random;
	}
	
	// metodos get
	public double obtenerAfinidadAlpha(){return (poblacion.get(0).getAfinidad());}
	public Qlearning obtenerAlpha(){return (poblacion.get(0).getQ());}
	public double[] obternerGenotipoAlpha(){return poblacion.get(0).getGenotipo();}
	
	public int getNumeroIndividuos(){return poblacion.size();}
}
