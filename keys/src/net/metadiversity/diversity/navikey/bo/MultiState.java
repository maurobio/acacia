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
 * @author Noel Cross
 * @author Michael Bartley
 * @author Dieter Neubacher
 */
public class MultiState extends State 
{
    public boolean ordered;
    Integer id;
    
    private int count = 0;
    private Hashtable< Integer, Object > cache;
    
    public MultiState()
    {
        super();
        cache = new Hashtable< Integer, Object >();
        count++;        
        ordered = false;
    }
    //----------------------------------------------------------------------    
    public void setId(Object id)
    {
        this.id = (Integer)id;
        if(id != null)
        {
            cache.put(this.id, this);
        }
    }
    
    //----------------------------------------------------------------------    
    public Object getId() 
    {
        return id;
    }
    
    //----------------------------------------------------------------------    
    public void setOrdered() 
    {    
        ordered = true;
    }
    //----------------------------------------------------------------------    
    public void setUnOrdered() 
    {
        ordered = false;
    }
    //----------------------------------------------------------------------    
    public boolean isOrdered() 
    {
        return ordered;
    }
    //----------------------------------------------------------------------    
    public boolean isUnOrdered() 
    {
        return ordered;
    }
    
    //----------------------------------------------------------------------    
    public boolean containedIn(Vector states)
    {
        boolean b = false;
        int limit = states.size();
        for(int i = 0; i < limit; i++)
        {
            State state = (State)states.elementAt(i);
            if(state == this)
            {
                b = true;
            }
        }
        return b;
    }
    
    //----------------------------------------------------------------------    
    public String getStringValue()
    {
        return getName();
    }
    //----------------------------------------------------------------------    
    public String toString()
    {
        return "name: " + getName();
    }
    //----------------------------------------------------------------------    
    public void print()
    {
        System.out.println(toString());
    }
}
