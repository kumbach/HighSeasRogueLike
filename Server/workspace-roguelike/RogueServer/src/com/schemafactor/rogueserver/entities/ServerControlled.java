package com.schemafactor.rogueserver.entities;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.entities.Entity;
import com.schemafactor.rogueserver.universe.Dungeon;

public abstract class ServerControlled extends Entity
{  
    // The entity this creature is currently chasing    
    Entity target = null;   
    
    // States
    protected enum States {IDLE, WANDERING, CHASING, ATTACKING, RETREATING};
    protected States State = States.IDLE;
    
    // Time in between moves.  May change based on state.  
    float actionTime = 1000f;  // Milliseconds    
       
    /** Creates a new instance of Server Controlled */
    public ServerControlled(String name, Position startposition, entityTypes type, byte charCode, float actionTime, float maxDamage)
    {
       super(name, startposition, type, charCode, maxDamage);  
       this.actionTime = actionTime;
    }
    
    @Override
    public void update() 
    {
        Duration elapsed = Duration.between(lastAction, Instant.now());
        
        if (elapsed.toMillis() <= actionTime)   // Move at this rate
        {
            return;   // Not time to act yet           
        }
        else
        {
            takeAction();
        }
    }
    
    @Override
    public void updateNow()
    {
       ;        
    }
    
    abstract protected void takeAction();  // Action to take when actionTime occurs
}