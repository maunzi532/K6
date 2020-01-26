package building.adv;

import arrow.*;
import building.*;
import building.transport.*;
import geom.f1.*;
import item.inv.*;
import java.util.*;
import java.util.stream.*;
import levelMap.*;
import logic.xstate.*;

public class Transport implements BuildingFunction
{
	private final String name;
	private final int range;
	private final int amount;
	private final List<DoubleInv> targets;
	private final InvTransporter invTransporter;

	public Transport(String name, int range, int amount)
	{
		this.name = name;
		this.range = range;
		this.amount = amount;
		targets = new ArrayList<>();
		invTransporter = new InvTransporter(targets, targets, amount);
	}

	@Override
	public String name()
	{
		return name;
	}

	@Override
	public boolean playerTradeable(boolean levelStarted)
	{
		return false;
	}

	@Override
	public Inv inputInv()
	{
		return BlockedInv.INSTANCE;
	}

	@Override
	public Inv outputInv()
	{
		return BlockedInv.INSTANCE;
	}

	@Override
	public void afterTrading(){}

	@Override
	public void productionPhase(boolean canWork, LevelMap levelMap, Tile location){}

	@Override
	public void transportPhase(boolean canWork, LevelMap levelMap)
	{
		targets.removeIf(e -> !e.active());
		if(canWork)
		{
			Optional<PossibleTransport> transportOpt = invTransporter.transport();
			if(transportOpt.isPresent())
			{
				PossibleTransport transport = transportOpt.get();
				invTransporter.doTheTransport(transport);
				levelMap.addArrow(ShineArrow.factory(transport.from().location(), transport.to().location(),
						TransportPhaseState.TRANSPORT_TIME, false, transport.item().image()));
			}
		}
	}

	@Override
	public void afterProduction(){}

	@Override
	public void afterTransport(){}

	@Override
	public void loadConnect(LevelMap levelMap, XBuilding connectTo)
	{
		List<DoubleInv> list = targets.stream().map(e -> ((PreConnectMapObject) e).restore(levelMap)).collect(Collectors.toList());
		targets.clear();
		targets.addAll(list);
	}

	public boolean isTarget(DoubleInv target)
	{
		return target.active() && targets.contains(target);
	}

	public void toggleTarget(DoubleInv target)
	{
		if(targets.contains(target))
		{
			targets.remove(target);
		}
		else if(target.active())
		{
			targets.add(target);
		}
	}
}