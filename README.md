# fastcampus_part3

## chapter01_push message

> Firebase Cloud Messae 의 토큰을 확인할 수 있다.<br>
일반, 확장형, 커스텀 Notification을 볼 수 있다.


**활용 기술**

- `Firebase Cloud Messaging (FCM)`

- `Notification`

## chapter02_오늘의 명언

> FireBase의 Remote Config를 활용해 별도의 코드 수정이나 배포없이 앱의 내용을 수정할수 있다.


**활용 기술**

- `Firebase Remote Config`

    - 앱 업데이트를 하지 않아도 앱의 동작과 모양을 변경할 수 있다.

    - 최대 2000개의 매개변수와 최대 500개의 조건 까지 구성 가능하다.

- `ViewPager2`

    - ViewPager 에서 개선된 버전
  
    - 가로,세로 방향 모두 지원. RTL 지원
  
    - recyclerView 를 기반으로 되어있다.

## chapter03_Alarm

> 지정된 시간에 알람이 울리게 할 수 있다.

**활용 기술**

 - `AlarmManager`

    - Real time(실제 시간) 으로 실행 시키는 방법
  
    - Elapsed Time(기기가 부팅된지부터 얼마나 지났는지) 으로 실행 시키는 방법

 - `Broadcast receiver`

## chapter04_book review

 > open Api를 통해 도서 정보를 가져와 보여주며 검색 및 개인 리뷰를 저장할 수 있다.

**활용 기술**

 - RecyclerView
 
    - view를 재사용해서 리소스 사용에 이점이 있는 리스트 형식의 뷰

    - layoutmanager 를 통해 view가 나오는 형식을 지정할 수 있으며,

    - adapter를 통해 view와 데이터를 결합해서 사용할 수 있다.

 - viewBinding

    - layout(xml파일)을 바인딩 해주는 클래스를 제공한다.

    - findViewById, kotlin-android-extension보다 viewBinding을 권장합니다.
    
      - ex)activity_main.xml -> ActivityMainBinding

 - Retrofit

    - HttpClient 라이브러리
    
    - 쉽게 rest api를 이용할 수 있다.
    
 - Glide

    - 이미지 로딩 라이브러리

 - Room

    - jetpack에서 제공하는 내부 db

    - sqlite를 편하게 쓰기위한 래핑된 클래스라고 생각 하면 된다.

