package building;

import arrow.*;
import building.blueprint.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import geom.f1.*;
import item.*;
import building.transport.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import levelMap.*;
import logic.xstate.*;

public class Transporter extends Buildable
{
	private List<DoubleInv> targets;
	private int range;
	private int amount;
	private InvTransporter invTransporter;

	public Transporter(Tile location, BuildingBlueprint blueprint)
	{
		super(location, blueprint.constructionBlueprint.blueprints.get(0).get(0),
				blueprint.constructionBlueprint.blueprints.get(0).get(0).refundable, blueprint.name);
		range = blueprint.transporterBlueprint.range;
		amount = blueprint.transporterBlueprint.amount;
		targets = new ArrayList<>();
		invTransporter = new InvTransporter(targets, targets, amount);
	}

	public Transporter(Tile location, CostBlueprint costs, ItemList refundable, BuildingBlueprint blueprint)
	{
		super(location, costs, refundable, blueprint.name);
		range = blueprint.transporterBlueprint.range;
		amount = blueprint.transporterBlueprint.amount;
		targets = new ArrayList<>();
		invTransporter = new InvTransporter(targets, targets, amount);
	}

	public int getRange()
	{
		return range;
	}

	public boolean isTarget(Object target)
	{
		return DoubleInv.isTargetable(target) && targets.contains(target);
	}

	public Map<Tile, MarkType> targets(LevelMap levelMap)
	{
		return levelMap.y1.range(location(), 0, range).stream().filter(e -> levelMap.getBuilding(e) instanceof DoubleInv)
				.collect(Collectors.toMap(e -> e, e -> isTarget(levelMap.getBuilding(e)) ? MarkType.ON : MarkType.OFF));
	}

	public void toggleTarget(DoubleInv target)
	{
		if(isTarget(target))
			targets.remove(target);
		else
			targets.add(target);
	}

	@Override
	public void transportPhase(LevelMap levelMap)
	{
		targets.removeIf(e -> !e.active());
		Optional<PossibleTransport> transportOpt = invTransporter.transport();
		if(transportOpt.isPresent())
		{
			PossibleTransport transport = transportOpt.get();
			invTransporter.doTheTransport(transport);
			levelMap.addArrow(ShineArrow.factory(transport.from().location(), transport.to().location(),
					TransportPhaseState.TRANSPORT_TIME, false, transport.item().image()));
		}
	}

	@Override
	public void loadConnect(LevelMap levelMap)
	{
		targets = targets.stream().map(e -> ((PreConnectMapObject) e).restore(levelMap)).collect(Collectors.toList());
	}

	public Transporter(JrsObject data, ItemLoader itemLoader, TileType y1)
	{
		super(data, itemLoader, y1);
		targets = new ArrayList<>();
		((JrsArray) data.get("Targets")).elements().forEachRemaining(e -> targets.add(new PreConnectMapObject((JrsObject) e, y1)));
		range = ((JrsNumber) data.get("Range")).getValue().intValue();
		amount = ((JrsNumber) data.get("Amount")).getValue().intValue();
		invTransporter = new InvTransporter(targets, targets, amount);
	}

	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1) throws
			IOException
	{
		a1 = super.save(a1, itemLoader, y1);
		var a2 = a1.startArrayField("Targets");
		for(DoubleInv target : targets)
		{
			a2 = new PreConnectMapObject(target.location(), target.type()).save(a2.startObject(), y1).end();
		}
		return a2.end().put("Range", range).put("Amount", amount);
	}
}