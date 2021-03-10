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

	mysql_connect($config['host'], $config['user'], $config['pwd']) or die("Connection error: ".mysql_errno().": ".mysql_error());
	mysql_select_db($config['dbname']);
	$sql = "SELECT * FROM metadata";
	$query = mysql_query($sql) or die("Error: MySQL query failed"); 
	$title = mysql_result($query, 0, 'M_TITLE');
	
	// DELTA chars
	$filename = "../keys/data/chars";
	$out = fopen($filename, 'w');
	$sql = "SELECT * FROM descriptors ORDER BY D_NO, D_STATE_NO";
	$query = mysql_query($sql) or die("Error: MySQL query failed");
	fwrite($out, "*SHOW: ".$title." - character list. ".date("d M Y, g:i a")."\n");
	fwrite($out, "*CHARACTER LIST\n");
	$grpCheck = '';
	$grpEval = '';
	$subCheck = '';
	$subEval = '';
	while($row = mysql_fetch_array($query)) {
		$grpCheck = $row['D_CHARACTER'];
		if ($grpEval != $grpCheck) {
			fwrite($out, "\n");
			$charNo = $row['D_NO'];
			$grpEval = $grpCheck;
			fwrite($out, "#".$charNo.". ".$grpCheck."/\n");
			if ($row['D_CHAR_TYPE'] == "RN") {
				fwrite($out, " ".$row['D_UNIT']."/\n");
			}
		}
		$subCheck = $row['D_STATE'];
		if ($subEval != $subCheck) {
			$stateNo = $row['D_STATE_NO'];
			$subEval = $subCheck;
			if (($row['D_CHAR_TYPE'] != "IN") && ($row['D_CHAR_TYPE'] != "RN") && ($row['D_CHAR_TYPE'] != "TE")) {
				fwrite($out, "\t".$stateNo.". ".$subCheck."/\n");
			}
		}
	}
	fclose($out);

	//DELTA items
	$filename = "../keys/data/items";
	$out = fopen($filename, 'w');
	$sql = "SELECT * FROM taxa ORDER BY T_GENUS, T_SPECIES, T_SUBSP";
	$query = mysql_query($sql) or die("Error: MySQL query failed");
	fwrite($out, "*SHOW: ".$title." - item descriptions. ".date("d M Y, g:i a")."\n");
	fwrite($out, "*ITEM DESCRIPTIONS\n\n");
	while($row = mysql_fetch_array($query)) {
		$t_no = $row['T_NO'];
		fwrite($out, "# ".$row['T_GENUS']." ".$row['T_SPECIES']." <".$row['T_S_AUTHOR'].">/"."\n");
		$sql = "SELECT T_NO, D_NO, D_STATE_NO FROM descriptors WHERE T_NO='$t_no'";
		$sql = $sql." ORDER BY T_NO, D_NO, D_STATE_NO";
		$query_a = mysql_query($sql) or die("Error: MySQL query failed");
		$grpCheck = '';
		$grpEval = '';
		while ($res_a = mysql_fetch_array($query_a)) {
			$d_no = $res_a['D_NO'];
			$sql = "SELECT T_NO, D_NO, D_STATE_NO FROM descriptors WHERE T_NO='$t_no' AND D_NO='$d_no'";
			$sql = $sql." ORDER BY T_NO, D_NO, D_STATE_NO";
			$query_b = mysql_query($sql) or die("Error: MySQL query failed");
			$nstates = mysql_num_rows($query_b);
			if ($nstates == 1) {
				fwrite($out, $res_a['D_NO'].",".$res_a['D_STATE_NO']." ");
			}
			else {
				$count = 1;
				$grpCheck = $res_a['D_NO'];
				if ($grpEval != $grpCheck) {
					$grpEval = $grpCheck;
					fwrite($out, $grpCheck.",");
				}
				else {
					fwrite($out, "/");
					$count++;
				}
				fwrite($out, $res_a['D_STATE_NO']);
			}
			mysql_free_result($query_b);
			if (($nstates > 1) && ($count == $nstates)) {
				fwrite($out, " ");
			}
		}
		mysql_free_result($query_a);
		fwrite($out, "\n\n");
	}
	fclose($out);

	// DELTA specs
	$filename = "../keys/data/specs";
	$out = fopen($filename, 'w');
	$sql = "SELECT * FROM descriptors ORDER BY D_NO, D_STATE_NO";
	$query = mysql_query($sql) or die("Error: MySQL query failed");
	fwrite($out, "*SHOW: ".$title." - specifications. ".date("d M Y, g:i a")."\n\n");

	// Number of characters
	$sql = "SELECT DISTINCT D_CHARACTER FROM descriptors";
	$query = mysql_query($sql) or die("Error: MySQL query failed");
	$nchar = mysql_num_rows($query);
	fwrite($out, "*NUMBER OF CHARACTERS ".$nchar."\n"); 

	// Maximum number of states
	$sql = "SELECT DISTINCT D_CHARACTER, COUNT(D_STATE_NO) FROM descriptors GROUP BY D_CHARACTER ORDER BY COUNT(D_STATE_NO)";
	$query = mysql_query($sql) or die("Error: MySQL query failed");
	$maxchar = mysql_result($query, 2, 1);
	fwrite($out, "*MAXIMUM NUMBER OF STATES ".$maxchar."\n");

	// Number of items
	$sql = "SELECT * FROM taxa";
	$query = mysql_query($sql) or die("Error: MySQL query failed");
	$ntax = mysql_num_rows($query);
	fwrite($out, "*MAXIMUM NUMBER OF ITEMS ".$ntax."\n");

	// Character types
	$sql = "SELECT DISTINCT D_NO, D_CHARACTER, D_CHAR_TYPE FROM descriptors";
	$query = mysql_query($sql) or die("Error: MySQL query failed");
	fwrite($out, "*CHARACTER TYPES ");
	while($row = mysql_fetch_array($query)) {
		fwrite($out, $row['D_NO'].",".$row['D_CHAR_TYPE']." ");
	}
	fwrite($out, "\n");

	// Number of states
	$sql = "SELECT DISTINCT D_NO, COUNT(D_STATE_NO) FROM descriptors GROUP BY D_NO";
	$query = mysql_query($sql) or die("Error: MySQL query failed");
	fwrite($out, "*NUMBERS OF STATES ");
	while($row = mysql_fetch_array($query)) {
		fwrite($out, $row[0].",".$row[1]." ");
	}
	fclose($out);
	mysql_free_result($query);
?>

<script language="javascript">
	alert("Output files written to the 'keys/data' folder");
	history.back();
</script>