package madang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MainApp {
  static Connection conn = DBConn.makeConnection();

  public static void main(String[] args) throws SQLException {
    Scanner sc = new Scanner(System.in);
    int input = 0;

    if (conn != null) {
      while (true) {
        System.out.println("|------------------------------------------|");
        System.out.println("|------- 도서 마당 ------- (-1 입력시 종료)---|");
        System.out.println("|------------------------------------------|");
        System.out.println("|1. 도서 정보 | 2. 고객 리스트 | 3. 주문 리스트 |");
        System.out.println("|------------------------------------------|");
        System.out.println("|----열람 하고 싶은 정보의 번호를 입력하세요.----|");
        System.out.println("|------------------------------------------|");
        System.out.print("입력 : ");
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
    conn.close();
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

    conn.close();
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

    conn.close();
    pstmt.close();
  }
}
