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

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Vector;

import net.metadiversity.diversity.navikey.bo.BasicItem;
import net.metadiversity.diversity.navikey.bo.BasicItemCharacter;
import net.metadiversity.diversity.navikey.bo.BasicState;
import net.metadiversity.diversity.navikey.bo.DeltaInterface;
import net.metadiversity.diversity.navikey.bo.Item;
import net.metadiversity.diversity.navikey.bo.Resource;
import net.metadiversity.diversity.navikey.resource.ImageResource;
/**
 *
 * HttpClientDelta.java
 * @author Michael Bartley
 * 7/31/98
 */

public class HttpClientDelta implements DeltaInterface
{
    URL url; //server to connect to
    
    static final String CHARACTER_ENCODING = "UTF-8";
    
    private String urlString;
    
    public HttpClientDelta( URL url )
    {
        this.url = url;
        urlString = url.toString() + '?';
    }
    
    public String getItemImage( String item )
    {
        return "not implemented";
    }        
    public void setUrl( URL url )
    {
        this.url = url;
        urlString = url.toString() + '?';
    }
    
    public URL getUrl()
    {
        return url;
    }
    
    //vector of BasicItemCharacters returned
    public Vector getItemCharacterList( Vector basicStates, boolean intelligent, boolean dependencies ) 
    {
        Vector<BasicItemCharacter> rv = new Vector<BasicItemCharacter>();
        try
        {
            String query = "method=" + URLEncoder.encode( "getItemCharacterList", CHARACTER_ENCODING );
            if( basicStates != null )
            {
                for( int i=0; i<basicStates.size(); i++ )
                {
                    query += "&";
                    BasicState bs = ( BasicState )basicStates.elementAt( i );
                    query += "name" + i + "=" + URLEncoder.encode( bs.getName(), CHARACTER_ENCODING );
                    query += "&";
                    query += "characterId" + i + "=" + URLEncoder.encode( bs.getCharacterId().toString(), CHARACTER_ENCODING );
                }
            }
            //pass in intelligence
            query += "&smarts=" + ( intelligent ? "true" : "false" );
            System.out.println( "Query is: " + query );
            int cl = query.length();
        
            URL currentUrl = new URL( urlString + query );
            URLConnection uc = currentUrl.openConnection();
            uc.setDoOutput( true );
            uc.setDoInput( true );
            uc.setUseCaches( false );
            uc.setAllowUserInteraction( false );
            BufferedReader bufferdReader = new BufferedReader( new java.io.InputStreamReader( uc.getInputStream() ) );            
            
            Vector<String> values = new Vector<String>();
            String nextLine;
            while( ( nextLine = bufferdReader.readLine() ) != null )
            {
                values.addElement( nextLine );
                //System.out.println( nextLine );
            }
            bufferdReader.close();
            //Now have vector values filled with id's, types, and strings ( I hope )
            // must process and then return
            if( values.size()%3 != 0 )
            {
                return null;
            }
            for( int i=0; i<values.size(); i += 3 )
            {
                BasicItemCharacter bic = new BasicItemCharacter();
                bic.setId( new Integer( values.elementAt(i) )  );
                bic.setType( values.elementAt(i + 1) );
                bic.setFeature( values.elementAt(i + 2) );
                rv.addElement( bic );
            }
        }
        catch( java.io.UnsupportedEncodingException ex )
        {
            System.out.println( "Error in communication. Get ItemCharacterList" + ex );            
            ex.printStackTrace();
        }
        catch( IOException ex )
        {
            System.out.println( "Error in communication. Get ItemCharacterList" + ex );
        }
        //System.out.println( "Done with getItemCharacterList" );
        return rv;
    }
    
    //vector of BasicStates returned
    public Vector getStateList( Vector basicStates, Object characterId, boolean intelligent )
    {
        Vector<BasicState> rv = new Vector<BasicState>();
        try
        {
            String query = "method=" + URLEncoder.encode( "getStateList", CHARACTER_ENCODING );
            query += "&";
            query += "forCharacterId=" + URLEncoder.encode( characterId.toString(), CHARACTER_ENCODING );
            for( int i=0; i<basicStates.size(); i++ )
            {
                query += "&";
                BasicState bs = ( BasicState )basicStates.elementAt( i );
                query += "name" +i+ "=" + URLEncoder.encode( bs.getName(), CHARACTER_ENCODING );
                query += "&";
                query += "characterId" +i+ "=" + URLEncoder.encode( bs.getCharacterId().toString(), CHARACTER_ENCODING );
            }
            //add intelligence info
            query += "&smarts=" + URLEncoder.encode( intelligent ? "true" : "false", CHARACTER_ENCODING );
            int cl = query.length();

            URL currentUrl = new URL( urlString + query );
            URLConnection uc = currentUrl.openConnection();
            uc.setDoOutput( true );
            uc.setDoInput( true );
            uc.setAllowUserInteraction( false );
            
            BufferedReader bufferdReader = new BufferedReader( new java.io.InputStreamReader( uc.getInputStream() ) );            
            Vector<String> values = new Vector<String>();
            String nextLine;
            while( ( nextLine = bufferdReader.readLine() ) != null )
            {
                values.addElement( nextLine );
                //System.out.println( nextLine );
            }
            bufferdReader.close();
            //Now have vector values filled with id's and strings ( I hope )
            // must process and then return
            if( values.size()%2 != 0 )
                return null;
            for( int i=0; i<values.size(); i += 2 )
            {
                BasicState bs = new BasicState();
                bs.setCharacterId( new Integer( values.elementAt(i) )  );
                bs.setName( values.elementAt(i + 1) );
                rv.addElement( bs );
            }
        }
        catch( java.io.UnsupportedEncodingException ex )
        {
            System.out.println( "Error in communication. Get ItemCharacterList" + ex );            
            ex.printStackTrace();
        }
        catch( Exception e )
        {
            System.out.println( "Error in communication. GetStateList"+e );
        }
        return rv;
    }
    
    // vector of BasicItems returned
    @SuppressWarnings("unchecked")
public Vector getItemList( Vector basicStates )
    {
        Vector rv = new Vector();
        try
        {
            String query = "method=" + URLEncoder.encode( "getItemList", CHARACTER_ENCODING );
            if( basicStates != null )
            {
                for( int i = 0; i < basicStates.size(); i++ )
                {
                    query += "&";
                    BasicState bs = ( BasicState )basicStates.elementAt( i );
                    query += "name" +i+ "=" + URLEncoder.encode( bs.getName(), CHARACTER_ENCODING );
                    query += "&";
                    query += "characterId" +i+ "=" + URLEncoder.encode( bs.getCharacterId().toString(), CHARACTER_ENCODING );
                }
            }
            int cl = query.length();

            URL currentUrl = new URL( urlString + query );
            URLConnection uc = currentUrl.openConnection();
            uc.setDoOutput( true );
            uc.setDoInput( true );
            uc.setAllowUserInteraction( false );
            
            BufferedReader bufferdReader = new BufferedReader( new java.io.InputStreamReader( uc.getInputStream() ) );            
            Vector<String> values = new Vector<String>();
            String nextLine;
            while( ( nextLine = bufferdReader.readLine() ) != null )
            {
                values.addElement( nextLine );
                //System.out.println( nextLine );
            }
            bufferdReader.close();
            //Now have vector values filled with id's and strings ( I hope )
            // must process and then return
            if( values.size()%2 != 0 )
                return null;
            for( int i=0; i<values.size(); i += 2 )
            {
                BasicItem bi = new BasicItem();
                bi.setId( new Integer( values.elementAt(i) )  );
                bi.setName( values.elementAt(i + 1) );
                rv.addElement( bi );
            }
        }
        catch( java.io.UnsupportedEncodingException ex )
        {
            System.out.println( "Error in communication. Get ItemCharacterList" + ex );            
            ex.printStackTrace();
        }
        catch( IOException e )
        {
            System.out.println( "Error in communication. GetItemList"+e );
        }
        return rv;
    }
    
    public BasicItemCharacter getBasicItemCharacter( Object itemCharacterId )
    {
        try
        {
            String query = "method=" + URLEncoder.encode( "getBasicItemCharacter", CHARACTER_ENCODING );
            query += "&";
            query += "forCharacterId=" + URLEncoder.encode( itemCharacterId.toString(), CHARACTER_ENCODING );

            int cl = query.length();
            URL currentUrl = new URL( urlString + query );
            URLConnection uc = currentUrl.openConnection();
            uc.setDoOutput( true );
            uc.setDoInput( true );
            uc.setAllowUserInteraction( false );
            
            BufferedReader bufferdReader = new BufferedReader( new java.io.InputStreamReader( uc.getInputStream() ) );            
            Vector<String> values = new Vector<String>();
            String nextLine;
            while( ( nextLine = bufferdReader.readLine() ) != null )
            {
                values.addElement( nextLine );
                //System.out.println( nextLine );
            }
            bufferdReader.close();
            //Now have vector values filled with id's and strings ( I hope )
            // must process and then return
            if( values.size() != 3 )
            {
                return null;
            }
            BasicItemCharacter bic = new BasicItemCharacter();
            bic.setId( new Integer( values.elementAt(0) )  );
            bic.setType( values.elementAt(1) );
            bic.setFeature( values.elementAt(2) );
            return bic;
        }
        catch( java.io.UnsupportedEncodingException ex )
        {
            System.out.println( "Error in communication. Get ItemCharacterList" + ex );            
            ex.printStackTrace();
        }
        catch( Exception e )
        {
            System.out.println( "Error in communication. "+e );
        }
        return null;
    }
    
    //for use in generating description list NOT USED, SHOULD BE REMOVED!!!
    public Item getItem( Object itemId )
    {
        return null;
    }
    
    public String getItemTitle( Object itemId )
    {
        try
        {
            String query = "method=" + URLEncoder.encode( "getItemTitle", CHARACTER_ENCODING );
            query += "&";
            query += "forItemId=" + URLEncoder.encode( itemId.toString(), CHARACTER_ENCODING );

            int cl = query.length();
            URL currentUrl = new URL( urlString + query );
            URLConnection uc = currentUrl.openConnection();
            uc.setDoOutput( true );
            uc.setDoInput( true );
            uc.setAllowUserInteraction( false );
            
            BufferedReader bufferdReader = new BufferedReader( new java.io.InputStreamReader( uc.getInputStream() ) );            
            Vector<String> values = new Vector<String>();
            String nextLine;
            while( ( nextLine = bufferdReader.readLine() ) != null )
            {
                values.addElement( nextLine );
                //System.out.println( nextLine );
            }
            bufferdReader.close();
            //Now have vector values filled with id's and strings ( I hope )
            // must process and then return
            if( values.size() < 1 )
            {
                return null;
            }
            return values.elementAt(0);
        }
        catch( java.io.UnsupportedEncodingException ex )
        {
            System.out.println( "Error in communication. Get ItemCharacterList" + ex );            
            ex.printStackTrace();
        }
        catch( Exception e )
        {
            System.out.println( "Error in communication. "+e );
        }
        return null;
    }
    
    public Vector< String > getItemDescription( Object itemId )
    {
        try
        {
            String query = "method=" + URLEncoder.encode( "getItemDescription", CHARACTER_ENCODING );
            query += "&";
            query += "forItemId=" + URLEncoder.encode( itemId.toString(), CHARACTER_ENCODING );

            int cl = query.length();
//            Vector rv = new Vector();
            URL currentUrl = new URL( urlString + query );
            URLConnection uc = currentUrl.openConnection();
            uc.setDoOutput( true );
            uc.setDoInput( true );
            uc.setAllowUserInteraction( false );
            
            BufferedReader bufferdReader = new BufferedReader( new java.io.InputStreamReader( uc.getInputStream() ) );            
            Vector<String> values = new Vector<String>();
            String nextLine;
            while( ( nextLine = bufferdReader.readLine() ) != null )
            {
                values.addElement( nextLine );
                //System.out.println( nextLine );
            }
            bufferdReader.close();
            //Now have vector values filled with id's and strings ( I hope )
            // must process and then return
            return values;
        }
        catch( java.io.UnsupportedEncodingException ex )
        {
            System.out.println( "Error in communication. Get ItemCharacterList" + ex );            
            ex.printStackTrace();
        }
        catch( Exception e )
        {
            System.out.println( "Error in communication. "+e );
        }
        return null;
    }
    
    public Vector getItemResources( Object itemId )
    {
        try
        {
            String query = "method=" + URLEncoder.encode( "getItemResources", CHARACTER_ENCODING );
            query += "&";
            query += "forItemId=" + URLEncoder.encode( itemId.toString(), CHARACTER_ENCODING );

            int cl = query.length();
            Vector<Resource> rv = new Vector<Resource>();
            URL currentUrl = new URL( urlString + query );
            URLConnection uc = currentUrl.openConnection();            
            uc.setDoOutput( true );
            uc.setDoInput( true );
            uc.setAllowUserInteraction( false );
            
            BufferedReader bufferdReader = new BufferedReader( new java.io.InputStreamReader( uc.getInputStream() ) );            
            Vector<String> values = new Vector<String>();
            String nextLine;
            while( ( nextLine = bufferdReader.readLine() ) != null )
            {
                values.addElement( nextLine );
                //System.out.println( nextLine );
            }
            bufferdReader.close();
            //Now have vector values filled with id's and strings ( I hope )
            // must process and then return
            if( values.size()%5 != 0 )
            {
                return null;
            }
            for( int i = 0; i < values.size(); i += 5 )
            {
                String mimeType = values.elementAt(i);
                if( mimeType.equals( "image" ) )
                {
                    Resource r = new ImageResource();
                    String s;
                    s = values.elementAt(i + 1);
                    if( !s.equals( "none" ) )
                        r.setLocation( new URL( s ) );
                    s = values.elementAt(i + 2);
                    if( !s.equals( "none" ) )
                        r.setFile( s );
                    s = values.elementAt(i + 3);
                    if( !s.equals( "none" ) )
                        r.setNotes( s );
                    s = values.elementAt(i + 4);
                    if( !s.equals( "none" ) )
                        r.setTitle( s );
                    rv.addElement( r );
                }
            }
            return rv;
        }
        catch( java.io.UnsupportedEncodingException ex )
        {
            System.out.println( "Error in communication. Get ItemCharacterList" + ex );            
            ex.printStackTrace();
        }
        catch( IOException e )
        {
            System.out.println( "Error in communication. GetResources"+e );
        }
        return null;
    }    
}
