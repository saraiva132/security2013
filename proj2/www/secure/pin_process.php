<?php
session_start();
require_once '../utils/db.php';
require_once '../utils/function.php';
if (!isset($_SESSION['on'])) {
	redirect('../login.php');
	exit();
}
if (!isset($_POST['account']) || !can_create_account($_SESSION['username'])) {
	redirect('../secure/account.php');
	exit();
}
$query = "SELECT * FROM passwd WHERE username = :account";
$stmt = $db->prepare($query);
$stmt->bindValue(':account', $_POST['account']);
$result = $stmt->execute();
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
while ($row = $result->fetchArray() && $rows < 1) {
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
$salt = gensalt();
$query = "INSERT INTO passwd (uid, gid, username, password, salt, home, bash, expdate, expflag, retrycount) VALUES (" . $uid . ", " . $gid . ", :account, '" . salt_hash($pin, $salt) . "', '" . $salt . "', :home, '" . $bash . "', " . $expdate . ", " . $expflag . ", '" . $retrycount . "')";
$stmt = $db->prepare($query);
$stmt->bindValue(':account', $account);
$stmt->bindValue(':home', $home);
$result = $stmt->execute();
$query = "INSERT INTO useraccounts (username, account) VALUES ( '" . $_SESSION['username'] . "', :account)";
$stmt = $db->prepare($query);
$stmt->bindValue(':account', $_POST['account']);
$result = $stmt->execute();
$_SESSION['pin'] = $pin;
$_SESSION['account'] = $_POST['account'];
$_SESSION['has_pin'] = TRUE;
$_SESSION['account_state'] = 'Created';
redirect('../secure/account.php');
?>
