# redis-lab

redis를 익히기 위한 실습용 repository로, 테스트에 사용한 커맨드는 [여기](https://redis.io/commands/)에서 확인할 수 있다.

<br>

### 테스트 환경

- Java 11
- Spring Boot 2.7.7
- Spring Data Redis 2.7.7
- Redis 3.2.100
- Windows 11

<br>

### 테스트 종류

- [Strings Test](https://github.com/kkangmj/redis-lab/blob/master/src/test/java/com/mangoo/redis/OperationTest/ValueOperationTest.java)
- [Geospatial Indexes Test](https://github.com/kkangmj/redis-lab/blob/master/src/test/java/com/mangoo/redis/OperationTest/GeoOperationTest.java)

> Geospatial API는 레디스 버전 3.2 이상부터 지원하기 때문에 버전을 맞춰야 테스트가 정상적으로 돌아간다.

<br>

### 테스트 실행 방법

로컬에 레디스 서버를 띄운 뒤 테스트를 실행해야 한다. Windows의 경우, 레디스가 설치된 폴더에서 redis-server.exe를 찾아 실행시키면 된다.

<br>

### 기타

처음에는 [embedded-redis](https://github.com/ozimov/embedded-redis)를 사용했으나 몇 가지 이슈사항이 있었다.

- 로깅 시스템이 중복 구현되어 exclude 해줘야 한다.

    - 해당 문제는 [embedded-redis 레포의 Issue](https://github.com/ozimov/embedded-redis/pull/14)로도 등록되어 있지만 업데이트가 이루어지지 않았다. 마지막 업데이트는 2020년도이다. 

- redis 버전이 2.8.19로 고정되어 있어 Geospatial API가 지원되지 않는다.
    
    - embedded-redis 사용해 Geospatial Indexes Test 돌리면 아래와 같이 command를 찾을 수 없다는 에러 메시지가 출력된다.
    
      ![img](https://user-images.githubusercontent.com/52561963/207505804-ab3c06a3-9bc3-4753-b565-1365bdaf3ae3.png)

따라서, 로컬에 레디스를 설치해 사용하는 것으로 변경했다.
