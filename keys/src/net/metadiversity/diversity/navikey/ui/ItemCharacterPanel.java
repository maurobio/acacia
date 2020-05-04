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
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.*;
import java.io.*;
import java.net.URISyntaxException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.metadiversity.diversity.navikey.bo.BasicItemCharacter;

/**
 * ItemCharacterPanel.java
 *
 * @author Michael Bartley
 * 3/25/98
 * @author Dieter Neubacher
 */

class ItemCharacterPanel extends javax.swing.JPanel
{
    String searchString = null;
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3257007657100653361L;
    Vector basicItemCharacters;
    javax.swing.JList theList;
    javax.swing.JScrollPane jScrollPane;
    
    Vector<String> listData;
    
    private final static int VISIBLE_ROWS = 15;
    private boolean enableColorSelection = false;
    
    public ItemCharacterPanel()
    {
        super();
        setLayout( new BorderLayout() );

        JLabel title = new JLabel( "Characters available" );
        add( java.awt.BorderLayout.NORTH, title );
        
        basicItemCharacters = null;
        
        listData = new java.util.Vector<String>();
        
        theList = new JList( listData );
        theList.setVisibleRowCount( VISIBLE_ROWS );
        theList.setSelectionMode( javax.swing.ListSelectionModel.SINGLE_SELECTION );
        
        jScrollPane = new javax.swing.JScrollPane();
        jScrollPane.setViewportView( theList );
        
        add( java.awt.BorderLayout.CENTER, jScrollPane );
        
        if( enableColorSelection )
        {
            theList.setBackground( NaviKey.naviKeyBackgroundColor );
            theList.setForeground( NaviKey.itemCharacterColor );            
        }
    }
        
/*******************
        theList.addMouseListener(new java.awt.event.MouseAdapter() 
        {
            public void mouseClicked(java.awt.event.MouseEvent evt) 
            {
                if( evt.getButton() != java.awt.event.MouseEvent.BUTTON1 )
                {
                    String selection = (String) theList.getModel().getElementAt( theList.locationToIndex( theList.getMousePosition() ) );

                    System.out.println( "lList1MouseClicked" + evt );
                    System.out.println( "Point " + evt.getPoint() );
                    System.out.println( "Mouse Position" + theList.getMousePosition() );
                    System.out.println( "Index " + theList.locationToIndex( theList.getMousePosition() ) );
                    System.out.println(  selection );

                    searchString = selection;

                    // Popup Menu
                    javax.swing.JPopupMenu popup;

                    //Create the popup menu.
                    popup = new javax.swing.JPopupMenu();
                    javax.swing.JMenuItem pmi0 = new javax.swing.JMenuItem( "ItemCharacterPanel"  );
                    javax.swing.JMenuItem pmi1 = new javax.swing.JMenuItem( "LIAS Search "  );
                    javax.swing.JMenuItem pmi2 = new javax.swing.JMenuItem( "Google Search" );
                    javax.swing.JMenuItem pmi3 = new javax.swing.JMenuItem( "Google Maps "  );
                    javax.swing.JMenuItem pmi4 = new javax.swing.JMenuItem( "Show Image"  );

                    MouseListener popupListener = new PopupListener();    

                    pmi0.addMouseListener( popupListener );
                    pmi1.addMouseListener( popupListener );
                    pmi2.addMouseListener( popupListener );
                    pmi3.addMouseListener( popupListener );
                    pmi4.addMouseListener( popupListener );

                    popup.add( pmi0 );
                    popup.add( pmi1 );
                    popup.add( pmi2 );
                    popup.add( pmi3 );
                    popup.add( pmi4 );
                    
                    // menuItem.addActionListener(this);
                    popup.show(evt.getComponent(), evt.getX(), evt.getY() );
                }
            }
        });
    }
        
        public class PopupListener extends MouseAdapter 
        {
            public void mousePressed(MouseEvent e) 
            {
                System.out.println( ((javax.swing.JMenuItem) e.getComponent().getComponentAt( e.getPoint() )).getText() );

                String s = ((javax.swing.JMenuItem) e.getComponent().getComponentAt( e.getPoint() )).getText();        

                String service = null;

                if( s.startsWith( "LIAS" ) ) service = "http://www.google.com/search?q=";
                if( s.startsWith( "Google Search" ) ) service = "http://www.google.com/search?q=";
                if( s.startsWith( "Google Maps" ) ) service = "http://images.google.com/images?q=";

                search( service, searchString );
            }
        };

        private void search( String search, String s )
        {    
            if( java.awt.Desktop.isDesktopSupported() )
            {
                try 
                {
                    java.awt.Desktop dt = java.awt.Desktop.getDesktop();

                    System.out.println( search );
                    search += java.net.URLEncoder.encode( s, "UTF-8" );
                    System.out.println( search );

                    java.net.URI uri = new java.net.URI( search );
                    java.net.URL url = new java.net.URL( search );

                    System.out.println( url );
                    System.out.println( uri );


                    dt.browse( uri.normalize() );
                    
                } 
                catch (IOException ex) 
                {
                    Logger.getLogger("global").log(Level.SEVERE, null, ex);
                }
                catch( URISyntaxException ex) 
                {
                    Logger.getLogger("global").log( Level.SEVERE, null, ex) ;
                }
            }    
       }
********************/        
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void addListSelectionListener( javax.swing.event.ListSelectionListener l )
    {
        theList.addListSelectionListener( l );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public JList getTheList()
    {
        return theList;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public BasicItemCharacter getItemCharacter()
    {
        return ( BasicItemCharacter ) basicItemCharacters.elementAt( theList.getSelectedIndex() );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public BasicItemCharacter getItemCharacter( int index )
    {
        return ( BasicItemCharacter ) basicItemCharacters.elementAt( index );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setItemCharacters( Vector basicItemCharacters )
    {
        this.basicItemCharacters = basicItemCharacters;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void updateList()
    {
        theList.removeAll();
        listData.removeAllElements();
        
        if( basicItemCharacters == null )
        {
            System.out.println( "NULL, idiot" );
        }
        else
        {
            for( int i = 0; i < basicItemCharacters.size(); i++ )
            {
                BasicItemCharacter ic = ( BasicItemCharacter )basicItemCharacters.elementAt( i );              
                listData.add( ic.getFeature() );
            }
            // Don't sort ItemCharacters
            // java.util.Collections.sort( listData );
            theList.setListData( listData );
        }
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
}
