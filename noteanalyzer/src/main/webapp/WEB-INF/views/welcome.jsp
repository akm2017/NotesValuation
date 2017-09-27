<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="Sat, 01 Dec 1997 00:00:00 GMT">
<title>Note Analyzer</title>
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.6.4/angular.min.js"></script>
	<script
		src="https://maps.googleapis.com/maps/api/js?key=AIzaSyB6lfbjsaLDihAIKq_mMXIhjCJYDZBlhXc&libraries=places"
		async defer></script>
 <link rel="stylesheet" href="static/css/jquery.calculator.css"/> 
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/themes/start/jquery-ui.min.css" /> 
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/angular-toastr/2.1.1/angular-toastr.css"
	rel="stylesheet" />
<link
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet" />
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.2/animate.css"
	rel="stylesheet" />
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-grid/4.0.4/ui-grid.css"
	rel="stylesheet" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-formhelpers/2.3.0/css/bootstrap-formhelpers.css" />
<link rel="stylesheet" href="static/lib/isteven-multi-select.css" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/angular-xeditable/0.7.1/css/xeditable.min.css" />
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery.bootstrapvalidator/0.5.3/css/bootstrapValidator.min.css" />
<link rel="shortcut icon" href="static/img/favicon.ico" />
<link rel="styleSheet" href="static/css/app.css" />
<link href="static/css/styles.css" rel="stylesheet"/>
<link rel="styleSheet" href="static/css/custom.css" />
<link rel="stylesheet" href="static/css/subscription-style.css"/>
<link type="text/css" rel="stylesheet"
	href="https://fonts.googleapis.com/css?family=Roboto:300,400,500"/>

<!-- 
<style>
#locationField, #controls {
	position: relative;
	width: 480px;
}

#autocomplete {
	position: absolute;
	top: 0px;
	left: 0px;
	width: 99%;
}

.label {
	text-align: right;
	font-weight: bold;
	width: 100px;
	color: #303030;
}

#address {
	border: 1px solid #000090;
	background-color: #f0f0ff;
	width: 480px;
	padding-right: 2px;
}

#address td {
	font-size: 10pt;
}

.field {
	width: 99%;
}

.slimField {
	width: 80px;
}

.wideField {
	width: 200px;
}

#locationField {
	height: 20px;
	margin-bottom: 40px;
}
</style> -->


</head>
<body>
<div ng-controller="NavbarCtrl">	
 <nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Menu</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <div style="width:50%"><a class="navbar-brand" href="/notes/#!/" style="padding-top:0px;padding-left:20%;"><img src="static/img/note_unlimited.png" alt="Note Unlimited"></a></div>
    </div>

    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
       <!--  <li id="home" class="active"><a href="/notes/#!/"><span class="glyphicon glyphicon-home"></span> Home <span class="sr-only">(current)</span></a></li> -->
        <!-- <li id="myProfile" ng-if="isAuthenticated()"><a href="/notes/#!/profile"><span class="glyphicon glyphicon-user"></span> My Profile</a></li> -->
       
      </ul>
     
      <ul class="nav navbar-nav navbar-right">
       	 <li id="noteDashboard" ng-if="isAuthenticated()"><a href="/notes/#!/noteDashboard"><span class="x-glyphicon x-glyphicon-th-list"></span> My Dashboard</a></li>
         <li id="calculator"><a href="/notes/#!/calculator"><span class="x-glyphicon x-glyphicon-calendar"></span> Calculator</a></li>
     	 <li id="login" ng-if="!isAuthenticated()"><a href="/notes/#!/login"><span class="x-glyphicon x-glyphicon-log-in"></span> Sign in</a></li>
     	 <li  ng-if="!isAuthenticated()" class="orHomepage">or</li>
		 <li id="signUp" ng-if="!isAuthenticated()"><a href="/notes/#!/signup"><span class="glyphicon x-glyphicon-user"></span> Join</a></li>
     	 <li id="profile" ng-if="isAuthenticated()"><a href="/notes/#!/profile"><span class="glyphicon x-glyphicon-user"></span><span id="welcomeUserName"> {{loggedInUserDisplayName}}</span></a></li>
		 <li id="logout" ng-if="isAuthenticated()"><a href="/notes/#!/logout"><span class="x-glyphicon x-glyphicon-log-out"></span> Logout</a></li>
      </ul>
    </div>
  </div>
</nav>
</div>
 

 <div ui-view></div>

	<!-- Third-party Libraries -->


	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.6.4/angular-animate.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/angular-messages/1.6.4/angular-messages.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/angular-resource/1.6.4/angular-resource.min.js"></script>

	<!-- Angular Material Library -->

	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/angular-sanitize/1.6.4/angular-sanitize.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-router/1.0.3/angular-ui-router.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/angular-toastr/1.7.0/angular-toastr.tpls.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/angular-touch/1.6.4/angular-touch.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-grid/4.0.4/ui-grid.min.js"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/2.5.0/ui-bootstrap-tpls.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-formhelpers/2.3.0/js/bootstrap-formhelpers.js"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/angular-xeditable/0.7.1/js/xeditable.min.js"></script>
	<!--[if lt IE 9]><script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
	<!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-multiselect/0.9.13/js/bootstrap-multiselect.min.js"></script> -->
	<script src="static/lib/isteven-multi-select.js"></script>
	
	<!-- Application Code -->
	<script src="static/note.js"></script>
	<script src="static/js/directives/passwordStrength.js"></script>
	<script src="static/js/directives/passwordMatch.js"></script>
	<script src="static/js/constant/constant.js"></script>
	<script src="static/js/controller/home.js"></script>
	<script src="static/js/controller/login.js"></script>
	<script src="static/js/controller/signup.js"></script>
	<script src="static/js/controller/logout.js"></script>
	<script src="static/js/controller/profile.js"></script>
	<script src="static/js/controller/noteDashboard.js"></script>
	<script src="static/js/controller/noteDetail.js"></script>
	<script src="static/js/service/userService.js"></script>
	<script src="static/js/service/noteService.js"></script>
	<script src="static/js/service/utilityService.js"></script>
	<script src="static/js/utility/jquery.plugin.min.js"></script>
	<script src="static/js/utility/jquery.calculator.js"></script>
	
	
	
</body>
<script type="text/javascript">
	angular.element(document).ready(function () {
    angular.bootstrap(document, ['NoteApp']);
    $(".nav li").on("click", function() {
	      $(".nav li").removeClass("active");
	      $(this).addClass("active");
	 });
    });
</script>

</html>
