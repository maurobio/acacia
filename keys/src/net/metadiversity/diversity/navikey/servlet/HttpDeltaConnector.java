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
package net.metadiversity.diversity.navikey.servlet;
/*
 * @author Michael Bartley
 */

import java.net.URL;

import net.metadiversity.diversity.navikey.bo.DeltaInterface;
import net.metadiversity.diversity.navikey.delta.DeltaConnector;

public class HttpDeltaConnector extends DeltaConnector
{
    private URL urlBase;
    
    public HttpDeltaConnector(String url)
    {
        System.out.println("Want to connect to: " + url);
        urlBase = null;
        try
        {
            //urlBase = new URL("http://dev.huh.harvard.edu:8765/servlet/NaviKeyHttpServlet");
            urlBase = new URL(url);
        }
        catch(Exception e)
        {
            System.out.println("Unable to connect to servlet" + e);
        }
    }
    
    public DeltaInterface getObject()
    {
    	System.out.println( urlBase );
        return new HttpClientDelta(urlBase);
    }
    
}
