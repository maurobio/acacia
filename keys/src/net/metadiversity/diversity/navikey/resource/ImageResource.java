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
package net.metadiversity.diversity.navikey.resource;
import java.net.URL;

import net.metadiversity.diversity.navikey.bo.Resource;

/**
 * ImageResource
 * a multimedia display of image, uses IconPanel and ImageFrame to display
 * @author Michael Bartley, based on Image by Noel Cross and Rick Ree
 * 7/16/98
 */


public class ImageResource extends Resource
{
    protected String author = null;
    protected String copyrightInfo =  null;
    
    public ImageResource()
    {
        mimeType = "image";
    }
    
    public ImageResource(String fname)
    {
        this(fname, "image");
    }
    
    public ImageResource(String fname, String mimeType)
    {
        super.setMimeType(mimeType);
        super.setFile(fname);
    }
    
    public ImageResource(URL location)
    {
        this(location, "image");
    }
    
    public ImageResource(URL location, String mimeType)
    {
        super(location, mimeType);
    }
    
    public void display()
    {
        if(location == null)
        {
            System.out.println("Location of resource not set.");
            return;
        }
        ImageFrame iff = new ImageFrame(location, toString());
        iff.setTitle(super.title);
    }
    
    public void setAuthor(String s)
    {
        author = s;
    }
    public void setCopyrightInfo(String s)
    {
        copyrightInfo = s;
    }
    
    public String getAuthor()
    {
        return author;
    }
    public String getCopyrightInfo()
    {
        return copyrightInfo;
    }
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        if(super.title != null)
        {
            sb.append(super.title);
        }
        if(author != null)
        {
            sb.append(" by ");
            sb.append(author);
        }
        if(copyrightInfo != null)
        {
            sb.append(".  Copyright: ");
            sb.append(copyrightInfo);
        }
        if(super.notes != null)
        {
            sb.append("  Notes: ");
            sb.append(super.notes);
        }
        sb.append(" ");
        sb.append(super.location.toString());
        return new String(sb);
    }
    
    public static void main(String arg[])
    {
        try
        {
            ImageResource ir = new ImageResource(new URL("http://dev.huh.harvard.edu/~mbartley/images/test.jpg"), "jpeg");
            ir.setTitle("A test");
            ir.setAuthor("Michael Bartley");
            ir.setCopyrightInfo("none");
            ir.setNotes("none");
            ir.display();
        }
        catch(Exception e)
        {
            System.out.println("It ain't workin'" +e);
        }
    }
    
}
