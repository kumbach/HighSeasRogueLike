package com.schemafactor.rogueserver.entities;

import java.time.Instant;
import java.util.List;

import com.schemafactor.rogueserver.common.Constants;
import com.schemafactor.rogueserver.common.JavaTools;
import com.schemafactor.rogueserver.items.Item;
import com.schemafactor.rogueserver.universe.Cell;
import com.schemafactor.rogueserver.universe.Dungeon;

public abstract class Entity 
{
   public static enum entityTypes {NONE, HUMAN_PLAYER, NPC, MONSTER}
   protected entityTypes myType = entityTypes.NONE;
	   
   protected String description;
   protected Position position;
   
   protected byte charCode = 0;   // Character code shown on client screen
   
   /** Flag that this entity is to be removed at the end of this update cycle.  true=remove */
   protected boolean removeMeFlag = false;
   
   protected Instant lastAction = Instant.now();         // Used by server-controlled entities
   
   Item item = null;  // Currently carried item
     
   /** Creates a new instance of Entity */
   public Entity(String description, Position startposition, entityTypes type, byte charCode)
   {
       this.description = new String(description);
       this.position = Dungeon.getInstance().getClosestEmptyCell(startposition, 10);
       this.myType = type;
       this.charCode = charCode;
       
       // Mark cell this entity starts in
       Dungeon.getInstance().getCell(startposition).setEntity(this);
   }
   
   /**
    * @param direction
    * @return true on success, false if blocked
    */
   protected boolean attemptMove(byte direction) 
   {
       int dx=0;
       int dy=0;
       int dz=0;
       
       Cell current_cell = Dungeon.getInstance().getCell(this.position);
       
       switch (direction)
       {
          case Constants.DIRECTION_NONE:          
              break;
              
          case Constants.DIRECTION_NORTH:
              dy=-1;
              break;
              
          case Constants.DIRECTION_NE:
              dy=-1;
              dx=+1;
              break;
              
          case Constants.DIRECTION_EAST:
              dx=+1;
              break;
              
          case Constants.DIRECTION_SE:
              dy=+1;
              dx=+1;
              break;
              
          case Constants.DIRECTION_SOUTH:
              dy=+1;
              break;
                
          case Constants.DIRECTION_SW:
              dy=+1;
              dx=-1;
              break;
              
          case Constants.DIRECTION_WEST:
              dx=-1;
              break;              
              
          case Constants.DIRECTION_NW:   
              dx=-1;
              dy=-1;
              break;
          
          case Constants.DIRECTION_UP:
              // if current cell type == stairs_up...
              break;
              
          case Constants.DIRECTION_DOWN:
             if (current_cell.getTrueCharCode() == Constants.CHAR_STAIRS_DOWN)
             {
                 dz = 1;
             }
             else
             {
                 return false;
             }
             break;
             
          default:
             JavaTools.printlnTime("Unknown move direction code " + direction + " from " + description);
             return false;            
       }
       
       Position destination = new Position(this.position.x+dx, this.position.y+dy, this.position.z+dz);     
       Cell dest_cell = Dungeon.getInstance().getCell(destination);
       
       if (dest_cell == null)
       {
           JavaTools.printlnTime("DEBUG: " + description + " attempted to move out of map?  Last known location X=" + position.x + " Y=" + position.y + " Z=" + position.z);
           return false;
       }
       
       if (dest_cell.canEnter())
       {
           // Update cell references
           this.position = destination;
           dest_cell.setEntity(this);
           current_cell.setEntity(null);
           
           //JavaTools.printlnTime("DEBUG: " + description + " moved to location X=" + position.x + " Y=" + position.y + " Z=" + position.z);
           return true;
       }
       else
       {
           //JavaTools.printlnTime("DEBUG: " + description + " was blocked moving to X=" + destination.x + " Y=" + destination.y + " Z=" + destination.z);
           return false;
       }    
   }
   
   protected boolean attemptPickup()
   {
       if (item != null)  // Already carrying something
       {
           return false;
       }
       
      Cell current_cell = Dungeon.getInstance().getCell(this.position);      
      item = current_cell.takeItem();   
      
      if (item != null) 
      {
          JavaTools.printlnTime("DEBUG: " + description + " picked up " + item.getDescription());
          return true;
      }
      else  // Nothing picked up
      {
          JavaTools.printlnTime("DEBUG: " + description + " picked up nothing");
          return true;
      }
   }
   
   // TODO - Prevent drop on cells that aren't truly empty (i.e stairs)
   protected boolean attemptDrop()
   {
       if (item == null)  // Not carrying anything
       {
           return false;
       }
       
      Cell current_cell = Dungeon.getInstance().getCell(this.position);     
      
      boolean success = current_cell.addItem(item);  
      
      if (success)
      {
          item = null;   // No longer carrying the item
      }
      
      return success;
   }
   
   abstract public void update();       // Called on every game loop
   abstract public void updateNow();    // Called from update(), or from other Entities to force an update
   
   /** Return X,Y positions */
   public int getXpos()
   {
       return position.x;
   }
   
   public int getYpos()
   {
       return position.y;
   }
   
   public String getDescription() 
   {
       return description;
   }
   
   // Helper function to get distance to another Entity
   public double distanceTo(Entity target)
   {   
       if (target == null)
       {
           return Double.MAX_VALUE;
       }
       
       return Math.sqrt( Math.pow((this.getXpos() - target.getXpos()), 2) + Math.pow((this.getYpos() - target.getYpos()), 2)); 
   }

    public void removeMe() 
    {   
        // Clear cell this entity is removed from
        Dungeon.getInstance().getCell(position).setEntity(null);
        removeMeFlag = true;
    }
    
    public boolean getRemoved() 
    {   
        return removeMeFlag;
    }

	public byte getCharCode() 
	{
		return charCode;
	}

	public entityTypes getType() 
	{		
		return myType;
	}
	
	protected void finishMove(boolean moved)
    {
        // If we moved, update other entities in area.
	    // TODO, future:  Set this as a flag, and update all at once at end of turn
	    if (moved)
	    {
            lastAction = Instant.now();            
            List<Entity> onscreen = Dungeon.getInstance().getEntitiesOnScreenCentered(this.position);
            
            // Don't update self
            onscreen.remove(this);
            
            for (Entity e : onscreen)
            {
                e.updateNow();
            }            
        }        
    }   
}