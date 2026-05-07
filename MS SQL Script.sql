
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Teachers](
	[teacher_id] [int] NOT NULL,
	[teacher_name] [nvarchar](100) NULL,
	[teacher_email] [nvarchar](100) NULL,
	[teacher_phone] [nvarchar](20) NULL,
	[is_delete] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[teacher_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[courses](
	[course_id] [int] NOT NULL,
	[course_code] [varchar](50) NULL,
	[course_dow] [varchar](20) NULL,
	[course_duration] [int] NULL,
	[course_capacity] [int] NULL,
	[course_type_of_class] [varchar](50) NULL,
	[course_difficulty] [varchar](20) NULL,
	[course_time_of_course] [varchar](10) NULL,
	[course_description] [nvarchar](max) NULL,
	[course_need_equipment] [nvarchar](max) NULL,
	[course_price_per_class] [decimal](10, 2) NULL,
	[is_delete] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[course_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

CREATE TABLE [dbo].[classes](
	[class_id] [int] NOT NULL,
	[class_name] [nvarchar](255) NULL,
	[teacher_id] [int] NOT NULL,
	[course_id] [int] NOT NULL,
	[date_of_class] [nvarchar](50) NOT NULL,
	[comments] [nvarchar](max) NULL,
	[is_delete] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[class_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

CREATE TABLE [dbo].[Customers](
	[cid] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](500) NULL,
	[email] [nvarchar](100) NULL,
	[password] [nvarchar](100) NULL,
PRIMARY KEY CLUSTERED 
(
	[cid] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[RegisterClass](
	[customer_id] [int] NULL,
	[class_id] [int] NULL
) ON [PRIMARY]
GO

CREATE View [dbo].[classRegister] as
	select c.class_id,  isnull(count(rc.class_id),0) as register_count
	From classes c
	left join RegisterClass rc on rc.class_id = c.class_id
	Where c.is_delete <> 1
	group by c.class_id
GO

CREATE Procedure [dbo].[UpDateData] as
begin
	Delete From classes
	Where teacher_id not in (Select teacher_id From Teachers) OR
	course_id not in (Select course_id From courses)

	Delete From RegisterClass
	Where class_id not in (Select class_id From classes)

end;
GO

CREATE Procedure [dbo].[classesResetData] as
begin
	Delete From classes
	exec UpDateData;
	SELECT '{"response": "Class table is reset."}'
end;
GO

CREATE PROCEDURE [dbo].[classesUploadData] 
	@para nvarchar(max)
AS
BEGIN
		
	Delete From classes Where class_id = (Select CAST(JSON_VALUE(value, '$.class_id') AS INT) AS class_id FROM OPENJSON(@para));

	INSERT INTO Classes (
		class_id,
		class_name, 
        teacher_id, 
        course_id, 
        date_of_class, 
        comments,
		is_delete)
	SELECT
		CAST(JSON_VALUE(value, '$.class_id') AS INT) AS class_id,
		JSON_VALUE(value, '$.class_name') AS class_name,
        CAST(JSON_VALUE(value, '$.teacher_id') AS INT) AS teacher_id,
        CAST(JSON_VALUE(value, '$.course_id') AS INT) AS course_id,
		JSON_VALUE(value, '$.date_of_class') AS date_of_class,
        JSON_VALUE(value, '$.comments') AS comments,
		JSON_VALUE(value, '$.class_is_delete') AS is_delete
	FROM OPENJSON(@para);

	SELECT '{"response": "Class table is updated."}'
END
GO

CREATE PROCEDURE [dbo].[classRegistration] 
	@para varchar(max)
as
begin
	DECLARE @customerId int, @classId int, @isRegister int

	SET @customerId = JSON_VALUE(@para, '$.customerId');
	SET @classId = JSON_VALUE(@para, '$.classId');
	SET @isRegister = JSON_VALUE(@para, '$.isRegister');

	if(@isRegister=0)
	begin
		Delete From RegisterClass Where class_id = @classId and customer_id = @customerId 
	end
	else
	begin
		Insert Into RegisterClass(customer_id, class_id) values(@customerId, @classId)
	end

	Select @customerId

end
Go

CREATE Procedure [dbo].[coursesResetData] as
begin
	Delete From courses
	exec UpDateData;
	SELECT '{"response": "Course table is reset."}'
end;
GO

CREATE PROCEDURE [dbo].[coursesUploadData] 
	@para nvarchar(max)
AS
BEGIN
	
	Delete From courses Where course_id = (SELECT
		JSON_VALUE(value, '$.course_id') AS course_id FROM OPENJSON(@para))

	INSERT INTO Courses (course_id,
    course_code,
    course_dow,
    course_duration,
    course_capacity,
    course_type_of_class,
    course_difficulty,
    course_time_of_course,
    course_description,
    course_need_equipment,
    course_price_per_class,
	is_delete)
	SELECT
		JSON_VALUE(value, '$.course_id') AS course_id,
		JSON_VALUE(value, '$.course_code') AS course_code,
		JSON_VALUE(value, '$.course_dow') AS course_dow,
		CAST(JSON_VALUE(value, '$.course_duration') AS INT) AS course_duration,
		CAST(JSON_VALUE(value, '$.course_capacity') AS INT) AS course_capacity,
		JSON_VALUE(value, '$.course_type_of_class') AS course_type_of_class,
		JSON_VALUE(value, '$.course_difficulty') AS course_difficulty,
		JSON_VALUE(value, '$.course_time_of_course') AS course_time_of_course,
		JSON_VALUE(value, '$.course_description') AS course_description,
		JSON_VALUE(value, '$.course_need_equipment') AS course_need_equipment,
		CAST(JSON_VALUE(value, '$.course_price_per_class') AS DECIMAL(10, 2)) AS course_price_per_class,
		CAST(JSON_VALUE(value, '$.course_is_delete') AS int) AS is_delete
	FROM OPENJSON(@para);

	SELECT '{"response": "Course table is updated."}'
END
GO

CREATE PROCEDURE [dbo].[teachersUploadData] 
	@para nvarchar(max)
AS
BEGIN
	Delete From Teachers where teacher_id = (SELECT
		JSON_VALUE(value, '$.teacher_id') AS teacher_id FROM OPENJSON(@para));

	INSERT INTO Teachers (teacher_id, teacher_name, teacher_email, teacher_phone, is_delete)
	SELECT
		JSON_VALUE(value, '$.teacher_id') AS teacher_id,
		JSON_VALUE(value, '$.teacher_name') AS teacher_name,
		JSON_VALUE(value, '$.teacher_email') AS teacher_email,
		JSON_VALUE(value, '$.teacher_phone') AS teacher_phone,
		JSON_VALUE(value, '$.teacher_is_delete') AS is_delete
	FROM OPENJSON(@para);

	SELECT '{"response": "Teacher table is updated."}'
END
GO

CREATE Procedure [dbo].[teachersResetData] as
begin
	Delete From Teachers
	exec UpDateData;
	SELECT '{"response": "Teacher table is reset."}'
end;
GO

CREATE PROCEDURE [dbo].[signupCustomer] 
	@para varchar(max)
as
begin
	DECLARE  @name nvarchar(500), @email nvarchar(100), @password nvarchar(100)
	
	DECLARE @flag int

	SET @name = JSON_VALUE(@para, '$.name');
	SET @email = JSON_VALUE(@para, '$.email');
	SET @password = JSON_VALUE(@para, '$.password');
	
	IF (Select count(*) From Customers Where email = @email) > 0
		set @flag = 0
	ELSE
	BEGIN
		INSERT INTO Customers (name, email, password) Values (@name, @email, @password)
		Select @flag = cid From Customers Where email = @email and password = @password
	END

	Select @flag
end
GO

CREATE PROCEDURE [dbo].[loginCustomer] 
	@para varchar(max)
as
begin
	DECLARE @email varchar(100), @password nvarchar(100)
	SET @email = JSON_VALUE(@para, '$.email');
	SET @password = JSON_VALUE(@para, '$.password');
	

	Select isnull((Select cid From Customers Where email = @email and password = @password), 0)
end
GO

CREATE Procedure [dbo].[getRegisteredClassList]
	@para varchar(max)
AS
BEGIN
	DECLARE @customerId varchar(10)
	SET @customerId = JSON_VALUE(@para, '$.customerId');

	SELECT cast(c.class_id as varchar) class_id, c.class_name, cou.course_code, cou.course_type_of_class, 
		isnull(t.teacher_name, '') teacher_name, c.date_of_class, cou.course_time_of_course, cou.course_dow, 
		CASE WHEN (cou.course_capacity - COALESCE(cr.register_count, 0)) = 0 THEN '1'
        ELSE '0' END is_full
	Into #tmp
	FROM classes c
	left join classRegister cr on cr.class_id = c.class_id
	join courses cou on c.course_id = cou.course_id
	join Teachers t on c.teacher_id = t.teacher_id
	Where t.is_delete <> 1 and cou.is_delete <> 1 and c.is_delete <> 1

	Select t.*, case when rc.customer_id = @customerId then '1' else '0' end is_register
	From #tmp t
	left join RegisterClass rc on rc.class_id = t.class_id
	Where rc.customer_id = @customerId
	group by t.class_id, t.class_name, t.course_code, t.course_type_of_class, t.teacher_name, t.date_of_class,
	t.course_time_of_course, t.is_full, rc.customer_id, t.course_dow
	order by t.date_of_class
	FOR JSON PATH

END;
GO

CREATE Procedure [dbo].[getConnection] as
begin
	SELECT '{"response": "API connected."}'
end;
GO

CREATE Procedure [dbo].[getClassList]
	@para varchar(max)
AS
BEGIN
	DECLARE @customerId varchar(10)
	SET @customerId = JSON_VALUE(@para, '$.customerId');

	SELECT cast(c.class_id as varchar) class_id, c.class_name, cou.course_code, cou.course_type_of_class, 
		isnull(t.teacher_name, '') teacher_name, c.date_of_class, cou.course_time_of_course, cou.course_dow, 
		CASE WHEN (cou.course_capacity - COALESCE(cr.register_count, 0)) = 0 THEN '1'
        ELSE '0' END is_full
	Into #tmp
	FROM classes c
	left join classRegister cr on cr.class_id = c.class_id
	join courses cou on c.course_id = cou.course_id
	join Teachers t on c.teacher_id = t.teacher_id
	Where t.is_delete <> 1 and cou.is_delete <> 1 and c.is_delete <> 1

	Select t.*, case when rc.customer_id = @customerId then '1' else '0' end is_register
	From #tmp t
	left join RegisterClass rc on rc.class_id = t.class_id
	group by t.class_id, t.class_name, t.course_code, t.course_type_of_class, t.teacher_name, t.date_of_class,
	t.course_time_of_course, t.is_full, rc.customer_id, t.course_dow
	order by t.date_of_class
	FOR JSON PATH
END;
GO

CREATE Procedure [dbo].[getClassDetail]
	@para varchar(max)
AS
BEGIN
	
	DECLARE @customerId int, @classId int

	SET @customerId = JSON_VALUE(@para, '$.customerId');
	SET @classId = JSON_VALUE(@para, '$.classId');

		SELECT cast(c.class_id as varchar) class_id, c.class_name, cou.course_code, cou.course_type_of_class,
		cou.course_difficulty,
		isnull(t.teacher_name, '') teacher_name, c.date_of_class, cou.course_time_of_course, cou.course_dow,
		cast(cou.course_price_per_class as varchar) course_price_per_class, cast(cou.course_duration as varchar) course_duration, 
		cast(cou.course_capacity - COALESCE(cr.register_count, 0) as varchar) slots,
		isnull(cou.course_need_equipment, '') course_need_equipment,
		isnull('Need Equipment: ' + cou.course_need_equipment+c.comments, '') comments,
		case when rc.customer_id = @customerId then '1' else '0' end is_register
	FROM classes c
	left join classRegister cr on cr.class_id = c.class_id
	left join RegisterClass rc on rc.class_id = c.class_id
	left join courses cou on c.course_id = cou.course_id
	left join Teachers t on c.teacher_id = t.teacher_id
	Where c.class_id = @classId
	and t.is_delete <> 1 and cou.is_delete <> 1 and c.is_delete <> 1

	FOR JSON PATH
END;
GO

