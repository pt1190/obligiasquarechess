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
	
	@Override
	public boolean equals(Object object)
	{
		if (object.getClass().equals(Posicion.class))
		{
			Posicion pos = (Posicion)object;
			return (pos.x == this.x) && (pos.y == this.y);
		}
		
		return false;
	}
	
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