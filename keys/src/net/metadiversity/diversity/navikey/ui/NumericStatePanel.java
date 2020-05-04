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

// import java.awt.Event;

import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import javax.swing.JPanel;

import net.metadiversity.diversity.navikey.bo.BasicState;
import net.metadiversity.diversity.navikey.bo.NumericState;

/**
 * NumericStatePanel.java
 *
 * @author Michael Bartley
 * 3/25/98
 * @author Dieter Neubacher
  */

class NumericStatePanel extends javax.swing.JPanel implements java.awt.event.ActionListener
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3834313933862744371L;

    BasicState toBeReturned;
    
    javax.swing.JLabel      unit1;
    javax.swing.JLabel      unit2;
    javax.swing.JLabel      unit3;
    javax.swing.JTextField  singleInput;
    javax.swing.JTextField  lowerRange;
    javax.swing.JTextField  upperRange;
    javax.swing.JCheckBox   single;
    javax.swing.JCheckBox   range;
    javax.swing.ButtonGroup cbg;
    javax.swing.JButton     set;

    private boolean enableColorSelection = false;
    
    private java.awt.event.ActionListener navikeyGuiListener;
    
    public NumericStatePanel()
    {
        super();
        
        setLayout( new java.awt.GridLayout( 5, 1 ) );
        
        unit1 = new JLabel();
        unit2 = new JLabel();
        unit3 = new JLabel();
        
        singleInput = new JTextField( 10 );
        
        cbg = new javax.swing.ButtonGroup();
        
        lowerRange  = new JTextField( 10 );
        lowerRange.setEditable( false );
        
        upperRange  = new JTextField( 10 );
        upperRange.setEditable( false );
        

            
        cbg = new javax.swing.ButtonGroup();
        single = new JCheckBox( "Single Measurement", true );
        cbg.add( single );
        range  = new JCheckBox( "Measurements fall in the range", false );
        cbg.add( range );

        JPanel emptyPanel = new JPanel();
        add( emptyPanel );
        
        JPanel topPanel = new JPanel();
        topPanel.add( single );
        topPanel.add( singleInput );
        topPanel.add( unit1 );
        add( topPanel );
        
        JPanel rangePanel = new JPanel();
        rangePanel.add( range );
        add( rangePanel );
        
        JPanel bottomPanel = new JPanel();
        JLabel llower = new JLabel( "between" );
        JLabel lupper = new JLabel( "and" );

        bottomPanel.add( llower );
        bottomPanel.add( lowerRange );
        // bottomPanel.add( unit2 );
        bottomPanel.add( lupper );
        bottomPanel.add( upperRange );
        bottomPanel.add( unit3 );
        add( bottomPanel );

        JPanel addPanel = new JPanel();
        addPanel.setLayout( new java.awt.BorderLayout() );
        set = new JButton( "Add" );
        set.setMaximumSize( new Dimension( 150, 20 ) );
        // "Add" Button size like "Select" Button size, so add an empty Label 
        JLabel xxx = new JLabel( "" );
        addPanel.add( java.awt.BorderLayout.NORTH, xxx );
        addPanel.add( java.awt.BorderLayout.SOUTH, set );
        add( addPanel );
        
        set.addActionListener( this );
        single.addActionListener( this );
        range.addActionListener( this );
        
        if( enableColorSelection )
        {
            singleInput.setBackground( NaviKey.boxBGColor );
            singleInput.setForeground( NaviKey.boxFGColor );
           
            lowerRange.setBackground( NaviKey.boxBGColor );
            lowerRange.setForeground( NaviKey.boxFGColor );
            
            upperRange.setBackground( NaviKey.boxBGColor );
            upperRange.setForeground( NaviKey.boxFGColor );
            
            set.setBackground( NaviKey.naviKeyBackgroundColor );
            set.setForeground( NaviKey.buttonColor );
            
            // single.setBackground( NaviKey.naviKeyBackgroundColor );
            // single.setForeground( NaviKey.buttonColor );

            // range.setBackground( NaviKey.naviKeyBackgroundColor );
            // range.setForeground( NaviKey.buttonColor );
        }
        setVisible(true );
    }
    //--------------------------------------------------------------------------    
    //--------------------------------------------------------------------------    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent)    
    {
        System.out.println( "NumericStatePanel : " + actionEvent );
        
        if( actionEvent.getSource() == range )
        {
            System.out.println( "range" );
            
            singleInput.setText( "" );
            singleInput.setEditable( false );
            lowerRange.setEditable( true );
            upperRange.setEditable( true );
        }
        else if( actionEvent.getSource() == single )
        {
            System.out.println( "single" );

            singleInput.setEditable( true );
            lowerRange.setText( "" );
            lowerRange.setEditable( false );
            upperRange.setText( "" );
            upperRange.setEditable( false );
        }
        else if( actionEvent.getSource() == set )
        {
            System.out.println( "set" );

            toBeReturned = new NumericState();
            if( single.isSelected() )
            {
                String temp = singleInput.getText();
                double d = toDouble( temp );
                toBeReturned.setName( ( new Double( d ) ).toString() );
            }
            else if( range.isSelected() )
            {
                String temp = lowerRange.getText();
                double dl = toDouble( temp );
                temp = upperRange.getText();
                double du = toDouble( temp );
                toBeReturned.setName( dl + "-" + du );                
            }
            java.awt.event.ActionEvent ae = new java.awt.event.ActionEvent( this, 1, "Set numeric state" );        
            navikeyGuiListener.actionPerformed( ae );        
        }
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
    public BasicState getState()
    {
        return toBeReturned;
    }
    //--------------------------------------------------------------------------    
    //--------------------------------------------------------------------------    
    public double toDouble( String temp )
    {
        double d;
        try
        {
            d = Double.valueOf( temp ).doubleValue();
        }
        catch( NumberFormatException e )
        {
            d = 0;
        }
        return d;
    }
    //--------------------------------------------------------------------------    
    //--------------------------------------------------------------------------    
    public void reset()
    {
        singleInput.setText( "" );
        lowerRange.setText( "" );
        upperRange.setText( "" );
    }
    //--------------------------------------------------------------------------    
    //--------------------------------------------------------------------------    
    public String getInput()
    {
        return singleInput.getText();
    }
    //--------------------------------------------------------------------------    
    //--------------------------------------------------------------------------        
    public void addNavikeyGuiListener( java.awt.event.ActionListener al )
    {
        navikeyGuiListener = al;
    }
    //--------------------------------------------------------------------------    
    //--------------------------------------------------------------------------        
    public void setText( String text )
    {
        int pos = text.lastIndexOf( "[" );
        if( pos > 0 )
        {    
            unit1.setText( text.substring( pos ) );
            unit2.setText( text.substring( pos ) );
            unit3.setText( text.substring( pos ) );
        }
        else
        {
            unit1.setText( "" );
            unit2.setText( "" );
            unit3.setText( "" );
        }
    }
}
