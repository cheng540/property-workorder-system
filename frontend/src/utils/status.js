// 工单状态与紧急程度的显示文案，几个页面共用
export const statusMap = {
  0: { text: '待派单', type: 'warning' },
  1: { text: '处理中', type: 'primary' },
  2: { text: '已完成', type: 'success' },
  3: { text: '已评价', type: 'info' }
}

export const statusText = status => (statusMap[status] ? statusMap[status].text : '未知')

export const statusType = status => (statusMap[status] ? statusMap[status].type : 'info')

export const urgencyText = urgency => (urgency === 2 ? '紧急' : '一般')
