var vm = new Vue({
	el : '#app',
	data : function() {
		return {
			userInfo : {
				realName : ''
			},
			tableData : [ {
				date : '2016-05-02',
				name : '王小虎',
				address : '上海市普陀区金沙江路 1518 弄'
			}, {
				date : '2016-05-04',
				name : '王小虎',
				address : '上海市普陀区金沙江路 1517 弄'
			} ]
		}
	},
	created : function() {
		this.getUserInfo();
	},
	methods : {
		getUserInfo : function() {
			axios.get(zuulProxy+'/user/getUserByUserName/JACK').then(function(response) {
				vm.userInfo = response.data;
			});
		}
	}
});