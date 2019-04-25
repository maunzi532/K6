package arrow;

import entity.*;
import javafx.scene.paint.*;
import logic.*;

public class EntityArrowC
{
	private XEntity entity;
	private XEntity other;
	private int effect;
	private int startingTick;
	private int effectLength;
	private int startingTickC;
	private int changingTicks;
	private int start;
	private int blinkOnZero;
	private int change;
	private int counter;
	private int counter2;
	private InfoArrow infoArrow;

	public EntityArrowC(MainState mainState, XEntity entity, XEntity other,
			int effect, int startingTick, int effectLength,
			int startingTickC, int changingTicks, int start, int max, int change1, int blinkOnZero)
	{
		this.entity = entity;
		this.other = other;
		this.effect = effect;
		this.startingTick = startingTick;
		this.effectLength = effectLength;
		this.startingTickC = startingTickC;
		this.changingTicks = changingTicks;
		this.start = start;
		this.blinkOnZero = blinkOnZero;
		change = Math.max(-start, Math.min(max - start, change1));
		infoArrow = new InfoArrow(entity.location(), other.location(),
				80, entity instanceof XHero ? Color.GREEN : Color.GRAY, Color.BLACK,
				Color.WHITE, start, max);
		mainState.levelMap.addArrow(infoArrow);
	}

	public int tick(MainState mainState)
	{
		if(counter == startingTick)
		{
			XArrow arrow = switch(effect)
					{
						case 1 -> XArrow.factory(entity.location(), other.location(),
								effectLength, false, entity.getImage(), false);
						case 2 -> new BlinkArrow(entity.location(),
								effectLength, false, entity.getImage(), effectLength / 4);
						default -> throw new RuntimeException();
					};
			mainState.levelMap.addArrow(arrow);
			entity.setReplacementArrow(arrow);
			/*if(effect == 1)
			{
				XArrow arrow = XArrow.factory(entity.location(), other.location(),
						effectLength, false, entity.getImage(), false);
				mainState.levelMap.addArrow(arrow);
				entity.setReplacementArrow(arrow);
			}
			if(effect == 2)
			{
				XArrow arrow = new BlinkArrow(entity.location(),
						effectLength, false, entity.getImage(), effectLength / 4);
				mainState.levelMap.addArrow(arrow);
				entity.setReplacementArrow(arrow);
			}*/
		}
		int cht = 0;
		if(counter >= startingTickC && (counter - startingTickC) % changingTicks == 0)
		{
			if(counter2 < Math.abs(change))
			{
				cht = change > 0 ? 1 : -1;
				//infoArrow.setData(stats().getStat(0));
				counter2++;
				infoArrow.setData(start + cht * counter2);
			}
		}
		if(counter == startingTickC + changingTicks * Math.abs(change) && start + change <= 0 && blinkOnZero > 0)
		{
			mainState.levelMap.addArrow(new BlinkArrow(entity.location(),
					blinkOnZero, false, entity.getImage(), blinkOnZero / 8));
		}
		counter++;
		return cht;
	}

	public int finished()
	{
		return Math.min(counter - startingTick - effectLength,
				counter - startingTickC - counter2 * changingTicks - (start + change <= 0 ? blinkOnZero : 0));
	}
}