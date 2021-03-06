package vis.sideinfo;

import arrow.*;
import entity.*;
import java.util.*;
import javafx.scene.canvas.*;
import javafx.scene.text.*;
import vis.vis.*;

public final class VisualSideInfo
{
	private static final int SHIFT_TIME = 8;

	private final XGraphics graphics;
	private final boolean flipped;
	private SideInfo current;
	private SideInfo last;
	private int counter;

	public VisualSideInfo(XGraphics graphics, boolean flipped)
	{
		this.graphics = graphics;
		this.flipped = flipped;
	}

	public void setSideInfo(SideInfo sideInfo)
	{
		if(!Objects.equals(current == null ? null : current.location(), sideInfo == null ? null : sideInfo.location()))
		{
			counter = SHIFT_TIME;
			last = current;
		}
		current = sideInfo;
	}

	public void tick()
	{
		if(counter > 0)
			counter--;
	}

	public double takeY2()
	{
		return current != null ? graphics.scaleHW() * (YS_1 + S_I) : 0.0;
	}

	public void draw(Scheme scheme)
	{
		if(counter > 0 && last != null)
			drawShifted(last, (SHIFT_TIME - counter) / (double) SHIFT_TIME, scheme);
		if(current != null)
			drawShifted(current, counter / (double) SHIFT_TIME, scheme);
	}

	private void drawShifted(SideInfo sideInfo, double shift, Scheme scheme)
	{
		graphics.gd().setImageSmoothing(false);
		if(flipped)
			drawR(sideInfo, shift, scheme);
		else
			drawL(sideInfo, shift, scheme);
	}

	private static final double XD_I = 0.1;
	private static final double S_I = 0.35;
	private static final double XS_1 = 0.9;
	private static final double YS_1 = 0.25;
	private static final double XD_S = 0.5;
	private static final double YD_S = 0.14;
	private static final double XS_S = 0.25;
	private static final double XD_T = 0.15;
	private static final double YD_T = 0.07;
	private static final double XS_T = 0.7;
	private static final double YS_T = 0.04;

	private void drawL(SideInfo sideInfo, double shift, Scheme scheme)
	{
		GraphicsContext gd = graphics.gd();
		double size = graphics.scaleHW();
		double lx = -size * shift;
		double by = graphics.yHW() * 2.0;
		gd.setFill(scheme.color("sideinfo.background"));
		gd.drawImage(scheme.image(sideInfo.imageName()), lx + size * XD_I, by - size * (YS_1 + S_I), size * S_I, size * S_I);
		gd.fillRect(lx, by - size * YS_1, size * XS_1, size * YS_1);
		if(sideInfo.statBar() != null)
			drawStatBar(sideInfo.statBar(), lx + size * XD_S, by - size * (YD_S + YS_T), size * XS_S, size * YS_T, scheme);
		gd.setFill(scheme.color("sideinfo.text"));
		gd.setFont(new Font(size * YS_T));
		CharSequence[] texts = sideInfo.texts();
		for(int i = 0; i < texts.length; i++)
		{
			if(texts[i] != null)
				gd.fillText(scheme.localXText(texts[i]), lx + size * XD_T + size * XS_T / texts.length * i, by - size * YD_T);
		}
	}

	private void drawR(SideInfo sideInfo, double shift, Scheme scheme)
	{
		GraphicsContext gd = graphics.gd();
		double size = graphics.scaleHW();
		double rx = graphics.xHW() * 2.0 + size * shift;
		double by = graphics.yHW() * 2.0;
		gd.setFill(scheme.color("sideinfo.background"));
		gd.drawImage(scheme.image(sideInfo.imageName()), rx - size * (XD_I + S_I), by - size * (YS_1 + S_I), size * S_I, size * S_I);
		gd.fillRect(rx - size * XS_1, by - size * YS_1, size * XS_1, size * YS_1);
		if(sideInfo.statBar() != null)
			drawStatBar(sideInfo.statBar(), rx - size * (XD_S + XS_S), by - size * (YD_S + YS_T), size * XS_S, size * YS_T,
					scheme);
		gd.setFill(scheme.color("sideinfo.text"));
		gd.setFont(new Font(size * YS_T));
		CharSequence[] texts = sideInfo.texts();
		for(int i = 0; i < texts.length; i++)
		{
			if(texts[i] != null)
				gd.fillText(scheme.localXText(texts[i]), rx - size * XD_T - size * XS_T / texts.length * i, by - size * YD_T);
		}
	}

	private void drawStatBar(StatBar statBar, double xs, double ys, double xw, double yw, Scheme scheme)
	{
		GraphicsContext gd = graphics.gd();
		gd.setFill(scheme.color(statBar.getBg()));
		gd.fillRect(xs, ys, xw, yw);
		gd.setFill(scheme.color(statBar.getFg()));
		gd.fillRect(xs, ys, xw * statBar.filledPart(), yw);
		gd.setStroke(scheme.color(statBar.getBg()));
		gd.strokeRect(xs, ys, xw, yw);
		gd.setFont(new Font(yw * 0.8));
		gd.setFill(scheme.color(statBar.getTc()));
		gd.fillText(scheme.localXText(statBar.getText()), xs + xw / 2.0, ys + yw / 2.0, xw);
	}
}