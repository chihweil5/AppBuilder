<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>

<div class="container">
	<h1>Android Application Builder</h1>
	<br> 
	<form id="projectForm" method="post" class="form-horizontal">
		<label class="col-xs-2 control-label">Number of Projects:</label> 
		<div class="col-xs-2">
			<input type="text" class="form-control" name="numOfProj" />
		</div>
	

		<div class="form-group">
			<div class="col-xs-5 col-xs-offset-1">
				<button id="CloneBtn" type="submit" class="btn btn-primary btn-md">GO</button>
			</div>
		</div>
	</form>
</div>

<script>
		$('#numOfProj').keyup(function () {
		  	var $num = $(this).val();
		  	console.log( $num );
		});
</script>
<%@ include file="common/footer.jspf"%>
	


