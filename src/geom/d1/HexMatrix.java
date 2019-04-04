package geom.d1;

public class HexMatrix
{
	public static final double Q3 = Math.sqrt(3);
	public static final double Z3 = 2d / 3d;
	public static final HexMatrix
			LP = new HexMatrix(new double[][]{{Q3, Q3 / 2, 0, 3d / 2}, {Q3 / 3, -1d / 3, 0, 2d / 3}}, 0.5);
	public static final HexMatrix
			LF = new HexMatrix(new double[][]{{3d / 2, 0, Q3 / 2, Q3}, {2d / 3, 0, -1d / 3, Q3 / 3}}, 0);

	public final double[][] matrix;
	public final double startAngle;

	private HexMatrix(double[][] matrix, double startAngle)
	{
		this.matrix = matrix;
		this.startAngle = startAngle;
	}

	public HexMatrix(double startAngle)
	{
		this.startAngle = startAngle;
		matrix = new double[2][4];
		double a1 = startAngle * 2 - 1;
		double a1p = a1 * Math.PI / 6;
		double a1z = (a1 - 2) * Math.PI / 6;
		matrix[0][0] = Math.cos(a1p) * Q3;
		matrix[0][1] = Math.cos(a1z) * Q3;
		matrix[0][2] = -Math.sin(a1p) * Q3;
		matrix[0][3] = -Math.sin(a1z) * Q3;
		matrix[1][0] = -Math.sin(a1z) * Z3;
		matrix[1][1] = -Math.cos(a1z) * Z3;
		matrix[1][2] = Math.sin(a1p) * Z3;
		matrix[1][3] = Math.cos(a1p) * Z3;
	}
}