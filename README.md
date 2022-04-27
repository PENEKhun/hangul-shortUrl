[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2FPENEKhun%2FshortUrl&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)

# ShortUrl
**한글**이 가지는 **특징** 을 이용해  
**전세계 모든 링크**를 **짧게** 만들 수 있습니다.

```
한글은 초성 중성 종성으로 이루어져있습니다.  
  
초성은 19개 중성은 21개 종성은 27개  
영어는 알파벳 소문자 대문자 모두 합쳐 56개 밖에 안되지만,  
한글은 초성 중성 종성을 조합하여 약 10,000개의 글자를 만들 수 있습니다.  
  
결국, 5글자의 한글을 통해 어림잡아 10000⁵의 데이터를 표현할 수 있습니다.  
10000⁵는 1해(垓)이며 이는 경(京)의 만 배인 수입니다.  
  
짧링은 이러한 한글의 특징을 이용하여  
한글 5글자로 세계의 모든 url를 표현 할 수 있습니다.
```

## 기술 스택
서버
`Java`
`SpringBoot`
`JPA`

클라이언트
`Thymeleaf`
`Html`
`JavaScript`

데이터베이스
`Mysql`


## 필요한 환경
실행할때 : `java`  
데이터베이스 : `mysql`

`application.yml을 통해 DB 접속정보를 작성해주셔야 합니다.`
`RecaptchaService.java에 있는 리캡챠 key값을 수정해주셔야 합니다.`



## 사용법
Without Running TestCode (Todo: TestCode Write)  
`./gradlew bootRun`


## 중요 알고리즘
일단 한글은 초성 중성 종성으로 이루어져있습니다

초성 중성 종성을 한글자로 만드려면
다음과 같이 할 수 있는데요

```java
int chosung; //초성 (0~18)
int jungsung; //중성 (0~20)
int jongsung; //종성 (0~27)
char unicode = (char) (44032 + (chosung*21*28) + (jungsung * 28) + jongsung);
```

여기서 초성으로 올 수 있는 수는 0~18
중성은 0~20, 종성은 0~27입니다.

우리가 초성 + 중성 + 종성으로 표현 할 수 있는 경우의 수는 19 * 21 * 28 = 11172입니다.

예를들어서  
만약에 숫자 0을 넣으면 '**가**'가 추출되고
숫자 18를 넣으먄 '**하**'가 추출되고....  
중요한건 **0**부터 **11172**까지의 입력에서
**중복**으로 **추출되는 한글**이 **없어야** 합니다.


저는 이러한 문제를 **진수**로 해결 했습니다.

숫자 1을 넣으면

| 초성(0~18) | 중성(0~20) | 종성(0~27) |
|------|------|------|
|1      |      |      |
```java
int chosung = 1; // 초성
int jungsung = 0; // 중성
int jongsung = 0; //종성
char unicode = (char) (44032 + (chosung*21*28) + (jungsung * 28) + jongsung);
```
>result = 까

숫자 18을 넣으면

| 초성(0~18) | 중성(0~20) | 종성(0~27) |
|------|------|------|
|18      |      |      |
```java
int chosung = 18; // 초성
int jungsung = 0; // 중성
int jongsung = 0; //종성
char unicode = (char) (44032 + (chosung*21*28) + (jungsung * 28) + jongsung);
```
>result = 하

만약의 19가 입력되면 어떡할까요?

| 초성(0~18) | 중성(0~20) | 종성(0~27) |
|------|------|------|
|19      |      |      |

초성은 0~18까지의 수만 가질 수 있기 때문에
다음 처럼 처리 해줍니다.

| 초성(0~18) | 중성(0~20) | 종성(0~27) |
|------|------|------|
|0      |1      |      |

```java
int chosung = 0; // 초성
int jungsung = 1; // 중성
int jongsung = 0; //종성
char unicode = (char) (44032 + (chosung*21*28) + (jungsung * 28) + jongsung);
```
>result = 개

  
# 라이센스 정보
* Bootstrap v5.1.3 (https://getbootstrap.com/)
* Copyright 2011-2021 The Bootstrap Authors (https://github.com/twbs/bootstrap/graphs/contributors)
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* This Repository includes GeoLite2 data created by MaxMind, available from <a href="https://www.maxmind.com">https://www.maxmind.com</a>.
