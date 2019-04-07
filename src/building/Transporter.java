package building;

import arrow.*;
import building.blueprint.*;
import geom.f1.*;
import item.*;
import item.inv.transport.*;
import java.util.*;
import java.util.stream.*;
import levelMap.*;

public class Transporter extends Buildable
{
	public static final int TRANSPORT_TIME = 60;
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

	public boolean isTarget(Object target)
	{
		return target instanceof DoubleInv && ((DoubleInv) target).active() && targets.contains(target);
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
			levelMap.addArrow(XArrow.factory(transport.from.location(),
					transport.to.location(), TRANSPORT_TIME, false, transport.item.image(), true));
		}
	}
}