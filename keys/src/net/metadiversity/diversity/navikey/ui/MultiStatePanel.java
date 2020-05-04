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
import java.awt.event.MouseEvent;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JLabel;

import java.util.Vector;

import net.metadiversity.diversity.navikey.bo.BasicState;

/**
 * MultiStatePanel.java
 *
 * @author Michael Bartley
 * 3/25/98
 * @author Dieter Neubacher
 */
class MultiStatePanel extends javax.swing.JPanel
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3257008769614690608L;
    
    private Vector<BasicState>      basicStates;
    private JList                   theList;
    private javax.swing.JScrollPane jScrollPane;
    private Vector<String>          listData;
    private JButton                 selectButton;
    // ??? private BasicState toBeReturned;
    private java.awt.event.ActionListener navikeyGuiListener;
    
    private final static int VISIBLE_ROWS = 15;    
    private boolean enableColorSelection = false;
    
    public MultiStatePanel()
    {
        super();
        listData = new Vector<String>();
        setLayout( new BorderLayout() ); 
        
        JLabel title = new JLabel( "Character states available" );
        add( java.awt.BorderLayout.NORTH, title );
        
        basicStates = new Vector<BasicState>();
        
        theList = new JList( listData );
        theList.setVisibleRowCount( VISIBLE_ROWS );
        theList.setSelectionMode( javax.swing.ListSelectionModel.SINGLE_SELECTION );
        
        jScrollPane = new javax.swing.JScrollPane();
        jScrollPane.setViewportView( theList );
        
        add( java.awt.BorderLayout.CENTER, jScrollPane );
        // add select button (multiple selection)
        
        selectButton = new JButton( "Select" );
        add( BorderLayout.SOUTH, selectButton );
        
        if( enableColorSelection )
        {
            theList.setBackground( NaviKey.naviKeyBackgroundColor );
            theList.setForeground( NaviKey.itemStateColor );
        }
        setVisible( true );

        theList.addListSelectionListener( new javax.swing.event.ListSelectionListener()
        {
            public void valueChanged( javax.swing.event.ListSelectionEvent evt )
            {
                ValueChanged( evt );
            }
        } );
                
        selectButton.addActionListener( new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                System.out.println( evt.toString() );
                updateGui();
            }
        });
        /*
        theList.addMouseListener( new java.awt.event.MouseAdapter()
        {
            public void mouseClicked( java.awt.event.MouseEvent evt )
            {
                MouseClicked( evt );
            }
        } );
         */
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    void updateGui()
    {
        java.awt.event.ActionEvent ae = new java.awt.event.ActionEvent( this, 1, "UpdateGui" );
        
        navikeyGuiListener.actionPerformed( ae );        
    }    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    private void ValueChanged( javax.swing.event.ListSelectionEvent evt )
    {
        // System.out.println( "MultiStatePanel : " + evt );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /*
    private void MouseClicked( java.awt.event.MouseEvent evt )
    {
        System.out.println( evt );
    } 
     */   
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void addListSelectionListener( javax.swing.event.ListSelectionListener l )
    {
    	// enable multible selection
    	theList.setSelectionMode( DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
        theList.addListSelectionListener( l );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /*
    public void addActionListener( java.awt.event.ActionListener l )
    {
        selectButton.addActionListener( l );
    }
    */
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /*
    public boolean handleEvent( Event e )
    {
        if( e.id == Event.LIST_SELECT )
        {
            toBeReturned = ( BasicState )basicStates.elementAt( theList.getSelectedIndex() );
        }
        return false;
    }
     */
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /*
    public BasicState getState()
    {
        return toBeReturned;
    }
     */
    //--------------------------------------------------------------------------
    // ??? if multible states can be selected. So we have to return a vactor of BasicStates
    //--------------------------------------------------------------------------
    public BasicState getState( int index )
    {
        return basicStates.elementAt( index );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    public void setStates( Vector<BasicState> basicStates )
    {
        this.basicStates = basicStates;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    public void updateList()
    {     
        java.util.Comparator<BasicState> myComparator = new java.util.Comparator<BasicState>()
        {
            public int compare( BasicState a, BasicState b)
            {
                return a.getName().compareTo( b.getName() );         
            }
        };

        java.util.Collections.sort( basicStates, myComparator );
        listData.removeAllElements();
        for( int i = 0; i < basicStates.size(); i++ )
        {
            BasicState temp = basicStates.elementAt( i );
            listData.add( temp.getName() );
        }
        // java.util.Collections.sort( listData );
        theList.setListData( listData );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    public JList getTheList()
    {
        return theList;
    }
    //--------------------------------------------------------------------------    
    //--------------------------------------------------------------------------        
    public void addNavikeyGuiListener( java.awt.event.ActionListener al )
    {
        navikeyGuiListener = al;
    }    
}
