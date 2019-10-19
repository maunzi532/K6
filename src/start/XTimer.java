package start;

import javafx.animation.*;
import javafx.scene.input.*;
import visual.*;
import visual.keybind.*;

public class XTimer extends AnimationTimer
{
	private XInputInterface inputInterface;
	private KeybindFile keybindFile;
	private long lastNanoTime;
	private boolean mouseDown;
	private boolean isDrag;
	private double xStart, yStart;
	private boolean clicked;
	private boolean clickedK;
	private boolean dragged;
	private double xClicked, yClicked;
	private boolean moved;
	private boolean inside;
	private double xMoved, yMoved;
	private MouseButton mouseKey;
	private MouseButton mouseKeyD;
	private KeyCode keyCode;

	XTimer(XInputInterface inputInterface, KeybindFile keybindFile)
	{
		this.inputInterface = inputInterface;
		this.keybindFile = keybindFile;
	}

	public void onMouseDown(MouseEvent mouseEvent)
	{
		mouseDown = true;
		xStart = mouseEvent.getSceneX();
		yStart = mouseEvent.getSceneY();
		mouseKey = mouseEvent.getButton();
		mouseKeyD = mouseEvent.getButton();
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
			{
				if(keybindFile.mouseKeys.get(mouseEvent.getButton()).canDrag())
					dragged = true;
			}
			else
			{
				if(keybindFile.mouseKeys.get(mouseEvent.getButton()).canClick())
					clicked = true;
			}
			isDrag = false;
			xClicked = mouseEvent.getSceneX();
			yClicked = mouseEvent.getSceneY();
			mouseKey = mouseEvent.getButton();
			mouseKeyD = mouseEvent.getButton();
		}
	}

	public void onMouseMove(MouseEvent mouseEvent)
	{
		moved = true;
		inside = true;
		xMoved = mouseEvent.getSceneX();
		yMoved = mouseEvent.getSceneY();
	}

	public void onMouseExit(MouseEvent mouseEvent)
	{
		mouseDown = false;
		isDrag = false;
		inside = false;
		xMoved = mouseEvent.getSceneX();
		yMoved = mouseEvent.getSceneY();
	}

	public void onKeyEvent(KeyEvent keyEvent)
	{
		keyCode = keyEvent.getCode();
	}

	public void onKeyUp(KeyEvent keyEvent)
	{
		if(keybindFile.keyboardKeys.get(keyEvent.getCode()).canClick())
		{
			clickedK = true;
			keyCode = keyEvent.getCode();
		}
	}

	@Override
	public void handle(long currentNanoTime)
	{
		//System.out.println((currentNanoTime - lastNanoTime) / 1000000);
		//System.out.println(1000000000d / (currentNanoTime - lastNanoTime));
		inputInterface.frameTime(currentNanoTime - lastNanoTime);
		lastNanoTime = currentNanoTime;
		if(clicked && inside)
		{
			inputInterface.mousePosition(xClicked, yClicked, true, moved, isDrag, keybindFile.mouseKeys.getOrDefault(mouseKeyD, KeybindFile.NONE));
			moved = false;
			clicked = false;
		}
		else if(clickedK && inside)
		{
			inputInterface.mousePosition(xClicked, yClicked, true, moved, isDrag, keybindFile.keyboardKeys.getOrDefault(keyCode, KeybindFile.NONE));
			moved = false;
			clickedK = false;
			keyCode = null;
		}
		else if(inside)
		{
			inputInterface.mousePosition(xMoved, yMoved, true, moved, isDrag, KeybindFile.NONE);
			moved = false;
		}
		else
		{
			inputInterface.mousePosition(0, 0, false, moved, isDrag, KeybindFile.NONE);
			clicked = false;
			clickedK = false;
		}
		if(dragged)
		{
			inputInterface.dragPosition(true, xStart, yStart, xClicked, yClicked, keybindFile.mouseKeys.getOrDefault(mouseKeyD, KeybindFile.NONE), true);
			dragged = false;
		}
		else if(isDrag)
		{
			inputInterface.dragPosition(true, xStart, yStart, xMoved, yMoved, keybindFile.mouseKeys.getOrDefault(mouseKeyD, KeybindFile.NONE), false);
		}
		else
		{
			inputInterface.dragPosition(false, 0, 0, 0, 0, KeybindFile.NONE, false);
		}
		if(!clicked && !clickedK && !dragged)
		{
			if(keyCode != null)
			{
				inputInterface.handleKey(keybindFile.keyboardKeys.getOrDefault(keyCode, KeybindFile.NONE));
				keyCode = null;
			}
			else if(mouseKey != null)
			{
				inputInterface.handleKey(keybindFile.mouseKeys.getOrDefault(mouseKeyD, KeybindFile.NONE));
				mouseKey = null;
			}
		}
		inputInterface.tick();
	}
}