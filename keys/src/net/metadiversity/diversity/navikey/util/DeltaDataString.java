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
 * RemoveDeltaComment.java
 *
 * Created on 10. März 2006, 06:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.metadiversity.diversity.navikey.util;

/**
 *
 * @author Dieter Neubacher
 */
public class DeltaDataString
{
    String data;
    String comment;
    String dataClean;
    
    /** Creates a new instance of DeltaDataString */
    public DeltaDataString( StringBuffer dataString, boolean removeComments, boolean removeStatesComments )
    {
        data = dataString.toString();
                
        dataClean = data;
        try
        {
            comment = "";
            
            int start, end, nextStart;
            int firstEvalPos = 0;
            int maxEvalPos   = Integer.MAX_VALUE;
            
            if( removeComments == false && removeStatesComments == false )
            {
                return;
            }
            if( ! removeComments )
            {
                // Don't remove comment from Description
                firstEvalPos = dataClean.indexOf( "/" );
            }
            if( ! removeStatesComments )
            {
                // Don't remove comment from Description
                maxEvalPos = dataClean.indexOf( "/" );
            }
            if( firstEvalPos < 0 ) firstEvalPos = 0;
            // Start search comment
            start = dataClean.indexOf( "<", firstEvalPos );
            end   = dataClean.indexOf( ">", firstEvalPos );
            nextStart = data.indexOf( "<", start + 1  );
            
            while( end > start && start > 0 && end > 0 && start < maxEvalPos )
            {
                StringBuffer str = new StringBuffer();
                
                if( nextStart > start && nextStart < end )
                {
                    start = nextStart;
                }
                str.append( dataClean.substring( 0, start ) );
                str.append( dataClean.substring( end + 1 ) );
                maxEvalPos -= end - start + 2;
                comment = comment + dataClean.substring( start, end );
                dataClean  = str.toString();
                
                start = dataClean.indexOf( "<" );
                end   = dataClean.indexOf( ">" );
                nextStart = dataClean.indexOf( "<", start + 1  );
            }
            
        }
        catch( java.lang.StringIndexOutOfBoundsException ex )
        {
            ex.printStackTrace();
        }
    }
    
    public String getString()
    {
        return dataClean;
    }
    public String getComment()
    {
        return comment;
    }
    public String getOriginalString()
    {
        return data;
    }
}
