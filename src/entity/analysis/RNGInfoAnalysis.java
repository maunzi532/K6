package entity.analysis;

import java.util.*;
import java.util.stream.*;

public final class RNGInfoAnalysis<O extends RNGOutcome, Divider extends RNGDivider<O>>
{
	private final Divider start;
	private final List<Divider> stack;
	private final List<Integer> snum;
	private final List<O> outcomes;

	public RNGInfoAnalysis(Divider start)
	{
		this.start = start;
		stack = new ArrayList<>();
		snum = new ArrayList<>();
		outcomes = new ArrayList<>();
	}

	public RNGInfoAnalysis<O, Divider> create()
	{
		stack.add(start);
		snum.add(0);
		while(!stack.isEmpty())
		{
			current().build();
			if(current().paths.isEmpty())
				outcomes.add(current().asOutcome());
			next();
		}
		return this;
	}

	private Divider current()
	{
		return stack.get(stack.size() - 1);
	}

	private void next()
	{
		while(snum.get(snum.size() - 1) >= current().paths.size())
		{
			stack.remove(stack.size() - 1);
			snum.remove(snum.size() - 1);
			if(stack.size() <= 0)
				return;
		}
		stack.add((Divider) current().paths.get(snum.get(snum.size() - 1)));
		snum.set(snum.size() - 1, snum.get(snum.size() - 1) + 1);
		snum.add(0);
	}

	public Divider getStart()
	{
		return start;
	}

	public List<O> outcomes()
	{
		return outcomes;
	}

	public List<RNGOutcome> outcomes2()
	{
		return outcomes.stream().collect(Collectors.groupingBy(e -> e.compareText))
				.values().stream().map(RNGOutcome::new).collect(Collectors.toList());
	}
}