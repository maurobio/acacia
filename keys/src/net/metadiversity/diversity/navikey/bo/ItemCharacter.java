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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * <pre>
 * This class  models a DELTA  character file as defined in the
 * Definition of the Delta Format
 * 14 March, 1995
 * Dallwitz & Paine
 *
 * We have substituted the name ItemCharacter for Character,
 * for convenience, since Java already has a Character class.
 *
 * </pre>
 * @author Noel Cross
 * @author Michael Bartley
 * @author Dieter Neubacher
 */

public class ItemCharacter extends BasicItemCharacter
{
    private Vector<State> states;    
    private Hashtable<Integer, Object> cache;
    
    //boolean isMandatory
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public ItemCharacter()
    {
        super();
        states  = new Vector<State>();
        cache   = new Hashtable<Integer, Object>();
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setId(Object o)
    {
        if(o instanceof Integer)
        {
            Integer id = (Integer)o;
            this.id = id;
            if(id != null)
            {
                cache.put(this.id, this);
            }
        }
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setFeature(String s)
    {
        super.setFeature(s);
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setStates( Vector<State> states)
    {
        this.states = states;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void addState( State state)
    {
        states.addElement( state );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void removeState( State state )
    {
        states.removeElement( state );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    public Vector<State> getStates()
    {
        return states;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public MultiState getMultiState( Integer id )
    {
        for(int i = 0; i < states.size(); i++)
        {
            MultiState ms = (MultiState)states.elementAt(i);
            if(ms.getId().equals(id))
            {
                return ms;
            }
        }
        // if no matching id is found then:
        return null;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void print()
    {
        System.out.println(toString());
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        if(id != null)
        {
            sb.append("id :" + id);
        }
        if(type != null)
        {
            sb.append("type :" + type);
        }
        if(feature != null)
        {
            sb.append("feature :" + feature);
        }
        if(states != null)
        {
            sb.append("There are " + states.size() + " states:");
            for(int i = 0; i < states.size(); i++)
            {
                State s = states.elementAt(i);
                sb.append(s.toString());
            }
        }
        return new String(sb);
    }
}
