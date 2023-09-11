package madang;

import java.sql.*;
import java.util.Scanner;

import static javax.swing.UIManager.getInt;

public class MainApp {
  static Connection conn = DBConn.makeConnection();

  public static void main(String[] args) throws SQLException {
    Scanner sc = new Scanner(System.in);
    int input = 0;

    if (conn != null) {
      while (true) {
        System.out.println("|-------------------------------------------------------|");
        System.out.println("|----------------------- 도서 마당 -----------------------|");
        System.out.println("|-------------------------------------------------------|");
        System.out.println("| 1. 도서 정보 확인  | 2. 고객 정보 확인  | 3. 주문 정보 확인  |");
        System.out.println("| 4. 도서 정보 입력  | 5. 고객 정보 입력  | 6. 주문 정보 입력  |");
        System.out.println("| 7. 도서 정보 수정  | 8. 고객 정보 수정  | 9. 주문 정보 수정  |");
        System.out.println("|10. 도서 정보 삭제  |11. 고객 정보 삭제  |12. 주문 정보 삭제  |");
        System.out.println("|-------------------------------------------------------|");
        System.out.println("|----원하는 메뉴의 번호를 입력하세요.---- (-1 입력시 종료) ----|");
        System.out.println("|-------------------------------------------------------|");
        System.out.print("번호 입력 : ");
        input = sc.nextInt();

        if (input < 0) {
          System.out.println("프로그램 종료.");
          break;
        }

        switch (input) {
          case 1:
            bookList();
            break;
          case 2:
            customerList();
            break;
          case 3:
            orderList();
            break;
          case 4:
            addBook();
            break;
          case 5:
            addCustomer();
            break;
          case 6:
            addOrder();
            break;
          case 7:
            changeBook();
            break;
          case 8:
            changeCustomer();
            break;
          case 9:
            changeOrder();
            break;
          case 10:
            removeBook();
            break;
          case 11:
            removeCustomer();
            break;
          case 12:
            removeOrder();
            break;
        }
      }
    }
  }

  static void bookList() throws SQLException {
    String sql = "select * from book;";

    PreparedStatement pstmt = conn.prepareStatement(sql);

    ResultSet rs = pstmt.executeQuery();

    System.out.println("|-----------------도서 정보-----------------|");
    System.out.println("|bookid|---bookname--|--publisher--|-price-|");
    System.out.println("|------------------------------------------|");
    while (rs.next()) {
      System.out.println("|\t" + rs.getString(1) + "\t|\t"
          +rs.getString(2) + "\t|\t"
          +rs.getString(3) + "\t|\t"
          +rs.getString(4) + "\t|");
    }

    System.out.println("|------------------------------------------|");
    System.out.println();
//    conn.close();
    pstmt.close();
  }

  static void customerList() throws SQLException {
    String sql = "select * from customer;";

    PreparedStatement pstmt = conn.prepareStatement(sql);

    ResultSet rs = pstmt.executeQuery();

    System.out.println("|-----------------고객 리스트----------------|");
    System.out.println("|custid|---name--|---address---|---phone---|");
    System.out.println("|------------------------------------------|");

    while (rs.next()) {
      System.out.println("|\t" + rs.getString(1) + "\t|\t"
          +rs.getString(2) + "\t|\t"
          +rs.getString(3) + "\t|\t"
          +rs.getString(4) + "\t|");
    }
    System.out.println("|------------------------------------------|");
    System.out.println();

//    conn.close();
    pstmt.close();
  }

  static void orderList() throws SQLException {
    String sql = "select orderid, name, bookname, saleprice, orderdate " +
        "from orders inner join customer on orders.custid = customer.custid " +
        "inner join book on orders.bookid = book.bookid;";

    PreparedStatement pstmt = conn.prepareStatement(sql);

    ResultSet rs = pstmt.executeQuery();

    System.out.println("|---------------------------주문 리스트--------------------------|");
    System.out.println("|orderid|---name--|---bookname---|---saleprice---|--orderdate--|");
    System.out.println("|--------------------------------------------------------------|");

    while (rs.next()) {
      System.out.println("|\t" + rs.getString(1) + "\t|\t"
          +rs.getString(2) + "\t|\t"
          +rs.getString(3) + "\t|\t"
          +rs.getString(4) + "\t|\t"
          +rs.getString(5) + "\t|");
    }
    System.out.println("|-------------------------------------------------------------|");
    System.out.println();

//    conn.close();
    pstmt.close();
  }

  static void addBook() throws SQLException {
    Scanner sc = new Scanner(System.in);
    String bookInfo[] = new String[3];
    System.out.println("|---------------도서 정보 입력---------------|");
    System.out.print("|1. 도서 이름 : ");
    bookInfo[0] = sc.nextLine();
    System.out.print("|2. 출 판 사 : ");
    bookInfo[1] = sc.nextLine();
    System.out.print("|3. 도서 가격 : ");
    bookInfo[2] = sc.nextLine();
    System.out.println("|------------------------------------------|");

    String sql = "select max(bookid) from book;";
    PreparedStatement pstmt = conn.prepareStatement(sql);
    ResultSet rs = pstmt.executeQuery();
    rs.next();
    int maxBookId = rs.getInt(1);

    sql = "insert into book values(?, ?, ?, ?);";
    pstmt = conn.prepareStatement(sql);
    pstmt.setInt(1, maxBookId + 1);
    pstmt.setString(2, bookInfo[0]);
    pstmt.setString(3, bookInfo[1]);
    pstmt.setInt(4, Integer.parseInt(bookInfo[2]));

    int i = pstmt.executeUpdate();
    if (i == 1) {
      System.out.println("데이터 추가 성공");
    } else {
      System.out.println("데이터 추가 실패");
    }

    System.out.println();
//    conn.close();
    pstmt.close();
  }

  static void addCustomer() throws SQLException {
    Scanner sc = new Scanner(System.in);
    String custInfo[] = new String[3];
    System.out.println("|---------------회원 정보 입력---------------|");
    System.out.print("|1. 회원 이름 : ");
    custInfo[0] = sc.nextLine();
    System.out.print("|2. 주    소 : ");
    custInfo[1] = sc.nextLine();
    System.out.print("|3. 전화 번호 : ");
    custInfo[2] = sc.nextLine();
    System.out.println("|------------------------------------------|");

    String sql = "select max(custid) from customer;";
    PreparedStatement pstmt = conn.prepareStatement(sql);
    ResultSet rs = pstmt.executeQuery();
    rs.next();
    int maxCustId = rs.getInt(1);

    sql = "insert into customer values(?, ?, ?, ?);";
    pstmt = conn.prepareStatement(sql);
    pstmt.setInt(1, maxCustId + 1);
    pstmt.setString(2, custInfo[0]);
    pstmt.setString(3, custInfo[1]);
    pstmt.setString(4, custInfo[2]);

    int i = pstmt.executeUpdate();
    if (i == 1) {
      System.out.println("데이터 추가 성공");
    } else {
      System.out.println("데이터 추가 실패");
    }

    System.out.println();
//    conn.close();
    pstmt.close();
  }

  static void addOrder() throws SQLException {
    Scanner sc = new Scanner(System.in);
    String orderInfo[] = new String[4];
    System.out.println("|---------------주문 정보 입력---------------|");
    System.out.print("|1. 고객  id : ");
    orderInfo[0] = sc.nextLine();
    System.out.print("|2. 도서  id : ");
    orderInfo[1] = sc.nextLine();
    System.out.print("|3. 판매 가격 : ");
    orderInfo[2] = sc.nextLine();
    System.out.print("|4. 판매 일자 : ");
    orderInfo[3] = sc.nextLine();
    System.out.println("|------------------------------------------|");

    String sql = "select max(orderid) from orders";
    PreparedStatement pstmt = conn.prepareStatement(sql);
    ResultSet rs = pstmt.executeQuery();
    rs.next();
    int maxOrderId = rs.getInt(1);

    sql = "insert into orders (orderid, custid, bookid, saleprice, orderdate)" +
        " values (?, ?, ?, ?, ?)";
    pstmt = conn.prepareStatement(sql);
    pstmt.setInt(1, maxOrderId + 1);
    pstmt.setInt(2, Integer.parseInt(orderInfo[0]));
    pstmt.setInt(3, Integer.parseInt(orderInfo[1]));
    pstmt.setInt(4, Integer.parseInt(orderInfo[2]));
    pstmt.setDate(5, Date.valueOf(orderInfo[3]));

    int i = pstmt.executeUpdate();
    if (i == 1) {
      System.out.println("데이터 추가 성공");
    } else {
      System.out.println("데이터 추가 실패");
    }

    System.out.println();
//    conn.close();
    pstmt.close();
  }

  static void changeBook() throws SQLException {
    Scanner sc = new Scanner(System.in);
    // 수정하고 싶은 책id 입력 받아서 내용 확인하기
    System.out.print("수정할 책의 id를 입력하세요 : ");
    int bookid = sc.nextInt();
    String sql = "select bookname, publisher, price from book where bookid = ?;";
    PreparedStatement pstmt = conn.prepareStatement(sql);
    pstmt.setInt(1, bookid);
    ResultSet rs = pstmt.executeQuery();

    if (rs != null & rs.next()) {
      System.out.println(rs.getString(1) + ", " +
          rs.getString(2) + ", " + rs.getInt(3));
    }

    // 수정할 도서 정보 입력 받아서 db에 업데이트 하기
//    System.out.print("책의 제목을 수정하려면 입력하세요 : ");
//    String bookName = sc.next();
//    System.out.print("책의 출판사를 수정하려면 입력하세요 : ");
//    String publisher = sc.next();
    System.out.print("책의 가격을 수정하려면 입력하세요 (수정하지 않으려면 !) : ");
    String price = sc.next();
    if (!price.equals("!")) {
      sql = "update book set price = ? where bookid = ?;";
      pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, Integer.parseInt(price));
      pstmt.setInt(2, bookid);
      int i = pstmt.executeUpdate();
      if (i == 1) {
        System.out.println("데이터 수정 성공");
      } else {
        System.out.println("데이터 수정 실패");
      }
    }
    System.out.println();
//    conn.close();
    pstmt.close();
  }

  static void changeCustomer() {

  }

  static void changeOrder() {

  }

  static void removeBook() {

  }

  static void removeCustomer() throws SQLException {
    Scanner sc = new Scanner(System.in);
    // 삭제하고 싶은 고객id 입력 받아서 내용 확인하기
    System.out.print("삭제할 고객의 id를 입력하세요 : ");
    int custId = sc.nextInt();
    String sql = "select name, address, phone from customer where custid = ?;";
    PreparedStatement pstmt = conn.prepareStatement(sql);
    pstmt.setInt(1, custId);
    ResultSet rs = pstmt.executeQuery();

    System.out.println("---고객 정보---");
    if (rs != null & rs.next()) {
      System.out.println(rs.getString(1) + ", " +
          rs.getString(2) + ", " + rs.getString(3));
    }

    sql = "select name, bookname, saleprice, orderdate from vorders where custid = ?;";
    pstmt = conn.prepareStatement(sql);
    pstmt.setInt(1, custId);
    rs = pstmt.executeQuery();

    System.out.println("---주문 정보---");
    while (rs != null & rs.next()) {
      System.out.println(rs.getString(1) + ", " +
          rs.getString(2) + ", " + rs.getInt(3) + ", " +
          rs.getDate(4));
    }

    System.out.println("--------------");
    // 삭제 확인 후 db에서 삭제 하기
    System.out.println("위의 데이터가 전부 삭제 됩니다.");
    System.out.print("삭제하시겠습니까? (Y / N)");
    String confirm = sc.next();
    if (confirm.equalsIgnoreCase("y")) {
      sql = "delete from orders where custid = ?;";
      pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, custId);
      int i = pstmt.executeUpdate();
      if (i > 0) {
        System.out.println("주문 데이터 삭제 성공");
        sql = "delete from customer where custid = ?;";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, custId);
        i = pstmt.executeUpdate();
        if (i > 0) {
          System.out.println("고객 데이터 삭제 성공");
        } else {
          System.out.println("고객 데이터 삭제 실패");
        }
      } else {
        System.out.println("주문 데이터 삭제 실패");
      }
    }
    System.out.println();
//    conn.close();
    pstmt.close();
  }

  static void removeOrder() throws SQLException {
    Scanner sc = new Scanner(System.in);
    // 삭제하고 싶은 책id 입력 받아서 내용 확인하기
    System.out.print("삭제할 책의 id를 입력하세요 : ");
    int orderid = sc.nextInt();
    String sql = "select name, bookname, saleprice, orderdate from vorders where orderid = ?;";
    PreparedStatement pstmt = conn.prepareStatement(sql);
    pstmt.setInt(1, orderid);
    ResultSet rs = pstmt.executeQuery();

    if (rs != null & rs.next()) {
      System.out.println(rs.getString(1) + ", " +
          rs.getString(2) + ", " + rs.getInt(3) + ", " +
          rs.getDate(4));
    }

    // 삭제 확인 후 db에서 삭제 하기
    System.out.print("삭제하시겠습니까? (Y / N)");
    String confirm = sc.next();
    if (confirm.equalsIgnoreCase("y")) {
      sql = "delete from order where orderid = ?;";
      pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, orderid);
      int i = pstmt.executeUpdate();
      if (i == 1) {
        System.out.println("데이터 삭제 성공");
      } else {
        System.out.println("데이터 삭제 실패");
      }
    }
    System.out.println();
//    conn.close();
    pstmt.close();
  }
}
