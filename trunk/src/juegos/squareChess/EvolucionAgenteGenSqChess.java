package juegos.squareChess;

import java.util.Arrays;

import org.jgap.BulkFitnessFunction;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.DefaultFitnessEvaluator;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.DoubleGene;

import juegos.Partida;
import juegos.agentes.AgenteGeneticoSqChess;
import juegos.agentes.AgenteMiniMaxSqChess;

/** 
 * Evoluci�n de una heur�stica para utilizar en el juego Square Chess.
 * Los individuos se representan mediante vectores de 6 n�meros, representando
 * los siguientes conceptos:
 * 		<li> Factor cantidad de fichas del jugador
 * 		<li> Factor cantidad de fichas del oponente
 * 		<li> Factor cantidad de cuadrados parciales del jugador
 * 		<li> Factor cantidad de cuadrados parciales del oponente
 * 		<li> Factor cantidad de cuadrados completos del jugador
 * 		<li> Factor cantidad de cuadrados completos del oponente
 * <br><br>
 * La funci�n de aptitud se define como la cantidad de partidas ganadas frente
 * a un agente que utiliza la t�cnica MiniMax con poda Alfa-Beta. Por lo tanto
 * se busca maximizar esta funci�n para ir obteniendo las heur�sticas que m�s
 * ganen.
 * <br><br>
 * La poblaci�n se inicializa con 10 individuos iniciados al azar con factores
 * que var�an entre -1 y 1. Como es de esperar, determinados factores pueden 
 * ser beneficiosos y otros contraproducentes, es por eso que se decidi� ese
 * rango de valores.
 * <br><br>
 * Los operadores gen�ticos son los de la configuraci�n por defecto de JGAP.
 * La evoluci�n termina cuando se encuentra una soluci�n (aptitud 15) o luego 
 * de 20 generaciones.
 * 
 * @see AgenteGeneticoSqChess
 */
public class EvolucionAgenteGenSqChess {
	
	public static final int POPULATION_SIZE = 10;
	public static final long LOG_TIME = 10;
	public static final BulkFitnessFunction FITNESS_FUNCTION = new SqChessBulkFitnessFunction();
	
	public static boolean endEvolution(Genotype population, int generation) {
		return generation > 20;// || population.getFittestChromosome().getFitnessValue() == 15;
	}
	
	public static void main(String[] args) throws InvalidConfigurationException {
		Configuration conf = new DefaultConfiguration();
		Configuration.reset();
		conf.setFitnessEvaluator(new DefaultFitnessEvaluator());
		conf.setBulkFitnessFunction(FITNESS_FUNCTION);
		
		Gene[] sampleGenes = new Gene[6];
	    for (int i = 0; i < 6; i++) {
	    	sampleGenes[i] = new DoubleGene(conf, -1, 1);
	    }
	    IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);
	    conf.setSampleChromosome(sampleChromosome);
	    
	    conf.setPopulationSize(POPULATION_SIZE);
	    Genotype population = Genotype.randomInitialGenotype(conf);

	    System.out.println("Evolving.");
	    long startTime = System.currentTimeMillis();
	    long lastTime = startTime;
	    for (int i = 1; !endEvolution(population, i); i++) {	    	
	    	if (System.currentTimeMillis() - lastTime > LOG_TIME) {
	    		lastTime = System.currentTimeMillis();
	    		IChromosome fittest = population.getFittestChromosome();
	    		System.out.println("\tEvolution time: "+ (lastTime - startTime) / 1000 
	    				+" sec, generation "+ i +", fittest = "+
	    				Arrays.toString(SqChessBulkFitnessFunction.chromosomeToArray(fittest))
	    				+" with "+ fittest.getFitnessValue() +" fitness.");	
	    	}
	    	population.evolve();
	    }
	    long endTime = System.currentTimeMillis();
	    
	    System.out.println("Total evolution time: "+ ((endTime - startTime) / 1000) +" sec");
	    IChromosome fittest = population.getFittestChromosome();
	    System.out.println("Fittest solution is "+ 
	    		Arrays.toString(SqChessBulkFitnessFunction.chromosomeToArray(fittest)) 
	    		+" with "+ fittest.getFitnessValue() +" fitness.");
	    
	    System.out.println();
	    System.out.println("************** Prueba de heur�stica **************");
	    Partida partida = Partida.completa(SquareChess.JUEGO, 
	    		new AgenteGeneticoSqChess(SqChessBulkFitnessFunction.chromosomeToArray(fittest)), 
				new AgenteMiniMaxSqChess());
	    System.out.println(partida.toString());
	    System.out.println(partida.resultados()[0] == 1 ? "Heur�stica acertada" : "Heur�stica no acertada");
	}
}
