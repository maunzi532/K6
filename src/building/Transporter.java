package building;

import arrow.*;
import building.blueprint.*;
import geom.hex.Hex;
import item.ItemList;
import item.inv.transport.*;
import java.util.*;

public class Transporter extends Buildable implements WithTargets
{
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

	@Override
	public void transportPhase(CanAddArrows canAddArrows)
	{
		Optional<PossibleTransport> transportOpt = invTransporter.transport();
		if(transportOpt.isPresent())
		{
			PossibleTransport transport = transportOpt.get();
			invTransporter.doTheTransport(transport);
			canAddArrows.addArrow(new VisualArrow(transport.from.location(),
					transport.to.location(), ArrowMode.TARROW, 60, transport.item.image()));
		}
	}

	@Override
	public int range()
	{
		return range;
	}

	@Override
	public void addTarget(DoubleInv target)
	{
		targets.add(target);
	}

	@Override
	public void removeTarget(DoubleInv target)
	{
		targets.remove(target);
	}

	@Override
	public void toggleTarget(DoubleInv target)
	{
		if(targets.contains(target))
			targets.remove(target);
		else
			targets.add(target);
	}

	@Override
	public List<DoubleInv> targets()
	{
		return targets;
	}
}