package start;

import javafx.animation.*;
import javafx.scene.canvas.*;
import javafx.scene.input.*;
import logic.*;

public class XTimer extends AnimationTimer
{
	//private long last;
	private MainVisual mainVisual;
	private boolean clicked;
	private double xClicked, yClicked;
	private boolean primary;
	private KeyCode keyCode;

	public XTimer(GraphicsContext gd, int w, int h)
	{
		mainVisual = new MainVisual(gd, w, h);
	}

	public void onMouseEvent(MouseEvent mouseEvent)
	{
		clicked = true;
		xClicked = mouseEvent.getSceneX();
		yClicked = mouseEvent.getSceneY();
		primary = mouseEvent.getButton() == MouseButton.PRIMARY;
	}

	public void onKeyEvent(KeyEvent keyEvent)
	{
		keyCode = keyEvent.getCode();
	}

	@Override
	public void handle(long currentNanoTime)
	{
		//System.out.println(1000000000d / (currentNanoTime - last));
		//last = currentNanoTime;
		if(clicked)
		{
			mainVisual.handleClick(xClicked, yClicked, primary);
			clicked = false;
		}
		else if(keyCode != null)
		{
			mainVisual.handleKey(keyCode);
			keyCode = null;
		}
		mainVisual.tick();
	}
}