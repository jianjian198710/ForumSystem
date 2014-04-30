create table t_user(user_id int(11) primary key, 
					user_name varchar(30), 
					password varchar(30), 
					user_type tinyint(4),
					locked tinyint(4),
					credit int(11), 
					last_visit datetime, 
					last_ip varchar(20));
					
create table t_board(board_id int(11) primary key, 
					 board_name varchar(150), 
					 board_desc varchar(255), 
					 topic_num int(11));

create table t_topic(topic_id int(11) primary key,
					 board_id int(11), 
					 topic_title varchar(100), 
				     user_id int(11), 
				     create_time date, 
				     last_post date, 
				     topic_views int(11), 
				     topic_replies int(11), 
				     digest int(11),
				     FOREIGN KEY (board_id) REFERENCES t_board(board_id),
				     FOREIGN KEY (user_id) REFERENCES t_user(user_id));

create table t_post(post_id int(11) primary key, 
					board_id int(11), 
					topic_id int(11), 
					user_id int(11), 
					post_type tinyint(4), 
					post_title varchar(50), 
					post_text text, 
					create_time date,
					FOREIGN KEY (topic_id) REFERENCES t_topic(topic_id),
					FOREIGN KEY (user_id) REFERENCES t_user(user_id));
					
create table t_login_log(login_log_id int(11) primary key, 
				   user_id int(11), 
				   ip varchar(30), 
				   login_datetime varchar(14),
				   FOREIGN KEY (user_id) REFERENCES t_user(user_id));

create table t_board_manager(board_id int(11), 
					   user_id int(11), 
					   primary key(board_id,user_id),
					   FOREIGN KEY (board_id) REFERENCES t_board(board_id),
					   FOREIGN KEY (user_id) REFERENCES t_user(user_id));
					 
					