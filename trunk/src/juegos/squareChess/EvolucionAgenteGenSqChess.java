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
import org.jgap.impl.IntegerGene;

import juegos.agentes.AgenteGeneticoSqChess;

/** 
 * Obtiene una función heurística óptima para utilizar en el Square Chess.
 * Los individuos se representan mediante vectores de 6 números, representando
 * los siguientes conceptos:
 * 		<li> Factor cantidad de fichas del jugador
 * 		<li> Factor cantidad de fichas del oponente
 * 		<li> Factor cantidad de cuadrados parciales del jugador
 * 		<li> Factor cantidad de cuadrados parciales del oponente
 * 		<li> Factor cantidad de cuadrados completos del jugador
 * 		<li> Factor cantidad de cuadrados completos del oponente
 * <br>
 * La función de aptitud se define como la cantidad de partidas ganadas frente
 * a un agente que utiliza la técnica MiniMax con poda Alfa-Beta. Por lo tanto
 * se busca maximizar esta función para ir obteniendo las heurísticas que más
 * ganen.
 * <br>
 * La población con 100 individuos iniciados al azar.
 * Los operadores genéticos son los de la configuración por defecto de JGAP.
 * La evolución termina cuando se encuentra una solución (aptitud 0) o luego de 
 * 1000 generaciones.
 * 
 * @see AgenteGeneticoSqChess
 */
public class EvolucionAgenteGenSqChess {
	
	public static final int POPULATION_SIZE = 10;
	public static final long LOG_TIME = 100;
	public static final BulkFitnessFunction FITNESS_FUNCTION = new SqChessBulkFitnessFunction();
	
	public static boolean endEvolution(Genotype population, int generation) {
		return generation > 50 || population.getFittestChromosome().getFitnessValue() == 5;
	}
	
	public static void main(String[] args) throws InvalidConfigurationException {
		Configuration conf = new DefaultConfiguration();
		Configuration.reset();
		conf.setFitnessEvaluator(new DefaultFitnessEvaluator());
		conf.setPreservFittestIndividual(true);
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
	    		System.out.println("\tEvolution time: "+ (lastTime - startTime) 
	    				+" ms, generation "+ i +", fittest = "+
	    				Arrays.toString(SqChessBulkFitnessFunction.chromosomeToArray(fittest))
	    				+" with "+ fittest.getFitnessValue() +" fitness.");	
	    	}
	    	population.evolve();
	    }
	    long endTime = System.currentTimeMillis();
	    
	    System.out.println("Total evolution time: "+ (endTime - startTime) +" ms");
	    IChromosome fittest = population.getFittestChromosome();
	    System.out.println("Fittest solution is "+ 
	    		Arrays.toString(SqChessBulkFitnessFunction.chromosomeToArray(fittest)) 
	    		+" with "+ fittest.getFitnessValue() +" fitness.");
	}
}
