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
package net.metadiversity.diversity.navikey.resource;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;
import java.awt.ScrollPane;
import java.net.MalformedURLException;
import java.net.URL;

import net.metadiversity.diversity.navikey.ui.ImageViewer;


/**
 * ImageFrame
 * displays image in self, with close button at bottom
 * @author Michael Bartley
 * 7/16/98
 */

public class ImageFrame extends javax.swing.JFrame
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3544672880030856241L;
    
    private Button button;
    private IconPanel ip;
    private Panel bottomPanel;
    private boolean scrollable = true;
    
    private ImageFrame()
    {
        super();
        setLayout(new BorderLayout());
        //setBackground(NaviKeyApplet.imageBGColor);
        //setForeground(NaviKeyApplet.imageFGColor);
        bottomPanel = new Panel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setLayout(new BorderLayout());
        button = new Button("close");
        bottomPanel.add("West", button);
        add("South",bottomPanel);
    }
    
    public ImageFrame(String url) throws MalformedURLException
    {
        this(new IconPanel(url));
    }
    
    public ImageFrame(URL url, String label)
    {
        ImageViewer iv = new ImageViewer( url );
        this.add( iv );
        //if we used a scroll pane, make the window smaller
        if( scrollable )
        {
            setSize( 400, 300 );
        }
        this.setVisible( true );
            
    }
    
    
    public ImageFrame(IconPanel ip)
    {
        this();
        this.ip = ip;
        String label = ip.getLabel();
        if(label != null)
        {    
            bottomPanel.add("East", new Label(label));
        }
        setSize(ip.getImageWidth() + 10, ip.getImageHeight() + 52);
        ip.adjustPosition( new Point(0, 0) );
        setVisible( true );
        try
        {
            ScrollPane sp = new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
            sp.add(ip);
            add("Center",sp);
        }
        //ScollPane is Java 1.1, so older browsers need help
        catch(Exception noscrollpane)
        {
            scrollable = false;
            add("Center",ip);
        }
    }
    public static void main(String arg[])
    {
        //IconPanel ip = new IconPanel();
        //ImageFrame iff = new ImageFrame(ip);
        //ImageFrame iff = new ImageFrame("http://dev.huh.harvard.edu/~mbartley/images/test.jpg");
    }
}
