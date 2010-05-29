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
 * La evoluci�n termina luego de 20 generaciones.
 * 
 * @see AgenteGeneticoSqChess
 */
public class EvolucionAgenteGenSqChess {
	
	public static final int POPULATION_SIZE = 10;
	public static final long LOG_TIME = 10;
	public static final BulkFitnessFunction FITNESS_FUNCTION = new SqChessBulkFitnessFunction();
	
	/**
	 * Indicador de cuando se termina la evoluci�n basado en la cantidad
	 * de generaciones (parametrizado inicialmente en 20 generaciones)
	 * @param 	population	Poblaci�n actual
	 * @param 	generation	Generaci�n actual
	 * @return True si esa generaci�n sobrepasa el valor determinado
	 */
	public static boolean endEvolution(int generation) {
		return generation > 20;
	}
	
	/**
	 * Programa principal para la evoluci�n de la heur�stica.
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
	    
	    // Se procede a seleccionar a las mejores heur�sticas para realizar un
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
	    
	    // Se realiza una �ltima prueba de la heur�stica ganadora contra un
	    // Agente Minimax para testear su efectividad
	    System.out.println();
	    System.out.println("************** Prueba de heur�stica ganadora **************");
	    Partida partida = Partida.completa(SquareChess.JUEGO, 
	    		mejorAgente, 
				new AgenteMiniMaxSqChess());
	    System.out.println(partida.toString());
	    double result = partida.resultados()[0];
	    System.out.println(result == 1 ? "Heur�stica acertada" : (result == 0 ? "Heur�stica equivalente" : "Heur�stica no acertada"));
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
