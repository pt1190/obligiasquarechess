package juegos.squareChess;

import java.util.ArrayList;
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
import juegos.torneos.TorneoTodosContraTodos;

/** 
 * Evolución de una heurística para utilizar en el juego Square Chess.
 * Los individuos se representan mediante vectores de 6 números, representando
 * los siguientes conceptos:
 * 		<li> Factor cantidad de fichas del jugador
 * 		<li> Factor cantidad de fichas del oponente
 * 		<li> Factor cantidad de cuadrados parciales del jugador
 * 		<li> Factor cantidad de cuadrados parciales del oponente
 * 		<li> Factor cantidad de cuadrados completos del jugador
 * 		<li> Factor cantidad de cuadrados completos del oponente
 * <br><br>
 * La función de aptitud se define como la cantidad de partidas ganadas frente
 * a un agente que utiliza la técnica MiniMax con poda Alfa-Beta. Por lo tanto
 * se busca maximizar esta función para ir obteniendo las heurísticas que más
 * ganen.
 * <br><br>
 * La población se inicializa con 10 individuos iniciados al azar con factores
 * que varían entre -1 y 1. Como es de esperar, determinados factores pueden 
 * ser beneficiosos y otros contraproducentes, es por eso que se decidió ese
 * rango de valores.
 * <br><br>
 * Los operadores genéticos son los de la configuración por defecto de JGAP.
 * La evolución termina cuando se encuentra una solución (aptitud 15) o luego 
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
	    	if (i % 2 == 0)
	    		sampleGenes[i] = new DoubleGene(conf, 0, 1);
	    	else
	    		sampleGenes[i] = new DoubleGene(conf, -1, 0);
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
	    
	    ArrayList aFittests = new ArrayList();
	    IChromosome[] cromosomas = population.getPopulation().toChromosomes();
	    for (int i = 0; i < cromosomas.length; i++)
	    {
	    	if (cromosomas[i].getFitnessValue() == fittest.getFitnessValue())
	    	{
	    		aFittests.add(new AgenteGeneticoSqChess(SqChessBulkFitnessFunction.chromosomeToArray(cromosomas[i])));
	    	}
	    }
	    AgenteGeneticoSqChess mejorAgente = null;
	    if (aFittests.size() == 1)
	    {
	    	mejorAgente = (AgenteGeneticoSqChess)aFittests.get(0);
	    }
	    else
	    {
		    AgenteGeneticoSqChess[] agentes = new AgenteGeneticoSqChess[aFittests.size()];
		    for (int i = 0; i < aFittests.size(); i++)
		    	agentes[i] = (AgenteGeneticoSqChess)aFittests.get(i);
		    TorneoTodosContraTodos torneo = new TorneoTodosContraTodos(SquareChess.JUEGO, agentes );
		    mejorAgente = agentes[0];
		    for (int i = 1; i < agentes.length; i++)
		    {
		    	if (torneo.partidasGanadas(agentes[i]) > torneo.partidasGanadas(mejorAgente))
		    	{
		    		mejorAgente = agentes[i];
		    	}
		    }
	    }
	    System.out.println("Fittest solution is "+ 
	    		mejorAgente.toString()
	    		+" with "+ fittest.getFitnessValue() +" fitness.");
	    
	    System.out.println();
	    System.out.println("************** Prueba de heurística **************");
	    Partida partida = Partida.completa(SquareChess.JUEGO, 
	    		mejorAgente, 
				new AgenteMiniMaxSqChess());
	    System.out.println(partida.toString());
	    System.out.println(partida.resultados()[0] == 1 ? "Heurística acertada" : "Heurística no acertada");
	}
}
