<?php
/*================================================================================*
*       Acacia - A Generic Conceptual Schema for Taxonomic Databases              *
*                 Copyright 2008-2025 Mauro J. Cavalcanti                         *
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
    include("../mysql.php");

	$link = mysql_connect($config['host'], $config['user'], $config['pwd'], $config['dbname']) or die("Connection error: ".mysql_errno().": ".mysql_error());
	$sql = "SELECT * FROM metadata";
	$query = mysql_query($sql, $link) or die("Error: MySQL query failed"); 
	$title = mysql_result($query, 0, 'M_TITLE');
	
	// SLIKS chars
	$filename = "../keys/sliks/data.js";
	$out = fopen($filename, 'w');
	$sql = "SELECT DISTINCT D_CHARACTER, D_STATE, D_STATE_NO, D_NO, T_NO FROM descriptors ORDER BY D_NO, D_STATE_NO";
	$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
	fwrite($out, "var dataset = \"<h2>".$title."</h2>\"\n\n");
	
	// Initialize arrays to organize the data
	$characters = [];

	while ($row = mysql_fetch_assoc($query)) {
		$charName = $row['D_CHARACTER'];
		$stateName = $row['D_STATE'];
		$charId = $row['D_NO'];
    
		if (!isset($characters[$charId])) {
			// Initialize with character name as first element
			$characters[$charId] = [
				'name' => $charName,
				'states' => [$charName] // Start with character name
			];
		}
    
		// Add the state to the array (avoiding duplicates)
		if (!in_array($stateName, $characters[$charId]['states'])) {
			$characters[$charId]['states'][] = $stateName;
		}
	}

	// Sort characters by D_NO to maintain order
	ksort($characters);

	// Start building the output string
	$output = "var chars = [ [ \"Latin Name\"], \n";

	// Process each character
	$charCount = 0;
	foreach ($characters as $charId => $charData) {
		$charCount++;
    
		// Format the states array
		$statesString = implode('", "', $charData['states']);
    
		// Add to output
		$output .= "              [ \"$statesString\"]";
    
		// Check if this is the last character
		if ($charCount < count($characters)) {
			$output .= ",\n";
		} else {
			// Last character - add closing bracket
			$output .= " ]\n";
		}
	}

	// Output to text file
	fwrite($out, $output);
	fwrite($out, "\n");
	
	// SLIKS items - FIXED CODE
	// Get all characters in correct order from your original table
	$charsQuery = "SELECT DISTINCT D_NO FROM descriptors ORDER BY D_NO";
	$charsResult = mysql_query($charsQuery, $link) or die("Error: MySQL query failed");

	$characterOrder = [];
	while ($row = mysql_fetch_assoc($charsResult)) {
		$characterOrder[] = $row['D_NO'];
	}

	// Get species from taxa table
	$sql = "SELECT T_NO, CONCAT(T_GENUS, ' ', T_SPECIES) as species_name FROM taxa ORDER BY T_GENUS, T_SPECIES, T_SUBSP";
	$speciesResult = mysql_query($sql, $link) or die("Error: MySQL query failed");
	
	// Start items output
	fwrite($out, "var items = [ [\"\"],\n");

	$firstRow = true;
	while ($speciesRow = mysql_fetch_assoc($speciesResult)) {
		$speciesId = $speciesRow['T_NO'];
		$speciesName = $speciesRow['species_name']; // Now this will work
    
		// Build character states array for this species
		$charStates = [$speciesName];
    
		foreach ($characterOrder as $charId) {
			// Query for this character state from descriptors table
			$stateQuery = "SELECT D_STATE_NO FROM descriptors WHERE T_NO = $speciesId AND D_NO = $charId LIMIT 1";
			$stateResult = mysql_query($stateQuery, $link);
        
			if ($stateRow = mysql_fetch_assoc($stateResult)) {
				$charStates[] = $stateRow['D_STATE_NO'];
			} else {
				$charStates[] = "0";
			}
		}
    
		// Format the row
		$rowString = implode('","', $charStates);
    
		if (!$firstRow) {
			fwrite($out, ",\n");
		}
    
		fwrite($out, "              [\"$rowString\"]");
		$firstRow = false;
	}

	// Close the items array
	fwrite($out, " ];\n");

	fclose($out);
	mysql_free_result($query);
?>

<script language="javascript">
	alert("Output files written to the 'keys/data' folder");
	history.back();
</script>