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
 * ResourceState is a special TextState -- it deals with Resources or
 * multimedia objects
 * @author Michael Bartley
 * 7/16/98
 */

public class ResourceState extends State
{
    private Resource resource;
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public ResourceState()
    {
        super();
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public ResourceState(Resource r)
    {
        super();
        resource = r;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setResource(Resource r)
    {
        resource = r;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public Resource getResource()
    {
        return resource;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    // ???
    public boolean containedIn(Vector v)
    {
        return false;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String toString()
    {
        return resource.getLocation().toString() + "\n" + resource.getMimeType();
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String getStringValue()
    {
        return toString();
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void print()
    {
        System.out.println(toString());
    }
}
