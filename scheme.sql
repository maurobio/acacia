-- ------------------------------------------------------------
-- Description: Acacia: A Generic Conceptual Schema for Taxonomic Databases
-- Author: M. J. Cavalcanti <maurobio@gmail.com>
-- (c) copyright 2004-2021 Mauro J. Cavalcanti
-- Revision history:
-- Version 1.00, 13-Dec-2004:
--   * Initial release
-- Version 1.01, 26-Jun-2007:
--   * Added additional taxonomic levels (Kingdom, Phylum, Class, Order) to Higher Taxa table
-- Version 1.02, 12-Jul-2008:
--   * Added Descriptors dictionary and changed Descriptions table
--   * Added more taxonomic levels (Subphylum, Subclass, Suborder, Superfamily, Subfamily, Tribe) to Higher Taxa table
--   * Changed name of the Gazetteer table to Geography 
--   * Added Metadata table
-- Version 1.03, 3-Oct-2008:
--   * Added optional attributes (Locality, Latitude, Longitude) to Distribution table
-- Version 1.04, 27-Oct-2008:
--   * Added a new attribute (Title) to Bibliography table
--   * Attribute home in Metadata table renamed to url
--   * Added attributes Acronym and Logotype name to Metadata table
--   * Added attribute Type to Resources table
--   * Removed attribute Descriptor Number from Resources table
-- Version 1.05, 14-Nov-2008:
--   * Added attribute Sequence to Bibliography table
-- Version 1.06, 07-Apr-2010:
--   * Added attribute Subgenus to Synonyms and Taxa tables
-- Version 1.07, 23-Mar-2012:
--   * Changed type of Latitude and Longitude attributes in Distribution table to float
-- Version 1.08, 29-Apr-2012:
--   * Attribute Resource extended to 128 characters in Resources table
-- Version 1.09, 03-May-2012:
--   * Removed the Geography dictionary table
--   * Added attributes Continent, Region, Country, State, and County to Distribution table
--   * Attribute Type changed to 20 characters in Resources table 
--   * Removed the Descriptors dictionary table
--   * Changed table Descriptions to Descriptors
--   * Added attributes Character Number, Character Type, Unit, and State Number to Descriptors table
-- Version 1.10, 08-May-2012:
--   * Added attribute Type to Bibliography table
--   * Added attributes Scope and Environment to Metadata table
-- Version 1.11, 03-Jul-2012:
--   * Attribute Title changed to 254 characters in Bibliography table
--   * Attribute Locality changed to 254 characters in Distribution table
-- Version 1.12, 07-Jul-2012:
--   * Added Conservation Status table
-- Version 1.13, 10-Jul-2012:
--   * Attribute Descriptor Type changed to 20 characters in Descriptors table
-- Version 1.14, 12-Jul-2012:
--   * Added Genome table
-- Version 1.15, 21-Aug-2012:
--   * Attribute Detail changed to 160 characters in Bibliography table
--   * Added attribute Population Trend to Conservation Status table
-- Version 1.16, 06-Set-2012:
--   * Added attribute Banner to Metadata table
-- Version 1.17, 21-Oct-2012:
--   * Added attribute Collection Code to Distribution table
-- Version 1.18, 29-Nov-2015:
--   * Attribute Specific Authority changed to 50 characters in Taxa table
--   * Attribute Specific Authority changed to 50 characters in Synonyms table
-- Version 1.19, 30-Nov-2015:
--   * Attribute Vernacular Name changed to 40 characters in Common table
--   * Attribute Language changed to 30 characters in Common table 
-- Version 1.20, 2-Dec-2015:
--   * Attribute Author changed to 128 characters in Bibliography table
--   * Attribute Detail changed to 512 characters in Bibliography table
-- Version 1.21, 3-Dec-2015:
--   * Added Literature Pointers table 
--   * Changed attribute Resource Number to an auto-increment field in Resources table
-- Version 1.22, 9-Dec-2015:
--   * Added attribute Bibliographic number to Resources table
--   * Changed attribute Detail to text in Bibliography table
--   * Changed attribute Database identifier to an auto-increment field in Metadata table
-- Version 1.23, 15-Mar-2021:
--   * Added more taxonomic levels (Superclass, Infraclass, Superorder) to Higher Taxa table
-- ------------------------------------------------------------

-- --------------------------------------------------------

--
-- Structure of table `bibliography`
--

CREATE TABLE IF NOT EXISTS `bibliography` (
  `B_NO` int(10) NOT NULL auto_increment,
  `B_TYPE` varchar(20) default NULL,
  `B_AUTHOR` varchar(128) default NULL,
  `B_YEAR` int(10) default NULL,
  `B_SEQUENCE` char(1) default NULL,
  `B_TITLE` varchar(254) default NULL,
  `B_DETAIL` text default NULL,
  PRIMARY KEY  (`B_NO`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=31 ;

-- --------------------------------------------------------

--
-- Structure of table `commonnames`
--

CREATE TABLE IF NOT EXISTS `commonnames` (
  `ID` int(10) NOT NULL auto_increment,
  `T_NO` int(10) NOT NULL,
  `V_NAME` varchar(40) default NULL,
  `V_COUNTRY` varchar(30) default NULL,
  `V_LANGUAGE` varchar(30) default NULL,
  `B_NO` int(10) NOT NULL default '0',
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

-- --------------------------------------------------------

--
-- Structure of table `descriptors`
--

CREATE TABLE IF NOT EXISTS `descriptors` (
  `ID` int(10) NOT NULL auto_increment,
  `T_NO` int(10) NOT NULL,
  `D_NO` int(10) NOT NULL,
  `D_CHARACTER` varchar(50) NOT NULL,
  `D_UNIT` char(3) NOT NULL,
  `D_STATE_NO` int(10) NOT NULL,
  `D_STATE` varchar(50) NOT NULL,
  `D_CHAR_TYPE` char(20) NOT NULL,
  `B_NO` int(10) NOT NULL default '0',
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=16 ;

-- --------------------------------------------------------

--
-- Structure of table `distribution`
--

CREATE TABLE IF NOT EXISTS `distribution` (
  `ID` int(10) NOT NULL auto_increment,
  `T_NO` int(10) NOT NULL,
  `P_CODE` varchar(50) NOT NULL,
  `P_CONTINENT` varchar(30) NOT NULL,
  `P_REGION` varchar(30) NOT NULL,
  `P_COUNTRY` varchar(30) NOT NULL,
  `P_STATE` varchar(30) NOT NULL,
  `P_COUNTY` varchar(30) NOT NULL,
  `P_LOCALITY` varchar(160) NOT NULL,
  `P_LATITUDE` float default NULL,
  `P_LONGITUDE` float default NULL,
  `P_I_STATUS` varchar(10) default NULL,
  `B_NO` int(10) NOT NULL default '0',
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=296 ;

-- --------------------------------------------------------

--
-- Structure of table `genome`
--

CREATE TABLE IF NOT EXISTS `genome` (
  `ID` int(10) NOT NULL auto_increment,
  `T_NO` int(10) NOT NULL,
  `G_TAXID` int(10) NOT NULL,
  `G_SEQID` int(10) NOT NULL,
  `G_SEQ_TYPE` varchar(12) NOT NULL,
  `G_DESCRIPTION` varchar(254) NOT NULL,
  `G_SEQUENCE` text NOT NULL,
  `B_NO` int(10) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=80 ;

--
-- Structure of table `habitats`
--

CREATE TABLE IF NOT EXISTS `habitats` (
  `ID` int(10) NOT NULL auto_increment,
  `T_NO` int(10) NOT NULL,
  `H_PLACE` varchar(30) NOT NULL,
  `H_HABITAT` varchar(78) NOT NULL,
  `B_NO` int(10) NOT NULL default '0',
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

-- --------------------------------------------------------

--
-- Structure of table `highertaxa`
--

CREATE TABLE IF NOT EXISTS `highertaxa` (
  `ID` int(10) NOT NULL auto_increment,
  `T_NO` int(10) unsigned NOT NULL,
  `T_KINGDOM` varchar(50) default NULL,
  `T_PHYLUM` varchar(50) default NULL,
  `T_SUBPHYLUM` varchar(50) default NULL,
  `T_SUPERCLASS` varchar(50) default NULL,
  `T_CLASS` varchar(50) default NULL,
  `T_INFRACLASS` varchar(50) default NULL,
  `T_SUPERORDER` varchar(50) default NULL,
  `T_ORDER` varchar(50) default NULL,
  `T_SUBORDER` varchar(50) default NULL,
  `T_SUPERFAMILY` varchar(50) default NULL,
  `T_FAMILY` varchar(50) default NULL,
  `T_SUBFAMILY` varchar(50) default NULL,
  `T_TRIBE` varchar(50) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14 ;

-- --------------------------------------------------------

--
-- Structure of table `metadata`
--

CREATE TABLE IF NOT EXISTS `metadata` (
  `ID` int(10) NOT NULL auto_increment,
  `M_ACRONYM` varchar(50) default NULL,
  `M_TITLE` varchar(128) default NULL,
  `M_DESCRIPTION` varchar(255) default NULL,
  `M_SCOPE` varchar(20) default NULL,
  `M_ENVIRONMENT` varchar(20) default NULL,
  `M_COVERAGE` varchar(255) default NULL,
  `M_AUTHOR` varchar(255) default NULL,
  `M_VERSION` varchar(128) default NULL,
  `M_DATE` date default NULL,
  `M_PUBLISHER` varchar(128) default NULL,
  `M_URL` varchar(128) default NULL,
  `M_LOGO` varchar(128) default NULL,
  `M_BANNER` varchar(128) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

-- --------------------------------------------------------

--
-- Structure of table `notes`
--

CREATE TABLE IF NOT EXISTS `notes` (
  `ID` int(10) NOT NULL auto_increment,
  `T_NO` int(10) NOT NULL,
  `N_NOTE` varchar(255) NOT NULL,
  `B_NO` int(10) NOT NULL default '0',
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

-- --------------------------------------------------------

--
-- Structure of table `pointers`
--

CREATE TABLE IF NOT EXISTS `pointers` (
  `ID` int(10) NOT NULL auto_increment,
  `T_NO` int(10) NOT NULL,
  `L_TYPE` varchar(20) NOT NULL,
  `B_NO` int(10) NOT NULL default '0',
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10 ;

-- --------------------------------------------------------

--
-- Structure of table `resources`
--

CREATE TABLE IF NOT EXISTS `resources` (
  `ID` int(10) NOT NULL auto_increment,
  `T_NO` int(10) NOT NULL,
  `R_TYPE` varchar(50) default NULL,
  `R_RESOURCE` varchar(128) default NULL,
  `R_CAPTION` varchar(255) default NULL,
  `B_NO` int(10) NOT NULL default '0',
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10 ;

-- --------------------------------------------------------

--
-- Structure of table `status`
--

CREATE TABLE IF NOT EXISTS `status` (
  `ID` int(10) NOT NULL auto_increment,
  `T_NO` int(10) NOT NULL,
  `C_STATUS` varchar(22) NOT NULL,
  `C_TREND` varchar(12) NOT NULL,
  `B_NO` int(10) NOT NULL default '0',
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

-- --------------------------------------------------------

--
-- Structure of table `synonyms`
--

CREATE TABLE IF NOT EXISTS `synonyms` (
  `ID` int(10) NOT NULL auto_increment,
  `T_NO` int(10) NOT NULL,
  `S_STATUS` varchar(22) default NULL,
  `S_GENUS` varchar(30) default NULL,
  `S_G_AUTHOR` varchar(40) default NULL,
  `S_SUBGENUS` varchar(30) default NULL,
  `S_SPECIES` varchar(30) default NULL,
  `S_S_AUTHOR` varchar(50) default NULL,
  `S_RANK` varchar(7) default NULL,
  `S_SUBSP` varchar(30) default NULL,
  `S_SP_AUTHOR` varchar(40) default NULL,
  `B_NO` int(10) NOT NULL default '0',
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=15 ;

-- --------------------------------------------------------

--
-- Structure of table `taxa`
--

CREATE TABLE IF NOT EXISTS `taxa` (
  `T_NO` int(10) NOT NULL auto_increment,
  `T_STATUS` varchar(22) default NULL,
  `T_GENUS` varchar(30) default NULL,
  `T_G_AUTHOR` varchar(40) default NULL,
  `T_SUBGENUS` varchar(30) default NULL,
  `T_SPECIES` varchar(30) default NULL,
  `T_S_AUTHOR` varchar(50) default NULL,
  `T_RANK` varchar(7) default NULL,
  `T_SUBSP` varchar(30) default NULL,
  `T_SP_AUTHOR` varchar(40) default NULL,
  `B_NO` int(10) NOT NULL default '0',
  PRIMARY KEY  (`T_NO`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=19 ;

-- --------------------------------------------------------

--
-- Structure of table `uses`
--

CREATE TABLE IF NOT EXISTS `uses` (
  `ID` int(10) NOT NULL auto_increment,
  `T_NO` int(10) NOT NULL,
  `U_NAME` varchar(20) default NULL,
  `U_TYPE` varchar(30) default NULL,
  `U_PART` varchar(40) default NULL,
  `B_NO` int(10) NOT NULL default '0',
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=12 ;
