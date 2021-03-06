package BitOrderSystem;

import java.util.ArrayList;
import java.util.Scanner;

// 가게 클래스
public class Owner {
  OrderDb db = null;
  Scanner sc = null;
  ArrayList<String> menuNames;
  Integer[] orderNumbers;
  final String[] orderStatus= { "접수중","접수 완료","접수 취소" };

  //Owner 생성자에서 BitOrderSystem 클래스의 db, sc 를 가지고 온다.
  Owner() {
    this.db = BitOrderSystem.db;
    this.sc = BitOrderSystem.sc;

  } // end Constructor Owner

  // 가게 메뉴를 보여주는 메소드
  public void process() {
    while(true) {
      try {
        System.out.println("[가게 메뉴]");
        System.out.print("[ 1. 메뉴 등록  2. 메뉴 수정  3.주문정보확인 4. 주문처리 5. 공지 9. 로그아웃 ] >> ");
        int menuNum=Integer.parseInt(sc.nextLine());

        switch (menuNum) {
          case 1: enrollMenu(); break;
          case 2: modifyMenu(); break;
          case 3: OrderDetail(); break;
          case 4: processOrder(); break;
          case 5: notice(); break;
          case 9: 
            System.out.println("가게 로그아웃\n");
            LoginSession.logout(); 
            return;
          default :
            System.out.println("잘못된 번호를 입력하였습니다.");
            break;

        } // end switch 

      } catch(NumberFormatException nfe) {
        System.out.println("숫자를 입력해주세요.");

      } catch (Exception e) {
        System.out.println("알수없는 에러..");
        e.printStackTrace();

      } // end try / catch

    } // end while

  } // end Method process

  public void enrollMenu() {
    try {
      menuNames= db.showMenuNames(LoginSession.mmsq, false);
      db.showMenu(); // 가게 주인이 자기만의 메뉴를 조회하는 메서드
      System.out.println();
      System.out.print("음식 이름은 무엇입니까 ? >> ");
      String foodName=sc.nextLine();

      for (String s:menuNames) {
        if (s.equals(foodName)) {
          System.out.println("이미 등록한 메뉴입니다.");
          return;

        } // end if

      } // end for

      System.out.print("가격은 얼마죠?>>>");
      int foodPrice=Integer.parseInt(sc.nextLine());

      System.out.println("정말 등록하시겠습니까?(y/n)");
      String answer=sc.nextLine();

      if (answer.equals("n")) {
        return;

      } // end if

      db.registerMenu(foodName,foodPrice); // 가게 메뉴를 등록하는 메서드
      System.out.println();

    } catch(NumberFormatException nfe) {
      System.out.println("숫자만 입력해 주세요.");
    } catch (Exception e) {
      System.out.println("형식에 맞지 않네요~");

    } // end try / catch

  } // end Method enrollMenu

  public void modifyMenu() {
    try {
      menuNames = db.showMenuNames(LoginSession.mmsq, false);
      if (menuNames.isEmpty()) {
        System.out.println("등록한 음식이 없네요~");

      } // end if

      System.out.print("\n[메뉴리스트]");
      for (int i=0;i<menuNames.size();i++) {
        // 메뉴 리스트가 5개가 넘어갈시 한칸씩 아래로..
        if((i%5) == 0) {
          System.out.println();

        } // end if

        System.out.print((i+1)+"."+menuNames.get(i)+"\t");

      } // end for

      System.out.print("\n\n바꾸고자 하는 음식의 번호를 입력해 주세요. (숫자) >> ");

      int foodNum=Integer.parseInt(sc.nextLine());
      String prevFoodName=menuNames.get(foodNum-1).trim();

      System.out.print("이름을 어떻게 바꾸시겠습니까?>>");
      String foodName=sc.nextLine();

      System.out.print("변경하고자 하는 음식의 가격을 적어주세요. >> ");
      int foodPrice=Integer.parseInt(sc.nextLine());

      db.alterMenu(prevFoodName,foodName, foodPrice);
    } catch (ArrayIndexOutOfBoundsException ae) {
      System.out.println("번호를 제대로 입력해주세요.");

    } catch (Exception e) {
      System.out.println("형식에 맞지 않네요~");


    } // end try / catch

  } // end Method modifyMenu

  public void processOrder() { // 주문 내역의 주문상태를 처리하는 메서드~
    int count=0;
    orderNumbers=db.getOrderNum();

    try {
      db.showOrderManager(); // 가게 소유자 자신의 주문 내역을 조회
      System.out.print("주문번호를 입력하세요. >> ");
      int orderNum=Integer.parseInt(sc.nextLine());

      for (Integer i:orderNumbers) {
        if (orderNum==i) 
          count++;

      } // end for

      if (count==0) {
        System.out.println("존재하지 않는 주문번호입니다~");
        return;

      } // end if

      System.out.println();

      for (int i=0;i<orderStatus.length;i++) {
        System.out.print((i+1)+"."+orderStatus[i]+" ");

      } // end for

      System.out.println();
      System.out.print("주문상태를 골라주세요. >> ");
      int num=Integer.parseInt(sc.nextLine());
      String orderMsg=orderStatus[num-1];

      db.handleOrder(orderNum,orderMsg); // 주문 내역의 주문상태를 처리하는 메서드~

    } catch(NumberFormatException nfe) {
      System.out.println("숫자만 입력해 주세요.");

    } catch (Exception e) {
      System.out.println("오류발생.");

    } // end try / catch

  } // end Method processOrder

  public void OrderDetail() { // 주문 내역의 주문상태를 처리하는 메서드~
    int count=0;
    orderNumbers = db.getOrderNum();

    try {
      db.showOrderManager(); // 가게 소유자 자신의 주문 내역을 조회
      System.out.print("주문번호를 입력하세요. >> ");
      int orderNum=Integer.parseInt(sc.nextLine());

      for (Integer i:orderNumbers) {
        if (orderNum==i) 
          count++; // end if

      } // end for

      if (count==0) {
        System.out.println("존재하지 않는 주문번호입니다~");
        return;

      } // end if

      db.showMyOrderDetail(orderNum);

    } catch(NumberFormatException nfe) {
      System.out.println("숫자만 입력해 주세요.");

    } catch (Exception e) {
      System.out.println("오류발생.");

    } // end try / catch

  } // end Method processOrder

  public void notice() { //db.store 테이블의 공지를 등록하는 메서드(notice)
    System.out.print("공지사항을 등록해주세요. >> ");

    try {
      String notice=sc.nextLine();
      db.registerNotice(notice);

    } catch (Exception e) {
      System.out.println("오류발생.");

    } // end try / catch

  } // end Method notice

} // end Class Owner
