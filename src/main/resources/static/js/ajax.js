 function makeShort() {
      var data=$("#url").val();
      var messageDTO={
        result:data
    };
    $.ajax({
        url: "/dataSend",
        data: messageDTO,
        type:"POST",
    }).done(function (fragment) {
        $("#resultDiv").replaceWith(fragment);
    });
}