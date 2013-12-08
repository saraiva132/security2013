<?php
$filename='/var/www/db/users.sqlite';
$db = new SQLite3($filename);
if (!$db) {
	header('HTTP/1.1 500 Internal Server Error');
}
?>
