<?php
include '../utils/db.php';
include '../utils/function.php';
if(empty($_SESSION)) {
	session_start();
}
if (!empty($_SESSION) || !isset($_POST['submit'])) {
   redirect('../index.php');
   exit();
}
if (isset($_POST['account'])) {
	if (!can_create_account($_SESSION['username'])) {
		redirect('../secure/account.php');
		exit();
	}
	$query = "SELECT * FROM passwd WHERE username = '" . $_POST['account'] . "'";
	$result = $db->query($query);
	$rows = 0;
	while($row = $result->fetchArray()) {
		$rows++;
	}
	if ($rows > 0) {
		redirect('../secure/account.php');
		exit();
	}
	$length = config_length();
	$pin = genpin($length);
	$days = config_days();
	$account = $_POST['account'];
	//TODO
	$uid = 
	$gid = 10000;
	$home = '/home/' . $_POST['account'];
	mkdir($home, 0755);
	chown($home, $uid . ':' . $gid);
	$bash = '/bin/bash';
	$expdate = time() + 24 * 60 * 60 * $days;
	$expflag = 0;
	$retrycount = 0;
	$query = "INSERT INTO passwd (uid, gid, username, password, home, bash, expdate, expflag, retrycount) VALUES (" . $uid . ", " . $gid . ", '" . $account . "', '" . $pin . "', '" . $home . "', '" . $bash . "', " . $expdate . ", " . $expflag . ", '" . $retrycount . "')";
	$db->exec($query);
	$query = "INSERT INTO useraccount (username, account) VALUES ('" . $_SESSION['username'] . "', '" . $_POST['account'] . "')";
	$db->exec($query);
	$_SESSION['pin'] = $pin;
	$_SESSION['account'] = $_POST['account'];
	$_SESSION['has_pin'] = TRUE;
}
redirect('../secure/account.php');
?>
