<?php
$home = '/home/temp/lol';
if (!mkdir($home, 0755)) {
	echo 'cannot mkdir!';
	exit();
}
$uid = 10000;
$gid = 10000;
if (!chown($home, $uid . ':' . $gid)) {
	echo 'cannot chmod!';
	exit();
}
echo 'success!';
?>
