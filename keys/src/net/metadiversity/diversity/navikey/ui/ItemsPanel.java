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
import javax.swing.JList;
import javax.swing.JLabel;
import java.util.Vector;

import java.io.*;
import java.net.URISyntaxException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.metadiversity.diversity.navikey.bo.BasicItem;

/**
 * ItemsPanel.java
 *
 * @author Michael Bartley
 * 3/25/98
 * @author Dieter Neubacher
 */

class ItemsPanel extends javax.swing.JPanel
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3258132461857551927L;
    private Vector basicItems;
    private JList theList;
    private Vector<String> listData;
    private javax.swing.JScrollPane jScrollPane;

    private String searchString = null;
    
    // ??? private BasicItem selectedItem;
    
    private final static int VISIBLE_ROWS = 15;
    private boolean enableColorSelection = false;
    
    public ItemsPanel( final NaviKey navikey )
    {
        super();
        setLayout( new BorderLayout() );


        JLabel title = new JLabel( "Resulting  items" );
        add( java.awt.BorderLayout.NORTH, title );
        
        basicItems = null;
        
        listData = new Vector<String>();
        theList = new JList( listData );
        theList.setVisibleRowCount( VISIBLE_ROWS );
        theList.setSelectionMode( javax.swing.ListSelectionModel.SINGLE_SELECTION );
        
        jScrollPane = new javax.swing.JScrollPane();
        jScrollPane.setViewportView( theList );
        
        add( java.awt.BorderLayout.CENTER, jScrollPane );
        
        if( enableColorSelection )
        {
            theList.setBackground(NaviKey.naviKeyBackgroundColor);
            theList.setForeground(NaviKey.itemListColor);            
        }
        setVisible( true );

        theList.addListSelectionListener(new javax.swing.event.ListSelectionListener()
        {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt)
            {
                ValueChanged(evt);
            }
        });
        
        
        theList.addMouseListener(new java.awt.event.MouseAdapter() 
        {
            public void mouseClicked(java.awt.event.MouseEvent evt) 
            {
                if( evt.getButton() != java.awt.event.MouseEvent.BUTTON1 )
                {

                    System.out.println( "lList1MouseClicked" + evt );
                    System.out.println( "Point " + evt.getPoint() );
                    System.out.println( "Mouse Position" + theList.getMousePosition() );
                    System.out.println( "Index " + theList.locationToIndex( theList.getMousePosition() ) );
                    
                    String selection = (String) theList.getModel().getElementAt( theList.locationToIndex( theList.getMousePosition() ) );
                    navikey.setSearchString( selection );

                    javax.swing.JPopupMenu popup = navikey.getItemsPanelPopupMenu();    
                    
                    //--------------------------------------------------------------------------

                    popup.show( evt.getComponent(), evt.getX(), evt.getY() );

                }
            }
        });
        
    }
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private void ValueChanged(javax.swing.event.ListSelectionEvent evt)
    {
        // System.out.println( "ItemsPanel ValueChanged(): " + evt );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /*
    public BasicItem getItem()
    {
        return selectedItem;
    }
     */
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public BasicItem getItem( int index )
    {
        // ??? return selectedItem = (BasicItem)basicItems.elementAt( index );
        return (BasicItem)basicItems.elementAt( index );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /*
    public JList getTheList()
    {
        return theList;
    }
     */
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void updateList()
    {   
        listData.removeAllElements();
        
        if(basicItems == null)
        {    
            System.out.println("Null, idiot");
        }
        else
        {
            for(int i = 0; i < basicItems.size(); i++)
            {
                BasicItem item = (BasicItem)basicItems.elementAt(i);
                //System.out.println("adding : " + item.getName());
                // ??? theList.addItem(item.getName());
                listData.add( item.getName() );
            }
            theList.setListData( listData );
        }
        repaint();
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    public void setItems(Vector basicItems)
    {
        this.basicItems = basicItems;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    public Vector getItems()
    {
        return basicItems;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void addListSelectionListener( javax.swing.event.ListSelectionListener l )
    {
        theList.addListSelectionListener( l );
    }
}
