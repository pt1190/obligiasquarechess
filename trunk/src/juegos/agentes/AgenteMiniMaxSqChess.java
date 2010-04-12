package juegos.agentes;

import juegos.base.Agente;
import juegos.base.Estado;
import juegos.base.Jugador;
import juegos.base.Movimiento;
import juegos.squareChess.SquareChess.EstadoSqChess;

public class AgenteMiniMaxSqChess implements Agente {
	
	private Jugador jugador;

	private int profundidadMax = 20;
	
	public AgenteMiniMaxSqChess() {
	}

	@Override
	public void comienzo(Jugador jugador, Estado estado) {
		this.jugador = jugador;		
	}

	@Override
	public Movimiento decision(Estado estado) {
		AlfaBeta alfaBeta = minimax(estado, new AlfaBeta(-100.0), new AlfaBeta(100.0));
		System.out.println("AB = " + alfaBeta.valor);
		return alfaBeta.getMov();
	}
	
	private AlfaBeta minimax(Estado estado, AlfaBeta alfa, AlfaBeta beta)	
	{
		if (alfa.profundidad >= profundidadMax)
			return new AlfaBeta(0.0);
		Double res = estado.resultado(jugador);
		if (res != null)
		{
			return new AlfaBeta(res);
		}
		
		EstadoSqChess estSqChess = (EstadoSqChess)estado;
		Jugador jugadorEstado = estSqChess.getJugador();
		
		if (jugador.equals(jugadorEstado))
		{
			Movimiento[] movs = estado.movimientos(jugador);
			AlfaBeta ab;
			for (Movimiento mov : movs)
			{
				ab = minimax(estado.siguiente(mov), alfa.sig(), beta.sig());
				ab.mov = mov;
				if (ab.valor > alfa.valor  || (ab.valor == alfa.valor && ab.profundidad < alfa.profundidad))
					alfa = ab;
				if (alfa.valor >= beta.valor)
					return alfa;
			}
			return alfa;
		}
		else
		{
			Movimiento[] movs = estado.movimientos(jugadorEstado);
			AlfaBeta ab;
			for (Movimiento mov : movs)
			{
				ab = minimax(estado.siguiente(mov), alfa.sig(), beta.sig());
				ab.mov = mov;
				if (ab.valor < beta.valor  || (ab.valor == beta.valor && ab.profundidad < beta.profundidad))
					beta = ab;
				if (alfa.valor > beta.valor)
					return beta;
			}
			
			return beta;
		}
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
	
	public class AlfaBeta {
		
		private Movimiento mov;
		private Double valor; // Valor correspondiente al estado final contenido en el movimiento
		private int profundidad;
		
		public Movimiento getMov() {
			return mov;
		}

		public void setMov(Movimiento mov) {
			this.mov = mov;
		}

		public Double getValor() {
			return valor;
		}

		public void setValor(Double valor) {
			this.valor = valor;
		}
		
		public AlfaBeta(Double v, Movimiento m, int prof){
			valor = v;
			mov = m;
			profundidad = prof;
		}
		
		public AlfaBeta(Double v){
			valor = v;
			mov = null;
			profundidad = 1;
		}
		
		public AlfaBeta sig()
		{
			return new AlfaBeta(this.valor, this.mov, this.profundidad+1);
		}

	}
}
