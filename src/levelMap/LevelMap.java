package levelMap;

import arrow.*;
import entity.*;
import geom.f1.*;
import java.nio.*;
import java.util.*;
import java.util.stream.*;

public class LevelMap
{
	public static final int TIME_PER_DISTANCE = 20;

	public final TileType y1;
	private final HashMap<Tile, AdvTile> advTiles;
	private final List<Boolean> visibleSectors;
	private final ArrayList<XHero> entitiesH;
	private final ArrayList<XEnemy> entitiesE;
	private Map<Tile, MarkType> marked;
	private final ArrayList<XArrow> arrows;

	public LevelMap(TileType y1)
	{
		this.y1 = y1;
		advTiles = new HashMap<>();
		visibleSectors = new ArrayList<>();
		entitiesH = new ArrayList<>();
		entitiesE = new ArrayList<>();
		marked = Map.of();
		arrows = new ArrayList<>();
	}

	public AdvTile advTile(Tile t1)
	{
		return advTiles.getOrDefault(t1, AdvTile.EMPTY);
	}

	public void productionPhase()
	{
		for(AdvTile advTile : advTiles.values())
		{
			if(advTile.getBuilding() != null)
				advTile.getBuilding().productionPhase(this);
		}
		for(AdvTile advTile : advTiles.values())
		{
			if(advTile.getBuilding() != null)
				advTile.getBuilding().afterProduction();
		}
	}

	public void transportPhase()
	{
		for(AdvTile advTile : advTiles.values())
		{
			if(advTile.getBuilding() != null)
				advTile.getBuilding().transportPhase(this);
		}
		for(AdvTile advTile : advTiles.values())
		{
			if(advTile.getBuilding() != null)
				advTile.getBuilding().afterTransport();
		}
	}

	public void clearTile(Tile t1)
	{
		if(advTiles.containsKey(t1))
		{
			XEntity entity = advTiles.get(t1).getEntity();
			if(entity instanceof XHero)
				entitiesH.remove(entity);
			if(entity instanceof XEnemy)
				entitiesE.remove(entity);
			advTiles.remove(t1);
		}
	}

	public FloorTile getFloor(Tile t1)
	{
		return advTile(t1).getFloorTile();
	}

	public void setFloorTile(Tile t1, FloorTile floorTile)
	{
		while(floorTile.sector >= visibleSectors.size())
			visibleSectors.add(true);
		if(advTiles.containsKey(t1))
		{
			advTile(t1).setFloorTile(floorTile);
		}
		else
			advTiles.put(t1, new AdvTile(floorTile));
	}

	public MBuilding getBuilding(Tile t1)
	{
		return advTile(t1).getBuilding();
	}

	public void addBuilding(MBuilding building)
	{
		advTile(building.location()).setBuilding(building);
	}

	public MBuilding getOwner(Tile t1)
	{
		return advTile(t1).getOwned();
	}

	public void addOwner(Tile t1, MBuilding building)
	{
		advTile(t1).setOwned(building);
	}

	public void removeOwner(Tile t1)
	{
		advTile(t1).setOwned(null);
	}

	public XEntity getEntity(Tile t1)
	{
		return advTile(t1).getEntity();
	}

	public void addEntity(XEntity entity)
	{
		advTile(entity.location()).setEntity(entity);
		if(entity instanceof XHero)
		{
			entitiesH.add((XHero) entity);
		}
		if(entity instanceof XEnemy)
		{
			entitiesE.add((XEnemy) entity);
		}
	}

	public void removeEntity(XEntity entity)
	{
		advTile(entity.location()).setEntity(null);
		if(entity instanceof XHero)
		{
			entitiesH.remove(entity);
		}
		if(entity instanceof XEnemy)
		{
			entitiesE.remove(entity);
		}
	}

	public void moveEntity(XEntity entity, Tile newLocation)
	{
		XArrow arrow = XArrow.factory(entity.location(), newLocation,
				y1.distance(newLocation, entity.location()) * TIME_PER_DISTANCE, false, entity.getImage(), false);
		addArrow(arrow);
		entity.setReplacementArrow(arrow);
		advTile(entity.location()).setEntity(null);
		entity.setLocation(newLocation);
		advTile(newLocation).setEntity(entity);
	}

	public int createSector(boolean visible)
	{
		visibleSectors.add(visible);
		return visibleSectors.size() - 1;
	}

	public boolean sectorVisible(int sector)
	{
		return visibleSectors.get(sector);
	}

	public List<Tile> noEntityTiles()
	{
		return advTiles.entrySet().stream().filter(e -> e.getValue().getEntity() == null).map(Map.Entry::getKey).collect(Collectors.toList());
	}

	public ArrayList<XHero> getEntitiesH()
	{
		return entitiesH;
	}

	public ArrayList<XEnemy> getEntitiesE()
	{
		return entitiesE;
	}

	public Map<Tile, MarkType> getMarked()
	{
		return marked;
	}

	public void setMarked(Map<Tile, MarkType> marked)
	{
		Objects.requireNonNull(marked);
		this.marked = marked;
	}

	public List<XArrow> getArrows()
	{
		return arrows;
	}

	public void addArrow(XArrow arrow)
	{
		arrows.add(arrow);
	}

	public void tickArrows()
	{
		arrows.forEach(XArrow::tick);
		arrows.removeIf(XArrow::finished);
	}

	public byte[] saveData()
	{
		List<XEntity> entities = new ArrayList<>();
		ByteBuffer sb = ByteBuffer.allocate(advTiles.size() * 4 + 12);
		sb.putInt(0xA4D2839F);
		sb.putInt(advTiles.size());
		for(Map.Entry<Tile, AdvTile> entry : advTiles.entrySet())
		{
			Tile t1 = entry.getKey();
			AdvTile adv = entry.getValue();
			if(adv.getFloorTile() != null)
			{
				sb.put((byte) y1.sx(t1));
				sb.put((byte) y1.sy(t1));
				sb.put((byte) adv.getFloorTile().sector);
				sb.put((byte) adv.getFloorTile().type.ordinal());
				if(adv.getEntity() != null)
					entities.add(adv.getEntity());
			}
		}
		sb.putInt(entities.size());
		List<int[]> v = entities.stream().map(e -> e.save(y1)).collect(Collectors.toList());
		ByteBuffer sb2 = ByteBuffer.allocate(sb.capacity() + v.stream().mapToInt(e -> e.length).sum() * 4);
		sb2.put(sb.array());
		IntBuffer sb3 = sb2.asIntBuffer();
		v.forEach(sb3::put);
		return sb2.array();
	}

	public void createTile(byte x, byte y, byte s, byte t)
	{
		while(s >= visibleSectors.size())
			visibleSectors.add(true);
		advTiles.put(y1.create2(x, y), new AdvTile(new FloorTile(s, FloorTileType.values()[t])));
	}
}