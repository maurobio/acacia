<!--
#=================================================================================#
#       Acacia - A Generic Conceptual Schema for Taxonomic Databases              #
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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<?php
	// Original PHP code by Chirp Internet: www.chirp.com.au
	// Please acknowledge use of this code by including this header.
  
	include("../config.php");
  
	function cleanData(&$str) {
		$str = preg_replace("/\t/", "\\t", $str);
		$str = preg_replace("/\r?\n/", "\\n", $str);
		if(strstr($str, '"')) $str = '"' . str_replace('"', '""', $str) . '"';
	}

	$link = mysqli_connect($config['host'], $config['user'], $config['pwd']) or die("Connection error: ".mysqli_errno().": ".mysqli_error());
	$selected = mysqli_select_db($link, $config['dbname']);
  
	$table = $_GET['table'];
	$sort = $_GET['sort'];
	$filter = $_GET['filter'];
    $filename = $_GET['filename'];

	if (!isset($filename)) {
		// file name for download
		$filename = $table.".xls";

		header("Content-Disposition: attachment; filename=\"$filename\"");
		header("Content-Type: application/vnd.ms-excel");
		$out = fopen("php://output", 'w');
	}
	else {
		$out = fopen($filename, 'w');
	}	

	$sql = "SELECT * FROM ".$table;
	if (isset($filter)) {
		$sql = $sql." WHERE ".$filter;
	}
	if (isset($sort)) {
		$sql = $sql." ORDER BY ".$sort;
	}
	$flag = false;
	$result = mysqli_query($link, $sql) or die('Query failed!');
	while($row = mysqli_fetch_assoc($result)) {
		if(!$flag) {
			// display field/column names as first row
			echo implode("\t", array_keys($row)) . "\r\n";
			$flag = true;
		}
		array_walk($row, 'cleanData');
		echo implode("\t", array_values($row)) . "\r\n";
	}
	fclose($out);
	mysqli_free_result($result);
?>