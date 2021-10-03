<?php
/*================================================================================*
*       Acacia - A Generic Conceptual Schema for Taxonomic Databases              *
*                 Copyright 2008-2019 Mauro J. Cavalcanti                         *
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

	function cleanData(&$str) {
		if($str == 't') $str = 'TRUE';
		if($str == 'f') $str = 'FALSE';
		if(preg_match("/^0/", $str) || preg_match("/^\+?\d{8,}$/", $str) || preg_match("/^\d{4}.\d{1,2}.\d{1,2}/", $str)) {
			$str = "'$str";
		}
		if(strstr($str, '"')) $str = '"' . str_replace('"', '""', $str) . '"';
	}
	
	$link = mysqli_connect($config['host'], $config['user'], $config['pwd']) or die("Connection error: ".mysqli_errno().": ".mysqli_error());
	$selected = mysqli_select_db($link, $config['dbname']);
  
	$table = $_GET['table'];
	$sort = $_GET['sort'];
	/*$filter = $_GET['filter'];
	$filename = $_GET['filename'];*/

	if (!isset($filename)) {	
		// file name for download
		$filename = $table.".csv";

		header("Content-Disposition: attachment; filename=\"$filename\"");
		header("Content-Type: application/vnd.ms-excel;");

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
			fputcsv($out, array_keys($row), ',', '"');
			$flag = true;
		}
		array_walk($row, 'cleanData');
		fputcsv($out, array_values($row), ',', '"');
	}
	fclose($out);
	mysqli_free_result($result);
?>