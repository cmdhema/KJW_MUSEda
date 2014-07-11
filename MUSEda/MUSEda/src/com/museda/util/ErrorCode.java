package com.museda.util;

public class ErrorCode {

	public static String getErrorMessage(int code) {
		switch(code) {
		case 999 :
			return "�˼� ���� �����Դϴ�. �ٽ� �õ����ּ���.";
		case 101 :
			return "�ߺ��� ���̵� �Դϴ�.";
		case 102 :
			return "�������� �ʴ� ID �Դϴ�.";
		case 103 :
			return "��й�ȣ�� ��ġ���� �ʽ��ϴ�.";
		case 104 : 
			return "�ڱ� �ڽ��� Follow�� �� �����ϴ�.";
		case 105 :
			return "�ڱ� �������� ��Ʈ�� �߰��� �� �����ϴ�.";
		case 106 :
		case 107 :
			return "�׸� �����Դϴ�. �ٽ� �õ����ּ���";
		case 108 :
			return "�ش� ����ڴ� ��� �ƴմϴ�.";
		case 109 :
			return "�ߺ� �����Ͱ� �����մϴ�.";
		case 110 :
			return "�ش� �����Ͱ� �������� �ʽ��ϴ�. �ٽ� �õ����ּ���";
		case 111 :
			return "��ϵ��� ���� �̸����Դϴ�. �ٽ� �õ����ּ���";
		case 112 : 
			return "�̸��� ���� �����Դϴ�. �ٽ� �õ����ּ���";
		case 113 :
			return "�ߺ��� �̸����Դϴ�. �ٽ� �Է����ּ���";
		case 201 :
			return "�Էµ��� ���� ���� �ֽ��ϴ�. �ٽ� �õ����ּ���";
		case 202 : 
			return "�ʵ忡 Ÿ�԰� ���� �ʴ� �����Ͱ� �ֽ��ϴ�. �ٽ� �õ����ּ���";
		case 401 :
			return "�������� �����Դϴ�.";
		case 402 :
			return "���ϻ��� �����Դϴ�.";
		case 501 :
			return "��Ű �����Դϴ�.";
		case 701 :
			return "�ش� ������ �������� �ʽ��ϴ�.";
		case 800 :
			return "DB Ŀ�ؼ� Ǯ �����Դϴ�.";
		case 900 :
			return "���� DB �����Դϴ�.";
		case 901 :
			return "Insert �����Դϴ�.";
		case 902 :
			return "Delete �����Դϴ�.";
		case 903 :
			return "Update �����Դϴ�.";
		}
		
		return "�ٽ� �õ����ּ���";
	}
}
