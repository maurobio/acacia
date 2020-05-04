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

/**
 * @author Noel Cross
 * @author  Michael Bartley
 */
public class TextState extends State
{
    private String value;
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public TextState()
    {
        super();
        value = "";
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setValue(String value)
    {
        this.value = value;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String getValue()
    {
        return value;
    }    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    // ???
    public boolean containedIn(Vector v)
    {
        return true;
    }    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String getStringValue()
    {
        return getName() + value;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String toString()
    {
        return "name: " + getName() + "\nvalue: " + value;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void print()
    {
        System.out.println(toString());
    }
}
