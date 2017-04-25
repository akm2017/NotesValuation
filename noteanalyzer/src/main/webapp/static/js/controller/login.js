var app = angular.module('NoteApp');
app.controller('LoginCtrl', function($scope,$rootScope,$state, $location, toastr, loginService, loginModel,$stateParams) {
  $scope.login = function() {
    //call https in post to get accesstoken then put into localstoarge
    loginModel.username = $scope.user.email;
    loginModel.password = $scope.user.password;
    loginService.doLogin(loginModel).then(function(response) {
      console.log('token' + response.token)
      localStorage.setItem('token', response.token);
      toastr.success('You have successfully signed in with give n user name');
      console.log('refer Value is  '+$stateParams.referer);
      if($stateParams.referer){
        $state.go($stateParams.referer,{'loginState':$stateParams.loginState});
      }else{
      $location.path('/');
      }
    }, function(response) {
      toastr.error(response.message);
      localStorage.removeItem('token');
      $location.path('/');
    });
  };

});
app.service("loginService", function($http, $q) {

  var deferred = $q.defer();

  var doLogin = function(loginModel) {
    //put the localhost:8080/notes/api/login here
    return $http.post('api/auth/login', loginModel)
      .then(function(response) {
        // promise is fulfilled
        deferred.resolve(response.data);
        return deferred.promise;
      }, function(response) {
        // the following line rejects the promise 
        deferred.reject(response);
        return deferred.promise;
      });
  };

  return {
    doLogin: doLogin
  }
});
app.constant("loginModel", {
  "username": "",
  "password": ""
});