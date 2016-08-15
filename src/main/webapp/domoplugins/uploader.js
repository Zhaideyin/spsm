if(jQuery) {
$.fn.imguploader = function(opts) {
	var defaults = {
		urlrec: "#PIC"	
	};
	var options = $.extend(defaults, opts);
	var picker = $(this)
	var listid = (new Date()).getTime();
	picker.before("<div id='"+listid+"'></div>");
	var uploader = WebUploader.create({
		auto: true,
	    // swf文件路径
//	    swf: BASE_URL + '/js/Uploader.swf',

	    // 文件接收服务端。
	    server: 'imgfile/save.do',

	    // 选择文件的按钮。可选。
	    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
	    pick: picker,

	    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
	    resize: false
	});
	// 当有文件被添加进队列的时候
	uploader.on( 'fileQueued', function( file ) {
	    $('#'+listid).append( '<div id="' + file.id + '" class="item">' +
	        '<h4 class="info">' + file.name + '</h4>' +
	        '<p class="state">等待上传...</p>' +
	    '</div>' );
	});
	uploader.on( 'uploadProgress', function( file, percentage ) {
	    var $li = $( '#'+file.id ),
	        $percent = $li.find('.progress .progress-bar');

	    // 避免重复创建
	    if ( !$percent.length ) {
	        $percent = $('<div class="progress progress-striped active">' +
	          '<div class="progress-bar" role="progressbar" style="width: 0%">' +
	          '</div>' +
	        '</div>').appendTo( $li ).find('.progress-bar');
	    }

	    $li.find('p.state').text('上传中');

	    $percent.css( 'width', percentage * 100 + '%' );
	});
	uploader.on( 'uploadSuccess', function( file ,response) {
	    $( '#'+file.id ).find('p.state').text('已上传');
	    $(options.urlrec).val(response.path);
	    picker.before("<img style='height:20px' src='uploadFiles/uploadImgs/"+response.path+"' />")
	});

	uploader.on( 'uploadError', function( file ) {
	    $( '#'+file.id ).find('p.state').text('上传出错');
	});

	uploader.on( 'uploadComplete', function( file ) {
	    $( '#'+file.id ).find('.progress').fadeOut();
	});
}}


