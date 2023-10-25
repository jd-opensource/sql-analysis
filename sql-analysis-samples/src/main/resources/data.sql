CREATE TABLE `task` (
                         `id` INT(11) NOT NULL AUTO_INCREMENT,
                         `title` VARCHAR(50) NOT NULL DEFAULT '' ,
                         `content` VARCHAR(50) NOT NULL DEFAULT '' ,
                         `create_user` VARCHAR(50) NOT NULL DEFAULT '' ,
                         `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         PRIMARY KEY (`id`) USING BTREE,
                         INDEX `idx_create_user` (`create_user`) USING BTREE,
                         INDEX `idx_update_time` (`update_time`) USING BTREE
)
    COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

INSERT INTO `task` ( `title`, `content`, `create_user`, `create_time`, `update_time`) VALUES ('任务1', '任务内容1', 'zhangsan1', '2023-10-11 20:10:43', '2023-10-11 20:10:43');
INSERT INTO `task` ( `title`, `content`, `create_user`, `create_time`, `update_time`) VALUES ('任务2', '任务内容2', 'zhangsan2', '2023-10-12 21:10:43', '2023-10-12 21:10:43');
INSERT INTO `task` ( `title`, `content`, `create_user`, `create_time`, `update_time`) VALUES ('任务3', '任务内容3', 'zhangsan3', '2023-10-13 22:10:43', '2023-10-13 22:10:43');
INSERT INTO `task` ( `title`, `content`, `create_user`, `create_time`, `update_time`) VALUES ('任务4', '任务内容4', 'zhangsan4', '2023-10-14 23:10:43', '2023-10-14 23:10:43');
INSERT INTO `task` ( `title`, `content`, `create_user`, `create_time`, `update_time`) VALUES ('任务5', '任务内容5', 'zhangsan5', '2023-10-15 20:10:43', '2023-10-15 20:10:43');
INSERT INTO `task` ( `title`, `content`, `create_user`, `create_time`, `update_time`) VALUES ('任务6', '任务内容6', 'zhangsan6', '2023-10-16 20:10:43', '2023-10-16 20:10:43');

