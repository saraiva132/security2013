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
	if (file_exists('../manage/config.xml')) {
		$xml = simplexml_load_file('../manage/config.xml');
		$length = $xml->pin->length;
		return (int)$length;
	} else {
		exit('Failed to open test.xml.');
	}
}
function config_days() {
	if (file_exists('../manage/config.xml')) {
		$xml = simplexml_load_file('../manage/config.xml');
		$days = $xml->pin->days;
		return (int)$days;
	} else {
		exit('Failed to open test.xml.');
	}
}
function can_create_account($username) {
	if (file_exists('../manage/config.xml')) {
		$xml = simplexml_load_file('../manage/config.xml');
		$permited = $xml->permited;
		foreach($permited->user as $user) {
			if (strcmp($username, $user) == 0) {
				return TRUE;
			}
		}
		return FALSE;
	} else {
		exit('Failed to open test.xml.');
	}
}
?>
