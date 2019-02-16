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
	private boolean moved;
	private double xMoved, yMoved;
	private MouseButton mouseKey;
	private KeyCode keyCode;

	public XTimer(GraphicsContext gd, int w, int h)
	{
		mainVisual = new MainVisual(gd, w, h);
	}

	public void onMouseClick(MouseEvent mouseEvent)
	{
		clicked = true;
		xClicked = mouseEvent.getSceneX();
		yClicked = mouseEvent.getSceneY();
		mouseKey = mouseEvent.getButton();
	}

	public void onMouseMove(MouseEvent mouseEvent)
	{
		moved = true;
		xMoved = mouseEvent.getSceneX();
		yMoved = mouseEvent.getSceneY();
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
		if(moved)
		{
			mainVisual.handleMouseMove(xMoved, yMoved);
			moved = false;
		}
		if(clicked)
		{
			mainVisual.handleClick(xClicked, yClicked, mouseKey.ordinal());
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