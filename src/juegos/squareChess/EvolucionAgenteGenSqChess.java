package juegos.squareChess;

import java.util.Arrays;

import org.jgap.BulkFitnessFunction;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.DeltaFitnessEvaluator;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.DoubleGene;
import juegos.agentes.AgenteGeneticoSqChess;

/** 
 * Obtiene una funci�n heur�stica �ptima para utilizar en el Square Chess.
 * Los individuos se representan mediante vectores de 6 n�meros, representando
 * los siguientes conceptos:
 * 		<li> Factor cantidad de fichas del jugador
 * 		<li> Factor cantidad de fichas del oponente
 * 		<li> Factor cantidad de cuadrados parciales del jugador
 * 		<li> Factor cantidad de cuadrados parciales del oponente
 * 		<li> Factor cantidad de cuadrados completos del jugador
 * 		<li> Factor cantidad de cuadrados completos del oponente
 * <br>
 * La funci�n de aptitud se define como la cantidad de partidas ganadas frente
 * a un agente que utiliza la t�cnica MiniMax con poda Alfa-Beta. Por lo tanto
 * se busca maximizar esta funci�n para ir obteniendo las heur�sticas que m�s
 * ganen.
 * <br>
 * La poblaci�n con 100 individuos iniciados al azar.
 * Los operadores gen�ticos son los de la configuraci�n por defecto de JGAP.
 * La evoluci�n termina cuando se encuentra una soluci�n (aptitud 0) o luego de 
 * 1000 generaciones.
 * 
 * @see AgenteGeneticoSqChess
 */
public class EvolucionAgenteGenSqChess {
	
	public static final int POPULATION_SIZE = 100;
	public static final long LOG_TIME = 100;
	public static final BulkFitnessFunction FITNESS_FUNCTION = new SqChessBulkFitnessFunction();
	
	public static boolean endEvolution(Genotype population, int generation) {
		return generation > 1000 || population.getFittestChromosome().getFitnessValue() == 10;
	}
	
	public static void main(String[] args) throws InvalidConfigurationException {
		Configuration conf = new DefaultConfiguration();
		Configuration.reset();
		conf.setFitnessEvaluator(new DeltaFitnessEvaluator());
		conf.setBulkFitnessFunction(FITNESS_FUNCTION);
		
		Gene[] sampleGenes = new Gene[6];
	    for (int i = 0; i < 6; i++) {
	    	sampleGenes[i] = new DoubleGene(conf, -1, 1);
	    }
	    IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);
	    conf.setSampleChromosome(sampleChromosome);
	    conf.setSelectFromPrevGen(0.1);
	    conf.setPreservFittestIndividual(true);
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
