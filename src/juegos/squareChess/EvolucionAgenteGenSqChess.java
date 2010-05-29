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
 * La evolución termina luego de 20 generaciones.
 * 
 * @see AgenteGeneticoSqChess
 */
public class EvolucionAgenteGenSqChess {
	
	public static final int POPULATION_SIZE = 10;
	public static final long LOG_TIME = 10;
	public static final BulkFitnessFunction FITNESS_FUNCTION = new SqChessBulkFitnessFunction();
	
	/**
	 * Indicador de cuando se termina la evolución basado en la cantidad
	 * de generaciones (parametrizado inicialmente en 20 generaciones)
	 * @param 	population	Población actual
	 * @param 	generation	Generación actual
	 * @return True si esa generación sobrepasa el valor determinado
	 */
	public static boolean endEvolution(int generation) {
		return generation > 20;
	}
	
	/**
	 * Programa principal para la evolución de la heurística.
	 */
	public static void main(String[] args) throws InvalidConfigurationException {
		Configuration conf = new DefaultConfiguration();
		Configuration.reset();
		conf.setFitnessEvaluator(new DefaultFitnessEvaluator());
		conf.setPreservFittestIndividual(true);
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
	    for (int i = 1; !endEvolution(i); i++) {	    	
	    	if (System.currentTimeMillis() - lastTime > LOG_TIME) {
	    		lastTime = System.currentTimeMillis();
	    		IChromosome fittest = population.getFittestChromosome();
	    		System.out.println("\tEvolution time: "+ milisToTimeString(lastTime - startTime) 
	    				+", generation "+ i +", fittest = "+
	    				Arrays.toString(SqChessBulkFitnessFunction.chromosomeToArray(fittest))
	    				+" with "+ fittest.getFitnessValue() +" fitness.");	
	    	}
	    	population.evolve();
	    }
	    long endTime = System.currentTimeMillis();

	    System.out.println("Total evolution time: "+ milisToTimeString(endTime - startTime));
	    IChromosome fittest = population.getFittestChromosome();
	    
	    // Se procede a seleccionar a las mejores heurísticas para realizar un
	    // torneo todos contra todos de manera de obtener el mejor de todos.
	    ArrayList<AgenteGeneticoSqChess> aFittests = new ArrayList<AgenteGeneticoSqChess>();
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
		    TorneoTodosContraTodos torneo = new TorneoTodosContraTodos(SquareChess.JUEGO, agentes);
		    torneo.completar();
		    mejorAgente = agentes[0];
		    for (int i = 1; i < agentes.length; i++)
		    {
		    	if (torneo.partidasGanadas(agentes[i]) > torneo.partidasGanadas(mejorAgente))
		    	{
		    		mejorAgente = agentes[i];
		    	}
		    }
	    }
	    System.out.println("Fittest solution is "+ mejorAgente.toString()
	    		+" with "+ fittest.getFitnessValue() +" fitness.");
	    
	    // Se realiza una última prueba de la heurística ganadora contra un
	    // Agente Minimax para testear su efectividad
	    System.out.println();
	    System.out.println("************** Prueba de heurística ganadora **************");
	    Partida partida = Partida.completa(SquareChess.JUEGO, 
	    		mejorAgente, 
				new AgenteMiniMaxSqChess());
	    System.out.println(partida.toString());
	    double result = partida.resultados()[0];
	    System.out.println(result == 1 ? "Heurística acertada" : (result == 0 ? "Heurística equivalente" : "Heurística no acertada"));
	}
	
	/**
	 * Convierte milisegundos a minutos y segundos.
	 * @param milis Cantidad de milisegundos a convertir
	 * @return String con minutos y segundos
	 */
	private static String milisToTimeString(long milis)
	{
		long totalSeconds = milis / 1000;
		long minutes = totalSeconds / 60;
		long seconds = totalSeconds % 60;		
		return minutes + "m " + seconds + "s";
	}
}
