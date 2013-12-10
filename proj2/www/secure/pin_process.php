<?php
session_start();
include '../utils/db.php';
include '../utils/function.php';
if (isset($_SESSION['on']) || !isset($_POST['account'])) {
   redirect('../index.php');
   exit();
}
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
$query = "SELECT * FROM passwd";
$result = $db->query($query);
$rows = 0;
while($row = $result->fetchArray() && $rows < 1) {
	$rows++;
}
if ($rows > 0) {
	$query = "SELECT max(uid) as uid FROM passwd";
	$result = $db->query($query);
	$row = $result->fetchArray();
	$uid = $row['uid'] + 1;
} else {
	$uid = 10000;
}
$length = config_length();
$pin = genpin($length);
$days = config_days();
$account = $_POST['account'];
$gid = 10000;
$home = '/home/' . $_POST['account'];
$bash = '/bin/bash';
$expdate = time() + 24 * 60 * 60 * $days;
$expflag = 0;
$retrycount = 0;
$query = "INSERT INTO passwd (uid, gid, username, password, home, bash, expdate, expflag, retrycount) VALUES (" . $uid . ", " . $gid . ", '" . $account . "', '" . $pin . "', '" . $home . "', '" . $bash . "', " . $expdate . ", " . $expflag . ", '" . $retrycount . "')";
$db->exec($query);
$query = "INSERT INTO useraccounts (username, account) VALUES ('" . $_SESSION['username'] . "', '" . $_POST['account'] . "')";
$db->exec($query);
$_SESSION['pin'] = $pin;
$_SESSION['account'] = $_POST['account'];
$_SESSION['has_pin'] = TRUE;
$_SESSION['account_state'] = 'Created';
redirect('../secure/account.php');
?>
