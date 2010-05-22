package juegos.squareChess;

import juegos.Partida;
import juegos.agentes.AgenteAleatorio;
import juegos.agentes.AgenteGeneticoSqChess;
import juegos.agentes.AgenteMiniMaxSqChess;
import juegos.base.Agente;

import org.jgap.BulkFitnessFunction;
import org.jgap.IChromosome;
import org.jgap.Population;

/**
 * Función de aptitud para la evolución de una heurística.
 * Se implementó como un BulkFitnessFunction ya que dado la
 * estrategía de determinar que un cromosoma es mejor que otro
 * viene dado por el resultado de los demás cromosomas por lo
 * que la asignación de aptitud depende de la población.
 * 
 * @see EvolucionAgenteSqChess
 */
public class SqChessBulkFitnessFunction extends BulkFitnessFunction {

	private static final long serialVersionUID = 2162546917792237253L;
	
	private static final double porcentajeCompetencia = 0.2;
	
	private static final int minCompetidores = 6;

	@Override
	public void evaluate(Population pop){
		IChromosome[] cromosomas = pop.toChromosomes();
		
		int nPartidas = Math.max((int)Math.round(cromosomas.length * porcentajeCompetencia), minCompetidores);
		for (int k = 0; k < cromosomas.length; k++)
		{
			Agente jugador = new AgenteGeneticoSqChess(chromosomeToArray(cromosomas[k]));;
			int partidasGanadas = 0;
			
			for (int j = 0; j < nPartidas; j++)
			{
				Agente oponente;
				int idxOp = k;
				while (idxOp == k)
					idxOp = (int)Math.round(Math.random() * (cromosomas.length + 1));
				if (idxOp < cromosomas.length)
				{					
					oponente = new AgenteGeneticoSqChess(chromosomeToArray(cromosomas[idxOp]));
				}
				else if (idxOp == cromosomas.length)
				{
					oponente = new AgenteMiniMaxSqChess();
				}
				else
				{
					oponente = new AgenteAleatorio();
				}
				
				Partida partida = Partida.completa(SquareChess.JUEGO, jugador, oponente);
				if (partida.resultados()[0] == 1)
				{
					partidasGanadas++;
				}    				
			}
			cromosomas[k].setFitnessValue(partidasGanadas);
		}		
	}
	
	public static final double[] chromosomeToArray(IChromosome chromosome) {
		double[] genes = new double[6];
		for (int i = 0; i < 6; i++) {
			genes[i] = (Double)chromosome.getGene(i).getAllele();
		}
		return genes;
	}

}
