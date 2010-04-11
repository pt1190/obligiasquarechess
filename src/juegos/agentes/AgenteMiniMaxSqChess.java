package juegos.agentes;

import juegos.base.Agente;
import juegos.base.Estado;
import juegos.base.Jugador;
import juegos.base.Movimiento;
import juegos.squareChess.SquareChess;
import juegos.squareChess.SquareChess.EstadoSqChess;

public class AgenteMiniMaxSqChess implements Agente {
	
	private Jugador jugador;

	public AgenteMiniMaxSqChess() {
	}

	@Override
	public void comienzo(Jugador jugador, Estado estado) {
		this.jugador = jugador;		
	}

	@Override
	public Movimiento decision(Estado estado) {
		AlfaBeta alfaBeta = minimax(estado, new AlfaBeta(-15.0), new AlfaBeta(15.0));
		System.out.println("AB = " + alfaBeta.valor);
		return alfaBeta.getMov();
	}
	
	private AlfaBeta minimax(Estado estado, AlfaBeta alfa, AlfaBeta beta)
	{
		EstadoSqChess estSqChess = (EstadoSqChess)estado;
		Jugador jugadorEstado = estSqChess.getJugador();
		if (jugador == jugadorEstado)
		{
			Double res = estado.resultado(jugador);
			if (res != null)
			{
				return new AlfaBeta(res);
			}
			else
			{
				Jugador oponente = getOther(jugador, estado);
				Movimiento[] movs = estado.movimientos(jugador);
				AlfaBeta ab;
				for (Movimiento mov : movs)
				{
					ab = minimax(estado.siguiente(mov), alfa, beta);
					ab.mov = mov;
					if (ab.valor > alfa.valor)
						alfa = ab;
					if (alfa.valor > beta.valor)
						return alfa;
				}
				return alfa;
			}
		}
		else
		{
			Double res = estado.resultado(jugadorEstado);
			if (res != null)
			{
				return new AlfaBeta(res);
			}
			else
			{
				Movimiento[] movs = estado.movimientos(jugadorEstado);
				AlfaBeta ab;
				for (Movimiento mov : movs)
				{
					ab = minimax(estado.siguiente(mov), alfa, beta);
					ab.mov = mov;
					if (ab.valor < beta.valor)
						beta = ab;
					if (alfa.valor > beta.valor)
						return beta;
				}
				
				return beta;
			}
		}
	}
	
	private Jugador getOther(Jugador j, Estado e){
		Jugador[] jugs = e.jugadores();
		return jugs[0].equals(j) ? jugs[1] : jugs[0];
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
		
		public AlfaBeta(Double v, Movimiento m){
			valor = v;
			mov = m;
		}
		
		public AlfaBeta(Double v){
			valor = v;
			mov = null;
		}

	}

}
