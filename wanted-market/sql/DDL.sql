/* 회원 테이블 설계 */
CREATE TABLE members(
    member_id VARCHAR2(100) PRIMARY KEY
    , member_pwd VARCHAR2(100) NOT NULL
    , roles VARCHAR2(255) DEFAULT 'ROLE_USER'
);
DROP TABLE members;

/* 상품 테이블 설계 */
DROP TABLE products;
CREATE TABLE products (
                          product_id NUMBER PRIMARY KEY
    , member_id NUMBER
    , product_name VARCHAR2(2000) NOT NULL
    , product_price NUMBER
    , reserve_state VARCHAR2(50) CONSTRAINT reserve_state_ck CHECK(reserve_state in('판매중', '예약중', '완료'))
    , CONSTRAINT fk_product_seller_id FOREIGN KEY (member_id) REFERENCES members(member_id)
);

/* 거래 테이블 설계 */
DROP TABLE orders;
CREATE TABLE orders (
    order_id NUMBER PRIMARY KEY,
    buyer_id NUMBER,
    product_id NUMBER,
    order_state VARCHAR2(50) CONSTRAINT order_state_ck CHECK(order_state IN ('판매승인전', '판매승인완료')),
    order_date DATE,
    CONSTRAINT fk_buyer_id FOREIGN KEY (buyer_id) REFERENCES members(member_id),
    CONSTRAINT fk_product_id FOREIGN KEY (product_id) REFERENCES products(product_id)
);
