<?php include '../theme/menu.php'?>
<div class="container">
	<hgroup>
		<h1>Local accounts.</h1>
		<h2>Renew your Linux accounts.</h2>
	</hgroup>
	<table id="accounts">
		<tr>
			<th>Account Name</th>
			<th>Expiration Date</th>
		</tr>
		<?php
		include '../utils/db.php';
		$query = "SELECT username, expdate, expflag FROM passwd JOIN useraccounts ON passwd.username = useraccounts.name WHERE useraccounts.name = '" . $_SESSION['username'] . "'";
		$result = $db->query($query);
		for($idx = 0; $row = $result->fetchArray(); $idx++) {
			if ($idx % 2 == 0) {
				echo '<tr class="alt">';
			} else {
				echo '<tr>';
			}
			echo '
				<td>' . $row['username'] . '</td>
				<td>' . date("m/d/y", $row['expdate']) . '</td>
			'; 
			echo '</tr>';
		}
		?>
	</table>
	<?php
	$result = $db->query($query);
	$rowsRENEW = 0;
	$rowsDELETE = 0;
	$listRENEW = '<select id = "renewList">';
	$listDELETE = '<select id = "deleteList">';
	while ($row = $result->fetchArray()) {
		if ($row['expflag'] || time() >= $row['expdate']) {
			$RENEW++;
			$list = $list . '<option value = "' . $rowsRENEW . '">' . $row['username'] . '</option>';
		}
		$rowsDELETE++;
		$list = $list . '<option value = "' . $rowsDELETE . '">' . $row['username'] . '</option>';
	}
	$listRENEW = $listRENEW . '</select>';
	$listDELETE = $listDELETE . '</select>';
	if ($rowsRENEW > 0) {
		echo '
			<form>
			' . $list . '
				<input type="submit" name="renew" id="renew" value="Renew" formmethod="post" formaction="secure/renew_account.php"/>
			</form>
		';
	}
	if ($rowsDELETE > 0) {
		echo '
			<form>
			' . $list . '
				<input type="submit" name="delete" id="delete" value="Delete" formmethod="post" formaction="secure/delete_account.php"/>
			</form>
		';
	}
	?>
</div>
<?php include '../theme/footer.php'?>
