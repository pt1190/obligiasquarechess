package juegos.agentes;

import juegos.base.Agente;
import juegos.base.Estado;
import juegos.base.Jugador;
import juegos.base.Movimiento;

public class AgenteMiniMaxTateti implements Agente {
	
	private Jugador jugador;

	public AgenteMiniMaxTateti() {
	}

	@Override
	public void comienzo(Jugador jugador, Estado estado) {
		this.jugador = jugador;		
	}

	@Override
	public Movimiento decision(Estado estado) {
		long init = System.currentTimeMillis();
		
		AlfaBeta alfaBeta = max(estado, jugador, new AlfaBeta(-900.0), new AlfaBeta(900.0));
		
		long fin = System.currentTimeMillis();
		double deltaSecs = (fin - init) / 100.0;
		System.out.println("Tiempo decision = " + deltaSecs);
		return alfaBeta.getMov();
	}
	
	private AlfaBeta max(Estado estado, Jugador jugadorActual, AlfaBeta alfa, AlfaBeta beta)
	{
		Double res = estado.resultado(jugador);
		if (res != null)
		{
			return new AlfaBeta(res, null, alfa.profundidad);
		}
		else
		{
			Jugador oponente = getOther(jugadorActual, estado);
			Movimiento[] movs = estado.movimientos(jugadorActual);
			AlfaBeta aux;
			for (Movimiento mov : movs)
			{
				aux = min(estado.siguiente(mov), oponente, alfa.sig(), beta.sig());
				aux.mov = mov;
				if(aux.valor > alfa.valor || (aux.valor == alfa.valor && aux.profundidad < alfa.profundidad)){
					alfa = aux;
				}
				if (beta.valor <= alfa.valor)
					return alfa;
			}
			return alfa;
		}
	}
	
	private AlfaBeta min(Estado estado, Jugador jugadorActual, AlfaBeta alfa, AlfaBeta beta)
	{
		Double res = estado.resultado(jugador);
		if (res != null)
		{
			return new AlfaBeta(res, null, beta.profundidad);
		}
		else
		{
			Jugador oponente = getOther(jugadorActual, estado);
			Movimiento[] movs = estado.movimientos(jugadorActual);
			for (Movimiento mov : movs)
			{
				AlfaBeta aux = max(estado.siguiente(mov), oponente, alfa.sig(), beta.sig());
				aux.mov = mov;
				if(aux.valor < beta.valor || (aux.valor == beta.valor && aux.profundidad < beta.profundidad)){
					beta = aux;
				}
				if (beta.valor <= alfa.valor)
					return beta;
			}			
			return beta;
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
			profundidad = 0;
		}
		
		public AlfaBeta sig()
		{
			return new AlfaBeta(this.valor, this.mov, this.profundidad++);
		}

	}

}
