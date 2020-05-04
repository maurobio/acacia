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

import java.net.URL;

/**
 * Resource.java
 * resources will be displays of useful information (ie. images/sounds/urls)
 * @author Michael Bartley
 * 7/16/98
 */

public abstract class Resource
{
    public    String defaultLocation;
    
    protected String mimeType = null;
    protected URL location =  null;
    protected String file = null;
    protected String notes = null;
    protected String title = null;
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    protected Resource()
    {
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    protected Resource(URL url, String s)
    {
        location = url;
        mimeType = s;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public abstract void display();
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setMimeType(String s)
    {
        mimeType = s;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setLocation(URL url)
    {
        location = url;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setNotes(String s)
    {
        notes = s;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setTitle(String s)
    {
        title = s;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setLocation(String url)
    {
        try
        {
            location = new URL(url);
        }
        catch(Exception e)
        {
            System.out.println(e+"\nPassed bad url for location: "+url);
        }
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    public void setFile(String fname)
    {
        file = fname;
        setLocation(defaultLocation + file);
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public URL getLocation()
    {
        return location;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String getMimeType()
    {
        return mimeType;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String getFile()
    {
        return file;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String getNotes()
    {
        return notes;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String getTitle()
    {
        return title;
    }
}
