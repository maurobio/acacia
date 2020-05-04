<!--
#=================================================================================#
#  Acacia - A Generic Conceptual Schema for Taxonomic Databases                   #
#                 Copyright 2008-2019 Mauro J. Cavalcanti                         #
#                           maurobio@gmail.com                                    #
#                                                                                 #
#   This program is free software: you can redistribute it and/or modify          #
#   it under the terms of the GNU General Public License as published by          #
#   the Free Software Foundation, either version 3 of the License, or             #
#   (at your option) any later version.                                           #
#                                                                                 #
#   This program is distributed in the hope that it will be useful,               #
#   but WITHOUT ANY WARRANTY; without even the implied warranty of                #
#   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the                  #
#   GNU General Public License for more details.                                  #
#                                                                                 #
#   You should have received a copy of the GNU General Public License             #
#   along with this program. If not, see <http://www.gnu.org/licenses/>.          #
#=================================================================================#
-->

<?php

	// Referential integrity
	mysql_query("DELETE FROM commonnames WHERE T_NO =".$this->rec);
	mysql_query("DELETE FROM descriptors WHERE T_NO =".$this->rec);
	mysql_query("DELETE FROM distribution WHERE T_NO =".$this->rec);
	mysql_query("DELETE FROM genome WHERE T_NO =".$this->rec);
	mysql_query("DELETE FROM habitats WHERE T_NO =".$this->rec);
	mysql_query("DELETE FROM highertaxa WHERE T_NO =".$this->rec);
	mysql_query("DELETE FROM notes WHERE T_NO =".$this->rec);
	mysql_query("DELETE FROM resources WHERE T_NO =".$this->rec);
	mysql_query("DELETE FROM status WHERE T_NO =".$this->rec);
	mysql_query("DELETE FROM synonyms WHERE T_NO =".$this->rec);
	mysql_query("DELETE FROM uses WHERE T_NO =".$this->rec);
	
	return true;
?>