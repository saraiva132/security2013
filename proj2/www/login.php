<?php
if(empty($_SESSION)) {
   session_start();
}
if($_SESSION['on']) {
   header('location: debug.php');
   exit();
}
?>
<?php include 'theme/menu.php'?>
<div class="container">
	<section id="intro">
		<hgroup>
			<h1>Login</h1>
			<h2>Insert your citizen card into your card reader!</h2>
		</hgroup>
	</section>
	<form>
		<div class=”field_container”>
			<label for="name">Username</label>
			<input type="text" name="username" required>
		</div>
		<div class=”field_container”>
			<label for="password">Password</label>
			<input type="password" name="password" required>
		</div>
		<p>
			<input type="submit" name="submit" id="submit" value="Login" formmethod="post" formaction="secure/login_process.php"/>
		</p>
	</form>
</div>
<?php include 'theme/footer.php'?>
