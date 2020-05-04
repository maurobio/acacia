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
 * DeleteFormItemsFile.java
 *
 * Created on February 28, 2007, 11:12 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.metadiversity.diversity.navikey.util;

import java.io.IOException;

/**
 *
 * @author didi
 */
public class DeleteFormItemsFile 
{    
    static String itemStart = "#";
    /**
     * Creates a new instance of DeleteFormItemsFile
     */
    public DeleteFormItemsFile( String regex ) 
    {
        java.io.BufferedReader in  = new java.io.BufferedReader( new java.io.InputStreamReader( System.in ) );
        java.io.BufferedWriter out = new java.io.BufferedWriter( new java.io.OutputStreamWriter( System.out ) );
        
        boolean skipLines = false;
        
        try 
        {            
            String str = null;
            
            while( true )
            {
                str = in.readLine();
                
                if( str == null ) break;
                    
                if( str.startsWith( itemStart ) )
                {
                    // out.write( str );
                    
                    if( str.matches( regex ) )
                    {
                        skipLines = true;
                    } 
                    else
                    {
                        skipLines = false;
                    }
                }
                    
                if( ! skipLines ) 
                {
                    out.write( str );
                    out.newLine();
                }
            }
            out.flush();
            out.close();
        } 
        catch ( IOException ex ) 
        {
            ex.printStackTrace();
        }
        
    }
    
    public static void main( String args[] )
    {
        if( args.length == 0 || args.length > 1 )
        {
            System.err.println( "Usage: DeleteFromItemsFile regex" );
            System.exit( 0 );
        }
        // System.out.println( args[ 0 ] );
        new DeleteFormItemsFile( args[ 0 ] );
    }
}
