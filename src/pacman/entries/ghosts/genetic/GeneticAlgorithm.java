package pacman.entries.ghosts.genetic;

import java.util.ArrayList;

import pacman.game.Game;

// Crear la primera generación 
public class GeneticAlgorithm {
	ArrayList <Individuo> poblacion;
	
	public GeneticAlgorithm(Game game, int numeroDeIndividuos){
		poblacion = new ArrayList<Individuo>();
		crearPoblacion(numeroDeIndividuos, game);
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


}
