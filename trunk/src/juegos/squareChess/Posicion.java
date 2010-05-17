package juegos.squareChess;

/**
 * Representaci�n de una posici�n en el tablero.
 */
public class Posicion
{
	/**
	 * Coordenada x de la posici�n
	 */
	public final int x;
	
	/**
	 * Coordenada y de la posici�n
	 */
	public final int y;
	
	public Posicion(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Retorna una nueva instancia de la clase con 
	 * sus respectivos valores en las coordenadas
	 */
	public Posicion clone()
	{
		return new Posicion(this.x, this.y);
	}
	
	/**
	 * Se realiza un movimiento respecto a las coordenadas
	 * de la instancia. 
	 * @param 	x	Incremento para la coordenada x	
	 * @param 	y	Incremento para la coordenada y
	 * @return		Nueva instancia con las coordenadas nuevas
	 */
	public Posicion mover(int x, int y)
	{
		return new Posicion(this.x + x, this.y + y);
	}
	
	/**
	 * Representaci�n de la posici�n en String.
	 * Se realiza la conversi�n de por ej. (0,0) a A1 para
	 * mantener la coordenada en valores standards.
	 */
	public String toString()
	{
		int posy = this.y + 1;
		return SquareChess.alfabeto.charAt(this.x) + "," + posy;
	}
	
	@Override
	public boolean equals(Object object)
	{
		if (object == null)
			return false;
		
		if (object.getClass().equals(Posicion.class))
		{
			Posicion pos = (Posicion)object;
			return (pos.x == this.x) && (pos.y == this.y);
		}
		
		return false;
	}
	
	/**
	 * TODO Que hace este m�todo?
	 * @param pos1
	 * @param pos2
	 * @return
	 */
	public static Posicion[] linea(Posicion pos1, Posicion pos2)
	{
		double deltaX = Math.abs(pos2.x - pos1.x);
		double deltaY = Math.abs(pos2.y - pos1.y);
        if (deltaX >= deltaY)
		{
            Posicion min = pos2.x < pos1.x ? pos2 : pos1;
            boolean invertido = min.equals(pos2);

			Posicion[] linea = new Posicion[(int)deltaX + 1];
			for (int u = 0; u <= deltaX; u ++)
			{
				int x = min.x + u;
				int y = (int)Math.round(min.y + (u / deltaX) * deltaY);
                if (!invertido)
                    linea[u] = new Posicion(x, y);
                else
                    linea[linea.length - 1 - u] = new Posicion(x, y);
			}
			
			return linea;
		}
		else
		{
            Posicion min = pos2.y < pos1.y ? pos2 : pos1;
            boolean invertido = min.equals(pos2);

			Posicion[] linea = new Posicion[(int)deltaY + 1];
			for (int v = 0; v <= deltaY; v++)
			{
				int y = min.y + v;
				int x = (int)Math.round(min.x + (v / deltaY) * deltaX);
                if (!invertido)
                    linea[v] = new Posicion(x, y);
                else
                    linea[linea.length - 1 - v] = new Posicion(x, y);
			}
			
			return linea;
		}
	}
}