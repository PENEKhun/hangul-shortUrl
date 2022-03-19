function copyToClipboard(val) {
    const t = document.createElement("textarea");
    document.body.appendChild(t);
    t.value = val;
    t.select();
    document.execCommand('copy');
    document.body.removeChild(t);
}

function makeToast(title, message) {
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

function makeCopyBtn(url) {
    var str = '<button type=\"button\" class=\"btn btn-secondary\" style=\"display:inline-block;\" onclick=\"copyToClipboard(\'' + url + '\');">복사하기</button>';
    return str;
}

function makeNotice(message) {
    var str = '<br/><div class="alert alert-dark" role="alert">';
    str += message;
    str += '</div>';
    return str;
}

function makeWait() {
    var str = '<div class="resultDiv" name="resultDiv"><div class="spinner-border" role="status"><span class="sr-only"></span></div>';
    return str;
}

function makeShort(token) {
    $(".resultDiv").html(makeWait()); //loading

    var data = $("#url").val();
    var messageDTO = {
        originUrl: data,
        'token': token
    };
    $.ajax({
        url: "/make/",
        data: messageDTO,
        type: "POST",
        timeout: 3000,
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
        error: function(xhr, status, error) {
            var jsonResponse = JSON.parse(xhr.responseText);
            var err = jsonResponse["message"];
            console.log(err);
            if (err == "redirect URL") {
                //리다이렉션되는 url
                var msg = "악성 url로 간주되어<br/>";
                msg += "짧게 만들 수 없습니다.";
            } else if (err == "bad security URL") {
                var msg = "애석하게도...<br/>";
                msg += "짧링은 더 짧아질 수 없습니다 ㅜㅜ";
            } else if (err == "no Input") {
                var msg = "링크를 입력해주세요";
            } else if (err == "link not found") {
                var msg = "실존하지 않는 링크입니다";
                msg += "링크를 확인해주세요.";
            } else if (err == "captcha error") {
                var msg = "캡챠인증에 실패했습니다.";
                msg += "새로고침 후 재시도 해보세요.";
            } else if (err == "no input") {
                var msg = "링크를 입력해주세요.";
            } else {
                var msg = "잘못된 링크입니다.<br/>";
                msg += "링크에 오탈자가 있는지 확인해주세요.";
            }


            $(".resultDiv").html(msg);
        }
    });
}