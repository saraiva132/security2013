<?php session_start(); ?>
<!DOCTYPE html>
<html id="default"> 
	<head>
		<meta charset="utf-8" />
		<title>Linux Authentication</title>
		<link rel="stylesheet" href="/css/style.css" />
	</head>
	<body>
		<header>
			<div id="header_middle">
				<div class="container">
					<div id="primary">
						<nav>
							<ul>
								<li><a href="/index.php">Home</a></li>
								<?php
								if (isset($_SESSION['on'])) {
									echo '<li><a href="/secure/profile.php">Profile</a></li>';
									echo '<li><a href="/secure/account.php">New Account</a></li>';
									echo '<li><a href="/secure/accounts.php">My Accounts</a></li>';
									echo '<li><a href="/logout.php">Logout</a></li>';
								} else {
									echo '<li><a href="/login.php">Login</a></li>';
									echo '<li><a href="/register.php">Register</a></li>';
								}
								?>
							</ul>
						</nav>
					</div>
					<div id="secondary">
						<div id="slogan">
							<h1>Linux Authentication</h1>
						</div>
					</div>
				</div>
				<div id="header_bottom">
			</div>
		</div>
	</header>
	<section>
