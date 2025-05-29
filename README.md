## withins_server
### 설정파일 관리
Github Repository에 노출되지 말아야 하는 설정파일들은 private 서브모듈로 관리한다.

`withins_server/config` 디렉토리에서 설정파일을 수정하고 저장한다.
- `withins_server/config` 디렉토리가 아닌 각 모듈의 `resources`는 `.gitignore`에 의해 git에서 관리한다.
    - ./gitignore안의 설정 예) `**/src/main/resources/*.yml`
- withins_server의 최상위 디렉토리에 존재하는 build.gradle.kts의 Task 정의에 따라, `withins_server/config`의 설정은 Task에서 정의한 디렉토리에 복사된다.
- 새 모듈을 생성할 경우, 최상위 디렉토리의 `build.gradle.kts`에 모듈의 resources에 저장할 Task를 정의해야 한다.