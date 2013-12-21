<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
$filename='/etc/www/db/users.sqlite';
$db = new SQLite3($filename);
if (!$db) {
	header('HTTP/1.1 500 Internal Server Error');
}
?>
