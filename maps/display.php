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
<?php include("../library/functions.php"); ?>

<?php
	$link = mysql_connect($config['host'], $config['user'], $config['pwd']) or die("Connection error: ".mysql_errno().": ".mysql_error());
	$selected = mysql_select_db($config['dbname']) or die("Could not select ".$config['dbname']);
	$sql = "SELECT * FROM metadata";
	$query = mysql_query($sql, $link) or die("Error: MySQL query failed"); 
	$title = mysql_result($query, 0, 'M_TITLE');
	$pub = mysql_result($query, 0, 'M_PUBLISHER');
	$logo = mysql_result($query, 0, 'M_LOGO');
	$banner = mysql_result($query, 0, 'M_BANNER');
?>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="apple-mobile-web-app-capable" content="yes">
	<link rel="stylesheet" href="../library/stylesheet.css" type="text/css">
	<!--style type="text/css">
        #map {
            width: 600px;
            height: 400px;
            border: 1px solid black;
        }
    </style-->
	<script src="../library/functions.js"></script>
	<script src="lib/OpenLayers.js"></script>
    <script type="text/javascript">
        var map, select;
		var species = [];

        function init() {
			var options = {
                projection: new OpenLayers.Projection("EPSG:900913"),
                displayProjection: new OpenLayers.Projection("EPSG:4326"),
				controls: []
            };
			
            var map = new OpenLayers.Map('map', options);
			
			// Political map
			var osm = new OpenLayers.Layer.OSM(
				"Political Borders", 
				null, 
				{transitionEffect: 'resize'}
			);

			// Physical map
			var wms = new OpenLayers.Layer.WMS(
                "Physical Map",
                "http://vmap0.tiles.osgeo.org/wms/vmap0",
                {layers: 'basic'}
            );
			
			// Satellite imagery
			var gwc = new OpenLayers.Layer.WMS(
				"Satellite Imagery",
				"http://maps.opengeo.org/geowebcache/service/wms",
				{layers: 'bluemarble'}
			);
			
			map.addLayers([osm, wms, gwc]);
			
			// KML data
			<?php 
				$names = $_GET['name'];
				$species = implode(",", $names);
		
				$i = 1;
				foreach($names as $name) {
					$fn = "data/".strtr($name, ' ', '_').".kml";
					echo "
						var species".$i." = new OpenLayers.Layer.Vector(\"".$name."\", {"; 
					echo "	
							projection: map.displayProjection,
							strategies: [new OpenLayers.Strategy.Fixed()],
							protocol: new OpenLayers.Protocol.HTTP({
								url: \"$fn\",
								format: new OpenLayers.Format.KML({
									extractStyles: true,
									extractAttributes: true
								})
							}),
							styleMap: new OpenLayers.StyleMap({
								pointRadius: 5,
								fillOpacity: 1.0, //0.5,
								fillColor: randomColor(), 
								strokeColor: \"#000000\",
								strokeWidth: 1
							})
						});
					";
					
					echo "
						species[species.length] = species".$i."\n";
					
					$parts = explode(" ", $name);
					if (count($parts) == 3) {
						$sql = "SELECT T_NO FROM taxa WHERE T_GENUS='$parts[0]' AND T_SPECIES='$parts[1]' AND T_SUBSP='$parts[2]'";
					}
					else {
						$sql = "SELECT T_NO FROM taxa WHERE T_GENUS='$parts[0]' AND T_SPECIES='$parts[1]'";
					}
					$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
					$t_no = mysql_result($query, 0, 'T_NO');
					writeToKml($fn, $t_no);
				$i++;
				}
			?>
			
			for (var i = 0; i < species.length; i++) {
				map.addLayer(species[i]);
			}
			map.addControl(new OpenLayers.Control.Navigation());
			map.addControl(new OpenLayers.Control.PanZoom());
			map.addControl(new OpenLayers.Control.LayerSwitcher());
			map.addControl(new OpenLayers.Control.MousePosition());
			map.addControl(new OpenLayers.Control.OverviewMap());
			map.addControl(new OpenLayers.Control.KeyboardDefaults());
            
			select = new OpenLayers.Control.SelectFeature(species1, {hover: false});
			map.addControl(select);
            select.activate();
			
			if (!map.getCenter()) {
				map.setCenter(new OpenLayers.LonLat(0, 0), 1);
				map.zoomToMaxExtent();
			}
        }
	</script>
	<?php echo "<title>".$title." distribution maps</title>\n"; ?>
</head>

<body OnLoad="init()">
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
		?>
		| <a href="maps.php" title="Distribution maps">Maps</a>
		| <a href="../report/stats.php" title="Database statistics">Statistics</a>
		| <a href="../help/about.php" title="Get help">About</a>
		]
		</td>
	</tr>	
</table>

<center>
	<?php 
		echo "<h3>Distribution map for:</h3>\n";
		echo "<p><i>".$species."</i></p>\n";
	?>
	<div id="map" class="smallmap"></div>
</center>

<?php
	mysql_free_result($query);
	mysql_close($link);
?>

<p>Powered by <a href="http://www.openlayers.org/" target="_blank">OpenLayers v. 2.0</a><br>

<p>
Download as: <a href="../export/excel.php?table=distribution&field=T_NO">Excel</a> | <a href="../export/csv.php?table=distribution&field=T_NO">CSV</a> | <a href="../export/kml.php?table=distribution&field=T_NO">KML</a>
</p>
<hr>
<p align="center">
<a class="footer" href="http://sites.google.com/site/acaciadb/" target=_blank>Powered by ACACIA</a></p>
</body>
</html>