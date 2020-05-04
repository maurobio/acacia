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

import java.awt.GridLayout;
import java.util.Vector;

import net.metadiversity.diversity.navikey.bo.BasicItemCharacter;
import net.metadiversity.diversity.navikey.bo.BasicState;
import net.metadiversity.diversity.navikey.bo.DeltaInterface;

/**
 * SuperPanel.java
 *
 * @author Michael Bartley
 * 3/24/98
 * @author Dieter Neubacher
 */

class SuperPanel extends javax.swing.JPanel
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3256719576480758066L;
    
    ItemCharacterPanel itemCharacterPanel;
    MultiStatePanel    multiStatePanel;
    NumericStatePanel  numericStatePanel;
    ItemsPanel         itemsPanel;
    MyItemPanel        myItemPanel;
    StatePanel         statePanel; //will hold Multi/Numeric StatePanels
    // ??? ItemStatePanel     itemStatePanel; //extra popup
    DeltaInterface     delta;
    NaviKey            navikey;
    
    private String numericStatePanelAction =    "NumericStatePanelAction";
    private String myItemPanelAction =          "MyItemPanelAction";
    private String multiStatePanelAction =      "MultiStatePanelAction";
    
    private Vector<Integer> multiStatePanelSelectedIds = new Vector<Integer>();
    
    // Panel size (each of the for panels)
    
    private int panelSizeX  = 300;
    private int panelSizeY  = 250;
    
    private boolean debug = false;
    //
    // Constructor
    //
    public SuperPanel( NaviKey navikey, DeltaInterface delta )
    {
        super();
        this.delta = delta;
        this.navikey = navikey;
        
        setBackground( NaviKey.naviKeyBackgroundColor );
        setForeground( NaviKey.buttonColor );
        setLayout( new GridLayout( 2,2 ) );
        // top left
        itemCharacterPanel = new ItemCharacterPanel();
        // top right
        statePanel         = new StatePanel();        
        multiStatePanel    = new MultiStatePanel();
        numericStatePanel  = new NumericStatePanel();
        // button left
        myItemPanel        = new MyItemPanel( delta );
        // buttom right
        itemsPanel         = new ItemsPanel( navikey );
        
        multiStatePanel.addListSelectionListener( new javax.swing.event.ListSelectionListener()
        {
            public void valueChanged( javax.swing.event.ListSelectionEvent evt )
            {
                multiStatePanelChange( evt );
            }
        } );
        
        itemCharacterPanel.addListSelectionListener( new javax.swing.event.ListSelectionListener()
        {
            public void valueChanged( javax.swing.event.ListSelectionEvent evt )
            {
                itemCharacterPanelChange( evt );
            }
        } );
        
        itemsPanel.addListSelectionListener( new javax.swing.event.ListSelectionListener()
        {
            public void valueChanged( javax.swing.event.ListSelectionEvent evt )
            {
                itemsPanelSelection( evt );
            }
        } );
        
        
        GuiAction a;
        a = new GuiAction( navikey, numericStatePanelAction );        
        numericStatePanel.addNavikeyGuiListener( a );
        
        a = new GuiAction( navikey, myItemPanelAction );
        myItemPanel.addNavikeyGuiListener( a );
        
        a = new GuiAction( navikey, multiStatePanelAction );
        multiStatePanel.addNavikeyGuiListener( a );
        /*
        myItemPanel.getRemoveButton().addActionListener( new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                myItemPanelRemoveReset( evt );
            }
        } );
         */
        // All items Top Left
        itemCharacterPanel.setSize( new java.awt.Dimension( panelSizeX, panelSizeX ) );
        // States : multiStatePanel or numericStatePanel 
        statePanel.setSize( new java.awt.Dimension( panelSizeX, panelSizeX ) );
        multiStatePanel.setSize( new java.awt.Dimension( panelSizeX, panelSizeX ) );
        numericStatePanel.setSize( new java.awt.Dimension( panelSizeX, panelSizeX ) );      // TOP RIGHT
        // selected states Buttom Left
        myItemPanel.setSize( new java.awt.Dimension( panelSizeX, panelSizeX ) );        
        // results Buttom Right
        itemsPanel.setSize( new java.awt.Dimension( panelSizeX, panelSizeX ) );             // 
    }    
    //--------------------------------------------------------------------------
    // ??? Configuration Panel needs a NavikeyGuiListener to perform Options changed
    //--------------------------------------------------------------------------
    public javax.swing.AbstractAction getNavikeyGuiListener( String str )
    {
        return new GuiAction( navikey, str );       
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void myItemPanelRemoveReset( java.awt.event.ActionEvent evt )
    {
        if( debug ) System.out.println( "SuperPanel: myItemPanelRemoveReset" + evt );
        myItemPanel.updateList();
        itemsPanel.setItems( delta.getItemList( myItemPanel.getStates() ) );
        itemsPanel.updateList();
        // Useful for shrinking/growing character lists,
        // uncomment next two lines once implemented
        //COMMENTED OUT FOR SPEED
        //MIKE, 3/9/99
        if( navikey.isIntelligence() )
        {
            itemCharacterPanel.setItemCharacters( delta.getItemCharacterList( myItemPanel.getStates(), navikey.isIntelligence(), navikey.isDependencies() ) );
            itemCharacterPanel.updateList();
        }
        //Blank out state panel
        blankOutStatePanel();
        repaint();
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void itemCharacterPanelChange( javax.swing.event.ListSelectionEvent evt )
    {
        if( debug ) System.out.println( "SuperPanel: itemCharacterPanelChange(): " + evt );
        if( ! evt.getValueIsAdjusting() )
        {   
            // System.out.println( "SuperPanel : itemCharacterSelection :" + evt.getLastIndex() );
            int index = ((javax.swing.JList)evt.getSource()).getSelectedIndex();

            if( index >= 0 )
            {
                itemCharacterSelection( index );
            }
        }
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void multiStatePanelChange( javax.swing.event.ListSelectionEvent evt )
    {
        if( debug ) System.out.println( "SuperPanel: multiStatePanelChange" + evt );
        if( ! evt.getValueIsAdjusting() )
        {  
            // System.out.println( "SuperPanel : multiStatePanel :" + evt.getLastIndex() );
            int firstIndex = evt.getFirstIndex();
            int lastIndex = evt.getLastIndex();
            boolean isAdjusting = evt.getValueIsAdjusting();
            System.out.print( "Event for indexes " + firstIndex + " - " + lastIndex + "; isAdjusting is " + isAdjusting + "; selected indexes:");

            javax.swing.JList theList = (javax.swing.JList) evt.getSource();
            if( theList.isSelectionEmpty()) 
            {
            	System.out.print(" <none>");
            } 
            else 
            {
                multiStatePanelSelectedIds.clear();
                // Find out which indexes are selected.
                int minIndex = theList.getMinSelectionIndex();
                int maxIndex = theList.getMaxSelectionIndex();
                for( int i = minIndex; i <= maxIndex; i++ ) 
                {
                    if( theList.isSelectedIndex( i ) ) 
                    {
                        // System.out.print( " " + i );
                        
                        multiStatePanelSelectedIds.add( new Integer( i ) );
                    }
                }
            }
            // System.out.println();

            // multible selection is posible
            
            // ???
/* moved to GuiAction **********************************************************************
            // old source, only one item is selected
            int index = ((javax.swing.JList)evt.getSource()).getSelectedIndex();
            
            index = -1; // only selection test
            if( index >= 0 )
            {    
                // if a State is selected update it in MyItem
                myItemPanel.addState( multiStatePanel.getState( index ) );
                myItemPanel.updateList();               

                itemsPanel.setItems( delta.getItemList( myItemPanel.getStates() ) );
                itemsPanel.updateList();                
                // Useful for shrinking/growing character lists,
                //COMMENTED OUT FOR SPEED
                //MIKE, 3/9/99
                if( NaviKey.intelligence )
                {
                    itemCharacterPanel.setItemCharacters( delta.getItemCharacterList( myItemPanel.getStates(), NaviKey.intelligence ) );
                    itemCharacterPanel.updateList();
                }                
                blankOutStatePanel();
            }
            ******************************************************/
            // clear selection
            // ((javax.swing.JList) evt.getSource()).clearSelection();
        }        
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void itemsPanelSelection( javax.swing.event.ListSelectionEvent evt )
    {
        if( evt.getValueIsAdjusting() == true )
        {
            return;
        }
        // pop-up list of states defining selected item
        int selectedIndex = ((javax.swing.JList) evt.getSource()).getSelectedIndex();
                
        if( selectedIndex >= 0 )
        {
                Object o = itemsPanel.getItem( selectedIndex ).getId();
                String title = delta.getItemTitle( o );
                Vector<String> strings = delta.getItemDescription( o );
                Vector resources = delta.getItemResources( o );
                // Create and Show panel
                if( navikey.isUseTabSheetsFlag() )
                {
                    
                    title = net.metadiversity.diversity.navikey.delta.TImageFileReader.removeFormatAndComment( title );
                    
                    String image = delta.getItemImage( title );
                 
System.out.println( "Item: " + title + " Image: " + image );
                    
                    navikey.showResult( title, new ItemStatePanel(  strings, resources ), image );
                    
                }
                else
                {
                    // ItemStatePanel isp = 
                    new ResultItemStateFrame( title, strings, resources );
                }
        }
        // clear selection
        ((javax.swing.JList) evt.getSource()).clearSelection();
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void build()
    {
        statePanel.add( "multi",    multiStatePanel );
        statePanel.add( "numeric",  numericStatePanel );
        
        java.lang.Object o = statePanel.getLayout();        
        /*
        if( o instanceof java.awt.CardLayout )
        {    
            java.awt.CardLayout cl = ( java.awt.CardLayout ) statePanel.getLayout();
            cl.last( statePanel );
        }
         */
        add( itemCharacterPanel );      // Top left
        add( statePanel );              // top right
        add( myItemPanel );             // buttem left
        add( itemsPanel );              // butten right
        if( o instanceof java.awt.CardLayout )
        {    
            // statePanel.getTheLayout().show( statePanel, "numeric" );
            java.awt.CardLayout cl = ( java.awt.CardLayout ) statePanel.getLayout();
            cl.show( statePanel, "numeric" );
        }
        this.setVisible(  true );
    }    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void itemCharacterSelection( int index )
    {
        if( index < 0 )
        {
            return; // no selection
        }
        BasicItemCharacter ic = itemCharacterPanel.getItemCharacter( index );

        //if it is multi, set StatePanel to MultiState and update
        if(  ic.isMultiState() )
        {
            multiStatePanel.setStates( delta.getStateList( myItemPanel.getStates(), ic.getId(), navikey.isIntelligence() ) );
            multiStatePanel.updateList();
            (( java.awt.CardLayout ) statePanel.getLayout()).first( statePanel );
            repaint();
        }

        //if it is numeric set StatePanel to Numeric
        else if(  ic.isNumeric() )
        {   
            if( navikey.isShowLiasUnitsFlag() )
            {    
                numericStatePanel.setText( ic.getName() );
            }
/*            
            else
            {
                //String s = ic;
                ItemCharacter iic = (ItemCharacter) ic;
                State s = (State) iic.getStates().get( 0 );
                numericStatePanel.setText( ic.getName() + " [" + s.getName() + "]" );
            }
 */
            (( java.awt.CardLayout )statePanel.getLayout()).last( statePanel );
            repaint();
        }
        //handle text case
        else
        {
            System.out.println( "I hope this is text." );
        }
    }
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private void blankOutStatePanel()
    {
        numericStatePanel.reset();
        multiStatePanel.setStates( new Vector<BasicState>() );
        multiStatePanel.updateList();
        (( java.awt.CardLayout )statePanel.getLayout()).first( statePanel );

        itemCharacterPanel.getTheList().clearSelection();
    }    
    //--------------------------------------------------------------------------
    // GUI Actions 
    //--------------------------------------------------------------------------
    private class GuiAction extends javax.swing.AbstractAction
    {      
        boolean DEBUG_FLAG = false;
        /**
         * Comment for <code>serialVersionUID</code>
         */
        private static final long serialVersionUID = 3257001068687863860L;

        public GuiAction( NaviKey navikey, String text )
        {
            super( text );
        }
        
        public void actionPerformed(java.awt.event.ActionEvent e)
        {
            String name = (String) this.getValue( "Name" );
            
            // logger.info( "GuiAction: " + name + " [" + e.getActionCommand() + "]" );
            if( DEBUG_FLAG )
            {    
                System.out.println( "GuiAction: " + name + " [" + e.getActionCommand() + "]" );
            }    

            if( name.compareTo( "Options" ) == 0 )
            {  
                if( e.getActionCommand().compareTo( "OptionsChanged" ) == 0 )
                {
                    // Navikey Options are changed, update all panels                    
                    myItemPanel.updateList();
                    itemsPanel.setItems( delta.getItemList( myItemPanel.getStates() ) );
                    itemsPanel.updateList();
                    itemCharacterPanel.setItemCharacters( delta.getItemCharacterList( myItemPanel.getStates(), navikey.isIntelligence(), navikey.isDependencies() ) );
                    itemCharacterPanel.updateList();
                    repaint();
                }
            }
                    
            if( name.compareTo( numericStatePanelAction ) == 0 )
            {  
                if( e.getActionCommand().compareTo( "Set numeric state" ) == 0 )
                {
                    BasicState ns = numericStatePanel.getState();
                    ns.setCharacterId( itemCharacterPanel.getItemCharacter().getId() );

                    myItemPanel.addState( ns );
                    myItemPanel.updateList();
                    
                    itemsPanel.setItems( delta.getItemList( myItemPanel.getStates() ) );
                    itemsPanel.updateList();

                    // Useful for shrinking/growing character lists,
                    // uncomment next two lines once implemented
                    //COMMENTED OUT FOR SPEED
                    //MIKE, 3/9/99
                    if( navikey.isIntelligence() )
                    {
                        itemCharacterPanel.setItemCharacters( delta.getItemCharacterList( myItemPanel.getStates(), navikey.isIntelligence(), navikey.isDependencies() ) );
                        itemCharacterPanel.updateList();
                    }
                    blankOutStatePanel();
                    repaint();
                }
            }
            // Remove states or reset from the myItemPanel
            if( name.compareTo( myItemPanelAction ) == 0 )
            {              
                myItemPanelRemoveReset( e );
                repaint();
            }
            // Select pressed on multi state panel
            if( name.compareTo( multiStatePanelAction ) == 0 )
            {
                
                for( int i = 0; i < multiStatePanelSelectedIds.size(); i++ )
                {
                    int index = multiStatePanelSelectedIds.get(i).intValue();                    
                    if( index >= 0 )
                    {    
                        // if a State is selected update it in MyItem
                        myItemPanel.addState( multiStatePanel.getState( index ) );
                    }
                }
                myItemPanel.updateList();              
                itemsPanel.setItems( delta.getItemList( myItemPanel.getStates() ) );
                itemsPanel.updateList();                
                // Useful for shrinking/growing character lists,
                if( navikey.isIntelligence() || navikey.isDependencies() )
                {
                    itemCharacterPanel.setItemCharacters( delta.getItemCharacterList( myItemPanel.getStates(), navikey.isIntelligence(), navikey.isDependencies() ) );
                    itemCharacterPanel.updateList();
                }                
                blankOutStatePanel();
            }
        }
    }
}
