<?php
/*================================================================================*
*       Acacia - A Generic Conceptual Schema for Taxonomic Databases              *
*                 Copyright 2008-2021 Mauro J. Cavalcanti                         *
*                           maurobio@gmail.com                                    *
*                                                                                 *
*   This program is free software: you can redistribute it and/or modify          *
*   it under the terms of the GNU General Public License as published by          *
*   the Free Software Foundation, either version 3 of the License, or             *
*   (at your option) any later version.                                           *
*                                                                                 *
*   This program is distributed in the hope that it will be useful,               *
*   but WITHOUT ANY WARRANTY; without even the implied warranty of                *
*   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the                  *
*   GNU General Public License for more details.                                  *
*                                                                                 *
*   You should have received a copy of the GNU General Public License             *
*   along with this program. If not, see <http://www.gnu.org/licenses/>.          *
*=================================================================================*/?>

<?php
	include("../config.php");

	$t_no = $_POST['t_no'];
	$t_kingdom = $_POST['kingdom'];
	$t_division = $_POST['division'];
	$t_class = $_POST['class'];
	$t_order = $_POST['order'];
	$t_family = $_POST['family'];
	
	$link = mysql_connect($config['host'], $config['user'], $config['pwd']) or die("Connection error: ".mysql_errno().": ".mysql_error());
	$selected = mysql_select_db($config['dbname']) or die("Could not select ".$config['dbname']);
	
	// Insert new record into HigherTaxa table
	mysql_query("INSERT INTO highertaxa (T_NO, T_KINGDOM, T_PHYLUM, T_CLASS, T_ORDER, T_FAMILY)
		VALUES ($t_no, '$t_kingdom', '$t_division', '$t_class', '$t_order', '$t_family')");
		
	// Insert new record into Conservation Status table
	mysql_query("INSERT INTO status (T_NO, C_STATUS, C_TREND, B_NO)
		VALUES ($t_no, 'Not Evaluated', 'Unknown', 0)");	
	
	mysql_close($link);	
	
	echo "
		<script language=\"javascript\">
			window.close();
		</script>
		";
?>