package com.penek.shortUrl.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.penek.shortUrl.Entity.LogEntity;
import com.penek.shortUrl.Exception.CustomException;
import com.penek.shortUrl.Exception.ErrorCode;
import com.penek.shortUrl.Repository.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {
    private final LogRepository logRepository;
    private final ShortService shortService;


    public JSONObject analyLog(String hangul) {

        BigInteger idx = shortService.shortUrlToIdx(hangul);

        log.info("로그를 가져옵니다 {}/{}", hangul, idx);

        List<LogEntity> result = logRepository.findByIdx(idx);

        return logToJson(result);

     /*
*/
        /*for(LogEntity log : result ){

        }*/

    }

    public JSONObject logToJson(List<LogEntity> logData){

        JSONObject jObject = new JSONObject();

        String[] osDataSets = new String[]{"Unknown OS", "Windows", "MacOS", "Linux", "Android", "iOS"};
        String[] browserDataSets = new String[]{"Unknown Browser", "Chrome", "Firefox", "Trident", "Safari", "Edge", "Opera", "Whale", "Samsung Internet"};

        ArrayList<String> listPlatformAtLog = new ArrayList<String>();
        ArrayList<String> listBrowserAtLog = new ArrayList<String>();
        ArrayList<String> listCountryAtLog = new ArrayList<String>();
        ArrayList<String> listEnterAtLog = new ArrayList<String>();

        JSONArray jArray = new JSONArray();


        for(LogEntity log : logData) {
            JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
            sObject.put("country", log.getCountry());
            listCountryAtLog.add(log.getCountry());

            sObject.put("platform", log.getPlatform());
            listPlatformAtLog.add(log.getPlatform());

            sObject.put("referer", log.getReferer());

            sObject.put("browser", log.getUserAgent());
            listBrowserAtLog.add(log.getUserAgent());

            sObject.put("time", log.getEnteredAt().toString().split(" ")[0]);
            listEnterAtLog.add(log.getEnteredAt().toString().split(" ")[0]);

            jArray.add(sObject);
        }
        jObject.put("log", jArray);

        //2022-03-20T09:07:00.000+00:00
/*        일일 접속자 add

        LogData 불러와서
        최근 7일을 yyyy:MM:dd로 불러와서
        case문으로 하나하나 매칭? */

/*        JSONObject browserObj = new JSONObject();
        JSONObject osObj = new JSONObject();
        JSONObject sObject = new JSONObject();
        sObject.put("visitor", i);
        long k=0;
        for (String browserString : browserDataSets){
            browserObj.put(browserString, browser.get((int) k));
            k++;
        } */


        JSONObject analyObj = new JSONObject();

        JSONArray jArray2 = new JSONArray(); //statistics
        JSONArray jArray3 = new JSONArray(); //statistics for draw chart
        JSONObject jObject2 = new JSONObject(); //browser
        JSONObject jObject3 = new JSONObject(); //os platform
        JSONObject jObject4 = new JSONObject(); //date


        for(String browserString : browserDataSets) {
            int browserFrequnecy = Collections.frequency(listBrowserAtLog, browserString);
            jObject2.put(browserString, browserFrequnecy);
        }

        analyObj.put("browser", jObject2);

        for(String osString : osDataSets) {
            int browserFrequnecy = Collections.frequency(listPlatformAtLog, osString);
            jObject3.put(osString, browserFrequnecy);
        }

        analyObj.put("platform", jObject3);

        analyObj.put("totalVisitor", jArray.size());



        for (int i =0; i<7; i ++){
            JSONObject jObject5 = new JSONObject(); //date sub
            String dateString = getDateBeforeDay(i);
            int dateFrequnecy = Collections.frequency(listEnterAtLog, dateString);
            jObject5.put("date", dateString);
            jObject5.put("day", getDayOfDate(dateString));
            jObject5.put("visitor", dateFrequnecy);
            jArray2.add(jObject5);
            log.warn(dateString);
            //jObject4.put("dayVisitor", jObject5);
        }

        analyObj.put("dayVisitor", jArray2);



        jObject.put("statistics", analyObj);



        return jObject;
    }


    private String getDateBeforeDay(int day) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, (-1 * day));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(cal.getTime());
    }

    public String getDayOfDate(String date) {
        try {
            String dateType = "yyyy-MM-dd";
            SimpleDateFormat dateFormat = new SimpleDateFormat(dateType);

            String day = "";

            Date nDate = dateFormat.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(nDate);

            int dayNum = cal.get(Calendar.DAY_OF_WEEK);


            switch (dayNum) {
                case 1:
                    day = "일";
                    break;
                case 2:
                    day = "월";
                    break;
                case 3:
                    day = "화";
                    break;
                case 4:
                    day = "수";
                    break;
                case 5:
                    day = "목";
                    break;
                case 6:
                    day = "금";
                    break;
                case 7:
                    day = "토";
                    break;

            }
                return day + "요일";
        } catch (java.text.ParseException e){
            e.printStackTrace();
            throw new CustomException(ErrorCode.DATE_FORMATTING_ERROR);
        }
    }


   /* public JSONObject addDayVisitor(JSONObject jObject){
*//*
        int limitDay = 10;
        SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd");
        Date time = new Date();
        String nowDate = format1.format(time);

        Calendar cal = Calendar.getInstance ();


*//*

    }*/






}
