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
/**
 * ConfigurationPanel
 *   Presents choices needed to enable/disable various features of NaviKey
 *   Intended to be incorporated in the main NaviKey UI
 * @author Michael Bartley
 * @date 7/1/99
 * @author Dieter Neubacher
*/

package net.metadiversity.diversity.navikey.ui;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import net.metadiversity.diversity.navikey.ui.ConfigurationPanel;

class ConfigurationPanel extends javax.swing.JPanel
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 4051049644454720816L;
    private JCheckBox   useBestAlgorithm;
    private JCheckBox   useCharacterDependencies;
    private JCheckBox   smartsbox;
    private JCheckBox   excludeUndefined;
    private JCheckBox   multipleSelectionAndOr;
    private JCheckBox   extremeInterval;
    private JCheckBox   overlappingInterval;
    private JCheckBox   dependencies;
    private JButton     infobutton;
    
    // Informations
    
    private JPanel totalItemCount;
    private JLabel totalLabel;
    private JLabel totalCount;
    
    private JPanel resultItemCount;
    private JLabel resultLabel;
    private JLabel resultCount;

    private java.awt.event.ActionListener navikeyGuiListener;
    
    private NaviKey     navikey;
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    public ConfigurationPanel( NaviKey navikey )
    {
        new ConfigurationPanel( navikey, false );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    public ConfigurationPanel( NaviKey navikey, boolean useTabSheeds )
    {
        super();
        this.navikey = navikey;
     
        setLayout( new java.awt.BorderLayout() );
        // 
        JLabel label = new JLabel( "NaviKey options: " );
        label.setVerticalAlignment( javax.swing.SwingConstants.TOP );
        
        useBestAlgorithm = new JCheckBox( "Use \"Best\" algorithm (To be implemented)");
        useBestAlgorithm.setEnabled( false );
        // dependencies
        useCharacterDependencies = new JCheckBox( "Use character dependencies");
        useCharacterDependencies.setSelected( navikey.isDependencies()  );
        
        useCharacterDependencies.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                dependenciesActionPerformed( evt );
            }
        });
        
        smartsbox = new JCheckBox( "Restrict view on used characters and character states of remaining items" );
        smartsbox.setSelected( navikey.isIntelligence()  );
        
        smartsbox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                smartsBoxActionPerformed( evt );
            }
        });
//        excludeUndefined = new JCheckBox( "Exclude items with undefined states in selected characters" );
        excludeUndefined = new JCheckBox( "Retain items unrecorded for the selected characters" );
        // Don't evaluate undefind CharacterStates
        excludeUndefined.setSelected( ! navikey.isExcludeUndefined()  );
        
        excludeUndefined.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                excludeUndefinedActionPerformed( evt );
            }
        });
        // Multiple State selection with AND / OR
//        multipleSelectionAndOr = new JCheckBox( "Enable multiple selection of character states (with OR)" );
        multipleSelectionAndOr = new JCheckBox( "Retain items matching at least one selected state" );
        multipleSelectionAndOr.setSelected( navikey.isMultipleSelectionOR()  );
        
        multipleSelectionAndOr.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                multipleSelectionAndOrActionPerformed( evt );
            }
        });
        // extreme interval validation
        extremeInterval = new JCheckBox( "Use extreme interval validation" );
        extremeInterval.setSelected( navikey.isExtremeInterval()  );
        
        extremeInterval.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                extremeIntervalActionPerformed( evt );
            }
        });
        // overlapping interval vaidation
        overlappingInterval = new JCheckBox( "Use overlapping interval validation" );
        overlappingInterval.setSelected( navikey.isOverlappingInterval()  );
        
        overlappingInterval.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                overlappingIntervalActionPerformed( evt );
            }
        });
        if( ! useTabSheeds )
        {
            // Show "About" Window
            infobutton = new JButton( "About NaviKey" );        
            infobutton.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    infoButtonActionPerformed( evt );
                }
            });
        }
        JPanel optspanel = new JPanel();
        optspanel.setLayout( new java.awt.GridLayout( 7, 1 ) );
        optspanel.add( useBestAlgorithm );
        optspanel.add( useCharacterDependencies );
        optspanel.add( smartsbox );
        optspanel.add( excludeUndefined );
        optspanel.add( multipleSelectionAndOr );
        optspanel.add( extremeInterval  );
        optspanel.add( overlappingInterval );
        
        add( java.awt.BorderLayout.WEST,    label       );
        add( java.awt.BorderLayout.CENTER,  optspanel   );

        // ItemCount Panel

        JPanel infoPanel = new JPanel();
       
        if( useTabSheeds )
        {
            infoPanel.setLayout( new java.awt.GridLayout( 1, 2 ) );
            //----
            totalItemCount = new JPanel();
            totalItemCount.setLayout( new java.awt.GridLayout( 1, 2 ) );
            totalLabel = new JLabel( "All items: ");
            totalCount = new JLabel( "??" );

            totalItemCount.add( totalLabel );
            totalItemCount.add( totalCount );
            //-----        
            resultItemCount = new JPanel();
            resultItemCount.setLayout( new java.awt.GridLayout( 1, 2 ) );
            resultLabel = new JLabel( "Resulting items: ");
            resultCount = new JLabel( "?" );

            resultItemCount.add( resultLabel );
            resultItemCount.add( resultCount );

            infoPanel.add( totalItemCount );
            infoPanel.add( resultItemCount );
            
            navikey.getContentPane().add( java.awt.BorderLayout.SOUTH, infoPanel );
        }    
        else    
        {
            infoPanel.setLayout( new java.awt.GridLayout( 3, 1 ) );
            //----
            totalItemCount = new JPanel();
            totalItemCount.setLayout( new java.awt.GridLayout( 1, 2 ) );
            totalLabel = new JLabel( "All items: ");
            totalCount = new JLabel( "??" );

            totalItemCount.add( totalLabel );
            totalItemCount.add( totalCount );
            //-----        
            resultItemCount = new JPanel();
            resultItemCount.setLayout( new java.awt.GridLayout( 1, 2 ) );
            resultLabel = new JLabel( "Resulting items: ");
            resultCount = new JLabel( "?" );

            resultItemCount.add( resultLabel );
            resultItemCount.add( resultCount );

            infoPanel.add( totalItemCount );
            infoPanel.add( resultItemCount );
            infoPanel.add( infobutton );       // About button
    //        add( java.awt.BorderLayout.EAST,    infobutton  );
            add( java.awt.BorderLayout.EAST,    infoPanel  );
        }
        
        setVisible( true );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private void infoButtonActionPerformed( java.awt.event.ActionEvent evt )
    {
        System.out.println( "Info about NaviKey:" );        
        
        InfoFrame inf = new InfoFrame();
        inf.setSize( 500,450 );
        inf.setVisible( true );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private void smartsBoxActionPerformed( java.awt.event.ActionEvent evt )
    {
        boolean old = smartsbox.isSelected(); 
        navikey.setIntelligence( ( old ? true : false ) );
        
        java.awt.event.ActionEvent ae = new java.awt.event.ActionEvent( this, 1, "OptionsChanged" );        
        navikeyGuiListener.actionPerformed( ae );        
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private void excludeUndefinedActionPerformed( java.awt.event.ActionEvent evt )
    {
        boolean old = ! excludeUndefined.isSelected(); 
        navikey.setExcludeUndefined( ( old ? true : false ) );
        
        java.awt.event.ActionEvent ae = new java.awt.event.ActionEvent( this, 1, "OptionsChanged" );        
        navikeyGuiListener.actionPerformed( ae );        
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    private void multipleSelectionAndOrActionPerformed( java.awt.event.ActionEvent evt )
    {
        boolean old = multipleSelectionAndOr.isSelected(); 
        navikey.setMultipleSelectionOR( ( old ? true : false ) );
        
        java.awt.event.ActionEvent ae = new java.awt.event.ActionEvent( this, 1, "OptionsChanged" );        
        navikeyGuiListener.actionPerformed( ae );        
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    private void extremeIntervalActionPerformed( java.awt.event.ActionEvent evt )
    {
        boolean old = extremeInterval.isSelected(); 
        navikey.setExtremeInterval( ( old ? true : false ) );
        
        java.awt.event.ActionEvent ae = new java.awt.event.ActionEvent( this, 1, "OptionsChanged" );        
        navikeyGuiListener.actionPerformed( ae );        
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    private void overlappingIntervalActionPerformed( java.awt.event.ActionEvent evt )
    {
        boolean old = overlappingInterval.isSelected(); 
        navikey.setOverlappingInterval( ( old ? true : false ) );
        
        java.awt.event.ActionEvent ae = new java.awt.event.ActionEvent( this, 1, "OptionsChanged" );        
        navikeyGuiListener.actionPerformed( ae );        
    }
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    private void dependenciesActionPerformed( java.awt.event.ActionEvent evt )
    {
        boolean old = useCharacterDependencies.isSelected(); 
        navikey.setDependencies( ( old ? true : false ) );
        
        java.awt.event.ActionEvent ae = new java.awt.event.ActionEvent( this, 1, "OptionsChanged" );        
        navikeyGuiListener.actionPerformed( ae );        
    }
            /* ???
    public void setForeground( Color c )
    {
        smartsbox.setForeground( c );
        infobutton.setForeground( c );
        super.setForeground( c );
    }
    
    public void setBackground( Color c )
    {
        smartsbox.setBackground( c );
        infobutton.setBackground( c );
        super.setBackground( c );
    }
    */

    /*
    public boolean processEvent( Event e )
    {
        if( e.target == smartsbox )
        {
            if( e.id == Event.ACTION_EVENT )
            {
                boolean old = ( ( JCheckBox )e.target ).isSelected(); 
                NaviKey.intelligence = ( old ? true : false );
            }
        }
        else if( e.target == infobutton )
        {
            if( e.id == Event.ACTION_EVENT )
            {
                InfoFrame inf = new InfoFrame();
                inf.setSize( 500,450 );
                inf.setVisible( true );
                System.out.println( "Info about NaviKey:" );
            }
        }
        return super.processEvent( e );
    }
    */
    //--------------------------------------------------------------------------    
    //--------------------------------------------------------------------------        
    public void addNavikeyGuiListener( java.awt.event.ActionListener al )
    {
        navikeyGuiListener = al;
    }    
    // Informations displayed on GUI
    /**
     * @param resultLabelText The resultLabelText to set.
     */
    public void setResultLabelText( String resultLabelText )
    {
        this.resultCount.setText( resultLabelText );
        this.resultCount.repaint();
    }
    /**
     * @param totalLabelText The totalLabelText to set.
     */
    public void setTotalLabelText( String totalLabelText )
    {
        this.totalCount.setText( totalLabelText );
        this.totalCount.repaint();
    }
}
