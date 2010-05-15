package juegos.squareChess;

import java.util.Arrays;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.DeltaFitnessEvaluator;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.DoubleGene;


import juegos.agentes.AgenteAleatorio;
import juegos.agentes.AgenteMiniMaxSqChess;
import juegos.agentes.AgenteGeneticoSqChess;
import juegos.base.Agente;
import juegos.Partida;
public class EvolucionAgente {
	
	public static AgenteGeneticoSqChess EvolucionarAgenteSqChess(int poblacionInicial, int generaciones) throws Exception
	{
		Configuration conf = new DefaultConfiguration();
		Configuration.reset();
		conf.setFitnessEvaluator(new DeltaFitnessEvaluator());
		
		Gene[] sampleGenes = new Gene[6];
	    for (int i = 0; i < 6; i++) {
	    	sampleGenes[i] = new DoubleGene(conf, -1, 1);
	    }
	    IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);
	    conf.setSampleChromosome(sampleChromosome);
	    conf.setSelectFromPrevGen(10);
	    conf.setPreservFittestIndividual(true);
	    conf.setPopulationSize(poblacionInicial);
	    Genotype population = Genotype.randomInitialGenotype(conf);

	    System.out.println("Evolving.");
	    long startTime = System.currentTimeMillis();
	    for (int i = 1; i < generaciones; i++) {
	    	
    		//Competencias
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
	    System.out.println("Fin Evolución. Tiempo = " + (endTime-startTime));
	    
	    return new AgenteGeneticoSqChess(chromosomeToArray(population.getFittestChromosome()));
	}
	
	public static final double[] chromosomeToArray(IChromosome chromosome) {
		double[] genes = new double[6];
		for (int i = 0; i < 6; i++) {
			genes[i] = (Double)chromosome.getGene(i).getAllele();
		}
		return genes;
	}
}
