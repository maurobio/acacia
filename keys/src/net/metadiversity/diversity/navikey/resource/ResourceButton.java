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


import net.metadiversity.diversity.navikey.bo.Resource;

/**
 * ResourceButton.java
 * extends Button
 * holds a Resource, which will be fired upon the buttons selection
 * @author Michael Bartley
 * @author Dieter Neubacher
 * 7/17/98
 */

public class ResourceButton extends javax.swing.JButton
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3762813796632115509L;
    
    private Resource resource;
    
    public ResourceButton(Resource r)
    {
        super();
        resource = r;
        this.setText( resource.getMimeType() );
        
        this.addActionListener( new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                resource.display();                
            }
        }
        );
    }
    
    public void setResource(Resource r)
    {
        resource = r;
    }
    public Resource getResource()
    {
        return resource;
    }
}
