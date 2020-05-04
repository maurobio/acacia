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
package net.metadiversity.diversity.navikey.delta;
/*
 *
 */

//import java.io.BufferedReader;

import java.net.URL;

import net.metadiversity.diversity.navikey.bo.Delta;
import net.metadiversity.diversity.navikey.bo.DeltaInterface;

import net.metadiversity.diversity.navikey.ui.NaviKey;

public class LocalDeltaDatabaseConnector extends DeltaConnector
{
    Delta delta;
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public LocalDeltaDatabaseConnector( NaviKey navikey )
    {
        delta = new Delta( navikey );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public LocalDeltaDatabaseConnector( NaviKey navikey, URL dbUrl )
    {
        this( navikey );
/*        
        try
        {

            URL specUrl = new URL(baseUrl + specs);
            BufferedReader specReader = new BufferedReader( new java.io.InputStreamReader( specUrl.openStream() ) );            
            URL charUrl = new URL(baseUrl + chars);
            BufferedReader charReader = new BufferedReader( new java.io.InputStreamReader( charUrl.openStream() ) );            
            URL itemUrl = new URL(baseUrl + items);
            BufferedReader itemReader = new BufferedReader( new java.io.InputStreamReader( itemUrl.openStream() ) );            
            
            DeltaFilePeer dfp = new DeltaFilePeer( specReader, charReader, itemReader, removeCharCommentFlag );
            // load and init DeltaData 
            delta.setData( dfp.loadDeltaData() );
        }
        catch(Exception e)
        {
            System.out.println( "NaviKey: Error dealing with files.\n" );
            e.printStackTrace();
        }
 **/
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public DeltaInterface getObject()
    {
        return delta;
    }
}
