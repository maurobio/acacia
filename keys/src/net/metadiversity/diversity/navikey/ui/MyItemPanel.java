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
import java.awt.Color;
// import java.awt.Event;

import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.util.Vector;

import net.metadiversity.diversity.navikey.bo.BasicItemCharacter;
import net.metadiversity.diversity.navikey.bo.BasicState;
import net.metadiversity.diversity.navikey.bo.DeltaInterface;
import net.metadiversity.diversity.navigator.db.delta_editor.InapplicalibleTest;

/**
 * MyItemPanel.java
 *
 * @author Michael Bartley
 * 3/25/98
 * @author Dieter Neubacher
  */

class MyItemPanel extends javax.swing.JPanel implements java.awt.event.ActionListener
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 4050484504068370745L;
    
    Vector<BasicState> basicStates;
    JList theList;
    javax.swing.JScrollPane jScrollPane;
    javax.swing.JPanel buttonPanel;
    JButton remove;
    JButton reset;
    //Checkbox smarts;
    DeltaInterface delta;
    Vector<String> listData;
    
    private java.awt.event.ActionListener navikeyGuiListener;
    
    private int selectedIndex = -1;
    
    private final static int VISIBLE_ROWS = 15;    
    private boolean enableColorSelection = false;

    public MyItemPanel( DeltaInterface delta )
    {
        super();

        setLayout( new BorderLayout() );

        JLabel title = new JLabel( "Selection criteria" );
        add( java.awt.BorderLayout.NORTH, title );

        listData = new Vector<String>();
        this.delta = delta;
        basicStates = new Vector<BasicState>();
        
        theList = new JList( listData );
        theList.setVisibleRowCount( VISIBLE_ROWS );
        theList.setSelectionMode( javax.swing.ListSelectionModel.SINGLE_SELECTION );
        
        jScrollPane = new javax.swing.JScrollPane();
        jScrollPane.setViewportView( theList );
        
        add( java.awt.BorderLayout.CENTER, jScrollPane );
        
        //smarts  = new Checkbox( "Smarts" );
        //smarts.setState( true );
        remove  = new JButton( "Remove Selection" );
        reset   = new JButton( "Remove All" );
        
        if( enableColorSelection )
        {
            theList.setBackground( NaviKey.naviKeyBackgroundColor );
            theList.setForeground( NaviKey.itemSelectionsColor );
            remove.setBackground( Color.black );
            remove.setForeground( Color.pink );
            reset.setBackground( Color.black );
            reset.setForeground( Color.pink );
        }
        buttonPanel = new javax.swing.JPanel( new BorderLayout() );
        buttonPanel.add( java.awt.BorderLayout.WEST, remove );
        buttonPanel.add( java.awt.BorderLayout.EAST, reset );

        this.setVisible( true );

        // remove.addActionListener( this );
        // reset.addActionListener( this );
        
        remove.addActionListener( new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                // ??? selectedIndex = theList.getSelectedIndex();
                selectedIndex = theList.getAnchorSelectionIndex();
                if( selectedIndex != -1 )
                {
                    removeStateAt( selectedIndex );
                    updateList();
                    updateGui();
                }
            }
        } );
        
        reset.addActionListener( new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                removeAllStates();
                updateList();
                updateGui();
            }
        } );
        
        add( java.awt.BorderLayout.SOUTH, buttonPanel );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void updateList()
    {
        listData.clear();        
        InapplicalibleTest it = null;
        
        for( int i = 0; i < basicStates.size(); i++ )
        {
            BasicState st = basicStates.elementAt(i);
            BasicItemCharacter bc = delta.getBasicItemCharacter( st.getCharacterId() );
            listData.add( bc.getFeature() + ":  " + st.getName() );            
        }
        theList.setListData( listData );
    }
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public Vector<BasicState> getStates()
    {
        return basicStates;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setStates( Vector<BasicState> basicStates )
    {
        this.basicStates = basicStates;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void addState( BasicState st )
    {
        basicStates.addElement( st );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void removeStateAt( int id )
    {
        basicStates.removeElementAt( id );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void removeAllStates()
    {
        basicStates.removeAllElements();
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void actionPerformed(java.awt.event.ActionEvent actionEvent)
    {
        System.out.println( "MyItemPanel : " + actionEvent );        
        selectedIndex = theList.getSelectedIndex();
        System.out.println( selectedIndex );
        System.out.println( theList.getAnchorSelectionIndex() );
        System.out.println( theList.getLeadSelectionIndex() );
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
    public void addNavikeyGuiListener( java.awt.event.ActionListener al )
    {
        navikeyGuiListener = al;
    }    
}
