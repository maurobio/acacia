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


/**
 * State.java
 * @author Noel Cross
 * @author Michael Bartley
 * @author Dieter Neubacher
 */
public class BasicState implements java.util.Comparator, java.lang.Comparable
{
    
    private String  name;
    private Integer characterId;
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public BasicState()
    {
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String getName()
    {
        return(name);
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public Integer getCharacterId()
    {
        return characterId;
    }    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setName(String name)
    {
        this.name = name;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setCharacterId( Object id )
    {
        if(id instanceof Integer)
        {
            characterId = (Integer)id;
        }
    }
    //--------------------------------------------------------------------------
    // used for sorting the selection list
    //--------------------------------------------------------------------------
    public int compare( java.lang.Object a, java.lang.Object b )
    {
        return ((BasicState) a).getName().compareTo( ((BasicState) b).getName() ); 
    }
    public int compareTo( java.lang.Object o )
    {
        return this.getName().compareTo( ((BasicState) o).getName() ); 
    }
    
    
            
}

