package com.schemafactor.rogueserver.items;

import com.schemafactor.rogueserver.common.Constants;

public class Sword extends Item
{
    /** Creates a new instance of Sword */
   public Sword(String description, float maxDamage)
   {
       super(description, itemTypes.WEAPON, Constants.CHAR_ITEM_SWORD, true, maxDamage); 
   }
}