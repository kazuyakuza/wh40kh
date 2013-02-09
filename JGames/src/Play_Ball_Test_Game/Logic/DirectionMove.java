package Play_Ball_Test_Game.Logic;

import java.util.Hashtable;

/**
 * Clase para archivar las direcciones posibles de movimiento de las entidades.
 * 
 * Las direcciones posibles dependen de la dimensión de la entidad.
 * Si la posición inicial de la entidad es (x0,y0), entonces la entidad tiene 360° de direcciones posibles.
 * Si el tamaño de cada camino posible a tomar es T, entonces el total de caminos/direcciones posibles será 360/T.
 * Cada dirección es calculada con un valor de grado. Las primeras direcciones son caculadas, partiendo de 0°, cada 45°.
 * Si aún hay caminos posibles, entonces las siguientes direcciones son calculadas cada 45°/2, y así siguiendo hasta calcular cada no menos de 10°.
 * os valores guardados en el tabla serán 2 números double, por los cuales multiplicar la velocidad de movimiento de la entidad.
 * 
 * Ej: si la entidad e posee velX = 4, velY = 4, y la nueva dirección es, desde la posición actual, a 90°, entonces se calcula velX * 0 y velY * 1.
 * 
 * @author Kazuya
 */
public class DirectionMove
{
	
	//Variable de Clase
	private static DirectionMove dirMove = new DirectionMove ();
	
	//Variables de Instancia
	private Hashtable<String, Hashtable<Double, double[]>> table;
	
	/* CONSTRUCTOR */
	
	/**
	 * Crea la Tabla Hash.
	 */
	@SuppressWarnings("unchecked")
	private DirectionMove ()
	{
		table = (Hashtable<String, Hashtable<Double, double[]>>) new Hashtable ();
	}
	
	/* COMANDOS */
	
	/**
	 * Crear una Tabla Hash con las Direcciones de la Entidad indicada.
	 * 
	 * @param s Nombre del Tipo de Entidad.
	 * @param size Tamaño en pixeles que deben tener los caminos posibles de cada dirección posible de la Entidad e.
	 */
	@SuppressWarnings("unchecked")
	public void defineDirectionsMoves (String s, int size)
	{
		int maxDirs = 360/size;
		double gradoMax = 360;
		double grado=0;
		int dirs=1;
		int actDiv = 1;
		double gradoBase = 45.0;
		Hashtable<Double, double[]> tablaDirecciones = (Hashtable<Double, double[]>) new Hashtable ();
		double[] valXvalY;
		double x, y;
		while (dirs<=maxDirs)
		{
			x = Math.cos(Math.toRadians(grado));
			y = Math.sin(Math.toRadians(grado));
			if ((x > 0.9) && (x < 1))
				x = 1;
			else
				if ((x < -0.9) && (x > -1))
					x = -1;
			if ((y > 0.9) && (y < 1))
				y = 1;
			else
				if ((y < -0.9) && (y > -1))
					y = -1;
			if ((x > 0) && (x < 0.1))
				x = 0;
			else
				if ((x < 0) && (x > -0.1))
					x = 0;
			if ((y > 0) && (y < 0.1))
				y = 0;
			else
				if ((y < 0) && (y > -0.1))
					y = 0;
				
			valXvalY = new double[2];
			
			valXvalY[0] = x;
			valXvalY[1] = y;
			tablaDirecciones.put(grado, valXvalY);
			dirs++;
			grado += (gradoBase/actDiv);
			if ((dirs != 0) && (dirs%8 == 0))
			{
				actDiv++;
				if ((gradoBase/actDiv) < 5.0)
					break;
			}
			if (grado == gradoMax)
				grado = (gradoBase/actDiv);
			while (tablaDirecciones.containsKey(grado))
				grado += (gradoBase/actDiv);
		}
		table.put(s, tablaDirecciones);
	}
	
	/* CONSULTAS */
	
	/**
	 * Retorna la tabla con las direcciones para las Entidades de nombre de tipo s. 
	 * 
	 * @param s Nombre del Tipo de Entidad.
	 */
	public Hashtable<Double, double[]> getMyDirections (String s)
	{
		return table.get(s);
	}
	
	/**
	 * Retorna la cantidad de direcciones para las Entidades de nombre de tipo s. 
	 * 
	 * @param s Nombre del Tipo de Entidad.
	 */
	public int getMyMaxDirections (String s)
	{
		return table.get(s).values().size();
	}
	
	/**
	 * Obtener instancia de la clase.
	 */
	public static DirectionMove getDirMove ()
	{
		return dirMove;
	}

}