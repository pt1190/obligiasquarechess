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
		AlfaBeta alfaBeta = max(estado, jugador, new AlfaBeta(-15.0), new AlfaBeta(15.0));
		System.out.println("AB = " + alfaBeta.valor);
		return alfaBeta.getMov();
	}
	
	private AlfaBeta max(Estado estado, Jugador jugador, AlfaBeta alfa, AlfaBeta beta)
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
				ab = min(estado.siguiente(mov), oponente, alfa, beta);
				ab.mov = mov;
				if (ab.valor > alfa.valor)
					alfa = ab;
				if (alfa.valor >= beta.valor)
					return alfa;
			}
			return alfa;
		}
	}
	
	private AlfaBeta min(Estado estado, Jugador jugador, AlfaBeta alfa, AlfaBeta beta)
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
				ab = max(estado.siguiente(mov), oponente, alfa, beta);
				ab.mov = mov;
				if (ab.valor < beta.valor)
					beta = ab;
				if (alfa.valor >= beta.valor)
					return beta;
			}
			
			return beta;
		}
	}
	
	private Jugador getOther(Jugador j, Estado e){
		Jugador[] jugs = e.jugadores();
		return jugs[0].equals(j) ? jugs[1] : jugs[0];
	}

	private AlfaBeta minimo(AlfaBeta alfa, AlfaBeta beta) {
		return alfa.getValor() <= beta.getValor() ? alfa : beta;
	}

	private AlfaBeta maximo(AlfaBeta alfa, AlfaBeta beta) {
		return alfa.getValor() >= beta.getValor() ? alfa : beta;
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
