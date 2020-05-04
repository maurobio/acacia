/*
 * Copyright (C) 2001-2006 University Bayreuth, BayCEER, Dept. of Mycology 
 * 
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
/*
 * DeltaData.java
 *
 * Created on 20. Februar 2006, 16:56
 *
 * @author Dieter Neubacher
 */

package net.metadiversity.diversity.navikey.delta;

import java.util.Vector;
import java.util.HashMap;

import net.metadiversity.diversity.navikey.bo.Item;
import net.metadiversity.diversity.navikey.bo.ItemCharacter;
import net.metadiversity.diversity.navigator.db.delta_editor.Dependencies;
/**
 *
 * @author Dieter Neubacher
 */
public class DeltaData
{
    Vector itemCharacterTypes;
    java.util.Hashtable itemCharacters;
    Vector items;

    HashMap itemImages;
//    Vector inapplicableVector = null;
    Vector< Dependencies > dependenciesVector = null;
    
    /** Creates a new instance of DeltaData */
    public DeltaData()
    {
        itemCharacterTypes  = new java.util.Vector();
        itemCharacters      = new java.util.Hashtable();
        items               = new java.util.Vector();
        
        itemImages             = new java.util.HashMap();
        // Dependencies
  //      inapplicableVector  = null;
        dependenciesVector  = null;

    }
  
    public void setItemCharacterTypesVector(  Vector v )
    {
        itemCharacterTypes = v;
    }
    public void setDependenciesVector( Vector< Dependencies > v )
    {
        dependenciesVector = v;
    }
    public void setItemCharacters( java.util.Hashtable ht )
    {
        itemCharacters = ht;
    }
    public void setItemsVector(  Vector v )
    {
        items = v;
    }
  
    public Vector getItemCharacterTypesVector()
    {
        return itemCharacterTypes;
    }
    public java.util.Hashtable getItemCharacters()
    {
        return itemCharacters;
    }
    /*
    public Vector getItemCharactersVector()
    {
        return itemCharacters;
    }
     */
    public Vector getItemsVector()
    {
        return items; 
    }
    
    public ItemCharacter getItemCharacter( int id )
    {
        return ( ItemCharacter ) itemCharacters.get( new Integer(id) );
    }
    public Item getItem( int itemId )
    {
        return ( Item ) items.get( itemId );    
    }
    // Images
    public HashMap getItemImageHashMap( )
    {
        return itemImages;
    }
    public void setItemImageHashMap( HashMap hm )
    {
        itemImages = hm;
    }
    /*
    public Item getItem( Integer itemId )
    {
        Item item = ( Item )cache.get( id );
        if( item == null )
        {
            item = new Item();
            item.setId( id );
            item.restore();
        }
        //try again.
        item = ( Item )cache.get( id );
        return item;    
    }
    */

    public Vector< Dependencies > getDependenciesVector() 
    {
        return dependenciesVector;
    }
}
