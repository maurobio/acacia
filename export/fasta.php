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
	include("../config.php");
  
	mysqli_connect($config['host'], $config['user'], $config['pwd']) or die("Connection error: ".mysqli_errno().": ".mysqli_error());
	mysql_select_db($config['dbname']);
	  
	$sort = $_GET['sort'];
	$filter = $_GET['filter'];
    $filename = $_GET['filename'];

	if (!isset($filename)) {	
		// file name for download
		$filename = "genome.fasta";

		header("Content-Disposition: attachment; filename=\"$filename\"");
		header("Content-Type: application/octet-stream;");

		$out = fopen("php://output", 'w');
	}
	else {
		$out = fopen($filename, 'w');
	}

	$sql = "SELECT * FROM genome";
	if (isset($filter)) {
		$sql = $sql." WHERE ".$filter;
	}
	if (isset($sort)) {
		$sql = $sql." ORDER BY ".$sort;
	}
	$result = mysqli_query($sql) or die('Query failed!');
	
	while($row = mysqli_fetch_array($result)) {
		$header = $row['G_DESCRIPTION'];
		$seq = $row['G_SEQUENCE'];
		fwrite($out, ">".chunk_split($header));
		fwrite($out, chunk_split($seq));
	}
	fclose($out);
	mysqli_free_result($result);
?>