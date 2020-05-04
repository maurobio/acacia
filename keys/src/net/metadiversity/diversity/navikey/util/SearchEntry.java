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
/*
 * SearchEntry.java
 * 
 * Created on 08.11.2007, 15:32:13
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.metadiversity.diversity.navikey.util;

/**
 *
 * @author didi
 */
public class SearchEntry 
{
    String guiString;
    String uri;
    boolean mappingFlag;
    String mappingFile;
    String regex;

    
    public SearchEntry()            
    {
        this.guiString      = "";
        this.uri            = "";
        this.mappingFile    = "";
        this.regex          = "";
    }
    
    public SearchEntry( String guiString, String uri, String mappingFile, String regex )            
    {
        this.guiString      = guiString;
        this.uri            = uri;
        this.mappingFile    = mappingFile;
        this.regex          = regex;
    }

    public String getMappingFile() 
    {
        return mappingFile;
    }

    public void setMappingFile(String mappingFile) 
    {
        this.mappingFile = mappingFile;
    }

    public String getGuiString() 
    {
        return guiString;
    }

    public void setGuiString(String guiString) 
    {
        this.guiString = guiString;
    }

    public void setMappingFlag( String str ) 
    {
        if( str.compareToIgnoreCase( "true") == 0 )
        {
            mappingFlag = true;
        }    
        else
        {
            mappingFlag = false;
        }    
    }

    public void setURI(String uri) 
    {
        this.uri = uri;
    }

    public String getURI() 
    {
        return uri;
    }

    public boolean isMappingFlag() 
    {
        return mappingFlag;
    }

    public void setMappingFlag( boolean mappingFlag ) 
    {
        this.mappingFlag = mappingFlag;
    }

    public String getRegex() 
    {
        return regex;
    }

    public void setRegex(String regex) 
    {
        this.regex = regex;
    }
    
}
