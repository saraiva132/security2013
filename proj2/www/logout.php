<?php
include 'utils/function.php';
if (empty($_SESSION)) {
   session_start();
}
unset($_SESSION['username']);
session_destroy();
$_SESSION['on'] = FALSE;
redirect('index.php');
?>
