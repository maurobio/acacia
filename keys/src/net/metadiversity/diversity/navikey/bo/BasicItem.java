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
 *
 * BasicItem.java
 *
 * @author Noel Cross
 * @author Michael Bartley
 * @author Dieter Neubacher
 */

public class BasicItem implements java.lang.Comparable, java.util.Comparator
{
    protected Integer   id;
    protected String    name;    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public BasicItem()
    {
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setId(Object id)
    {
        if (id instanceof Integer)
        {
            this.id = (Integer)id;
        }
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setName(String name)
    {
        this.name = name;
    }    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public Integer getId()
    { 
        return id;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String getName()
    { 
        return name;
    }
    //--------------------------------------------------------------------------
    // implement java.lang.Comparable
    //--------------------------------------------------------------------------
    public int compareTo( Object obj )
    {
        return id.compareTo( ((BasicItem) obj).getId() );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public int compare(Object obj, Object obj1)    
    {
        return (((BasicItem) obj).id).compareTo( ((BasicItem) obj1).id );        
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String toString()
    {
        return id + ": " + name;
    }
}
