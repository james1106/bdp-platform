new Vue({
	el : '#nav',
	data : function() {
		return {
			activeIndex : '1',
			activeIndex2 : '1'
		};
	},
	methods : {
		handleSelect : function(key, keyPath) {
			console.log(key, keyPath);
		}
	}
});

new Vue({
	el : '#table',
	data : function() {
		return {
			tableData : [ {
				date : '2016-05-02',
				name : '王小虎',
				address : '上海市普陀区金沙江路 1518 弄'
			}, {
				date : '2016-05-04',
				name : '王小虎',
				address : '上海市普陀区金沙江路 1517 弄'
			}, {
				date : '2016-05-01',
				name : '王小虎',
				address : '上海市普陀区金沙江路 1519 弄'
			}, {
				date : '2016-05-03',
				name : '王小虎',
				address : '上海市普陀区金沙江路 1516 弄'
			} ]
		};
	}
});