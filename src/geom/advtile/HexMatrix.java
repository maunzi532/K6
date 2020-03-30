package geom.advtile;

public final class HexMatrix
{
	public static final double Q3 = 1.7320508075688772; //sqrt 3
	private static final double Z3 = 0.6666666666666666; // 2 / 3
	private static final double EDGE_ANGLE = 0.5235987755982988; //PI / 6

	public final double[][] matrix;
	public final double startAngle;

	public HexMatrix(double startAngle)
	{
		this.startAngle = startAngle;
		matrix = new double[2][4];
		double a1p = (startAngle * 2.0 - 1.0) * EDGE_ANGLE;
		double a1z = (startAngle * 2.0 - 3.0) * EDGE_ANGLE;
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