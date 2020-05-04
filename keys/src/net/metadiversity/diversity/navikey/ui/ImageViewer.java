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
 * ImageViewer.java
 *
 * Created on 1. August 2003, 17:48
 */

package net.metadiversity.diversity.navikey.ui;

/**
 *
 * @author  Dieter Neubacher
 */
public class ImageViewer extends javax.swing.JPanel
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3258417231057596473L;
        
    private javax.swing.JScrollPane imageScrollPane;
    private javax.swing.JPanel      zoomPanel;
    private javax.swing.JButton     zoomInButton;
    private javax.swing.JButton     zoomOutButton;
    private javax.swing.JButton     saveButton;
    
    javax.swing.ImageIcon           imageIcon;
    javax.swing.JLabel              imageLabel;
    double zoomFactor;
    java.awt.image.AffineTransformOp  transform;
    java.awt.geom.AffineTransform     t;

    
    private java.awt.image.BufferedImage srcImage;
    private java.awt.image.BufferedImage destImage;
    
    private final static String ZOOM_IN_LABEL    = "Zoom In";
    private final static String ZOOM_OUT_LABEL   = "Zoom Out";
    private final static String SAVE_LABEL       = "Save image";
    
    /** Creates new form ImageViewer */
    public ImageViewer( java.net.URL url )
    {
        zoomFactor = 1.0;        
        t = new java.awt.geom.AffineTransform();
        
        try
        {
            loadImage( url );
            imageIcon  = new javax.swing.ImageIcon( destImage );
        }
        catch( java.io.IOException ex )
        {

// ???            DnOptionPane.showMessageDialog( null, ex.toString(), "Error" , DnOptionPane.ERROR_MESSAGE );                
/*            
            logger.error( ex.getMessage(), ex.fillInStackTrace() );
 */
        }        
        initComponents();
        repaint();
    }
    //--------------------------------------------------------------------------
    //
    //--------------------------------------------------------------------------
    private void loadImage( java.net.URL url ) throws java.io.IOException
    {
/*          // works only whis JPEG-Pictures  
            srcImage  = javax.imageio.ImageIO.read( url );
            destImage = javax.imageio.ImageIO.read( url );
            imageIcon = new javax.swing.ImageIcon( destImage );
 */
/*        
            logger.info( "Load image from URL: " + url );
 */
            // GIF-Pictures must converted
            java.awt.Image img = null;
            
            if( url != null )
            {    
                System.out.println( url.toString() );
// ???                TestUrlConnection test = new TestUrlConnection( url );
//                System.out.println( "return from TestUrlConnection()" );

// ??                if( test.isConnected() )
                {
                    try
                    {
                        img = javax.imageio.ImageIO.read( url );
                    }
                    catch( javax.imageio.IIOException ex )
                    {
// ???                        DnOptionPane.showMessageDialog( null, ex.toString(), "Error" , DnOptionPane.ERROR_MESSAGE );     
                    }
                    catch( java.io.IOException ex )
                    {
// ???                        DnOptionPane.showMessageDialog( null, ex.toString(), "Error" , DnOptionPane.ERROR_MESSAGE );     
                    }
                }
            }
            
            if( img == null )
            {    
                // open an empty image
                img = new java.awt.image.BufferedImage( 100, 200, java.awt.image.BufferedImage.TYPE_INT_RGB );
            }    
            srcImage = new java.awt.image.BufferedImage( (int)( img.getWidth( null ) * zoomFactor ), (int)( img.getHeight( null ) * zoomFactor ), java.awt.image.BufferedImage.TYPE_INT_RGB );
            java.awt.Graphics2D srcImageContext = srcImage.createGraphics();
            srcImageContext.drawImage( img, 0, 0, null );
            
            destImage = new java.awt.image.BufferedImage( (int)( srcImage.getWidth() * zoomFactor ), (int)( srcImage.getHeight() * zoomFactor ), java.awt.image.BufferedImage.TYPE_INT_RGB );
            java.awt.Graphics2D destImageContext = destImage.createGraphics();
            destImageContext.drawImage( img, 0, 0, null );        
    }
    //--------------------------------------------------------------------------
    //
    //--------------------------------------------------------------------------
    public void showUrl( java.net.URL url )
    {        
//        if( logger.isDebugEnabled() ) logger.debug( url );
        try
        { 
            loadImage( url );
            imageIcon  = new javax.swing.ImageIcon( destImage );
        }
        catch( java.io.IOException ex )
        {
// ???            DnOptionPane.showMessageDialog( null, ex.toString(), "Error" , DnOptionPane.ERROR_MESSAGE );                
//            logger.error( ex.getMessage(), ex.fillInStackTrace() );
        }        
        zoomFactor = 1.0;         
                
        t.setToScale( zoomFactor, zoomFactor );
//        if( logger.isDebugEnabled() ) logger.debug( "SrcImageType: " + srcImage.getType() );
        destImage = new java.awt.image.BufferedImage( (int)( srcImage.getWidth() * zoomFactor ), (int)( srcImage.getHeight() * zoomFactor ), srcImage.getType() );
        transform = new java.awt.image.AffineTransformOp( t, java.awt.image.AffineTransformOp.TYPE_NEAREST_NEIGHBOR );
        transform.filter( srcImage, destImage );
//        if( logger.isDebugEnabled() ) logger.debug( destImage.toString() );
        imageIcon  = new javax.swing.ImageIcon( destImage );

        imageLabel.setIcon( imageIcon );
        imageScrollPane.setViewportView( imageLabel );        
        repaint();
    }
    //--------------------------------------------------------------------------
    //
    //--------------------------------------------------------------------------
    private void initComponents()
    {
        imageIcon       = new javax.swing.ImageIcon( destImage );
        imageLabel      = new javax.swing.JLabel( imageIcon );
        zoomPanel       = new javax.swing.JPanel();
        imageScrollPane = new javax.swing.JScrollPane();
        zoomOutButton   = new javax.swing.JButton();
        zoomInButton    = new javax.swing.JButton();
        saveButton      = new javax.swing.JButton();

        setLayout( new java.awt.BorderLayout() );
        imageScrollPane.setViewportView( imageLabel );
        add( imageScrollPane, java.awt.BorderLayout.CENTER);
        zoomOutButton.setText( ZOOM_OUT_LABEL );
        zoomOutButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                zoomOutButtonActionPerformed(evt);
            }
        });
        zoomPanel.add( zoomOutButton );
        zoomInButton.setText( ZOOM_IN_LABEL );
        zoomInButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                zoomInButtonActionPerformed(evt);
            }
        });
        zoomPanel.add( zoomInButton );
/*
 * the Applet can't save the image
 *
        // save image        
        saveButton.setText( SAVE_LABEL );
        saveButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                saveButtonActionPerformed(evt);
            }
        });
        zoomPanel.add( saveButton );
*/        
        add( zoomPanel, java.awt.BorderLayout.SOUTH );
    }
    //--------------------------------------------------------------------------
    //
    //--------------------------------------------------------------------------
    private void zoomInButtonActionPerformed( java.awt.event.ActionEvent evt )
    {
        zoomFactor *= 1.33;
        t.setToScale( zoomFactor, zoomFactor );
//        logger.debug( "SrcImageType: " + srcImage.getType() );
        destImage = new java.awt.image.BufferedImage( (int)( srcImage.getWidth() * zoomFactor ), (int)( srcImage.getHeight() * zoomFactor ), srcImage.getType() );
        transform = new java.awt.image.AffineTransformOp( t, java.awt.image.AffineTransformOp.TYPE_NEAREST_NEIGHBOR );
        transform.filter( srcImage, destImage );
        imageIcon  = new javax.swing.ImageIcon( destImage );
        imageLabel.setIcon( imageIcon );
        imageScrollPane.setViewportView( imageLabel );
        
        // logger.debug( "x: " + destImage.getWidth() + " y: " + destImage.getHeight() );
        repaint();
    }
    //--------------------------------------------------------------------------
    //
    //--------------------------------------------------------------------------
    private void zoomOutButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        zoomFactor *= 0.75;
        t.setToScale( zoomFactor, zoomFactor );
//        logger.debug( "SrcImageType: " + srcImage.getType() );
        destImage = new java.awt.image.BufferedImage( (int)( srcImage.getWidth() * zoomFactor ), (int)( srcImage.getHeight() * zoomFactor ), srcImage.getType() );
        transform = new java.awt.image.AffineTransformOp( t, java.awt.image.AffineTransformOp.TYPE_NEAREST_NEIGHBOR );
        transform.filter( srcImage, destImage );
        imageIcon  = new javax.swing.ImageIcon( destImage );
        imageLabel.setIcon( imageIcon );
        imageScrollPane.setViewportView( imageLabel );
        
        // logger.debug( "x: " + destImage.getWidth() + " y: " + destImage.getHeight() );
        repaint();
    }
    //--------------------------------------------------------------------------
    // Save image on local disk
    //--------------------------------------------------------------------------
    public class ImageFileFilter extends javax.swing.filechooser.FileFilter
    {
        public final static String png = ".png";    
        ImageFileFilter()
        {
        }
        // Accept all directories and all xml files.        
        public boolean accept(java.io.File file)
        {
            if( file.isDirectory() ) 
            {
                return true;
            }
            if( file.getName().endsWith( png ) ) 
            {
                return true;
            } 
            else
            {    
                return false;
            }    
        }    
        // The description of this filter
        public String getDescription() 
        {
            return "png image format";
        }
    };

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
//        logger.debug( "Save image: " + srcImage.getType() );
        // get output filename
        java.io.File file;            
        javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
        fc.setDialogType( javax.swing.JFileChooser.SAVE_DIALOG );
        fc.setDialogTitle( "Save image" );
        fc.setFileSelectionMode( javax.swing.JFileChooser.FILES_ONLY );
                                
        ImageFileFilter filter = new ImageFileFilter();
	fc.setFileFilter( filter );
        
        int ret = fc.showSaveDialog( this );
        if( ret == javax.swing.JFileChooser.APPROVE_OPTION ) 
        {
            String fullpath = fc.getSelectedFile().getPath();
//            logger.info( "Save image: " + fullpath +" ..." );
            try
            {
                javax.imageio.ImageIO.write( destImage, "png", fc.getSelectedFile() );
            }
            catch( java.io.IOException ex )
            {
                
//                logger.error( ex.getMessage(), ex.fillInStackTrace() );            
            }
//            logger.info( "finish" );
        }				        
	return;
    }
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt)
    {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        org.apache.log4j.BasicConfigurator.configure();
//        logger.setLevel( Level.DEBUG );
        ImageViewer iv = null;
        try
        {
            // iv = new ImageViewer( new java.net.URL( "http://localhost/icons/grass-map.jpg") );            
            
            String file = "./dn-cvs-096/dncode/net/metadiversity/diversity/navigator/images/dn.gif";
            // iv = new ImageViewer( new java.net.URL( "file:" + file ) );
            
            iv = new ImageViewer( new java.net.URL( "http://geo.cbs.umn.edu/treekey/images/AlnusviridisLamvarcrispa.gif" ) );
            /*
            */
        }    
        catch( java.net.MalformedURLException ex )
        {
  //          logger.error( ex.getMessage(), ex.fillInStackTrace() );
        }
            javax.swing.JFrame frame = new javax.swing.JFrame();
            frame.getContentPane().add( iv, java.awt.BorderLayout.CENTER );
            frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
            frame.setSize( 500, 100 );
            frame.pack();
            frame.setVisible( true );
            
        frame.addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
//                frame.exitForm(evt);
            }
        });
    }
}
