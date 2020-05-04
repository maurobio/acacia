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

import java.io.FileInputStream;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.metadiversity.diversity.navikey.bo.BasicItem;
import net.metadiversity.diversity.navikey.bo.BasicItemCharacter;
import net.metadiversity.diversity.navikey.bo.BasicState;
import net.metadiversity.diversity.navikey.bo.Delta;
import net.metadiversity.diversity.navikey.bo.Resource;
import net.metadiversity.diversity.navikey.delta.DeltaFilePeer;

import net.metadiversity.diversity.navikey.ui.NaviKey;

/**
 * NaviKeyHttpServlet.java
 *
 * 8/5/98
 * @author Michael Bartley
 */

public class NaviKeyHttpServlet extends HttpServlet
{
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3257847688379643954L;

    PrintStream out;
    
    private static Delta delta;
    
    public static void setDelta(Delta d)
    {
        delta = d;
    }
    
    public void init(ServletConfig conf) throws ServletException
    {
        super.init(conf);
        System.out.println("Servlet init started");
        try
        {
            String propsFile = "NaviKeyServlet.props";
            Properties props = new Properties();
            props.load(new FileInputStream(propsFile));
            String navprops = props.getProperty("navikeyPropsFiles");
            
            boolean removeCharCommentFlag = false;  // ??? Must be read from PropsFile
            
            Properties p2 = new Properties();
            p2.load(new FileInputStream(navprops));
                        
            BufferedReader specsReader  = new BufferedReader( new java.io.FileReader( p2.getProperty( "specsfile") ) );            
            BufferedReader charsReader  = new BufferedReader( new java.io.FileReader( p2.getProperty( "charsfile") ) );            
            BufferedReader itemsReader  = new BufferedReader( new java.io.FileReader( p2.getProperty( "itemsfile") ) );            
/* ?????            
            Delta.setPersistentPeer( new DeltaFilePeer( specsReader, charsReader, itemsReader, removeCharCommentFlag ) );
            
            Resource.defaultLocation = p2.getProperty("webpage");
            String imageloc = p2.getProperty("imageDirectory");
            Resource.defaultLocation = urlconcat(Resource.defaultLocation, imageloc);
            
            delta = new Delta( null );  // ??? NaviKey ??? Options
            delta.restore();
 */
        }
        catch(Exception e)
        {
            System.out.println("Unable to get going" + e);
            System.exit(0);
        }
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void service(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException
    {
        res.setContentType("text/html");
        out = new PrintStream(res.getOutputStream());
        
        String s = req.getParameter("method");
        Vector v = new Vector();
        System.out.println(s);
        System.out.println(req.getParameter("name"));
        
        if(s == null)
        {
            System.out.println("Big bad error, no method passed:  " + req.getQueryString());
        }
        else if(s.equals("getItemCharacterList"))
            v = getItemCharacterList(req);
        else if(s.equals("getStateList"))
            v = getStateList(req);
        else if(s.equals("getItemList"))
            v = getItemList(req);
        else if(s.equals("getBasicItemCharacter"))
            v = getBasicItemCharacter(req);
        else if(s.equals("getItemName"))
            v = getItemName(req);
        else if(s.equals("getItemDescription"))
            v = getItemDescription(req);
        else if(s.equals("getItemResources"))
            v = getItemResources(req);
        
        if(v == null)
        {
            System.out.println("error while on " + s);
        }
        else
        {
            for(int i=0; i<v.size(); i++)
            {
                out.println((String)v.elementAt(i));
                //System.out.println((String)v.elementAt(i));
            }
        }
    }
    
    /**
     * Takes the current request, processes it via delta object, and returns a
     * vector of strings ready to be processed by client
     */
    private Vector getItemCharacterList(HttpServletRequest req)throws ServletException, IOException
    {
        String name;
        String charId;
        Vector<BasicState> v = new Vector<BasicState>();
        for(int i=0; ; i++)
        {
            name = req.getParameter("name"+i);
            if(name == null)
                break;
            charId = req.getParameter("characterId"+i);
            BasicState bs = new BasicState();
            bs.setName(name);
            try
            {
                bs.setCharacterId(new Integer(charId));
            }
            catch(Exception e)
            {
                System.out.println("Bad string passed for id.  "+e);
            }
            v.addElement(bs);
        }
        String smarts = req.getParameter( "smarts" );
        boolean intelligent     = ( smarts.equals( "true" ) ? true : false );
        String dep              = req.getParameter( "dependencies" );
        boolean dependencies    = ( dep.equals( "true" )  ? true : false );
        Vector icl = delta.getItemCharacterList(v, intelligent, dependencies );
        Vector<String> rv = new Vector<String>();
        for(int i=0; i<icl.size(); i++)
        {
            BasicItemCharacter bic = (BasicItemCharacter)icl.elementAt(i);
            rv.addElement(bic.getId().toString());
            rv.addElement(bic.getType());
            rv.addElement(bic.getName());
        }
        return rv;
    }
    /**
     * Takes the current request, processes it via delta object, and returns a
     * vector of strings ready to be processed by client
     */
    private Vector getStateList(HttpServletRequest req)throws ServletException, IOException
    {
        String name;
        String charId;
        Vector<BasicState> v = new Vector<BasicState>();
        for(int i=0; ; i++)
        {
            name = req.getParameter("name"+i);
            if(name == null)
                break;
            charId = req.getParameter("characterId"+i);
            BasicState bs = new BasicState();
            bs.setName(name);
            try
            {
                bs.setCharacterId(new Integer(charId));
            }
            catch(Exception e)
            {
                System.out.println("Bad string passed for id.  "+e);
            }
            v.addElement(bs);
        }
        String smarts = req.getParameter("smarts");
        boolean intelligent = (smarts.equals("true") ? true : false);
        Vector sl = delta.getStateList(v,
        new Integer(req.getParameter
        ("forCharacterId")),
        intelligent);
        Vector<String> rv = new Vector<String>();
        for(int i=0; i<sl.size(); i++)
        {
            BasicState bs = (BasicState)sl.elementAt(i);
            rv.addElement(bs.getCharacterId().toString());
            rv.addElement(bs.getName());
        }
        return rv;
    }
    
    /**
     * Takes the current request, processes it via delta object, and returns a
     * vector of strings ready to be processed by client
     */
    private Vector getItemList(HttpServletRequest req)throws ServletException, IOException
    {
        String name;
        String charId;
        Vector<BasicState> v = new Vector<BasicState>();
        for(int i=0; ; i++)
        {
            name = req.getParameter("name"+i);
            if(name == null)
                break;
            charId = req.getParameter("characterId"+i);
            BasicState bs = new BasicState();
            bs.setName(name);
            try
            {
                bs.setCharacterId(new Integer(charId));
            }
            catch(Exception e)
            {
                System.out.println("Bad string passed for id.  "+e);
            }
            v.addElement(bs);
        }
        Vector il = delta.getItemList(v);
        Vector<String> rv = new Vector<String>();
        for(int i=0; i<il.size(); i++)
        {
            BasicItem bi = (BasicItem)il.elementAt(i);
            rv.addElement(bi.getId().toString());
            rv.addElement(bi.getName());
        }
        return rv;
    }
    
    /**
     * Takes the current request, processes it via delta object, and returns a
     * vector of strings ready to be processed by client
     */
    private Vector getBasicItemCharacter(HttpServletRequest req)throws ServletException, IOException
    {
        String charId;
        charId = req.getParameter("forCharacterId");
        BasicItemCharacter bic = null;
        try
        {
            bic = delta.getBasicItemCharacter(new Integer(charId));
        }
        catch(Exception e)
        {
            System.out.println("Unable to make id from input." + e);
        }
        Vector<String> v = new Vector<String>();
        v.addElement(bic.getId().toString());
        v.addElement(bic.getType());
        v.addElement(bic.getName());
        return v;
    }
    /**
     * Takes the current request, processes it via delta object, and returns a
     * vector of strings ready to be processed by client
     */
    private Vector getItemName(HttpServletRequest req)throws ServletException, IOException
    {
        String itemId;
        itemId = req.getParameter("forItemId");
        String itemName = null;
        itemName = delta.getItemTitle(new Integer(itemId));
        Vector<String> rv = new Vector<String>();
        rv.addElement(itemName);
        return rv;
    }
    /**
     * Takes the current request, processes it via delta object, and returns a
     * vector of strings ready to be processed by client
     */
    private Vector getItemDescription(HttpServletRequest req)throws ServletException, IOException
    {
        String itemId;
        itemId = req.getParameter("forItemId");
        Vector rv = null;
        rv = delta.getItemDescription(new Integer(itemId));
        return rv;
    }
    /**
     * Takes the current request, processes it via delta object, and returns a
     *  vector of strings ready to be processed by client
     */
    private Vector getItemResources(HttpServletRequest req)throws ServletException, IOException
    {
        String itemId;
        itemId = req.getParameter("forItemId");
        System.out.println("Running get item resources for item id " + itemId);
        Vector resources = null;
        resources = delta.getItemResources(new Integer(itemId));
        Vector<String> rv = new Vector<String>();
        for( int i = 0; i < resources.size(); i++)
        {
            Resource r = (Resource)resources.elementAt(i);
            String s = r.getMimeType();
            if(s == null)
                s = "none";
            rv.addElement(s);
            s = r.getLocation().toString();
            if(s == null)
                s = "none";
            rv.addElement(s);
            s = r.getFile();
            if(s == null)
                s = "none";
            rv.addElement(s);
            s = r.getNotes();
            if(s == null)
                s = "none";
            rv.addElement(s);
            s = r.getTitle();
            if(s == null)
                s = "none";
            rv.addElement(s);
        }
        return rv;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private String urlconcat(String urlstart, String urladd)
    {
        int length = urlstart.length();
        if(urlstart.charAt(length-1) == '/')
            return urlstart + urladd;
        else
            return urlstart + '/' + urladd;
    }
    
}
