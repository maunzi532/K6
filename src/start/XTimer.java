package start;

import javafx.animation.*;
import javafx.scene.input.*;
import logic.*;

public class XTimer extends AnimationTimer
{
	private XInputInterface inputInterface;
	private long lastNanoTime;
	private boolean mouseDown;
	private boolean isDrag;
	private double xStart, yStart;
	private boolean clicked;
	private boolean dragged;
	private double xClicked, yClicked;
	private boolean moved;
	private boolean inside;
	private double xMoved, yMoved;
	private MouseButton mouseKey;
	private KeyCode keyCode;

	XTimer(XInputInterface inputInterface)
	{
		this.inputInterface = inputInterface;
	}

	public void onMouseDown(MouseEvent mouseEvent)
	{
		mouseDown = true;
		xStart = mouseEvent.getSceneX();
		yStart = mouseEvent.getSceneY();
		mouseKey = mouseEvent.getButton();
	}

	public void onDragDetected(MouseEvent mouseEvent)
	{
		isDrag = true;
	}

	public void onMouseUp(MouseEvent mouseEvent)
	{
		if(mouseDown)
		{
			mouseDown = false;
			if(isDrag)
				dragged = true;
			else
				clicked = true;
			isDrag = false;
			xClicked = mouseEvent.getSceneX();
			yClicked = mouseEvent.getSceneY();
			mouseKey = mouseEvent.getButton();
		}
	}


	public void onMouseMove(MouseEvent mouseEvent)
	{
		moved = true;
		xMoved = mouseEvent.getSceneX();
		yMoved = mouseEvent.getSceneY();
		inside = true;
	}

	public void onMouseExit(MouseEvent mouseEvent)
	{
		mouseDown = false;
		isDrag = false;
		xMoved = mouseEvent.getSceneX();
		yMoved = mouseEvent.getSceneY();
		inside = false;
	}

	public void onKeyEvent(KeyEvent keyEvent)
	{
		keyCode = keyEvent.getCode();
	}

	@Override
	public void handle(long currentNanoTime)
	{
		//System.out.println((currentNanoTime - lastNanoTime) / 1000000);
		//System.out.println(1000000000d / (currentNanoTime - lastNanoTime));
		inputInterface.frameTime(currentNanoTime - lastNanoTime);
		lastNanoTime = currentNanoTime;
		if(inside)
		{
			inputInterface.mousePosition(xMoved, yMoved);
		}
		if(moved)
		{
			inputInterface.handleMouseMove(xMoved, yMoved);
			moved = false;
		}
		if(isDrag)
		{
			inputInterface.dragPosition(xStart, yStart, xMoved, yMoved, mouseKey.ordinal());
		}
		if(clicked)
		{
			inputInterface.handleClick(xClicked, yClicked, mouseKey.ordinal());
			clicked = false;
		}
		else if(dragged)
		{
			inputInterface.handleDrag(xStart, yStart, xClicked, yClicked, mouseKey.ordinal());
		}
		else if(keyCode != null)
		{
			inputInterface.handleKey(keyCode);
			keyCode = null;
		}
		inputInterface.tick();
	}
}