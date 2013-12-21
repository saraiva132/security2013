<?php
function redirect($url) {
   header('Location: ' . $url);
   exit();
}
function genpin($length) {
	$characters = 'abcdefghijklmnopqrstuvwxyz0123456789';
	$string = '';
	for ($i = 0; $i < $length; $i++) {
		 $string .= $characters[rand(0, strlen($characters) - 1)];
	}
	return $string;
}
function config_length() {
	if (file_exists('/etc/www/manage/config.xml')) {
		$xml = simplexml_load_file('/etc/www/manage/config.xml');
		$length = $xml->pin->length;
		return (int)$length;
	} else {
		return 4;
	}
}
function config_days() {
	if (file_exists('/etc/www/manage/config.xml')) {
		$xml = simplexml_load_file('/etc/www/manage/config.xml');
		$days = $xml->pin->days;
		return (int)$days;
	} else {
		return 1;
	}
}
function can_create_account($username) {
	if (file_exists('/etc/www/manage/config.xml')) {
		$xml = simplexml_load_file('/etc/www/manage/config.xml');
		$permited = $xml->permited;
		foreach($permited->user as $user) {
			if (strcmp($username, $user) == 0) {
				return TRUE;
			}
		}
		return FALSE;
	} else {
		return FALSE;
	}
}
function max_accounts() {
	if (file_exists('/etc/www/manage/config.xml')) {
		$xml = simplexml_load_file('/etc/www/manage/config.xml');
	} else {
		return 3;
	}
	return (int)$xml->max;
}
function salt_hash($password, $salt) {
    return crypt($password, $salt);
}
function gensalt() {
	if (file_exists('/etc/www/manage/config.xml')) {
		$xml = simplexml_load_file('/etc/www/manage/config.xml');
		$algorithm = '$' . $xml->salt->algorithm . '$';
		$length = (int)$xml->salt->length;
	} else {
		$algorithm = '';
		$length = 21;
	}
	$characters = 'abcdefghijklmnopqrstuvwxyz0123456789';
	$salt = '';
	for ($i = 0; $i < $length; $i++) {
		 $salt .= $characters[rand(0, strlen($characters) - 1)];
	}
	return $algorithm . $salt;
}
function getbi() {
	$matches = array();
	preg_match('/BI([0-9]+)/', $_SERVER['SSL_CLIENT_S_DN'], $matches);
	return substr($matches[0],2,-1);
}
?>
