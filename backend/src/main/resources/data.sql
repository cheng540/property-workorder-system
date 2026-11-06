MERGE INTO sys_user (id, username, password, real_name, role, phone, address, status) KEY(id) VALUES
(1, 'admin',      '$2a$10$DapdB5brVU1kYoK.pWPcteboyV.9dQ9Qm2yxk54I0IoT3zs8d4XxO', '张伟',   'ADMIN',    '13800138001', '物业服务中心', 1),
(2, 'resident01', '$2a$10$DapdB5brVU1kYoK.pWPcteboyV.9dQ9Qm2yxk54I0IoT3zs8d4XxO', '李静',   'RESIDENT', '13800138002', '3栋2单元501', 1),
(3, 'resident02', '$2a$10$DapdB5brVU1kYoK.pWPcteboyV.9dQ9Qm2yxk54I0IoT3zs8d4XxO', '王磊',   'RESIDENT', '13800138003', '5栋1单元302', 1),
(4, 'resident03', '$2a$10$DapdB5brVU1kYoK.pWPcteboyV.9dQ9Qm2yxk54I0IoT3zs8d4XxO', '赵敏',   'RESIDENT', '13800138004', '1栋3单元801', 1),
(5, 'resident04', '$2a$10$DapdB5brVU1kYoK.pWPcteboyV.9dQ9Qm2yxk54I0IoT3zs8d4XxO', '陈晨',   'RESIDENT', '13800138005', '2栋2单元1204', 0),
(6, 'worker01',   '$2a$10$DapdB5brVU1kYoK.pWPcteboyV.9dQ9Qm2yxk54I0IoT3zs8d4XxO', '周建国', 'WORKER',   '13800138101', '水电班组', 1),
(7, 'worker02',   '$2a$10$DapdB5brVU1kYoK.pWPcteboyV.9dQ9Qm2yxk54I0IoT3zs8d4XxO', '刘志强', 'WORKER',   '13800138102', '综合维修组', 1),
(8, 'worker03',   '$2a$10$DapdB5brVU1kYoK.pWPcteboyV.9dQ9Qm2yxk54I0IoT3zs8d4XxO', '孙海涛', 'WORKER',   '13800138103', '电梯维保组', 1);

MERGE INTO repair_type (id, name, remark) KEY(id) VALUES
(1, '水电维修', '上下水、电路、开关插座、热水器'),
(2, '门窗锁具', '门窗变形、合页松动、门锁芯损坏'),
(3, '电梯故障', '运行异响、按键失灵、门机故障'),
(4, '公共设施', '楼道照明、单元门禁、健身器材'),
(5, '卫生环境', '垃圾清运、公共区域保洁、地面积水'),
(6, '其他', '上述类型以外的报修事项');

MERGE INTO repair_order (id, order_no, resident_id, type_id, title, content, address, urgency, status, worker_id, handle_remark, rating, evaluate, create_time, assign_time, finish_time) KEY(id) VALUES
(1, 'BX20261019001', 2, 1, '厨房水管接口渗水', '水槽下方接口处一直滴水，地上已经积了一小滩，需要用盆接着。', '3栋2单元501', 2, 0, NULL, NULL, NULL, NULL, DATEADD('HOUR', -3, CURRENT_TIMESTAMP), NULL, NULL),
(2, 'BX20261018002', 3, 2, '入户门锁芯转动困难', '钥匙插进去要反复拧好几次才能开门，怀疑锁芯磨损。', '5栋1单元302', 1, 0, NULL, NULL, NULL, NULL, DATEADD('DAY', -1, CURRENT_TIMESTAMP), NULL, NULL),
(3, 'BX20261018001', 4, 3, '3栋电梯运行有异响', '电梯上行到五层附近有金属摩擦声，持续时间两三秒。', '1栋3单元801', 2, 0, NULL, NULL, NULL, NULL, DATEADD('DAY', -1, CURRENT_TIMESTAMP), NULL, NULL),
(4, 'BX20261016001', 2, 4, '楼道声控灯不亮', '四层到五层之间两盏声控灯都不亮，晚上上下楼看不见。', '3栋2单元4层', 1, 1, 6, NULL, NULL, NULL, DATEADD('DAY', -3, CURRENT_TIMESTAMP), DATEADD('DAY', -3, CURRENT_TIMESTAMP), NULL),
(5, 'BX20261015001', 3, 5, '地下车库地面油污', 'B区车位旁边的地面有一片油污，走路打滑，希望安排清理。', '地下车库B区', 1, 1, 7, NULL, NULL, NULL, DATEADD('DAY', -4, CURRENT_TIMESTAMP), DATEADD('DAY', -4, CURRENT_TIMESTAMP), NULL),
(6, 'BX20261014001', 4, 1, '阳台地漏返味', '下雨后阳台地漏一直返味，已经用清水冲过还是有味道。', '1栋3单元801', 1, 1, 8, NULL, NULL, NULL, DATEADD('DAY', -5, CURRENT_TIMESTAMP), DATEADD('DAY', -5, CURRENT_TIMESTAMP), NULL),
(7, 'BX20261012001', 2, 2, '卧室窗户合页松动', '窗户关不严，合页螺丝松动，风大的时候会响。', '3栋2单元501', 1, 2, 6, '已更换合页螺丝并调整窗框位置，窗户开合正常。', NULL, NULL, DATEADD('DAY', -7, CURRENT_TIMESTAMP), DATEADD('DAY', -7, CURRENT_TIMESTAMP), DATEADD('DAY', -6, CURRENT_TIMESTAMP)),
(8, 'BX20261010001', 3, 4, '单元门禁刷卡失灵', '单元门禁卡要刷很多次才开，邻居也有同样情况。', '5栋1单元', 2, 2, 7, '门禁读卡器进水导致接触不良，已更换读卡器并复测。', NULL, NULL, DATEADD('DAY', -9, CURRENT_TIMESTAMP), DATEADD('DAY', -9, CURRENT_TIMESTAMP), DATEADD('DAY', -8, CURRENT_TIMESTAMP)),
(9, 'BX20261008001', 4, 3, '电梯按键失灵', '3栋电梯的十二层按键按下去没有反应，指示灯不亮。', '1栋3单元电梯', 2, 2, 8, '按键板排线松动，重新插接后测试各层按键均正常。', NULL, NULL, DATEADD('DAY', -11, CURRENT_TIMESTAMP), DATEADD('DAY', -11, CURRENT_TIMESTAMP), DATEADD('DAY', -10, CURRENT_TIMESTAMP)),
(10, 'BX20261005001', 2, 1, '热水器不打火', '燃气热水器打开水龙头后没有点火声，也没有热水。', '3栋2单元501', 2, 3, 6, '点火针积碳，已清理并更换电池，点火恢复正常。', 5, '师傅当天下午就上门了，处理得很快，态度也好。', DATEADD('DAY', -14, CURRENT_TIMESTAMP), DATEADD('DAY', -14, CURRENT_TIMESTAMP), DATEADD('DAY', -13, CURRENT_TIMESTAMP)),
(11, 'BX20261003001', 3, 5, '垃圾房清运不及时', '东门垃圾房两天没有清运，味道比较大。', '东门垃圾房', 1, 3, 7, '已联系清运单位增加一次清运，并对地面进行冲洗。', 4, '处理结果可以，希望以后按时清运。', DATEADD('DAY', -16, CURRENT_TIMESTAMP), DATEADD('DAY', -16, CURRENT_TIMESTAMP), DATEADD('DAY', -15, CURRENT_TIMESTAMP)),
(12, 'BX20261001001', 4, 2, '阳台推拉门卡顿', '推拉门推到一半卡住了，滑轨里有杂物。', '1栋3单元801', 1, 3, 8, '清理滑轨杂物并加注润滑，推拉顺畅。', 5, '清理得很干净，问题解决了。', DATEADD('DAY', -18, CURRENT_TIMESTAMP), DATEADD('DAY', -18, CURRENT_TIMESTAMP), DATEADD('DAY', -17, CURRENT_TIMESTAMP));

MERGE INTO order_log (id, order_id, operator_id, operator_name, action, remark, create_time) KEY(id) VALUES
(1,  1,  2, '李静',   '提交报修', '住户提交报修单，等待物业派单', DATEADD('HOUR', -3, CURRENT_TIMESTAMP)),
(2,  2,  3, '王磊',   '提交报修', '住户提交报修单，等待物业派单', DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
(3,  3,  4, '赵敏',   '提交报修', '住户提交报修单，等待物业派单', DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
(4,  4,  2, '李静',   '提交报修', '住户提交报修单，等待物业派单', DATEADD('DAY', -3, CURRENT_TIMESTAMP)),
(5,  4,  1, '张伟',   '派单',     '指派给维修工 周建国', DATEADD('HOUR', -70, CURRENT_TIMESTAMP)),
(6,  5,  3, '王磊',   '提交报修', '住户提交报修单，等待物业派单', DATEADD('DAY', -4, CURRENT_TIMESTAMP)),
(7,  5,  1, '张伟',   '派单',     '指派给维修工 刘志强', DATEADD('DAY', -4, CURRENT_TIMESTAMP)),
(8,  6,  4, '赵敏',   '提交报修', '住户提交报修单，等待物业派单', DATEADD('DAY', -5, CURRENT_TIMESTAMP)),
(9,  6,  1, '张伟',   '派单',     '指派给维修工 孙海涛', DATEADD('DAY', -5, CURRENT_TIMESTAMP)),
(10, 7,  2, '李静',   '提交报修', '住户提交报修单，等待物业派单', DATEADD('DAY', -7, CURRENT_TIMESTAMP)),
(11, 7,  1, '张伟',   '派单',     '指派给维修工 周建国', DATEADD('DAY', -7, CURRENT_TIMESTAMP)),
(12, 7,  6, '周建国', '完工',     '已更换合页螺丝并调整窗框位置，窗户开合正常。', DATEADD('DAY', -6, CURRENT_TIMESTAMP)),
(13, 8,  3, '王磊',   '提交报修', '住户提交报修单，等待物业派单', DATEADD('DAY', -9, CURRENT_TIMESTAMP)),
(14, 8,  1, '张伟',   '派单',     '指派给维修工 刘志强', DATEADD('DAY', -9, CURRENT_TIMESTAMP)),
(15, 8,  7, '刘志强', '完工',     '门禁读卡器进水导致接触不良，已更换读卡器并复测。', DATEADD('DAY', -8, CURRENT_TIMESTAMP)),
(16, 9,  4, '赵敏',   '提交报修', '住户提交报修单，等待物业派单', DATEADD('DAY', -11, CURRENT_TIMESTAMP)),
(17, 9,  1, '张伟',   '派单',     '指派给维修工 孙海涛', DATEADD('DAY', -11, CURRENT_TIMESTAMP)),
(18, 9,  8, '孙海涛', '完工',     '按键板排线松动，重新插接后测试各层按键均正常。', DATEADD('DAY', -10, CURRENT_TIMESTAMP)),
(19, 10, 2, '李静',   '提交报修', '住户提交报修单，等待物业派单', DATEADD('DAY', -14, CURRENT_TIMESTAMP)),
(20, 10, 1, '张伟',   '派单',     '指派给维修工 周建国', DATEADD('DAY', -14, CURRENT_TIMESTAMP)),
(21, 10, 6, '周建国', '完工',     '点火针积碳，已清理并更换电池，点火恢复正常。', DATEADD('DAY', -13, CURRENT_TIMESTAMP)),
(22, 10, 2, '李静',   '评价',     '5 星评价：师傅当天下午就上门了，处理得很快，态度也好。', DATEADD('DAY', -13, CURRENT_TIMESTAMP)),
(23, 11, 3, '王磊',   '提交报修', '住户提交报修单，等待物业派单', DATEADD('DAY', -16, CURRENT_TIMESTAMP)),
(24, 11, 1, '张伟',   '派单',     '指派给维修工 刘志强', DATEADD('DAY', -16, CURRENT_TIMESTAMP)),
(25, 11, 7, '刘志强', '完工',     '已联系清运单位增加一次清运，并对地面进行冲洗。', DATEADD('DAY', -15, CURRENT_TIMESTAMP)),
(26, 11, 3, '王磊',   '评价',     '4 星评价：处理结果可以，希望以后按时清运。', DATEADD('DAY', -15, CURRENT_TIMESTAMP)),
(27, 12, 4, '赵敏',   '提交报修', '住户提交报修单，等待物业派单', DATEADD('DAY', -18, CURRENT_TIMESTAMP)),
(28, 12, 1, '张伟',   '派单',     '指派给维修工 孙海涛', DATEADD('DAY', -18, CURRENT_TIMESTAMP)),
(29, 12, 8, '孙海涛', '完工',     '清理滑轨杂物并加注润滑，推拉顺畅。', DATEADD('DAY', -17, CURRENT_TIMESTAMP)),
(30, 12, 4, '赵敏',   '评价',     '5 星评价：清理得很干净，问题解决了。', DATEADD('DAY', -17, CURRENT_TIMESTAMP));

MERGE INTO notice (id, title, content, create_time) KEY(id) VALUES
(1, '电梯年检停梯通知', '3栋两部电梯定于本月二十五日上午八点至十一点进行年度检验，期间暂停使用，请业主提前安排出行。', DATEADD('DAY', -4, CURRENT_TIMESTAMP)),
(2, '报修工单处理时效说明', '一般报修事项受理后二十四小时内派单，紧急事项两小时内响应。维修完成后住户可在系统内对本次服务进行评价。', DATEADD('DAY', -9, CURRENT_TIMESTAMP)),
(3, '地下车库照明改造施工', 'B区地库照明改造施工预计持续五天，施工期间部分车位临时封闭，请留意现场指示标识。', DATEADD('DAY', -13, CURRENT_TIMESTAMP));
