package entity;

import item.*;
import java.util.*;
import logic.*;

public interface Wugu1<T extends Stats1, A extends AttackInfo, I extends Item>
{
	int movement(MainState mainState, XEntity entity, T stats);

	int maxAccessRange(MainState mainState, XEntity entity, T stats);

	List<Integer> attackRanges(MainState mainState, XEntity entity, T stats, boolean counter);

	List<A> attackInfo(MainState mainState, XEntity entity, T stats, XEntity entityT, T statsT);

	List<I> items(MainState mainState, XEntity entity, T stats, int distance);

	Optional<I> equipItem(MainState mainState, XEntity entity, T stats, int distance);
}