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

import java.applet.AppletContext;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;

import net.metadiversity.diversity.navikey.util.SearchEntry;
import net.metadiversity.diversity.navikey.util.NavikeyPopupSearchItem;
// XML Configuration

import java.io.IOException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import javax.xml.xpath.*;


import net.metadiversity.diversity.navikey.bo.DeltaInterface;
import net.metadiversity.diversity.navikey.bo.Resource;
import net.metadiversity.diversity.navikey.delta.DeltaConnector;
import net.metadiversity.diversity.navikey.delta.LocalDeltaConnector;
import net.metadiversity.diversity.navikey.delta.DnDatabaseConnector;
import net.metadiversity.diversity.navikey.servlet.HttpDeltaConnector;


/**
 *
 * NaviKey.java
 *
 * @author Michael Bartley
 * 3/25/98
 * @author Dieter Neubacher
 */

public class NaviKey extends javax.swing.JPanel
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3762532317360829234L;

    
    java.util.HashMap< String, java.util.HashMap< String, String >> itemMappingData;
    
    //public static String imageLocation;
        
    private boolean useTabSheets = false;    
    javax.swing.JTabbedPane tabbedPane;

    SuperPanel          superPanel;
    ConfigurationPanel  cp;
    javax.swing.JLabel  navikeyIcon = null;    
    DeltaInterface      delta;
    
    //These will be adjusted by user, and used by all subpanels as selections
    static Color naviKeyBackgroundColor = Color.black;
    static Color itemCharacterColor     = Color.cyan;
    static Color itemStateColor         = Color.magenta;
    static Color itemSelectionsColor    = Color.green;
    static Color itemListColor          = Color.yellow;
    static Color buttonColor            = Color.pink;
    static Color boxBGColor             = Color.white;
    static Color boxFGColor             = Color.black;
    static Color itemDisplayColor       = Color.cyan;
    static Color itemDisplayTextColor   = Color.white;
    static Color itemDisplayBGColor     = Color.darkGray;
    static Color itemDisplayFGColor     = Color.green;
    static Color imageBGColor           = Color.black;
    static Color imageFGColor           = Color.white;
    
    // Popup Menue Item Color for Imageselection
    
    private Color UriLocalAvailableColor  = java.awt.Color.GREEN;
    private Color UriRemoteAvailableColor = java.awt.Color.ORANGE;
    private Color UriNotAvailableColor    = java.awt.Color.LIGHT_GRAY;
        
    
    private boolean standAlone = true;
    // DiversityNavigator
    private boolean useDnDatabaseConnector = false;
    private String specsString = null;
    private String charsString = null;
    private String itemsString = null;
    private String tImagesString = null;
    
    
    private String specsFile = null;
    private String charsFile = null;
    private String itemsFile = null;
    private String tImagesFile = null;
    private String imageDirectory=null;
    //
    // Options
    //
    private boolean useBestAlgorithm        = false; // to be implemented       

    //
    private boolean intelligence          = true;
    private boolean excludeUndefined      = true;
    private boolean multipleSelectionOR   = true;
    private boolean extremeInterval       = true;
    private boolean overlappingInterval   = true;
    public  boolean dependenciesFlag    = true; // Default
    // Remove Comments wenn readin data files.
    public  boolean removeCharsCommentFlag = false;
    public  boolean removeItemsCommentFlag = false;
    public  boolean removeSpecsCommentFlag = false;
    //
    // Remove StatesComments wenn readin data files.
    public  boolean removeCharsStatesCommentFlag = false;
    public  boolean removeItemsStatesCommentFlag = false;
    public  boolean removeSpecsStatesCommentFlag = false;
    // Show LIAS units with []
    public  boolean showLiasUnitsFlag   = false;
    // Dependencies
    //
    // Display all and found ItemCount
    // Will be displayed on the configurationPanel
    //
    private long allItemCount;
    private long foundItemCount;
    //
    //
    private String servletURL=null;
    
    private java.lang.String        configFileName = null;
    private java.net.URL            codebase         = null;
    private java.awt.Container      contentPane      = null;
    
    
    private Resource resource;
    
    
    
    //
    // Popup Menus
    // 
    // net.metadiversity.diversity.navikey.util.SearchEntry
            
    java.util.ArrayList< SearchEntry > itemsPanelSearchPopupsList = new java.util.ArrayList<SearchEntry>();
            
    javax.swing.JPopupMenu itemsPanelPopupMenu = null;

    String searchString = null;

    java.applet.AppletContext navikeyAppletContext = null;
            
    public void setAppletContext( AppletContext appletContext ) 
    {
        navikeyAppletContext = appletContext;
    }
    
    private void readXmlConfig( java.net.URI uri )
    {
        itemMappingData = new java.util.HashMap< String, java.util.HashMap< String, String >>();
        
        try 
        {
            /*
            java.lang.String navikeyConfigFileName = "NaviKeyConfig.xml";
            // get ConfigFileName from configuration
            java.net.URI uri = new java.net.URI( getCodeBase().toString() + navikeyConfigFileName );
             */
            javax.xml.xpath.XPathFactory    factory;
            javax.xml.xpath.XPath           xPath;
            XPathExpression                 expr;
/*
            //
            // DOM
            //
            java.lang.System.out.println( "-----------------------------------------------" );
            java.lang.System.out.println( "------------- D O M ---------------------------" );
            java.lang.System.out.println( "-----------------------------------------------" );
*/                 
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true); // never forget this!
            DocumentBuilder builder = domFactory.newDocumentBuilder();
//            Document doc = builder.parse( navikeySearchConfigFileName );
//            java.lang.System.out.println( "URI: " + uri.toString() );
            Document doc = builder.parse( uri.toString() );

            factory = javax.xml.xpath.XPathFactory.newInstance();
            xPath = factory.newXPath();
            
            //------------------------------------------------------------------
            // Read all configuration values
            //------------------------------------------------------------------
            java.lang.System.out.println( "NaviKey configuration: " );

            String data = null;
            String search = null;
            //
            // undefinied in configuration: (Test)
            //
            /*
            search = "/Test1/Test2/Test3";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            */
            //
            //
            search = "/Config/Standalone";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            // Default value is "false"
            if( data.equals( "true" ) ) {  this.standAlone = true; } else { this.standAlone = false; }
            //
            //
            //
            search = "/Config/ServletURL";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            this.servletURL = data;
            
            // Config/Display

            search = "/Config/Display/UseGuiTabs";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            // Default value is "false"
            if( data.equals( "true" ) ) {  useTabSheets = true; } else { useTabSheets  = false; }


            search = "/Config/Display/ShowUnits";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            // Default value is "false"
            if( data.equals( "true" ) ) {  this.showLiasUnitsFlag = true; } else { this.showLiasUnitsFlag = false; }


            search = "/Config/Display/RemoveCharsComments";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            // Default value is "false"
            if( data.equals( "true" ) ) {  this.removeCharsCommentFlag = true; } else { this.removeCharsCommentFlag = false; }



            search = "/Config/Display/RemoveCharsStatesComments";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            // Default value is "false"
            if( data.equals( "true" ) ) {  this.removeCharsStatesCommentFlag = true; } else { this.removeCharsStatesCommentFlag = false; }


            search = "/Config/Display/RemoveItemsComments";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            // Default value is "false"
            if( data.equals( "true" ) ) {  this.removeItemsCommentFlag = true; } else { this.removeItemsCommentFlag = false; }


            search = "/Config/Display/RemoveItemsStatesComments";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            // Default value is "false"
            if( data.equals( "true" ) ) {  this.removeItemsStatesCommentFlag = true; } else { this.removeItemsStatesCommentFlag = false; }


            search = "/Config/Display/RemoveSpecsComments";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            // Default value is "false"
            if( data.equals( "true" ) ) {  this.removeSpecsCommentFlag = true; } else { this.removeSpecsCommentFlag = false; }


            search = "/Config/Display/RemoveSpecsStatesComments";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            // Default value is "false"
            if( data.equals( "true" ) ) {  this.removeSpecsStatesCommentFlag = true; } else { this.removeSpecsStatesCommentFlag = false; }

            // Display Popup Color
            
            java.awt.Color c = null;                    
            
            search = "/Config/Display/PopUpMenu/URILocalAvailableColor";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);            
            try
            {
                    c =  new java.awt.Color( java.lang.Integer.decode( data ).intValue() );
                    UriLocalAvailableColor = c;
            }
            catch( NumberFormatException ex )
            {
                java.lang.System.out.println("Error in: Data ( " + search + " ): " + data);
            }
            search = "/Config/Display/PopUpMenu/URIRemoteAvailableColor";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);            
            try
            {
                    c =  new java.awt.Color( java.lang.Integer.decode( data ).intValue() );
                    UriRemoteAvailableColor = c;
            }
            catch( NumberFormatException ex )
            {
                java.lang.System.out.println("Error in: Data ( " + search + " ): " + data);
            }
            search = "/Config/Display/PopUpMenu/URINotAvailableColor";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);            
            try
            {
                    c =  new java.awt.Color( java.lang.Integer.decode( data ).intValue() );
                    UriNotAvailableColor = c;
            }
            catch( NumberFormatException ex )
            {
                java.lang.System.out.println("Error in: Data ( " + search + " ): " + data);
            }
                    
                    
            
            // MatchingOptions
            
            search = "/Config/MatchingOptions/UseBestAlgorithm";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            // Default value is "false"
            if( data.equals( "true" ) ) {  this.useBestAlgorithm = true; } else { this.useBestAlgorithm = false; }

                                              
            search = "/Config/MatchingOptions/UseCharacterDependencies";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            // Default value is "false"
           if( data.equals( "true" ) ) {  this.dependenciesFlag = true; } else { this.dependenciesFlag = false; }

            
            search = "/Config/MatchingOptions/RestrictViewOnUsedCharactersAndCharacterStatesOfRemainingItems";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            // Default value is "false"
            if( data.equals( "true" ) ) {  this.intelligence = true; } else { this.intelligence = false; }

            
            search = "/Config/MatchingOptions/RetainItemsUnrecordedForTheSelectedCharacters";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            // Default value is "false"
            if( data.equals( "true" ) ) {  this.excludeUndefined = false; } else { this.excludeUndefined = true; }

            
            search = "/Config/MatchingOptions/RetainItemsMatchingAtLeastOneSelectedState";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            // Default value is "false"
            if( data.equals( "true" ) ) {  this.multipleSelectionOR = true; } else { this.multipleSelectionOR = false; }

            
            search = "/Config/MatchingOptions/UseExtremeIntervalValidation";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            // Default value is "false"
            if( data.equals( "true" ) ) {  this.extremeInterval = true; } else { this.extremeInterval = false; }

            
            search = "/Config/MatchingOptions/UseOverlappingIntervalValidation";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            // Default value is "false"
            if( data.equals( "true" ) ) {  this.overlappingInterval = true; } else { this.overlappingInterval = false; }

            
            // DataFiles
            
            search = "/Config/DataFiles/SpecsFileName";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            this.specsFile = data;

            
            search = "/Config/DataFiles/CharsFileName";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            this.charsFile = data;

            
            search = "/Config/DataFiles/ItemsFileName";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            this.itemsFile = data;

            
            search = "/Config/DataFiles/TimagesFileName";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            this.tImagesFile = data;

            
            search = "/Config/DataFiles/ImageDirectory";            
            data = xPath.compile( search ).evaluate( doc );
            java.lang.System.out.println("Data ( " + search + " ): " + data);
            this.imageDirectory = data;




            //
            //
            search = "/Config/Display/showLiasUnits";            
            //inputSource = new org.xml.sax.InputSource( new java.io.FileInputStream( xmlDocument ) );
            expr = xPath.compile( search );
            data = expr.evaluate( doc );            
            java.lang.System.out.println("Data ( " + search + " ): " + data);



            //------------------------

            expr = xPath.compile( "/Config/ItemURIs/*/child::text()");
            Object result = expr.evaluate( doc, javax.xml.xpath.XPathConstants.NODESET );
            NodeList nodes = (NodeList) result;

                    
            int n = 0;
            for( int i = 0;  i < nodes.getLength(); ) 
            {
                SearchEntry se = new SearchEntry();
                // System.out.println( nodes.item(i).getNodeName() + " " + nodes.item(i).getNodeValue() ); 
                se.setGuiString( nodes.item(i++).getNodeValue() );
                se.setURI( nodes.item(i++).getNodeValue() );
                se.setMappingFlag( nodes.item(i++).getNodeValue() );
                se.setMappingFile( nodes.item(i++).getNodeValue() );
                se.setRegex( nodes.item(i++).getNodeValue() );
                
                itemsPanelSearchPopupsList.add( n++, se  );
                
                // init mapping data
                
                if( se.isMappingFlag() == true )
                {
                   java.util.HashMap< String, String > hm = new java.util.HashMap< String, String >(); 
                   itemMappingData.put( se.getGuiString(),  hm );
                   readItemMapping(     se.getMappingFile(), hm );
                }    
            }
        } 
        catch (XPathExpressionException ex) 
        {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) 
        {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) 
        {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        }
        catch (ParserConfigurationException ex) 
        {  
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        }
    }
    
    private void initializeParameters()
    {
        BufferedReader bufferdReader = null;
        String props = null;

        String xmlConfigFile = "NaviKeyConfig.xml";  // Default file name

        try
        {
            
//            if( ! useDnDatabaseConnector )
            {
                /* old: now use XML configuration
                 * 
                props = getParameter( "propsfile" );
                URL url = new URL( getCodeBase().toString() + props ); 1
                
                // JAVA 1.4: can't load the file "file://..." 
                // ERROR sun.net.ftp.FtpLoginException: Not logged in                
                        
                System.out.println( "URL: " + url );
                System.out.println( "Protocol: " + url.getProtocol() );
                
                boolean useUrlFlag = ! url.getProtocol().equalsIgnoreCase( "FILE" );
                
                if( useUrlFlag )
                {                                    
                    bufferdReader = new BufferedReader( new java.io.InputStreamReader( url.openStream() ) );
                }
                else
                {
                    String filename = getCodeBase().toString() + props;
                    filename = filename.substring( filename.indexOf( ":/") + 2 );
                    bufferdReader = new BufferedReader( new java.io.InputStreamReader( new java.io.FileInputStream( filename ) ) );
                }
                
                 * 
                 */
                
                // XML Configuration

                xmlConfigFile = getParameter( "configfile" );
                java.net.URI uri = new java.net.URI( getCodeBase().toString() + xmlConfigFile );

                System.out.println( "Config file URI: " + uri );
//                Logger.getLogger("global").log(Level.SEVERE, null, "URI: " + uri );
                
                readXmlConfig( uri );

            }
        }
        catch( Exception e )
        {            
            System.out.println( "Can't read configfile: " + xmlConfigFile );
//            System.out.println( "Can't read propsfile: " + getCodeBase().toString() + props );
//            System.out.println( "NaviKey: No color scheme specified, using defaults" );
            standAlone = true;  // Run as Application
            return;
        }
  
        return;

    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /**
     * NaviKey ( Plugin for DiversityNavigator )
     */
    public NaviKey( java.awt.Container contentPane, String specsString, String charsString, String itemsString )
    {   
        this.specsString = specsString;
        this.charsString = charsString;
        this.itemsString = itemsString;
        useDnDatabaseConnector = true;
        configFileName = "NaviKeyConfig.xml";
        this.contentPane = contentPane;
               
        try
        {
                String path = System.getProperty( "user.home" );                                        	
        	path = "FILE:///" + path + "/.diversity_navigator/";	// Windows: working directory        	
        	this.codebase = new URL( path );
        }
        catch( java.net.MalformedURLException ex )
        {
        	System.out.println( "NavikeyApplication: ERROR can't get CodeBase" );
                ex.printStackTrace();
                
                this.codebase = null;
        }          
    }
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public NaviKey( java.awt.Container contentPane, String configfile, URL codebase )
    {
        configFileName   = configfile;
        this.codebase    = codebase;
        this.contentPane = contentPane;
        
        // ??? resource = new Resource();
    }            
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private URL getCodeBase()
    {
        System.out.println( "getCodeBase() : " + codebase );
        return codebase;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    String getParameter( String str )
    {
        System.out.println( "getParam( " + str +" )" );
        if( str.equals( "configfile" ) )
        {
            return configFileName;
        }
        else
        {
            return null;
        }
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    java.awt.Container getContentPane()
    {
        // System.out.println( "getContentPane() : " + contentPane );
        return contentPane;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    public void init()
    {
        getContentPane().setLayout( new BorderLayout() );
        setBorder( javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10) );
        
        initializeParameters();

        DeltaConnector dc;
        
        System.out.println( "init()" );
        
        if( imageDirectory != null )
        {
            /* ???
            try
            {
                resource.defaultLocation = getCodeBase().toString() + imageDirectory +"/";
            }
            catch( Exception e )
            {
                System.out.println( "NaviKey: Unable to get code base. " + e );
            }
             */
        }
        if( useDnDatabaseConnector )
        {
            dc = runDnDatabaseConnector();
        }
        else if( standAlone )
        {
            dc = runStandAlone();
        }
        else
        {
            dc = runHttpConnector();
        }

        // Configuration Panel
        cp = new ConfigurationPanel( this, useTabSheets );

        //----------------------------------------------------------------------        
        // setup PopupMenus
        //----------------------------------------------------------------------
        createItemsPanelPopupMenu();

        //
        delta = dc.getObject();
        superPanel = new SuperPanel( this, delta );
        // Item Character Panel
        System.out.println( "Setting panels" );
        superPanel.itemCharacterPanel.setItemCharacters( delta.getItemCharacterList( null, this.isIntelligence(), this.isDependencies() ) );
        superPanel.itemCharacterPanel.updateList();

        System.out.println( "setting layouts" );
        cp.addNavikeyGuiListener( superPanel.getNavikeyGuiListener( "Options" ) );
        // init options
        java.awt.event.ActionEvent ae = new java.awt.event.ActionEvent( this, 1, "OptionsChanged" );        
        superPanel.getNavikeyGuiListener( "Options" ).actionPerformed( ae );        
                
        // Item Panel
        superPanel.itemsPanel.setItems( delta.getItemList( null ) );
        superPanel.itemsPanel.updateList();
        System.out.println( "Composing all" );
        superPanel.build();
        // State Panel
        (( java.awt.CardLayout )superPanel.statePanel.getLayout()).first( superPanel.statePanel );        
        superPanel.setSize( new java.awt.Dimension( 400, 640 ) );

        if( useTabSheets )
        {
            tabbedPane = new javax.swing.JTabbedPane();
            tabbedPane.setBorder( javax.swing.BorderFactory.createEmptyBorder( 3, 3, 3, 3 ) );

            tabbedPane.add( "Identification", superPanel  );
            
            tabbedPane.add( "Options", cp );
            tabbedPane.add( "About", new About() );
                    

            getContentPane().add( java.awt.BorderLayout.CENTER, tabbedPane );
        }
        else
        {
            getContentPane().add( java.awt.BorderLayout.SOUTH, cp );
            // 
            getContentPane().add( java.awt.BorderLayout.CENTER, superPanel );
            // setup Navikey icon
            try
            {
                navikeyIcon = new javax.swing.JLabel( new javax.swing.ImageIcon( new java.net.URL( getCodeBase() + "images/nlogo.gif" ) ) );
                getContentPane().add( java.awt.BorderLayout.NORTH, navikeyIcon );
            }        
            catch( java.net.MalformedURLException ex )
            {
                System.out.println( "NaviKey: image file not available" + ex );
            }
        }
        this.setVisible( true );
        System.out.println( "NaviKey: Done with init" );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void showResult( String title, javax.swing.JPanel p, String image )
    {
        if( tabbedPane != null )
        {
            int tabCount = tabbedPane.getTabCount(); 
            // remove images            
            while( tabCount > 3 )
            {
                tabbedPane.remove( 3 );
                tabCount--;
            }    
            tabbedPane.add( title, p );
            tabbedPane.setSelectedIndex( 3 );
            
            // Add image tabs

            ImageViewer iv;
                       
            try 
            {
                if( image != null )
                {    
                    iv = new ImageViewer(new java.net.URL( getCodeBase() + "/"  + imageDirectory + "/" + image ));                    
                    tabbedPane.add( "Image", iv );
                }    
            } 
            catch (MalformedURLException ex) 
            {
                System.out.println( ex );
                ex.printStackTrace();
            }            
            
        }
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private LocalDeltaConnector runStandAlone()
    {
        if( specsFile == null || charsFile == null || itemsFile == null )
        {
            System.out.println( "NaviKey: Required files not specified." );
            System.out.println( "NaviKey: use default filenames: chars, specs, items, timages." );

            specsFile = "specs";
            charsFile = "chars";
            itemsFile = "items";
            tImagesFile = "timages";
        }
        try
        {
            URL base = getCodeBase();
            String url = base.toString();
            System.out.println( "Printme " );
            return new LocalDeltaConnector( this, url, specsFile, charsFile, itemsFile, tImagesFile );
        }
        catch( Exception e )
        {
            System.out.println( "NaviKey: Error reading delta files.\n" +e );
            System.exit( 0 );
            return new LocalDeltaConnector( this );
        }
    }    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private HttpDeltaConnector runHttpConnector()
    {
        //return new HttpDeltaConnector( "url goes here" );
        return new HttpDeltaConnector( servletURL );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private DnDatabaseConnector runDnDatabaseConnector()
    {
        return new DnDatabaseConnector( this, specsString, charsString, itemsString, tImagesString );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /*
    public void unused_start()
    {
        System.out.println( superPanel.getBounds() );
        
        cp.reshape( cp.getBounds().x, superPanel.getBounds().height, getBounds().width, 35 );
        cp.setBackground( naviKeyBackgroundColor );
        if( naviKeyBackgroundColor.equals( Color.black ) )
        {
            cp.setForeground( Color.white );
        }
        else
        {
            cp.setForeground( Color.black );
        }
        System.out.println( superPanel.getBounds() );
        repaint();
    } 
     */       
    /**
     * @return Returns the excludeUndefined.
     */
    public boolean isExcludeUndefined()
    {
        return excludeUndefined;
    }
    /**
     * @param excludeUndefined The excludeUndefined to set.
     */
    public void setExcludeUndefined( boolean excludeUndefined )
    {
        this.excludeUndefined = excludeUndefined;
    }
    /**
     * @return Returns the extremeInterval.
     */
    public boolean isExtremeInterval()
    {
        return extremeInterval;
    }
    /**
     * @param extremeInterval The extremeInterval to set.
     */
    public void setExtremeInterval( boolean extremeInterval )
    {
        this.extremeInterval = extremeInterval;
    }
    /**
     * @return Returns the intelligence.
     */
    public boolean isIntelligence()
    {
        return intelligence;
    }
    /**
     * @param intelligence The intelligence to set.
     */
    public void setIntelligence( boolean intelligence )
    {
        this.intelligence = intelligence;
    }        
    /**
     * @return Returns the dependenciesFlag.
     */
    public boolean isDependencies()
    {
        return dependenciesFlag;
    }
    /**
     * @param dependenciesFlag The dependenciesFlag to set.
     */
    public void setDependencies( boolean dependenciesFlag )
    {
        this.dependenciesFlag = dependenciesFlag;
    }
    
    /**
     * @return Returns the multipleSelectionOR.
     */
    public boolean isMultipleSelectionOR()
    {
        return multipleSelectionOR;
    }
    /**
     * @param multipleSelectionOR The multipleSelectionOR to set.
     */
    public void setMultipleSelectionOR( boolean multipleSelectionOR )
    {
        this.multipleSelectionOR = multipleSelectionOR;
    }
    /**
     * @return Returns the overlappingInterval.
     */
    public boolean isOverlappingInterval()
    {
        return overlappingInterval;
    }
    /**
     * @param overlappingInterval The overlappingInterval to set.
     */
    public void setOverlappingInterval( boolean overlappingInterval )
    {
        this.overlappingInterval = overlappingInterval;
    }
    
    public boolean isRemoveCharsCommentFlag()
    {
        return removeCharsCommentFlag;        
    }
    public boolean isRemoveItemsCommentFlag()
    {
        return removeItemsCommentFlag;        
    }
    public boolean isRemoveSpecsCommentFlag()
    {
        return removeSpecsCommentFlag;        
    }
    
    public boolean isRemoveCharsStatesCommentFlag()
    {
        return removeCharsStatesCommentFlag;        
    }
    public boolean isRemoveItemsStatesCommentFlag()
    {
        return removeItemsStatesCommentFlag;        
    }
    public boolean isRemoveSpecsStatesCommentFlag()
    {
        return removeSpecsStatesCommentFlag;        
    }
    public boolean isShowLiasUnitsFlag()
    {
        return showLiasUnitsFlag;
    }
    public boolean isUseTabSheetsFlag()
    {
        return useTabSheets;
    }
    /**
     * @return Returns the allItemCount.
     */
    public long getAllItemCount()
    {
        return allItemCount;
    }
    /**
     * @param allItemCount The allItemCount to set.
     */
    public void setAllItemCount( long allItemCount )
    {
        this.allItemCount = allItemCount;
        
        // Update the GUI
        /*
        java.awt.event.ActionEvent ae = new java.awt.event.ActionEvent( this, 1, "OptionsChanged" );        
        superPanel.getNavikeyGuiListener( "Options" ).actionPerformed( ae );                        
        */
        cp.setTotalLabelText( Long.toString( allItemCount ) );
    }
    /**
     * @return Returns the foundItemCount.
     */
    public long getFoundItemCount()
    {
        return foundItemCount;
    }
    /**
     * @param foundItemCount The foundItemCount to set.
     */
    public void setFoundItemCount( long foundItemCount )
    {
        this.foundItemCount = foundItemCount;
        
        // update the GUI
        /*
        java.awt.event.ActionEvent ae = new java.awt.event.ActionEvent( this, 1, "OptionsChanged" );        
        superPanel.getNavikeyGuiListener( "Options" ).actionPerformed( ae );     
        */
        cp.setResultLabelText( Long.toString( foundItemCount ) );
    }
/*
 * Create the PopUp Menue for the selection of extern links.
 * 
 */
    //----------------------------------------------------------------------
    //----------------------------------------------------------------------
    private final void createItemsPanelPopupMenu()
    {
        itemsPanelPopupMenu = new javax.swing.JPopupMenu();
        
        java.awt.event.MouseListener popupListener = new PopupListener();    
        
        javax.swing.JMenuItem pmi = null;
    
        for( int i = 0; i < itemsPanelSearchPopupsList.size(); i++ )
        {
            pmi = new net.metadiversity.diversity.navikey.util.NavikeyPopupSearchItem( itemsPanelSearchPopupsList.get( i ).getGuiString(), itemsPanelSearchPopupsList.get( i ).getURI(), itemsPanelSearchPopupsList.get( i ).isMappingFlag(), itemsPanelSearchPopupsList.get( i ).getRegex() );
            pmi.addMouseListener( popupListener );
            itemsPanelPopupMenu.add( pmi );            
        }    
    }
    //----------------------------------------------------------------------
    //----------------------------------------------------------------------
    public final javax.swing.JPopupMenu getItemsPanelPopupMenu()
    {   
        for( int i = 0; i < itemsPanelPopupMenu.getComponentCount() ; i++ )
        {    
            Object o = itemsPanelPopupMenu.getComponent( i );
            if( o.getClass().getName().equals( "net.metadiversity.diversity.navikey.util.NavikeyPopupSearchItem" ) )
            {    
                net.metadiversity.diversity.navikey.util.NavikeyPopupSearchItem ippm = (net.metadiversity.diversity.navikey.util.NavikeyPopupSearchItem) o;

                if( ippm.isMappingFlag() )
                {   
                    
                    String search = getTimageMapping( ippm.getGuiText(), regexResult( ippm.getRegex() ) );
                                    
                    if( search != null )
                    {
                        ippm.setBackground( UriLocalAvailableColor        );
                    }
                    else
                    {
                        ippm.setBackground( UriRemoteAvailableColor    );
                    }    
                }    
                else
                {   
                                        
                    ippm.setBackground( UriNotAvailableColor    );
                }
            }
        }
        itemsPanelPopupMenu.setVisible( true );

        return itemsPanelPopupMenu;           
    }               
    
    private String regexResult( String regex )
    {
         String resultSearchString = null;
        
            if( regex != null )
            {
                Pattern pattern = Pattern.compile( regex, Pattern.UNICODE_CASE ); 
                Matcher matcher = pattern.matcher( searchString );

                System.out.println( "String: " + searchString );
                if( matcher.find() ) 
                {
                    resultSearchString = matcher.group( 0 ).trim();
                    System.out.println( "Regex result : " + resultSearchString );
                }
            }
            
            return  resultSearchString;
    }
    
    //----------------------------------------------------------------------
    //----------------------------------------------------------------------
    public class PopupListener extends MouseAdapter 
    {
        public void mousePressed(MouseEvent e) 
        {
            itemsPanelPopupMenu.setVisible( false );
            
/*
            System.out.println( ((javax.swing.JMenuItem) e.getComponent().getComponentAt( e.getPoint() )).getText() );
            String s = ((javax.swing.JMenuItem) e.getComponent().getComponentAt( e.getPoint() )).getText();        

*/              
            System.out.println( ((NavikeyPopupSearchItem) e.getComponent().getComponentAt( e.getPoint() )).getText() );
            NavikeyPopupSearchItem npmi = (NavikeyPopupSearchItem) e.getComponent().getComponentAt( e.getPoint() );        

            String service = npmi.getURI();
            String s = npmi.getGuiText();
            String regex = npmi.getRegex();

            System.out.println( "GUI:    " + s );
            System.out.println( "SERVER: " + service );
            System.out.println( "REGEX:  " + regex );



            if( regex != null )
            {
                Pattern pattern = Pattern.compile( regex, Pattern.UNICODE_CASE ); 
                Matcher matcher = pattern.matcher( searchString );

                // boolean b = matcher.matches();

                System.out.println( "String: " + searchString );

//                while( matcher.find() ) 
                if( matcher.find() ) 
                {
/*
                    int i = matcher.groupCount();
                    System.out.println( "  groupCount(): " + i );        
                    for( int n = 0; n <= i; n++ )
                    {    
                        System.out.println( "  Result ( " + n + " ): " + matcher.group( n ) );
                    }    
*/
                    searchString = matcher.group( 0 ).trim();

                    System.out.println( "Regex result : " + searchString );
                }
            }

            if( npmi.isMappingFlag() ) 
            {           
                System.out.print( "Map: " + searchString );
                searchString = getTimageMapping( npmi.getGuiText(), searchString );
                System.out.println( " to " + searchString );

                if( searchString == null || searchString.length() <= 0 )
                {
                    return;
                }
            }
            if( searchString != null )
            {    
                search( service, searchString );
            }    
        }
    };
    //----------------------------------------------------------------------
    //----------------------------------------------------------------------
    void setSearchString(String selection) 
    {
        searchString = selection;
    }
    //----------------------------------------------------------------------
    //----------------------------------------------------------------------
    private void search( String search, String s )
    {    
        System.out.println( "Search " + search + " String: " + s );
        
        if( navikeyAppletContext != null )
        {
            try 
            {
                String replaceString = null;                
                replaceString = java.net.URLEncoder.encode( s, "UTF-8" );
                // ??? Replace BLANK's                
                replaceString = s.replace( " ", "%20" );
                search = search.replace( "[###]", replaceString );
//                search = java.net.URLEncoder.encode( search, "UTF-8" );
                System.out.println( search );                
                java.net.URI uri = new java.net.URI( search );
                
                navikeyAppletContext.showDocument( uri.toURL(), "_blank" );
            } 
            catch (IOException ex) 
            {
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
            }
            catch( URISyntaxException ex) 
            {
                Logger.getLogger("global").log( Level.SEVERE, null, ex) ;
            }
            
        }
        else
        
        if( java.awt.Desktop.isDesktopSupported() )
        {
            try 
            {
                java.awt.Desktop dt = java.awt.Desktop.getDesktop();

                String replaceString = null;
                
                replaceString = java.net.URLEncoder.encode( s, "UTF-8" );

                // ??? Replace BLANK's                
                replaceString = s.replace( " ", "%20" );

                search = search.replace( "[###]", replaceString );
//                search = java.net.URLEncoder.encode( search, "UTF-8" );
                System.out.println( search );

                
                java.net.URI uri = new java.net.URI( search );
                
                // dt.browse( uri.normalize() );                
                dt.browse( uri );                   
            } 
            catch (IOException ex) 
            {
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
            }
            catch( URISyntaxException ex) 
            {
                Logger.getLogger("global").log( Level.SEVERE, null, ex) ;
            }
        }    
   }
/*
 * #############################################################################
 * #############################################################################
 * 
 */
    
    //--------------------------------------------------------------------------
    // Read Timage Mapping file
    //--------------------------------------------------------------------------
        
    void readItemMapping( String mappingFile, java.util.HashMap< String, String > hashmap )
    {
        try 
        {

            java.net.URI uri = new java.net.URI( getCodeBase().toString() + mappingFile ); 

            
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true); // never forget this!
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            java.lang.System.out.println( "URI: " + uri.toString() );
            Document doc = builder.parse( uri.toString() );
            
            
           Element root = doc.getDocumentElement();
           NodeList nl = root.getChildNodes();
           // itterate over all "map" nodes 
           for( int i = 0; i < nl.getLength(); i++) 
           {
              Node n = nl.item(i);
              if( (n.getNodeType() == Node.ELEMENT_NODE) && (((Element)n).getTagName().equals( "map" )) ) 
              {
                 // we have an "map" node, now we need to find the 
                 // 'from' child
                 Node from = ((Element)n).getElementsByTagName( "from" ).item( 0 );
                 Node to   = ((Element)n).getElementsByTagName( "to" ).item( 0 );

                 String fromStr = ((Text) from.getFirstChild()).getData();
                 String toStr   = ((Text) to.getFirstChild()).getData();

                 System.out.println( fromStr + " " + toStr );

                 hashmap.put( fromStr, toStr);
              }
           }
        } 
        catch (URISyntaxException ex) 
        {
            Logger.getLogger("global").log(Level.SEVERE, "1", ex);
        }
        catch (SAXException ex) 
        {
            Logger.getLogger("global").log(Level.SEVERE, "2", ex);
        }
        catch (IOException ex) 
        {
            Logger.getLogger("global").log(Level.SEVERE, "3", ex);
        }
        catch (ParserConfigurationException ex) 
        {
            Logger.getLogger("global").log(Level.SEVERE, "4", ex);
        }
    }
    // ???    
    public String getTimageMapping( String GuiString, String s )
    {
        return itemMappingData.get( GuiString ).get( s );
    }
}
