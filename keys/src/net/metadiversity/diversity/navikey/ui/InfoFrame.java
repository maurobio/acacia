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
 * InfoFrame.java
 *  Displays information about the current version of NaviKey
 * @author Michael Bartley
 * @date 7/1/99
 * @author Dieter Neubacher
 */

package net.metadiversity.diversity.navikey.ui;

public class InfoFrame extends javax.swing.JFrame
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3258409547327419444L;
    private javax.swing.JButton closebutton;

    public InfoFrame()
    {
        // super( "About NaviKey v. 4.07" );
        
        super.setTitle( "About NaviKey " + About.VERSION );
        
        getContentPane().setLayout( new java.awt.BorderLayout() );
        
        closebutton = new javax.swing.JButton( "Close" );
        closebutton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                closeButtonActionPerformed( evt );
            }
        });
        
        getContentPane().add( java.awt.BorderLayout.SOUTH, closebutton );
        getContentPane().add( java.awt.BorderLayout.CENTER, new About() );
    }
    
    private void closeButtonActionPerformed( java.awt.event.ActionEvent evt )
    {
        setVisible( false );
        dispose();
    }
}
