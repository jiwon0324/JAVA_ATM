package controller;

import acoount.AccountDAO;
import client.ClientDAO;
import utils.Utils;

public class Controller {
	public AccountDAO accDAO;
	public ClientDAO clientDAO;
	private final String BANK_NAME = "더조은뱅크";
	private int log;
	private Utils utils;

	public Controller() {
		accDAO = AccountDAO.getInstance();
		clientDAO = ClientDAO.getInstance();
		utils = Utils.getInstance();
		log = -1;

	}
	

	private void BankName() {
		System.out.println("-< BANK NAME >-");
	}

	private void MainMenu() {
		// 초기화면
		System.out.printf("[1]관리자 %n[2]사용자 %n[0]종료%n");
	}

	private void AdminMenu() { 
		System.out.printf("[1] 회원목록 %n[2] 회원수정 %n[3]회원 삭제 %n[4]데이터 저장 %n[5]데이터 불러오기%n");
	}

	private int UserMenu() { // 사용자 메뉴
		if (log == -1) {
			System.out.printf("[1] 회원가입 %n[2] 로그인 %n[0] 뒤로가기%n");
			return 2;
		} else {
			System.out.printf("[1] 계좌추가 %n[2] 계좌삭제 %n[3] 입금 %n[4] 출금 %n[5]이체 %n[6]탈퇴 %n[7]마이페이지 [0]로그아웃%n");
			return 7;
		}
	}

	private void adminRun() {
		// 관리자
		while (true) {
			BankName();
			AdminMenu(); 
			int sel = utils.getValue("메뉴 선택", 0, 5);
			if (sel == 0) {
				return;
			} else if (sel == 1) {
			
				clientDAO.ClientList();
			} else if (sel == 2) {
			
				clientDAO.updateClient();
			} else if (sel == 3) {
			
				clientDAO.deleteClient(accDAO);
			} else if (sel == 4) {
				
				clientDAO.ClientData();
				accDAO.AccountData();
			} else if (sel == 5) {
				
				utils.laodFromFile(accDAO, clientDAO);
			}
		}
	}

	private void logout() {
		while (true) {
			BankName();
			int end = UserMenu();
			int sel = utils.getValue("사용자 메뉴 선택", 0, end);
			if (sel == 0) {
				return;
			}
			if (log == -1) {
				if (sel == 1) {
					
					clientDAO.addClient();
				} else if (sel == 2) {
					
					log = clientDAO.login();
					if (log == -1) {
						utils.printMsg(" 로그인 먼저 해주세요 ");
					}
					utils.printMsg("로그인 성공 ");
					login();
					return;
				}
			}
		}
	}

	private void login() {
		while (true) {
			BankName();
			int end = UserMenu();
			int sel = utils.getValue("사용자 메뉴 선택", 0, end);
			if (sel == 1) {
				
				accDAO.addAccount(clientDAO.getClientNum(log), clientDAO);
			} else if (sel == 2) {
				
				accDAO.deleteAccount(clientDAO.getClientNum(log));
			} else if (sel == 3) {
				
				accDAO.deposit(clientDAO.getClientNum(log));
			} else if (sel == 4) {
				
				accDAO.withdraw(clientDAO.getClientNum(log));
			} else if (sel == 5) {
				
				accDAO.transfer(clientDAO.getClientNum(log));
			} else if (sel == 6) {
				
				log = clientDAO.deleteClient(accDAO, log);
			} else if (sel == 7) {
				
				clientDAO.myPage(accDAO, log);
			} else {
				log = -1;
				utils.printMsg("로그아웃 완료 ");
				return;
			}
		}
	}

	public void run() { 
		utils.laodFromFile(accDAO, clientDAO);
		while (true) {
			BankName();
			MainMenu(); 
			
			int sel = utils.getValue("메뉴 선택", 0, 2); // 메뉴 사용자 입력
			if (sel == 0) {
				utils.printMsg("시스템 종료");
				return;
			} else if (sel == 1) { 
				adminRun();
			} else if (sel == 2 && log == -1) {
				logout();
			} else if (sel == 2 && log != -1) {
				login();

			}
		}

	}

}