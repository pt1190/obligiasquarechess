package juegos.agentes;

import juegos.base.Agente;
import juegos.base.Estado;
import juegos.base.Jugador;
import juegos.base.Movimiento;

public class AgenteMiniMax implements Agente {
	
	public int profundidad;

	public AgenteMiniMax(int i) {
		this.profundidad = i;
	}

	@Override
	public void comienzo(Jugador jugador, Estado estado) {
		alfaBeta(jugador, estado, Integer.MIN_VALUE, Integer.MAX_VALUE);		
	}

	private void alfaBeta(Jugador jugador, Estado estado, int alfa,	int beta) {
		//if(estado.resultado(jugador) == null)
			//return estado.resultado(jugador);
		
	}

	@Override
	public Movimiento decision(Estado estado) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fin(Estado estado) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Jugador jugador() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void movimiento(Movimiento movimiento, Estado estado) {
		// TODO Auto-generated method stub
		
	}

}
