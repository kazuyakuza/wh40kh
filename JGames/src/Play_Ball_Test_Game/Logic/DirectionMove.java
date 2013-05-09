package Play_Ball_Test_Game.Logic;

import java.util.Random;

/**
 * Clase para archivar las direcciones posibles de movimiento de las entidades.
 * 
 * Las direcciones posibles dependen de la dimensi�n de la entidad.
 * Si la posici�n inicial de la entidad es (x0,y0), entonces la entidad tiene 80 direcciones posibles.
 * Estas 80 direcciones son una reducci�n de las 360 (por 360 grados). Cada una de las 80 direcci�nes se indican con un valor (x,y).
 * En f�sica, un objeto de posici�n P, puede moverse a una velocidad V.
 * Aqu� V ser� computada, y se usar� una velocidad final de la forma (x,y), dado que habr� una determinada velocidad "en el" eje x y otro "en el" eje y.
 * Esto es para indicar la direcci�n de movimiento indicada. 
 * Sea d = "direcci�n", v = "velocidad" y vf = "velocidad final":
 * 		_ d = (dx, dy) <- direcci�n en el eje x, y eje y
 * 		_ si dx = 1
 * 			entonces -1 <= dy <= 1
 * 		  sino dy = +- 1
 * 		_ si dy = 1
 * 			entonces -1 <= dx <= 1
 * 		  sino dx = +- 1
 * 		_ vf = (vfx, vfy) <- velocidad final en el eje x, y eje y
 * 		_ vfx = dx * v
 * 		_ vfy = dy * v
 *  
 * @author Kazuya
 */
public class DirectionMove
{
	
	/* CONSTRUCTOR */
	
	private DirectionMove () {}
	
	/* COMANDOS */
	
	/* CONSULTAS */
	
	/**
	 * Retorna el vector direcci�n (dx,dy) hacia izquierda.
	 * 
	 * @return Un vector direcci�n (dx,dy) hacia izquierda.
	 */
	public static double[] getLeftDirection ()
	{
		return new double[] {-1, 0};
	}
	
	/**
	 * Retorna el vector direcci�n (dx,dy) hacia derecha.
	 * 
	 * @return Un vector direcci�n (dx,dy) hacia derecha.
	 */
	public static double[] getRightDirection ()
	{
		return new double[] {1, 0};
	}
	
	/**
	 * Retorna el vector direcci�n (dx,dy) hacia arriba.
	 * 
	 * @return Un vector direcci�n (dx,dy) hacia arriba.
	 */
	public static double[] getUpDirection ()
	{
		return new double[] {0, -1};
	}
	
	/**
	 * Retorna el vector direcci�n (dx,dy) hacia abajo.
	 * 
	 * @return Un vector direcci�n (dx,dy) hacia abajo.
	 */
	public static double[] getDownDirection ()
	{
		return new double[] {0, 1};
	}
	
	/**
	 * Retorna el vector direcci�n (dx,dy) hacia diagonal arriba derecha.
	 * 
	 * @return Un vector direcci�n (dx,dy) hacia diagonal arriba derecha.
	 */
	public static double[] getUpRightDirection ()
	{
		return new double[] {1, -1};
	}
	
	/**
	 * Retorna el vector direcci�n (dx,dy) hacia diagonal arriba izquierda.
	 * 
	 * @return Un vector direcci�n (dx,dy) hacia diagonal arriba izquierda.
	 */
	public static double[] getUpLeftDirection ()
	{
		return new double[] {-1, -1};
	}
	
	/**
	 * Retorna el vector direcci�n (dx,dy) hacia diagonal abajo derecha.
	 * 
	 * @return Un vector direcci�n (dx,dy) hacia diagonal abajo derecha.
	 */
	public static double[] getDownRightDirection ()
	{
		return new double[] {1, 1};
	}
	
	/**
	 * Retorna el vector direcci�n (dx,dy) hacia diagonal abajo izquierda.
	 * 
	 * @return Un vector direcci�n (dx,dy) hacia diagonal abajo izquierda.
	 */
	public static double[] getDownLeftDirection ()
	{
		return new double[] {-1, 1};
	}
	
	/**
	 * Retorna un vector direcci�n (dx,dy) pseudo-random de los 80 posibles.
	 * 
	 * @return Un vector direcci�n de las 80 posibles.
	 */
	public static double[] getRandomDirection ()
	{
		double[] r = {0.0, 0.0};
		Random rd = new Random();
		double aux = ((double) rd.nextInt(10) + 1)/10;
		if (rd.nextBoolean())
			aux *= -1;
		if (aux == 1.0 || aux == -1.0)
			if (rd.nextBoolean())
			{
				r[0] = aux;
				r[1] = ((double) rd.nextInt(10) + 1)/10;
				if (rd.nextBoolean())
					r[1] *= -1;
			}
			else
			{
				r[1] = aux;
				r[0] = ((double) rd.nextInt(10) + 1)/10;
				if (rd.nextBoolean())
					r[0] *= -1;
			}
		else
			if (rd.nextBoolean())
			{
				r[0] = aux;
				r[1] = 1.0;
				if (rd.nextBoolean())
					r[1] *= -1;
			}
			else
			{
				r[1] = aux;
				r[0] = 1.0;
				if (rd.nextBoolean())
					r[0] *= -1;
			}
		return r;
	}
	
	/**
	 * Retorna la direcci�n opuesta a d.
	 * 
	 * @param d Direcci�n a la cual calcularle su opuesto.
	 * @return Direcci�n opuesta a d.
	 */
	public static double[] getOppositeDirection (double[] d)
	{
		return new double[] {-d[0], -d[1]};
	}
	
	/**
	 * Retorna la direcci�n resultante al "chocar" horizontalmente por la direcci�n d.
	 * 
	 * @param d Direcci�n a la cual calcuarle la direcci�n resultante.
	 * @return Direcci�n resultante al "chocar" horizontalmente por la direcci�n d.
	 */
	public static double[] getHitHorizontalDirection (double[] d)
	{
		return new double[] {d[0], -d[1]};
	}
	
	/**
	 * Retorna la direcci�n resultante al "chocar" lateralmente por la direcci�n d.
	 * 
	 * @param d Direcci�n a la cual calcuarle la direcci�n resultante.
	 * @return Direcci�n resultante al "chocar" verticalmente por la direcci�n d.
	 */
	public static double[] getHitSideDirection (double[] d)
	{
		return new double[] {-d[0], d[1]};
	}

}