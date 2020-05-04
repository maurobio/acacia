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
 * NaviKeyApplication.java
 *
 * Created on 7. April 2004, 15:06
 *
 * @author Dieter Neubacher
 */

package net.metadiversity.diversity.navikey.ui;

import java.net.URL;

/**
 *
 * @author Dieter Neubacher
  */
public class NaviKeyApplication extends javax.swing.JFrame
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3833184739816584758L;
    NaviKey navikey = null;
    
    /** Creates new form NaviKeyApplication */
    public NaviKeyApplication()
    {
        navikey = new NaviKey( getContentPane(), getParameter( "configfile" ), getCodeBase() );
        this.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
        navikey.init();
        setSize( 800, 600 );
    }    
    //--------------------------------------------------------------------------
    public NaviKeyApplication( String configfile )
    {
        System.out.println( "Create New NaviKey, use configfile : " + configfile  );
        navikey = new NaviKey( getContentPane(), configfile, getCodeBase() );
        this.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
        navikey.init();
        setSize( 800, 600 );
    }    
    //--------------------------------------------------------------------------
    private URL getCodeBase()
    {
    	System.out.println( "NavikeyApplicaion: getCodeBase()" );
        try
        {
        	String file = null;
        	
        	String userDir = System.getProperty( "user.dir" );
        	
        	file = "FILE:///" + userDir + "/";	// Windows: working directorey
        	
        	return new URL( file );
        }
        catch( java.net.MalformedURLException ex )
        {
        	System.out.println( "NavikeyApplication: ERROR can't get CodeBase" );
            ex.printStackTrace();
        }
        return null;
    }
    //--------------------------------------------------------------------------
    private String getParameter( String arg )
    {
        if( arg.equals( "configfile"))
        {    
            return "NaviKeyConfig.xml";
        }
        else
        {
            return null;
        }
    }
    private void initComponents()
    {
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                exitForm(evt);
            }
        });
        setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
        pack();
    }
    
    /** Exit the Application */
    private void exitForm( java.awt.event.WindowEvent evt )
    {
        System.exit( 0 );
    }
    public void windowClosing( java.awt.event.WindowEvent evt )
    {
        System.exit( 0 );        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        // default properties
        if( args.length == 0 )
        {
            System.out.println( "Start NaviKey with defaults" );
            new NaviKeyApplication().setVisible( true );
        }
        else
        {    
            int i = 0; 
            while( args.length > i )
            {    
                System.out.println();
                System.out.println( "Start NaviKey with propsfile: " + args[ i ] );
                new NaviKeyApplication( args[ i ] ).setVisible( true );
                i++;
            }
        }
    }    
}
