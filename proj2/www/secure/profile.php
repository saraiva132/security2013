<?php
include '../utils/function.php';
if(empty($_SESSION)) {
   session_start();
}
if(!$_SESSION['on']) {
   redirect('../login.php');
   exit();
}
?>
<?php include '../theme/menu.php'?>
<div class="container">
	<hgroup>
		<?php
		echo '<h2>Username: ' . $_SESSION['username'] . '</h2>';
		echo '<h2>Name: ' . $_SERVER['SSL_CLIENT_S_DN_CN'] . '</h2>';
		echo '<h2>Expiration Date: ' . $_SERVER['SSL_CLIENT_V_END'] . '</h2>';
		echo '<h2>' . $_SERVER['SSL_CLIENT_S_DN_OU'] . '</h2>';
		?>
	</hgroup>
</div>
<?php include '../theme/footer.php'?>
