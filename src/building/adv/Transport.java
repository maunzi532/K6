package building.adv;

import arrow.*;
import building.*;
import building.blueprint.*;
import building.transport.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import java.io.*;
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

	public Transport(String name, TransporterBlueprint blueprint)
	{
		this.name = name;
		range = blueprint.range();
		amount = blueprint.amount();
		targets = new ArrayList<>();
		invTransporter = new InvTransporter(targets, targets, amount);
	}

	public int range()
	{
		return range;
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
		invTransporter.transportHistory().replaceAll(e -> new PossibleTransport(e.item(),
				((PreConnectMapObject) e.from()).restore(levelMap), ((PreConnectMapObject) e.to()).restore(levelMap), 0, 0));
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

	public Transport(JrsObject data, ItemLoader itemLoader, TileType y1)
	{
		name = data.get("Name").asText();
		range = ((JrsNumber) data.get("Range")).getValue().intValue();
		amount = ((JrsNumber) data.get("Amount")).getValue().intValue();
		targets = new ArrayList<>();
		((JrsArray) data.get("Targets")).elements().forEachRemaining(e -> targets.add(PreConnectMapObject.create((JrsObject) e, y1)));
		ArrayList<PossibleTransport> history = new ArrayList<>();
		((JrsArray) data.get("Targets")).elements().forEachRemaining(e -> history.add(PossibleTransport.create((JrsObject) e, itemLoader, y1)));
		invTransporter = new InvTransporter(targets, targets, amount, history);
	}

	@Override
	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1)
			throws IOException
	{
		a1.put("Name", name);
		a1.put("Range", range);
		a1.put("Amount", amount);
		var a2 = a1.startArrayField("Targets");
		for(DoubleInv target : targets)
		{
			new PreConnectMapObject(target.location(), target.type()).save(a2.startObject(), y1).end();
		}
		a2.end();
		var a3 = a1.startArrayField("History");
		for(PossibleTransport entry : invTransporter.transportHistory())
		{
			entry.save(a3.startObject(), itemLoader, y1).end();
		}
		return a1;
	}
}