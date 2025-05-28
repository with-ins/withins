import {Pageable} from "@/domain/Pageable";
import {Recruit} from "@/domain/Recruit";
import {RecruitDetail} from "@/domain/RecruitDetail";
import {Career} from "@/domain/Career";
import {News} from "@/domain/News";
export class ApiServer {

  static server : String = 'http://localhost:8080/api/v1';

  static fetchPost(url : String, json : Object) {
    return fetch(`${ApiServer.server}${url}` , {
      method : 'post',
      credentials: 'include',
      headers : {'Content-Type' : 'application/json'},
      body : JSON.stringify(json)
    })
      .then(res => res.json())
  }

  static async fetchGet(url : String, params: object) {
    const requestUrl = `${ApiServer.server}${url}${this.toQueryParams(params)}`;

    try {
      const response = await fetch(requestUrl, {
        method: 'GET',
        headers: {
          'Accept': 'application/json'
        },
        credentials: 'include'
      });

      // 텍스트가 유효한 JSON인지 확인 후 파싱
      try {
        return response.json();
      } catch (parseError) {
        console.error('JSON 파싱 오류:', parseError);
        throw new Error('응답이 유효한 JSON 형식이 아닙니다');
      }
    } catch (error) {
      console.error('API 호출 중 오류 발생:', error);
      throw error;
    }
  }

  static mockRecruit(url : String) : Pageable<Recruit> {
    const json = {
      'page' : {
        'totalElements': 71,
        'totalPages': 8,
        'pageNumber': 0,
      },
      'condition' : {
        'word': '테스트',
        'region': 'all',
      },
      'content': [
        {
          'recruitId': 1,
          'title': '2025년 사회교육프로그램 강사모집 [웰빙댄스]',
          'organization': {
            'organizationId': 1,
            'name' : '서초구립양재노인종합복지관',
            'region': '인천 남동구'
          },
          'createAt': '2025-01-01T00:00:00',
          'deadline': '2025-06-01T12:00:00',
        },
        {
          'recruitId': 2,
          'title': '사회교육프로그램 강사모집 [웰빙댄스]',
          'organization': {
            'organizationId': 1,
            'name' : '서초구립양재노인종합복지관',
            'region': '인천 남동구'
          },
          'createAt': '2025-01-01T00:00:00',
          'deadline': '2025-06-01T12:00:00',
        },
        {
          'recruitId': 3,
          'title': '2025년 사회교육프로그램 강사모집 [웰빙댄스]123123123123123123123123123123123123',
          'organization': {
            'organizationId': 1,
            'name' : '서초구립양재노인종합복지관',
            'region': '인천 남동구'
          },
          'createAt': '2025-01-01T00:00:00',
          'deadline': '2025-06-01T12:00:00',
        },
        {
          'recruitId': 3,
          'title': '2025년 사회교육프로그램 강사모집 [웰빙댄스]123123123123123123123123123123123123',
          'organization': {
            'organizationId': 1,
            'name' : '서초구립양재노인종합복지관',
            'region': '인천 남동구'
          },
          'createAt': '2025-01-01T00:00:00',
          'deadline': '2025-06-01T12:00:00',
        },
        {
          'recruitId': 3,
          'title': '2025년 사회교육프로그램 강사모집 [웰빙댄스]123123123123123123123123123123123123',
          'organization': {
            'organizationId': 1,
            'name' : '서초구립양재노인종합복지관',
            'region': '인천 남동구'
          },
          'createAt': '2025-01-01T00:00:00',
          'deadline': '2025-06-01T12:00:00',
        },
        {
          'recruitId': 3,
          'title': '2025년 사회교육프로그램 강사모집 [웰빙댄스]123123123123123123123123123123123123',
          'organization': {
            'organizationId': 1,
            'name' : '서초구립양재노인종합복지관',
            'region': '인천 남동구'
          },
          'createAt': '2025-01-01T00:00:00',
          'deadline': '2025-06-01T12:00:00',
        },
        {
          'recruitId': 3,
          'title': '2025년 사회교육프로그램 강사모집 [웰빙댄스]123123123123123123123123123123123123',
          'organization': {
            'organizationId': 1,
            'name' : '서초구립양재노인종합복지관',
            'region': '인천 남동구'
          },
          'createAt': '2025-01-01T00:00:00',
          'deadline': '2025-06-01T12:00:00',
        },
        {
          'recruitId': 3,
          'title': '2025년 사회교육프로그램 강사모집 [웰빙댄스]123123123123123123123123123123123123',
          'organization': {
            'organizationId': 1,
            'name' : '서초구립양재노인종합복지관',
            'region': '인천 남동구'
          },
          'createAt': '2025-01-01T00:00:00',
          'deadline': '2025-06-01T12:00:00',
        },
        {
          'recruitId': 3,
          'title': '2025년 사회교육프로그램 강사모집 [웰빙댄스]123123123123123123123123123123123123',
          'organization': {
            'organizationId': 1,
            'name' : '서초구립양재노인종합복지관',
            'region': '인천 남동구'
          },
          'createAt': '2025-01-01T00:00:00',
          'deadline': '2025-06-01T12:00:00',
        },
        {
          'recruitId': 3,
          'title': '2025년 사회교육프로그램 강사모집 [웰빙댄스]123123123123123123123123123123123123',
          'organization': {
            'organizationId': 1,
            'name' : '서초구립양재노인종합복지관',
            'region': '인천 남동구'
          },
          'createAt': '2025-01-01T00:00:00',
          'deadline': '2025-06-01T12:00:00',
        },
      ]
    };

    const pageable = new Pageable<Recruit>(json);
    let recruits : Array<Recruit> = json.content.map(value => new Recruit(value));
    pageable.setContent(recruits)
    return pageable;
  }
  static mockNews(url: String, params: object) : Pageable<News> {
    const json = {
      'page' : {
        'totalElements': 71,
        'totalPages': 8,
        'pageNumber': 0,
      },
      'condition' : {
        'word': '테스트',
        'region': 'all',
        'type': 'ALL',
      },
      'content': [
        {
          'newsId': 1,
          'title': '2025년 사회교육프로그램 강사모집 [웰빙댄스]',
          'type' : 'NOTICE',
          'link' : 'https://naver.com',
          'organization': {
            'organizationId': 1,
            'name' : '서초구립양재노인종합복지관',
            'region': '인천 남동구'
          },
          'createAt': '2025-01-01T00:00:00',
        },
        {
          'newsId': 1,
          'title': '2025년 사회교육프로그램 강사모집 [웰빙댄스]',
          'type' : 'NOTICE',
          'link' : 'https://naver.com',
          'organization': {
            'organizationId': 1,
            'name' : '서초구립양재노인종합복지관',
            'region': '인천 남동구'
          },
          'createAt': '2025-01-01T00:00:00',
        },
        {
          'newsId': 1,
          'title': '2025년 사회교육프로그램 강사모집 [웰빙댄스]',
          'type' : 'NOTICE',
          'link' : 'https://naver.com',
          'organization': {
            'organizationId': 1,
            'name' : '서초구립양재노인종합복지관',
            'region': '인천 남동구'
          },
          'createAt': '2025-01-01T00:00:00',
        }
      ]
    };

    const pageable = new Pageable<News>(json);
    let news : Array<News> = json.content.map(value => new News(value));
    pageable.setContent(news)
    return pageable;
  }
  static mockRecruitDetail(url : String) : RecruitDetail {
    const json = {
      'recruitId': 1,
      'title': '2025년 사회교육프로그램 강사모집 [웰빙댄스]',
      'content': '1. 채용분야 및 지원자격\n   가. 채용분야 : 운전원 및 시설관리인\n   나. 지원자격 및 기타사항\n   * 운전원 및 시설관리인\n    - 대형운전면허 및 대형버스 운전 가능자(필수)\n    - 소방안전관리자(방화관리자) 자격증 소지자 우대\n    - 전기 및 시설물 관리 관련 자격증 소지자 우대\n2. 채용방법\n   가. 제1차 : 서류전형\n   나. 제2차 : 면접전형(서류전형 합격자에 한함)\n3. 제출서류\n   가. 입사지원서 및 자기소개서 각1부\n   나. 최종학력 증명서 및 성적증명서 각1부\n   다. 직무 관련 경력(재직)증명서(건강보험 자격득실확인서 포함) 1부\n   라. 직무 관련 자격증 사본 각1부\n   마. 주민등록 등본 1부\n   바. 주민등록 초본 또는 병적증명서(해당자에 한함)\n   사. 개인정보 수집·활용 동의서 1부\n   아. 취업보호대상자증명서(해당자의 한함)\n   자. 채용기준 신체검사서(공무원용) 1부\n※ 입사지원서, 자기소개서, 개인정보 수집·활용 동의서는 필히 영양군노인복지관 양식을 사용\n※ 채용기준 신체검사서는 최종 합격 후 제출\n- 국가유공자 등 예우 및 지원에 관한 법률 제29조에 의한 취업지원대상자는 입사지원에 해당 사항을 체크해 주시기 바랍니다.\n4. 접수방법\n   가. 접수기간 : 2025년 4월 21일부터 2025년 4월 28일 / 오후 17:00까지\n   나. 제출방법 : E-mail 접수 (yygswc-re@naver.com)\n※ 모든 서류는 한 개의 파일로 첨부 (제목 : 입사지원서 제출 - 성명)\n※ 방문접수 및 우편접수는 불가(이메일 접수만 가능)\n5. 기타사항\n   가. 적격자가 없을 경우에는 선발하지 않습니다.\n   나. 제출된 입사지원서에 기재된 내용이 사실과 다를 경우에 발생하는 불이익은 모두 본인 책임입니다.\n   다. 합격자 통보 후 신원조회 및 채용신체검사 등을 통하여 부적합한 결격사유가 있을 경우 합격이 취소 될 수도 있습니다.\n   라. 입사지원서에는 반드시 연락처를 기재하시기 바랍니다.\n   마. 기타문의사항 : 복지행정팀 054)683-8870',
      'type' : '복지 일반',
      'attachedFiles': [
        {
          'fileName': '무슨무슨첨부파일.pdf',
          'filePath': 'http://localhost:8080/download'
        },
        {
          'fileName': '무슨무슨첨부파일.pdf',
          'filePath': 'http://localhost:8080/download'
        }
      ],
      'organization': {
        'organizationId': 1,
        'name': '서초구립양재노인종합복지관',
        'region': '인천 남동구'
      },
      'createAt': '2025-01-01T00:00:00',
      'deadline': '2025-06-01T12:00:00'
    };

    return new RecruitDetail(json);
  }
  static mockCareer(url : string) : Pageable<Career> {
    const json = {
      'page' : {
        'totalElements': 71,
        'totalPages': 8,
        'pageNumber': 0,
      },
      'condition' : {
        'sort': 'POPULAR',
        'tags': [
            '노래교육', '안전'
        ],
      },
      'content': [
        {
          careerId : 1,
          imageUrl : null,
          summary : '한줄소개 테스트임',
          experience : '· 12년 경력 전문 MC & 가수\n· 맞춤형 진행 + 센스 폭발 + 유머 장착\n· 레크레이션 지도자 & 가요전문지도사 보유',
          tags : [
              '어쩌구', '저쩌구', '테스트', '테스트임', '다른거', '10년경력'
          ]
        },
        {
          careerId : 2,
          imageUrl : null,
          summary : '한줄소개 테스트임',
          experience : '· 12년 경력 전문 MC & 가수\n· 맞춤형 진행 + 센스 폭발 + 유머 장착\n· 레크레이션 지도자 & 가요전문지도사 보유',
          tags : [
            '어쩌구', '저쩌구', '테스트', '테스트임', '다른거', '10년경력'
          ]
        },
        {
          careerId : 3,
          imageUrl : null,
          summary : '한줄소개 테스트임',
          experience : '· 12년 경력 전문 MC & 가수\n· 맞춤형 진행 + 센스 폭발 + 유머 장착\n· 레크레이션 지도자 & 가요전문지도사 보유',
          tags : [
            '어쩌구', '저쩌구', '테스트', '테스트임', '다른거', '10년경력'
          ]
        },
        {
          careerId : 4,
          imageUrl : null,
          summary : '한줄소개 테스트임',
          experience : '· 12년 경력 전문 MC & 가수\n· 맞춤형 진행 + 센스 폭발 + 유머 장착\n· 레크레이션 지도자 & 가요전문지도사 보유',
          tags : [
            '어쩌구', '저쩌구', '테스트', '테스트임', '다른거', '10년경력'
          ]
        },
        {
          careerId : 5,
          imageUrl : null,
          summary : '한줄소개 테스트임',
          experience : '· 12년 경력 전문 MC & 가수\n· 맞춤형 진행 + 센스 폭발 + 유머 장착\n· 레크레이션 지도자 & 가요전문지도사 보유',
          tags : [
            '어쩌구', '저쩌구', '테스트', '테스트임', '다른거', '10년경력'
          ]
        },
        {
          careerId : 6,
          imageUrl : null,
          summary : '한줄소개 테스트임',
          experience : '· 12년 경력 전문 MC & 가수\n· 맞춤형 진행 + 센스 폭발 + 유머 장착\n· 레크레이션 지도자 & 가요전문지도사 보유',
          tags : [
            '어쩌구', '저쩌구', '테스트', '테스트임', '다른거', '10년경력'
          ]
        },
        {
          careerId : 7,
          imageUrl : null,
          summary : '한줄소개 테스트임',
          experience : '· 12년 경력 전문 MC & 가수\n· 맞춤형 진행 + 센스 폭발 + 유머 장착\n· 레크레이션 지도자 & 가요전문지도사 보유',
          tags : [
            '어쩌구', '저쩌구', '테스트', '테스트임', '다른거', '10년경력'
          ]
        },
        {
          careerId : 8,
          imageUrl : null,
          summary : '한줄소개 테스트임',
          experience : '· 12년 경력 전문 MC & 가수\n· 맞춤형 진행 + 센스 폭발 + 유머 장착\n· 레크레이션 지도자 & 가요전문지도사 보유',
          tags : [
            '어쩌구', '저쩌구', '테스트', '테스트임', '다른거', '10년경력'
          ]
        },
        {
          careerId : 9,
          imageUrl : null,
          summary : '한줄소개 테스트임',
          experience : '· 12년 경력 전문 MC & 가수\n· 맞춤형 진행 + 센스 폭발 + 유머 장착\n· 레크레이션 지도자 & 가요전문지도사 보유',
          tags : [
            '어쩌구', '저쩌구', '테스트', '테스트임', '다른거', '10년경력'
          ]
        },
        {
          careerId : 10,
          imageUrl : null,
          summary : '한줄소개 테스트임',
          experience : '· 12년 경력 전문 MC & 가수\n· 맞춤형 진행 + 센스 폭발 + 유머 장착\n· 레크레이션 지도자 & 가요전문지도사 보유',
          tags : [
            '어쩌구', '저쩌구', '테스트', '테스트임', '다른거', '10년경력'
          ]
        },
      ]
    };

    const pageable = new Pageable<Career>(json);
    let careers : Array<Career> = json.content.map(value => new Career(value));
    pageable.setContent(careers)
    return pageable;
  }

  static toQueryParams(params : object) {
    if (params == null) return '';
    const queryParts = Object.entries(params)
      // 빈 문자열, null, undefined 값을 가진 키는 필터링
      .filter(([_, value]) => value !== null && value !== undefined && value !== '')
      .map(([key, value]) => {
        return `${encodeURIComponent(key)}=${encodeURIComponent(value)}`;
      });

    return queryParts.length > 0 ? `?${queryParts.join('&')}` : '';
  }

}