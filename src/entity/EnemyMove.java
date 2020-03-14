package entity;

import system2.*;

public record EnemyMove(XCharacter entity, int initiative, PathLocation moveTo, AttackInfo attackInfo, int tileAdvantage){}