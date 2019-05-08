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
		if(flipped)
			drawR(sideInfo, shift);
		else
			drawL(sideInfo, shift);
	}

	private void drawL(SideInfo sideInfo, int shift)
	{
		double xlen = graphics.xHW();
		double xstart = graphics.xHW() * shift / -SHIFT_TIME;
		double ylen = xlen / 2;
		double ystart = graphics.yHW() * 2 - ylen;
		graphics.gd().setFill(Color.GRAY);
		graphics.gd().drawImage(sideInfo.getCharImage(), xstart + xlen * 0.1, ystart, xlen * 0.3, xlen * 0.3);
		graphics.gd().fillRect(xstart, ystart + xlen * 0.3, xlen * 0.7, ylen - xlen * 0.3);
		graphics.gd().setStroke(Color.BLACK);
		for(int i = 0; i < sideInfo.getTexts().length; i++)
		{
			graphics.gd().strokeText(sideInfo.getTexts()[i], xstart + xlen * 0.1 + xlen * 0.1 * i, ystart + xlen * 0.4);
		}
	}

	private void drawR(SideInfo sideInfo, int shift)
	{
		double xlen = graphics.xHW();
		double xstart = graphics.xHW() + graphics.xHW() * shift / SHIFT_TIME;
		double ylen = xlen / 2;
		double ystart = graphics.yHW() * 2 - ylen;
		graphics.gd().setFill(Color.GRAY);
		graphics.gd().drawImage(sideInfo.getCharImage(), xstart + xlen * 0.6, ystart, xlen * 0.3, xlen * 0.3);
		graphics.gd().fillRect(xstart + xlen * 0.3, ystart + xlen * 0.3, xlen * 0.7, ylen - xlen * 0.3);
		for(int i = 0; i < sideInfo.getTexts().length; i++)
		{
			graphics.gd().strokeText(sideInfo.getTexts()[i], xstart + xlen * 0.4 + xlen * 0.1 * i, ystart + xlen * 0.4);
		}
	}
}