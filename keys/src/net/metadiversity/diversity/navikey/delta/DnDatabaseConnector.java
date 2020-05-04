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

import java.io.BufferedReader;

import net.metadiversity.diversity.navikey.bo.Delta;
import net.metadiversity.diversity.navikey.bo.DeltaInterface;

import net.metadiversity.diversity.navikey.ui.NaviKey;

public class DnDatabaseConnector extends DeltaConnector
{
    Delta delta;
    
    boolean removeCharsCommentFlag;
    boolean removeItemsCommentFlag;
    boolean removeSpecsCommentFlag;
    
    boolean removeCharsStatesCommentFlag;
    boolean removeItemsStatesCommentFlag;
    boolean removeSpecsStatesCommentFlag;
    
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public DnDatabaseConnector( NaviKey navikey)
    {
        delta = new Delta( navikey );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public DnDatabaseConnector( NaviKey navikey, String specString, String charString, String itemString, String tImagesString )
    {
        this( navikey );
        
        removeCharsCommentFlag = navikey.isRemoveCharsCommentFlag();
        removeItemsCommentFlag = navikey.isRemoveItemsCommentFlag();
        removeSpecsCommentFlag = navikey.isRemoveSpecsCommentFlag();

        removeCharsStatesCommentFlag = navikey.isRemoveCharsStatesCommentFlag();
        removeItemsStatesCommentFlag = navikey.isRemoveItemsStatesCommentFlag();
        removeSpecsStatesCommentFlag = navikey.isRemoveSpecsStatesCommentFlag();
        
        try
        {
            BufferedReader specReader = new BufferedReader( new java.io.StringReader( specString ) );
            BufferedReader charReader = new BufferedReader( new java.io.StringReader( charString ) );
            BufferedReader itemReader = new BufferedReader( new java.io.StringReader( itemString ) );
            
            BufferedReader tImagesReader = null;
                
            if( tImagesString != null)
            {    
                tImagesReader = new BufferedReader( new java.io.StringReader( tImagesString ) );
            }    
            
            DeltaFilePeer dfp = new DeltaFilePeer(  specReader, 
                                                    removeSpecsCommentFlag, 
                                                    removeSpecsStatesCommentFlag, 
                                                    charReader, 
                                                    removeCharsCommentFlag, 
                                                    removeCharsStatesCommentFlag, 
                                                    itemReader, 
                                                    removeItemsCommentFlag,
                                                    removeItemsStatesCommentFlag,
                                                    tImagesReader
                                                );
            // load and init DeltaData 
            delta.setData( dfp.loadDeltaData() );
        }
        catch( Exception ex )
        {
            System.out.println( "NaviKey: Error dealing with files.\n" );
            ex.printStackTrace();
        }
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public DeltaInterface getObject()
    {
        return delta;
    }
}
