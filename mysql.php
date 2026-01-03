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
//$host = "your host";      
//$un   = "username";    
//$pw   = "password";       
//$db   = "database"; 

//$MYSQLI_CONNECT = mysqli_connect($host, $un, $pw, $db);

//function mysql_query($sql) {
//    global $MYSQLI_CONNECT;
//    return mysqli_query($MYSQLI_CONNECT, $sql);
//}

function mysql_connect($host, $user, $pw, $db) {
	return mysqli_connect($host, $user, $pw, $db);
}	

function mysql_query($sql, $link) {
    return mysqli_query($link, $sql);
}

function mysql_fetch_object($res) {
    return mysqli_fetch_object($res);
}

function mysql_fetch_assoc($res) {
    return mysqli_fetch_assoc($res);
}

function mysql_fetch_array($res){
    return mysqli_fetch_array($res, MYSQLI_BOTH);
}

function mysql_fetch_row($res){
    return mysqli_fetch_row($res);
}

function mysql_num_rows($res){
    return mysqli_num_rows($res);
}

//function mysql_insert_id() {
//   global $MYSQLI_CONNECT;
//    return mysqli_insert_id($MYSQLI_CONNECT);
//}

function mysql_insert_id($link) {
    return mysqli_insert_id($link);
}

//function mysql_real_escape_string($string) {
//    global $MYSQLI_CONNECT;
//    return mysqli_real_escape_string($MYSQLI_CONNECT, $string);
//}

function mysql_real_escape_string($string, $link) {
    return mysqli_real_escape_string($link, $string);
}

function mysql_close($link) {
	return mysqli_close($link);
}

function mysql_free_result($res) {
	return mysqli_free_result($res);
}

function mysql_result($res, $row = 0, $col = 0) { 
		$numrows = mysqli_num_rows($res); 
		if ($numrows && $row <= ($numrows - 1) && $row >= 0){
			mysqli_data_seek($res, $row);
			$resrow = (is_numeric($col)) ? mysqli_fetch_row($res) : mysqli_fetch_assoc($res);
			if (isset($resrow[$col])){
				return $resrow[$col];
			}
		}
		return false;
	}
?>