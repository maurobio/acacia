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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * IconPanel
 * Displays an image in itself, positionable relative to container of self
 *
 * @author Michael Bartley
 * @author Noel Cross
 * 7/14/98
 */

public class IconPanel extends Panel
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3256446906304771126L;
    
    private Image im = null;
    private int x = -1;
    private int y = -1;
    private String label = null;
    //ScrollPanel sp;
    
    
    //private constructor used to set stuff internally
    private IconPanel()
    {
        setLayout(new BorderLayout());
        setBackground(Color.white);
    }
    
    public IconPanel(String url) throws MalformedURLException
    {
        this(new URL(url));
    }
    
    public IconPanel(String url, String label) throws MalformedURLException
    {
        this(url);
        this.label = label;
    }
    
    public IconPanel(URL url, String label)
    {
        this(url);
        this.label = label;
    }
    
    //Constructor that does the work
    public IconPanel(URL url)
    {
        this();
        im = Toolkit.getDefaultToolkit().getImage(url);
        MediaTracker tracker = new MediaTracker(this);
        try
        {
            tracker.addImage(im, 0);
            tracker.waitForID(0);
        }
        catch(InterruptedException e)
        {
            System.out.println("Problem tracking media. ");
            e.printStackTrace();
        }
        this.setVisible( true );
    }
    
    public void adjustPosition(Point p)
    {
        this.x = p.x;
        this.y = p.y;
    }
    
    public void setLabel(String s)
    {
        label = s;
    }
    
    public String getLabel()
    {
        return label;
    }
    
    public int getImageHeight()
    {
        return im.getHeight(this);
    }
    
    public int getImageWidth()
    {
        return im.getWidth(this);
    }
    /*    
    public void paint(Graphics g)
    {
        if(this.x > -1)
            g.drawImage(im,this.x,this.y,this);
    }
    public void resize(int width, int height)
    {
        super.setSize( width, height );
        //im = im.getScaledInstance(width, height, Image.SCALE_FAST);
    }
    public Dimension size()
    {
        return getSize();
    }
    */
    
    public Dimension getSize()
    {
        return new Dimension(getImageWidth(), getImageHeight());
    }
    /*    
    public Rectangle bounds()
    {
        return new Rectangle(getImageWidth(), getImageHeight());
    }
    
    public Rectangle getBounds()
    {
        return bounds();
    }
    
    public Dimension getMaximumSize()
    {
        return getSize();
    }
    
    public Dimension getMinimumSize()
    {
        return getSize();
    }
    
    public Dimension getPreferredSize()
    {
        return getSize();
    }
    
    public Dimension minimumSize()
    {
        return getSize();
    }
    
    public Dimension maximumSize()
    {
        return getSize();
    }
    
    public Dimension preferredSize()
    {
        return getSize();
    }
    */
    public static void main(String argv[])
    {
        Image im = null;
        Frame f = new Frame();
        f.setSize( 200, 200 );
        IconPanel ip = new IconPanel();
        ip.adjustPosition(new Point(10,10));
        f.add("Center",ip);
        f.setVisible( true );
    }
}
