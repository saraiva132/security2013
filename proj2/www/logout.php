<?php
session_start();
session_destroy();
include 'utils/function.php';
redirect('index.php');
?>
