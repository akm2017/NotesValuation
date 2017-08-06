var app = angular.module('NoteApp');

app.controller('NoteDetailCtrl', NoteDetailCtrl);
app.controller('RowEditCtrl', RowEditCtrl);
app.service('RowEditor', RowEditor);

NoteDetailCtrl.$inject = ['$scope', '$http','$auth', '$rootScope', '$uibModal', 'RowEditor', 'uiGridConstants', 'noteDetailModel','noteUploadAPI','NoteService','WaitingDialog'];
function NoteDetailCtrl($scope, $http,$auth, $rootScope, $uibModal, RowEditor, uiGridConstants, noteDetailModel,noteUploadAPI,NoteService,WaitingDialog) {
  var vm = this;
  $scope.noteDetailModel = noteDetailModel;
  vm.getNoteDetail = RowEditor.getNoteDetail;
  vm.noteAnalyzer = function() {
		NoteService.noteAnalyze(vm.inputZipCode);
	};
	  
  $scope.uploadFile = function() {
	  NoteService.uploadNoteFile($scope.myFile, noteUploadAPI);
  };

  $scope.noteSearch = function(){
	  angular.forEach( $scope.selectedCityList, function( value, key ) {    
		   	console.log('selected'+$scope.selectedCityList);
		   	console.log('value'+value);
		   	console.log('key'+key);
		});
	  
	  angular.element(document.querySelector('.collapse')).collapse("hide");
  }
  

  
  vm.serviceGrid = {
    enableRowSelection: true,
    enableRowHeaderSelection: false,
    multiSelect: false,
    enableSorting: false,
    enableFiltering: true,
    enableGridMenu: false,
    enableCellEdit: false,
    enablePaginationControls:true,
    paginationPageSizes: [10,25, 50, 75],
    paginationPageSize: 10,
    rowHeight:40,
          
    rowTemplate : "<div ng-dblclick=\"grid.appScope.vm.getNoteDetail(grid, row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
  };

  vm.serviceGrid.columnDefs = [
	  {
		    field: 'noteAddress',
		    displayName: 'Address',
		    enableSorting: true,
		    enableFiltering: true,
		    headerCellClass:'addressHeaderClass',
		    cellClass:'uiGridCellClass',
		    width:260,
		    cellTemplate: "<a href =\"#\"><div ng-click=\"grid.appScope.vm.getNoteDetail(grid, row)\">{{row.entity.noteAddress}}</div></a>"
		  },
{    field: 'yield',
    displayName: 'Yield',
    enableSorting: true,
    enableCellEdit: false,
    enableFiltering: false,
    cellClass:'uiGridCellClass',
    cellTemplate: "<div>{{row.entity.yield}}</div>"
  }, {
    field: 'itv',
    displayName: 'ITV',
    enableSorting: true,
    enableCellEdit: false,
    enableFiltering: false,
    cellClass:'uiGridCellClass',
    cellTemplate: "<div>{{row.entity.itv}}</div>"
  }, {
    field: 'ltv',
    displayName: 'LTV',
    enableSorting: true,
    enableCellEdit: false,
    enableFiltering: false,
    cellClass:'uiGridCellClass',
    cellTemplate: "<div>{{row.entity.ltv}}</div>"
  },{
	    field: 'crime',
	    displayName: 'Crime',
	    enableSorting: true,
	    enableCellEdit: false,
	    enableFiltering: false,
	    cellClass:'uiGridCellClass',
	    cellTemplate: "<div>{{row.entity.crime}}</div>"
	  },{
    field: 'marketPrice',
    displayName: 'Market Price',
    enableSorting: true,
    enableCellEdit: false,
    enableFiltering: false,
    cellClass:'uiGridCellClass',
    sort: {
      direction: uiGridConstants.ASC,
      priority: 1,
    },
    cellTemplate: "<div>{{row.entity.marketPrice}}</div>"
  }];

  $scope.init = function(){
    WaitingDialog.show();
	  $http.get('api/fetchAllNotes').then(function(response) {
		  $scope.vm.serviceGrid.data = response.data;
	  }, function(response) {
		  $scope.vm.serviceGrid.data = [];
		  $auth.checkLoginFromServer(response.status);
	  }).then(function(){
		  WaitingDialog.hide();
	  });
	  
  }


}

RowEditor.$inject = ['$http', '$rootScope', '$uibModal','NoteService','toastr','$state'];
function RowEditor($http, $rootScope, $uibModal,NoteService,toastr,$state) {
  var service = {};
  service.getNoteDetail = getNoteDetail;

 function getNoteDetail(grid, row) {
	  NoteService.getNoteDetail(row.entity.noteId).then(function(response) {
		  NoteService.setNoteDetailModel(response);
		  $state.go('noteDetail');
	/*	  $uibModal.open({
		      templateUrl: 'static/template/note-detail.html',
		      controller: ['$http','$scope', '$uibModalInstance', 'grid','noteDetailModel', 'row','NoteService','toastr', RowEditCtrl],
		      controllerAs: 'editCtrl',
		      resolve: {
		        grid: function() {
		          return grid;
		        },
		        noteDetail: function() {
		          return response.data;
		        }
		      }
		    });*/
	  },function(response) {
		  toastr.error("We are unable to find details for this note. Please try after sometime.")

	  });
 };
	  
	 /* function editRow(grid, row) {
    $uibModal.open({
      templateUrl: 'static/template/note-detail.html',
      controller: ['$http','$scope', '$uibModalInstance', 'grid','noteDetailModel', 'row','NoteService','toastr', RowEditCtrl],
      controllerAs: 'editCtrl',
      resolve: {
        grid: function() {
          return grid;
        },
        row: function() {
          return row;
        }
      }
    });
  }*/
	  

  return service;
}

function RowEditCtrl($http,$scope, $uibModalInstance, grid, noteDetail,NoteService,toastr) {
	var editCtrl = this;
	
	$scope.noteDetailModel = noteDetail;
	
	editCtrl.removeNote= function(){
		console.log('delete Notes..');
		NoteService.deleteNote(row.entity.noteId).then(function(response){
			toastr.success("Note is deleted successfully");
			$uibModalInstance.close(row.entity);
		},function(response){
			toastr.error("Unable to delete the note. Please try after sometime");
		});
	}
	editCtrl.saveNote= function(data){
		console.log('save Notes.. '+data+'  note'+$scope.noteDetailModel.rate);
		NoteService.editNote($scope.noteDetailModel).then(function(response){
			$scope.noteDetailModel = response.data;
			toastr.success("Note is updated successfully");
			$uibModalInstance.close(row.entity);
		},function(response){
			toastr.error("Unable to save the note. Please try after sometime");
		});
	}

	$scope.checkName = function(data){
		console.log('name  check Nname.'+data);
		
	}
	

}