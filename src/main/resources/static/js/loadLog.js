function drawChart(dayVisitor) {

    var label = [];
    var data = [];

    dayVisitor.slice().reverse().forEach(function(key) {
        label.push(key.day);
        data.push(key.visitor);
    });

    var ctx = document.getElementById('myChart')

    var myChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: label,
            datasets: [{
                data: data,
            }],
            lineTension: 0,
            backgroundColor: 'transparent',
            borderColor: '#007bff',
            borderWidth: 4,
            pointBackgroundColor: '#007bff'
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: false
                    }
                }]
            },
            legend: {
                display: false
            }
        }
    });
}

function writeRecentLog(log) {
    //최근 접속기록에 json데이터를 띄워주는 함수

    console.log(log);

    var sSrc = "";

    log.slice().reverse().forEach(function(key, index) {
        console.log(index);
        var idx = (Number(index) + 1);
        sSrc += "<tr>";
        sSrc += "<td>" + idx + "</td>"; //idx
        sSrc += "<td>" + key.country + "</td>"; //접속 국가
        sSrc += "<td>" + key.platform + "</td>"; //접속 플랫폼
        sSrc += "<td>" + key.time + "</td>"; //접속 시간
        sSrc += "</tr>";
    });

    $("table.table-striped.table-sm tbody").html(sSrc);




}

function loadAll() {

    var sEncodedHangul = window.location.href.split("/dashboard/")[1]
    var sHangul = decodeURI(sEncodedHangul);
    console.log(sHangul);

    var sUrl = "/api/dashboard/all/" + sHangul;
    console.log("url : " + sUrl);

    var objMessage = {
        // originUrl: data,
        //  'token': token
    };
    $.ajax({
        url: sUrl,
        // data: objMessage,
        type: "GET",
        timeout: 3000,
        success: function(data) {
            var log = data.log;
            var dayVisitor = data.statistics.dayVisitor;

            drawChart(dayVisitor);
            writeRecentLog(log);
        },
        error: function(xhr, status, error) {
            var jsonResponse = JSON.parse(xhr.responseText);
            var err = jsonResponse["message"];
            console.log(err);
            var msg = "";

            if (err == "invaild input") {
            msg = "표시할 데이터가 없습니다";
            }

            //todo: ErroCode 핸들링

            $(".d-flex.justify-content-between.flex-wrap.flex-md-nowrap.align-items-center").remove(); //차트 삭제

            $(".col-md-9.ms-sm-auto.col-lg-10.px-md-4 h2").html("<br/>표시할 데이터가 없습니다.<br/>잘못된 링크를 입력하신 것 같습니다");
            $(".table.table-striped.table-sm").html(""); //테이블 삭제
            $("#myChart").remove();

        }
    });
}