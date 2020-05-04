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
import java.io.FileNotFoundException;

import java.net.URL;

import net.metadiversity.diversity.navikey.bo.Delta;
import net.metadiversity.diversity.navikey.bo.DeltaInterface;
import net.metadiversity.diversity.navikey.ui.NaviKey;

public class LocalDeltaConnector extends DeltaConnector
{
    Delta delta;
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public LocalDeltaConnector( NaviKey navikey )
    {
        delta = new Delta( navikey );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public LocalDeltaConnector( NaviKey navikey, String baseUrl, String specs, String chars, String items, String tImages )
    {
        this( navikey );
        System.out.println( "Use LocalDeltaConnector()" );
        try
        {
            BufferedReader specReader = null;
            BufferedReader charReader = null;
            BufferedReader itemReader = null;
            BufferedReader tImagesReader = null;
            
            System.out.println( "NaviKey: base URL: " + baseUrl );
            
            boolean removeCharsCommentFlag = navikey.isRemoveCharsCommentFlag();
            boolean removeItemsCommentFlag = navikey.isRemoveItemsCommentFlag();
            boolean removeSpecsCommentFlag = navikey.isRemoveSpecsCommentFlag();
            
            boolean removeCharsStatesCommentFlag = navikey.isRemoveCharsStatesCommentFlag();
            boolean removeItemsStatesCommentFlag = navikey.isRemoveItemsStatesCommentFlag();
            boolean removeSpecsStatesCommentFlag = navikey.isRemoveSpecsStatesCommentFlag();
            
            // DIDI: 2005.09.26
            // JAVA 1.4: can't load the file "file://..." 
            // ERROR sun.net.ftp.FtpLoginException: Not logged in
            
            boolean useUrlFlag = ! baseUrl.startsWith( "file" );                
            
            if( useUrlFlag )
            {
                try
                {
                    URL specUrl = new URL(baseUrl + specs);
                    System.out.println( "load specs from URL: " + specUrl );
                    specReader = new BufferedReader( new java.io.InputStreamReader( specUrl.openStream(), "UTF-8" ) );            
                }
                catch( FileNotFoundException e )
                {
                    System.out.println( e.getLocalizedMessage() );
                }
                
                try
                {
                    URL charUrl = new URL(baseUrl + chars);
                    System.out.println( "load chars from URL: " + charUrl );
                    charReader = new BufferedReader( new java.io.InputStreamReader( charUrl.openStream(), "UTF-8"  ) );            
                }
                catch( FileNotFoundException e )
                {
                    System.out.println( e.getLocalizedMessage() );
                }
                try
                {
                    URL itemUrl = new URL(baseUrl + items);
                    System.out.println( "load items from  URL: " + itemUrl );
                    itemReader = new BufferedReader( new java.io.InputStreamReader( itemUrl.openStream(), "UTF-8"  ) );            
                }
                catch( FileNotFoundException e )
                {
                    System.out.println( e.getLocalizedMessage() );
                }
                try
                {
                    if( tImages.trim().length() != 0 )
                    {
                        URL tImagesUrl = new URL(baseUrl + tImages );
                        System.out.println( "load timages from  URL: " + tImagesUrl );
                        tImagesReader = new BufferedReader( new java.io.InputStreamReader( tImagesUrl.openStream(), "UTF-8"  ) );            
                    }    
                }
                catch( FileNotFoundException e )
                {
                    System.out.println( e.getLocalizedMessage() );
                }
            }
            else
            {
                String filename;
                try
                {
                    filename = baseUrl + specs;
                    filename = filename.substring( filename.indexOf( ":/") + 2 );
                    System.out.println( "load specs from file: " + filename );

                    specReader = new BufferedReader( new java.io.InputStreamReader( new java.io.FileInputStream( filename ), "UTF-8"  ) );            
                }
                catch( FileNotFoundException e )
                {
                    System.out.println( e.getLocalizedMessage() );
                }
                try
                {
                    filename = baseUrl + chars;
                    filename = filename.substring( filename.indexOf( ":/") + 2 );
                    System.out.println( "load chars from file: " + filename );
                    charReader = new BufferedReader( new java.io.InputStreamReader( new java.io.FileInputStream( filename ), "UTF-8"  ) );            
                }
                catch( FileNotFoundException e )
                {
                    System.out.println( e.getLocalizedMessage() );
                }
                try
                {
                    filename = baseUrl + items;
                    filename = filename.substring( filename.indexOf( ":/") + 2 );
                    System.out.println( "load items from file: " + filename );
                    itemReader = new BufferedReader( new java.io.InputStreamReader( new java.io.FileInputStream( filename ), "UTF-8"  ) );            
                }
                catch( FileNotFoundException e )
                {
                    System.out.println( e.getLocalizedMessage() );
                }
                try
                {
                    if( tImages.trim().length() != 0 )
                    {
                        filename = baseUrl + tImages;
                        filename = filename.substring( filename.indexOf( ":/") + 2 );
                        System.out.println( "load timages from file: " + filename );
                        tImagesReader = new BufferedReader( new java.io.InputStreamReader( new java.io.FileInputStream( filename ), "UTF-8"  ) );            
                    }    
                }
                catch( FileNotFoundException e )
                {
                    System.out.println( e.getLocalizedMessage() );
                }
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
            
            delta.setData( dfp.loadDeltaData() );
        }
        catch(Exception e)
        {
            System.out.println( "Error dealing with files.\n" );
            e.printStackTrace();
        }
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public DeltaInterface getObject()
    {
        return delta;
    }
    
}
