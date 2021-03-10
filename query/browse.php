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

<?php include("../config.php"); ?>
		
<?php
	$link = mysql_connect($config['host'], $config['user'], $config['pwd']) or die("Connection error: ".mysql_errno().": ".mysql_error());
	$selected = mysql_select_db($config['dbname']) or die("Could not select ".$config['dbname']);
	$sql = "SELECT * FROM metadata";
	$query = mysql_query($sql, $link) or die("Error: MySQL query failed"); 
	$title = mysql_result($query, 0, 'M_TITLE');
	$pub = mysql_result($query, 0, 'M_PUBLISHER');
	$logo = mysql_result($query, 0, 'M_LOGO');
	$banner = mysql_result($query, 0, 'M_BANNER');
	$environ = mysql_result($query, 0, 'M_ENVIRONMENT');
	$sql = "SELECT * FROM highertaxa";
	$query = mysql_query($sql, $link) or die("Error: MySQL query failed"); 
	$kingdom = mysql_result($query, 0, 'T_KINGDOM');
?>
		
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<?php echo "<title>".$title." - Search Results</title>"; ?>
	<link rel="stylesheet" href="../library/stylesheet.css" type="text/css">
</head>
<body>

<?php
	if (!empty($banner)) {
		echo "<img src=\"".$banner."\" alt=\"Project banner\" width=\"991\" height=\"120\">\n";
	}
	else {
		echo "<table class=\"header\" width=\"100%\">\n";
		echo "<tr>\n";
		echo "<td width=\"10%\"> <img src=\"".$logo."\" alt=\"Project logo\" width=\"128\" height=\"80\"></td>\n";
		echo "<td width=\"50%\">".$title."<br></td>\n";
		echo "</tr>\n";
		echo "</table>\n";
	}
?>

<table class="menu" width="100%">
	<tr>
		<td align="center" width="100%">
		[ <a href="../index.php" title="Return to main page">Home</a>
		| <a href="search.php" title="Data retrieval">Search</a>
		<?php
			if (!$config['readonly']) {
				echo "| <a href=\"../edit/taxa.php\" title=\"Data input\">Edit</a>\n";
			}	
			else {
				echo "| <a href=\"../edit/taxa.php\" title=\"Data browse\">Browse</a>\n";
			}
			if ($config['morph']) {
				if ($config['keys']) {
					echo "| <a href=\"../keys/keys.php\" title=\"Interactive keys\">Keys</a>\n";
				}
			}
			if ($config['geog']) {	
				if ($config['maps']) {
					echo "| <a href=\"../maps/maps.php\" title=\"Distribution maps\">Maps</a>\n";
				}
			}	
		?>	
		| <a href="../report/stats.php" title="Database statistics">Statistics</a>
		| <a href="../help/about.php" title="Get help">About</a>
		]
		</td>
	</tr>	
</table>

<?php
	$page_name = "browse.php";
	$start = $_GET['start'];
	if (!($start > 0)) {
		$start = 0;
	}	
		
	$class = $_GET['class'];
	$field = $_GET['field'];
	$filter = $_GET['filter'];
	
	$link = mysql_connect($config['host'], $config['user'], $config['pwd']) or die("Connection error: ".mysql_errno().": ".mysql_error());
	mysql_select_db($config['dbname']);
  
	// Create temporary table
	$sql = "CREATE TABLE IF NOT EXISTS _taxa (
		T_NO int(10) NOT NULL,
		T_STATUS varchar(22) default NULL,
		T_GENUS varchar(30) default NULL,
		T_G_AUTHOR varchar(40) default NULL,
		T_SUBGENUS varchar(30) default NULL,
		T_SPECIES varchar(30) default NULL,
		T_S_AUTHOR varchar(40) default NULL,
		T_RANK varchar(7) default NULL,
		T_SUBSP varchar(30) default NULL,
		T_SP_AUTHOR varchar(40) default NULL,
		B_NO int(10) NOT NULL,
		PRIMARY KEY (T_NO)
		)"; 
	$result = mysql_query($sql, $link) or die("Error creating temporary table");
	
	// Clear temporary table if it already exists
	$sql = "TRUNCATE _taxa";
	$result = mysql_query($sql, $link);
	
	// Insert selected records into temporary table
	$sql = "INSERT IGNORE INTO _taxa SELECT 
		taxa.T_NO,
		taxa.T_STATUS,
		taxa.T_GENUS,
		taxa.T_G_AUTHOR,
		taxa.T_SUBGENUS,
		taxa.T_SPECIES,
		taxa.T_S_AUTHOR,
		taxa.T_RANK,
		taxa.T_SUBSP,
		taxa.T_SP_AUTHOR,
		taxa.B_NO";
	
	if ($class == "taxa") {
		$sql = $sql." FROM taxa WHERE UPPER(taxa.".$field.") = '".strtoupper($filter)."'";
	}
	elseif (($class == "highertaxa") or ($class == "distribution")) {
		$sql = $sql." FROM taxa,".$class." WHERE ".$class.".T_NO=taxa.T_NO AND UPPER(".$class.".".$field.") = '".strtoupper($filter)."'";
	}
	else {
		$sql = $sql." FROM taxa,".$class." WHERE ".$class.".T_NO=taxa.T_NO AND LOWER(".$class.".".$field.") LIKE '%".strtolower($filter)."%'";
	}
	$result = mysql_query($sql, $link) or die("Error: ". mysql_error());
	
	$query = mysql_query("SELECT * FROM _taxa", $link) or die("Error: ". mysql_error());
	$nrows = mysql_num_rows($query);
	if ($nrows == 0) {
		echo "<h3>No results found for '".$filter."'</h3>";
		echo "<p><form><input type=button value=\"Back\" OnClick=\"window.close()\"></form></p>";
		exit();
	}
?>

<center>
<h3>Data Browser</h3>
</center>
<?php 
	$expr = ($nrows > 1) ? " (".$nrows." results)" : " (".$nrows." result)";
	echo "<h3>Your query: "; 
	if ($field == "T_PHYLUM") {
		if (ucfirst($kingdom) == "Animalia") {
			echo ".....      Phylum equals '".ucfirst($filter)."'".$expr;
		}
		else {
			echo ".....      Division equals '".ucfirst($filter)."'".$expr;
		}
	}
	elseif ($field == "T_CLASS") {
		echo ".....      Class equals '".ucfirst($filter)."'".$expr;
	}
	elseif ($field == "T_ORDER") {
		echo ".....      Order equals '".ucfirst($filter)."'".$expr;
	}	
	elseif ($field == "T_FAMILY") {
		echo ".....      Family equals '".ucfirst($filter)."'".$expr;
	}
	elseif ($field == "T_GENUS") {
		echo ".....      Genus equals '".ucfirst($filter)."'".$expr;
	}
	elseif ($field == "T_SPECIES") {
		echo ".....      Species epithet equals '".strtolower($filter)."'".$expr;
	}
	elseif ($field == "D_STATE") {
		echo ".....      State descriptor equals '".ucfirst($filter)."'".$expr;
	}
	elseif ($field == "P_CONTINENT") {
		if (ucfirst($environ) == "Marine") {
			echo ".....      Ocean equals '".ucfirst($filter)."'".$expr;
		}
		else {
			echo ".....      Continent equals '".ucfirst($filter)."'".$expr;
		}
	}
	elseif ($field == "P_REGION") {
		echo ".....      Region equals '".ucfirst($filter)."'".$expr;
	}
	elseif ($field == "P_COUNTRY") {
		echo ".....      Country equals '".ucfirst($filter)."'".$expr;
	}
	elseif ($field == "P_STATE") {
		echo ".....      State/Province equals '".ucfirst($filter)."'".$expr;
	}
	elseif ($field == "H_HABITAT") {
		echo ".....      Habitat contains '".ucfirst($filter)."'".$expr;
	}
	elseif ($field == "V_NAME") {
		echo ".....      Vernacular name contains '".strtolower($filter)."'".$expr;
	}
	elseif ($field == "U_NAME") {
		echo ".....      Use contains '".ucfirst($filter)."'".$expr;
	}
	elseif ($field == "C_STATUS") {
		echo ".....      Conservation status contains '".ucwords($filter)."'".$expr;
	}
	elseif ($field == "G_DESCRIPTION") {
		echo ".....      Gene description contains '".ucwords($filter)."'".$expr;
	}
	echo "</h3>\n";
?>

<?php
	$eu = ($start - 0);                
	$limit = 15;  // No of records to be shown per page
	$this1 = $eu + $limit; 
	$back = $eu - $limit; 
	$next = $eu + $limit; 

	// Find out number of records in the table, in order to break the pages.
	$query2 = " SELECT * FROM _taxa ORDER BY T_GENUS, T_SPECIES, T_SUBSP";
	$result2 = mysql_query($query2);
	echo mysql_error();
	$nume = mysql_num_rows($result2);

	// Print table headers
	$bgcolor = "#f1f1f1";
	echo "<table class=\"report\" width=\"80%\" align=\"center\">\n";
	echo "<tbody>";
	echo "<th>Genus</th>"; 
	echo "<th>Species</th>"; 
	echo "<th>Specific authority</th>"; 
	echo "<th>Subspecies</th>"; 
	echo "<th>Subspecific authority</th>"; 
	echo "<th>Detail</th>"; 

	// Executing the query with variables $eu and $limit set at the top of the page
	$query1 = "SELECT * FROM _taxa ORDER BY T_GENUS, T_SPECIES, T_SUBSP LIMIT $eu, $limit";
	$result1 = mysql_query($query1);
	echo mysql_error();

	// Display the returned records inside the table rows
	while($noticia = mysql_fetch_array($result1)) {
		if ($bgcolor == "#f1f1f1") {
			$bgcolor = "#ffffff";
		}
		else {
			$bgcolor = "#f1f1f1";
		}
		
		$name = $noticia['T_GENUS']." ".$noticia['T_SPECIES'];
		if ($config['subsp']) {
			if (strlen($noticia['T_SUBSP']) > 0) {
				$name = $name." ".$noticia['T_SUBSP'];
			}
		}

		echo "<tr>";
		echo "<td align=\"left\" bgcolor=$bgcolor id=\"title\">&nbsp;<i>$noticia[T_GENUS]</i></td>"; 
		echo "<td align=\"left\" bgcolor=$bgcolor id=\"title\">&nbsp;<i>$noticia[T_SPECIES]</i></td>"; 
		echo "<td align=\"left\" bgcolor=$bgcolor id=\"title\">&nbsp;$noticia[T_S_AUTHOR]</td>"; 
		echo "<td align=\"left\" bgcolor=$bgcolor id=\"title\">&nbsp;<i>$noticia[T_SUBSP]</i></td>"; 
		echo "<td align=\"left\" bgcolor=$bgcolor id=\"title\">&nbsp;$noticia[T_SP_AUTHOR]</td>";
		echo "<td align=\"left\" bgcolor=$bgcolor id=\"title\">&nbsp;<a href=\"species.php?name=".urlencode(chop($name))."\">View</a></td>";
		echo "</tr>\n";
	}
	echo "<tbody>\n";
	echo "</table>\n";

	// Variables set for advance paging
	$p_limit = 30; // This should be more than $limit and set to a value for whick links to be breaked

	$p_f = $_GET['p_f']; 
	if (!($p_f > 0)) {   // This variable is set to zero for the first page
		$p_f = 0;
	}

	$p_fwd = $p_f + $p_limit;
	$p_back = $p_f - $p_limit;
	
	// Start the buttom links with Prev and Next links with page numbers
	echo "<table class=\"report\" align=\"center\" width=\"80%\"><tr>\n";
	echo "<td align=\"left\" width=\"20%\">\n";
	if ($p_f <> 0) {
		print "<a href='$page_name?start=$p_back&p_f=$p_back&class=$class&field=$field&filter=$filter'>Prev $p_limit</a>"; 
	}
	echo "</td><td align=\"left\" width=\"10%\">";
	
	// If variable $back is equal to or larger than 0, display only the link to move back
	if ($back >= 0 and ($back >= $p_f)) { 
		print "<a href='$page_name?start=$back&p_f=$p_f&class=$class&field=$field&filter=$filter'>Prev</a>"; 
	}
	
	// Display page links at center, excluding the current page
	echo "</td><td align=\"center\" width=\"30%\">";
	for($i = $p_f; $i < $nume and $i < ($p_f + $p_limit); $i = $i + $limit) {
		if($i <> $eu) {
			$i2 = $i + $p_f;
			echo "<a href='$page_name?start=$i&p_f=$p_f&class=$class&field=$field&filter=$filter'>&nbsp;$i&nbsp;</a> ";
		}
		else { 
			echo "<font color=\"red\">&nbsp;$i&nbsp;</font>"; // Current page is not displayed as link, but shown in red
		}        
	}
	echo "</td><td align=\"right\" width=\"10%\">";
	
	// If not in the last page then Next link will be displayed
	if ($this1 < $nume and $this1 < ($p_f + $p_limit)) { 
		print "<a href='$page_name?start=$next&p_f=$p_f&class=$class&field=$field&filter=$filter'>Next</a>";
	} 
	echo "</td><td align=\"right\" width=\"20%\">";
	if ($p_fwd < $nume) {
		print "<a href='$page_name?start=$p_fwd&p_f=$p_fwd&class=$class&field=$field&filter=$filter'>Next $p_limit</a>"; 
	}
	echo "</td></tr>\n";
	echo"</table>\n";
?>

<p>
Download as: <a href="../export/excel.php?table=_taxa&sort=T_NO">Excel</a> | <a href="../export/csv.php?table=_taxa&sort=T_NO">CSV</a>
</p>
<hr>
<p align="center">
<a class="footer" href="http://sites.google.com/site/acaciadb/" target=_blank>Powered by ACACIA</a></p>
</body>
</html>