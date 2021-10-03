<?php
global $config;
$config['host'] = "localhost"; // database server
$config['user'] = "database user"; // database user
$config['pwd'] = "database password"; // database password
$config['dbname'] = "database name"; // database name
$config['readonly'] = true; // database mode (false = edit, true = browse)
$config['morph'] = true; // morphology (descriptors) data class
$config['gene'] = true; // genome data class
$config['status'] = true; // conservation status data class
$config['common'] = true; // common knowledge (vernacular names, uses) data class
$config['ecol'] = true; // ecology (habitats) data class
$config['geog'] = true; // geography (distribution records) data class
$config['subphy'] = true; // subphylum higher taxon attribute
$config['subcla'] = true; // subclass higher taxon attribute
$config['subord'] = true; // suborder higher taxon attribute
$config['superfam'] = true; // superfamily higher taxon attribute
$config['subfam'] = true; // subfamily higher taxon attribute
$config['tribe'] = true; // tribe higher taxon attribute
$config['subgen'] = true; // subgenus higher taxon attribute
$config['subsp'] = true; // subspecies or variety taxon attribute
$config['keys'] = true; // identification keys option
$config['maps'] = true; // distribution maps option
?>