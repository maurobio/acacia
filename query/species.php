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

<?php include("../config.php"); ?>
<?php include("../library/functions.php"); ?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
		"http://www.w3.org/TR/html4/loose.dtd">
		
<?php
	$link = mysqli_connect($config['host'], $config['user'], $config['pwd']) or die("Connection error: ".mysqli_errno().": ".mysqli_error());
	$selected = mysqli_select_db($link, $config['dbname']) or die("Could not select ".$config['dbname']);
	$sql = "SELECT * FROM metadata";
	$query = mysqli_query($link, $sql) or die("Error: MySQL query failed"); 
	$title = mysqli_result($query, 0, 'M_TITLE');
	$pub = mysqli_result($query, 0, 'M_PUBLISHER');
	$logo = mysqli_result($query, 0, 'M_LOGO');
	$banner = mysqli_result($query, 0, 'M_BANNER');
?>
		
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="apple-mobile-web-app-capable" content="yes">
	<link rel="stylesheet" href="../library/stylesheet.css" type="text/css">
	<link rel="stylesheet" type="text/css" href="../library/plusimageviewer.css"/>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
	<script type="text/javascript" src="../library/plusimageviewer.js">
	/***********************************************
	* Plus Size Image Viewer- by JavaScript Kit (www.javascriptkit.com)
	* This notice must stay intact for usage
	* Visit JavaScript Kit at http://www.javascriptkit.com/ for full source code
	***********************************************/
	</script>
	<script src="../library/functions.js"></script>
	<?php echo "<title>".$title." - Search Results</title>"; ?>
</head>
<body OnUnload="finish()">

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
		| <a href="../report/stats.php" title="Database statistics">Statistics</a>
		| <a href="../help/about.php" title="Get help">About</a>
		]
		</td>
	</tr>	
</table>

<?php
	$name = $_GET['name'];
	$parts = explode(" ", $name);
	if (count($parts) < 2) {
		if ($config['subsp']) {
			echo "<h3>You can enter two words (a binomial: genus and species) or three words (a trinomial: subspecies or variety)</h3>\n";
		}	
		else {
			echo "<h3>You can enter two words (a binomial: genus and species)</h3>\n";

		}
		echo "<p><form><input type=button value=\"Back\" onClick=\"window.close()\"></form></p>\n";
		exit();
	}
	$genus = $parts[0];
	$species = $parts[1];
	if (count($parts) == 3) {
		$infra = $parts[2];
	}	
	
	$genus = ucfirst($genus);
	$species = strtolower($species);
	if (!empty($infra)) {
		$infra = strtolower($infra);
	}

	$sql = "SELECT * FROM taxa WHERE T_GENUS='$genus' AND T_SPECIES='$species'";
	if (!empty($infra)) {
		$sql = $sql." AND T_SUBSP='$infra'";
	}

	$link = mysqli_connect($config['host'], $config['user'], $config['pwd']) or die("Connection error: ".mysqli_errno().": ".mysqli_error());
	mysqli_select_db($link, $config['dbname']);
	$query = mysqli_query($link, $sql) or die("Query error: ".mysqli_errno().": ".mysqli_error());

	$nrows = mysqli_num_rows($query);
	if ($nrows == 0) {
		// Synonimic indexing
		$sql =  "SELECT * FROM synonyms WHERE S_GENUS='$genus' AND S_SPECIES='$species'";
		if (!empty($infra)) {
			$sql = $sql." AND S_SUBSP='$infra'";
		}
		$query = mysqli_query($link, $sql) or die("Query error: ".mysqli_errno().": ".mysqli_error());
		$result = mysqli_num_rows($query);
		if ($result == 0) {
			echo "<h3>No results found for '".$genus." ".$species." ".$infra."'</h3>\n";
			echo "<p><form><input type=button value=\"Back\" onClick=\"window.close()\"></form></p>\n";
			exit();
		}
		else {
			$row = mysqli_fetch_array($query);
			$id = $row['T_NO'];
			$sql = "SELECT * FROM taxa WHERE T_NO='$id'";
			$query = mysqli_query($link, $sql) or die("Query error: ".mysqli_errno().": ".mysqli_error());
		}
	}

	$row = mysqli_fetch_array($query);
	$id = $row['T_NO'];
	
	// Bibliographic citations array
	$biblio = array();
	$index = 0;

	// Accepted Name & Higher Taxon 
	if (isset($infra)) {
		echo "<h2>Search results for: <i>".$genus." ".$species." ".$infra."</i></h2>\n";
	} else {
		echo "<h2>Search results for: <i>".$genus." ".$species."</i></h2>\n";
	}
	echo "<table class=\"report\" width=\"100%\">\n";
	echo "<tr><th><font size=+1>Taxonomy and Nomenclature</font></th></tr>\n";
	echo "</table>\n";
	echo "<h4>".$row['T_STATUS']." name</h4>\n";
	echo "<ul>\n";
	echo "<i>".$row['T_GENUS']." ".$row['T_SPECIES']."</i> ";
	if (strlen($row['T_SUBSP']) > 0) {
		echo strtolower($row['T_RANK'])." <i>". $row['T_SUBSP']."</i> ".$row['T_SP_AUTHOR'];
	}
	else {
		echo $row['T_S_AUTHOR'];
	}
	echo "<a name=cite-".$row['B_NO']."> [<a href=#note-".$row['B_NO'].">".$row['B_NO']."</a>]<p>\n";
	echo "</ul>\n\n";
	$biblio[$index] = $row['B_NO'];
	$index += 1;

	// Synonyms
	$sql = "SELECT * FROM synonyms WHERE T_NO='$id'";
	$query = mysqli_query($link, $sql) or die("Query error: ".mysqli_errno().": ".mysqli_error());
	$num = mysqli_num_rows($query);
	echo "<h4>Synonyms</h4>\n";
	echo "<ul>\n";
	if ($num > 0) {
		while($res = mysqli_fetch_array($query)) {
			echo "<i>".$res['S_GENUS']." ".$res['S_SPECIES']."</i> ";
			if (strlen($row['S_SUBSP']) > 0) {
				echo strtolower($row['S_RANK'])." <i>". $row['S_SUBSP']."</i> ".$row['S_SP_AUTHOR'];
			}
			else {
				echo $res['S_S_AUTHOR'];
			}	
			echo " {".$res['S_STATUS']."} ";
			echo "<a name=cite-".$res['B_NO'].">[<a href=#note-".$res['B_NO'].">".$res['B_NO']."</a>]<br>\n";
			$biblio[$index] = $res['B_NO'];
			$index += 1;
		}
	}
	else {
		echo "<i>No synonyms available</i>\n";
	}
	echo "</ul>\n\n";
	echo "<p align=\"right\">\n";
    echo "<a href=\"#top\">return to top</a>\n";

	// Taxonomy
	$sql = "SELECT T_KINGDOM, T_PHYLUM, T_CLASS, T_ORDER, T_FAMILY FROM highertaxa WHERE T_NO='$id'";
	$query = mysqli_query($link, $sql) or die("Query error: ".mysqli_errno().": ".mysqli_error());
	$value = mysqli_fetch_array($query);
	$kingdom = $value[0];
	echo "<h4>Classification</h4>\n";
	echo "<ul>\n";
	echo "<b>Kingdom:</b> ".$value[0]."<br>\n";
	if (ucfirst($kingdom) == "Animalia") {
		echo "<b>Phylum:</b> ".$value[1]."<br>\n";
	}	
	else {
		echo "<b>Division:</b> ".$value[1]."<br>\n";
	}
	echo "<b>Class:</b> ".$value[2]."<br>\n";
	echo "<b>Order:</b> ".$value[3]."<br>\n";
	echo "<b>Family:</b> ".$value[4]."<br>\n";
	echo "</ul>\n\n";
    echo "<p align=\"right\">\n";
    echo "<a href=\"#top\">return to top</a>\n";

	if ($config['common']) {
		// Vernacular Names
		$sql = "SELECT * FROM commonnames WHERE T_NO='$id'";
		$sql = $sql." ORDER BY V_NAME";
		$query = mysqli_query($link, $sql) or die("Query error: ".mysqli_errno().": ".mysqli_error());
		$num = mysqli_num_rows($query);
		echo "<h4>Vernacular names</h4>\n";
		echo "<ul>\n";
		if ($num > 0) {
			while($res = mysqli_fetch_array($query)) {
				echo $res['V_NAME']." (".$res['V_LANGUAGE'].", ".$res['V_COUNTRY'].") ";
				echo "<a name=cite-".$res['B_NO'].">[<a href=#note-".$res['B_NO'].">".$res['B_NO']."</a>]; ";
				$biblio[$index] = $res['B_NO'];
				$index += 1;
			}
		}
		else {
			echo "<i>No vernacular names available</i>\n";
		}
		echo "</ul>\n\n";
		echo "<p align=\"right\">\n";
		echo "<a href=\"#top\">return to top</a>\n";
	}
	
	if ($config['status']) {
		// Conservation Status
		echo "<table class=\"report\" width=\"100%\">\n";
		echo "<tr><th><font size=+1>Conservation</font></th></tr>\n";
		echo "</table>\n";
		$sql = "SELECT * FROM status WHERE T_NO='$id'";
		$query = mysqli_query($link, $sql) or die("Query error: ".mysqli_errno().": ".mysqli_error());
		$num = mysqli_num_rows($query);
		echo "<h4>Conservation status</h4>\n";
		echo "<ul>\n";
		if ($num > 0) {
			while($res = mysqli_fetch_array($query)) {
				switch ($res['C_STATUS']) {
					case 'Not Evaluated':
						$img_status = "status_iucn3.1_NE.png";
						break;
					case 'Data Deficient':
						$img_status = "status_iucn3.1_DD.png";
						break;
					case 'Least Concern':
						$img_status = "status_iucn3.1_LC.png";
						break;
					case 'Near Threatened': 
						$img_status = "status_iucn3.1_NT.png";
						break;
					case 'Vulnerable':
						$img_status = "status_iucn3.1_VU.png";
						break;
					case 'Endangered':
						$img_status = "status_iucn3.1_EN.png";
						break;
					case 'Critically Endangered': 
						$img_status = "status_iucn3.1_CR.png";
						break;
					case 'Extinct in the Wild':
						$img_status = "status_iucn3.1_EW.png";
						break;
					case 'Extinct':
						$img_status = "status_iucn3.1_EX.png";
						break;
				}
				echo $res['C_STATUS']."<img border=0 src='../images/".$img_status."' />&nbsp;";
				switch ($res['C_TREND']) {
					case 'Decreasing':
						$img_trend = "down.png";
						break;
					case 'Increasing':
						$img_trend = "up.png";
						break;
					case 'Stable':
						$img_trend = "stable.png";
						break;
					case 'Unknown':
						$img_trend = "unknown.png";
						break;
				}
				echo $res['C_TREND']."<img border=0 src='../images/".$img_trend."' /> ";
				echo "<a name=cite-".$res['B_NO'].">[<a href=#note-".$res['B_NO'].">".$res['B_NO']."</a>]\n";
				$biblio[$index] = $res['B_NO'];
				$index += 1;		
			}
		}
		else {
			echo "<i>No status available</i>\n";
		}
		echo "</ul>\n\n";
		echo "<p align=\"right\">\n";
		echo "<a href=\"#top\">return to top</a>\n";
	}
		
	if ($config['morph']) {
		// Morphological Descriptors
		echo "<table class=\"report\" width=\"100%\">\n";
		echo "<tr><th><font size=+1>Morphology</font></th></tr>\n";
		echo "</table>\n";
		$grpCheck = '';
		$grpEval = '';
		$subCheck = '';
		$subEval = '';
		$sql = "SELECT * FROM descriptors WHERE T_NO='$id'";
		$sql = $sql." ORDER BY D_CHARACTER, D_STATE";
		$query = mysqli_query($link, $sql) or die("Query error: ".mysqli_errno().": ".mysqli_error());
		$num = mysqli_num_rows($query);
		echo "<h4>Descriptors</h4>\n";
		echo "<ul>\n";
		if ($num > 0) {
			while($res = mysqli_fetch_array($query)) {
				$grpCheck = $res['D_CHARACTER'];
				if ($grpEval != $grpCheck) {
					$grpEval = $grpCheck;
					echo "<br>".$grpCheck.": ";
				}
				$subCheck = $res['D_STATE'];
				if ($subEval != $subCheck) {
					$subEval = $subCheck;
					echo $subCheck."<a name=cite-".$res['B_NO']."> [<a href=#note-".$res['B_NO'].">".$res['B_NO']."</a>], \n";
				}
				$biblio[$index] = $res['B_NO'];
				$index += 1;
			}
		}
		else {
			echo "<i>No descriptors available</i>\n";
		}
		echo "</ul>\n\n";
		echo "<p align=\"right\">\n";
		echo "<a href=\"#top\">return to top</a>\n";
	}
	
	if ($config['gene']) {
		// Genomics
		echo "<table class=\"report\" width=\"100%\">\n";
		echo "<tr><th><font size=+1>Genomics</font></th></tr>\n";
		echo "</table>\n";
		
		$sql = "SELECT DISTINCT(G_TAXID) FROM genome WHERE T_NO='$id'";
		$query = mysqli_query($link, $sql) or die("Query error: ".mysqli_errno().": ".mysqli_error());
		$num_rows = mysqli_num_rows($query);
		if ($num_rows > 0) {
			$taxId = mysqli_result($query, 0, 'G_TAXID');
			$sql = "SELECT COUNT(*) FROM genome WHERE G_SEQ_TYPE='Nucleotide'";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$nucNum = mysqli_result($query, 0, 'COUNT(*)');
			$sql = "SELECT COUNT(*) FROM genome WHERE G_SEQ_TYPE='Protein'";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$protNum = mysqli_result($query, 0, 'COUNT(*)');
		}

		echo "<h4>Genomics from NCBI</h4>\n";
		echo "<ul>\n";
		if ($num_rows > 0) {
			$urlId = "http://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?mode=Info&id=".$taxId;
			$urlNuc = "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=Nucleotide&cmd=Search&dopt=DocSum&term=txid".$taxId."[Organism:exp]";
			$urlProt = "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=Protein&cmd=Search&dopt=DocSum&term=txid".$taxId."[Organism:exp]";
			echo "TaxId: <a href=".$urlId.">".$taxId."</a> ";
			echo "Sequences: "."<a href=".$urlNuc.">".$nucNum."</a> nucleotide, "."<a href=".$urlProt.">".$protNum."</a> protein\n";
		}
		else {
			echo "<i>No genomic data available</i>\n";
		}
		echo "</ul>\n\n";
		echo "<p align=\"right\">\n";
		echo "<a href=\"#top\">return to top</a>\n";
	}	

	if ($config['common']) {
		// Uses
		echo "<table class=\"report\" width=\"100%\">\n";
		echo "<tr><th><font size=+1>Uses</font></th></tr>\n";
		echo "</table>\n";
		$sql = "SELECT * FROM uses WHERE T_NO='$id'";
		$sql = $sql." ORDER BY U_NAME";
		$query = mysqli_query($link, $sql) or die("Query error: ".mysqli_errno().": ".mysqli_error());
		$num = mysqli_num_rows($query);
		echo "<h4>Uses</h4>\n";
		echo "<ul>\n";
		if ($num > 0) {
			while($res = mysqli_fetch_array($query)) {
				echo $res['U_NAME'];
				echo "<a name=cite-".$res['B_NO']."> [<a href=#note-".$res['B_NO'].">".$res['B_NO']."</a>]; \n";
				$biblio[$index] = $res['B_NO'];
				$index += 1;
			}
		}
		else {
			echo "<i>No uses available</i>\n";
		}
		echo "</ul>\n\n";
		echo "<p align=\"right\">\n";
		echo "<a href=\"#top\">return to top</a>\n";	
	}

	// Notes
	echo "<table class=\"report\" width=\"100%\">\n";
	echo "<tr><th><font size=+1>Remarks</font></th></tr>\n";
	echo "</table>\n";

	$sql = "SELECT * FROM notes WHERE T_NO='$id'";
	$query = mysqli_query($link, $sql) or die("Query error: ".mysqli_errno().": ".mysqli_error());
	$num = mysqli_num_rows($query);
	echo "<h4>Notes</h4>\n";
	echo "<ul>\n";
	if ($num > 0) {
		while($res = mysqli_fetch_array($query)) {
			echo $res['N_NOTE'];
			echo "<a name=cite-".$res['B_NO']."> [<a href=#note-".$res['B_NO'].">".$res['B_NO']."</a>]; \n";
			$biblio[$index] = $res['B_NO'];
			$index += 1;
		}
	}
	else {
		echo "<i>No notes available</i>\n";
	}
	echo "</ul>\n\n";
	echo "<p align=\"right\">\n";
    echo "<a href=\"#top\">return to top</a>\n";
	
	if ($config['geog']) {
		// Geographical Records
		echo "<table class=\"report\" width=\"100%\">\n";
		echo "<tr><th><font size=+1>Distribution</font></th></tr>\n";
		echo "</table>\n";
		$grpCheck = '';
		$grpEval = '';
		$sql = "SELECT DISTINCT T_NO, P_CONTINENT, P_COUNTRY, P_I_STATUS, B_NO FROM distribution WHERE T_NO='$id' ORDER BY P_CONTINENT, P_COUNTRY";
		$query = mysqli_query($link, $sql) or die("Query error: ".mysqli_errno().": ".mysqli_error());
		$num = mysqli_num_rows($query);
		echo "<h4>Geographical records</h4>\n";
		echo "<ul>\n";
		if ($num > 0) {
			while($res = mysqli_fetch_array($query)) {
				$grpCheck = $res['P_CONTINENT'];
				if ($grpEval != $grpCheck) {
					$grpEval = $grpCheck;
					echo "<p><b>".$grpCheck."</b>: ";
				}
				if (strlen($res['P_I_STATUS']) > 0) {
					echo $res['P_COUNTRY']." (".$res['P_I_STATUS'].") ";
					echo "<a name=cite-".$res['B_NO'].">[<a href=#note-".$res['B_NO'].">".$res['B_NO']."</a>], \n";
					$biblio[$index] = $res['B_NO'];
					$index += 1;
				}
			}
		}
		else {
			echo "<i>No geographical records available</i>\n";
		}
		echo "</ul>\n\n";
		echo "<p align=\"right\">\n";
		echo "<a href=\"#top\">return to top</a>\n";
		
		// OpenLayers
		if ($config['maps']) {
			echo "<h4>Distribution Map</h4>\n";
			echo "<ul>\n";
			
			$fn = "../maps/species.kml";
			writeToKml($fn, $id, $link);
			
			echo "<div id=\"map\" class=\"smallmap\"></div>\n";
			echo "<script src=\"../maps/lib/OpenLayers.js\"></script>";
			echo "
			<script type=\"text/javascript\">
				var options = {
					projection: new OpenLayers.Projection(\"EPSG:900913\"),
					displayProjection: new OpenLayers.Projection(\"EPSG:4326\"),
					controls: []
				};
			
				var map = new OpenLayers.Map('map', options);

				// Political borders
				var osm = new OpenLayers.Layer.OSM(
					\"Political Borders\", 
					null, 
					{transitionEffect: 'resize'}
				);
				
				// Physical map
				var wms = new OpenLayers.Layer.WMS(
					\"Physical Map\",
					\"http://vmap0.tiles.osgeo.org/wms/vmap0\",
					{layers: 'basic'}
				);
				
				// Satellite imagery
				var gwc = new OpenLayers.Layer.WMS(
					\"Satellite Imagery\",
					\"http://maps.opengeo.org/geowebcache/service/wms\",
					{layers: 'bluemarble'}
				);
			
				map.addLayers([osm, wms, gwc]);
			
				// KML data
				var species = new OpenLayers.Layer.Vector(\"$name\", {
					projection: map.displayProjection,
					strategies: [new OpenLayers.Strategy.Fixed()],
					protocol: new OpenLayers.Protocol.HTTP({
						url: \"../maps/species.kml\",
						format: new OpenLayers.Format.KML({
							extractStyles: true,
							extractAttributes: true
						})
					}),
					styleMap: new OpenLayers.StyleMap({
						pointRadius: 5,
						fillOpacity: 1.0,
						fillColor: \"#ff0000\", 
						strokeColor: \"#000000\", 
						strokeWidth: 1
					})
				});
			
				map.addLayers([species]);
				map.addControl(new OpenLayers.Control.Navigation());
				map.addControl(new OpenLayers.Control.PanZoom());
				map.addControl(new OpenLayers.Control.LayerSwitcher());
				map.addControl(new OpenLayers.Control.MousePosition());
				map.addControl(new OpenLayers.Control.OverviewMap());
				map.addControl(new OpenLayers.Control.KeyboardDefaults());
            
				select = new OpenLayers.Control.SelectFeature(species, {hover: false});
						            
				species.events.on({
				    \"featureselected\": onFeatureSelect,
				    \"featureunselected\": onFeatureUnselect
				});
			
				map.addControl(select);
				select.activate();
			
				if (!map.getCenter()) {
					map.setCenter(new OpenLayers.LonLat(0, 0), 1);
					map.zoomToMaxExtent();
				}
		
				function onPopupClose(event) {
					//var feature = event.feature;
					select.unselectAll();
					//feature.unselectAll();
				}
		
				function onFeatureSelect(event) {
					var feature = event.feature;
					// Since KML is user-generated, do naive protection against Javascript.
					var content = \"<i>\" + feature.attributes.name + \"</i><hr>\" + feature.attributes.description;
					if (content.search(\"<script\") != -1) {
						content = \"Content contained Javascript! Escaped content below.<br>\" + content.replace(/</g, \"&lt;\");
					}
					popup = new OpenLayers.Popup.FramedCloud(\"chicken\", 
                            feature.geometry.getBounds().getCenterLonLat(),
                            new OpenLayers.Size(100,100),
                            content,
                            null, true, onPopupClose);
					feature.popup = popup;
					map.addPopup(popup);
				}
		
				function onFeatureUnselect(event) {
					var feature = event.feature;
					if (feature.popup) {
						map.removePopup(feature.popup);
						feature.popup.destroy();
						delete feature.popup;
					}
				}
			</script>\n";	
		
			echo "</ul>\n\n";
			echo "<p align=\"right\">\n";
			echo "<a href=\"#top\">return to top</a>\n";
		}
	}

	if ($config['ecol']) {
		// Habitats
		echo "<table class=\"report\" width=\"100%\">\n";
		echo "<tr><th><font size=+1>Ecology</font></th></tr>\n";
		echo "</table>\n";
		$grpCheck = '';
		$grpEval = '';
		$subCheck = '';	
		$subEval = '';
		$sql = "SELECT * FROM habitats WHERE T_NO='$id'";
		$query = mysqli_query($link, $sql) or die("Query error: ".mysqli_errno().": ".mysqli_error());
		$num = mysqli_num_rows($query);
		echo "<h4>Habitats</h4>\n";
		echo "<ul>\n";
		if ($num > 0) {
			while($res = mysqli_fetch_array($query)) {
				$grpCheck = $res['H_PLACE'];
				if ($grpEval != $grpCheck) {
					$grpEval = $grpCheck;
					echo "<b>".$grpCheck."</b>: ";
				}
				$subCheck = $res['H_HABITAT'];
				if ($subEval != $subCheck) {
					$subEval = $subCheck;
					echo $subCheck."<a name=cite-".$res['B_NO']."> [<a href=#note-".$res['B_NO'].">".$res['B_NO']."</a>], \n";
				}
				$biblio[$index] = $res['B_NO'];
				$index += 1;
			}
		}
		else {
			echo "<i>No habitats available</i>\n";
		}
		echo "</ul>\n\n";
		echo "<p align=\"right\">\n";
		echo "<a href=\"#top\">return to top</a>\n";
	}
	
	// Media resources
	echo "<table class=\"report\" width=\"100%\">\n";
	echo "<tr><th><font size=+1>Media</font></th></tr>\n";
	echo "</table>\n";
	$sql = "SELECT * FROM resources WHERE T_NO='$id'";
	$query = mysqli_query($link, $sql) or die("Query error: ".mysqli_errno().": ".mysqli_error());
	echo "<h4>Media</h4>\n";
	echo "<ul>\n";
	
	$images = 0;
	echo "<h4>Images</h4>\n";
	while($res = mysqli_fetch_array($query)) {
		if (ucfirst($res['R_TYPE']) == "Photo" or ucfirst($res['R_TYPE']) == "Illustration") {
			$refUrl = "<a href='".$res['R_RESOURCE']."'>";
			echo $refUrl."<img src='".$res['R_RESOURCE']."' title='".$res['R_CAPTION']."' alt='".$res['R_CAPTION']."' style='width:150px' data-plusimage='".$res['R_RESOURCE']."' data-plussize='600,450'></a>\n";
			$images += 1;
		}
	}
	if ($images == 0) {
		echo "<i>No images available</i>\n";
	}
	
	$sql = "SELECT * FROM resources WHERE T_NO='$id'";
	$query = mysqli_query($link, $sql) or die("Query error: ".mysqli_errno().": ".mysqli_error());
	$sounds = 0;
	echo "<h4>Sounds</h4>\n";
	while($res = mysqli_fetch_array($query)) {
		if (ucfirst($res['R_TYPE']) == "Audio") {
			echo "<embed src='".$res['R_RESOURCE']."' autostart=\"false\" loop=\"false\" height=109 width=145>
				<noembed>Sorry, your browser doesn't support the embedding of multimedia.</noembed>
				</embed>\n";
				$sounds += 1;
			}
	}
	if ($sounds == 0) {
		echo "<i>No sounds available</i>\n";
	}	
	
	$sql = "SELECT * FROM resources WHERE T_NO='$id'";
	$query = mysqli_query($link, $sql) or die("Query error: ".mysqli_errno().": ".mysqli_error());
	$movies = 0;
	echo "<h4>Movies</h4>\n";
	while($res = mysqli_fetch_array($query)) {
		if (ucfirst($res['R_TYPE']) == "Video") {
			if (stripos($res['R_RESOURCE'], "youtube")) {
				echo "<embed width=145 height=109 src='".$res['R_RESOURCE']."'type=\"application/x-shockwave-flash\"></embed><br>\n";
			}
			else {
				echo "<embed src='".$res['R_RESOURCE']."' autostart=\"false\" loop=\"false\">
					<noembed>Sorry, your browser doesn't support the embedding of multimedia.</noembed>
					</embed>\n";
			}
			$movies += 1;	
		}
	}
	if ($movies == 0) {
		echo "<i>No movies available</i>\n";
	}	
	echo "</ul>\n\n";
	echo "<p align=\"right\">\n";
    echo "<a href=\"#top\">return to top</a>\n";
	
	// Bibliography
	echo "<table class=\"report\" width=\"100%\">\n";
	echo "<tr><th><font size=+1>References</font></th></tr>\n";
	echo "</table>\n";
	$biblio = array_unique($biblio);
	$sql = "SELECT * FROM bibliography ORDER BY B_NO";
	$query = mysqli_query($link, $sql) or die("Query error: ".mysqli_errno().": ".mysqli_error());
	$refs = 0;
	echo "<dl>\n";
	while($res = mysqli_fetch_array($query)) {
		if (in_array($res['B_NO'], $biblio)) {
			echo "<dd><a href=#cite-".$res['B_NO'].">^</a><a name=note-".$res['B_NO'].">".$res['B_NO']."</a>. ";
			echo $res['B_AUTHOR']." (".$res['B_YEAR'].$res['B_SEQUENCE'].") ";
			if (!empty($res['B_TITLE'])) {
				echo $res['B_TITLE'].". ";
			}
			echo $res['B_DETAIL']."</dd>\n";
			$refs += 1;
		}	
	}
	if ($refs == 0) {
		echo "<dd><i>No references available</i>\n";
	}
	echo "</dl>\n";
	echo "<p align=\"right\">\n";
    echo "<a href=\"#top\">return to top</a>\n";
	echo "<p>\n\n";

	// Links
	echo "<table class=\"report\" width=\"100%\">\n";	
	echo "<tr><th><font size=+1>Links</font></th></tr>\n";
	echo "</table>\n";
	echo "<ul>\n";
    echo '<li><a href="http://www.biodiversitylibrary.org/name/'.$genus.'_'.$species.'">Biodiversity Heritage Library</a></li>'."\n";
	echo '<li><a href="http://www.catalogueoflife.org/annual-checklist/'.date("Y").'/search/all/key/'.$genus.'+'.$species.'">Catalogue of Life</a></li>'."\n";
	echo '<li><a href="http://www.eol.org/search?q='.$genus.'+'.$species.'">Encyclopedia of Life</a></li>'."\n";
	echo '<li><a href="http://www.ncbi.nlm.nih.gov/sites/entrez?db=nuccore&term='.$genus.'+'.$species.'">GenBank</a></li>'."\n";
	echo '<li><a href="http://data.gbif.org/search/taxa/'.$genus.'+'.$species.'">Global Biodiversity Information Facility</a></li>'."\n";
	echo '<li><a href="http://www.itis.gov/servlet/SingleRpt/SingleRpt?search_topic=Scientific_Name&search_value='.$genus.'%20'.$species.'&search_kingdom=every&search_span=exactly_for&categories=All&source=html&search_credRating=All">Integrated Taxonomic Information System</a></li>'."\n";
	if (ucfirst($kingdom) == "Plantae") {
		echo '<li><a href="http://www.theplantlist.org/tpl/search?q='.$genus.'+'.$species.'">The Plant List</a></li>'."\n";
	}
	echo '<li><a href="http://www.marinespecies.org/aphia.php?p=taxlist&tName='.$genus.'%20'.$species.'">World Register of Marine Species</a></li>'."\n";
	echo "\n</ul>\n\n";
	echo "<p align=\"right\">\n";
    echo "<a href=\"#top\">return to top</a>\n";
	
	mysqli_free_result($query);
	mysqli_close($link);
?>
<hr>
<p align="center">
<a class="footer" href="http://sites.google.com/site/acaciadb/" target=_blank>Powered by ACACIA</a></p>
</body>
</html>