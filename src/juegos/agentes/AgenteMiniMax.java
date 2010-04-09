package juegos.agentes;

import juegos.base.Agente;
import juegos.base.Estado;
import juegos.base.Jugador;
import juegos.base.Movimiento;

public class AgenteMiniMax implements Agente {
	
	private Jugador jugador;

	public AgenteMiniMax() {
	}

	@Override
	public void comienzo(Jugador jugador, Estado estado) {
		this.jugador = jugador;		
	}

	@Override
	public Movimiento decision(Estado estado) {
		@SuppressWarnings("unused")
		Double value = alfaBeta(estado, Double.MIN_VALUE, Double.MAX_VALUE);
		return null;
	}

	private Double alfaBeta(Estado estado, Double alfa, Double beta) {
		Double r = estado.resultado(jugador);
		if(r != null)
			return r;
		else
		{
			Movimiento[] movs = estado.movimientos(jugador);
			for(Movimiento m : movs)
			{
				if(jugador.equals(m.jugador()))
				{
					alfa = maximo(alfa,alfaBeta(m.estado(), alfa, beta));
					if(alfa >= beta)
						return beta;
					return alfa;
				}
				else
				{
					beta = minimo(beta,alfaBeta(m.estado(), alfa, beta));
					if(alfa >= beta)
						return alfa;
					return beta;
				}
			}
			return null;
		}
	}

	private Double minimo(Double alfa, Double beta) {
		return alfa <= beta ? alfa : beta;
	}

	private Double maximo(Double alfa, Double beta) {
		return alfa >= beta ? alfa : beta;
	}

	@Override
	public void fin(Estado estado) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Jugador jugador() {
		return jugador;
	}

	@Override
	public void movimiento(Movimiento movimiento, Estado estado) {
		// TODO Auto-generated method stub
		
	}

}
