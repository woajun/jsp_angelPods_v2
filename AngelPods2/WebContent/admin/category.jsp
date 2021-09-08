<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<!-- booststrap css -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We" crossorigin="anonymous">

<style>
</style>
</head>
<body>

<div class="container my-3">
    <div class="row">
        <div class="col-6 col-md-4 col-lg-3 pe-0">
                <select id="cSlt" class="form-select" size="8">
                <option selected>Open this select menu</option>
                <option value="1">One</option>
                <option value="2">Two</option>
                <option value="3">Three</option>
                </select>
                <div class="text-end">
                <button>+</button>
                <button>△</button>
                <button>▽</button>
                </div>
            </div>
            <div class="col-6 col-md-4 col-lg-3 ps-0 ">
                <select id="cdSlt" class="form-select" size="8">
                    <option selected>Open this select menu</option>
                    <option value="1">One</option>
                    <option value="2">Two</option>
                    <option value="3">Three</option>
                </select>
                <div class="text-end">
                    <button>+</button>
                    <button>△</button>
                    <button>▽</button>
                </div>
        </div>
        <div class="col-12 col-md-4 col-lg-6 ">
            <div class="px-3 border rounded py-3 my-3">
                <dl>
                    <dt>대분류명</dt>
                    <dd>
                        <input id="ipt-cName" type="text">
                    </dd>

                    <dt>소분류명</dt>
                    <dd>
                        <input id="ipt-cdName" type="text">
                    </dd>
                    
                    <dt>일련번호</dt>
                    <dd >
                        <input type="radio" name="needSn" id="pub_c1"><label for="pub_c1">사용</label>
                        <input type="radio" name="needSn" id="pub_c2" checked><label for="pub_c2" >미사용</label>
                    </dd>

                    <dt>기본 이미지</dt>
                    <dd>
                        <input type="file">
                    </dd>
                </dl>

                <button>수정</button>
                <button>삭제</button>
            </div>
            <div class="text-end">
                <button>적용</button>
                <button>취소</button>
                
            </div>
        </div>
    </div>
</div>

<script>
//ajax로 cd값 가져오기
$('#cSlt'),on('click',function(){
	$.ajax({
		url:'cd_list.do',
		type:'post',
		
	
	});
});
</script>
</body>
</html>