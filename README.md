# HTML 스크립트 크롤러

#### 설명

jsoup 기반으로 HTML script를 긁어온다. 긁어온 script Text는 특정 조건에 맞게 텍스트 가공 처리된다.   
대문자 소문자 숫자 제외 파싱 -> 중복 처리 -> 대소대소..숫자 정렬 -> 대-소-숫 기준으로 노출된다.   

**실행**

```bash
# 실행
java -jar parser-0.0.1-SNAPSHOT.jar

# 빌드
mvn clean package
```

#### 사용

- jdk 11
- spring boot 2.7.2
- ehcache 3.8
- jsoup
- lombok

#### entpoint 정보

| URL          |METHOD| PARAMETER                                                                               |
|--------------|------|-----------------------------------------------------------------------------------------|
| /api/crawler |GET| {targetURLs/Array<String>/Optional/크롤링대상목록},<br />{isCaching/Boolean/Optional/캐싱여부(true)} |

*예시 호출*

```basn
# 기본 크롤링 대상 주소로 캐싱되어 동작합니다.
curl -XGET http://localhost:8080/api/crawler       

# 기본 크롤링 대상 주소로 캐싱되지 않고 동작합니다.
curl -XGET http://localhost:8080/api/crawler?isCaching=false 

# 임의의 대상 경로 페이지 스크립트를 가져옵니다. (Protocol도 같이 입력해야합니다.)
curl -XGET http://localhost:8080/api/crawler?targetURLs=https://api.myip.com/
```

*예시 응답*

```bash
# 성공
HttpStatusCode 200
{
  "Status": 200,
  "Merge": "Aa1B2C4DdefghIilmtv"
}

# 일부 경로 성공
HttpStatusCode 206
{
  "Status": 206,
  "Merge": "Aa1B2Iilmtv"
}

# 실패
HttpStatusCode 500
{
  "Status": 500,
  "Merge": ""
}
```
