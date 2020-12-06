package start;

import javafx.animation.*;
import javafx.scene.input.*;
import logic.*;
import vis.*;
import vis.keybind.*;

public final class XTimer extends AnimationTimer
{
	private final XInputInterface inputInterface;
	private final KeybindFile keybindFile;
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
			XKey key = keybindFile.mouseKey(mouseEvent.getButton());
			mouseDown = false;
			if(isDrag)
			{
				if(key.canDrag())
					dragged = true;
			}
			else
			{
				if(key.canClick())
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
		XKey key = keybindFile.keyboardKey(keyEvent.getCode());
		if(key.canClick())
		{
			clickedK = true;
			keyCode = keyEvent.getCode();
		}
	}

	@Override
	public void handle(long currentNanoTime)
	{
		try
		{
			inputInterface.frameTime(currentNanoTime - lastNanoTime);
			lastNanoTime = currentNanoTime;
			if(clicked && inside)
			{
				inputInterface.mousePosition(xClicked, yClicked, true, moved, isDrag, keybindFile.mouseKey(mouseKeyD));
				moved = false;
				clicked = false;
			}
			else if(clickedK && inside)
			{
				inputInterface.mousePosition(xClicked, yClicked, true, moved, isDrag, keybindFile.keyboardKey(keyCode));
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
				inputInterface.mousePosition(0.0, 0.0, false, moved, isDrag, KeybindFile.NONE);
				clicked = false;
				clickedK = false;
			}
			if(dragged)
			{
				inputInterface.dragPosition(true, xStart, yStart, xClicked, yClicked, keybindFile.mouseKey(mouseKeyD), true);
				dragged = false;
			}
			else if(isDrag)
			{
				inputInterface.dragPosition(true, xStart, yStart, xMoved, yMoved, keybindFile.mouseKey(mouseKeyD), false);
			}
			else
			{
				inputInterface.dragPosition(false, 0.0, 0.0, 0.0, 0.0, KeybindFile.NONE, false);
			}
			if(!clicked && !clickedK && !dragged)
			{
				if(keyCode != null)
				{
					inputInterface.handleKey(keybindFile.keyboardKey(keyCode));
					keyCode = null;
				}
				else if(mouseKey != null)
				{
					inputInterface.handleKey(keybindFile.mouseKey(mouseKeyD));
					mouseKey = null;
				}
			}
			inputInterface.tick();
		}catch(Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
}