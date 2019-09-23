package levelMap;

import arrow.*;
import com.fasterxml.jackson.jr.ob.*;
import entity.*;
import geom.f1.*;
import item.*;
import java.io.*;
import java.nio.*;
import java.util.*;

public class LevelMap
{
	public final TileType y1;
	private final HashMap<Tile, AdvTile> advTiles;
	private final List<Boolean> visibleSectors;
	private final ArrayList<XHero> entitiesH;
	private final ArrayList<XEnemy> entitiesE;
	private final List<VisMark> visMarked;
	private final ArrayList<XArrow> arrows;

	public LevelMap(TileType y1)
	{
		this.y1 = y1;
		advTiles = new HashMap<>();
		visibleSectors = new ArrayList<>();
		entitiesH = new ArrayList<>();
		entitiesE = new ArrayList<>();
		visMarked = new ArrayList<>();
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
		XArrow arrow = XArrow.factory(entity.location(), newLocation, y1, false, entity.getImage());
		addArrow(arrow);
		entity.setReplacementArrow(arrow);
		advTile(entity.location()).setEntity(null);
		entity.setLocation(newLocation);
		advTile(newLocation).setEntity(entity);
	}

	public void swapEntities(XEntity entity1, XEntity entity2)
	{
		Tile location1 = entity1.location();
		Tile location2 = entity2.location();
		XArrow arrow1 = XArrow.factory(location1, location2, y1, false, entity1.getImage());
		addArrow(arrow1);
		entity1.setReplacementArrow(arrow1);
		XArrow arrow2 = XArrow.factory(location2, location1, y1, false, entity2.getImage());
		addArrow(arrow2);
		entity2.setReplacementArrow(arrow2);
		advTile(location1).setEntity(entity2);
		advTile(location2).setEntity(entity1);
		entity1.setLocation(location2);
		entity2.setLocation(location1);
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

	public ArrayList<XHero> getEntitiesH()
	{
		return entitiesH;
	}

	public ArrayList<XEnemy> getEntitiesE()
	{
		return entitiesE;
	}

	public List<VisMark> getVisMarked()
	{
		return visMarked;
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
		visMarked.clear();
		arrows.forEach(XArrow::tick);
		arrows.removeIf(XArrow::finished);
	}

	public String[] saveDataJSON(ItemLoader itemLoader)
	{
		try
		{
			var a1 = JSON.std.with(JSON.Feature.PRETTY_PRINT_OUTPUT)
					.composeString()
					.startObject()
					.put("code", 0xA4D2839F);
			var xheroSave = JSON.std.with(JSON.Feature.PRETTY_PRINT_OUTPUT)
					.composeString()
					.startObject()
					.put("code", 0xA4D2839F)
					.startArrayField("Characters");
			List<XEntity> entities = new ArrayList<>();
			List<MBuilding> buildings = new ArrayList<>();
			ByteBuffer sb = ByteBuffer.allocate(advTiles.size() * 4);
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
					if(adv.getBuilding() != null && adv.getBuilding().active())
						buildings.add(adv.getBuilding());
					if(adv.getEntity() != null)
						entities.add(adv.getEntity());
				}
			}
			var a2 = a1.put("FloorTiles", Base64.getEncoder().encodeToString(sb.array()))
					.startArrayField("Buildings");
			for(MBuilding building : buildings)
			{
				a2 = building.save(a2.startObject(), itemLoader, y1).end();
			}
			var a3 = a2.end().startArrayField("XEntities");
			for(XEntity entity : entities)
			{
				a3 = entity.save(a3.startObject(), itemLoader, y1).end();
				if(entity instanceof XHero)
					xheroSave = entity.save3(xheroSave.startObject(), itemLoader).end();
			}
			return new String[]{a3.end().end().finish(), xheroSave.end().end().finish()};
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void createTile(byte x, byte y, byte s, byte t)
	{
		while(s >= visibleSectors.size())
			visibleSectors.add(true);
		advTiles.put(y1.create2(x, y), new AdvTile(new FloorTile(s, FloorTileType.values()[t])));
	}
}