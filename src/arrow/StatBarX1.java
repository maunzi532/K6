package arrow;

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
}