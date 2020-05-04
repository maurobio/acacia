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
/*
 * ResultItemStateFrame.java
 *
 * Created on August 21, 2006, 12:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.metadiversity.diversity.navikey.ui;

import java.util.Vector;
import java.awt.BorderLayout;
import javax.swing.JButton;

/**
 *
 * @author Dieter Neubacher
 */
public class ResultItemStateFrame extends javax.swing.JFrame 
{    
    javax.swing.JButton close; // kill frame
    
    /** Creates a new instance of ResultItemStateFrame */
    public ResultItemStateFrame(  String title,  Vector<String> strings, Vector resources  )    
    {
        super();
        setTitle( title );
        setLayout( new BorderLayout() );       
        
        // close frame
        close = new JButton( "Close" );        
        close.addActionListener( new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                dispose();
            }
        } );
        
        setSize( 640, 480 );
       
        this.getContentPane().add( new ItemStatePanel( strings, resources ) );
        this.getContentPane().add( java.awt.BorderLayout.SOUTH, close );
        
        setVisible( true );
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        // TODO code application logic here
    }
    
}
