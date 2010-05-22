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
 * Funci�n de aptitud para la evoluci�n de una heur�stica.
 * Se implement� como un BulkFitnessFunction ya que dado la
 * estrateg�a de determinar que un cromosoma es mejor que otro
 * viene dado por el resultado de los dem�s cromosomas por lo
 * que la asignaci�n de aptitud depende de la poblaci�n.
 * 
 * @see EvolucionAgenteSqChess
 */
public class SqChessBulkFitnessFunction extends BulkFitnessFunction {

	private static final long serialVersionUID = 2162546917792237253L;
	
	private static final double porcentajeCompetencia = 0.4;
	
	private static final int minCompetidores = 0;

	@Override
	public void evaluate(Population pop){
		IChromosome[] cromosomas = pop.toChromosomes();
		
		int nPartidas = Math.max((int)Math.round(cromosomas.length * porcentajeCompetencia), minCompetidores);
		
		int[] aJugados = new int[nPartidas];
		
		for (int k = 0; k < cromosomas.length; k++)
		{
			//Reinicar array de indices de jugadores contra los cuales ya jug�
			for (int i = 0; i < nPartidas; i++)
			{
				aJugados[i] = -1;
			}
			
			//jugador actual
			Agente jugador = new AgenteGeneticoSqChess(chromosomeToArray(cromosomas[k]));;
			
			int partidasGanadas = 0;
			
			//Partidas contra oponentes
			for (int j = 0; j < nPartidas; j++)
			{
				Agente oponente;
				int idxOp = k;
				boolean hayOponente = false;
				while (!hayOponente)
				{
					//Elegir oponente
					idxOp = (int)Math.round(Math.random() * (cromosomas.length + 1));
					
					//Fijarse que no sea si mismo
					hayOponente = idxOp != k;
					
					//Revisar que no se juegue contra el mismo si hay suficientes
					if (nPartidas <= cromosomas.length)
					{
						for (int i = 0; i < j; i++)
						{
							if (!hayOponente || aJugados[i] == idxOp)
							{
								hayOponente = false;
								break;
							}
						}
					}
						
				}
				
				//Agregar al array contra quien jug�
				aJugados[j] = idxOp;
				
				//Si es un indice dentro del array tomar el agente
				if (idxOp < cromosomas.length)
				{					
					oponente = new AgenteGeneticoSqChess(chromosomeToArray(cromosomas[idxOp]));
				}
				//Sino usar el agente minimax
				else if (idxOp == cromosomas.length)
				{
					oponente = new AgenteMiniMaxSqChess();
				}
				// o el aleatorio
				else
				{
					oponente = new AgenteAleatorio();
				}
				
				//Partida
				Partida partida = Partida.completa(SquareChess.JUEGO, jugador, oponente);
				//Si gana sumar el resultado
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
