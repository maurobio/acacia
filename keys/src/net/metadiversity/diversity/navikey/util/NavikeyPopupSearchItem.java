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
 * NavikeyPopupSearchItem.java
 * 
 * Created on 08.11.2007, 17:00:07
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.metadiversity.diversity.navikey.util;

/**
 *
 * @author didi
 */
public class NavikeyPopupSearchItem extends javax.swing.JMenuItem        
{
    String  guiText;
    String  uri;
    boolean mappingFlag;
    String  regex;

    public String getRegex() 
    {
        return regex;
    }

    public void setRegex(String regex) 
    {
        this.regex = regex;
    }

    public boolean isMappingFlag() 
    {
        return mappingFlag;
    }

    public void setMappingFlag(boolean mappingFlag) 
    {
        this.mappingFlag = mappingFlag;
    }

    public String getGuiText() 
    {
        return guiText;
    }

    public void setGuiText(String guiText) 
    {
        this.guiText = guiText;
    }

    public String getURI() 
    {
        return uri;
    }

    public void setURI(String uri) 
    {
        this.uri = uri;
    }
    
    public NavikeyPopupSearchItem( String guiText, String uri, boolean mappingFlag, String regex ) 
    {
        super();        
        this.guiText = guiText;
        this.uri  = uri;
        this.mappingFlag = mappingFlag;
        this.regex = regex;
        super.setText( guiText );
    }
}
