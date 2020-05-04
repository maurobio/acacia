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

import java.util.Vector;
import net.metadiversity.diversity.navikey.delta.DeltaData;


/**
 * State.java
 * @author Noel Cross
 * @author Michael Bartley
 * @author Dieter Neubacher
 */
public abstract class State extends BasicState
{    
    private ItemCharacter ic; // The character this state belongs to
 
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public State()
    {
        ic = new ItemCharacter();
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public ItemCharacter getItemCharacter()
    {
        return ic;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setItemCharacter( ItemCharacter ic )
    {
        this.ic = ic;
        setCharacterId( ic.getId() );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /** True if this state is contained in a Vector of States */
    public abstract boolean containedIn(Vector states);
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public static State getState( BasicState bs, DeltaData dd )
    {
        ItemCharacter ic = (ItemCharacter) dd.getItemCharacters().get( bs.getCharacterId() );
        
        if( ic == null )    // ???
        {
            return new NumericState( bs, dd );
        }
        else if( ic.isMultiState() )
        {
            Vector states = ic.getStates();
            for( int i=0; i < states.size(); i++ )
            {
                State s = (State)states.elementAt( i );
                if( s.getName().equals( bs.getName() ) )
                {
                    return s;
                }
            }
        }
        else if( ic.isNumeric() )
        {
            return new NumericState( bs, dd );
        }
        return null;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public abstract String getStringValue();
    public abstract void print();
    public abstract String toString();
}

