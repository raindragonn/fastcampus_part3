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

    - 사용되는 아이템 레이아웃의 루트 height는 match를 사용해야 한다.

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

 - `RecyclerView`
 
    - view를 재사용해서 리소스 사용에 이점이 있는 리스트 형식의 뷰

    - layoutmanager 를 통해 view가 나오는 형식을 지정할 수 있으며,

    - adapter를 통해 view와 데이터를 결합해서 사용할 수 있다.

 - `viewBinding`

    - layout(xml파일)을 바인딩 해주는 클래스를 제공한다.

    - findViewById, kotlin-android-extension보다 viewBinding을 권장합니다.
    
      - ex)activity_main.xml -> ActivityMainBinding

 - `Retrofit`

    - HttpClient 라이브러리
    
    - 쉽게 rest api를 이용할 수 있다.
    
 - `Glide`

    - 이미지 로딩 라이브러리

 - `Room`

    - jetpack에서 제공하는 내부 db

    - sqlite를 편하게 쓰기위한 래핑된 클래스라고 생각 하면 된다.



## chapter05_tinder

 > Firebase Authentication 을 통해 이메일 로그인과 페이스북 로그인을 활용<br>
 FirebaseRealtime Database를 이용하여 기록 저장 및 불러오기


**활용 기술**

 - `Firebase Authentication`

   - sns Login, email 등 을 사용해 유저 인증에 사용된다.
  

 - `Firebase Realtime Database`

   - 실시간으로 연동하는 데이터베이스 데이터가 바뀔때 리스너를 통해 실시간으로 처리할 수 있다.


## chapter06_중고거래 앱

 > Firebase Authentication을 통해 로그인 회원가입 기능으로
 회원기반 중고거래 앱 이며 Firebase Storage를 통해 사진 업로드를 할 수 있습니다.

**활용 기술**

 - `RecyclerView`

 - `View Binding`

 - `Fragment`

   - 앱의 UI중 독립적인 수명주기를 가진 레이아웃을 말합니다.

   - Activity안에 여려화면을 보여주기 위해 사용됩니다.

   - UI의 모듈성이 커져 재사용성이 증가합니다.

 - `BottmNavigationView`

   - 하단 탭 구조, 네비게이션 역을 하는 각각의 버튼을 통해 프래그먼트들을 컨트롤 할 수 있다.

 - `Firebase Storage`

   - 저장소로써 파일을 업로드하고 접근이 가능하다.

 - `Firebase Realtime Database`

   - 데이터를 커스텀 모델 클래스형태로 받으려면 해당 클래스에는 빈 생성자가 필요합니다.

 - `Firebase Authentication`


## chapter07_airbnb

 > Naver Map Api를 이용해 지도를띄우고 Mock Api 에서 예약가능 숙소목록을 받아와 활용할 수 있습니다.

 [api 가이드](https://navermaps.github.io/android-map-sdk/guide-ko/)

**활용 기술**

 - `ViewPager2`

 - `CoordinaterLayout`

    - FrameLayout의 확장 개념 (FrameLayout의 경우 본래 여러 뷰를 겹칠수 있지만 하나만 사용하기를 권장합니다.)
    
    - 자식 뷰들간의 인터렉션을 위한 컨테이너로 사용.

 - `BottomSheetBehavior`
  
    - 슬라이딩 드로어나 스와이프 해제액션 등 뷰의 다양한 움직임이나 애니메이션에 따른 상호작용을 구현하기 위해 사용된다.

    - 기본적으로 제공되는 Behavior

      -  BottomSheetBehavior : Bottom Sheet처럼 동작하도록 지원하는 Behavior
      
      - FloatActionButton.Behavior
      
      - SwipeDismissBehavior : swipe-to-dismiss제스쳐를 지원하는 Behavior
        - BaseTransientBottomBar.Behavior
      - ViewOffsetBehavior : 뷰들의 offset을 지정
        - HeaderScrollingViewBehavior : 수직으로 된 레이아웃에서 다른 뷰 아래에 있으면서 스크롤되는 뷰를 위한 Behavior
          - AppBarLayout.ScrollingViewBehavior
      - HeaderBehavior : 수직으로 스크롤되는 뷰 위에 놓이는 뷰를 위한 Behavior
        - AppBarLayout.Behavior

 - `retrofit`

 - `Glide`

[CoordinatorLayout, Behavior참고](http://dktfrmaster.blogspot.com/2018/03/coordinatorlayout.html)