-- Create table
create table TBL_SPRING_DEMO
(
  sp_id         NUMBER(20) not null,
  sp_name       VARCHAR2(100),
  sp_age        NUMBER(3),
  sp_createdate DATE,
  sp_modifydate DATE
);

-- Add comments to the table 
comment on table TBL_SPRING_DEMO
  is 'DEMO练习表';
-- Add comments to the columns 
comment on column TBL_SPRING_DEMO.sp_id
  is '主键，自增序列';
comment on column TBL_SPRING_DEMO.sp_name
  is '名称';
comment on column TBL_SPRING_DEMO.sp_age
  is '年龄';
comment on column TBL_SPRING_DEMO.sp_createdate
  is '创建日期';
comment on column TBL_SPRING_DEMO.sp_modifydate
  is '修改日期';
-- Create/Recreate primary, unique and foreign key constraints 
alter table TBL_SPRING_DEMO
  add constraint PK_ID primary key (SP_ID);
  
  
  -- Create sequence 
create sequence SEQ_SPRING_DEMO
minvalue 1
maxvalue 9999999999999999999999999999
start with 0
increment by 1
cache 20;
