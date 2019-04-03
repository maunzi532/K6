package building;

import arrow.*;
import building.blueprint.*;
import geom.hex.*;
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

	public Transporter(Hex location, BuildingBlueprint blueprint)
	{
		super(location, blueprint.constructionBlueprint.blueprints.get(0).get(0),
				blueprint.constructionBlueprint.blueprints.get(0).get(0).refundable, blueprint.name);
		range = blueprint.transporterBlueprint.range;
		amount = blueprint.transporterBlueprint.amount;
		targets = new ArrayList<>();
		invTransporter = new InvTransporter(targets, targets, amount);
	}

	public Transporter(Hex location, CostBlueprint costs, ItemList refundable, BuildingBlueprint blueprint)
	{
		super(location, costs, refundable, blueprint.name);
		range = blueprint.transporterBlueprint.range;
		amount = blueprint.transporterBlueprint.amount;
		targets = new ArrayList<>();
		invTransporter = new InvTransporter(targets, targets, amount);
	}

	public Map<Hex, MarkType> targets(LevelMap levelMap)
	{
		//noinspection SuspiciousMethodCalls
		return location().range(0, range).stream().filter(e -> levelMap.getBuilding(e) instanceof DoubleInv)
				.collect(Collectors.toMap(e -> e, e -> targets.contains(levelMap.getBuilding(e)) ? MarkType.ON : MarkType.OFF));
	}

	public void toggleTarget(DoubleInv target)
	{
		if(targets.contains(target))
			targets.remove(target);
		else
			targets.add(target);
	}

	@Override
	public void transportPhase(LevelMap levelMap)
	{
		Optional<PossibleTransport> transportOpt = invTransporter.transport();
		if(transportOpt.isPresent())
		{
			PossibleTransport transport = transportOpt.get();
			invTransporter.doTheTransport(transport);
			levelMap.addArrow(new VisualArrow(transport.from.location(),
					transport.to.location(), ArrowMode.TARROW, TRANSPORT_TIME, transport.item.image()));
		}
	}
}