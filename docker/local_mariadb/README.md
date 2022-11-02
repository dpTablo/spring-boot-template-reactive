테스트를 위한 docker 로컬 MariaDB 설정 내용입니다.

# 디렉토리 구성
```
- db
    - conf.d
        - my.cnf
    - data
        - ...
    - initdb.d
        - create_table.sql
        - load_data.sql
- docker-compose.yml
- .env
```

## db/conf.d/my.cnf
mariadb docker 인스턴스에 적용될 my.cnf 파일 입니다.
기본값으로 character-set 이 설정되어 있습니다.

## db/initdb.d/create_tables.sql
컨테이너 생성 시에 실행될 ddl을 정의합니다.

## .dnv
컨테이너 생성 시에 사용될 mariadb 의 환경변수 입니다.
root계정, user계정에 대한 정보와 데이터베이스명을 정의합니다.

