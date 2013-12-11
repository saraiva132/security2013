<?php
session_start();
require_once 'utils/function.php';
if(isset($_SESSION['on'])) {
   redirect('index.php');
   exit();
}
require 'theme/menu.php';
?>
<div class="container">
	<hgroup>
		<h1>Login</h1>
		<h2>Insert your citizen card into your card reader!</h2>
	</hgroup>
	<form>
		<div class=”field_container”>
			<label for="name">Username</label>
			<input type="text" name="username" pattern="[_a-zA-Z0-9]{4,16}" required>
		</div>
		<div class=”field_container”>
			<label for="password">Password</label>
			<input type="password" name="password" pattern="[a-zA-Z0-9]{4,16}" required>
		</div>
		<p>
			<input type="submit" name="submit" id="submit" value="Login" formmethod="post" formaction="secure/login_process.php"/>
		</p>
	</form>
</div>
<?php require 'theme/footer.php'?>
