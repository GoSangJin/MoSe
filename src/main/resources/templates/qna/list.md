<!DOCTYPE html>
<html lang="ko"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      xmlns:sec="http://thymeleaf.org/extras/spring-security"
      layout:decorate="~{layouts/main}">

<head>
  <meta charset="utf-8">
  <meta content="width=device-width, initial-scale=1.0" name="viewport">
  <title>Services - Active Bootstrap Template</title>
  <meta content="" name="description">
  <meta content="" name="keywords">

  <!-- Favicons -->
  <link href="/assets/img/favicon.png" rel="icon">
  <link href="/assets/img/apple-touch-icon.png" rel="apple-touch-icon">

  <!-- Fonts -->
  <link href="https://fonts.googleapis.com" rel="preconnect">
  <link href="https://fonts.gstatic.com" rel="preconnect" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&family=Lato:ital,wght@0,100;0,300;0,400;0,700;0,900;1,100;1,300;1,400;1,700;1,900&display=swap" rel="stylesheet">

  <!-- Vendor CSS Files -->
  <link href="/assets/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <link href="/assets/vendor/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
  <link href="/assets/vendor/aos/aos.css" rel="stylesheet">
  <link href="/assets/vendor/swiper/swiper-bundle.min.css" rel="stylesheet">
  <link href="/assets/vendor/glightbox/css/glightbox.min.css" rel="stylesheet">

  <!-- Main CSS File -->
  <link href="/assets/css/main.css" rel="stylesheet">

  <!-- =======================================================
  * Template Name: Active
  * Template URL: https://bootstrapmade.com/active-bootstrap-website-template/
  * Updated: Jun 29 2024 with Bootstrap v5.3.3
  * Author: BootstrapMade.com
  * License: https://bootstrapmade.com/license/
  ======================================================== -->
</head>

<body class="services-page">

<div layout:fragment="content">
  <main class="main">

    <!-- Page Title -->
    <div class="page-title light-background">
      <div class="container">
        <h1>Services</h1>
        <nav class="breadcrumbs">
          <ol>
            <li><a th:href="@{/}">메인</a></li>
            <li class="current">상품</li>
          </ol>
        </nav>
      </div>
    </div><!-- End Page Title -->

    <!-- Services 2 Section -->
    <section id="services-2" class="services-2 section">

      <div class="container">
            <h1>체험활동</h1>
            <!-- 상단 버튼 영역 -->
            <div class="mb-3 text-end">
              <span sec:authorize="hasAnyRole('admin', 'master')"><a href="/product/new" class="btn btn-primary">상품 등록</a></span>
            </div>

            <!-- 상품 카드 목록 -->
        <div class="container" data-aos="fade-up">
          <div class="row" th:each="product, iterStat : ${list.content}">
            <div class="col-md-3 mb-4">
              <div class="card">
                <img th:src="|https://${bucket}.s3.${region}.amazonaws.com/${folder}/${product.image_url}|"
                     class="card-img-top"
                     alt="상품 이미지"
                     th:if="${product.image_url != null}" />
                <img th:src="@{/assets/img/no_image.jpg}"
                     class="card-img-top"
                     alt="상품 이미지"
                     th:if="${product.image_url == null}" />
                <div class="card-body">
                  <h5 class="card-title" th:text="${product.product}"></h5>
                  <p class="card-text" th:text="${product.description}"></p>
                  <p class="card-text"><strong>가격:</strong> <span th:text="${product.price}"></span> 원</p>
                  <p class="card-text"><strong>평점:</strong> <span th:text="${product.rating}"></span> / 5</p>
                  <p class="card-text"><strong>상태:</strong>
                    <span th:if="${product.status.name()=='AVAILABLE'}">판매중</span>
                    <span th:if="${product.status.name()=='OUTOFSTOCK'}">품절</span>
                    <span th:if="${product.status.name()=='DISCONTINUED'}">단종</span>
                  </p>
                  <p class="card-text"><strong>등록일:</strong>
                    <!--<span th:text="${#DateTimeUtil.}"></span>-->
                  </p>
                  <a th:href="@{/product/view(id=${product.id})}" class="btn btn-info">상세 보기</a>
                  <a th:href="@{/product/edit(id=${product.id})}" class="btn btn-warning" sec:authorize="hasAnyRole('admin', 'master')">수정</a>
                  <a th:href="@{/product/delete(id=${product.id})}" class="btn btn-danger" sec:authorize="hasAnyRole('admin', 'master')" th:onclick="return confirm('정말 삭제하시겠습니까?')">삭제</a>
                </div>
              </div>
            </div>
            <div th:if="${iterStat.index % 4 == 3}" class="w-100"></div> <!-- 4개의 카드마다 줄바꿈 -->
          </div>
        </div>


      </div>
    </section><!-- /Services 2 Section -->



  </main>
</div>
<div class="mb-3">
                    <label for="question">질문:</label>
                    <input type="text" id="question" name="question" class="form-control" required="" autocomplete="off">
                </div>

<div class="mb-3">
                    <label for="answer">답:</label>
                    <textarea id="answer" name="answer" class="form-control" required=""></textarea>
                </div>


<div class="mb-3">
                    <label for="question">질문:</label>
                    <input type="text" id="question" name="question" class="form-control" required="" autocomplete="off">
                </div>
<form action="/qna/new" method="post">
                <div class="mb-3">
                    <label for="question">질문:</label>
                    <input type="text" id="question" name="question" class="form-control" required="" autocomplete="off">
                </div>
                <div class="mb-3">
                    <label for="answer">답:</label>
                    <textarea id="answer" name="answer" class="form-control" required=""></textarea>
                </div>
                <button type="submit" class="btn btn-primary">Submit</button>
                <a href="/qna/list" class="btn btn-secondary">Back to List</a>
            </form>
위의 폼으로  밑의질문과 답을 넣어서 

<div class="accordion adata" id="cntrstr_faq_list_area">
                <div class="card show">
                    <div class="card-header2" id="faq_0">
                        <strong>참가신청은 어떻게 하나요?</strong>
                        <button class="btn btn-link btn-block text-left accordion-btn" type="button" data-toggle="collapse" aria-expanded="true" data-target="#collapse_0" aria-controls="collapse_0">버튼</button>
                    </div>
                    <div class="collapse show" data-parent="#cntrstr_faq_list_area" id="collapse_0" aria-labelledby="faq_0">
                        <div class="card-body">
                            <p>로그인후 참가신청을 해 주십시요. 참가신청자 중 전화 또는 영상면접을 통해 현장운영기관별로 별도 안내가 진행될수도 있습니다.</p>
                        </div>
                    </div>
                </div>
                <div class="card">
                    <div class="card-header2" id="faq_1">
                        <strong>정확하고 신속한 정보를 얻기위해 어떻게 해야합니까?</strong>
                        <button class="btn btn-link btn-block text-left accordion-btn collapsed" type="button" data-toggle="collapse" aria-expanded="false" data-target="#collapse_1" aria-controls="collapse_1">버튼</button>
                    </div>
                    <div class="collapse" data-parent="#cntrstr_faq_list_area" id="collapse_1" aria-labelledby="faq_1">
                        <div class="card-body">
                            <p>프로그램은 얻고자 하시는 정보의 목적을 명확히 할 수 있도록 도와드립니다. 농사에 필요한 기상, 환경, 토양 병충해 진단 등 관련된 정보를 제공합니다.</p>
                        </div>
                    </div>
                </div>
                <div class="card">
                    <div class="card-header2" id="faq_2">
                        <strong>프로그램에 참여가 가능한가요?</strong>
                        <button class="btn btn-link btn-block text-left accordion-btn collapsed" type="button" data-toggle="collapse" aria-expanded="false" data-target="#collapse_2" aria-controls="collapse_2">버튼</button>
                    </div>
                    <div class="collapse" data-parent="#cntrstr_faq_list_area" id="collapse_2" aria-labelledby="faq_2">
                        <div class="card-body">
                            <p>지역정착에 겪는 어려움을 해소하고 방법을 만들어가는데 사업의 목적이 있습니다.</p>
                        </div>
                    </div>
                </div>
                <div class="card">
                    <div class="card-header2" id="faq_3">
                        <strong>지속 가능한 대한민국 농촌이 될 수 있을까요?</strong>
                        <button class="btn btn-link btn-block text-left accordion-btn collapsed" type="button" data-toggle="collapse" aria-expanded="false" data-target="#collapse_3" aria-controls="collapse_3">버튼</button>
                    </div>
                    <div class="collapse" data-parent="#cntrstr_faq_list_area" id="collapse_3" aria-labelledby="faq_3">
                        <div class="card-body">
                            <p>공감대를 바탕으로 지속적인 관계형성에 대한 의지와 역량을 갖추고 있는 팀과 함께 생각하고 연구합니다.</p>
                        </div>
                    </div>
                </div>
            </div>
list,insert,update 순서데로 다시 짜주세요


<!DOCTYPE html>
<html lang="ko"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      layout:decorate="~{layouts/main}">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <title>자주 묻는 질문</title>
    <link href="/assets/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="/assets/css/main.css" rel="stylesheet">
</head>
<body>
<div layout:fragment="content">
    <main class="main">
        <div class="container">
            <h2>자주 묻는 질문</h2>

            <!-- Static FAQ Accordion Section -->
            <div class="accordion adata" id="cntrstr_faq_list_area">
                <div class="card show">
                    <div class="card-header2" id="faq_0">
                        <strong>참가신청은 어떻게 하나요?</strong>
                        <button class="btn btn-link btn-block text-left accordion-btn" type="button" data-toggle="collapse" aria-expanded="true" data-target="#collapse_0" aria-controls="collapse_0">버튼</button>
                    </div>
                    <div class="collapse show" data-parent="#cntrstr_faq_list_area" id="collapse_0" aria-labelledby="faq_0">
                        <div class="card-body">
                            <p>로그인후 참가신청을 해 주십시요. 참가신청자 중 전화 또는 영상면접을 통해 현장운영기관별로 별도 안내가 진행될수도 있습니다.</p>
                        </div>
                    </div>
                </div>
                <div class="card">
                    <div class="card-header2" id="faq_1">
                        <strong>정확하고 신속한 정보를 얻기위해 어떻게 해야합니까?</strong>
                        <button class="btn btn-link btn-block text-left accordion-btn collapsed" type="button" data-toggle="collapse" aria-expanded="false" data-target="#collapse_1" aria-controls="collapse_1">버튼</button>
                    </div>
                    <div class="collapse" data-parent="#cntrstr_faq_list_area" id="collapse_1" aria-labelledby="faq_1">
                        <div class="card-body">
                            <p>프로그램은 얻고자 하시는 정보의 목적을 명확히 할 수 있도록 도와드립니다. 농사에 필요한 기상, 환경, 토양 병충해 진단 등 관련된 정보를 제공합니다.</p>
                        </div>
                    </div>
                </div>
                <div class="card">
                    <div class="card-header2" id="faq_2">
                        <strong>프로그램에 참여가 가능한가요?</strong>
                        <button class="btn btn-link btn-block text-left accordion-btn collapsed" type="button" data-toggle="collapse" aria-expanded="false" data-target="#collapse_2" aria-controls="collapse_2">버튼</button>
                    </div>
                    <div class="collapse" data-parent="#cntrstr_faq_list_area" id="collapse_2" aria-labelledby="faq_2">
                        <div class="card-body">
                            <p>지역정착에 겪는 어려움을 해소하고 방법을 만들어가는데 사업의 목적이 있습니다.</p>
                        </div>
                    </div>
                </div>
                <div class="card">
                    <div class="card-header2" id="faq_3">
                        <strong>지속 가능한 대한민국 농촌이 될 수 있을까요?</strong>
                        <button class="btn btn-link btn-block text-left accordion-btn collapsed" type="button" data-toggle="collapse" aria-expanded="false" data-target="#collapse_3" aria-controls="collapse_3">버튼</button>
                    </div>
                    <div class="collapse" data-parent="#cntrstr_faq_list_area" id="collapse_3" aria-labelledby="faq_3">
                        <div class="card-body">
                            <p>공감대를 바탕으로 지속적인 관계형성에 대한 의지와 역량을 갖추고 있는 팀과 함께 생각하고 연구합니다.</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Dynamic Q&A Insert Form -->
            <h3 class="mt-5">Q&A 추가</h3>
            <form th:action="@{/qna/new}" th:object="${qnaDTO}" method="post">
                <div class="mb-3">
                    <label for="question">질문:</label>
                    <input type="text" id="question" name="question" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label for="answer">답:</label>
                    <textarea id="answer" name="answer" class="form-control" required></textarea>
                </div>
                <button type="submit" class="btn btn-primary">Submit</button>
                <a href="/qna/list" class="btn btn-secondary">Back to List</a>
            </form>
        </div>
    </main>
</div>
<script src="/assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
</body>
</html>


</body>

</html>


<!DOCTYPE html>
<html lang="ko"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      layout:decorate="~{layouts/main}">

<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <title>Q&A Page</title>
    <meta content="" name="description">
    <meta content="" name="keywords">

    <!-- Favicons -->
    <link href="/assets/img/favicon.png" rel="icon">
    <link href="/assets/img/apple-touch-icon.png" rel="apple-touch-icon">

    <!-- Fonts -->
    <link href="https://fonts.googleapis.com" rel="preconnect">
    <link href="https://fonts.gstatic.com" rel="preconnect" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">

    <!-- Vendor CSS Files -->
    <link href="/assets/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="/assets/vendor/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
    <link href="/assets/vendor/aos/aos.css" rel="stylesheet">
    <link href="/assets/vendor/swiper/swiper-bundle.min.css" rel="stylesheet">
    <link href="/assets/vendor/glightbox/css/glightbox.min.css" rel="stylesheet">

    <!-- Main CSS File -->
    <link href="/assets/css/main.css" rel="stylesheet">
    
    <style>
        .card {
            border: 1px solid #ccc;
            margin: 10px 0;
            border-radius: 5px;
        }
        .card-header2 {
            background-color: #f7f7f7;
            padding: 15px;
            cursor: pointer;
        }
        .q-tit {
            list-style: none;
            margin: 0;
            padding: 0;
        }
        .q-tit li {
            font-weight: bold;
        }
        .accordion-btn {
            background: none;
            border: none;
            font-size: 14px;
            color: #007bff;
        }
        .collapse {
            display: none;
        }
        .collapse.show {
            display: block;
        }
        .card-body {
            padding: 15px;
            background-color: #fff;
        }
        .popup-setting-box {
            display: none;
        }
    </style>

</head>

<body>
<div layout:fragment="content">
    <main class="main">

        <!-- Page Title -->
        <div class="page-title" style="background-color: darkgreen;">
            <div class="container text-center">
                <h1 style="color: white;">Q&A</h1>
                <nav class="breadcrumbs">
                    <div>
                        <span style="color: white;">자주묻는 질문</span>
                    </div>
                </nav>
            </div>
        </div>

        <script>
            function funcClick(cardId) {
                var card = document.getElementById(cardId);
                var collapseDiv = card.querySelector('.collapse');
                collapseDiv.classList.toggle('show');
            }

            function funcView(element) {
                alert('Viewing: ' + element.innerText);
            }
        </script>

        <div class="page-content" style="background-color: white;">
            <div class="container">
                <h2>Q&A 목록</h2>
                <p>자주묻는 질문입니다.</p>
                <a class="btn btn-primary" th:href="@{/qna/insert}">등록</a> <!-- 삽입 페이지로 이동 -->

                <!-- Q&A Table -->
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>질문</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <!-- Q&A 목록 반복 -->
                        <tr th:each="data : ${list}">
                            <td th:text="${data.id}">1</td>
                            <td th:text="${data.title}">질문 제목</td>
                            <td>
                                <a th:href="@{/qna/detail/{id}(id=${data.id})}" class="btn btn-info">View</a>
                                <a th:href="@{/qna/update/{id}(id=${data.id})}" class="btn btn-warning">Update</a>
                                <a th:href="@{/qna/delete/{id}(id=${data.id})}" class="btn btn-danger">Delete</a>
                            </td>
                        </tr>
                    </tbody>
                </table>

                <!-- FAQ Card -->
                <div class="card" th:each="data : ${list}" th:id="'cardId' + ${data.id}">
                    <div class="card-header2" th:id="'heading_' + ${data.id}">
                        <ul class="q-tit">
                            <li th:text="${data.title}">질문 제목</li>
                        </ul>
                        <strong th:onclick="'funcView(this)'" th:text="${data.title}">질문 내용</strong>
                        <button class="btn btn-link btn-block text-left accordion-btn" type="button" 
                                th:onclick="'funcClick(\'cardId' + ${data.id} + '\')'">
                            보기
                        </button>
                    </div>
                    <div th:id="'collapseId_' + ${data.id}" class="collapse" th:classappend="${#lists.isEmpty(data)} ? 'show' : ''">
                        <div class="card-body">
                            <p th:text="${data.content}">질문 상세 내용</p>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </main>
</div>
</body>
</html>
