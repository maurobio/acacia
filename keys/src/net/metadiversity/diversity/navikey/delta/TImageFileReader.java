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
 * TImageFileReader.java
 * 
 * Created on 08.08.2007, 12:15:01
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.metadiversity.diversity.navikey.delta;

import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.rtf.*;
import javax.swing.text.html.*;
import java.io.*;

import net.metadiversity.diversity.navikey.util.ThumbnailCreator;

/**
 *
 * @author didi
 */
/*
 
*COMMENT ~ Taxon images.

*TAXON IMAGES
# \i{}Agrostis\i0{} <L.>/
     ag01.gif
          <@text x=96 y=650 w=800 h=-2 m
           t=\i{}Agrostis eriantha\i0{} var. \i{}eriantha\i0{}>
          <@item x=172 y=554 w=637 h=-1 c m>
          <@subject Habit - \i{}Agrostis eriantha\i0{}>
          <@sound peewee.wav>
     ag25.gif
          <@subject Floret>
          <@sound magpie.wav>

# \i{}Andropogon\i0{} <L.>/
     inf01.gif
          <@subject First>
          <@item x=163 y=449 w=337 h=-2 m
           t=(this is the item name with additional text)>
          <@text x=213 y=553 w=220 h=-2
           t=This is \b{}text\b0{} from a \lquote{}text\rquote{} overlay.>
          <@imagenotes x=284 y=655
           t=This is text from an \lquote\i{}image notes\i0\rquote{}overlay.>
     lig01.gif
          <@subject Second>
     nod01.gif
          <@subject Third and last>

*/

public class TImageFileReader 
{
    
    boolean BUILD_HTML_FILES = false;
    boolean FILENAME_TRANSLATE_BLANK_TO_UNTERSCORE = false;
    String  DIRECTORY         = null;  // HTML outout directory
    
    static int thumbnailWidth = 160;
    static int thumbnailHight = 160;
    
    
    class Annotation
    {
        int x;
        int y;
        int w;
        int h;
        
        boolean cFlag;
        boolean nFlag;
        boolean mFlag;
        
        String type;
        String text;
        String t;
        
        String str;
        
        public Annotation( String a )
        {
            /*
             *  \\\\        \
             * [^\\}\\\\]   [^}\]
             * 
             * "\\\\[^\\}\\\\]*\\{\\}"
             */
            
            java.util.regex.Pattern p = java.util.regex.Pattern.compile( "\\\\[^\\{\\\\]*" );
            java.util.regex.Matcher m = p.matcher( a );
            while( m.find() )
            {
                System.out.println( "REGEX: " + m.group() );            
        
                
                int start = a.indexOf( "\\" );
                int end   = a.indexOf( "{}");
                
                System.out.println( a.substring( start, end) );
            }
            
            
            int start = 2;
            int end   = 0;
            
            str = new String( a );
            // type            
            end = str.indexOf( " ", start );
            type = str.substring( 2, end );
            // x=            
            start = str.indexOf( "x=" ) + 2;            
            if( start > 2 ) x = Integer.parseInt( str.substring( start, str.indexOf( " ", start) ) );
            // y=            
            start = str.indexOf( "y=" ) + 2;            
            if( start > 2 ) y = Integer.parseInt( str.substring( start, str.indexOf( " ", start) ) );
            // w=            
            start = str.indexOf( "w=" ) + 2;            
            if( start > 2 ) w = Integer.parseInt( str.substring( start, str.indexOf( " ", start) ) );
            // h=            
            start = str.indexOf( "h=" ) + 2;            
            if( start > 2 ) h = Integer.parseInt( str.substring( start, str.indexOf( " ", start) ) );
            // t=
            
            int endpos = start = str.indexOf( "t=" );            

            if( endpos <= 0 )
            {    
                endpos = str.length();
            }
            // Flags are before "t=";
            
            cFlag = ( str.substring( end, endpos ).indexOf( "c" ) > 0 );
            nFlag = ( str.substring( end, endpos ).indexOf( "n" ) > 0 );
            mFlag = ( str.substring( end, endpos ).indexOf( "m" ) > 0 );
            
            start += 2;
            
            if( start > 2 ) t = str.substring( start, str.length() -1 );
            
//            print();
        }
        
        void print()
        {
            System.out.println( "Type: " + type );
            System.out.println( "x:    " + x    );
            System.out.println( "y:    " + y    );
            System.out.println( "w:    " + w    );
            System.out.println( "h:    " + h    );
            System.out.println( "c:    " + cFlag );
            System.out.println( "n:    " + nFlag );
            System.out.println( "m:    " + mFlag );
            System.out.println( "t:    " + t     );            
        }
    };

    static int imagePadding = 10;
    static String htmlHeader =
        
    
        "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
        "<html xmlns=\"http://www.w3.org/1999/xhtml\" >" +
        "<head>" +
        "<title>NaviKey</title>" +
        "<META HTTP-EQUIV=\"CONTENT-TYPE\" CONTENT=\"text/html; charset=utf-8\">" +        
        "<style>" +
        "div.float { float: left; width: " + (thumbnailWidth + 2 * imagePadding) + "px; height: " + (thumbnailHight + 2 * imagePadding + 40 ) + "px; border: 1px; padding: " + imagePadding + "px; padding-bottom: 0px;}" + 
        "div.float p { text-align: center; padding-top: 0px; } " +
        "div.container { border: 2px background-color: #ffe; }" +
        "div.spacer { clear: both; } " +
        "div.header { text-align: center; font-size: x-large } " +
        "</style>" +       
        "</head><body>";
    
    
    static String htmlFooter = 
        
        "<div class=\"spacer\">  &nbsp;</div></div></body></html>";  
    
    java.util.HashMap<String, String> itemImages = null;
    
    public TImageFileReader()             
    {
        itemImages = new java.util.HashMap<String, String>();
    }
    public java.util.HashMap<String, String> getItemImageHashMap()
    {
        return itemImages;
    }
    /**
     * Read in the file store the item lines a  Vector of Vectors.
     * After that, parse these vectors of strings into Item objects
     */
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void read( java.lang.String fileName )
    {
        if( fileName.trim().length() == 0 )
        {
            return;
        }    
        java.io.BufferedReader bufferdReader = null;
        try
        { 
            java.lang.String coding = "UTF-8";
            System.out.println( "TimageFileReader(), read UTF-8 file: " + fileName );
            // bufferdReader = new java.io.BufferedReader( new java.io.FileReader( fileName ) );
            bufferdReader = new java.io.BufferedReader( new java.io.InputStreamReader( new java.io.FileInputStream( fileName ), coding ) );
            
            if( bufferdReader == null )
            {
                System.out.println( "TimageFileReader(), can't read UTF-8 file: " + fileName );
                return;
            }
            
            read( bufferdReader );
            bufferdReader.close();
        }
        catch( java.io.IOException ex )
        {
            System.out.println( fileName + " not found" );
            return;
        }        
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void read( java.io.BufferedReader timagesReader )
    {
        try
        { 
            StringBuffer block  = null;
            StringBuffer header = null;
            
            if( timagesReader == null )
            {
                return;
            }    
            while( timagesReader.ready() )
            {       
                boolean newDefinitionBlock = false;
            
                String line = timagesReader.readLine().trim();
                
                if( line.length() == 0 )    continue; 
                if( line.startsWith( "*") ) continue; // comment
                
                if( line.startsWith( "#") )
                {               
                    if( header != null )
                    {
                        evalBlock( header.toString(), block.toString() );                        
                    }    
                    header = new StringBuffer();
                    header.append( line );
                    /*
                    System.out.println( block );                    
                    System.out.println( getHtmlString(  block.toString() ) );
                    */                                                                                
                    block = new StringBuffer();
                    newDefinitionBlock = true;
                }
                else
                {
                    block.append( " " + line );                        
                }
            }
            timagesReader.close();
        }
        catch( java.io.IOException ex )
        {
            System.out.println( ex );
            return;
        }                
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    void evalBlock( java.lang.String header, java.lang.String data )
    {
            int     angleBracketLevel = 0;
            boolean angleBracketFlag  = false;
            BufferedWriter bw = null;
            
            boolean debug = true;

            int version = 2;
                    
            StringBuffer html = new StringBuffer();
            
            
            // get Item Number / Name
            // #t1. f11 a11 f12 a12 ...f1j a1j ...
            // #n1/ f11 a11 f12 a12 ...f1j a1j ...
            
            // System.out.println( "Header: " + header );

            String h = removeFormatAndComment( header );
            if( h.endsWith( "/" ) )
            {
                // Item Name
            }    
            if( h.endsWith( "." ) )
            {
                // Item Number
            }    
            // Remove first "#" and last "/" or "."
            h = h.substring( 1, h.length() -1 ).trim();

            if( debug ) System.out.println( "Header: " + h );
            
            // header
            
            
            if( BUILD_HTML_FILES )
            {    
                html.append( htmlHeader );
                if( version == 2 )
                {    
                    html.append( "<div class=\"header\" ><i>" + h + "</i></div>" );    
                    html.append( "<div class=\"container\"><div class=\"spacer\">&nbsp;</div>" );
                }    
            }    
                                                                                   
            java.util.StringTokenizer st = new java.util.StringTokenizer( data, "<>", true );
                
                StringBuffer t = new StringBuffer();
                int imageIndex = 0;
                while( st.hasMoreElements() )
                {
                    String token = st.nextToken();
                    if( token.compareTo( "<") == 0 )
                    {
                        angleBracketLevel++;
                        angleBracketFlag = true;
                    }
                    if( token.compareTo( ">") == 0 )
                    {
                        angleBracketLevel--;
                    }
                    
                    if( angleBracketFlag )
                    {
                        t.append( token );    
                    }
                    else
                    {
                        // Image
                        String image = token.trim();
                        if( image != null && image.length() > 0 )
                        {
                            if( debug ) System.out.println( "Image: " + image );
                            
                            itemImages.put( h, image );

                            if( BUILD_HTML_FILES )
                            {    
                                if( version == 1)
                                {
                                   html.append( "<div><img src=\"../images/" + image + "\" alt=\"" + image + "\" ></div>" );                                                            
                                }                                
                                if( version == 2)
                                {                        
                                    // Build Thumbnail for the image
                            
                                    ThumbnailCreator tc = new ThumbnailCreator();
                                    String thumbnailFilename = h + " " + imageIndex++ + ".jpeg";

                                    tc.initFormat( "jpeg");
                                    tc.setSize( thumbnailWidth, thumbnailHight );
                                    tc.setURL( image );
                                    tc.setOutputFileName( DIRECTORY + "/thumbnail/" + thumbnailFilename );
                                    if( tc.build() == true )
                                    {
                                        // find protocoll ://
                                        String server    = "";
                                        int pp = image.indexOf( "://" );

                                        if( pp > 0 )
                                        {
                                            pp += 3;
                                            int ppp = image.indexOf( '/', pp );
                                            if( ppp > 0)
                                            {
                                                server = image.substring( pp, ppp );
                                            }
                                        }                                
                                        String imageSize = tc.getOrginalImageSize();    

                                        html.append( "<div class=\"float\">\n" +
                                                         "<div  align=\"center\" >" +
                                                         "<a href=\"" + image + "\" >" +
                                                         "<img src=\"../thumbnail/" + thumbnailFilename + "\" alt=\"" + h + "\" >" +
                                                         "</a>" +
                                                         "<p>" + server + 
                                                         "<br>" + imageSize +
                                                         "</p>" +
                                                         "</div>\n" +
                                                         "</div>\n"
                                                   );                            
                                    }
                                }
                            }                            
                        }
                    }
                    if( angleBracketLevel == 0 && angleBracketFlag )
                    {
                        // annotations "<@ ... >"
                        if( debug ) System.out.println( "Annotation: " + t );
                       
                        Annotation a = new Annotation( t.toString() );
                        
                        if( a.type.compareTo( "text" ) == 0 || a.type.compareTo( "imagenotes" ) == 0 )
                        {
             
                            if( BUILD_HTML_FILES )
                            {    
                                if( version == 1)
                                {                           
                                    html.append( "<div style=\"position:absolute; top:" + a.y + "px; left:" + a.x + "px; width:280px; height:280px;\">" );

                                    html.append( "<div>" );
                                    html.append( getHtmlString( a.t ) + "</div>" );                            
                                }    
                            }
                        }
                        angleBracketFlag = false;
                        t = new StringBuffer();
                    }
//                    System.out.println( st.nextToken() );
                }
                
            if( BUILD_HTML_FILES )
            {    
                html.append( htmlFooter );
            }    
                    
            // Write HTML-file    
                        
            if( BUILD_HTML_FILES )
            {
                try 
                {
                    String filename = DIRECTORY + "/html/" + getHtmlString( h );
                    if( FILENAME_TRANSLATE_BLANK_TO_UNTERSCORE )
                    {    
                        filename = filename.replace( ' ', '_' )  + ".html";
                    }
                    else
                    {    
                        filename = filename  + ".html";
                    }
                        
                    bw = new java.io.BufferedWriter(new java.io.FileWriter( filename));            
                    bw.write( html.toString() );
                    bw.close();                
                } 
                catch( IOException ex ) 
                {
                    Logger.getLogger("global").log(Level.SEVERE, null, ex);
                }
            }
            
            
                    
                    // @item
                    // @text
                    // @imagenotes
                    // @subject
                    // @sound
            
    }

    void test()
    {
        try 
        {
            java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(".html"));
            bw.write("jnj");
            bw.close();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        }
    }
    /*
     * 
     * italics         \i{}Agrostis\i0{}
     * bold            \b{}Habit\b0{}  
     * subscript       \sub{}4\nosupersub{}
     *
     * Left quote      \lquote
     * Right quote     \rquote
     * 
     * New paragraph    
     */
/*    
Centring

If parameter ‘m’ is specified, text will be centred in its box. Otherwise, it will begin in the upper-left hand corner of the box.
Including comments

If parameter ‘c’ is specified, comments within text obtained from the main data (for example, feature descriptions and taxon names) are included; otherwise they are omitted.
*/    
    
    String getHtmlString( String str)
    {   
        if( str == null ) return null;
                
        // * italics         \i{}Agrostis\i0{}
        
        str = str.replaceAll( "\\\\i\\{\\}", "<i>" );
        str = str.replaceAll( "\\\\i0\\{\\}", "</i>" );       

        // * bold            \b{}Habit\b0{}  
        
        str = str.replaceAll( "\\\\b\\{\\}", "<u>" );
        str = str.replaceAll( "\\\\b0\\{\\}", "</u>" );       

        // * Left quote      \lquote
        
        str = str.replaceAll( "\\\\lquote\\{\\}", "`" );
        
        // * Right quote     \rquote
        
        str = str.replaceAll( "\\\\rquote\\{\\}", "´" );
        
        //   Subscript       \sub{}4\nosupersub{}

        str = str.replaceAll( "\\\\sub\\{\\}", "<sub>" );
        str = str.replaceAll( "\\\\nosupersub\\{\\}", "</sub>" );

        return str;
    }

    public static String removeFormatAndComment( String str)
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
        
        return str.trim();        
    }
    
    // ------------------------------------------------------
    // --------------------------------------------------------------------------    
    public static void main( String args[] )
    {
        TImageFileReader tifr = new TImageFileReader();

/*        
        if( args.length < 2 )
        {
            System.out.println( "usage:");
            System.out.println( "MergeTimageFiles file [[file]...]");
        }        
*/        
        tifr.DIRECTORY        = "html";  // HTML outout directory
        tifr.BUILD_HTML_FILES = true;
        tifr.FILENAME_TRANSLATE_BLANK_TO_UNTERSCORE = false;

        System.out.println( "reading TImage file..." );
        tifr.DIRECTORY         = "AllImages";  // HTML outout directory
        tifr.read( "/home/didi/timagesWithoutLuciana-2008-04-25-part-2" );  
        System.out.println( "done." );
        // tifr.print();

        System.exit( 0 );
        
// Version 1        
/*        
        System.out.println( "reading TImage file..." );
        tifr.BUILD_HTML_FILES  = true;
        tifr.DIRECTORY         = "html";  // HTML outout directory
        tifr.read( "/home/didi/timagesWithoutLuciana-2008-04-25" );
        System.out.println( "done." );
        // tifr.print();

        System.exit( 0 );
 */       
    }
}
