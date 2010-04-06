package juegos.squareChess;

public class Posicion
{
	public final int x;
	public final int y;
	
	public Posicion(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Posicion clone()
	{
		return new Posicion(this.x, this.y);
	}
	
	public Posicion mover(int x, int y)
	{
		return new Posicion(this.x + x, this.y + y);
	}
	
	public String toString()
	{
		return "(" + this.x + "," + this.y + ")";
	}
	
	public static Posicion[] linea(Posicion pos1, Posicion pos2)
	{
		double deltaX = pos2.x - pos1.x;
		double deltaY = pos2.y - pos1.y;
		if (Math.abs(deltaX) >= Math.abs(deltaY))
		{
			Posicion[] linea = new Posicion[(int)deltaX + 1];
			int dir = (int)(deltaX / Math.abs(deltaX));
			for (int u = 0; u <= deltaX; u += dir)
			{
				int x = pos1.x + u;
				int y = (int)Math.round(pos1.y + (u / deltaX) * deltaY);
				linea[u] = new Posicion(x, y);
			}
			
			return linea;
		}
		else
		{
			Posicion[] linea = new Posicion[(int)deltaY + 1];
			int dir = (int)(deltaY / Math.abs(deltaY));
			for (int v = 0; v <= deltaY; v += dir)
			{
				int y = pos1.y + v;
				int x = (int)Math.round(pos1.x + (v / deltaY) * deltaX);
				linea[v] = new Posicion(x, y);
			}
			
			return linea;
		}
	}
}