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
			//注意user前面要/,加/表示从网站的根路径开始，不加则表示从当前路径，会出错
			axios.get('/user/getUserByUserName/JACK').then(function(response) {
				vm.userInfo = response.data;
			});
		}
	}
});