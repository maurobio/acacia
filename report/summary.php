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

<?php include("../config.php"); ?>
<?php include("../library/functions.php"); ?>
<?php include "libchart/libchart/classes/libchart.php"; ?>

<?php
	$link = mysqli_connect($config['host'], $config['user'], $config['pwd']) or die("Connection error: ".mysql_errno().": ".mysql_error());
	$selected = mysqli_select_db($link, $config['dbname']) or die("Could not select ".$config['dbname']);
	$sql = "SELECT * FROM metadata";
	$query = mysqli_query($link, $sql) or die("Error: MySQL query failed"); 
	$title = mysqli_result($query, 0, 'M_TITLE');
	$url = mysqli_result($query, 0, 'M_URL');
	$pub = mysqli_result($query, 0, 'M_PUBLISHER');
	$logo = mysqli_result($query, 0, 'M_LOGO');
	$banner = mysqli_result($query, 0, 'M_BANNER');
	$sql = "SELECT * FROM highertaxa";
	$query = mysqli_query($link, $sql) or die("Error: MySQL query failed"); 
	$kingdom = mysqli_result($query, 0, 'T_KINGDOM');
?>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" href="../library/stylesheet.css" type="text/css">
	<?php echo "<title>".$title." - Database Statistics</title>"; ?>
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
		| <a href="../query/search.php" title="Data retrieval">Search</a>
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
		| <a href="stats.php" title="Database statistics">Statistics</a>
		| <a href="../help/about.php" title="Get help">About</a>
		]
		</td>
	</tr>	
</table>

<center><h3>Summary Statistics</h3></center>
<ul>

<table class="report" width="25%">
<tbody>

<?php 
	$sections = explode(":", $_GET['section']);
	$plot = $_GET['plot'];
	if ($plot == "true") {
		$plot = true;
	}
	else {
		$plot = false;
	}
	
	if ($plot) {
		if (!is_dir("generated/")) {
			mkdir("generated/");
		}
	}
	
	// Taxa
	if (in_array("taxa", $sections) || in_array("all", $sections)) {
		echo "<tr><th colspan=\"2\">TAXA</th></tr>\n";
		echo "<tr>\n";
		echo "<td>\n";
		if (ucfirst($kingdom) == "Plantae") {
			echo "Divisions:";
		} 
		else {
			echo "Phyla:";
		}
		echo "</td>\n";
		$sql = "SELECT DISTINCT T_PHYLUM FROM highertaxa";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$value = mysqli_num_rows($query);
		echo "<td>".$value."</td></tr>\n";

		echo "<tr>\n";
		echo "<td>Classes:</td>\n";
		$sql = "SELECT DISTINCT T_CLASS FROM highertaxa";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$value = mysqli_num_rows($query);
		echo "<td>".$value."</td></tr>\n";

		echo "<tr>\n";
		echo "<td>Orders:</td>\n";
		$sql = "SELECT DISTINCT T_ORDER FROM highertaxa";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$value = mysqli_num_rows($query);
		echo "<td>".$value."</td></tr>\n";

		echo "<tr>\n";
		echo "<td>Families:</td>\n";
		$sql = "SELECT DISTINCT T_FAMILY FROM highertaxa";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$value = mysqli_num_rows($query);
		echo "<td>".$value."</td></tr>\n";

		echo "<tr>\n";
		echo "<td>Genera:</td>\n";
		$sql = "SELECT DISTINCT T_GENUS FROM taxa WHERE T_STATUS='Accepted'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$value = mysqli_num_rows($query);
		echo "<td>".$value."</td></tr>\n";

		echo "<tr>\n";
		echo "<td>Species:</td>\n";
		$sql = "SELECT T_SPECIES FROM taxa WHERE T_STATUS='Accepted' AND T_RANK='Species'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$value = mysqli_num_rows($query);
		echo "<td>".$value."</td></tr>\n";

		if($config['subsp']) {
			echo "<tr>\n";
			echo "<td>Subspecies:</td>\n";
			$sql = "SELECT T_SUBSP FROM taxa WHERE T_STATUS='Accepted' AND T_RANK='Subsp.'";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";

			if (ucfirst($kingdom) == "Plantae") {
				echo "<tr>\n";
				echo "<td>Varieties:</td>\n";
				$sql = "SELECT DISTINCT T_SUBSP FROM taxa WHERE T_STATUS='Accepted' AND T_RANK='Var.'";
				$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
				$value = mysqli_num_rows($query);
				echo "<td>".$value."</td></tr>\n";
			}
		}	
		echo "<tr>\n";
		echo "<td>&nbsp;</td>\n";
		echo "<td>&nbsp;</td></tr>\n";
	}

	// Names
	if (in_array("names", $sections) || in_array("all", $sections)) {
		if ($plot) {
			$chart = new PieChart();
			$dataSet = new XYDataSet();
		}
    	echo "<tr><th colspan=\"2\" style=\"background:*d4d4d4;\">NAMES</th></tr>\n";
		echo "<tr>\n";
		echo "<td>Accepted:</td>\n";
		$sql = "SELECT COUNT(*) FROM taxa WHERE T_STATUS='Accepted'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query);
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";
		if ($plot) {
			$dataSet->addPoint(new Point("Accepted", $value));
		}
		
		echo "<tr>\n";
		echo "<td>Provisional:</td>\n";
		$sql = "SELECT COUNT(*) FROM taxa WHERE T_STATUS='Provisional'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query);
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";
		if ($plot) {
			$dataSet->addPoint(new Point("Provisional", $value));
		}
		
		echo "<tr>\n";
		echo "<td>Doubtful:</td>\n";
		$sql = "SELECT COUNT(*) FROM synonyms WHERE S_STATUS='Doubtful'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query);
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";
		if ($plot) {
			$dataSet->addPoint(new Point("Doubtful", $value));
		}

		echo "<tr>\n";
		echo "<td>Misapplied:</td>\n";
		$sql = "SELECT COUNT(*) FROM synonyms WHERE S_STATUS='Misapplied'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query);
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";
		if ($plot) {
			$dataSet->addPoint(new Point("Misapplied", $value));
		}

		echo "<tr>\n";
		echo "<td>Orthographic:</td>\n";
		$sql = "SELECT COUNT(*) FROM synonyms WHERE S_STATUS='Orthographic'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query);
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";
		if ($plot) {
			$dataSet->addPoint(new Point("Ortographic", $value));
		}
		
		echo "<tr>\n";
		echo "<td>Synonyms:</td>\n";
		$sql = "SELECT COUNT(*) FROM synonyms WHERE S_STATUS='Synonym'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query);
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";
		if ($plot) {
			$dataSet->addPoint(new Point("Synonyms", $value));
		}
		
		if ($plot) {
			$chart->setDataSet($dataSet);
			$chart->setTitle("Nomenclature");
			$chart->render("generated/1.png");
			echo "<td><p><img alt=\"Pie chart\" src=\"generated/1.png\" style=\"border: 1px solid gray;\"/></p></td>";
		}
		
		echo "<tr>\n";
		echo "<td>&nbsp;</td>\n";
		echo "<td>&nbsp;</td></tr>\n";
	}

	// Common
	if (in_array("common", $sections) || in_array("all", $sections)) {
		if ($config['common']) {
			echo "<tr><th colspan=\"2\" style=\"background:*d4d4d4;\">COMMON</th></tr>\n";
			echo "<tr>\n";
			echo "<td>Vernacular:</td>\n";
			$sql = "SELECT DISTINCT V_NAME FROM commonnames";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";
	
			echo "<tr>\n";
			echo "<td>Uses:</td>\n";
			$sql = "SELECT DISTINCT U_NAME FROM uses";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";
	
			echo "<tr>";
			echo "<td>&nbsp;</td>";
			echo "<td>&nbsp;</td></tr>";
		}
	}	

	// Descriptors
	if (in_array("descriptors", $sections) || in_array("all", $sections)) {
		if ($config['morph']) {
			if ($plot) {
				$chart = new PieChart();
				$dataSet = new XYDataSet();
			}
			echo "<tr><th colspan=\"2\" style=\"background:*d4d4d4;\">DESCRIPTORS</th></tr>\n";
			echo "<tr>\n";
			echo "<td>Unordered:</td>\n";
			$sql = "SELECT DISTINCT D_CHARACTER FROM descriptors WHERE D_CHAR_TYPE='Unordered'";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";
			if ($plot) {
				$dataSet->addPoint(new Point("Unordered", $value));
			}

			echo "<tr>\n";
			echo "<td>Ordered:</td>\n";
			$sql = "SELECT DISTINCT D_CHARACTER FROM descriptors WHERE D_CHAR_TYPE='Ordered'";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";
			if ($plot) {
				$dataSet->addPoint(new Point("Ordered", $value));
			}

			echo "<tr>\n";
			echo "<td>Discrete:</td>\n";
			$sql = "SELECT DISTINCT D_CHARACTER FROM descriptors WHERE D_CHAR_TYPE='Discrete'";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";
			if ($plot) {
				$dataSet->addPoint(new Point("Discrete", $value));
			}

			echo "<tr>\n";
			echo "<td>Continuous:</td>\n";
			$sql = "SELECT DISTINCT D_CHARACTER FROM descriptors WHERE D_CHAR_TYPE='Continuous'";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";
			if ($plot) {
				$dataSet->addPoint(new Point("Continuous", $value));
			}

			echo "<tr>\n";
			echo "<td>Text:</td>\n";
			$sql = "SELECT DISTINCT D_CHARACTER FROM descriptors WHERE D_CHAR_TYPE='Text'";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";
			if ($plot) {
				$dataSet->addPoint(new Point("Text", $value));
			}
	
			if ($plot) {
				$chart->setDataSet($dataSet);
				$chart->setTitle("Descriptors");
				$chart->render("generated/2.png");
				echo "<td><p><img alt=\"Pie chart\" src=\"generated/2.png\" style=\"border: 1px solid gray;\"/></p></td>";
			}
	
			echo "<tr>";
			echo "<td>&nbsp;</td>";
			echo "<td>&nbsp;</td></tr>";
		}
	}	

	// Sequences
	if (in_array("sequences", $sections) || in_array("all", $sections)) {
		if ($config['gene']) {
			if ($plot) {
				$chart = new PieChart();
				$dataSet = new XYDataSet();
			}
			echo "<tr><th colspan=\"2\" style=\"background:*d4d4d4;\">SEQUENCES</th></tr>\n";
			echo "<tr>\n";
			echo "<td>Nucleotide:</td>\n";
			$sql = "SELECT COUNT(*) FROM genome WHERE G_SEQ_TYPE='Nucleotide'";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$count = mysqli_result($query, 0, 'COUNT(*)');
			echo "<td>".$count."</td></tr>\n";
			if ($plot) {
				$dataSet->addPoint(new Point("Nucleotide", $count));
			}

			echo "<tr>\n";
			echo "<td>Protein:</td>\n";
			$sql = "SELECT COUNT(*) FROM genome WHERE G_SEQ_TYPE='Protein'";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$count = mysqli_result($query, 0, 'COUNT(*)');
			echo "<td>".$count."</td></tr>\n";
			if ($plot) {
				$dataSet->addPoint(new Point("Protein", $count));
			}
			
			if ($plot) {
				$chart->setDataSet($dataSet);
				$chart->setTitle("Biomolecular Sequences");
				$chart->render("generated/3.png");
				echo "<td><p><img alt=\"Pie chart\" src=\"generated/3.png\" style=\"border: 1px solid gray;\"/></p></td>";
			}
	
			echo "<tr>";
			echo "<td>&nbsp;</td>";
			echo "<td>&nbsp;</td></tr>";
		}
	}	

	// Geography
	if (in_array("geography", $sections) || in_array("all", $sections)) {
		if ($config['geog']) {
			echo "<tr><th colspan=\"2\" style=\"background:*d4d4d4;\">GEOGRAPHY</th></tr>\n";
			echo "<tr>\n";
			echo "<td>Continents:</td>\n";
			$sql = "SELECT DISTINCT P_CONTINENT FROM distribution";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";
	
			echo "<tr>\n";
			echo "<td>Regions:</td>\n";
			$sql = "SELECT DISTINCT P_REGION FROM distribution";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";

			echo "<tr>\n";
			echo "<td>Countries:</td>\n";
			$sql = "SELECT DISTINCT P_COUNTRY FROM distribution";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";

			echo "<tr>\n";
			echo "<td>States/Provinces:</td>\n";
			$sql = "SELECT DISTINCT P_STATE FROM distribution";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";
	
			echo "<tr>\n";
			echo "<td>Localities:</td>\n";
			$sql = "SELECT DISTINCT P_LOCALITY FROM distribution";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";
	
			echo "<tr>\n";
			echo "<td>Occurrences:</td>\n";
			$sql = "SELECT COUNT(*) FROM distribution";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$res = mysqli_fetch_array($query);
			$value = $res[0];
			echo "<td>".$value."</td></tr>\n";
	
			echo "<tr>";
			echo "<td>&nbsp;</td>";
			echo "<td>&nbsp;</td></tr>";
	
		}
	}	

	// Ecology
	if (in_array("ecology", $sections) || in_array("all", $sections)) {
		if ($config['ecol']) {
			echo "<tr><th colspan=\"2\" style=\"background:*d4d4d4;\">ECOLOGY</th></tr>\n";
			echo "<tr>\n";
			echo "<td>Habitats:</td>\n";
			$sql = "SELECT DISTINCT H_HABITAT FROM habitats";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";
	
			echo "<tr>";
			echo "<td>&nbsp;</td>";
			echo "<td>&nbsp;</td></tr>";
		}
	}	

	// Conservation status
	if (in_array("status", $sections) || in_array("all", $sections)) {
		if ($config['status']) {
			if ($plot) {
				$chart = new PieChart();
				$dataSet = new XYDataSet();
			}
			echo "<tr><th colspan=\"2\" style=\"background:*d4d4d4;\">CONSERVATION</th></tr>\n";
			echo "<tr>\n";
			echo "<td>Not Evaluated:</td>\n";
			$sql = "SELECT C_STATUS FROM status WHERE C_STATUS='Not Evaluated'";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";
			if ($plot) {
				$dataSet->addPoint(new Point("Not Evaluated", $value));
			}
	
			echo "<tr>\n";
			echo "<td>Data Deficient:</td>\n";
			$sql = "SELECT C_STATUS FROM status WHERE C_STATUS='Data Deficient'";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";
			if ($plot) {
				$dataSet->addPoint(new Point("Data Deficient", $value));
			}
	
			echo "<tr>\n";
			echo "<td>Least Concern:</td>\n";
			$sql = "SELECT C_STATUS FROM status WHERE C_STATUS='Least Concern'";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";
			if ($plot) {
				$dataSet->addPoint(new Point("Least Concern", $value));
			}

			echo "<tr>\n";
			echo "<td>Near Threatened:</td>\n";
			$sql = "SELECT C_STATUS FROM status WHERE C_STATUS='Near Threatened'";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";
			if ($plot) {
				$dataSet->addPoint(new Point("Near Threatened", $value));
			}

			echo "<tr>\n";
			echo "<td>Vulnerable:</td>\n";
			$sql = "SELECT C_STATUS FROM status WHERE C_STATUS='Vulnerable'";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";
			if ($plot) {
				$dataSet->addPoint(new Point("vulnerable", $value));
			}
			
			echo "<tr>\n";
			echo "<td>Endangered:</td>\n";
			$sql = "SELECT C_STATUS FROM status WHERE C_STATUS='Endangered'";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";
			if ($plot) {
				$dataSet->addPoint(new Point("Endangered", $value));
			}

			echo "<tr>\n";
			echo "<td>Critically Endangered:</td>\n";
			$sql = "SELECT C_STATUS FROM status WHERE C_STATUS='Critically Endangered'";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";
			if ($plot) {
				$dataSet->addPoint(new Point("Critically Endangered", $value));
			}
	
			echo "<tr>\n";
			echo "<td>Extinct in the Wild:</td>\n";
			$sql = "SELECT C_STATUS FROM status WHERE C_STATUS='Extinct in the Wild'";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";
			if ($plot) {
				$dataSet->addPoint(new Point("Extinct in the Wild", $value));
			}
	
			echo "<tr>\n";
			echo "<td>Extinct:</td>\n";
			$sql = "SELECT C_STATUS FROM status WHERE C_STATUS='Extinct'";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_num_rows($query);
			echo "<td>".$value."</td></tr>\n";
			if ($plot) {
				$dataSet->addPoint(new Point("Extinct", $value));
			}
			
			if ($plot) {
				$chart->setDataSet($dataSet);
				$chart->setTitle("Conservation Status");
				$chart->render("generated/4.png");
				echo "<td><p><img alt=\"Pie chart\" src=\"generated/4.png\" style=\"border: 1px solid gray;\"/></p></td>";
			}
	
			echo "<tr>";
			echo "<td>&nbsp;</td>";
			echo "<td>&nbsp;</td></tr>";
		}
	}	
	
	// Notes
	if (in_array("notes", $sections) || in_array("all", $sections)) {
		echo "<tr><th colspan=\"2\"style=\"background:*d4d4d4;\">NOTES</th></tr>\n";
		echo "<tr>\n";
		echo "<td>Text notes:</td>\n";
		$sql = "SELECT COUNT(*) FROM notes";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query);
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";
		
		echo "<tr>";
		echo "<td>&nbsp;</td>";
		echo "<td>&nbsp;</td></tr>";
	}

	// Bibliography
	if (in_array("bibliography", $sections) || in_array("all", $sections)) {
		echo "<tr><th colspan=\"2\"style=\"background:*d4d4d4;\">BIBLIOGRAPHY</th></tr>\n";
		echo "<tr>\n";
		echo "<td>Articles:</td>\n";
		$sql = "SELECT COUNT(*) FROM bibliography WHERE B_TYPE='article'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query);
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";

		echo "<tr>\n";
		echo "<td>Books:</td>\n";
		$sql = "SELECT COUNT(*) FROM bibliography WHERE B_TYPE='book'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query);
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";

		echo "<tr>\n";
		echo "<td>Booklets:</td>\n";
		$sql = "SELECT COUNT(*) FROM bibliography WHERE B_TYPE='booklets'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query);
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";

		echo "<tr>\n";
		echo "<td>Conference Proceedings:</td>\n";
		$sql = "SELECT COUNT(*) FROM bibliography WHERE B_TYPE='conference' OR B_TYPE='inproceedings'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query);
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";

		echo "<tr>\n";
		echo "<td>Book Chapters:</td>\n";
		$sql = "SELECT COUNT(*) FROM bibliography WHERE B_TYPE='inbook' OR B_TYPE='incollection'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query) or die("Error: MySQL query failed");
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";

		echo "<tr>\n";
		echo "<td>Manuals:</td>\n";
		$sql = "SELECT COUNT(*) FROM bibliography WHERE B_TYPE='manual'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query) or die("Error: MySQL query failed");
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";

		echo "<tr>\n";
		echo "<td>M.Sc. Theses:</td>\n";
		$sql = "SELECT COUNT(*) FROM bibliography WHERE B_TYPE='masterthesis'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query);
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";

		echo "<tr>\n";
		echo "<td>Ph.D. Theses:</td>\n";
		$sql = "SELECT COUNT(*) FROM bibliography WHERE B_TYPE='phdthesis'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query);
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";

		echo "<tr>\n";
		echo "<td>Proceedings:</td>\n";
		$sql = "SELECT COUNT(*) FROM bibliography WHERE B_TYPE='proceedings'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query);
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";

		echo "<tr>\n";
		echo "<td>Technical Reports:</td>\n";
		$sql = "SELECT COUNT(*) FROM bibliography WHERE B_TYPE='techreport'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query);
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";

		echo "<tr>\n";
		echo "<td>Miscellaneous:</td>\n";
		$sql = "SELECT COUNT(*) FROM bibliography WHERE B_TYPE='misc'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query) or die("Error: MySQL query failed");
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";

		echo "<tr>\n";
		echo "<td>Unpublished:</td>\n";
		$sql = "SELECT COUNT(*) FROM bibliography WHERE B_TYPE='unpublished'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query);
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";
		
		echo "<tr>";
		echo "<td>&nbsp;</td>";
		echo "<td>&nbsp;</td></tr>";
		
		// Literature pointers
		echo "<tr><th colspan=\"2\"style=\"background:*d4d4d4;\">POINTERS</th></tr>\n";
		echo "<tr>\n";
		echo "<td>Descriptions:</td>\n";
		$sql = "SELECT COUNT(*) FROM pointers WHERE L_TYPE='Description'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query);
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";

		echo "<tr>\n";
		echo "<td>Illustrations:</td>\n";
		$sql = "SELECT COUNT(*) FROM pointers WHERE L_TYPE='Illustration'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query);
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";

		echo "<tr>\n";
		echo "<td>Maps:</td>\n";
		$sql = "SELECT COUNT(*) FROM pointers WHERE L_TYPE='Map'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query);
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";
		
		echo "<tr>";
		echo "<td>&nbsp;</td>";
		echo "<td>&nbsp;</td></tr>";
	}
	
	// Media resources
	if (in_array("media", $sections) || in_array("all", $sections)) {
		echo "<tr><th colspan=\"2\"style=\"background:*d4d4d4;\">MEDIA</th></tr>\n";
		echo "<tr>\n";
		echo "<td>Audios:</td>\n";
		$sql = "SELECT COUNT(*) FROM resources WHERE R_RESOURCE='Audio'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query);
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";
		
		echo "<tr>\n";
		echo "<td>Images:</td>\n";
		$sql = "SELECT COUNT(*) FROM resources WHERE R_RESOURCE='Image'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query);
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";
		
		echo "<tr>\n";
		echo "<td>Videos:</td>\n";
		$sql = "SELECT COUNT(*) FROM resources WHERE R_RESOURCE='Video'";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$res = mysqli_fetch_array($query);
		$value = $res[0];
		echo "<td>".$value."</td></tr>\n";
	}

	mysqli_free_result($query);
	mysqli_close($link);
?>

</tbody>
</table>
</ul>

<br>
<hr>
<p align="center">
<a class="footer" href="http://sites.google.com/site/acaciadb/" target=_blank>Powered by ACACIA</a></p>
</body>
</html>