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
package net.metadiversity.diversity.navikey.bo;

import java.util.Hashtable;
import java.util.Vector;

import net.metadiversity.diversity.navikey.ui.NaviKey;


/**
 *
 * This class models a DELTA Item.
 *
 * @author Noel Cross
 * @author Michael Bartley
 * @author Dieter Neubacher
 */

public class Item extends BasicItem implements java.util.Comparator, java.lang.Comparable
{
    private Vector<Attribute>          attributes;   
    // private Hashtable<Object, Object>  cache;
    
    private boolean excludeUndefinedStates = true;
    // used for multible "OR" selection
    private java.util.HashSet<Integer> multibleOrHashSet = null;
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public Item()
    {
        super();
        attributes  = new Vector<Attribute>();
        // cache       = new Hashtable<Object, Object>();
    }    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
/*    
    public void setId( Object id )
    {
        this.id = ( Integer )id;
        if( id != null )
        {
            cache.put( this.id, this );
        }
    }    
 */
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setAttributes( Vector<Attribute> attributes )
    {
        this.attributes = attributes;
    }
    //--------------------------------------------------------------------------
    // Init date used by cannotBe().
    // This data is used for the multiple "OR" proccessing.
    // For speed reasons, it is a saparate function, only proccessd ones. 
    //--------------------------------------------------------------------------
    public void initCannotBe()
    {
        java.util.HashSet<Integer> hs = new java.util.HashSet<Integer>();
        multibleOrHashSet = new java.util.HashSet<Integer>();
        
        for( int i = 0; i < attributes.size(); i++ )
        { 
            Attribute thisAttrib = attributes.elementAt(i);
            Integer   id = thisAttrib.ic.id;
            
            System.out.println( thisAttrib.ic.id );
            
            if( hs.contains( id ) )
            {
                multibleOrHashSet.add( id );
            }
            else
            {
               hs.add( id );                
            }
        }        
    }
            
    
    
    
    /**
     * Returns true if a given Item has no states that contradict mine.
     * This includes
     */
    public boolean cannotBe( NaviKey navikey, Item otherItem )
    {        
        if( navikey.isExcludeUndefined() )
        {
            excludeUndefinedStates = true;
        }
        else
        {
            excludeUndefinedStates = false;
        }
            
        //assume we could be the same until contradicted
        //loop though all my attributes
        //REVERSED COMPARISON ORDERS FOR SPEED GAINS -- MIKE 6/7/99
        
        // multiple selections 
        boolean multipleOR;
        boolean multipleOrFlag;
        if( navikey.isMultipleSelectionOR() )
        {
            multipleOrFlag = true;
            multipleOR     = true;
        }
        else
        {
            multipleOrFlag = false;
            multipleOR     = false;
        }
        
        java.util.HashSet<Integer> multibleOrFoundHashSet = new java.util.HashSet<Integer>();
        
        for( int i = 0; i < attributes.size(); i++ )
        {
            Attribute thisAttrib = attributes.elementAt(i);            
            Integer   id = thisAttrib.ic.id;

            if( multipleOrFlag )
            {
                if( multibleOrHashSet.contains( id ) )
                {    
                    multipleOR = true;
                }
                else
                {
                    multipleOR = false;
                }
            }
            //if this otherItem has this attribute, get the attribute
            ItemCharacter ic = thisAttrib.getItemCharacter();
            
            if( otherItem.containsItemCharacter( ic ) )
            {
                Attribute theOtherAttrib = otherItem.getAttributeWithItemCharacter( ic );
                
                if( multipleOR )
                {    
                    if( theOtherAttrib.sameAs( navikey, thisAttrib ) )
                    {
                        // is the same item
                        multibleOrFoundHashSet.add( id );
                        continue;
                    }
                }
                else
                {    
                    if( ! theOtherAttrib.sameAs( navikey, thisAttrib ) )
                    {
                        // can't be the same item
                        return true;
                    }
                }                  
            }
            else
            {
                if( ! excludeUndefinedStates )
                {
                    // Item don't has this attribute
                    if( multipleOR )
                    {
                        multibleOrFoundHashSet.add( id );
                        continue;                        
                    }
                }
                else
                {
                    return true; // This attribute is not defined.
                }
            }
        }
        if( ! multipleOrFlag )
        {
            return false;
        }
        else if( multibleOrFoundHashSet.size() == multibleOrHashSet.size() )
        {
            // all multiple sellections evaluated.
            return false;
        }    
        else
        {
            return true;
        }    
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public Vector getAttributes()
    {
        return attributes;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void addAttribute( Attribute a )
    {
        if( a !=  null )
        {
            attributes.addElement( a );
        }
    }    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public Attribute getAttributeWithState( State st )
    {
        int size = attributes.size();
        for( int i = 0; i < size; i++ )
        {
            Attribute a = attributes.elementAt(i);
            if( a.containsState( st ) )
            {
                return a;
            }
        }
        return null;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public Attribute getAttributeWithItemCharacter( ItemCharacter ic )
    {
        int size = attributes.size();
        for( int i = 0; i < size; i++ )
        {
            Attribute a = attributes.elementAt(i);
            ItemCharacter ic2 = a.getItemCharacter();
            if( ic == ic2 )
            {
                return a;
            }
        }
        return null;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public boolean hasAttribute( Attribute a )
    {
        return attributes.contains( a );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public boolean containsState( State s )
    {
        int size = attributes.size();
        for( int i = 0; i < size; i++ )
        {
            Attribute a = attributes.elementAt(i);
            if( a.containsState( s ) )
            {
                return true;
            }
        }
        return false;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public Vector<ItemCharacter> getItemCharacters()
    {
        Vector<ItemCharacter> v = new Vector<ItemCharacter>();
        int size = attributes.size();
        for( int i = 0; i < size; i++ )
        {
            Attribute a = attributes.elementAt(i);
            v.addElement( a.getItemCharacter() );
        }
        return v;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public boolean containsItemCharacter( ItemCharacter ic )
    {
        return getItemCharacters().contains( ic );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void clearAttributes()
    {
        attributes.removeAllElements();
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void print()
    {
        System.out.println( toString() );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append( name );
        for( int i = 0; i < attributes.size(); i++ )
        {
            Attribute a = attributes.elementAt(i);
            sb.append( a.toString() );
        }
        return new String( sb );
    }
    //--------------------------------------------------------------------------
    // used for sorting the selection list
    //--------------------------------------------------------------------------
    public int compare( java.lang.Object a, java.lang.Object b )
    {
        return ((Item) a).getName().compareTo( ((Item) b).getName() ); 
    }
    public int compareTo( java.lang.Object o )
    {
        return this.getName().compareTo( ((Item) o).getName() ); 
    }
/*
    //--------------------------------------------------------------------------
    // Persistence stuff
    //--------------------------------------------------------------------------
    public void setPersistentPeer( PersistentPeer peer )
    {
        thePeer = peer;
    }
    //--------------------------------------------------------------------------
    public PersistentPeer getPersistentPeer()
    {
        return thePeer;
    }    
    //--------------------------------------------------------------------------
    public Item restore( Integer id )
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
    //--------------------------------------------------------------------------
    public void restoreAll()
    {
        System.out.println( "Start restoreAll" );
        Item item = new Item();
        item.setId( null );
        item.restore();
        System.out.println( "End restoreAll" );
    }
    //--------------------------------------------------------------------------
    public Enumeration elements()
    {
        return cache.elements();
    }
    //--------------------------------------------------------------------------
    public void restore()
    {
        try
        {
            thePeer.restore( this );
        }
        catch( NothingRestoredException e )
        {
        }
    }
    //--------------------------------------------------------------------------
    public void update()
    {
        thePeer.update( this );
    }
    //--------------------------------------------------------------------------
    public void insert()
    {
        thePeer.insert( this );
    }
    //--------------------------------------------------------------------------
    public void delete()
    {
        thePeer.delete( this );
    }    
 */
}
