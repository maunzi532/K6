package hex;

public class MatrixH
{
	public static final double Q3 = Math.sqrt(3);
	public static final MatrixH LP = new MatrixH(new double[][]{{Q3, Q3 / 2, 0, 3d / 2}, {Q3 / 3, -1d / 3, 0, 2d / 3}}, 0.5);
	public static final MatrixH LF = new MatrixH(new double[][]{{3d / 2, 0, Q3 / 2, Q3}, {2d / 3, 0, -1d / 3, Q3 / 3}}, 0);

	public final double[][] matrix;
	public final double startAngle;

	private MatrixH(double[][] matrix, double startAngle)
	{
		this.matrix = matrix;
		this.startAngle = startAngle;
	}

	public static MatrixH layoutLerp(MatrixH m1, MatrixH m2, double t)
	{
		double[][] matrix1 = new double[2][4];
		for(int i = 0; i < 2; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				matrix1[i][j] = DoubleHex.lerp(m1.matrix[i][j], m2.matrix[i][j], t);
			}
		}
		return new MatrixH(matrix1, DoubleHex.lerp(m1.startAngle, m2.startAngle, t));
	}
}