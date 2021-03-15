<?php
global $config;
$config['host'] = "localhost"; // database server
$config['user'] = "root"; // database user
$config['pwd'] = ""; // database password
$config['dbname'] = "acacia"; // database name
$config['readonly'] = true; // database mode (false = edit, true = browse)
$config['morph'] = true; // morphology (descriptors) data class
$config['gene'] = true; // genome data class
$config['status'] = true; // conservation status data class
$config['common'] = true; // common knowledge (vernacular names, uses) data class
$config['ecol'] = true; // ecology (habitats) data class
$config['geog'] = true; // geography (distribution records) data class
$config['subphy'] = false; // subphylum higher taxon attribute
$config['supercla'] = false; // superclass higher taxon attribute
$config['subcla'] = false; // subclass higher taxon attribute
$config['infracla'] = false; // infraclass higher taxon attribute
$config['superord'] = false; // superorder higher taxon attribute
$config['subord'] = false; // suborder higher taxon attribute
$config['superfam'] = false; // superfamily higher taxon attribute
$config['subfam'] = false; // subfamily higher taxon attribute
$config['tribe'] = true; // tribe higher taxon attribute
$config['subgen'] = false; // subgenus higher taxon attribute
$config['subsp'] = true; // subspecies or variety taxon attribute
$config['keys'] = true; // identification keys option
$config['maps'] = true; // distribution maps option
?>