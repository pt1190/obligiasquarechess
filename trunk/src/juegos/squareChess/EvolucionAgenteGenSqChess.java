package juegos.squareChess;

import org.jgap.BulkFitnessFunction;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.DeltaFitnessEvaluator;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.DoubleGene;
import juegos.agentes.AgenteAleatorio;
import juegos.agentes.AgenteMiniMaxSqChess;
import juegos.agentes.AgenteGeneticoSqChess;
import juegos.base.Agente;
import juegos.Partida;

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
	
	public static final BulkFitnessFunction FITNESS_FUNCTION = new SqChessBulkFitnessFunction();
	
	public static AgenteGeneticoSqChess EvolucionarAgenteGenSqChess(int poblacionInicial, int generaciones) throws Exception
	{
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
	    conf.setPopulationSize(poblacionInicial);
	    Genotype population = Genotype.randomInitialGenotype(conf);

	    System.out.println("Evolving.");
	    long startTime = System.currentTimeMillis();
	    for (int i = 1; i < generaciones; i++) {
	    	
    		// Competencias
    		IChromosome[] cromosomas = population.getPopulation().toChromosomes();
    		for (int k = 0; k < cromosomas.length; k++)
    		{
    			Agente jugador = new AgenteGeneticoSqChess(chromosomeToArray(cromosomas[k]));;
    			int partidasGanadas = 0;
    			for (int j = 0; j < cromosomas.length / 10; j++)
    			{
    				Agente oponente;
    				int idxOp = k;
    				while (idxOp == k)
    					idxOp = (int)Math.round(Math.random() * (cromosomas.length + 1));

    				if (idxOp < cromosomas.length)
    				{
    					try
    					{
    						oponente = new AgenteGeneticoSqChess(chromosomeToArray(cromosomas[idxOp]));
    					}
    					catch (Exception e)
    					{
    						throw new Exception("Error al crear agente genetico");
    					}
    				}
    				else if (idxOp == cromosomas.length)
    				{
    					oponente = new AgenteMiniMaxSqChess();
    				}
    				else
    				{
    					oponente = new AgenteAleatorio();
    				}
    				
    				Partida partida = new Partida(SquareChess.JUEGO, jugador, oponente);
    				if (partida.resultados()[0] == 1)
    				{
    					partidasGanadas++;
    				}
    				
    			}
    			
    			cromosomas[k].setFitnessValue(partidasGanadas);
    		}

	    	population.evolve();
	    }
	    long endTime = System.currentTimeMillis();
	    System.out.println("Fin Evoluci�n. Tiempo = " + (endTime-startTime));
	    
	    return new AgenteGeneticoSqChess(chromosomeToArray(population.getFittestChromosome()));
	}
	
	public static final double[] chromosomeToArray(IChromosome chromosome) {
		double[] genes = new double[6];
		for (int i = 0; i < 6; i++) {
			genes[i] = (Double)chromosome.getGene(i).getAllele();
		}
		return genes;
	}
	
	public static void main(String[] args) throws Exception
	{
		AgenteGeneticoSqChess best = EvolucionarAgenteGenSqChess(100, 1000);
		System.out.println(best.toString());
	}
}
