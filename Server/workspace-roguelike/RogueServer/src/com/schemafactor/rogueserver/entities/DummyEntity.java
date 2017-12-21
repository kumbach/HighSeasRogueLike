package com.schemafactor.rogueserver.entities;

public class DummyEntity extends Entity
{  
    /** Creates a new instance */
    public DummyEntity(Position startposition)
    {
       super("Dummy", startposition, entityTypes.NONE, (byte)0, 0f);
    }

	@Override
	public void update() 
	{	
		;	
	}   
	
	@Override
    public void updateNow()
    {
       ;        
    }
}