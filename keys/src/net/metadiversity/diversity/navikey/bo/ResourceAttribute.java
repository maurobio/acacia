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

import net.metadiversity.diversity.navikey.ui.NaviKey;

import java.util.Vector;

/**
 * ResourceAttribute is a special TextAttribute -- it deals with Resources or
 * multimedia objects
 * @author Michael Bartley
 * 7/16/98
 */

public class ResourceAttribute extends Attribute
{
    private Vector<Object> resources;
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public ResourceAttribute()
    {
        super();
        resources = new Vector<Object>();
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public Vector getResources()
    {
        return resources;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void addResource(Resource r)
    {
        resources.addElement( new ResourceState( r ) );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public int countResources()
    {
        return resources.size();
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //FOLLOWING MUST BE IMPLEMENTED DUE TO INHERITENCE, but may be meaningless
    //////////////////////////////////////
    public void addCommentedState(State st, String comment)
    {
        return;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public Vector getCommentedStates()
    {
        return resources;
    }
    
    public boolean containsState(State s)
    {
        return false;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public Vector getStates()
    {
        return  resources;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public boolean sameAs( NaviKey navikey, Attribute a)
    {
        return false;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        for(int i=0; i< resources.size(); i++)
        {
            sb.append(( (Resource) resources.elementAt(i)).toString() );
        }
        return new String(sb);
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void print()
    {
        System.out.println(toString());
    }
}
