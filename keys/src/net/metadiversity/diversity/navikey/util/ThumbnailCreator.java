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


import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author didi
 */
public class ThumbnailCreator 
{
    /*
     * Image output format:
     * 
     *      jpeg
     *      png
     *      bmp
     *      wbmp
     *      gif
     * 
     */      
    private static String IMAGE_FORMAT[] = { "jpeg", "png", "bmp", "wbmp", "gif" };

    private int outputImageFormatIndex = -1;

    private int thumbWidth  = 150;
    private int thumbHeight = 150;
    private java.net.URL url = null;
    private String outputFileName = null;
    private java.io.File outputFile = null;
    
    private int orginalImageWidth = 0;
    private int orginalImageHeight = 0;

    public ThumbnailCreator()
    {
        
    }

    public void setOutputFileName( String filename )
    {
        outputFileName = filename;
        outputFile = new java.io.File( outputFileName );
    }
    
    public void setURL( String url )
    {
        url = url.replaceAll( "\\s", "%20");
        
        try 
        {
            this.url = new java.net.URL( url );                        
        }
        catch (MalformedURLException ex) 
        {
            System.err.print( ex );
            System.exit( 2 );
        }
    }
    
    public void setSize( String width, String hight )
    {
        thumbWidth  = Integer.parseInt( width);
        thumbHeight = Integer.parseInt( hight);
    }
    
    public void setSize( int width, int hight )
    {
        thumbWidth  = width;
        thumbHeight = hight;
    }
        
    public void initFormat( String f )
    {       
        outputImageFormatIndex = -1;
        
        for( int i = 0; i < IMAGE_FORMAT.length; i++ )
        {
            if( f.trim().compareToIgnoreCase( IMAGE_FORMAT[ i ] ) == 0 )
            {
                outputImageFormatIndex = i;
                break;
            }
        }
        if( outputImageFormatIndex == -1 )
        {    
            System.err.println( "invalid image format: " + f );
            System.exit( 3 );
        }    
    }
    
    public String getOrginalImageSize()
    {
        return java.lang.Integer.toString( orginalImageWidth ) + " x " + java.lang.Integer.toString( orginalImageHeight );
    }

    private void test()               
    {
         // this.setURL( "http://btbax3.bio.uni-bayreuth.de/BSM/reduced/M0038/M-0038556_20020624-144900~0495cm_o.jpg" );
         this.setURL( "http://www.tropicallichens.net/showphoto.aspx?q=fullimage&id=3169&speciesname=Acantholichen pannarioides" );
         this.setURL( "http://www.nhm.uio.no/botanisk/lav/Photo_Gallery/Acarospora/awdawda-habitat_G=awdawdawd_C=unknown_I=3F.jpg" );
         this.setSize( "180", "200" );
         this.initFormat( "png" );
         this.setOutputFileName( "/home/didi/output.png" );
        
         build();
    }
    
    public boolean build()
    {            
        try 
        {
            boolean TEST_FILE_EXIST = false;
            
            if( TEST_FILE_EXIST )
            {                
                if( outputFile.exists() )
                {
                    System.out.println( "Thumbnail exists for URL: " + url + " ... skip");
                    return false;
                }    
            }    
             
             
            BufferedImage image = null;
            // load image from URL    
            image = javax.imageio.ImageIO.read( url );
            //
            BufferedImage thumbImage = null;

            orginalImageWidth  = image.getWidth();
            orginalImageHeight = image.getHeight();

            double wf = (double) orginalImageWidth  / (double) thumbWidth;
            double hf = (double) orginalImageHeight / (double) thumbHeight;
            
            double resizeFactor = java.lang.Math.max( wf, hf );
                        
            thumbWidth  = (int) ( orginalImageWidth   / resizeFactor );
            thumbHeight = (int) ( orginalImageHeight  / resizeFactor );

            java.awt.Graphics2D g = null;
            // create output image
            thumbImage = new BufferedImage( thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB );
            
            g = thumbImage.createGraphics();
            g.setRenderingHint( java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR );
            // load data into image
            boolean test = g.drawImage( image, 0, 0, thumbWidth, thumbHeight, null );
            // System.out.println( "test:" + test );            
            
            javax.imageio.ImageIO.write( thumbImage, IMAGE_FORMAT[ outputImageFormatIndex ], outputFile );
        }
        catch( java.lang.NullPointerException ex )
        {
            Logger.getLogger(ThumbnailCreator.class.getName()).log(Level.SEVERE, null, ex);            
            return false;
        }    
        catch (IOException ex) 
        {
            Logger.getLogger(ThumbnailCreator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        ThumbnailCreator tc = new ThumbnailCreator();
        
        if( args.length == 2 )
        {
            tc.setURL( args[ 0 ] );
            tc.setOutputFileName( args[ 1 ] );
        }
        else
        if( args.length == 4 )
        {
            tc.setURL( args[ 0 ] );
            tc.setOutputFileName( args[ 1 ] );
            // determine thumbnail size from WIDTH and HEIGHT            
            tc.setSize( args[ 2 ], args[ 3 ] );            
        }
        if( args.length == 5 )
        {
            tc.initFormat( args[ 4 ] );
        }
        else
        {
            System.out.println( "ThumbnailCreator ImageURL outputFile [ThumbnailWidth ThumbnailHeight] { jpeg | png | bmp | wbmp | gif }" );
        }
    
        tc.test();
    }
}
