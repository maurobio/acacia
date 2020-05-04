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
package net.metadiversity.diversity.navikey.delta;

/**
 * ItemCharacterType.java
 * @author Noel Cross
 * @author Dieter Neubacher
 */

public class ItemCharacterType
{
    private Integer id;
    private String abbrev;
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public  ItemCharacterType(Integer id, String abbrev)
    {
        this.id = id;
        this.abbrev = abbrev;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public Integer getId()
    {
        return(id);
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String getAbbrev()
    {
        return(abbrev);
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void print()
    {
        System.out.println("id: " + id + " abbrev: " + abbrev);
    }
}
