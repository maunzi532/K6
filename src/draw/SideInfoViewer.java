package draw;

import geom.*;
import javafx.scene.paint.*;

public class SideInfoViewer
{
	private static final int SHIFT_TIME = 10;

	private XGraphics graphics;
	private boolean flipped;
	private SideInfo current;
	private SideInfo last;
	private int counter;

	public SideInfoViewer(XGraphics graphics, boolean flipped)
	{
		this.graphics = graphics;
		this.flipped = flipped;
	}

	public void setSideInfo(SideInfo sideInfo)
	{
		last = current;
		current = sideInfo;
		counter = SHIFT_TIME;
	}

	public void tick()
	{
		if(counter > 0)
			counter--;
	}

	public void draw()
	{
		if(counter > 0 && last != null)
			draw(last, SHIFT_TIME - counter);
		if(current != null)
			draw(current, counter);
	}

	private void draw(SideInfo sideInfo, int shift)
	{
		double xlen = graphics.xHW() * (flipped ? -1 : 1);
		double xend = graphics.xHW() + (xlen * (-1 - (double) shift / SHIFT_TIME));
		double ylen = -graphics.yHW();
		double yend = graphics.yHW() * 2;
		System.out.println("xlen = " + xlen);
		System.out.println("xend = " + xend);
		System.out.println("ylen = " + ylen);
		System.out.println("yend = " + yend);
		graphics.gd().setFill(Color.GRAY);
		minusrect(xend, xend + xlen, yend, yend + ylen);
	}

	private void minusrect(double x0, double x1, double y0, double y1)
	{
		graphics.gd().fillRect(Math.min(x0, x1), Math.min(y0, y1), Math.abs(x0 - x1), Math.abs(y0 - y1));
	}
}