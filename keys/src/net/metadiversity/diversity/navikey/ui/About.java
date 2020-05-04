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
 * About.java
 *
 * Created on August 21, 2006, 9:18 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.metadiversity.diversity.navikey.ui;

/**
 *
 * @date   8/21/2006
 * @author Dieter Neubacher
 */
public class About extends javax.swing.JPanel
{
    public final static String VERSION = "v. 5.01";
    public final static String BUILD_DATE = "2008-06-25";
    public final static String FONT_COLOR = "000000";
    
    /** Creates a new instance of About */
    public About() 
    {
        setLayout( new java.awt.BorderLayout() );
        
        String info = "<html>" +
/* !!! Didi: Can't change the font-family. !!!         
*        			  "<span style=\"font-family:'Times New Roman'\">" + 
*            		  "<b><h1>NaviKey " + VERSION + "</h1></b> " +
*                      "</span>" +
*
*       			  "<span style=\"font-family:'Arial'\">" + 
*
* Working:
*         			 "<p style='font-family:Arial; font-size:15px; color:#ff0000;'>1. Zeile<br>2. Zeile</p>" +
*/                      
        			 "<body style='font-family:Arial; font-size:12px; color:#000000;'>" +
        
            		  "<b><h1>NaviKey " + VERSION + "</h1></b> " +
                      "(<a href=\"http://www.gnu.org/licenses/licenses.html\">" + 
                      "GNU General Public Licence<a>)" +
                     "</p>" + 
         			 "<p>" +
                     BUILD_DATE + 
                     "</p>" + 
         			 "<p>" +
                      "This software is based on an earlier version (NaviKey v. 2.3 by Michael Bartley and Noel Cross, Harvard University Herbarium, Boston, USA) and has been further developed in the frame of the " +
                      "BIOTA Africa (<u>http://www.biota-africa.de</u>) project by Dieter Neubacher and Gerhard Rambold (University of Bayreuth, Germany). Email any comments about the program to navikey@uni-bayreuth.de." + 
                      
                      "<p>NaviKey " + VERSION + " is a tool for querying descriptive data and has been implemented as" +
                      "<ul>" + 
                      "<li>a Java applet providing the contents of " +
                        "DELTA (<u>http://www.delta-intkey.com</u>) flat files;" + 
                      "<li>a stand-alone Java application for accessing " +
                        "DELTA flat files or " +
                      //  "<i>DiversityDescriptions</i> (<u>http://www.diversitycampus.net/Workbench/download.html</u>) PostgreSQL databases, respectively;" +
                        "<i>DiversityDescriptions</i> (<u>http://www.diversityworkbench.net</u>) PostgreSQL databases, respectively;" +
                      "<li>a module (\"plug-in\") of the " +
                        "<i>Diversity Navigator</i> (<u>http://www.diversitynavigator.net</u>) Java database client for accessing " +
                        "DELTA flat files or " +
                        "<i>DiversityDescriptions</i> PostgreSQL databases, respectively." +
                      "</ul>" +
                      "Regular version updates of the NaviKey applet and stand-alone NaviKey application are provided on the " +
                        "NaviKey Home site (<u>http://www.navikey.net</u>)" +
                      "<p>" +    
                      "The user interface contains four main panels:" +
                      "<ul>" + 
                      "<li>top left - Character panel: selection of the characters;" + 
                      "<li>top right - Character states or numeric data panel: preselection of states of the selected character; for selection of more than one character state press CTRL plus character state; to finish the query, use the &lt;Select&gt; button;" + 
                      "<li>bottom left - Query criteria panel: display of previous character state selections;" + 
                      "<li>bottom right - Matching items panel: display of matching items (selecting any item will bring up a complete description of that item).</ul>" + 
                      "<p>NaviKey checkbox matching options are:<ul>" + 
                      "<li><b>Use \"Best\" algorithm</b><br>To be implemented.</li>" + 
                      "<li><b>Use character dependencies</b><br>"  +
                      "When checked, controlling character states make dependent characters - as  defined in the database - inapplicable." +
                      "</li>" +
                      "<li><b>Restrict view on used characters and character states of remaining items</b>" +
                       "<br>When checked, NaviKey only displays those characters and states (in the two top main panels) that will reduce the number of item matches; characters or states which are not applicable to the remaining items (in the bottom right main panel) or apply to all of the items in the same form are no longer displayed. If a character is selected, for which more than one state applies, multiple state selection may be performed using the &lt;Ctrl&gt; (or corresponding) button (see also \"Enable multiple selection of character states\"). " +
                       "In the case of numeric characters, the top right panel provides fields for input of numeric values. After the input of either a single measurement or a range of values, the selection of the &lt;Add&gt; button will finalize the query.</li>" +

                // "<li><b>Exclude items with undefined states in selected characters</b>" +
                "<li><b>Retain items unrecorded for the selected characters</b>" +

                      "<br>Checking the box enables the \"Direct identification\" mode with the undefined states included; i.e., NaviKey also displays those items (in the bottom right panel), for which the selected character(s) have (so far) not been specified in the database. When unchecked, it only displays the items with at least one of the selected characters being specified. This behaviour largely corresponds with the \"analysis/data retrieval mode\" in CSIRO-DELTA Intkey and in the Identify module of <i>DiversityDescriptions</i></li>" +

                // "<li><b>Enable multiple selection of character states (with OR)</b>" +
                "<li><b>Retain items matching at least one selected state of resp. characters</b>" +
                      
                      "<br>When checked, NaviKey selects those items matching by at least one character state of the selected characters, otherwise only those items matching directly the selected combination of character states.</li>" +
                
                      "<li><b>Use extreme interval validation</b><br>When checked, the whole range between the extreme values (if present in the item data set) is used in numerical queries, otherwise only the range between minimum and maximum values (if present in the item data set) is considered.</li>" +
                      "<li><b>Use overlapping interval validation</b>" +
                       "<br>When checked, overlapping between queried and the specified numerical intervals is matching, otherwise not.</li></ul>" +
                       "</p>" + 
                      "</body>" + 
                      "</html>";  
        
        javax.swing.JEditorPane ta;
        ta = new javax.swing.JEditorPane( "text/html", info );
        ta.setEditable( false );
        ta.setMargin( new java.awt.Insets( 10,10,10,10) );
        ta.setCaretPosition( 0 ); // Moveto first Position
        javax.swing.JScrollPane sp = new javax.swing.JScrollPane( ta );
        sp.setHorizontalScrollBarPolicy( javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
                
        add( java.awt.BorderLayout.CENTER, sp );        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        // TODO code application logic here
        
        About a = new About();
        
        javax.swing.JFrame f = new javax.swing.JFrame();
        
        f.getContentPane().add( a );
        f.setVisible( true );
    }
    
}
