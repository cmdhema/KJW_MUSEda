package com.museda.util;

public class ErrorCode {

	public static String getErrorMessage(int code) {
		switch(code) {
		case 999 :
			return "알수 없는 오류입니다. 다시 시도해주세요.";
		case 101 :
			return "중복된 아이디 입니다.";
		case 102 :
			return "존재하지 않는 ID 입니다.";
		case 103 :
			return "비밀번호가 일치하지 않습니다.";
		case 104 : 
			return "자기 자신을 Follow할 수 없습니다.";
		case 105 :
			return "자기 사진에는 하트를 추가할 수 없습니다.";
		case 106 :
		case 107 :
			return "항목 오류입니다. 다시 시도해주세요";
		case 108 :
			return "해당 사용자는 뮤즈가 아닙니다.";
		case 109 :
			return "중복 데이터가 존재합니다.";
		case 110 :
			return "해당 데이터가 존재하지 않습니다. 다시 시도해주세요";
		case 111 :
			return "등록되지 않은 이메일입니다. 다시 시도해주세요";
		case 112 : 
			return "이메일 전송 에러입니다. 다시 시도해주세요";
		case 113 :
			return "중복된 이메일입니다. 다시 입력해주세요";
		case 201 :
			return "입력되지 않은 값이 있습니다. 다시 시도해주세요";
		case 202 : 
			return "필드에 타입과 맞지 않는 데이터가 있습니다. 다시 시도해주세요";
		case 401 :
			return "폴더생성 오류입니다.";
		case 402 :
			return "파일생성 오류입니다.";
		case 501 :
			return "쿠키 오류입니다.";
		case 701 :
			return "해당 사진이 존재하지 않습니다.";
		case 800 :
			return "DB 커넥션 풀 에러입니다.";
		case 900 :
			return "내부 DB 오류입니다.";
		case 901 :
			return "Insert 오류입니다.";
		case 902 :
			return "Delete 오류입니다.";
		case 903 :
			return "Update 오류입니다.";
		}
		
		return "다시 시도해주세요";
	}
}
