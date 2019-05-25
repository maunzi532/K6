package arrow;

import entity.*;
import geom.f1.*;
import java.util.*;
import javafx.scene.paint.*;
import logic.*;

public class EntityArrowC
{
	private XEntity entity;
	private Tile target;
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

	public EntityArrowC(MainState mainState, XEntity entity, Tile target1,
			int effect, int startingTick, int effectLength,
			int startingTickC, int changingTicks, int start, int max, int change1, int blinkOnZero)
	{
		this.entity = entity;
		this.target = target1 != null ? target1 : mainState.y2.subtract(entity.location(), mainState.y2.upwardsT());
		this.effect = effect;
		this.startingTick = startingTick;
		this.effectLength = effectLength;
		this.startingTickC = startingTickC;
		this.changingTicks = changingTicks;
		this.start = start;
		this.blinkOnZero = blinkOnZero;
		change = Math.max(-start, Math.min(max - start, change1));
		infoArrow = new InfoArrow(entity.location(), target,
				80, entity instanceof XHero ? Color.GREEN : Color.GRAY, Color.BLACK,
				Color.WHITE, start, max);
		mainState.levelMap.addArrow(infoArrow);
	}

	public XEntity getEntity()
	{
		return entity;
	}

	public int tick(MainState mainState)
	{
		if(counter == startingTick)
		{
			Optional<XArrow> arrow = switch(effect)
					{
						case 1 -> Optional.of(XArrow.factory(entity.location(), target,
								effectLength, false, entity.getImage(), false));
						case 2 -> Optional.of(new BlinkArrow(entity.location(),
								effectLength, false, entity.getImage(), effectLength / 4));
						default -> Optional.empty();
					};
			if(arrow.isPresent())
			{
				mainState.levelMap.addArrow(arrow.get());
				entity.setReplacementArrow(arrow.get());
			}
		}
		int cht = 0;
		if(counter >= startingTickC && (counter - startingTickC) % changingTicks == 0)
		{
			if(Math.abs(counter2) < Math.abs(change))
			{
				cht = change > 0 ? 1 : -1;
				//infoArrow.setData(stats().getStat(0));
				counter2 += cht;
				infoArrow.statBar().setData(start + counter2);
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
				counter - startingTickC - Math.abs(change) * changingTicks - (start + change <= 0 ? blinkOnZero : 0));
	}
}