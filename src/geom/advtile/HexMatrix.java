package geom.advtile;

public final class HexMatrix
{
	public static final double Q3 = Math.sqrt(3);
	public static final double Z3 = 2d / 3d;
	public static final double EDGE_ANGLE = Math.PI / 6;

	public final double[][] matrix;
	public final double startAngle;

	public HexMatrix(double startAngle)
	{
		this.startAngle = startAngle;
		matrix = new double[2][4];
		double a1p = (startAngle * 2 - 1) * EDGE_ANGLE;
		double a1z = (startAngle * 2 - 3) * EDGE_ANGLE;
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