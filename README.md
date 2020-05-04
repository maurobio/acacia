# Acacia
 A generic biodiversity database management system

Acacia is an interactive data entry, querying, and editing system based on a generic conceptual schema for taxonomic databases. It combines the automated use of scientific names and synonyms in a species checklist with online access to geographical data and common knowledge data (morphological descriptors, genomics, ecology, vernacular names, economic uses, structured notes and conservation status) about the species. All these data can be cross-indexed to a citation list.

The design and standard permits rapid customization to suit any taxonomic group. 

Requirements
------------

	PHP 5.2+
	MySQL 5.0+

Installation
------------

1. Download the latest Acacia distribution package file. 
2. Extract and upload the entire folder to your web server under your domain name.
3. Open the config.php file and enter the mysql database user id, password, and db name. Change other variables as required for customizing your specific database.
4. Create an empty database under the name given in the config.php file. Use the provided scheme.sql file to create empty database tables.
5. To access the database, open http://your_domain_name.com/acacia/index.php' with your web browser. If you have set the 'read only' variable as false in the config.php file, the 'Edit' menu option will be enabled. You can then proceed to login to the administrative area.

Contact
-------

Please send bug reports, suggestions, and comments to the author.
	
For more information, please visit the Acacia website at http://sites.google.com/site/acaciadb

License
-------

This software is distributed under the terms of the GNU General Public License, version 3 (GPL3).