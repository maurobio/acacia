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
 * <pre>
 * This class  models a DELTA  character file as defined in the
 * Definition of the Delta Format
 * 14 March, 1995
 * Dallwitz & Paine
 *
 * </pre>
 * @author Noel Cross
 * @author Michael Bartley
 * @author Dieter Neubacher
 */

public class BasicItemCharacter implements java.lang.Comparable
{
    protected Integer id;
    protected String type;  //UM, OM, RN, IN, or TE
    protected String feature;        
    
    //boolean isMandatory
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public BasicItemCharacter()
    {
        //id = new Integer(0);
        type = "UM"; //default to unordered multi.
        feature = "";
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
    public void setType(String type)
    {
        this.type = type;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setFeature(String feature)
    {
        this.feature = feature;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setName(String s)
    {
        setFeature(s);
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public Integer getId()
    {
        return id;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String getType()
    {
        return type;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String getFeature()
    {
        return feature;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String getName()
    {
        return feature;
    }
    //--------------------------------------------------------------------------
    // implement Sortable
    //--------------------------------------------------------------------------
    /*
    public boolean greaterThan(Sortable s)
    {
        boolean rv = false;
        if(s instanceof BasicItemCharacter)
        {
            int i = ((Integer)s.getId()).intValue();
            rv = ((id.intValue() > i) ? true : false);
        }
        return rv;
    }
    
    public boolean lessThan(Sortable s)
    {
        boolean rv = false;
        if(s instanceof BasicItemCharacter)
        {
            int i = ((Integer)s.getId()).intValue();
            rv = ((id.intValue() < i) ? true : false);
        }
        return rv;
    }
    
    public boolean equalTo(Sortable s)
    {
        boolean rv = false;
        if(s instanceof BasicItemCharacter)
        {
            int i = ((Integer)s.getId()).intValue();
            rv = ((id.intValue() == i) ? true : false);
        }
        return rv;
    } 
     */   
    //--------------------------------------------------------------------------
    // implement java.lang.Comparable
    //--------------------------------------------------------------------------
    public int compareTo( Object obj )
    {
        return id.compareTo( ((BasicItemCharacter) obj).getId() );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    public boolean isNumeric()
    {
        if( type.equals( "IN" ) || type.equals( "RN" ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    public boolean isText()
    {
        boolean b = false;
        if( type.equals( "TE" ) )
        {
            b = true;
        }
        return b;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    public boolean isMultiState()
    {
        if( type.equals( "UM" ) || type.equals( "OM" ) )
        {
            return true;
        }
        else
        {
            return false;
        }
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
        return new String(sb);
    }        
}










