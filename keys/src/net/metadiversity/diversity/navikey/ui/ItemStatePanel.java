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
package net.metadiversity.diversity.navikey.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.util.Vector;

import java.io.*;

import java.net.URISyntaxException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.logging.Level;
import java.util.logging.Logger;


// import net.metadiversity.diversity.navikey.bo.Resource.*;
import net.metadiversity.diversity.navikey.resource.ResourceButton;


/**
 *
 * ItemStatePanel.java
 *
 * @author Michael Bartley
 * 3/25/98
 * @author Dieter Neubacher
 */

class ItemStatePanel extends javax.swing.JPanel
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 4050480101810254640L;
    Vector<String> strings;
    Vector resources;
    Vector<String> listData;

    String searchString = null;
    
    javax.swing.JPanel  bp;                     // hold all buttons
    javax.swing.JPanel  resourceButtonPanel;
    
    javax.swing.JList   theList; // display all multi-state, numeric, and text attributes
    Vector<ResourceButton> resourceButtons;    
    javax.swing.JScrollPane jScrollPane;
    
    private boolean enableColorSelection = false;
    
    private final static int VISIBLE_ROWS = 15;    
    private final static int MAX_STRING_LENGTH = 80;
    
    public ItemStatePanel( Vector<String> strings, Vector resources )
    {
        super();
        setLayout( new BorderLayout() );
        listData = new Vector<String>();
        
        this.strings = strings;
        this.resources = resources;
        if( strings == null )
        {
            strings = new Vector<String>();
        }
        if( resources == null )
        {
            resources = new Vector();
        }
        resourceButtons = new Vector<ResourceButton>();
        
        theList = new JList( listData );
        theList.setVisibleRowCount( VISIBLE_ROWS );
        theList.setSelectionMode( javax.swing.ListSelectionModel.SINGLE_SELECTION );
        
        updateList();
        updateResources();
        
        jScrollPane = new javax.swing.JScrollPane();
        jScrollPane.setViewportView( theList );
        
        add( java.awt.BorderLayout.CENTER, jScrollPane );
        
        if( enableColorSelection )
        {
            setBackground( NaviKey.itemDisplayBGColor );
            setForeground( NaviKey.itemDisplayFGColor );
            
            theList.setBackground( NaviKey.itemDisplayColor );
            theList.setForeground( NaviKey.itemDisplayTextColor );            
        }
        setVisible( true );
        
        //-- Resource ---
        resourceButtonPanel = new JPanel( new FlowLayout() );
        for( int i=0; i<resourceButtons.size(); i++ )
        {
            resourceButtonPanel.add( resourceButtons.elementAt(i) );
        }

        bp = new JPanel( new BorderLayout() );
        bp.add( java.awt.BorderLayout.WEST, resourceButtonPanel );
        
        add( java.awt.BorderLayout.NORTH, bp );
        setVisible( true );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void updateList()
    {
        // DEEMY ???
        boolean removeCommentFlag = false;
        
        for( int i = 0; i < strings.size(); i++ )
        {
            String s = strings.elementAt(i);
            
            // DEEMY: Comment starts with "<" and ends with ">"            
            if( removeCommentFlag )
            {
                try
                {
                
                int start, end, nextStart;
                
                start = s.indexOf( "<" );
                end   = s.indexOf( ">" );
                nextStart = s.indexOf( "<", start + 1  );
                
                while( end > start && start > 0 && end > 0 )
                {
                    StringBuffer str = new StringBuffer();
                    
                    if( nextStart > start && nextStart < end )
                    {
                        start = nextStart;
                    }
                    str.append( s.substring( 0, start ) );
                    str.append( s.substring( end + 1 ) );
                    
                    s = str.toString();
                    start = s.indexOf( "<" );
                    end   = s.indexOf( ">" );
                    nextStart = s.indexOf( "<", start + 1  );
                }
                
                }
                catch( java.lang.StringIndexOutOfBoundsException ex )
                {
                    ex.printStackTrace();
                }
            }
            
/*  Format Output Lines with "MAX_STRING_LENGTH"          
//            boolean first = true;            
            while( s.length() > MAX_STRING_LENGTH )
            {
                String addition = s.substring( 0, MAX_STRING_LENGTH );
                int cut = addition.lastIndexOf( " " );
                if( cut < 0 )
                {    
                    cut = MAX_STRING_LENGTH;
                }
                addition = addition.substring( 0, cut );
                listData.add( addition );
                s = "          " + s.substring( cut );
            }
 */
            listData.add( s );
        }
        theList.setListData( listData );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void updateResources()
    {
        if( resources != null )
        {
            for( int i=0; i<resources.size(); i++ )
            {
                resourceButtons.addElement( new ResourceButton( ( net.metadiversity.diversity.navikey.bo.Resource )resources.elementAt( i ) ) );
            }
        }
    }    
}
