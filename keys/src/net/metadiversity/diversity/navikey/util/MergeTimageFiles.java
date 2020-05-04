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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.metadiversity.diversity.navikey.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author didi
 */
public class MergeTimageFiles 
{
    java.util.TreeMap< String, java.util.List > timagedata = null; 
    

    private MergeTimageFiles()
    {
        init();
    }
    
    private final void init()
    {
        timagedata = new java.util.TreeMap< String, java.util.List >();
    }    
    
    private void add( String key, String data )
    {                
        if( key == null || data == null)
        {
            System.err.println( "Invalid data.");
            return;    
        }    
        
        data = data.trim();
        
        if( data.length() == 0 ) return;
        
        if( timagedata.containsKey( key ) )
        {
            timagedata.get( key ).add( data );
        }
        else
        {
            java.util.LinkedList< String > list = new java.util.LinkedList< String >();
            
            list.add( data );
            timagedata.put( key, list );
        }        
    }    
    
    
    private void test()
    {
        add( "A", "1" );
        add( "B", "1" );
        add( "C", "1" );
        add( "A", "2" );
        add( "A", "3" );
        add( "B", "2" );
    }
/*    
    private void sort()
    {
        java.util.Collections.sort( timagedata );
    }
 */ 
    private void print()
    {
        
        for( java.util.Iterator it = timagedata.keySet().iterator(); it.hasNext(); ) 
        {                        
            String key = (String) it.next();
            System.out.println( key );
            
            for( java.util.Iterator iit = timagedata.get( key ).iterator(); iit.hasNext();)
            {
                System.out.println( "    " + iit.next() );                
            }    
        }        
    }
    
    private void addFile( String file )
    {
        java.io.FileInputStream fis = null;
        try 
        {
            fis = new java.io.FileInputStream(file);
            java.io.InputStreamReader isr = new java.io.InputStreamReader(fis, "utf-8" );
            java.io.BufferedReader br = new java.io.BufferedReader(isr);

            String headerLine = null;
            String dataLine = null;

            while (br.ready()) 
            {
                dataLine = br.readLine();

                if (dataLine.startsWith("#")) 
                {
                    headerLine = removeFormatAndComment( dataLine );
                    int authorPos = headerLine.indexOf( '_');
                    
                    if( authorPos > 2 )
                    {
                        headerLine = headerLine.substring( 0, authorPos ) + "/";
                    }
                    continue;
                }
                else 
                {
                    add(headerLine, dataLine);
                }
            }
                br.close();

        }
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(MergeTimageFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) 
        {
            Logger.getLogger(MergeTimageFiles.class.getName()).log(Level.SEVERE, null, ex);
        }        
        finally 
        {
            try 
            {
                fis.close();
            }
            catch (IOException ex) 
            {
                Logger.getLogger(MergeTimageFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }    
    
    
    private String removeFormatAndComment( String str)
    {
        // Remove comment
        str = str.replaceAll( "<.*>", "" );
        // * italics         \i{}Agrostis\i0{}
        
        str = str.replaceAll( "\\\\i\\{\\}", "" );
        str = str.replaceAll( "\\\\i0\\{\\}", "" );       

        // * bold            \b{}Habit\b0{}  
        
        str = str.replaceAll( "\\\\b\\{\\}", "" );
        str = str.replaceAll( "\\\\b0\\{\\}", "" );       

        // * Left quote      \lquote
        
        str = str.replaceAll( "\\\\lquote\\{\\}", "'" );
        
        // * Right quote     \rquote
        
        str = str.replaceAll( "\\\\rquote\\{\\}", "'" );

        //   Subscript       \sub{}4\nosupersub{}

        str = str.replaceAll( "\\\\sub\\{\\}", "" );
        str = str.replaceAll( "\\\\nosupersub\\{\\}", "" );
        
        return str;        
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        // TODO code application logic here
        
        MergeTimageFiles mtf = new MergeTimageFiles(); 
        
        if( args.length < 2 )
        {
            System.out.println( "usage:");
            System.out.println( "MergeTimageFiles file [[file]...]");
  /*
        mtf.test();
        mtf.print();
  */      
        
            mtf.addFile( "/home/didi/NetBeans-6-0-Projects/DiversityNavigator/timages_Iceland.txt" );
            mtf.addFile( "/home/didi/NetBeans-6-0-Projects/DiversityNavigator/timages_Italic.txt" );
            mtf.addFile( "/home/didi/NetBeans-6-0-Projects/DiversityNavigator/timages_LIASLight_Oslo-new.txt" );
            mtf.addFile( "/home/didi/NetBeans-6-0-Projects/DiversityNavigator/timages_LIASLight_UK_070801.txt" );
//            mtf.addFile( "/home/didi/NetBeans-6-0-Projects/DiversityNavigator/timages_Luciana.txt" );
            mtf.addFile( "/home/didi/NetBeans-6-0-Projects/DiversityNavigator/timages_TropicLichens.txt" );
            mtf.addFile( "/home/didi/NetBeans-6-0-Projects/DiversityNavigator/timages_www_stridvall_se.txt" );

            mtf.print();
        
            System.exit( 1 );
        }
        
        
        
        for( int i = 1; i < args.length; i++ )
        {
            System.out.println( "Merge file: " + args[ i ]);            
            mtf.addFile( args[ i ] );
        }    
        mtf.print();
        
        System.exit( 1 );
    }    
}
