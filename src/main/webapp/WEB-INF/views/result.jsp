<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>


<div class="container">

	<h1>Meta-Data Query</h1>
	<br> <br>
	<form id="queryForm" method="get">
		<div class="form-group">
			<label class="col-xs-2 control-label">Search by: </label>
			<div class="col-xs-2">
				<select class="form-control" id="select1" name="select1" onchange="showOption()">
					<option value="">Please Select</option>
					<option value="Name">Repository Name</option>
					<option value="Status">Status</option>
					<option value="All">All</option>
				</select>
			</div>
			<div class="col-xs-2">
				<select class="form-control" id="select2" name="select2">
					<!--  onChange="search()"--> 
					<option value="">--</option>
				</select>
			</div>

			<div class="col-xs-1">
				<button class="btn btn-danger" type="submit">Query</button>
			</div>
<!-- 
			<div class="col-xs-1">
				<button class="btn btn-default" onclick="showAll()">Show All</button>
			</div>
 -->
		</div>
	</form>
	<br> <br>

	<table id="myTable" class="table table-striped">
		<thead>
			<tr>
				<th>User Name/Repository Name</th>
				<th>Status</th>
				<th>Data Information</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${_githubInfoList}" var="githubInfo" varStatus="i">
				<tr>
					<td>${githubInfo.username}/${githubInfo.reponame}</td>
					<td>${_Status[i.index]}</td>
					<c:if test="${_Status[i.index] != 'Built'}">
						<td>${_Msg[i.index]}</td>
					</c:if>
					<c:if test="${_Status[i.index] == 'Built'}">
						<td>Location: ${githubInfo.localpath}</td>
					</c:if>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<!--  
		<div>
			<a class="btn btn-primary" href="/appbuilder">Back</a>
		</div>
		-->
</div>

 
<script>

function showOption() {
	console.log("changing the option");
	var sel1 = document.getElementById('select1');
	var sel2 = document.getElementById('select2');
		
	if(sel1.value == "Name") {
		<c:forEach items="${repoList}" var="repo" varStatus="i">
			sel2.options[${i.index}] = new Option("${repo}", "${repo}");
		</c:forEach>
		/*for(i = 0; i < ${repoList}.length; i++) {
			console.log("Repository Name" + reponame);
			sel2.options[i] = new Option(reponame[i], reponame[i]);
		}*/
		
	} else if(sel1.value == "Status") {
			sel2.options[0] = new Option("Built", "Built");
			sel2.options[1] = new Option("Failed", "Failed");
			sel2.options[2] = new Option("Scheduled", "Scheduled");
			sel2.options[3] = new Option("Not Scheduled", "Not Scheduled");
			sel2.length = 4;
		}
		else {
			sel2.options[0] = new Option("All", "All");
			sel2.length = 1;
		}
	}
</script>


<!-- 
<script>
	function search() {
		// Declare variables 
		var input1, input2, table, tr, td, i;
		input1 = document.getElementById('select1');
		input2 = document.getElementById('select2');
		table = document.getElementById("myTable");
		tr = table.getElementsByTagName("tr");

		if (input1.value == "Name") {
			// Loop through all table rows, and hide those who don't match the search query
			for (i = 0; i < tr.length; i++) {
				td = tr[i].getElementsByTagName("td")[0];
				if (td) {
					if (td.innerHTML.indexOf(input2.value) > -1) {
						tr[i].style.display = "";
					} else {
						tr[i].style.display = "none";
					}
				}
			}
		} else if (input1.value == "Status") {
			for (i = 0; i < tr.length; i++) {
				td = tr[i].getElementsByTagName("td")[1];
				if (td) {
					if (td.innerHTML.indexOf(input2.value) > -1) {
						tr[i].style.display = "";
					} else {
						tr[i].style.display = "none";
					}
				}
			}
		}
		else {
			for (i = 0; i < tr.length; i++) {
				tr[i].style.display = "";
			}
		}
	}
</script>

<script>
	function showAll() {
		// Declare variables 
		var input1, input2, table, tr, td, i;
		table = document.getElementById("myTable");
		tr = table.getElementsByTagName("tr");
		for (i = 0; i < tr.length; i++) {
				tr[i].style.display = "";
		}
		
	}
</script>
-->
<!--  
<script>
window.location.reload(true);
</script>
-->
<%@ include file="common/footer.jspf"%>