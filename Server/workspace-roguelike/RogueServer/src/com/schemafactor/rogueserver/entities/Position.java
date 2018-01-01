package com.schemafactor.rogueserver.entities;

public class Position implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;
    
    public int x;
	public int y;
	public int z;  // Level
	
	public Position(int x, int y, int z) 
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Position(Position pos)
	{
		this.x = pos.x;
		this.y = pos.y;
		this.z = pos.z;
	}
}
