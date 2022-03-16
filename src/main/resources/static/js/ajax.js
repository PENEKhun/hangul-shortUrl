function copyToClipboard(val) {
  const t = document.createElement("textarea");
  document.body.appendChild(t);
  t.value = val;
  t.select();
  document.execCommand('copy');
  document.body.removeChild(t);
}

function makeToast(title, message){
var str = '<div class="toast" role="alert" aria-live="assertive" aria-atomic="true">';
str += '<div class="toast-header">';
str += '<img src="..." class="rounded mr-2" alt="...">';
str += '<strong class="mr-auto">' + title + '</strong>';
str += '<small>11 mins ago</small>';
str += '<button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">';
str += '<span aria-hidden="true">&times;</span>';
str += '</button>';
str += '</div>';
str += '<div class="toast-body">';
str += message;
str += '</div>';
str += '</div>';
return str;
}

function makeCopyBtn(url){
var str = '<button type=\"button\" class=\"btn btn-secondary\" style=\"display:inline-block;\" onclick=\"copyToClipboard(\'' + url + '\');">복사하기</button>';
return str;
}

function makeNotice(message){
var str = '<br/><div class="alert alert-dark" role="alert">';
str += message;
str += '</div>';
return str;
}

function makeWait(){
var str = '<div class="resultDiv" name="resultDiv"><div class="spinner-border" role="status"><span class="sr-only"></span></div>';
return str;
}

 function makeShort() {
      $(".resultDiv").html(makeWait());
      var data=$("#url").val();
      var messageDTO={
        originUrl:data
    };
    $.ajax({
        url: "/make/",
        data: messageDTO,
        type:"POST",
        success: function(data) {

        var url = "http://" + $(location).attr('host') + "/" + data.body;

        var str = "링크 생성 성공!<br/>";
        str += url;
        str += "<br/>";
        str += makeCopyBtn(url);
        str += "<br/>";


        var msg = makeNotice(str);
           $(".resultDiv").html(msg);
                },
        error: function() {
                    alert('Error occured');
                }
    });
}