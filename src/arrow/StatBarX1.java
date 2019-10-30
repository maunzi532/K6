package arrow;

import entity.*;
import javafx.scene.paint.*;

public class StatBarX1 extends StatBar
{
	private String extraText;

	public StatBarX1(Color fg, Color bg, Color tc, int data, int maxData, String extraText)
	{
		super(fg, bg, tc, data, maxData);
		this.extraText = extraText;
	}

	@Override
	public String getText()
	{
		return getData() + extraText + "/" + getMaxData();
	}

	public static StatBar forEntityX1(XEntity entity, String extraText)
	{
		return new StatBarX1(entity instanceof XHero ? Color.GREEN : Color.GRAY, Color.BLACK, Color.WHITE,
				entity.getStats().getVisualStat(0), entity.getStats().getMaxVisualStat(0), extraText);
	}
}